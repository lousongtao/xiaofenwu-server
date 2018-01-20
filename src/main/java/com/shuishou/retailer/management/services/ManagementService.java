package com.shuishou.retailer.management.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shuishou.retailer.ConstantValue;
import com.shuishou.retailer.account.models.IUserDataAccessor;
import com.shuishou.retailer.account.models.UserData;
import com.shuishou.retailer.common.models.IPayWayDataAccessor;
import com.shuishou.retailer.common.models.PayWay;
import com.shuishou.retailer.indent.models.IIndentDataAccessor;
import com.shuishou.retailer.indent.models.Indent;
import com.shuishou.retailer.log.models.LogData;
import com.shuishou.retailer.log.services.ILogService;
import com.shuishou.retailer.management.models.IShiftWorkDataAccessor;
import com.shuishou.retailer.management.models.ShiftWork;
import com.shuishou.retailer.management.views.CurrentDutyResult;
import com.shuishou.retailer.printertool.PrintJob;
import com.shuishou.retailer.printertool.PrintQueue;
import com.shuishou.retailer.views.ObjectListResult;
import com.shuishou.retailer.views.ObjectResult;
import com.shuishou.retailer.views.Result;

@Service
public class ManagementService implements IManagementService{

	@Autowired
	private IShiftWorkDataAccessor shiftWorkDA;
	
	@Autowired
	private IUserDataAccessor userDA;
	
	@Autowired
	private ILogService logService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private IPayWayDataAccessor paywayDA;
	
	@Autowired
	private IIndentDataAccessor indentDA;
	
	
	@Override
	@Transactional
	public CurrentDutyResult getCurrentDuty() {
		ShiftWork sw = shiftWorkDA.getLastShiftWork();
		if (sw == null){
			return new CurrentDutyResult(Result.OK, true);
		} else {
			if (sw.getEndTime() != null){
				return new CurrentDutyResult(Result.OK, true);
			} else {
				CurrentDutyResult result = new CurrentDutyResult(Result.OK, true);
				result.data.currentDutyName = sw.getUserName();
				result.data.currentDutyId = sw.getUserId();
				result.data.startTime = ConstantValue.DFYMDHMS.format(sw.getStartTime());
				return result;
			}
		}
	}

	@Override
	@Transactional
	public ObjectListResult getShiftWorkList(int userId, int start, int limit, String shiftName, Date startTime, Date endTime) {
		List<ShiftWork> sws = shiftWorkDA.queryShiftWork(start, limit, shiftName, startTime, endTime);
		if (sws == null || sws.isEmpty())
			return new ObjectListResult(Result.OK, true);

		int count = shiftWorkDA.queryShiftWorkCount(start, limit, shiftName, startTime, endTime);
		if (count >= 300)
			return new ObjectListResult("Record is over 300, please change the filter", false, null, count);
		ObjectListResult result = new ObjectListResult(Result.OK, true, sws);
		return result;
	}

	@Override
	@Transactional
	public CurrentDutyResult startShiftWork(int userId, boolean printLastDutyTicket) {
		UserData user = userDA.getUserById(userId);
		if (user == null){
			return new CurrentDutyResult(Result.FAIL, false);
		}
		//firstly, load last duty record
		ShiftWork lastSW = shiftWorkDA.getLastShiftWork();
		if (lastSW != null && lastSW.getEndTime() == null){
			lastSW.setEndTime(new Date());
			shiftWorkDA.save(lastSW);
		}
		ShiftWork sw = new ShiftWork();
		sw.setUserName(user.getUsername());
		sw.setUserId(userId);
		sw.setStartTime(new Date());
		shiftWorkDA.insertShitWork(sw);
		logService.write(user, LogData.LogType.SHIFTWORK.toString(),
				"User " + user.getUsername() + " start work.");
		CurrentDutyResult result = new CurrentDutyResult(Result.OK, true);
		result.data.currentDutyId = (int)user.getId();
		result.data.currentDutyName = user.getUsername();
		result.data.startTime = ConstantValue.DFYMDHMS.format(sw.getStartTime());
			
		return result;
	}

	@Override
	@Transactional
	public CurrentDutyResult endShiftWork(int userId, boolean printShiftTicket) {
		UserData user = userDA.getUserById(userId);
		if (user == null){
			return new CurrentDutyResult("cannot find user by id "+ userId, false);
		}
		ShiftWork sw = shiftWorkDA.getLastShiftWork();
		if (sw.getUserId() != userId){
			return new CurrentDutyResult("Database record error. The last on duty record is not for the user, userId : "+ userId, false);
		}
		sw.setEndTime(new Date());
		shiftWorkDA.save(sw);
		logService.write(user, LogData.LogType.SHIFTWORK.toString(),
				"User " + user.getUsername() + " end work.");
		
		return new CurrentDutyResult(Result.OK, true);
	}

	@Override
	@Transactional
	public ObjectResult printShiftWork(int userId, int shiftWorkId) {
		UserData user = userDA.getUserById(userId);
		if (user == null){
			return new CurrentDutyResult("cannot find user by id "+ userId, false);
		}
		ShiftWork sw = shiftWorkDA.getShiftWorkById(shiftWorkId);
		if (sw == null){
			return new ObjectResult("cannot find shift work record by id "+ shiftWorkId, false);
		}
		Date starttime = sw.getStartTime();
		Date endtime = sw.getEndTime();
		if (endtime == null)
			endtime = new Date();
		List<Indent> indents = indentDA.getIndents(0, 10000, starttime, endtime, null, null, null, null, null);
		if (indents == null || indents.isEmpty())
			return new ObjectResult(Result.OK, true, null);
		
		long millsecs = (sw.getEndTime() == null ? new Date().getTime() : sw.getEndTime().getTime()) - sw.getStartTime().getTime();
		int hours = (int)(millsecs / (60*60*1000));
		int minutes = (int)((millsecs - hours * 60 * 60 * 1000)/(60*1000));
		int seconds = (int)((millsecs - hours * 60 * 60 * 1000 - minutes * 60 * 1000)/1000);
		String workPeriod = (hours > 0 ? hours+"h " : "") + minutes + "m " + seconds + "s";
		
		int indentAmount = 0;
		int goodsAmount = 0;
		double cashMoney = 0;
		double bankcardMoney = 0;
		double memberMoney = 0;
		double totalPrice = 0;
		double paidPrice = 0;
		HashMap<String, Double> mapOtherPay = new HashMap<>();//other pay way money
		
		return new ObjectResult(Result.OK, true);
	}
}
