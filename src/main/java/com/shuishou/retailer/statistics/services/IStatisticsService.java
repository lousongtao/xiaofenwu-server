package com.shuishou.retailer.statistics.services;

import java.util.Date;

import com.shuishou.retailer.views.ObjectResult;

public interface IStatisticsService {
	ObjectResult statistics(int userId, Date startDate, Date endDate, int dimension, int sellGranularity, int sellByPeriod);
}
