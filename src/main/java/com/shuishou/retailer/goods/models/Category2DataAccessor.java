package com.shuishou.retailer.goods.models;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.shuishou.retailer.models.BaseDataAccessor;

@Repository
public class Category2DataAccessor extends BaseDataAccessor implements ICategory2DataAccessor {

	@Override
	public Serializable save(Category2 category) {
		return sessionFactory.getCurrentSession().save(category);
	}

	@Override
	public void update(Category2 category) {
		sessionFactory.getCurrentSession().update(category);
	}

	@Override
	public void delete(Category2 category) {
		sessionFactory.getCurrentSession().delete(category);
	}

	@Override
	public Category2 getCategory2ById(int id) {
		String hql = "from Category2 where id = "+id;
		return (Category2) sessionFactory.getCurrentSession().createQuery(hql).uniqueResult();
	}

	@Override
	public List<Category2> getAllCategory2() {
		String hql = "from Category2";
		return (List<Category2>)sessionFactory.getCurrentSession().createQuery(hql).list();
	}
	
	@Override
	public List<Category2> getCategory2ByParent(int category1Id) {
		String hql = "from Category2 where category1.id = "+category1Id;
		return (List<Category2>)sessionFactory.getCurrentSession().createQuery(hql).list();
	}
}
