package com.shuishou.retailer.goods.services;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shuishou.retailer.ConstantValue;
import com.shuishou.retailer.account.models.IUserDataAccessor;
import com.shuishou.retailer.account.models.UserData;
import com.shuishou.retailer.log.models.LogData;
import com.shuishou.retailer.log.services.ILogService;
import com.shuishou.retailer.goods.models.AddGoodsRecord;
import com.shuishou.retailer.goods.models.Category1;
import com.shuishou.retailer.goods.models.Category2;
import com.shuishou.retailer.goods.models.Goods;
import com.shuishou.retailer.goods.models.IAddGoodsRecordDataAccessor;
import com.shuishou.retailer.goods.models.ICategory1DataAccessor;
import com.shuishou.retailer.goods.models.ICategory2DataAccessor;
import com.shuishou.retailer.goods.models.IGoodsDataAccessor;
import com.shuishou.retailer.goods.models.IPackageBindDataAccessor;
import com.shuishou.retailer.goods.models.PackageBind;
import com.shuishou.retailer.views.ObjectListResult;
import com.shuishou.retailer.views.ObjectResult;
import com.shuishou.retailer.views.Result;
import com.shuishou.retailer.views.SimpleValueResult;

@Service
public class GoodsService implements IGoodsService {
	
	private final static Logger logger = Logger.getLogger(GoodsService.class);
	
	@Autowired
	private ILogService logService;
	
	@Autowired
	private IUserDataAccessor userDA;
	
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ICategory1DataAccessor category1DA;
	
	@Autowired
	private ICategory2DataAccessor category2DA;
	
	@Autowired
	private IGoodsDataAccessor goodsDA;
	
	@Autowired
	private IAddGoodsRecordDataAccessor addGoodsRecordDA;
	
	@Autowired
	private IPackageBindDataAccessor packageBindDA;
	
	/**
	 * @param userId, the operator Id
	 */
	@Override
	@Transactional
	public ObjectResult addCategory1(int userId, String name, int sequence) {
		Category1 c1 = new Category1();
		c1.setName(name);
		c1.setSequence(sequence);
		category1DA.save(c1);
		hibernateInitialCategory1(c1);
		
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CATEGORY1_CHANGE.toString(), "User " + selfUser + " add Category1 : " + c1);

