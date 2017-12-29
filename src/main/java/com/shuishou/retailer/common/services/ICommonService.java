package com.shuishou.retailer.common.services;

import org.springframework.web.multipart.MultipartFile;

import com.shuishou.retailer.views.ObjectListResult;
import com.shuishou.retailer.views.ObjectResult;

public interface ICommonService {
	
	ObjectResult saveOpenCashdrawerCode(int userId, String oldCode, String code);
	
	ObjectResult saveBranchName(int userId, String branchName);
	
	ObjectResult saveMemberManagementWay(int userId, boolean byScore, boolean byDeposit, double scorePerDollar);
	
	ObjectListResult getDiscountTemplates();
	
	ObjectResult saveDiscountTemplate(int userId, String name, double rate);
	
	ObjectResult deleteDiscountTemplate(int userId, int id);
	
	ObjectListResult getPayWays();
	
	ObjectResult savePayWay(int userId, String name);
	
	ObjectResult deletePayWay(int userId, int id);
	
	ObjectResult uploadErrorLog(String machineCode, MultipartFile logfile);
	
	ObjectResult queryConfigMap();
}
