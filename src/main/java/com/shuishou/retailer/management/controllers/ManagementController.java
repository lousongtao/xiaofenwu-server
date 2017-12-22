package com.shuishou.retailer.management.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.shuishou.retailer.account.services.IAccountService;
import com.shuishou.retailer.account.services.IPermissionService;
import com.shuishou.retailer.account.views.GetAccountsResult;
import com.shuishou.retailer.management.services.IManagementService;
import com.shuishou.retailer.management.views.CurrentDutyResult;
import com.shuishou.retailer.views.ObjectListResult;
import com.shuishou.retailer.views.ObjectResult;
import com.shuishou.retailer.views.Result;

@Controller
public class ManagementController extends BaseController {
	private Logger log = Logger.getLogger("ManagementController");

	@Autowired
	private IManagementService managementService;
	
	@Autowired
	private IAccountService accountService;
	
	@Autowired
	private IPermissionService permissionService;
	
	@RequestMapping(value="/management/getcurrentduty", method = (RequestMethod.GET))
	public @ResponseBody CurrentDutyResult getCurrentDuty() throws Exception{
		return managementService.getCurrentDuty();
	}
	
	@RequestMapping(value="/management/printshiftwork", method = (RequestMethod.POST))
	public @ResponseBody ObjectResult printShiftWork(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "shiftWorkId", required = true) int shiftWorkId) throws Exception{
		return managementService.printShiftWork(userId, shiftWorkId);
	}
	
	@RequestMapping(value="/management/getshiftwork", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody ObjectListResult getShiftWork(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "start", required = false, defaultValue = "0") int start,
			@RequestParam(value = "limit", required = false, defaultValue = "300") int limit,
			@RequestParam(value = "userName", required = false) String userName,
			@RequestParam(value = "startTime", required = false) String sStartTime,
			@RequestParam(value = "endTime", required = false) String sEndTime) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_QUERY_SHIFTWORK)){
			return new ObjectListResult("no_permission", false);
		}
		Date startTime = null;
		Date endTime = null;
		if (sStartTime != null && sStartTime.length() > 0){
			try {
				startTime = ConstantValue.DFYMD.parse(sStartTime);
			} catch (ParseException e) {
				log.error(e.getMessage());
			}
		}
		if (sEndTime != null && sEndTime.length() > 0){
			try {
				endTime = ConstantValue.DFYMD.parse(sEndTime);
			} catch (ParseException e) {
				log.error(e.getMessage());
			}
		}
		return managementService.getShiftWorkList(userId, start, limit, userName, startTime, endTime);
	}
	
	@RequestMapping(value="/management/endshiftwork", method = (RequestMethod.POST))
	public @ResponseBody CurrentDutyResult endWork(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "printShiftTicket", required = true) boolean printShiftTicket) throws Exception{
		return managementService.endShiftWork(userId, printShiftTicket);
	}
	
	@RequestMapping(value="/management/startshiftwork", method = (RequestMethod.POST))
	public @ResponseBody CurrentDutyResult startWork(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "printLastDutyTicket", required = true) boolean printLastDutyTicket) throws Exception{
		return managementService.startShiftWork(userId, printLastDutyTicket);
	}
}
