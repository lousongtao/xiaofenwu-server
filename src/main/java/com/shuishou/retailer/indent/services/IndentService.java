package com.shuishou.retailer.indent.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Hibernate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shuishou.retailer.ConstantValue;
import com.shuishou.retailer.DataCheckException;
import com.shuishou.retailer.account.models.IUserDataAccessor;
import com.shuishou.retailer.account.models.UserData;
import com.shuishou.retailer.common.models.Configs;
import com.shuishou.retailer.common.models.IConfigsDataAccessor;
import com.shuishou.retailer.goods.models.Goods;
import com.shuishou.retailer.goods.models.IGoodsDataAccessor;
import com.shuishou.retailer.goods.models.IPackageBindDataAccessor;
import com.shuishou.retailer.goods.models.PackageBind;
import com.shuishou.retailer.indent.models.IIndentDataAccessor;
import com.shuishou.retailer.indent.models.IIndentDetailDataAccessor;
import com.shuishou.retailer.indent.models.Indent;
import com.shuishou.retailer.indent.models.IndentDetail;
import com.shuishou.retailer.indent.view.GoodsSellRecord;
import com.shuishou.retailer.log.models.LogData;
import com.shuishou.retailer.log.services.ILogService;
import com.shuishou.retailer.management.models.IShiftWorkDataAccessor;
import com.shuishou.retailer.management.models.ShiftWork;
import com.shuishou.retailer.member.models.IMemberConsumptionDataAccessor;
import com.shuishou.retailer.member.models.IMemberDataAccessor;
import com.shuishou.retailer.member.models.IMemberScoreDataAccessor;
import com.shuishou.retailer.member.models.Member;
import com.shuishou.retailer.member.models.MemberConsumption;
import com.shuishou.retailer.member.models.MemberScore;
import com.shuishou.retailer.views.ObjectListResult;
import com.shuishou.retailer.views.ObjectResult;
import com.shuishou.retailer.views.Result;


@Service
public class IndentService implements IIndentService {
	
	private final static Logger logger = LoggerFactory.getLogger(IndentService.class);
	private DecimalFormat decimalFormat = new DecimalFormat("0.00");
	@Autowired
	private ILogService logService;
	
	@Autowired
	private IConfigsDataAccessor configsDA;
	
	@Autowired
	private IUserDataAccessor userDA;

	@Autowired
	private IIndentDataAccessor indentDA;
	
	@Autowired
	private IGoodsDataAccessor goodsDA;
	
	@Autowired
	private IIndentDetailDataAccessor indentDetailDA;
	
	@Autowired
	private IMemberDataAccessor memberDA;
	
	@Autowired
	private IMemberScoreDataAccessor memberScoreDA;
	
	@Autowired
	private IMemberConsumptionDataAccessor memberConsumptionDA;
	
	@Autowired
	private IPackageBindDataAccessor packageBindDA;
	
	@Autowired
	private IShiftWorkDataAccessor shiftworkDA;
	
	
	private DecimalFormat doubleFormat = new DecimalFormat("0.00");
	
	@Override
	@Transactional(rollbackFor=DataCheckException.class)
	public synchronized ObjectResult saveIndent(int userId, JSONArray jsonOrder, String payWay, double paidPrice, String memberCard) throws DataCheckException {
		Member member = null;
		if (memberCard != null && memberCard.length() > 0){
			member = memberDA.getMemberByCard(memberCard);
			if (member == null){
				return new ObjectResult("cannot find member by card " + memberCard, false);
			}
		}
		UserData selfUser = userDA.getUserById(userId);
		
		double totalprice = 0;
		Calendar calendar = Calendar.getInstance();
		
		Indent indent = new Indent();
		indent.setCreateTime(calendar.getTime());
		indent.setIndentCode(ConstantValue.DFYMDHMS_2.format(calendar.getTime()));
		for(int i = 0; i< jsonOrder.length(); i++){
			JSONObject o = (JSONObject) jsonOrder.get(i);
			int goodsid = o.getInt("id");
			int amount = o.getInt("amount");
			double soldPrice = o.getDouble("soldPrice");
			Goods goods = goodsDA.getGoodsById(goodsid);
			if (goods == null){
//				return new ObjectResult("cannot find goods by id "+ goodsid, false);
				throw new DataCheckException("cannot find goods by id "+ goodsid);
			}
			IndentDetail detail = new IndentDetail();
			detail.setIndent(indent);
			detail.setGoodsId(goodsid);
			detail.setAmount(amount);
			detail.setGoodsName(goods.getName());
			detail.setGoodsPrice(goods.getSellPrice());
			detail.setSoldPrice(soldPrice);
			indent.addItem(detail);
			totalprice += goods.getSellPrice() * amount; 
			changeGoodsLeftAmount(goods, amount);
		}
		indent.setTotalPrice(totalprice);
		indent.setPaidPrice(paidPrice);
		indent.setMemberCard(memberCard);
		indent.setPayWay(payWay);
		indent.setIndentType(ConstantValue.INDENT_TYPE_ORDER);
		indent.setOperator(selfUser.getUsername());
		indentDA.save(indent);
		if (member != null){
			recordMemberScore(member, paidPrice);
			recordMemberConsumption(member, paidPrice);
		}
		
		
		logService.write(selfUser, LogData.LogType.INDENT_MAKE.toString(), "User " + selfUser + " make order : " + indent.getId());
		
		return new ObjectResult(Result.OK, true, indent);
	}
	
