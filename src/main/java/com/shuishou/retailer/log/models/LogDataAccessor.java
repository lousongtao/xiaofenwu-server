/**
 * Copyright 2012 - 2013 Fglinxun Studios, Inc.
 * All rights reserved.
 */
package com.shuishou.retailer.log.models;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.shuishou.retailer.models.BaseDataAccessor;

/**
 * @author zhing the log data accessor.
 */
@Repository("logDA")
public class LogDataAccessor extends BaseDataAccessor implements ILogDataAccessor {

	@Override
	public void persistLog(LogData log) {
		sessionFactory.getCurrentSession().persist(log);
	}

	@Override
	public void updateLog(LogData log) {
		sessionFactory.getCurrentSession().update(log);
	}

	@Override
	public void deleteLog(LogData log) {
		sessionFactory.getCurrentSession().delete(log);
	}

	@Override
	public Serializable saveLog(LogData log) {
		return sessionFactory.getCurrentSession().save(log);
	}

	@Override
	public void saveOrUpdateLog(LogData log) {
		sessionFactory.getCurrentSession().saveOrUpdate(log);
	}

	@Override
	public LogData getLogById(long id) {
		return (LogData) sessionFactory.getCurrentSession().get(LogData.class, id);
	}

}
