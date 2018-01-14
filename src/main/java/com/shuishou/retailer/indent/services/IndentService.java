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
import com.shuishou.retailer.indent.models.IIndentDataAccessor;
import com.shuishou.retailer.indent.models.IIndentDetailDataAccessor;
import com.shuishou.retailer.indent.models.Indent;
import com.shuishou.retailer.indent.models.IndentDetail;
import com.shuishou.retailer.indent.view.GoodsSellRecord;
import com.shuishou.retailer.log.models.LogData;
import com.shuishou.retailer.log.services.ILogService;
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
		double totalprice = 0;
		Indent indent = new Indent();
		indent.setCreateTime(Calendar.getInstance().getTime());
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
			goods.setLeftAmount(goods.getLeftAmount() - amount);
			goodsDA.save(goods);
		}
		indent.setTotalPrice(totalprice);
		indent.setPaidPrice(paidPrice);
		indent.setMemberCard(memberCard);
		indent.setPayWay(payWay);
		indent.setIndentType(ConstantValue.INDENT_TYPE_ORDER);
		indentDA.save(indent);
		if (member != null){
			Date time = new Date();
			boolean byScore = false;
			boolean byDeposit = false;
			double scorePerDollar = 0;
			String branchName = "";
			List<Configs> configs = configsDA.queryConfigs();
			for(Configs config : configs){
				if (ConstantValue.CONFIGS_MEMBERMGR_BYSCORE.equals(config.getName())){
					byScore = Boolean.valueOf(config.getValue());
				} else if (ConstantValue.CONFIGS_MEMBERMGR_SCOREPERDOLLAR.equals(config.getName())){
					scorePerDollar = Double.parseDouble(config.getValue());
				} else if (ConstantValue.CONFIGS_MEMBERMGR_BYDEPOSIT.equals(config.getName())){
					byDeposit = Boolean.valueOf(config.getValue());
				} else if (ConstantValue.CONFIGS_BRANCHNAME.equals(config.getName())){
					branchName = config.getValue();
				}
			}
			if (byScore && scorePerDollar > 0){
				MemberScore ms = new MemberScore();
				ms.setDate(time);
				ms.setAmount(scorePerDollar * paidPrice);
				ms.setPlace(branchName);
				ms.setType(ConstantValue.MEMBERSCORE_CONSUM);
				ms.setMember(member);
				memberScoreDA.save(ms);
				member.setScore(member.getScore() + ms.getAmount());
				member.setLastModifyTime(time);
				memberDA.save(member);
			}
			if (byDeposit){
				if (member.getBalanceMoney() < paidPrice){
//					return new ObjectResult("Meber's balance is not enought to pay", false);
					throw new DataCheckException("Meber's balance is not enought to pay");
				}
				MemberConsumption mc = new MemberConsumption();
				mc.setAmount(paidPrice);
				mc.setDate(time);
				mc.setMember(member);
				mc.setPlace(branchName);
				mc.setType(ConstantValue.MEMBERDEPOSIT_CONSUM);
				memberConsumptionDA.save(mc);
				member.setBalanceMoney(member.getBalanceMoney() - paidPrice);
				member.setLastModifyTime(time);
				memberDA.save(member);
			}
		}
		
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.INDENT_MAKE.toString(), "User " + selfUser + " make order : " + indent.getId());
		
		return new ObjectResult(Result.OK, true, null);
	}
		
	@Override
	@Transactional
	public ObjectListResult queryIndent(int start, int limit, String sstarttime, String sendtime, String payway, String member, String orderby, String orderbydesc) {
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
		List<Indent> indents = indentDA.getIndents(start, limit, starttime, endtime, payway, member, orderbys, orderbydescs);
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

	@Override
	@Transactional(rollbackFor=DataCheckException.class)
	public ObjectResult changePreOrderToOrder(int userId, int indentId) throws DataCheckException {
		Indent indent = indentDA.getIndentById(indentId);
		if (indent == null){
			return new ObjectResult("cannot find indent by id "+indentId, false);
		}
		Member member = null;
		if (indent.getMemberCard() != null && indent.getMemberCard().length() > 0){
			member = memberDA.getMemberByCard(indent.getMemberCard());
			if (member == null)
				return new ObjectResult("cannot find member by card " + indent.getMemberCard(), false);
		}
		indent.setIndentType(ConstantValue.INDENT_TYPE_ORDER);
		indentDA.save(indent);
		
		if (member != null){
			double paidPrice = indent.getPaidPrice();
			Date time = new Date();
			boolean byScore = false;
			boolean byDeposit = false;
			double scorePerDollar = 0;
			String branchName = "";
			List<Configs> configs = configsDA.queryConfigs();
			for(Configs config : configs){
				if (ConstantValue.CONFIGS_MEMBERMGR_BYSCORE.equals(config.getName())){
					byScore = Boolean.valueOf(config.getValue());
				} else if (ConstantValue.CONFIGS_MEMBERMGR_SCOREPERDOLLAR.equals(config.getName())){
					scorePerDollar = Double.parseDouble(config.getValue());
				} else if (ConstantValue.CONFIGS_MEMBERMGR_BYDEPOSIT.equals(config.getName())){
					byDeposit = Boolean.valueOf(config.getValue());
				} else if (ConstantValue.CONFIGS_BRANCHNAME.equals(config.getName())){
					branchName = config.getValue();
				}
			}
			if (byScore && scorePerDollar > 0){
				MemberScore ms = new MemberScore();
				ms.setDate(time);
				ms.setAmount(scorePerDollar * paidPrice);
				ms.setPlace(branchName);
				ms.setType(ConstantValue.MEMBERSCORE_CONSUM);
				ms.setMember(member);
				memberScoreDA.save(ms);
				member.setScore(member.getScore() + ms.getAmount());
				member.setLastModifyTime(time);
				memberDA.save(member);
			}
			if (byDeposit){
				if (member.getBalanceMoney() < paidPrice){
//					return new ObjectResult("Meber's balance is not enought to pay", false);
					throw new DataCheckException("Meber's balance is not enought to pay");
				}
				MemberConsumption mc = new MemberConsumption();
				mc.setAmount(paidPrice);
				mc.setDate(time);
				mc.setMember(member);
				mc.setPlace(branchName);
				mc.setType(ConstantValue.MEMBERDEPOSIT_CONSUM);
				memberConsumptionDA.save(mc);
				member.setBalanceMoney(member.getBalanceMoney() - paidPrice);
				member.setLastModifyTime(time);
				memberDA.save(member);
			}
		}
		Hibernate.initialize(indent);
		Hibernate.initialize(indent.getItems());
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.INDENT_MAKE.toString(), "User " + selfUser + " make preorder to order : " + indent.getId());
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
	@Transactional
	public ObjectResult refundIndent(int userId, JSONArray jsonOrder, double refundPrice, String memberCard, boolean returnToStorage) {
		Member member = null;
		if (memberCard != null && memberCard.length() > 0){
			member = memberDA.getMemberByCard(memberCard);
			if (member == null){
				return new ObjectResult("cannot find member by card " + memberCard, false);
			}
		}
		double totalprice = 0;
		Indent indent = new Indent();
		indent.setCreateTime(Calendar.getInstance().getTime());
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
				goods.setLeftAmount(goods.getLeftAmount() + amount);
				goodsDA.save(goods);
			}
		}
		indent.setTotalPrice(totalprice);
		indent.setPaidPrice(refundPrice);
		indent.setMemberCard(memberCard);
		indent.setIndentType(ConstantValue.INDENT_TYPE_REFUND);
		indentDA.save(indent);
		if (member != null){
			Date time = new Date();
			boolean byScore = false;
			boolean byDeposit = false;
			double scorePerDollar = 0;
			String branchName = "";
			List<Configs> configs = configsDA.queryConfigs();
			for(Configs config : configs){
				if (ConstantValue.CONFIGS_MEMBERMGR_BYSCORE.equals(config.getName())){
					byScore = Boolean.valueOf(config.getValue());
				} else if (ConstantValue.CONFIGS_MEMBERMGR_SCOREPERDOLLAR.equals(config.getName())){
					scorePerDollar = Double.parseDouble(config.getValue());
				} else if (ConstantValue.CONFIGS_MEMBERMGR_BYDEPOSIT.equals(config.getName())){
					byDeposit = Boolean.valueOf(config.getValue());
				} else if (ConstantValue.CONFIGS_BRANCHNAME.equals(config.getName())){
					branchName = config.getValue();
				}
			}
			if (byScore && scorePerDollar > 0){
				MemberScore ms = new MemberScore();
				ms.setDate(time);
				ms.setAmount(scorePerDollar * refundPrice * (-1));
				ms.setPlace(branchName);
				ms.setType(ConstantValue.MEMBERSCORE_REFUND);
				ms.setMember(member);
				memberScoreDA.save(ms);
				member.setScore(member.getScore() + ms.getAmount());
				member.setLastModifyTime(time);
				memberDA.save(member);
			}
			if (byDeposit){
				MemberConsumption mc = new MemberConsumption();
				mc.setAmount(refundPrice * (-1));
				mc.setDate(time);
				mc.setMember(member);
				mc.setPlace(branchName);
				mc.setType(ConstantValue.MEMBERDEPOSIT_REFUND);
				memberConsumptionDA.save(mc);
				member.setBalanceMoney(member.getBalanceMoney() + refundPrice);
				member.setLastModifyTime(time);
				memberDA.save(member);
			}
		}
		
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.INDENT_MAKE.toString(), "User " + selfUser + " make refund order : " + indent.getId());
		
		return new ObjectResult(Result.OK, true, null);
	}

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
		double totalprice = 0;
		Indent indent = new Indent();
		indent.setCreateTime(Calendar.getInstance().getTime());
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
			goods.setLeftAmount(goods.getLeftAmount() - amount);
			goodsDA.save(goods);
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
		
		
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.INDENT_MAKE.toString(), "User " + selfUser + " make preorder : " + indent.getId());
		
		return new ObjectResult(Result.OK, true, null);
		
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
