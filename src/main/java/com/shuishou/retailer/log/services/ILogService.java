/**
 * Copyright 2012 - 2013 Fglinxun Studios, Inc.
 * All rights reserved.
 */
package com.shuishou.retailer.log.services;


import java.util.Date;

import com.shuishou.retailer.account.models.UserData;
import com.shuishou.retailer.log.models.LogData;
import com.shuishou.retailer.views.ObjectListResult;


/**
 * @author zhing
 * the log service.
 */
public interface ILogService {

  /**
   * write log to database
   * @param user    operator object
   * @param type    log type
   * @param message log message
   * @return
   */
  LogData write(UserData user, String type, String message);
  
  /**
   * query log record
   * @param start       start number of the query
   * @param limit       limitation of count of record
   * @param username    
   * @param beginTime
   * @param endTime
   * @param type
   * @param message
   * @return
   */
  ObjectListResult queryLog(int start, int limit, String username, Date beginTime, Date endTime, String type, String message);
}
