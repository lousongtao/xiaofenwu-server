package com.shuishou.retailer.member.models;

import java.util.Date;
import java.util.List;

public interface IMemberConsumptionDataAccessor {

	List<MemberConsumption> getMemberConsumptionByMemberId(int memberId);
	
	void save(MemberConsumption mc);
	
	void delete(MemberConsumption mc);
	
	void deleteByMember(int memberId);
}
