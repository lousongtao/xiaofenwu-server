package com.shuishou.retailer.common.models;

import java.io.Serializable;
import java.util.List;

public interface IPayWayDataAccessor {

	List<PayWay> queryPayWays();
	
	PayWay getPayWayById(int id);
	
	Serializable insertPayWay(PayWay payWay);
	
	void updatePayWay(PayWay payWay);
	
	void deletePayWay(PayWay payWay);
}
