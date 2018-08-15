package com.shuishou.retailer.goods.services;

import java.util.ArrayList;
import org.springframework.web.multipart.MultipartFile;

import com.shuishou.retailer.views.ObjectListResult;
import com.shuishou.retailer.views.ObjectResult;
import com.shuishou.retailer.views.Result;
import com.shuishou.retailer.views.SimpleValueResult;

public interface IGoodsService {

	public ObjectResult addCategory1(int userId, String name, int sequence);
	public ObjectResult addCategory2(int userId, String name, int sequence, int category1Id);
	public ObjectResult addGoods(int userId, String name, String barcode, double sellPrice, double buyPrice, double tradePrice, double memeberPrice, 
			int leftAmount, int category2Id, String description);
	public ObjectResult importGoods(int userId, int goodsId, int importAmount);
	public ObjectResult changeGoodsAmount(int userId, int goodsId, int newAmount);
	public ObjectResult refundGoods(int userId, int goodsId, int refundAmount);
	
	public ObjectResult updateCategory1(int userId, int id, String name, int sequence);
	public ObjectResult updateCategory2(int userId, int id, String name, int sequence, int category1Id);
	public ObjectResult updateGoods(int userId, int goodsId, String name, String barcode, double sellPrice, double buyPrice, double tradePrice,
			double memeberPrice, int category2Id, String description);
	
	public Result deleteCategory1(int userId, int category1Id);
	public Result deleteCategory2(int userId, int category2Id);
	public Result deleteGoods(int userId, int goodsId);
	
	public ObjectListResult queryAllGoods();
	
	public ObjectResult queryGoodsByBarcode(String barcode);
	
	public ObjectResult addPackageBind(int userId, int bigPackageId, int smallPackageId, int rate);
	public ObjectResult updatePackageBind(int userId, int packageBindId, int bigPackageId, int smallPackageId, int rate);
	public ObjectListResult queryPackageBind(int userId);
	public Result deletePackageBind(int userId, int packageBindId);
	
	public ObjectResult addPromotion(int userId, boolean forbidMemberDiscount, int objectAType, int objectAId, int objectAQuantity, int objectBType, int objectBId, int objectBQuantity, int rewardType, double rewardValue);
	public ObjectResult updatePromotion(int userId, int id, boolean forbidMemberDiscount, int objectAType, int objectAId, int objectAQuantity, int objectBType, int objectBId, int objectBQuantity, int rewardType, double rewardValue);
	public ObjectResult deletePromotion(int userId, int id);
	public ObjectResult changePromotionStatus(int userId, int id);
	public ObjectListResult queryAllPromotion();
}
