package com.shuishou.retailer.goods.controllers;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuishou.retailer.BaseController;
import com.shuishou.retailer.ConstantValue;
import com.shuishou.retailer.account.services.IAccountService;
import com.shuishou.retailer.account.services.IPermissionService;
import com.shuishou.retailer.goods.services.IGoodsService;
import com.shuishou.retailer.views.ObjectListResult;
import com.shuishou.retailer.views.ObjectResult;
import com.shuishou.retailer.views.Result;
import com.shuishou.retailer.views.SimpleValueResult;

@Controller
public class GoodsController extends BaseController {

	/**
	 * the logger.
	 */
	private final static Logger logger = LoggerFactory.getLogger(GoodsController.class);
	
	@Autowired
	private IAccountService accountService;
	
	@Autowired
	private IPermissionService permissionService;
	
	@Autowired
	private IGoodsService goodsService;
	
	@RequestMapping(value = "/goods/add_category1", method = {RequestMethod.POST})
	public @ResponseBody Result addCategory1(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "sequence", required = true) int sequence) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_GOODS)){
			return new Result("no_permission");
		}
		
		Result result = goodsService.addCategory1(userId, name, sequence);
		
		return result;
	}
	
	@RequestMapping(value = "/goods/update_category1", method = {RequestMethod.POST})
	public @ResponseBody Result updateCategory1(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "id", required = true) int id,
			@RequestParam(value = "name", required = true) String name, 
			@RequestParam(value = "sequence", required = true) int sequence) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_GOODS)){
			return new Result("no_permission");
		}
		
		Result result = goodsService.updateCategory1(userId, id, name, sequence);
		
		return result;
		
	}
	
	@RequestMapping(value="/goods/add_category2", method = {RequestMethod.POST})
	public @ResponseBody Result addCategory2(
			@RequestParam(value="userId", required = true) int userId, 
			@RequestParam(value = "name", required = true) String name, 
			@RequestParam(value = "sequence", required = true) int sequence, 
			@RequestParam(value = "category1Id", required = true) int category1Id) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_GOODS)){
			return new Result("no_permission");
		}
		
		Result result = goodsService.addCategory2(userId, name, sequence, category1Id);
		
		return result;
	}
	
	@RequestMapping(value="/goods/update_category2", method = {RequestMethod.POST})
	public @ResponseBody Result updateCategory2(
			@RequestParam(value="userId", required = true) int userId, 
			@RequestParam(value = "id", required = true) int id,
			@RequestParam(value = "name", required = true) String name, 
			@RequestParam(value = "sequence", required = true) int sequence, 
			@RequestParam(value = "category1Id", required = true) int category1Id) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_GOODS)){
			return new Result("no_permission");
		}
		
		
		Result result = goodsService.updateCategory2(userId, id, name, sequence, category1Id);
		
		return result;
	}
	
	@RequestMapping(value = "/goods/delete_category1", method = (RequestMethod.POST))
	public @ResponseBody Result deleteCategory1(
			@RequestParam(value="userId", required = true) int userId, 
			@RequestParam(value = "id", required = true) int id) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_GOODS)){
			return new Result("no_permission");
		}
		return goodsService.deleteCategory1(userId, id);
	}
	
	@RequestMapping(value = "/goods/delete_category2", method = (RequestMethod.POST))
	public @ResponseBody Result deleteCategory2(
			@RequestParam(value="userId", required = true) int userId, 
			@RequestParam(value = "id", required = true) int id) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_GOODS)){
			return new Result("no_permission");
		}
		return goodsService.deleteCategory2(userId, id);
	}
	
	@RequestMapping(value = "/goods/delete_goods", method = (RequestMethod.POST))
	public @ResponseBody Result deleteGoods(
			@RequestParam(value="userId", required = true) int userId, 
			@RequestParam(value = "id", required = true) int id) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_GOODS)){
			return new Result("no_permission");
		}
		return goodsService.deleteGoods(userId, id);
	}
	
	@RequestMapping(value="/goods/add_goods", method = {RequestMethod.POST})
	public @ResponseBody Result addGoods(
			@RequestParam(value="userId", required = true) int userId, 
			@RequestParam(value = "name", required = true) String name, 
			@RequestParam(value = "barcode", required = true) String barcode,
			@RequestParam(value = "buyPrice", required = true) double buyPrice,
			@RequestParam(value = "tradePrice", required = true) double tradePrice, 
			@RequestParam(value = "sellPrice", required = true) double sellPrice, 
			@RequestParam(value = "memberPrice", required = true) double memberPrice, 
			@RequestParam(value = "leftAmount", required = false, defaultValue = "0") int leftAmount, 
			@RequestParam(value = "description", required = false, defaultValue = "") String description,
			@RequestParam(value = "category2Id", required = true) int category2Id) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_GOODS)){
			return new Result("no_permission");
		}
		return goodsService.addGoods(userId, name, barcode, sellPrice, buyPrice, tradePrice, memberPrice, leftAmount, category2Id, description);
	}
	
	@RequestMapping(value="/goods/update_goods", method = {RequestMethod.POST})
	public @ResponseBody Result updateGoods(
			@RequestParam(value="userId", required = true) int userId,
			@RequestParam(value="id", required = true) int id,
			@RequestParam(value = "name", required = true) String name, 
			@RequestParam(value = "barcode", required = true) String barcode,
			@RequestParam(value = "buyPrice", required = true) double buyPrice,
			@RequestParam(value = "tradePrice", required = true) double tradePrice, 
			@RequestParam(value = "sellPrice", required = true) double sellPrice, 
			@RequestParam(value = "memberPrice", required = true) double memberPrice, 
			@RequestParam(value = "description", required = false, defaultValue = "") String description,
			@RequestParam(value = "category2Id", required = true) int category2Id) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_GOODS)){
			return new Result("no_permission");
		}
		return goodsService.updateGoods(userId, id, name, barcode, sellPrice, buyPrice, tradePrice, memberPrice, category2Id, description);
	}
	
	@RequestMapping(value="/goods/import_goods", method = {RequestMethod.POST})
	public @ResponseBody Result importGoods(
			@RequestParam(value="userId", required = true) int userId, 
			@RequestParam(value="id", required = true) int id,
			@RequestParam(value = "amount", required = true) int amount) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_GOODS)){
			return new Result("no_permission");
		}
		return goodsService.importGoods(userId, id, amount);
	}
	
	@RequestMapping(value="/goods/changeamount", method = {RequestMethod.POST})
	public @ResponseBody Result changeGoodsAmount(
			@RequestParam(value="userId", required = true) int userId, 
			@RequestParam(value="id", required = true) int id,
			@RequestParam(value = "amount", required = true) int newAmount) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_GOODS)){
			return new Result("no_permission");
		}
		return goodsService.changeGoodsAmount(userId, id, newAmount);
	}
	
	@RequestMapping(value="/goods/refund_goods", method = {RequestMethod.POST})
	public @ResponseBody Result refundGoods(
			@RequestParam(value="userId", required = true) int userId, 
			@RequestParam(value="id", required = true) int id,
			@RequestParam(value = "amount", required = true) int amount) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_GOODS)){
			return new Result("no_permission");
		}
		return goodsService.refundGoods(userId, id, amount);
	}
	
	@RequestMapping(value="/goods/querygoods", method = {RequestMethod.GET})
	public @ResponseBody ObjectListResult queryGoods() throws Exception{
		ObjectListResult result = goodsService.queryAllGoods();
		return result;
	}
	
	@RequestMapping(value="/goods/querygoodsbybarcode", method = {RequestMethod.POST})
	public @ResponseBody ObjectResult queryGoodsByBarcode(
			@RequestParam(value="barCode", required = true) String barcode) throws Exception{
		ObjectResult result = goodsService.queryGoodsByBarcode(barcode);
		return result;
	}
	
	@RequestMapping(value = "/goods/addpackagebind", method = {RequestMethod.POST})
	public @ResponseBody Result addPackageBind(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "bigPackageId", required = true) int bigPackageId,
			@RequestParam(value = "smallPackageId", required = true) int smallPackageId,
			@RequestParam(value = "rate", required = true) int rate) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_PACKAGEBIND)){
			return new Result("no_permission");
		}
		
		Result result = goodsService.addPackageBind(userId, bigPackageId, smallPackageId, rate);
		
		return result;
	}
	
	@RequestMapping(value = "/goods/updatepackagebind", method = {RequestMethod.POST})
	public @ResponseBody Result updatePackageBind(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "packageBindId", required = true) int packageBindId,
			@RequestParam(value = "bigPackageId", required = true) int bigPackageId,
			@RequestParam(value = "smallPackageId", required = true) int smallPackageId,
			@RequestParam(value = "rate", required = true) int rate) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_PACKAGEBIND)){
			return new Result("no_permission");
		}
		
		Result result = goodsService.updatePackageBind(userId, packageBindId, bigPackageId, smallPackageId, rate);
		
		return result;
		
	}
	
	@RequestMapping(value = "/goods/deletepackagebind", method = (RequestMethod.POST))
	public @ResponseBody Result deletePackageBind(
			@RequestParam(value="userId", required = true) int userId, 
			@RequestParam(value = "packageBindId", required = true) int packageBindId) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_PACKAGEBIND)){
			return new Result("no_permission");
		}
		return goodsService.deletePackageBind(userId, packageBindId);
	}
	
	@RequestMapping(value="/goods/querypackagebinds", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody ObjectListResult queryPackageBind(
			@RequestParam(value = "userId", required = true) int userId) throws Exception{
		ObjectListResult result = goodsService.queryPackageBind(userId);
		return result;
	}
	
	@RequestMapping(value = "promotion/addpromotion", method = {RequestMethod.POST})
	public @ResponseBody ObjectResult addPromotion(
			@RequestParam(value="userId", required = true) int userId, 
			@RequestParam(value="forbidMemberDiscount", required = true) boolean forbidMemberDiscount, 
			@RequestParam(value="objectAType", required = true) int objectAType, 
			@RequestParam(value="objectAId", required = true) int objectAId, 
			@RequestParam(value="objectAQuantity", required = true) int objectAQuantity, 
			@RequestParam(value="objectBType", required = false) int objectBType, 
			@RequestParam(value="objectBId", required = false) int objectBId, 
			@RequestParam(value="objectBQuantity", required = false) int objectBQuantity, 
			@RequestParam(value="rewardType", required = true) int rewardType, 
			@RequestParam(value="rewardValue", required = true) double rewardValue) throws Exception{
		return goodsService.addPromotion(userId, forbidMemberDiscount, objectAType, objectAId, objectAQuantity, objectBType, objectBId, objectBQuantity, rewardType, rewardValue);
	}
	
	@RequestMapping(value = "promotion/updatepromotion", method = {RequestMethod.POST})
	public @ResponseBody ObjectResult updatePromotion(
			@RequestParam(value="userId", required = true) int userId,
			@RequestParam(value="id", required = true) int id,
			@RequestParam(value="forbidMemberDiscount", required = true) boolean forbidMemberDiscount, 
			@RequestParam(value="objectAType", required = true) int objectAType, 
			@RequestParam(value="objectAId", required = true) int objectAId, 
			@RequestParam(value="objectAQuantity", required = true) int objectAQuantity, 
			@RequestParam(value="objectBType", required = false) int objectBType, 
			@RequestParam(value="objectBId", required = false) int objectBId, 
			@RequestParam(value="objectBQuantity", required = false) int objectBQuantity, 
			@RequestParam(value="rewardType", required = true) int rewardType, 
			@RequestParam(value="rewardValue", required = true) double rewardValue) throws Exception{
		return goodsService.updatePromotion(userId, id, forbidMemberDiscount, objectAType, objectAId, objectAQuantity, objectBType, objectBId, objectBQuantity, rewardType, rewardValue);
	}
	
	@RequestMapping(value = "promotion/queryallpromotion", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody ObjectListResult queryAllPromotion() throws Exception{
		return goodsService.queryAllPromotion();
	}
	
	@RequestMapping(value = "promotion/deletepromotion", method = {RequestMethod.POST})
	public @ResponseBody ObjectResult deletePromotion(
			@RequestParam(value="userId", required = true) int userId,
			@RequestParam(value="id", required = true) int id) throws Exception{
		return goodsService.deletePromotion(userId, id);
	}
}
