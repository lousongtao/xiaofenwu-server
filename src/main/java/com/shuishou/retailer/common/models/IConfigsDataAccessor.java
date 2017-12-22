package com.shuishou.retailer.common.models;

import java.io.Serializable;
import java.util.List;

public interface IConfigsDataAccessor {

	List<Configs> queryConfigs();
	
	Configs getConfigsById(int id);
	
	Configs getConfigsByName(String name);
	
	Serializable saveConfigs(Configs configs);
	
	void updateConfigs(Configs configs);
	
	void deleteConfigs(Configs configs);
}
