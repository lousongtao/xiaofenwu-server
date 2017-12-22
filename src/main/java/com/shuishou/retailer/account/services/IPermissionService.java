package com.shuishou.retailer.account.services;

import com.shuishou.retailer.views.ObjectListResult;

public interface IPermissionService {
	public boolean checkPermission(long userId, String permission);
	
	public ObjectListResult queryAllPermissions();
}
