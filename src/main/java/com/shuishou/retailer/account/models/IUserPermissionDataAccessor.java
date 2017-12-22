package com.shuishou.retailer.account.models;

import java.io.Serializable;

import org.hibernate.Session;

public interface IUserPermissionDataAccessor {
	Session getSession();
	public Serializable save(UserPermission up);
	
	public void deleteByUserId(long userId);
}
