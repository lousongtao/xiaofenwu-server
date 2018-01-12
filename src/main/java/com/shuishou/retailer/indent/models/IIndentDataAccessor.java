package com.shuishou.retailer.indent.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

public interface IIndentDataAccessor {
	public Session getSession();
	
	public Serializable save(Indent indent);
	
	public void update(Indent indent);
	
	public void delete(Indent indent);
	
	public Indent getIndentById(int id);
	
	public List<Indent> getAllIndent();
	
	public List<Indent> getUnpaidIndent();
	
	public List<Indent> getUnpaidIndent(String deskName);
	
	public List<Indent> getIndents(int start, int limit, Date starttime, Date endtime, String payway, String member, List<String> orderbys, List<String> orderByDescs);
	
	public List<Indent> getPrebuyIndents(int start, int limit, Date starttime, Date endtime, String member);
	
	public List<Indent> getIndentsByPaidTime(Date starttime, Date endtime);
	
	public int getIndentCount(Date starttime, Date endtime, String payway, String member);
	
}
