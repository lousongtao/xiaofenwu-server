package com.shuishou.retailer.management.services;

import java.util.Date;

import com.shuishou.retailer.management.views.CurrentDutyResult;
import com.shuishou.retailer.views.ObjectListResult;
import com.shuishou.retailer.views.ObjectResult;
import com.shuishou.retailer.views.Result;

public interface IManagementService {

	CurrentDutyResult getCurrentDuty();
	
	ObjectListResult getShiftWorkList(int userId, int start, int limit, String shiftName, Date startTime, Date endTime);
	
	CurrentDutyResult startShiftWork(int userId, boolean printLastDutyTicket);
	
	CurrentDutyResult endShiftWork(int userId, boolean printShiftTicket);
	
	ObjectResult printShiftWork(int userId, int shiftWorkId);
}
