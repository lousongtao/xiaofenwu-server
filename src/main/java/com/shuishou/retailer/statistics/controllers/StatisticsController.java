package com.shuishou.retailer.statistics.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shuishou.retailer.BaseController;
import com.shuishou.retailer.ConstantValue;
import com.shuishou.retailer.account.services.IPermissionService;
import com.shuishou.retailer.statistics.services.IStatisticsService;
import com.shuishou.retailer.views.ObjectResult;


@Controller
public class StatisticsController extends BaseController {
	@Autowired
	private IStatisticsService statisticsService;
	
	@Autowired
	private IPermissionService permissionService;
	
	@RequestMapping(value="/statistics/statistics", method = (RequestMethod.POST))
	public @ResponseBody ObjectResult statistics(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "startDate", required = true) String sStartDate,
			@RequestParam(value = "endDate", required = true) String sEndDate,
			@RequestParam(value = "statisticsDimension", required = true) int dimension,
			@RequestParam(value = "sellGranularity", required = false, defaultValue="0") int sellGranularity,
			@RequestParam(value = "sellByPeriod", required = false, defaultValue= "0") int sellByPeriod) throws Exception{
		if (!permissionService.checkPermission(userId, ConstantValue.PERMISSION_STATISTICS)){
			return new ObjectResult("no_permission", false);
		}
		Date startDate = ConstantValue.DFYMDHMS.parse(sStartDate);
		Date endDate = ConstantValue.DFYMDHMS.parse(sEndDate);
		return statisticsService.statistics(userId, startDate, endDate, dimension, sellGranularity, sellByPeriod);
	}
}
