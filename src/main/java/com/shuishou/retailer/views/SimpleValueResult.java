package com.shuishou.retailer.views;

public class SimpleValueResult extends ObjectResult {

	public String data;
	public SimpleValueResult(String result, boolean success, String simpleValue) {
		super(result, success);
		data = simpleValue;
	}

}
