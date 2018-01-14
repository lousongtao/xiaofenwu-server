package com.shuishou.retailer.goods.models;

import java.io.Serializable;
import java.util.List;

public interface IPackageBindDataAccessor {

	public Serializable save(PackageBind bind);
	
	public void update(PackageBind bind);
	
	public void delete(PackageBind bind);
	
	public PackageBind getPackageBindById(int id);
	
	public List<PackageBind> getAllPackageBinds();
}
