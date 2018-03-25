package com.shuishou.retailer.goods.models;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

public interface IPromotionDataAccessor {
	public Session getSession();
	
	public Serializable save(Promotion promotion);
	
	public void update(Promotion promotion);
	
	public void delete(Promotion promotion);
	
	public Promotion getPromotionById(int id);
	
	public List<Promotion> getAllPromotion();
}
