package com.shuishou.retailer.member.models;

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
public class MemberDataAccessor extends BaseDataAccessor implements IMemberDataAccessor {

	@Override
	public Member getMemberById(int id) {
		String hql = "from Member where id = "+id;
		return (Member) sessionFactory.getCurrentSession().createQuery(hql).uniqueResult();
	}

	@Override
	public List<Member> queryMember(String name, String memberCard, String address,
			String postCode, String telephone) {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Member.class);
		if (name != null && name.length() > 0)
			c.add(Restrictions.like("name", name));
		if (memberCard != null && memberCard.length() > 0)
			c.add(Restrictions.like("memberCard", memberCard));
		if (address != null && address.length() > 0)
			c.add(Restrictions.like("address", address));
		if (postCode != null && postCode.length() > 0)
			c.add(Restrictions.like("postCode", postCode));
		if (telephone != null && telephone.length() > 0)
			c.add(Restrictions.like("telephone", telephone));
		c.addOrder(Order.asc("id"));
		return (List<Member>)c.list();
	}
	
	@Override
	public List<Member> queryAllMember() {
		String hql = "from Member";
		return sessionFactory.getCurrentSession().createQuery(hql).list();
	}

	@Override
	public int queryMemberCount(String name, String memberCard, String address, String postCode,
			String telephone) {
		String countStmt = "select count(l) from Member l";
		List<String> condList = Lists.newArrayList();
		if (name != null && name.length() > 0)
			condList.add("l.name like '%" + name +"%'");
		if (memberCard != null && memberCard.length() > 0)
			condList.add("l.memberCard like '%" + memberCard +"%'");
		if (address != null && address.length() > 0)
			condList.add("l.address like '%" + address +"%'");
		if (postCode != null && postCode.length() > 0)
			condList.add("l.postCode like '%" + postCode +"%'");
		if (telephone != null && telephone.length() > 0)
			condList.add("l.telephone like '%" + telephone +"%'");
		for (int i = 0; i < condList.size(); i++) {
			if (i == 0)
				countStmt += " where ";
			else countStmt += " and ";
			countStmt += condList.get(i);
		}
		Query query = sessionFactory.getCurrentSession().createQuery(countStmt);
		return (int)(long)query.uniqueResult();
	}

	@Override
	public void save(Member m) {
		sessionFactory.getCurrentSession().save(m);
	}

	@Override
	public void delete(Member m) {
		sessionFactory.getCurrentSession().delete(m);
	}


}
