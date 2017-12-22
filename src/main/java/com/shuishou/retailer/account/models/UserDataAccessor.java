/**
 * Copyright 2012 - 2013 Fglinxun Studios, Inc.
 * All rights reserved.
 */
package com.shuishou.retailer.account.models;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.shuishou.retailer.models.BaseDataAccessor;

@Repository("userDA")
public class UserDataAccessor extends BaseDataAccessor implements IUserDataAccessor {

	@Override
	public void persistUser(UserData user) {
		sessionFactory.getCurrentSession().persist(user);
	}

	@Override
	public void updateUser(UserData user) {
		sessionFactory.getCurrentSession().update(user);
	}

	@Override
	public void deleteUser(UserData user) {
		sessionFactory.getCurrentSession().delete(user);
	}

	@Override
	public Serializable saveUser(UserData user) {
		return sessionFactory.getCurrentSession().save(user);
	}

	@Override
	public void saveOrUpdateUser(UserData user) {
		sessionFactory.getCurrentSession().saveOrUpdate(user);
	}

	@Override
	public UserData getUserById(long id) {
		return (UserData) sessionFactory.getCurrentSession().get(UserData.class, id);
	}

	@Override
	public UserData getUserByUsername(String username) {
//		UserData user = (UserData) sessionFactory.getCurrentSession().getNamedQuery("getUserByUsername")
//				.setParameter("username", username).setCacheable(true).setCacheRegion("Query.Account").uniqueResult();
		String hql = "from UserData where username='" + username+"'";
		UserData user = (UserData) sessionFactory.getCurrentSession().createQuery(hql).uniqueResult();
		return user;
	}

	@Override
	public List<UserData> getAllUsers() {
		@SuppressWarnings("unchecked")
		List<UserData> list = (List<UserData>) sessionFactory.getCurrentSession()
				.createQuery("select u from UserData u").setCacheable(true).setCacheRegion("Query.Account").list();
		return list;
	}

}
