package com.shuishou.retailer.common.services;

import org.springframework.web.multipart.MultipartFile;

import com.shuishou.retailer.views.ObjectListResult;
import com.shuishou.retailer.views.ObjectResult;

public interface ICommonService {
	
	ObjectResult saveOpenCashdrawerCode(long userId, String oldCode, String code);
	
	ObjectListResult getDiscountTemplates();
	
	ObjectResult saveDiscountTemplate(long userId, String name, double rate);
	
	ObjectResult deleteDiscountTemplate(long userId, int id);
	
	ObjectListResult getPayWays();
	
	ObjectResult savePayWay(long userId, String name);
	
	ObjectResult deletePayWay(long userId, int id);
	
	ObjectResult uploadErrorLog(String machineCode, MultipartFile logfile);
	
	ObjectResult queryConfigMap();
}
