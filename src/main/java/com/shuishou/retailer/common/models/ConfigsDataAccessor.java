package com.shuishou.retailer.common.models;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.shuishou.retailer.models.BaseDataAccessor;

@Repository
public class ConfigsDataAccessor extends BaseDataAccessor implements IConfigsDataAccessor {

	@Override
	public List queryConfigs() {
		return sessionFactory.getCurrentSession().createQuery("from Configs").list();
	}

	@Override
	public Serializable saveConfigs(Configs configs) {
		return sessionFactory.getCurrentSession().save(configs);
	}

	@Override
	public void updateConfigs(Configs configs) {
		sessionFactory.getCurrentSession().update(configs);
	}

	@Override
	public void deleteConfigs(Configs configs) {
		sessionFactory.getCurrentSession().delete(configs);
	}

	@Override
	public Configs getConfigsById(int id) {
		String hql = "from Configs where id = "+ id;
		
		return (Configs) sessionFactory.getCurrentSession().createQuery(hql).uniqueResult();
	}

	@Override
	public Configs getConfigsByName(String name) {
		String hql = "from Configs where name = '"+ name+"'";
		
		return (Configs) sessionFactory.getCurrentSession().createQuery(hql).uniqueResult();
	}
}
