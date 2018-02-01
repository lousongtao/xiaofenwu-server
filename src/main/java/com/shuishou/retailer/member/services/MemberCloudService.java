package com.shuishou.retailer.member.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shuishou.retailer.ConstantValue;
import com.shuishou.retailer.DataCheckException;
import com.shuishou.retailer.ServerProperties;
import com.shuishou.retailer.common.models.Configs;
import com.shuishou.retailer.common.models.IConfigsDataAccessor;
import com.shuishou.retailer.member.models.Member;
import com.shuishou.retailer.views.ObjectListResult;
import com.shuishou.retailer.views.ObjectResult;
import com.shuishou.retailer.views.Result;

@Service
public class MemberCloudService implements IMemberCloudService{

	@Autowired
	private IConfigsDataAccessor configDA;
	
	@Override
	public ObjectResult addMember(int userId, String name, String memberCard, String address, String postCode,
			String telephone, Date birth, double discountRate) {
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		params.put("name", name);
		params.put("memberCard", memberCard);
		params.put("discountRate", String.valueOf(discountRate));
		params.put("telephone", telephone);
		params.put("address", address);
		params.put("postCode", postCode);
		params.put("birth", ConstantValue.DFYMDHMS.format(birth));
		
		String url = "member/addmember";
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectResult("get null from server for add member. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<Member> result = gson.fromJson(response, new TypeToken<HttpResult<Member>>(){}.getType());
		if (!result.success){
			return new ObjectResult("return false while add member. URL = " + url + ", response = "+response, false);
		}
		return new ObjectResult(Result.OK, true, result.data);
	}

	@Override
	public ObjectResult updateMember(int userId, int id, String name, String memberCard, String address,
			String postCode, String telephone, Date birth, double discountRate) {
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		params.put("name", name);
		params.put("memberCard", memberCard);
		params.put("discountRate", String.valueOf(discountRate));
		params.put("telephone", telephone);
		params.put("address", address);
		params.put("postCode", postCode);
		params.put("birth", ConstantValue.DFYMDHMS.format(birth));
		params.put("id", String.valueOf(id));
		String url = "member/updatemember";
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectResult("get null from server for update member. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<Member> result = gson.fromJson(response, new TypeToken<HttpResult<Member>>(){}.getType());
		if (!result.success){
			return new ObjectResult("return false while update member. URL = " + url + ", response = "+response, false);
		}
		return new ObjectResult(Result.OK, true, result.data);
	}

	@Override
	public ObjectResult updateMemberScore(int userId, int id, double newScore) {
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		params.put("newScore", String.valueOf(newScore));
		params.put("id", String.valueOf(id));
		String url = "member/updatememberscore";
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectResult("get null from server for update member score. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<Member> result = gson.fromJson(response, new TypeToken<HttpResult<Member>>(){}.getType());
		if (!result.success){
			return new ObjectResult("return false while update member score. URL = " + url + ", response = "+response, false);
		}
		return new ObjectResult(Result.OK, true, result.data);
	}

	@Override
	public ObjectResult updateMemberBalance(int userId, int id, double newBalance) {
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		params.put("id",String.valueOf(id));
		params.put("newBalance", String.valueOf(newBalance));
		String url = "member/updatememberbalance";
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectResult("get null from server for update member balance. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<Member> result = gson.fromJson(response, new TypeToken<HttpResult<Member>>(){}.getType());
		if (!result.success){
			return new ObjectResult("return false while update member balance. URL = " + url + ", response = "+response, false);
		}
		return new ObjectResult(Result.OK, true, result.data);
	}

	@Override
	public ObjectResult memberRecharge(int userId, int id, double recharge) {
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		params.put("id",String.valueOf(id));
		params.put("rechargeValue", String.valueOf(recharge));
		String url = "member/memberrecharge";
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectResult("get null from server for update member balance. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<Member> result = gson.fromJson(response, new TypeToken<HttpResult<Member>>(){}.getType());
		if (!result.success){
			return new ObjectResult("return false while update member balance. URL = " + url + ", response = "+response, false);
		}
		return new ObjectResult(Result.OK, true, result.data);
	}

	@Override
	public ObjectResult deleteMember(int userId, int id) {
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		params.put("id",String.valueOf(id));
		String url = "member/deletemember";
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectResult("get null from server for delete member. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<Member> result = gson.fromJson(response, new TypeToken<HttpResult<Member>>(){}.getType());
		if (!result.success){
			return new ObjectResult("return false while update member. URL = " + url + ", response = "+response, false);
		}
		return new ObjectResult(Result.OK, true, result.data);
	}

	@Override
	public ObjectListResult queryMember(String name, String memberCard, String address, String postCode,
			String telephone) {
		String url = "member/querymember";
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		if (name != null && name.length() > 0)
			params.put("name",name);
		if (memberCard != null && memberCard.length() > 0)
			params.put("memberCard", memberCard);
		if (address != null && address.length() > 0)
			params.put("address", address);
		if (postCode != null && postCode.length() > 0)
			params.put("postCode", postCode);
		if (telephone != null && telephone.length() > 0)
			params.put("telephone", telephone);
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectListResult("get null from server for query member. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<ArrayList<Member>> result = gson.fromJson(response, new TypeToken<HttpResult<ArrayList<Member>>>(){}.getType());
		if (!result.success){
			return new ObjectListResult("return false while query member. URL = " + url + ", response = "+response, false);
		}
		return new ObjectListResult(Result.OK, true, result.data);
	}

	@Override
	public ObjectListResult queryAllMember() {
		String url = "member/queryallmember";
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectListResult("get null from server for loading member. URL = " + url, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<ArrayList<Member>> result = gson.fromJson(response, new TypeToken<HttpResult<ArrayList<Member>>>(){}.getType());
		if (!result.success){
			return new ObjectListResult("return false while loading member. URL = " + url + ", response = "+response, false);
		}
		return new ObjectListResult(Result.OK, true, result.data);
	}

	@Override
	public ObjectResult recordMemberConsumption(String memberCard, double consumptionPrice) throws DataCheckException {
		boolean byScore = false;
		double scorePerDollar = 0;
		boolean byDeposit = false;
		String branchName = "";
		List<Configs> configs = configDA.queryConfigs();
		for(Configs config : configs){
			if (ConstantValue.CONFIGS_MEMBERMGR_BYSCORE.equals(config.getName())){
				byScore = Boolean.valueOf(config.getValue());
			} else if (ConstantValue.CONFIGS_MEMBERMGR_SCOREPERDOLLAR.equals(config.getName())){
				scorePerDollar = Double.parseDouble(config.getValue());
			} else if (ConstantValue.CONFIGS_BRANCHNAME.equals(config.getName())){
				branchName = config.getValue();
			} else if (ConstantValue.CONFIGS_MEMBERMGR_BYDEPOSIT.equals(config.getName())){
				byDeposit = Boolean.valueOf(config.getValue());
			} 
		}
		
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		params.put("memberCard",memberCard);
		String url = "member/deletemember";
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectResult("get null from server for delete member. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<Member> result = gson.fromJson(response, new TypeToken<HttpResult<Member>>(){}.getType());
		if (!result.success){
			return new ObjectResult("return false while update member. URL = " + url + ", response = "+response, false);
		}
		return new ObjectResult(Result.OK, true, result.data);
	}

}
