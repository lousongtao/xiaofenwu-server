package com.shuishou.retailer.management.views;

import com.shuishou.retailer.views.ObjectResult;

public class CurrentDutyResult extends ObjectResult {

	public CurrentDutyInfo data = new CurrentDutyInfo();
	
	public final static class CurrentDutyInfo{
		public String currentDutyName;
		public int currentDutyId;
		public String startTime;//当前值班人员开始时间
	}
	
	public CurrentDutyResult(String result, boolean success) {
		super(result, success);
	}

//	public CurrentDutyResult(String result, boolean success, String currentDuty, String startTime) {
//		super(result, success);
//		this.currentDuty = currentDuty;
//		this.startTime = startTime;
//	}
}
