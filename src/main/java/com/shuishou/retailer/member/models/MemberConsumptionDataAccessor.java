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
public class MemberConsumptionDataAccessor extends BaseDataAccessor implements IMemberConsumptionDataAccessor {

	@Override
	public List<MemberConsumption> getMemberConsumptionByMemberId(int memberId) {
		String hql = "select ms from MemberConsumption ms where ms.member.id = " + memberId;
		return sessionFactory.getCurrentSession().createQuery(hql).list();
	}

	@Override
	public void save(MemberConsumption mc) {
		sessionFactory.getCurrentSession().save(mc);
	}

	@Override
	public void delete(MemberConsumption mc) {
		sessionFactory.getCurrentSession().delete(mc);
	}

	@Override
	public void deleteByMember(int memberId) {
		String hql = "delete from MemberConsumption mc where mc.member.id = "+ memberId;
		sessionFactory.getCurrentSession().createQuery(hql).executeUpdate();
	}


}
