/**
 * Copyright 2012 - 2013 Fglinxun Studios, Inc.
 * All rights reserved.
 */
package com.shuishou.retailer.log.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.shuishou.retailer.account.models.UserData;
import com.shuishou.retailer.log.models.ILogDataAccessor;
import com.shuishou.retailer.log.models.LogData;
import com.shuishou.retailer.views.ObjectListResult;
import com.shuishou.retailer.views.Result;

@Service("logService")
@Transactional(readOnly = true)
public class LogService implements ILogService {

	/**
	 * the log data accessor.
	 */
	@Autowired
	private ILogDataAccessor logDA;

	@Override
	@Transactional(readOnly = false)
	public LogData write(UserData user, String type, String message) {
		LogData log = new LogData();
		log.setUserName(user.getUsername());
		log.setType(type);
		log.setMessage(message);
		logDA.persistLog(log);

		return log;
	}

	@Override
	@SuppressWarnings("deprecation")
	public ObjectListResult queryLog(int start, int limit, String username, Date beginTime, Date endTime, String type,
			String message) {
		String countStmt = "select count(l) from LogData l";
		String listStmt = "select l from LogData l";
		List<String> condList = Lists.newArrayList();
		if (username != null && username.length() > 0) {
			listStmt += " inner join l.user u";
			countStmt += " inner join l.user u";
			condList.add("u.username='" + username + "'");
		}
		if (beginTime != null) {
			// condList.add("l.time >="+new java.sql.Date(beginTime.getTime()));
			condList.add("l.time >= :beginTime");
		}
		if (endTime != null) {
			// condList.add("l.time <="+new java.sql.Date(endTime.getTime()));
			condList.add("l.time <= :endTime");
		}
		if (type != null && type.length() > 0) {
			condList.add("l.type = '" + type + "'");
		}
		if (message != null && message.length() > 0) {
			condList.add("l.message like '%" + message + "%'");
		}
		for (int i = 0; i < condList.size(); i++) {
			countStmt += (i == 0 ? " where " : " and ") + condList.get(i);
			listStmt += (i == 0 ? " where " : " and ") + condList.get(i);
		}
		listStmt += " order by l.id desc";

		Query countQuery = this.logDA.getSession().createQuery(countStmt);
		Query listQuery = this.logDA.getSession().createQuery(listStmt);

		if (beginTime != null) {
			countQuery.setTimestamp("beginTime", beginTime);
			listQuery.setTimestamp("beginTime", beginTime);
		}
		if (endTime != null) {
			// set endTime as the next day, because system use 00:00:00 for this
			// day, but the customers' don't accept this thought
			endTime.setDate(endTime.getDate() + 1);
			countQuery.setTimestamp("endTime", endTime);
			listQuery.setTimestamp("endTime", endTime);
		}
		int count = (int) (long) countQuery.setCacheable(true).uniqueResult();
		if (count >= 300){
			return new ObjectListResult("Record is over 300, please change the filter", false, null, count);
		}

		@SuppressWarnings("unchecked")
		List<LogData> logs = listQuery.setFirstResult(start).setMaxResults(limit).setCacheable(true).list();
//		List<GetLogsResult.LogInfo> loginfos = new ArrayList<GetLogsResult.LogInfo>();
//		for (LogData log : logs) {
//			loginfos.add(new GetLogsResult.LogInfo(log.getId(), log.getUser().getId(), log.getUser().getUsername(),
//					log.getType(), log.getTime(), log.getMessage()));
//		}
		return new ObjectListResult(Result.OK, true, logs, count);
	}

}
