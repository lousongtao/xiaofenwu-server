package com.shuishou.retailer.indent.models;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.shuishou.retailer.ConstantValue;
import com.shuishou.retailer.models.BaseDataAccessor;

@Repository
public class IndentDataAccessor extends BaseDataAccessor implements IIndentDataAccessor {

	@Override
	public Serializable save(Indent indent) {
		return sessionFactory.getCurrentSession().save(indent);
	}

	@Override
	public void update(Indent indent) {
		sessionFactory.getCurrentSession().update(indent);
	}

	@Override
	public void delete(Indent indent) {
		sessionFactory.getCurrentSession().delete(indent);
	}

	@Override
	public Indent getIndentById(int id) {
		String hql = "from Indent where id="+id;
		return (Indent) sessionFactory.getCurrentSession().createQuery(hql).uniqueResult();
	}

	@Override
	public List<Indent> getAllIndent() {
		String hql = "from Indent";
		return sessionFactory.getCurrentSession().createQuery(hql).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Indent> getIndents(int start, int limit, Date starttime, Date endtime, String payway, String member, String indentCode, Integer[] types,
			List<String> orderBys, List<String> orderByDescs) {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Indent.class);
		if (starttime != null)
			c.add(Restrictions.ge("createTime", starttime));
		if (endtime != null)
			c.add(Restrictions.le("createTime", endtime));
		if (payway != null && payway.length() > 0){
			c.add(Restrictions.ilike("payway", "%" + payway + "%"));
		}
		if (member != null && member.length() > 0){
			c.add(Restrictions.ilike("memberCard", "%"+member+"%"));
		}
		if (indentCode != null && indentCode.length() > 0){
			c.add(Restrictions.ilike("indentCode", "%" + indentCode +"%"));
		}
		if (types != null && types.length > 0){
			c.add(Restrictions.in("indentType", types));
		}
		if (orderBys != null && !orderBys.isEmpty()){
			for (int i = 0; i < orderBys.size(); i++) {
				c.addOrder(Order.asc(orderBys.get(i)));
			}
		}
		if (orderByDescs != null && !orderByDescs.isEmpty()){
			for (int i = 0; i < orderByDescs.size(); i++) {
				c.addOrder(Order.desc(orderByDescs.get(i)));
			}
		}
		c.setFirstResult(start);
		c.setMaxResults(limit);
		return (List<Indent>)c.list();
	}
	
	@Override
	public int getIndentCount(Date starttime, Date endtime, String payway, String member, Integer[] types) {
		String countStmt = "select count(l) from Indent l";
		List<String> condList = Lists.newArrayList();
		if (starttime != null){
			condList.add("l.createTime >= :starttime");
		}
		if (endtime != null){
			condList.add("l.createTime <= :endtime");
		}
		if (payway != null && payway.length() > 0){
			condList.add("l.payway = :payway");
		}
		if (member != null && member.length() > 0){
			condList.add("l.memberCard = :member");
		}
		if (types != null && types.length > 0){
			String s = "l.indentType in (";
			for (int i = 0; i < types.length; i++) {
				if (i != 0)
					s += ",";
				s += types[i];
			}
			s += ")";
			condList.add(s);
		}
		for (int i = 0; i < condList.size(); i++) {
			countStmt += (i == 0 ? " where " : " and ") + condList.get(i);
		}
		Query query = sessionFactory.getCurrentSession().createQuery(countStmt);
		if (starttime != null){
			query.setTimestamp("starttime", starttime);
		}
		if (endtime != null){
			query.setTimestamp("endtime", endtime);
		}
		if (payway != null && payway.length() > 0){
			query.setParameter("payway", payway);
		}
		if (member != null && member.length() > 0){
			query.setParameter("member", member);
		}
		return (int)(long)query.uniqueResult();
	}

	/**
	 * query the indent records which are paid between the period
	 * @param starttime cannot be null
	 * @param endtime cannot be null
	 */
	@Override
	public List<Indent> getIndentsByTime(Date starttime, Date endtime) {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Indent.class);
		c.add(Restrictions.eq("indentType", ConstantValue.INDENT_TYPE_ORDER));
		c.add(Restrictions.ge("createTime", starttime));
		c.add(Restrictions.le("createTime", endtime));
		return (List<Indent>)c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Indent> getPrebuyIndents(int start, int limit, Date starttime, Date endtime, String member) {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Indent.class);
		if (starttime != null)
			c.add(Restrictions.ge("createTime", starttime));
		if (endtime != null)
			c.add(Restrictions.le("createTime", endtime));
		if (member != null && member.length() > 0){
			c.add(Restrictions.ilike("memberCard", "%" + member +"%"));
		}
		c.setFirstResult(start);
		c.setMaxResults(limit);
		c.add(Restrictions.in("indentType", new Integer[]{(int)ConstantValue.INDENT_TYPE_PREBUY_PAID, (int)ConstantValue.INDENT_TYPE_PREBUY_UNPAID}));
		return (List<Indent>)c.list();
	}
}
