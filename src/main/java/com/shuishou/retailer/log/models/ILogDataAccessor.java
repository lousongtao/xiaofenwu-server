/**
 * Copyright 2012 - 2013 Fglinxun Studios, Inc.
 * All rights reserved.
 */
package com.shuishou.retailer.log.models;

import java.io.Serializable;

import org.hibernate.Session;

public interface ILogDataAccessor {
  
  Session getSession();
  
  void persistLog(LogData log);

  void updateLog(LogData log);

  void deleteLog(LogData log);

  Serializable saveLog(LogData log);

  void saveOrUpdateLog(LogData log);

  LogData getLogById(long id);

}
