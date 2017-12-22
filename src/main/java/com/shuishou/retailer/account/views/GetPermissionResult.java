package com.shuishou.retailer.account.views;

import java.util.List;

import com.shuishou.retailer.views.ObjectResult;

public class GetPermissionResult extends ObjectResult{

	public final static class PermissionInfo{
		public String id;
		public String name;
		
		public PermissionInfo(String id, String name){
			this.id = id;
			this.name = name;
		}
	}
	
	public List<PermissionInfo> permissions;
	
	private GetPermissionResult(String result, boolean success, List<PermissionInfo> permissions) {
		super(result, success);
		this.permissions = permissions;
	}

}
