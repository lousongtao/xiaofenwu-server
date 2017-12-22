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
	public ObjectResult addGoods(int userId, String name, String barcode, double sellPrice, double buyPrice, double memeberPrice, int leftAmount, int category2Id);
	public ObjectResult importGoods(int userId, int goodsId, int importAmount);
	public ObjectResult refundGoods(int userId, int goodsId, int refundAmount);
	
	public ObjectResult updateCategory1(int userId, int id, String name, int sequence);
	public ObjectResult updateCategory2(int userId, int id, String name, int sequence, int category1Id);
	public ObjectResult updateGoods(int userId, int goodsId, String name, String barcode, double sellPrice, double buyPrice, double memeberPrice, int category2Id);
	
	public Result deleteCategory1(int userId, int category1Id);
	public Result deleteCategory2(int userId, int category2Id);
	public Result deleteGoods(int userId, int goodsId);
	
	public ObjectListResult queryAllGoods();
}