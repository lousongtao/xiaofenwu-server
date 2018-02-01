/**
 * Copyright 2012 - 2013 Fglinxun Studios, Inc.
 * All rights reserved.
 */
package com.shuishou.retailer.log.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shuishou.retailer.ConstantValue;

/**
 * @author zhing the log data.
 */
@Entity
@Table(name = "log", indexes = { @Index(name = "idx_type", columnList = "type"),
		@Index(name = "idx_time", columnList = "time") })
public class LogData {

	public static enum LogType {
		ACCOUNT_LOGIN("ACCOUNT_LOGIN"), 
		ACCOUNT_LOGOUT("ACCOUNT_LOGOUT"), 
		ACCOUNT_ADD("ACCOUNT_ADD"), 
		ACCOUNT_MODIFY("ACCOUNT_MODIFY"), 
		ACCOUNT_DELETE("ACCOUNT_DELETE"),

		CATEGORY1_CHANGE("CATEGORY1_CHANGE"),
		CATEGORY2_CHANGE("CATEGORY2_CHANGE"),
		GOODS_CHANGE("GOODS_CHANGE"),
		GOODS_CHANGEAMOUNT("GOODS_CHANGEAMOUNT"),
		GOODS_IMPORT("GOODS_IMPORT"),
		PACKAGEBIND_CHANGE("PACKAGEBIND_CHANGE"),
		
		CHANGE_CONFIG("CHANGE_CONFIG"),
		
		MEMBER_CHANGE("MEMBER_CHANGE"),
		
		CHANGE_DISCOUNTTEMPLATE("CHANGE_DISCOUNTTEMPLATE"),
		CHANGE_PAYWAY("CHANGE_PAYWAY"),
		
		INDENT_MAKE("INDENT_CANCEL"),
		
		SHIFTWORK("SHIFTWORK"),
		
		;

		private String type;

		private LogType(String _type) {
			type = _type;
		}

		public String toString() {
			return type;
		}
	}

	/**
	 * the id.
	 */
	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private long id;

	/**
	 * the operator.
	 */
//	@ManyToOne(optional = true, fetch = FetchType.EAGER)
//	@JoinColumn(name = "user_id")
//	private UserData user;
	
	@Column
	private String userName;

	/**
	 * the type.
	 */
	@Column(name = "type", nullable = false)
	private String type;

	/**
	 * the time.
	 */
	@JsonFormat(pattern=ConstantValue.DATE_PATTERN_YMDHMS, timezone = "GMT+8:00")
	@Column(name = "time", nullable = false)
	private Date time = new Date();

	/**
	 * the message.
	 */
	@Column(name = "message", length = 4096)
	private String message;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

//	/**
//	 * @return the user
//	 */
//	public UserData getUser() {
//		return user;
//	}
//
//	/**
//	 * @param user
//	 *            the user to set
//	 */
//	public void setUser(UserData user) {
//		this.user = user;
//	}

	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
