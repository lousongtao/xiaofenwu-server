/**
 * Copyright 2012 - 2013 Fglinxun Studios, Inc.
 * All rights reserved.
 */
package com.shuishou.retailer.views;

public class ObjectResult extends Result {

	/**
	 * success or not.
	 */
	public final boolean success;
	
	public Object data;

	/**
	 * the constructor.
	 * 
	 * @param result
	 *            the result.
	 * @param success
	 *            success or not.
	 */
	public ObjectResult(String result, boolean success) {
		super(result);
		this.success = success;
	}

	public ObjectResult(String result, boolean success, Object data) {
		super(result);
		this.success = success;
		this.data = data;
	}
}
