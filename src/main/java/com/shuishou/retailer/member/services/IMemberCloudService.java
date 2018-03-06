package com.shuishou.retailer.member.services;

import java.util.Date;

import com.shuishou.retailer.DataCheckException;
import com.shuishou.retailer.views.ObjectListResult;
import com.shuishou.retailer.views.ObjectResult;

public interface IMemberCloudService {

	ObjectResult addMember(int userId, String name, String memberCard, String address, String postCode, String telephone, Date birth, double discountRate);
	ObjectResult updateMember(int userId, int id, String name, String memberCard, String address, String postCode, String telephone, Date birth,double discountRate);
	ObjectResult updateMemberScore(int userId, int id, double newScore);
	ObjectResult updateMemberBalance(int userId, int id, double newBalance);
	ObjectResult memberRecharge(int userId, int id, double newBalance);
	ObjectResult deleteMember(int userId, int id);
	ObjectResult recordMemberConsumption(String memberCard, double consumptionPrice) throws DataCheckException;
	ObjectListResult queryMember(String name, String memberCard, String address, String postCode, String telephone);
	ObjectListResult queryAllMember();
	ObjectListResult queryMemberBalance(int memberId);
	ObjectListResult queryMemberScore(int memberId);
}
