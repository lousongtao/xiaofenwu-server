package com.shuishou.retailer.member.models;

import java.util.Date;
import java.util.List;

public interface IMemberDataAccessor {

	Member getMemberById(int id);
	Member getMemberByCard(String card);
	
	List<Member> queryMember(String name, String memberCard, String address, String postCode, String telephone);
	List<Member> queryAllMember();
	
	int queryMemberCount(String name, String memberCard, String address, String postCode, String telephone);
	
	void save(Member m);
	
	void delete(Member m);
}
