package com.shuishou.retailer.goods.models;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.shuishou.retailer.models.BaseDataAccessor;

@Repository
public class PromotionDataAccessor extends BaseDataAccessor implements IPromotionDataAccessor {

	

	@Override
	public Serializable save(Promotion promotion) {
		return sessionFactory.getCurrentSession().save(promotion);
	}

	@Override
	public void update(Promotion promotion) {
		sessionFactory.getCurrentSession().update(promotion);
	}

	@Override
	public void delete(Promotion promotion) {
		sessionFactory.getCurrentSession().delete(promotion);
	}

	@Override
	public Promotion getPromotionById(int id) {
		String hql = "from Promotion where id="+id;
		return (Promotion) sessionFactory.getCurrentSession().createQuery(hql)
				.setCacheable(true)
				.uniqueResult();
	}

	@Override
	public List<Promotion> getAllPromotion() {
		String hql = "from Promotion";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setCacheable(true)
				.list();
	}
}
