package com.shuishou.retailer.common.services;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaTray;
import javax.print.attribute.standard.PrinterName;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shuishou.retailer.ConstantValue;
import com.shuishou.retailer.account.models.IUserDataAccessor;
import com.shuishou.retailer.account.models.UserData;
import com.shuishou.retailer.common.models.Configs;
import com.shuishou.retailer.common.models.DiscountTemplate;
import com.shuishou.retailer.common.models.IConfigsDataAccessor;
import com.shuishou.retailer.common.models.IDiscountTemplateDataAccessor;
import com.shuishou.retailer.common.models.IPayWayDataAccessor;
import com.shuishou.retailer.common.models.PayWay;
import com.shuishou.retailer.goods.models.ICategory2DataAccessor;
import com.shuishou.retailer.log.models.LogData;
import com.shuishou.retailer.log.services.ILogService;
import com.shuishou.retailer.views.ObjectResult;
import com.shuishou.retailer.views.ObjectListResult;
import com.shuishou.retailer.views.Result;


@Service
public class CommonService implements ICommonService {
	private Logger logger = Logger.getLogger(CommonService.class);
	
	@Autowired
	private IConfigsDataAccessor configsDA;
	
	
	@Autowired
	private IUserDataAccessor userDA;
	
	@Autowired
	private ILogService logService;
	
	
	@Autowired
	private IDiscountTemplateDataAccessor discountTemplateDA;
	
	
	@Autowired
	private IPayWayDataAccessor payWayDA;
	
	@Autowired
	private ICategory2DataAccessor category2DA;
	
	@Autowired
	private HttpServletRequest request;
	
	
	@Override
	@Transactional
	public ObjectResult queryConfigMap(){
		List<Configs> configs = configsDA.queryConfigs();
		HashMap<String, String> maps = new HashMap<>();
		if (configs != null){
			for(Configs c : configs){
				maps.put(c.getName(), c.getValue());
			}
		}
		return new ObjectResult(Result.OK, true, maps);
	}
	
	@Override
	@Transactional
	public ObjectResult saveOpenCashdrawerCode(int userId, String oldCode, String code) {
		Configs c = configsDA.getConfigsByName(ConstantValue.CONFIGS_OPENCASHDRAWERCODE);
		if (c == null){
			c = new Configs();
			c.setName(ConstantValue.CONFIGS_OPENCASHDRAWERCODE);
		} else {
			if (!c.getValue().equals(oldCode)){
				return new ObjectResult("old code is wrong", false);
			}
		}
		c.setValue(code);
		configsDA.saveConfigs(c);
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CHANGE_CONFIG.toString(), "User "+ selfUser + " change open cashdrawer " + code);

