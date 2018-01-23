package com.shuishou.retailer.indent.controllers;

import java.util.Date;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shuishou.retailer.BaseController;
import com.shuishou.retailer.ConstantValue;
import com.shuishou.retailer.DataCheckException;
import com.shuishou.retailer.account.services.IAccountService;
import com.shuishou.retailer.account.services.IPermissionService;
import com.shuishou.retailer.indent.services.IIndentService;
import com.shuishou.retailer.views.ObjectListResult;
import com.shuishou.retailer.views.ObjectResult;



@Controller
public class IndentController extends BaseController {

	@Autowired
	private IIndentService indentService;
	
	@Autowired
	private IAccountService accountService;
	
	@Autowired
	private IPermissionService permissionService;
	
	@RequestMapping(value="/indent/makeindent", method = (RequestMethod.POST))
	public @ResponseBody ObjectResult makeOrder(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value="indents", required = true) String indents,
			@RequestParam(value="payWay", required = true) String payWay,
			@RequestParam(value="paidPrice", required = true) double paidPrice,
			@RequestParam(value="member", required = false, defaultValue = "") String member) throws Exception{
		JSONArray jsonOrder = new JSONArray(indents);
		try{
			ObjectResult result = indentService.saveIndent(userId, jsonOrder, payWay, paidPrice, member);
			return result;
		} catch(DataCheckException ex){
			return new ObjectResult(ex.getMessage(), false);
		}
	}
	
	@RequestMapping(value="/indent/refundindent", method = (RequestMethod.POST))
	public @ResponseBody ObjectResult refundOrder(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value="indents", required = true) String indents,
//			@RequestParam(value="refundPrice", required = true) double refundPrice,
			@RequestParam(value="returnToStorage", required = true) boolean returnToStorage,
			@RequestParam(value="member", required = false, defaultValue = "") String member) throws Exception{
		JSONArray jsonOrder = new JSONArray(indents);
		try{
			return indentService.refundIndent(userId, jsonOrder, member, returnToStorage);
		} catch(DataCheckException ex){
			return new ObjectResult(ex.getMessage(), false);
		}
	}
	
	@RequestMapping(value="/indent/prebuyindent", method = (RequestMethod.POST))
	public @ResponseBody ObjectResult prebuyOrder(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value="indents", required = true) String indents,
			@RequestParam(value="payWay", required = false, defaultValue = "") String payWay,
			@RequestParam(value="paidPrice", required = true) double paidPrice,
			@RequestParam(value="paid", required = true) boolean paid,
			@RequestParam(value="member", required = false, defaultValue = "") String member) throws Exception{
		JSONArray jsonOrder = new JSONArray(indents);
		
		return indentService.prebuyIndent(userId, jsonOrder, payWay, paidPrice, member, paid);
	}
	
	/**
	 * 
	 * @param pageStr
	 * @param startStr
	 * @param limitStr : java UI is not good to develop table page bar, so set this value is 300. if records are more, warn operator to set more filter
	 * @param starttime : just compare the indent's starttime
	 * @param endtime : just compare the indent's starttime
	 * @param orderby
	 * @param orderbydesc
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/indent/queryindent", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody ObjectListResult queryIndent(
			@RequestParam(value = "page", required = false, defaultValue = "0") String pageStr,
			@RequestParam(value = "start", required = false, defaultValue = "0") String startStr,
			@RequestParam(value = "limit", required = false, defaultValue = "300") String limitStr,
			@RequestParam(value="starttime", required = false) String starttime,
			@RequestParam(value="endtime", required = false) String endtime,
			@RequestParam(value="payway", required = false) String payway,
			@RequestParam(value="member", required = false) String member,
			@RequestParam(value="type", required = false) String sType,
			@RequestParam(value="indentCode", required = false) String indentCode,
			@RequestParam(value="orderby", required = false) String orderby,
			@RequestParam(value="orderbydesc", required = false) String orderbydesc) throws Exception{
		
		int page = Integer.parseInt(pageStr);
		int start = Integer.parseInt(startStr);
		int limit = Integer.parseInt(limitStr);
		Integer[] types = null;
		if ("ORDER".equals(sType)){
			types = new Integer[]{ConstantValue.INDENT_TYPE_ORDER};
		} else if ("REFUND".equals(sType)){
			types = new Integer[]{ConstantValue.INDENT_TYPE_REFUND};
		} else if ("PREORDER".equals(sType)){
			types = new Integer[]{ConstantValue.INDENT_TYPE_PREBUY_PAID, ConstantValue.INDENT_TYPE_PREBUY_UNPAID};
		}
		return indentService.queryIndent(start, limit, starttime, endtime, payway, member, indentCode, types, orderby,orderbydesc);
	}
	
	@RequestMapping(value="/indent/queryindentforshiftwork", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody ObjectListResult queryIndentForShiftwork(
			@RequestParam(value="shiftworkId", required = true) int shiftworkId) throws Exception{
		
		return indentService.queryIndentForShiftwork(shiftworkId);
	}
	
	@RequestMapping(value="/indent/queryprebuyindent", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody ObjectListResult queryPrebuyIndent(
			@RequestParam(value = "page", required = false, defaultValue = "0") String pageStr,
			@RequestParam(value = "start", required = false, defaultValue = "0") String startStr,
			@RequestParam(value = "limit", required = false, defaultValue = "300") String limitStr,
			@RequestParam(value="starttime", required = false) String starttime,
			@RequestParam(value="endtime", required = false) String endtime,
			@RequestParam(value="member", required = false) String member) throws Exception{
		
		int page = Integer.parseInt(pageStr);
		int start = Integer.parseInt(startStr);
		int limit = Integer.parseInt(limitStr);
		return indentService.queryPrebuyIndent(start, limit, starttime, endtime, member);
	}
	
	@RequestMapping(value="/indent/querygoodssoldrecord", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody ObjectListResult queryGoodsSoldRecord(
			@RequestParam(value="goodsId", required = true) int goodsId,
			@RequestParam(value="starttime", required = false) String starttime,
			@RequestParam(value="endtime", required = false) String endtime,
			@RequestParam(value="payway", required = false) String payway,
			@RequestParam(value="member", required = false) String member) throws Exception{
		return indentService.queryGoodsSoldRecord(goodsId, starttime, endtime, payway, member);
	}
	
	@RequestMapping(value="/indent/changepreordertoorder", method = (RequestMethod.POST))
	public @ResponseBody ObjectResult changePreOrderToOrder(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value="indentId", required = true) int indentId) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_ORDER)){
			return new ObjectResult("no_permission", false);
		}
		try{
			ObjectResult result = indentService.changePreOrderToOrder(userId, indentId);
			return result;
		} catch(DataCheckException ex){
			return new ObjectResult(ex.getMessage(), false);
		}
					
	}
	
	@RequestMapping(value="/indent/deletepreorder", method = (RequestMethod.POST))
	public @ResponseBody ObjectResult deletePreOrder(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value="indentId", required = true) int indentId) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_ORDER)){
			return new ObjectResult("no_permission", false);
		}
		ObjectResult result = indentService.deletePreOrder(userId, indentId);
		return result;
					
	}
	
	@RequestMapping(value="/indent/printindent", method = (RequestMethod.POST))
	public @ResponseBody ObjectResult printIndent(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value="indentId", required = true) int indentId) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_QUERY_ORDER)){
			return new ObjectResult("no_permission", false);
		}
		return indentService.printIndent(userId, indentId);
	}
	
}
