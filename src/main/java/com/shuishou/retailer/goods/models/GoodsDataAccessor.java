package com.shuishou.retailer.goods.models;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.shuishou.retailer.models.BaseDataAccessor;

@Repository
public class GoodsDataAccessor extends BaseDataAccessor implements IGoodsDataAccessor {
	@Override
	public Serializable save(Goods goods) {
		return sessionFactory.getCurrentSession().save(goods);
	}

	@Override
	public void update(Goods goods) {
		sessionFactory.getCurrentSession().update(goods);
	}

	@Override
	public void delete(Goods goods) {
		sessionFactory.getCurrentSession().delete(goods);
	}

	@Override
	public Goods getGoodsById(int id) {
		String hql = "from Goods where id = "+id;
		return (Goods) sessionFactory.getCurrentSession().createQuery(hql)
				.setCacheable(true)
				.uniqueResult();
	}

	@Override
	public List<Goods> getAllGoods() {
		String hql = "from Goods";
		return (List<Goods>)sessionFactory.getCurrentSession().createQuery(hql)
				.setCacheable(true)
				.list();
	}
	
	@Override
	public List<Goods> getGoodsByParent(int category1Id) {
		String hql = "from Goods where category1.id = "+category1Id;
		return (List<Goods>)sessionFactory.getCurrentSession().createQuery(hql)
				.setCacheable(true)
				.list();
	}

	@Override
	public Goods getGoodsByBarcode(String barcode) {
		String hql = "from Goods where barcode = '" + barcode +"'";
		return (Goods) sessionFactory.getCurrentSession().createQuery(hql)
				.setCacheable(true)
				.uniqueResult();
	}
	
}
