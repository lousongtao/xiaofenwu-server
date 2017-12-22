package com.shuishou.retailer.common.models;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.shuishou.retailer.models.BaseDataAccessor;

@Repository
public class DiscountTemplateDataAccessor extends BaseDataAccessor implements IDiscountTemplateDataAccessor {

	@Override
	public List queryDiscountTemplates() {
		return sessionFactory.getCurrentSession().createQuery("from DiscountTemplate order by name").list();
	}

	@Override
	public Serializable insertDiscountTemplate(DiscountTemplate discountTemplate) {
		return sessionFactory.getCurrentSession().save(discountTemplate);
	}

	@Override
	public void updateDiscountTemplate(DiscountTemplate discountTemplate) {
		sessionFactory.getCurrentSession().update(discountTemplate);
	}

	@Override
	public void deleteDiscountTemplate(DiscountTemplate discountTemplate) {
		sessionFactory.getCurrentSession().delete(discountTemplate);
	}

	@Override
	public DiscountTemplate getDiscountTemplateById(int id) {
		String hql = "from DiscountTemplate where id = "+ id;
		
		return (DiscountTemplate) sessionFactory.getCurrentSession().createQuery(hql).uniqueResult();
	}

}
