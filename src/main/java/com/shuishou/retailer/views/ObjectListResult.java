/**
 * Copyright 2012 - 2013 Fglinxun Studios, Inc.
 * All rights reserved.
 */
package com.shuishou.retailer.views;

import java.util.List;

public class ObjectListResult<T> extends Result {

	/**
	 * success or not.
	 */
	public final boolean success;
	private int count;
	public List<T> data;

	/**
	 * the constructor.
	 * 
	 * @param result
	 *            the result.
	 * @param success
	 *            success or not.
	 */
	public ObjectListResult(String result, boolean success) {
		super(result);
		this.success = success;
	}

	public ObjectListResult(String result, boolean success, List<T> data) {
		super(result);
		this.success = success;
		this.data = data;
	}
	
	public ObjectListResult(String result, boolean success, List<T> data, int count) {
		super(result);
		this.success = success;
		this.data = data;
		this.count = count;
	}
}
