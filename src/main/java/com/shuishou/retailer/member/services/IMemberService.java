package com.shuishou.retailer.member.services;

import java.util.Date;

import com.shuishou.retailer.views.ObjectListResult;
import com.shuishou.retailer.views.ObjectResult;

public interface IMemberService {

	ObjectResult addMember(int userId, String name, String memberCard, String address, String postCode, String telephone, Date birth, double discountRate);
	ObjectResult updateMember(int userId, int id, String name, String memberCard, String address, String postCode, String telephone, Date birth,double discountRate);
	ObjectResult updateMemberScore(int userId, int id, double newScore);
	ObjectResult updateMemberBalance(int userId, int id, double newBalance);
	ObjectResult deleteMember(int userId, int id);
	ObjectListResult queryMember(String name, String memberCard, String address, String postCode, String telephone);
	ObjectListResult queryAllMember();
	ObjectListResult queryPurchase(int userId, int id);
	ObjectListResult queryRecharge(int userId, int id);
	ObjectListResult queryScore(int userId, int id);
	
	
}
