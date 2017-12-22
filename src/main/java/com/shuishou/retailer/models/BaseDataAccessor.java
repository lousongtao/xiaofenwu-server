/**
 * Copyright 2012 - 2013 Fglinxun Studios, Inc.
 * All rights reserved.
 */
package com.shuishou.retailer.models;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseDataAccessor {

	/**
	 * the hibernate session factory.
	 */
	@Autowired
	protected SessionFactory sessionFactory;

	/**
	 * @return the sessionFactory
	 */
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

}
