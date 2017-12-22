package com.shuishou.retailer.common.models;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.shuishou.retailer.models.BaseDataAccessor;

@Repository
public class PayWayDataAccessor extends BaseDataAccessor implements IPayWayDataAccessor {

	@Override
	public List queryPayWays() {
		return sessionFactory.getCurrentSession().createQuery("from PayWay order by id").list();
	}

	@Override
	public Serializable insertPayWay(PayWay payWay) {
		return sessionFactory.getCurrentSession().save(payWay);
	}

	@Override
	public void updatePayWay(PayWay payWay) {
		sessionFactory.getCurrentSession().update(payWay);
	}

	@Override
	public void deletePayWay(PayWay payWay) {
		sessionFactory.getCurrentSession().delete(payWay);
	}

	@Override
	public PayWay getPayWayById(int id) {
		String hql = "from PayWay where id = "+ id;
		
		return (PayWay) sessionFactory.getCurrentSession().createQuery(hql).uniqueResult();
	}

}