		return new ObjectResult(Result.OK, true, c1);
	}

	/**
	 * @param userId, the operator Id
	 */
	@Override
	@Transactional
	public ObjectResult addCategory2(int userId, String name, int sequence, int category1Id) {
		Category1 c1 = category1DA.getCategory1ById(category1Id);
		if (c1 == null){
			return new ObjectResult("cannot find category1 by id : "+ category1Id, false, null);
		}
		
		Category2 c2 = new Category2();
		c2.setName(name);
		c2.setSequence(sequence);
		c2.setCategory1(c1);
		category2DA.save(c2);
		
		hibernateInitialCategory2(c2);
		
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CATEGORY2_CHANGE.toString(), "User " + selfUser + " add Category2 : " + c2);

		return new ObjectResult(Result.OK, true, c2);
	}

	@Override
	@Transactional
	public ObjectResult deleteCategory1(int userId, int category1Id) {
		Category1 c1 = category1DA.getCategory1ById(category1Id);
		if (c1 == null)
			return new ObjectResult("not find Category1 by id "+ category1Id, false);
		if (c1.getCategory2s() != null && !c1.getCategory2s().isEmpty())
			return new ObjectResult("this category is not empty", false);
		category1DA.delete(c1);
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CATEGORY1_CHANGE.toString(),
				"User " + selfUser + " delete Category1 " + c1);

		return new ObjectResult(Result.OK, true);	
	}

	@Override
	@Transactional
	public ObjectResult deleteCategory2(int userId, int category2Id) {
		Category2 c2 = category2DA.getCategory2ById(category2Id);
		if (c2 == null)
			return new ObjectResult("not find Category2 by id "+ category2Id, false);
		//must delete first from C1's children, otherwise report hibernate exception:
		//deleted object would be re-saved by cascade (remove deleted object from associations)
		c2.getCategory1().getCategory2s().remove(c2);
		category2DA.delete(c2);
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CATEGORY2_CHANGE.toString(),
				"User " + selfUser + " delete Category2 " + c2.getName() + ".");

		return new ObjectResult(Result.OK, true);		
	}

	@Override
	@Transactional
	public ObjectResult updateCategory1(int userId, int id, String name, int sequence) {
		Category1 c1 = category1DA.getCategory1ById(id);
		if (c1 == null)
			return new ObjectResult("not find Category1 by id "+ id, false, null);
		c1.setName(name);
		c1.setSequence(sequence);
		category1DA.save(c1);
		hibernateInitialCategory1(c1);
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CATEGORY1_CHANGE.toString(),
				"User " + selfUser + " update Category1, id = " + id 
				+ ", name = " + name + ", sequence = "+sequence+".");
		return new ObjectResult(Result.OK, true, c1);
	}

	@Override
	@Transactional
	public ObjectResult updateCategory2(int userId, int id, String name, int sequence, int category1Id) {
		Category1 c1 = category1DA.getCategory1ById(category1Id);
		if (c1 == null)
			return new ObjectResult("not find Category1 by id "+ category1Id, false, null);
		Category2 c2 = category2DA.getCategory2ById(id);
		if (c2 == null)
			return new ObjectResult("not find Category2 by id "+ id, false, null);
		
		c2.setName(name);
		c2.setSequence(sequence);
		c2.setCategory1(c1);
		category2DA.save(c2);
		
		hibernateInitialCategory2(c2);
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CATEGORY2_CHANGE.toString(),
				"User " + selfUser + " update Category2, id = " + id 
				+ ", name = " + name + ", sequence = "+sequence+", Category1 = "+ c1);
		return new ObjectResult(Result.OK, true, c2);
	}

	@Override
	@Transactional
	public ObjectResult addGoods(int userId, String name, String barcode, double sellPrice, double buyPrice, double tradePrice,
			double memberPrice, int leftAmount, int category2Id, String description) {
		Category2 c2 = category2DA.getCategory2ById(category2Id);
		if (c2 == null){
			return new ObjectResult("cannot find category2 by id "+ category2Id, false, null);
		}
		Goods goods = new Goods();
		goods.setName(name);
		goods.setBarcode(barcode);
		goods.setBuyPrice(buyPrice);
		goods.setTradePrice(tradePrice);
		goods.setSellPrice(sellPrice);
		goods.setMemberPrice(memberPrice);
		goods.setLeftAmount(leftAmount);
		goods.setDescription(description);
		goods.setCategory2(c2);
		goodsDA.save(goods);
		if (leftAmount > 0){
			AddGoodsRecord record = new AddGoodsRecord();
			record.setAmount(leftAmount);
			record.setGoodsId(goods.getId());
			record.setGoodsName(name);
			record.setType(ConstantValue.ADDGOODSTYPE_IMPORT);
			addGoodsRecordDA.save(record);
		}
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.GOODS_CHANGE.toString(),
				"User " + selfUser + " add goods, name = " + name + ", amount = " + leftAmount);
		
		return new ObjectResult(Result.OK, true, goods);
	}

	@Override
	@Transactional
	public ObjectResult importGoods(int userId, int goodsId, int importAmount) {
		Goods goods = goodsDA.getGoodsById(goodsId);
		if (goods == null){
			return new ObjectResult("cannot find goods by id " + goodsId, false, null);
		}
		goods.setLeftAmount(goods.getLeftAmount() + importAmount);
		goodsDA.save(goods);
		AddGoodsRecord record = new AddGoodsRecord();
		record.setAmount(importAmount);
		record.setGoodsId(goodsId);
		record.setGoodsName(goods.getName());
		record.setType(ConstantValue.ADDGOODSTYPE_IMPORT);
		addGoodsRecordDA.save(record);
		
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.GOODS_IMPORT.toString(),
				"User " + selfUser + " import goods, name = " + goods.getName() + ", importAmount = " + importAmount + ", leftAmount = " + goods.getLeftAmount());
		
		return new ObjectResult(Result.OK, true, goods);
	}
	
	@Override
	@Transactional
	public ObjectResult changeGoodsAmount(int userId, int goodsId, int newAmount) {
		Goods goods = goodsDA.getGoodsById(goodsId);
		if (goods == null){
			return new ObjectResult("cannot find goods by id " + goodsId, false, null);
		}
		int oldAmount = goods.getLeftAmount();
		goods.setLeftAmount(newAmount);
		goodsDA.save(goods);
		
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.GOODS_CHANGEAMOUNT.toString(),
				"User " + selfUser + " change goods amount, name = " + goods.getName() 
				+ ", from = " + oldAmount + ", to = " + newAmount);
		
		return new ObjectResult(Result.OK, true, goods);
	}

	@Override
	@Transactional
	public ObjectResult refundGoods(int userId, int goodsId, int refundAmount) {
		Goods goods = goodsDA.getGoodsById(goodsId);
		if (goods == null){
			return new ObjectResult("cannot find goods by id " + goodsId, false, null);
		}
		goods.setLeftAmount(goods.getLeftAmount() + refundAmount);
		goodsDA.save(goods);
		AddGoodsRecord record = new AddGoodsRecord();
		record.setAmount(refundAmount);
		record.setGoodsId(goodsId);
		record.setGoodsName(goods.getName());
		record.setType(ConstantValue.ADDGOODSTYPE_REFUND);
		addGoodsRecordDA.save(record);
		
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.GOODS_CHANGE.toString(),
				"User " + selfUser + " refund goods, name = " + goods.getName() + ", refundAmount = " + refundAmount + ", leftAmount = " + goods.getLeftAmount());
		
		return new ObjectResult(Result.OK, true, goods);
	}
	
	@Override
	@Transactional
	public ObjectResult updateGoods(int userId, int goodsId, String name, String barcode, double sellPrice, double buyPrice, double tradePrice,
			double memberPrice, int category2Id, String description){
		Goods goods = goodsDA.getGoodsById(goodsId);
		if (goods == null){
			return new ObjectResult("cannot find goods by id " + goodsId, false, null);
		}
		if (goods.getCategory2().getId() != category2Id){
			Category2 c2 = category2DA.getCategory2ById(category2Id);
			if (c2 == null){
				return new ObjectResult("cannot find category2 by id "+ category2Id, false, null);
			}
			goods.setCategory2(c2);
		}
		goods.setName(name);
		goods.setBarcode(barcode);
		goods.setBuyPrice(buyPrice);
		goods.setTradePrice(tradePrice);
		goods.setSellPrice(sellPrice);
		goods.setMemberPrice(memberPrice);
		goods.setDescription(description);
		goodsDA.save(goods);
		
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.GOODS_CHANGE.toString(),
				"User " + selfUser + " update goods, id = " + goodsId + ",name = " + name + ", barcode = " + barcode 
				+ ", buyPrice = " + buyPrice + ", sellPrice = " + sellPrice + ", memberPrice = " + memberPrice + ", CategoryId = " + category2Id);
		return new ObjectResult(Result.OK, true, goods);
	}
	
	@Override
	@Transactional
	public Result deleteGoods(int userId, int goodsId){
		Goods goods = goodsDA.getGoodsById(goodsId);
		if (goods == null)
			return new ObjectResult("not find Goods by id "+ goodsId, false);
		//must delete first from C2's children, otherwise report hibernate exception:
		//deleted object would be re-saved by cascade (remove deleted object from associations)
		goods.getCategory2().getGoods().remove(goods);
		goodsDA.delete(goods);
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.GOODS_CHANGE.toString(),
				"User " + selfUser + " delete Goods " + goods.getName() + ".");

		return new ObjectResult(Result.OK, true);		
	}
	
	@Override
	@Transactional
	public ObjectListResult queryAllGoods(){
		List<Category1> c1s = category1DA.getAllCategory1();
		hibernateInitialCategory1(c1s);
		return new ObjectListResult(Result.OK, true, c1s);
	}
	
	@Override
	@Transactional
	public ObjectResult addPackageBind(int userId, int bigPackageId, int smallPackageId, int rate) {
		Goods big = goodsDA.getGoodsById(bigPackageId);
		Goods small = goodsDA.getGoodsById(smallPackageId);
		if (big == null)
			return new ObjectResult("not find Goods by id "+ bigPackageId, false);
		if (small == null)
			return new ObjectResult("not find Goods by id "+ smallPackageId, false);
		PackageBind pb = new PackageBind();
		pb.setBigPackage(big);
		pb.setSmallPackage(small);
		pb.setRate(rate);
		packageBindDA.save(pb);
		Hibernate.initialize(big);
		Hibernate.initialize(small);
		
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.PACKAGEBIND_CHANGE.toString(),
				"User " + selfUser + " create PackageBind " + pb.toString() + ".");
		return new ObjectResult(Result.OK, true, pb);
	}

	@Override
	@Transactional
	public ObjectResult updatePackageBind(int userId, int packageBindId, int bigPackageId, int smallPackageId,
			int rate) {
		PackageBind pb = packageBindDA.getPackageBindById(packageBindId);
		if (pb == null)
			return new ObjectResult("not find PackageBind by id "+ packageBindId, false);
		Goods big = goodsDA.getGoodsById(bigPackageId);
		Goods small = goodsDA.getGoodsById(smallPackageId);
		if (big == null)
			return new ObjectResult("not find Goods by id "+ bigPackageId, false);
		if (small == null)
			return new ObjectResult("not find Goods by id "+ smallPackageId, false);
		pb.setBigPackage(big);
		pb.setSmallPackage(small);
		pb.setRate(rate);
		packageBindDA.save(pb);
		
		Hibernate.initialize(big);
		Hibernate.initialize(small);
		
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.PACKAGEBIND_CHANGE.toString(),
				"User " + selfUser + " update PackageBind " + pb.toString() + ".");
		return new ObjectResult(Result.OK, true, pb);
	}

	@Override
	@Transactional
	public ObjectListResult queryPackageBind(int userId) {
		List<PackageBind> pbs = packageBindDA.getAllPackageBinds();
		return new ObjectListResult(Result.OK, true, pbs);
	}

	@Override
	@Transactional
	public Result deletePackageBind(int userId, int packageBindId) {
		PackageBind pb = packageBindDA.getPackageBindById(packageBindId);
		if (pb == null)
			return new ObjectResult("not find PackageBind by id "+ packageBindId, false);
		packageBindDA.delete(pb);
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.PACKAGEBIND_CHANGE.toString(),
				"User " + selfUser + " delete PackageBind " + pb.toString() + ".");
		return new ObjectResult(Result.OK, true, null);
	}
	
	@Transactional
	public void hibernateInitialCategory1(Category1 c1){
		Hibernate.initialize(c1);
		if (c1.getCategory2s() != null){
			for(Category2 c2 : c1.getCategory2s()){
				hibernateInitialCategory2(c2);
			}
		}
	}
	
	@Transactional
	public void hibernateInitialCategory1(List<Category1> c1s){
		for(Category1 c1 : c1s){
			hibernateInitialCategory1(c1);
		}
	}
	
	@Transactional
	public void hibernateInitialCategory2(Category2 c2){
		Hibernate.initialize(c2);
		if (c2.getGoods() != null){
			for(Goods goods : c2.getGoods()){
				Hibernate.initialize(goods);
			}
		}
	}

	

	

}
