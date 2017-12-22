package com.shuishou.retailer.goods.models;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

public interface IGoodsDataAccessor {

public Session getSession();
	
	public Serializable save(Goods goods);
	
	public void update(Goods goods);
	
	public void delete(Goods goods);
	
	public Goods getGoodsById(int id);
	
	public List<Goods> getAllGoods();
	
	public List<Goods> getGoodsByParent(int category2Id);
}
