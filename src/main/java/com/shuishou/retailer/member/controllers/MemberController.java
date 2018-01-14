package com.shuishou.retailer.member.controllers;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shuishou.retailer.BaseController;
import com.shuishou.retailer.ConstantValue;
import com.shuishou.retailer.account.services.IPermissionService;
import com.shuishou.retailer.member.services.IMemberService;
import com.shuishou.retailer.views.ObjectListResult;
import com.shuishou.retailer.views.ObjectResult;

@Controller
public class MemberController extends BaseController {
	private Logger log = Logger.getLogger("MemberController");
	
	@Autowired
	private IPermissionService permissionService;
	
	@Autowired
	private IMemberService memberService;
	
	@RequestMapping(value = "/member/querymember", method = {RequestMethod.POST})
	public @ResponseBody ObjectListResult queryMember(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "memberCard", required = false, defaultValue = "") String memberCard,
			@RequestParam(value = "name", required = false, defaultValue = "") String name, 
			@RequestParam(value = "address", required = false, defaultValue = "") String address, 
			@RequestParam(value = "postCode", required = false, defaultValue = "") String postCode, 
			@RequestParam(value = "telephone", required = false, defaultValue = "") String telephone ) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_QUERY_MEMBER)){
			return new ObjectListResult("no_permission", false);
		}
		
		ObjectListResult result = memberService.queryMember(name, memberCard, address, postCode, telephone);
		
		return result;
		
	}
	
	@RequestMapping(value = "/member/queryallmember", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody ObjectListResult queryAllMember() throws Exception{
		@SuppressWarnings("rawtypes")
		ObjectListResult result = memberService.queryAllMember();
		
		return result;
		
	}
	
	@RequestMapping(value = "/member/addmember", method = {RequestMethod.POST})
	public @ResponseBody ObjectResult addMember(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "memberCard", required = true) String memberCard,
			@RequestParam(value = "name", required = true) String name, 
			@RequestParam(value = "address", required = false, defaultValue = "") String address, 
			@RequestParam(value = "postCode", required = false, defaultValue = "") String postCode, 
			@RequestParam(value = "telephone", required = false, defaultValue = "") String telephone,
			@RequestParam(value = "discountRate", required = true) double discountRate,
			@RequestParam(value = "birth", required = false, defaultValue = "") String sBirth) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_MEMBER)){
			return new ObjectResult("no_permission", false);
		}
		Date birth = null;
		if (sBirth != null && sBirth.length() > 0){
			birth = ConstantValue.DFYMD.parse(sBirth);
		}
		try{
			ObjectResult result = memberService.addMember(userId, name, memberCard, address, postCode, telephone, birth, discountRate);
		
			return result;
		} catch(Exception e){
			log.error(ConstantValue.DFYMDHMS.format(new Date()));
	        log.error("", e);
	        e.printStackTrace();
			return new ObjectResult(e.getMessage()+"\n"+e.getCause(), false);
		}
	}
	
	@RequestMapping(value = "/member/updatemember", method = {RequestMethod.POST})
	public @ResponseBody ObjectResult updateMember(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "id", required = true) int id,
			@RequestParam(value = "memberCard", required = true) String memberCard,
			@RequestParam(value = "name", required = true) String name, 
			@RequestParam(value = "address", required = false, defaultValue = "") String address, 
			@RequestParam(value = "postCode", required = false, defaultValue = "") String postCode, 
			@RequestParam(value = "telephone", required = false, defaultValue = "") String telephone,
			@RequestParam(value = "discountRate", required = false, defaultValue = "") double discountRate,
			@RequestParam(value = "birth", required = false, defaultValue = "") String sBirth) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_MEMBER)){
			return new ObjectResult("no_permission", false);
		}
		Date birth = null;
		if (sBirth != null && sBirth.length() > 0){
			birth = ConstantValue.DFYMD.parse(sBirth);
		}
		try{
			ObjectResult result = memberService.updateMember(userId, id, name, memberCard, address, postCode, telephone, birth, discountRate);
		
			return result;
		} catch(Exception e){
			log.error(ConstantValue.DFYMDHMS.format(new Date()));
	        log.error("", e);
	        e.printStackTrace();
			return new ObjectResult(e.getMessage()+"\n"+e.getCause(), false);
		}
	}
	
	@RequestMapping(value = "/member/updatememberscore", method = {RequestMethod.POST})
	public @ResponseBody ObjectResult updateMemberScore(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "id", required = true) int id,
			@RequestParam(value = "newScore", required = true) double newScore) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_MEMBERSCORE)){
			return new ObjectResult("no_permission", false);
		}
		ObjectResult result = memberService.updateMemberScore(userId, id, newScore);
		
		return result;
		
	}
	
	@RequestMapping(value = "/member/updatememberbalance", method = {RequestMethod.POST})
	public @ResponseBody ObjectResult updateMemberBalance(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "id", required = true) int id,
			@RequestParam(value = "newBalance", required = true) double newBalance) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_MEMBERBALANCE)){
			return new ObjectResult("no_permission", false);
		}
		ObjectResult result = memberService.updateMemberBalance(userId, id, newBalance);
		
		return result;
		
	}
}
