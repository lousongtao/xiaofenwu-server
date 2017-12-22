package com.shuishou.retailer.account.models;

import java.util.List;

import org.hibernate.Session;

public interface IPermissionDataAccessor {

	Session getSession();
	List<Permission> queryAllPermission();
}
