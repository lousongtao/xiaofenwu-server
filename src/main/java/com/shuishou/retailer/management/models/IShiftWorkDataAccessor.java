package com.shuishou.retailer.management.models;

import java.util.Date;
import java.util.List;

public interface IShiftWorkDataAccessor {

	ShiftWork getLastShiftWork();
	
	ShiftWork getShiftWorkById(int shiftWorkId);
	
	List<ShiftWork> queryShiftWork(int start, int limit,String shiftName, Date startTime, Date endTime);
	int queryShiftWorkCount(int start, int limit,String shiftName, Date startTime, Date endTime);
	
	void insertShitWork(ShiftWork sw);
	
	void save(ShiftWork sw);
}
