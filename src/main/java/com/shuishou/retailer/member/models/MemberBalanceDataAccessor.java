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
public class MemberBalanceDataAccessor extends BaseDataAccessor implements IMemberBalanceDataAccessor {

	@Override
	public List<MemberBalance> getMemberBalanceByMemberId(int memberId) {
		String hql = "select ms from MemberBalance ms where ms.member.id = " + memberId;
		return sessionFactory.getCurrentSession().createQuery(hql).list();
	}

	@Override
	public void save(MemberBalance mb) {
		sessionFactory.getCurrentSession().save(mb);
	}

	@Override
	public void delete(MemberBalance mb) {
		sessionFactory.getCurrentSession().delete(mb);
	}

	@Override
	public void deleteByMember(int memberId) {
		String hql = "delete from MemberBalance mc where mc.member.id = "+ memberId;
		sessionFactory.getCurrentSession().createQuery(hql).executeUpdate();
	}


}
