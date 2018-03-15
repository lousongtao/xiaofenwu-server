package com.shuishou.retailer.member.models;

import java.util.Date;
import java.util.List;

public interface IMemberUpgradeDataAccessor {

	List<MemberUpgrade> getAllMemberUpgrade();
	
	MemberUpgrade getMemberUpgrade(int id);
	
	void save(MemberUpgrade mu);
	
	void delete(MemberUpgrade mu);
}
