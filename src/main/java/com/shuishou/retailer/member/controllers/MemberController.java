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
import com.shuishou.retailer.ServerProperties;
import com.shuishou.retailer.account.services.IPermissionService;
import com.shuishou.retailer.member.services.IMemberCloudService;
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
	
	@Autowired
	private IMemberCloudService memberCloudService;
	
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
		if (ServerProperties.MEMBERLOCATION_LOCAL.equals(ServerProperties.MEMBERLOCATION)){
			return memberService.queryMember(name, memberCard, address, postCode, telephone);
		} else {
			return memberCloudService.queryMember(name, memberCard, address, postCode, telephone);
		}
	}
	
	@RequestMapping(value = "/member/queryallmember", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody ObjectListResult queryAllMember() throws Exception{
		if (ServerProperties.MEMBERLOCATION_LOCAL.equals(ServerProperties.MEMBERLOCATION)){
			return memberService.queryAllMember();
		} else {
			return memberCloudService.queryAllMember();
		}
	}
	
	@RequestMapping(value = "/member/querymemberscore", method = {RequestMethod.POST})
	public @ResponseBody ObjectListResult queryMemberScore(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "memberId", required = true) int memberId) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_QUERY_MEMBER)){
			return new ObjectListResult("no_permission", false);
		}
		if (ServerProperties.MEMBERLOCATION_LOCAL.equals(ServerProperties.MEMBERLOCATION)){
			return memberService.queryMemberScore(memberId);
		} else {
			return memberCloudService.queryMemberScore(memberId);
		}
	}
	
	@RequestMapping(value = "/member/querymemberbalance", method = {RequestMethod.POST})
	public @ResponseBody ObjectListResult queryMemberBalance(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "memberId", required = true) int memberId) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_QUERY_MEMBER)){
			return new ObjectListResult("no_permission", false);
		}
		if (ServerProperties.MEMBERLOCATION_LOCAL.equals(ServerProperties.MEMBERLOCATION)){
			return memberService.queryMemberBalance(memberId);
		} else {
			return memberCloudService.queryMemberBalance(memberId);
		}
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
			if (ServerProperties.MEMBERLOCATION_LOCAL.equals(ServerProperties.MEMBERLOCATION)){
				return memberService.addMember(userId, name, memberCard, address, postCode, telephone, birth, discountRate);
			} else {
				return memberCloudService.addMember(userId, name, memberCard, address, postCode, telephone, birth, discountRate);
			}
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
			if (ServerProperties.MEMBERLOCATION_LOCAL.equals(ServerProperties.MEMBERLOCATION)){
				return memberService.updateMember(userId, id, name, memberCard, address, postCode, telephone, birth, discountRate);
			} else {
				return memberCloudService.updateMember(userId, id, name, memberCard, address, postCode, telephone, birth, discountRate);
			}
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
		if (ServerProperties.MEMBERLOCATION_LOCAL.equals(ServerProperties.MEMBERLOCATION)){
			return memberService.updateMemberScore(userId, id, newScore);
		} else {
			return memberCloudService.updateMemberScore(userId, id, newScore);
		}
	}
	
	@RequestMapping(value = "/member/updatememberbalance", method = {RequestMethod.POST})
	public @ResponseBody ObjectResult updateMemberBalance(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "id", required = true) int id,
			@RequestParam(value = "newBalance", required = true) double newBalance) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_MEMBERBALANCE)){
			return new ObjectResult("no_permission", false);
		}
		if (ServerProperties.MEMBERLOCATION_LOCAL.equals(ServerProperties.MEMBERLOCATION)){
			return memberService.updateMemberBalance(userId, id, newBalance);
		} else {
			return memberCloudService.updateMemberBalance(userId, id, newBalance);
		}
	}
	
	@RequestMapping(value = "/member/memberrecharge", method = {RequestMethod.POST})
	public @ResponseBody ObjectResult memberRecharge(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "id", required = true) int id,
			@RequestParam(value = "rechargeValue", required = true) double rechargeValue) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_MEMBERBALANCE)){
			return new ObjectResult("no_permission", false);
		}
		if (ServerProperties.MEMBERLOCATION_LOCAL.equals(ServerProperties.MEMBERLOCATION)){
			return memberService.memberRecharge(userId, id, rechargeValue);
		} else {
			return memberCloudService.memberRecharge(userId, id, rechargeValue);
		}
	}

	@RequestMapping(value = "/member/addmemberupgrade", method = {RequestMethod.POST})
	public @ResponseBody ObjectResult addMemberUpgrade(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "compareField", required = true) String compareField,
			@RequestParam(value = "smallRelation", required = true) int smallRelation,
			@RequestParam(value = "bigRelation", required = true) int bigRelation,
			@RequestParam(value = "executeField", required = true) String executeField,
			@RequestParam(value = "smallValue", required = true) double smallValue,
			@RequestParam(value = "bigValue", required = true) double bigValue,
			@RequestParam(value = "executeValue", required = true) double executeValue) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_MEMBERUPGRADE)){
			return new ObjectResult("no_permission", false);
		}
		return memberService.addMemberUpgrade(userId, compareField, smallValue, smallRelation, bigValue, bigRelation, executeField, executeValue);
	}
	
	@RequestMapping(value = "/member/updatememberupgrade", method = {RequestMethod.POST})
	public @ResponseBody ObjectResult updateMemberUpgrade(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "id", required = true) int id,
			@RequestParam(value = "compareField", required = true) String compareField,
			@RequestParam(value = "smallRelation", required = true) int smallRelation,
			@RequestParam(value = "bigRelation", required = true) int bigRelation,
			@RequestParam(value = "executeField", required = true) String executeField,
			@RequestParam(value = "smallValue", required = true) double smallValue,
			@RequestParam(value = "bigValue", required = true) double bigValue,
			@RequestParam(value = "executeValue", required = true) double executeValue) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_MEMBERUPGRADE)){
			return new ObjectResult("no_permission", false);
		}
		return memberService.updateMemberUpgrade(userId, id, compareField, smallValue, smallRelation, bigValue, bigRelation, executeField, executeValue);
	}
	
	@RequestMapping(value = "/member/querymemberupgrade", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody ObjectListResult queryMemberUpgrade() throws Exception{
		return memberService.queryMemberUpgrade();
	}
	
	@RequestMapping(value = "/member/deletememberupgrade", method = {RequestMethod.POST})
	public @ResponseBody ObjectResult deleteMemberUpgrade(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "id", required = true) int id) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_MEMBERUPGRADE)){
			return new ObjectResult("no_permission", false);
		}
		return memberService.deleteMemberUpgrade(userId, id);
	}
	
	@RequestMapping(value = "/member/changestatusmemberupgrade", method = {RequestMethod.POST})
	public @ResponseBody ObjectResult changeMemberUpgrade(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "id", required = true) int id,
			@RequestParam(value = "status", required = true) int status) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_UPDATE_MEMBERUPGRADE)){
			return new ObjectResult("no_permission", false);
		}
		return memberService.changeStatusMemberUpgrade(userId, id, status);
	}
}
