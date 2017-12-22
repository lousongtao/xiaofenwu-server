package com.shuishou.retailer.account.models;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.shuishou.retailer.models.BaseDataAccessor;

@Repository
public class PermissionDataAccessor extends BaseDataAccessor implements IPermissionDataAccessor {

	@Override
	public List<Permission> queryAllPermission() {
		String hql = "from Permission";
		return (List<Permission>)sessionFactory.getCurrentSession().createQuery(hql).list();
	}

}
