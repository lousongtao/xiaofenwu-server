package com.shuishou.retailer.goods.models;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

public interface ICategory1DataAccessor {
	public Session getSession();
	
	public Serializable save(Category1 category);
	
	public void update(Category1 category);
	
	public void delete(Category1 category);
	
	public Category1 getCategory1ById(int id);
	
	public List<Category1> getAllCategory1();
}
