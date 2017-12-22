/**
 * Copyright 2012 - 2013 Fglinxun Studios, Inc.
 * All rights reserved.
 */
package com.shuishou.retailer.account.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Index;
import javax.persistence.JoinColumn;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


@NamedQueries({
		@NamedQuery(name = "getUserByUsername", query = "select u from UserData u where u.username = :username"), })
@Entity
@Table(name = "user", indexes = { @Index(name = "username_idx", columnList = "username") })
public class UserData {

	/**
	 * the user id.
	 */
	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private long id;

	/**
	 * the user name.
	 */
	@Column(name = "username", unique = true, nullable = false)
	private String username;

	/**
	 * the hashed password.
	 */
	@Column(name = "hashed_password", length = 40, nullable = false)
	private String hashedPassword;

	@OneToMany(cascade={CascadeType.ALL}, mappedBy="user")
//	@JoinColumn(name="user_id")
	public List<UserPermission> permissions;

	/*
	 * (non-Javadoc)
	 * 
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%d|%s", id, username);
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the hashedPassword
	 */
	public String getHashedPassword() {
		return hashedPassword;
	}

	/**
	 * @param hashedPassword
	 *            the hashedPassword to set
	 */
	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public List<UserPermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<UserPermission> permissions) {
		this.permissions = permissions;
	}

}
