package com.shuishou.retailer.goods.models;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.shuishou.retailer.models.BaseDataAccessor;
@Repository
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
	
	@Override
	public PackageBind getPackageBindBySmallGoodsId(int id){
		String hql = "from PackageBind pb where pb.smallPackage.id = "+id;
		List<PackageBind> pbs = sessionFactory.getCurrentSession().createQuery(hql).list();
		if (pbs == null || pbs.isEmpty()){
			return null;
		} else {
			return pbs.get(0);
		}
	}

}
