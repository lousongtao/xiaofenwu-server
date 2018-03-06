package com.shuishou.retailer.member.models;

import java.util.Date;
import java.util.List;

public interface IMemberBalanceDataAccessor {

	List<MemberBalance> getMemberBalanceByMemberId(int memberId);
	
	void save(MemberBalance mb);
	
	void delete(MemberBalance mb);
	
	void deleteByMember(int memberId);
}
