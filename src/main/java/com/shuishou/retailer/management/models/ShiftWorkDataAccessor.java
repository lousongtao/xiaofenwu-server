package com.shuishou.retailer.management.models;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.shuishou.retailer.models.BaseDataAccessor;

@Repository
public class ShiftWorkDataAccessor extends BaseDataAccessor implements IShiftWorkDataAccessor {

	@Override
	public ShiftWork getLastShiftWork() {
		String hql = "from ShiftWork l where l.id = (select max(t.id) from ShiftWork t)";
		return (ShiftWork) sessionFactory.getCurrentSession().createQuery(hql).uniqueResult();
	}
	
	public void insertShitWork(ShiftWork sw){
		sessionFactory.getCurrentSession().save(sw);
	}
	
	public void save(ShiftWork sw){
		sessionFactory.getCurrentSession().save(sw);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ShiftWork> queryShiftWork(int start, int limit, String shiftName, Date startTime, Date endTime) {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ShiftWork.class);
		if (shiftName != null){
			c.add(Restrictions.ilike("userName", "%"+shiftName+"%"));
		}
		if (startTime != null){
			c.add(Restrictions.ge("endTime", startTime));
		}
		if (endTime != null){
			c.add(Restrictions.le("startTime", endTime));
		}
		c.addOrder(Order.asc("id"));
		c.setFirstResult(start);
		c.setMaxResults(limit);
		return (List<ShiftWork>)c.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int queryShiftWorkCount(int start, int limit, String userName, Date startTime, Date endTime) {
		String countStmt = "select count(l) from ShiftWork l";
		List<String> condList = Lists.newArrayList();
		if (startTime != null){
			condList.add("l.startTime >= :starttime");
		}
		if (endTime != null){
			condList.add("l.startTime <= :endtime");
		}
		if (userName != null && userName.length() > 0){
			condList.add("l.userName = :userName");
		}
		for (int i = 0; i < condList.size(); i++) {
			countStmt += (i == 0 ? " where " : " and ") + condList.get(i);
		}
		Query query = sessionFactory.getCurrentSession().createQuery(countStmt);
		if (startTime != null){
			query.setTimestamp("starttime", startTime);
		}
		if (endTime != null){
			query.setTimestamp("endtime", endTime);
		}
		if (userName != null && userName.length() > 0){
			query.setParameter("userName", userName);
		}
		return (int)(long)query.uniqueResult();
		
	}

	@Override
	public ShiftWork getShiftWorkById(int shiftWorkId) {
		String hql = "from ShiftWork l where l.id = " + shiftWorkId;
		return (ShiftWork) sessionFactory.getCurrentSession().createQuery(hql).uniqueResult();
	}

}
