package com.shuishou.retailer.indent.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.shuishou.retailer.ConstantValue;
import com.shuishou.retailer.models.BaseDataAccessor;


@Repository
public class IndentDetailDataAccessor extends BaseDataAccessor implements IIndentDetailDataAccessor {

	@Override
	public Serializable save(IndentDetail indentDetail) {
		return sessionFactory.getCurrentSession().save(indentDetail);
	}

	@Override
	public void update(IndentDetail indentDetail) {
		sessionFactory.getCurrentSession().update(indentDetail);
	}

	@Override
	public void delete(IndentDetail indentDetail) {
		sessionFactory.getCurrentSession().delete(indentDetail);
	}

	@Override
	public IndentDetail getIndentDetailById(int id) {
		String hql = "from IndentDetail where id="+id;
		return (IndentDetail) sessionFactory.getCurrentSession().createQuery(hql).uniqueResult();
	}

	@Override
	public List<IndentDetail> getAllIndentDetail() {
		String hql = "from IndentDetail";
		return (List<IndentDetail>)sessionFactory.getCurrentSession().createQuery(hql).list();
	}

	@Override
	public List<IndentDetail> getIndentDetailByIndentId(int indentId) {
		String hql = "select d.* from IndentDetail d where d.indent_id = "+ indentId;
		return (List<IndentDetail>)sessionFactory.getCurrentSession().createQuery(hql).list();
	}

	@Override
	public IndentDetail getIndentDetailByParent(int indentId, int dishId) {
		String sql = "select * from indentdetail where indent_id = "+ indentId + " and dish_id = "+dishId;
		return (IndentDetail) sessionFactory.getCurrentSession().createSQLQuery(sql).uniqueResult();
	}
	
	@Override
	public List<IndentDetail> getIndentDetailByGoods(int goodsId, Date starttime, Date endtime, String payway, String member){
		String stmt = "select d from IndentDetail d where d.goodsId = " + goodsId +" and d.indent.indentType = " + ConstantValue.INDENT_TYPE_ORDER;
		if (starttime != null){
			stmt += " and d.indent.createTime >= :startTime"; 
		}
		if (endtime != null){
			stmt += " and d.indent.createTime <= :endTime";
		}
		if (payway != null && payway.length() > 0){
			stmt += " and d.indent.payWay like '%" + payway + "%'";
		}
		if (member != null&& member.length() > 0){
			stmt += " and d.indent.member = '" + member + "'";
		}
		stmt += " order by d.indent.createTime";
		Query query = sessionFactory.getCurrentSession().createQuery(stmt);
		if (starttime != null){
			query.setTime("startTime", starttime);
		}
		if (endtime != null){
			query.setTime("endtime", endtime);
		}
		return query.list();
	}

}
