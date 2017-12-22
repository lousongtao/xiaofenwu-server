package com.shuishou.retailer.indent.models;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

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

}