	/**
	 * 1. goods.leftamount >= amount: reduce the amount from the goods' leftamount;
	 * 2. goods.leftamount < amount: look for if there is existing package bind, if yes, reduce from the big package; if no, reduce amount from the goods
	 * @param goods
	 * @param amount
	 */
	@Transactional
	private void changeGoodsLeftAmount(Goods goods, int amount){
		if (goods.getLeftAmount() >= amount){
			goods.setLeftAmount(goods.getLeftAmount() - amount);
			goodsDA.save(goods);
		} else {
			//check package bind, reduce from big package
			PackageBind pb = packageBindDA.getPackageBindBySmallGoodsId(goods.getId());
			if (pb == null){
				goods.setLeftAmount(goods.getLeftAmount() - amount);
				goodsDA.save(goods);
			} else {
				Goods bigPackage = pb.getBigPackage();
				int bigAmount = (int)Math.ceil((double)amount / (double)pb.getRate());
				int left = bigAmount * pb.getRate() - amount;
				bigPackage.setLeftAmount(bigPackage.getLeftAmount() - bigAmount);
				goods.setLeftAmount(goods.getLeftAmount() + left);
				goodsDA.save(goods);
				goodsDA.save(bigPackage);
			}
		}
	}
		
	@Override
	@Transactional
	public ObjectListResult queryIndent(int start, int limit, String sstarttime, String sendtime, String payway, String member, String indentCode, String orderby, String orderbydesc) {
		if (orderby != null && orderby.length() > 0
				&& orderbydesc != null && orderbydesc.length() > 0){
			return new ObjectListResult("orderby and orderbydesc are conplicted", false);
		}
		List<String> orderbys = null;//default as null. if param "orderby" is not null, then initial this array.
		if (orderby != null && orderby.length() > 0){
			orderbys = new ArrayList<String>();
			if (orderby.indexOf("createtime")>=0){
				orderbys.add("createTime");
			}
		} 
		
		List<String> orderbydescs = null;
		if (orderbydesc != null && orderbydesc.length() > 0){
			orderbydescs = new ArrayList<>();
			if (orderbydesc.indexOf("id") >= 0){
				orderbydescs.add("id");
			}	
			if (orderbydesc.indexOf("createtime")>=0){
				orderbydescs.add("createTime");
			}
		}
		
		Date starttime = null;
		Date endtime = null;
		if (sstarttime != null && sstarttime.length() > 0){
			try {
				starttime = ConstantValue.DFYMDHMS.parse(sstarttime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (sendtime != null && sendtime.length() > 0){
			try {
				endtime = ConstantValue.DFYMDHMS.parse(sendtime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		int count = indentDA.getIndentCount(starttime, endtime, payway, member);
		if (count >= 300)
			return new ObjectListResult("Record is over 300, please change the filter", false, null, count);
		List<Indent> indents = indentDA.getIndents(start, limit, starttime, endtime, payway, member, indentCode, orderbys, orderbydescs);
		if (indents == null || indents.isEmpty())
			return new ObjectListResult(Result.OK, true, null, 0);
		for (int i = 0; i < indents.size(); i++) {
			Indent indent = indents.get(i);
			Hibernate.initialize(indent);
			Hibernate.initialize(indent.getItems());
		}
		return new ObjectListResult(Result.OK, true, (ArrayList<Indent>)indents, count);
	}
	
	@Override
	@Transactional
	public ObjectListResult queryIndentForShiftwork(int shiftworkId) {
		ShiftWork sw = shiftworkDA.getShiftWorkById(shiftworkId);
		if (sw == null)
			return new ObjectListResult("Cannot find Shiftwork record by id" + shiftworkId, false);
		
		Date starttime = sw.getStartTime();
		Date endtime = sw.getEndTime();
		if (endtime == null)
			endtime = new Date();
		List<Indent> indents = indentDA.getIndents(0, 10000, starttime, endtime, null, null, null, null, null);
		if (indents == null || indents.isEmpty())
			return new ObjectListResult(Result.OK, true, null, 0);
		for (int i = 0; i < indents.size(); i++) {
			Indent indent = indents.get(i);
			Hibernate.initialize(indent);
			Hibernate.initialize(indent.getItems());
		}
		return new ObjectListResult(Result.OK, true, (ArrayList<Indent>)indents, indents.size());
	}
	
	@Override
	@Transactional
	public ObjectListResult queryPrebuyIndent(int start, int limit, String sStarttime, String sEndtime, String member) {
		
		Date starttime = null;
		Date endtime = null;
		if (sStarttime != null && sStarttime.length() > 0){
			try {
				starttime = ConstantValue.DFYMDHMS.parse(sStarttime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (sEndtime != null && sEndtime.length() > 0){
			try {
				endtime = ConstantValue.DFYMDHMS.parse(sEndtime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		List<Indent> indents = indentDA.getPrebuyIndents(start, limit, starttime, endtime, member);
		if (indents == null || indents.isEmpty())
			return new ObjectListResult(Result.OK, true, null, 0);
		for (int i = 0; i < indents.size(); i++) {
			Indent indent = indents.get(i);
			Hibernate.initialize(indent);
			Hibernate.initialize(indent.getItems());
		}
		return new ObjectListResult(Result.OK, true, (ArrayList<Indent>)indents, 0);
	}

	/**
	 * 修改商品库存, 记录用户消费记录
	 */
	@Override
	@Transactional(rollbackFor=DataCheckException.class)
	public ObjectResult changePreOrderToOrder(int userId, int preindentId) throws DataCheckException {
		Indent preindent = indentDA.getIndentById(preindentId);
		if (preindent == null){
			return new ObjectResult("cannot find indent by id "+preindentId, false);
		}
		Member member = null;
		if (preindent.getMemberCard() != null &&preindent.getMemberCard().length() > 0){
			member = memberDA.getMemberByCard(preindent.getMemberCard());
			if (member == null)
				return new ObjectResult("cannot find member by card " + preindent.getMemberCard(), false);
		}
		UserData selfUser = userDA.getUserById(userId);
		Indent indent = new Indent();
		Date date = new Date();
		indent.setIndentType(ConstantValue.INDENT_TYPE_ORDER);
		indent.setCreateTime(date);
		indent.setOperator(selfUser.getUsername());
		indent.setIndentCode(ConstantValue.DFYMDHMS_2.format(date));
		indent.setMemberCard(preindent.getMemberCard());
		indent.setPaidPrice(preindent.getPaidPrice());
		indent.setTotalPrice(preindent.getTotalPrice());
		indent.setPayWay(preindent.getPayWay());
		indent.setIndentType(ConstantValue.INDENT_TYPE_ORDER);
		
		List<IndentDetail> predetails = preindent.getItems();
		for (int i = 0; i < predetails.size(); i++) {
			IndentDetail predetail = predetails.get(i);
			Goods goods = goodsDA.getGoodsById(predetail.getGoodsId());
			if (goods == null){
				throw new DataCheckException("cannot find goods by id "+ predetail.getGoodsId());
			}
			changeGoodsLeftAmount(goods, predetail.getAmount());
			//copy every detail to new indent
			IndentDetail detail = new IndentDetail();
			detail.setAmount(predetail.getAmount());
			detail.setGoodsId(predetail.getGoodsId());
			detail.setGoodsName(predetail.getGoodsName());
			detail.setGoodsPrice(predetail.getGoodsPrice());
			detail.setSoldPrice(predetail.getSoldPrice());
			detail.setIndent(indent);
			indentDetailDA.save(detail);
			indent.addItem(detail);
		}
		indentDA.save(indent);
		preindent.setIndentType(ConstantValue.INDENT_TYPE_PREBUY_FINISHED);
		indentDA.save(preindent);
		if (member != null){
			double paidPrice = indent.getPaidPrice();
			recordMemberScore(member, paidPrice);
			recordMemberConsumption(member, paidPrice);
		}
		Hibernate.initialize(indent);
		Hibernate.initialize(indent.getItems());
		
		logService.write(selfUser, LogData.LogType.INDENT_MAKE.toString(), "User " + selfUser + " make preorder (id="+preindentId+") to order (id=" + indent.getId()+")");
		return new ObjectResult(Result.OK, true, indent);
	}
	
	@Override
	@Transactional()
	public ObjectResult deletePreOrder(int userId, int indentId) {
		Indent indent = indentDA.getIndentById(indentId);
		if (indent == null){
			return new ObjectResult("cannot find indent by id "+indentId, false);
		}
		indentDA.delete(indent);
		
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.INDENT_MAKE.toString(), "User " + selfUser + " delete preorder : " + indent.getId());
		return new ObjectResult(Result.OK, true, indent);
	}
	
	@Override
	public ObjectResult printIndent(int userId, int indentId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(rollbackFor=DataCheckException.class)
	public ObjectResult refundIndent(int userId, JSONArray jsonOrder, double refundPrice, String memberCard, boolean returnToStorage) {
		Member member = null;
		if (memberCard != null && memberCard.length() > 0){
			member = memberDA.getMemberByCard(memberCard);
			if (member == null){
				return new ObjectResult("cannot find member by card " + memberCard, false);
			}
		}
		UserData selfUser = userDA.getUserById(userId);
		double totalprice = 0;
		Indent indent = new Indent();
		Calendar c = Calendar.getInstance();
		indent.setCreateTime(c.getTime());
		indent.setIndentCode(ConstantValue.DFYMDHMS_2.format(c.getTime()));
		indent.setOperator(selfUser.getUsername());
		for(int i = 0; i< jsonOrder.length(); i++){
			JSONObject o = (JSONObject) jsonOrder.get(i);
			int goodsid = o.getInt("id");
			int amount = o.getInt("amount");
			double soldPrice = o.getDouble("soldPrice");
			Goods goods = goodsDA.getGoodsById(goodsid);
			if (goods == null){
				return new ObjectResult("cannot find goods by id "+ goodsid, false);
			}
			IndentDetail detail = new IndentDetail();
			detail.setIndent(indent);
			detail.setGoodsId(goodsid);
			detail.setAmount(amount);
			detail.setGoodsName(goods.getName());
			detail.setGoodsPrice(goods.getSellPrice());
			detail.setSoldPrice(soldPrice);
			indent.addItem(detail);
			totalprice += goods.getSellPrice() * amount;
			if (returnToStorage){
				changeGoodsLeftAmount(goods, amount * (-1));
			}
		}
		indent.setTotalPrice(totalprice);
		indent.setPaidPrice(refundPrice);
		indent.setMemberCard(memberCard);
		indent.setIndentType(ConstantValue.INDENT_TYPE_REFUND);
		indentDA.save(indent);
		if (member != null){
			recordMemberScore(member, refundPrice * (-1));
			try {
				recordMemberConsumption(member, refundPrice * (-1));
			} catch (DataCheckException e) {
				logger.error("", e);
			}
		}
		
		
		logService.write(selfUser, LogData.LogType.INDENT_MAKE.toString(), "User " + selfUser + " make refund order : " + indent.getId());
		
		return new ObjectResult(Result.OK, true, indent);
	}
	
	@Transactional
	private void recordMemberScore(Member member, double amount){
		Date time = new Date();
		boolean byScore = false;
		double scorePerDollar = 0;
		String branchName = "";
		List<Configs> configs = configsDA.queryConfigs();
		for(Configs config : configs){
			if (ConstantValue.CONFIGS_MEMBERMGR_BYSCORE.equals(config.getName())){
				byScore = Boolean.valueOf(config.getValue());
			} else if (ConstantValue.CONFIGS_MEMBERMGR_SCOREPERDOLLAR.equals(config.getName())){
				scorePerDollar = Double.parseDouble(config.getValue());
			} else if (ConstantValue.CONFIGS_BRANCHNAME.equals(config.getName())){
				branchName = config.getValue();
			}
		}
		if (byScore && scorePerDollar > 0){
			MemberScore ms = new MemberScore();
			ms.setDate(time);
			ms.setAmount(scorePerDollar * amount);
			ms.setPlace(branchName);
			ms.setType(ConstantValue.MEMBERSCORE_REFUND);
			ms.setMember(member);
			memberScoreDA.save(ms);
			member.setScore(member.getScore() + ms.getAmount());
			member.setLastModifyTime(time);
			memberDA.save(member);
		}
	}
	
	@Transactional
	private void recordMemberConsumption(Member member, double amount) throws DataCheckException{
		Date time = new Date();
		boolean byDeposit = false;
		String branchName = "";
		List<Configs> configs = configsDA.queryConfigs();
		for(Configs config : configs){
			if (ConstantValue.CONFIGS_MEMBERMGR_BYDEPOSIT.equals(config.getName())){
				byDeposit = Boolean.valueOf(config.getValue());
			} else if (ConstantValue.CONFIGS_BRANCHNAME.equals(config.getName())){
				branchName = config.getValue();
			}
		}
		if (byDeposit){
			if (member.getBalanceMoney() < amount){
				throw new DataCheckException("Meber's balance is not enought to pay");
			}
			MemberConsumption mc = new MemberConsumption();
			mc.setAmount(amount);
			mc.setDate(time);
			mc.setMember(member);
			mc.setPlace(branchName);
			mc.setType(ConstantValue.MEMBERDEPOSIT_REFUND);
			memberConsumptionDA.save(mc);
			member.setBalanceMoney(member.getBalanceMoney() - amount);
			member.setLastModifyTime(time);
			memberDA.save(member);
		}
	}

	/**
	 * 预售单
	 * 库存数量不减, 如果预售单取消,不用考虑库存; 如果预售单转订单, 需要减去库存
	 * 会员消费记录不存, 如果
	 */
	@Override
	@Transactional
	public ObjectResult prebuyIndent(int userId, JSONArray jsonOrder, String payWay, double paidPrice,
			String memberCard, boolean paid) {
		Member member = null;
		if (memberCard != null && memberCard.length() > 0){
			member = memberDA.getMemberByCard(memberCard);
			if (member == null){
				return new ObjectResult("cannot find member by card " + memberCard, false);
			}
		}
		UserData selfUser = userDA.getUserById(userId);
		double totalprice = 0;
		Indent indent = new Indent();
		Calendar c = Calendar.getInstance();
		indent.setCreateTime(c.getTime());
		indent.setIndentCode(ConstantValue.DFYMDHMS_2.format(c.getTime()));
		indent.setOperator(selfUser.getUsername());
		for(int i = 0; i< jsonOrder.length(); i++){
			JSONObject o = (JSONObject) jsonOrder.get(i);
			int goodsid = o.getInt("id");
			int amount = o.getInt("amount");
			double soldPrice = o.getDouble("soldPrice");
			Goods goods = goodsDA.getGoodsById(goodsid);
			if (goods == null){
				return new ObjectResult("cannot find goods by id "+ goodsid, false);
			}
			IndentDetail detail = new IndentDetail();
			detail.setIndent(indent);
			detail.setGoodsId(goodsid);
			detail.setAmount(amount);
			detail.setGoodsName(goods.getName());
			detail.setGoodsPrice(goods.getSellPrice());
			detail.setSoldPrice(soldPrice);
			indent.addItem(detail);
			totalprice += goods.getSellPrice() * amount; 
		}
		indent.setTotalPrice(totalprice);
		indent.setPaidPrice(paidPrice);
		indent.setMemberCard(memberCard);
		indent.setPayWay(payWay);
		if (paid)
			indent.setIndentType(ConstantValue.INDENT_TYPE_PREBUY_PAID);
		else 
			indent.setIndentType(ConstantValue.INDENT_TYPE_PREBUY_UNPAID);
		indentDA.save(indent);
		
		
		
		logService.write(selfUser, LogData.LogType.INDENT_MAKE.toString(), "User " + selfUser + " make preorder : " + indent.getId());
		
		return new ObjectResult(Result.OK, true, indent);
		
	}
	
	@Override
	@Transactional
	public ObjectListResult queryGoodsSoldRecord(int goodsId, String sStarttime, String sEndtime, String payway, String member){
		Date starttime = null;
		Date endtime = null;
		if (sStarttime != null && sStarttime.length() > 0){
			try {
				starttime = ConstantValue.DFYMDHMS.parse(sStarttime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (sEndtime != null && sEndtime.length() > 0){
			try {
				endtime = ConstantValue.DFYMDHMS.parse(sEndtime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		List<IndentDetail> details = indentDetailDA.getIndentDetailByGoods(goodsId, starttime, endtime, payway, member);
		ArrayList<GoodsSellRecord> records = new ArrayList<>();
		for (int i = 0; i < details.size(); i++) {
			IndentDetail detail = details.get(i);
			GoodsSellRecord record = new GoodsSellRecord();
			record.setAmount(detail.getAmount());
			record.setMember(detail.getIndent().getMemberCard());
			record.setPayWay(detail.getIndent().getPayWay());
			record.setSoldPrice(detail.getSoldPrice());
			record.setSoldTime(ConstantValue.DFYMDHMS.format(detail.getIndent().getCreateTime()));
			records.add(record);
		}
		return new ObjectListResult(Result.OK, true, records);
	}
}
