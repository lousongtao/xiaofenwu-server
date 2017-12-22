/**
 * Copyright 2012 - 2013 Fglinxun Studios, Inc.
 * All rights reserved.
 */
package com.shuishou.retailer.views;

public class Result {
	public static final String OK = "ok";
	public static final String FAIL = "FAIL";
	public static final String NOPERMISSION = "no_permission";
	public static final String INVALIDSESSION = "invalid_session";
	/**
	 * the result.
	 */
	public final String result;

	/**
	 * the constructor.
	 * 
	 * @param result
	 *            the result.
	 */
	public Result(String result) {
		this.result = result;
	}

}
