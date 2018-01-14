package com.shuishou.retailer.goods.models;

import java.io.Serializable;
import java.util.List;

import com.shuishou.retailer.models.BaseDataAccessor;

public class PackageBindDataAccessor extends BaseDataAccessor implements IPackageBindDataAccessor{

	@Override
	public Serializable save(PackageBind bind) {
		return sessionFactory.getCurrentSession().save(bind);
	}

	@Override
	public void update(PackageBind bind) {
		sessionFactory.getCurrentSession().update(bind);
	}

	@Override
	public void delete(PackageBind bind) {
		sessionFactory.getCurrentSession().delete(bind);
	}

	@Override
	public List<PackageBind> getAllPackageBinds() {
		String hql = "from PackageBind";
		return (List<PackageBind>)sessionFactory.getCurrentSession().createQuery(hql).list();
	}
	
	@Override
	public PackageBind getPackageBindById(int id){
		String hql = "from PackageBind where id = "+id;
		return (PackageBind) sessionFactory.getCurrentSession().createQuery(hql).uniqueResult();
	}

}