		return new ObjectResult(Result.OK, true);
	}
	
	@Override
	@Transactional
	public ObjectResult saveBranchName(int userId, String branchName) {
		Configs c = configsDA.getConfigsByName(ConstantValue.CONFIGS_BRANCHNAME);
		if (c == null){
			c = new Configs();
			c.setName(ConstantValue.CONFIGS_BRANCHNAME);
		} 
		c.setValue(branchName);
		configsDA.saveConfigs(c);
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CHANGE_CONFIG.toString(), "User "+ selfUser + " change branch name " + branchName);

		return new ObjectResult(Result.OK, true);
	}
	
	@Override
	@Transactional
	public ObjectResult saveMemberManagementWay(int userId, boolean byScore, boolean byDeposit, double scorePerDollar) {
		Configs c = configsDA.getConfigsByName(ConstantValue.CONFIGS_MEMBERMGR_BYDEPOSIT);
		if (c == null){
			c = new Configs();
			c.setName(ConstantValue.CONFIGS_MEMBERMGR_BYDEPOSIT);
		}
		c.setValue(String.valueOf(byDeposit));
		configsDA.saveConfigs(c);
		
		c = configsDA.getConfigsByName(ConstantValue.CONFIGS_MEMBERMGR_BYSCORE);
		if (c == null){
			c = new Configs();
			c.setName(ConstantValue.CONFIGS_MEMBERMGR_BYSCORE);
		}
		c.setValue(String.valueOf(byScore));
		configsDA.saveConfigs(c);
		
		c = configsDA.getConfigsByName(ConstantValue.CONFIGS_MEMBERMGR_SCOREPERDOLLAR);
		if (c == null){
			c = new Configs();
			c.setName(ConstantValue.CONFIGS_MEMBERMGR_SCOREPERDOLLAR);
		}
		c.setValue(String.valueOf(scorePerDollar));
		configsDA.saveConfigs(c);
		
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CHANGE_CONFIG.toString(), "User "+ selfUser + " change member management way. byScore = " 
				+ byScore + ", byDeposit = " + byDeposit + ", scorePerDollar = "+ scorePerDollar);

		return new ObjectResult(Result.OK, true);
	}

	@Override
	@Transactional
	public ObjectListResult getDiscountTemplates() {
		List<DiscountTemplate> templates = discountTemplateDA.queryDiscountTemplates();
		return new ObjectListResult(Result.OK, true, templates);
	}

	@Override
	@Transactional
	public ObjectResult saveDiscountTemplate(int userId, String name, double value, int type) {
		DiscountTemplate t = new DiscountTemplate();
		t.setName(name);
		t.setValue(value);
		t.setType(type);
		discountTemplateDA.insertDiscountTemplate(t);
		
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CHANGE_DISCOUNTTEMPLATE.toString(), 
				"User "+ selfUser + " add discount template "+ name);

		return new ObjectResult(Result.OK, true);
	}

	@Override
	@Transactional
	public ObjectResult deleteDiscountTemplate(int userId, int id) {
		DiscountTemplate t = discountTemplateDA.getDiscountTemplateById(id);
		if (t == null)
			return new ObjectResult("No Discount Template found, id = "+ id, false);
		discountTemplateDA.deleteDiscountTemplate(t);
		
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CHANGE_DISCOUNTTEMPLATE.toString(), "User "+ selfUser + " delete discount template " + t.getName());

		return new ObjectResult(Result.OK, true);
	}

	@Override
	public ObjectResult uploadErrorLog(String machineCode, MultipartFile logfile) {
		String fileName = logfile.getOriginalFilename();
		String pathName = request.getSession().getServletContext().getRealPath("/")+"../" + ConstantValue.CATEGORY_ERRORLOG;
		File path = new File(pathName);
		if (!path.exists())
			path.mkdirs();
		File f = new File(pathName + "/" + fileName);
		try {
			logfile.transferTo(f);
		} catch (IllegalStateException | IOException e) {
			return new ObjectResult(Result.FAIL, false);
		}
		return new ObjectResult(Result.OK, true);
	}

	@Override
	@Transactional
	public ObjectListResult getPayWays() {
		List<PayWay> listPayWay = payWayDA.queryPayWays();
		return new ObjectListResult(Result.OK, true, listPayWay);
	}

	@Override
	@Transactional
	public ObjectResult addPayWay(int userId, String name, double rate, int sequence, String symbol) {
		PayWay payWay = new PayWay();
		payWay.setName(name);
		payWay.setRate(rate);
		payWay.setSequence(sequence);
		payWay.setSymbol(symbol);
		payWayDA.insertPayWay(payWay);
		
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CHANGE_PAYWAY.toString(), 
				"User "+ selfUser + " add pay way "+ name);

		return new ObjectResult(Result.OK, true, payWay);
	}

	@Override
	@Transactional
	public ObjectResult updatePayWay(int userId, int id, String name, double rate, int sequence, String symbol) {
		
		PayWay payWay = payWayDA.getPayWayById(id);
		if (payWay == null){
			return new ObjectResult("Cannot find PayWay by id "+ id, false);
		}
		payWay.setName(name);
		payWay.setRate(rate);
		payWay.setSequence(sequence);
		payWay.setSymbol(symbol);
		payWayDA.updatePayWay(payWay);
		
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CHANGE_PAYWAY.toString(), 
				"User "+ selfUser + " update pay way "+ name);

		return new ObjectResult(Result.OK, true, payWay);
	}
	
	@Override
	@Transactional
	public ObjectResult deletePayWay(int userId, int id) {
		PayWay payWay = payWayDA.getPayWayById(id);
		if (payWay == null)
			return new ObjectResult("No Payway found, id = "+ id, false);
		payWayDA.deletePayWay(payWay);
		
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.CHANGE_PAYWAY.toString(), "User "+ selfUser + " delete payway " + payWay.getName());

		return new ObjectResult(Result.OK, true);
	}

}
