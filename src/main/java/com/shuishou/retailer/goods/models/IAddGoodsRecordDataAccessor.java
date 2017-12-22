package com.shuishou.retailer.goods.models;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

public interface IAddGoodsRecordDataAccessor {

public Session getSession();
	
	public Serializable save(AddGoodsRecord record);
	
	public void update(AddGoodsRecord record);
	
	public void delete(AddGoodsRecord record);
	
	public AddGoodsRecord getAddGoodsRecordById(int id);
	
	public List<AddGoodsRecord> getAllAddGoodsRecords();
	
	public List<AddGoodsRecord> getAddGoodsRecordByGoodsId(int goodsId);
}
