/**
 * Copyright 2012 - 2013 Fglinxun Studios, Inc.
 * All rights reserved.
 */
package com.shuishou.retailer.account.views;

public class LoginResult {

	/**
	 * the result.
	 */
	private final String result;

	/**
	 * the user id.
	 */
	private final String userId;
	
	private final String userName;


	/**
	 * constructor.
	 * 
	 * @param result
	 *            the result.
	 * @param userId
	 *            the user id.
	 * @param sessionId
	 *            the session id.
	 */
	public LoginResult(String result, String userId, String userName) {
		this.result = result;
		this.userId = userId;
		this.userName = userName;
	}

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}


	public String getUserName() {
		return userName;
	}

	
}
