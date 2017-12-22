package com.shuishou.retailer.common.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.shuishou.retailer.BaseController;
import com.shuishou.retailer.ConstantValue;
import com.shuishou.retailer.account.services.IAccountService;
import com.shuishou.retailer.account.services.IPermissionService;
import com.shuishou.retailer.common.services.ICommonService;
import com.shuishou.retailer.views.ObjectListResult;
import com.shuishou.retailer.views.ObjectResult;
import com.shuishou.retailer.views.Result;

@Controller
public class CommonController extends BaseController {

	@Autowired
	private ICommonService commonService;
	
	@Autowired
	private IAccountService accountService;
	
	@Autowired
	private IPermissionService permissionService;
	
	@RequestMapping(value="/common/testserverconnection", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody Result testConnection() throws Exception{
		return new Result(Result.OK);
	}
	
	@RequestMapping(value="/common/queryconfigmap", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody ObjectResult queryConfigMap() throws Exception{
		return commonService.queryConfigMap();
	}
	
	
	@RequestMapping(value="/common/saveopencashdrawercode", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody Result saveOpenCashdrawerCode(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value="oldCode", required = true) String oldCode,
			@RequestParam(value="code", required = true) String code) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_CHANGE_CONFIG)){
			return new Result("no_permission");
		}
		return commonService.saveOpenCashdrawerCode(userId,oldCode, code);
	}
	
	@RequestMapping(value="/common/getdiscounttemplates", method =  {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody ObjectListResult getDiscountTemplates() throws Exception{
		return commonService.getDiscountTemplates();
	}
	
	@RequestMapping(value="/common/adddiscounttemplate", method =  {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody Result saveDiscountTemplate(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value="name", required = true) String name,
			@RequestParam(value="rate", required = true) double rate) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_DISCOUNTTEMPLATE)){
			return new Result("no_permission");
		}
		return commonService.saveDiscountTemplate(userId, name, rate);
	}
	
	@RequestMapping(value="/common/deletediscounttemplate", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody Result deleteDiscountTemplate(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value="id", required = true) int id) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_DISCOUNTTEMPLATE)){
			return new Result("no_permission");
		}
		return commonService.deleteDiscountTemplate(userId,id);
	}
	
	@RequestMapping(value="/common/getpayways", method =  {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody ObjectListResult getPayWays() throws Exception{
		return commonService.getPayWays();
	}
	
	@RequestMapping(value="/common/addpayway", method =  {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody Result savePayWay(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value="name", required = true) String name) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_PAYWAY)){
			return new Result("no_permission");
		}
		return commonService.savePayWay(userId, name);
	}
	
	@RequestMapping(value="/common/deletepayway", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody Result deletePayWay(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value="id", required = true) int id) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_PAYWAY)){
			return new Result("no_permission");
		}
		return commonService.deletePayWay(userId,id);
	}
	
	@RequestMapping(value="/common/uploaderrorlog", method= {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody ObjectResult uploadErrorLog(
			@RequestParam(value = "machineCode", required = true) String machineCode,
			@RequestParam(value = "logfile", required = true) MultipartFile logfile) throws Exception{
		return commonService.uploadErrorLog(machineCode, logfile);
	}
}