package com.shuishou.retailer.goods.models;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

public interface ICategory2DataAccessor {

public Session getSession();
	
	public Serializable save(Category2 category);
	
	public void update(Category2 category);
	
	public void delete(Category2 category);
	
	public Category2 getCategory2ById(int id);
	
	public List<Category2> getAllCategory2();
	
	public List<Category2> getCategory2ByParent(int category1Id);
}
