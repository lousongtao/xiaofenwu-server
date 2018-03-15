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
public class MemberUpgradeDataAccessor extends BaseDataAccessor implements IMemberUpgradeDataAccessor {
	@Override
	public void save(MemberUpgrade mu) {
		sessionFactory.getCurrentSession().save(mu);
	}

	@Override
	public void delete(MemberUpgrade mu) {
		sessionFactory.getCurrentSession().delete(mu);
	}

	@Override
	public List<MemberUpgrade> getAllMemberUpgrade() {
		String hql = "select ms from MemberUpgrade ms";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setCacheable(true)
				.list();
	}

	@Override
	public MemberUpgrade getMemberUpgrade(int id) {
		String hql = "select ms from MemberUpgrade ms where id = "+id;
		return (MemberUpgrade)sessionFactory.getCurrentSession().createQuery(hql)
				.setCacheable(true)
				.uniqueResult();
	}


}
