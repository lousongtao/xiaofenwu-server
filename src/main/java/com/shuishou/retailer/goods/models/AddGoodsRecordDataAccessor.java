package com.shuishou.retailer.goods.models;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.shuishou.retailer.models.BaseDataAccessor;

@Repository
public class AddGoodsRecordDataAccessor extends BaseDataAccessor implements IAddGoodsRecordDataAccessor {

	@Override
	public Serializable save(AddGoodsRecord record) {
		return sessionFactory.getCurrentSession().save(record);
	}

	@Override
	public void update(AddGoodsRecord record) {
		sessionFactory.getCurrentSession().update(record);
	}

	@Override
	public void delete(AddGoodsRecord record) {
		sessionFactory.getCurrentSession().delete(record);
	}

	@Override
	public AddGoodsRecord getAddGoodsRecordById(int id) {
		String hql = "from AddGoodsRecord where id = "+id;
		return (AddGoodsRecord) sessionFactory.getCurrentSession().createQuery(hql).uniqueResult();
	}

	@Override
	public List<AddGoodsRecord> getAllAddGoodsRecords() {
		String hql = "from AddGoodsRecord";
		return (List<AddGoodsRecord>)sessionFactory.getCurrentSession().createQuery(hql).list();
	}
	
	@Override
	public List<AddGoodsRecord> getAddGoodsRecordByGoodsId(int goodsId) {
		String hql = "from AddGoodsRecord where goodsId = "+ goodsId;
		return (List<AddGoodsRecord>)sessionFactory.getCurrentSession().createQuery(hql).list();
	}
}
