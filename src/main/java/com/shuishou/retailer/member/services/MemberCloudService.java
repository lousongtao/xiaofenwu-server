package com.shuishou.retailer.member.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shuishou.retailer.ConstantValue;
import com.shuishou.retailer.DataCheckException;
import com.shuishou.retailer.ServerProperties;
import com.shuishou.retailer.common.models.Configs;
import com.shuishou.retailer.common.models.IConfigsDataAccessor;
import com.shuishou.retailer.member.models.IMemberUpgradeDataAccessor;
import com.shuishou.retailer.member.models.Member;
import com.shuishou.retailer.member.models.MemberBalance;
import com.shuishou.retailer.member.models.MemberScore;
import com.shuishou.retailer.member.models.MemberUpgrade;
import com.shuishou.retailer.views.ObjectListResult;
import com.shuishou.retailer.views.ObjectResult;
import com.shuishou.retailer.views.Result;

@Service
public class MemberCloudService implements IMemberCloudService{

	@Autowired
	private IConfigsDataAccessor configDA;
	
	@Autowired
	private IMemberUpgradeDataAccessor memberUpgradeDA;
	
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
		if (birth != null)
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
		if (birth != null)
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
	public ObjectResult memberRecharge(int userId, int id, double recharge, String branchName) {
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		params.put("id",String.valueOf(id));
		params.put("rechargeValue", String.valueOf(recharge));
		params.put("branchName", branchName);
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
	@Transactional
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
		params.put("consumptionPrice", String.format(ConstantValue.FORMAT_DOUBLE, consumptionPrice));
		params.put("scorePerDollar", String.valueOf(scorePerDollar));
		params.put("byDeposit", String.valueOf(byDeposit));
		params.put("branchName", branchName);
		params.put("byScore", String.valueOf(byScore));
		
		String url = "member/recordconsumption";
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectResult("get null from server for delete member. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<Member> result = gson.fromJson(response, new TypeToken<HttpResult<Member>>(){}.getType());
		if (!result.success){
			return new ObjectResult("return false while update member. URL = " + url + ", response = "+response, false);
		}
		Member member = result.data;
		if (member != null)
			member = checkMemberUpgrade(member);
		return new ObjectResult(Result.OK, true, member);
	}
	
	/**
	 * 根据用户信息, 判断是否达到升级条件
	 * 1. 当比较值落入smallValue和bigValue之间, 修改目标项 
	 * @param m
	 */
	@Transactional
	private Member checkMemberUpgrade(Member m) {
		// 检查会员是否达到升级条件, 按照循序检查, 碰到不达标的条件, 终止退出
		List<MemberUpgrade> mus = memberUpgradeDA.getAllMemberUpgrade();
		if (mus != null && !mus.isEmpty()) {
			for (int i = 0; i < mus.size(); i++) {
				MemberUpgrade mu = mus.get(i);
				if (mu.getStatus() == ConstantValue.MEMBERUPGRADE_STATUS_UNAVAILABLE)
					continue;
				double compareValue = 0;
				if ("score".equalsIgnoreCase(mu.getCompareField())){
					compareValue = m.getScore();
				}
				boolean bSmall = false;
				boolean bBig = false;
				switch(mu.getSmallRelation()){
				case ConstantValue.MEMBERUPGRADE_RELATION_EQUAL:
					bSmall = mu.getSmallValue() == compareValue;
					break;
				case ConstantValue.MEMBERUPGRADE_RELATION_GREATER:
					bSmall = compareValue > mu.getSmallValue();
					break;
				case ConstantValue.MEMBERUPGRADE_RELATION_GREATEREQUAL:
					bSmall = compareValue >= mu.getSmallValue();
					break;
				}
				switch(mu.getBigRelation()){
				case ConstantValue.MEMBERUPGRADE_RELATION_EQUAL:
					bBig = mu.getBigValue() == compareValue;
					break;
				case ConstantValue.MEMBERUPGRADE_RELATION_LESS:
					bBig = compareValue < mu.getBigValue();
					break;
				case ConstantValue.MEMBERUPGRADE_RELATION_LESSEQUAL:
					bBig = compareValue <= mu.getBigValue();
					break;
				}
				if (bSmall & bBig){
					if ("discountrate".equalsIgnoreCase(mu.getExecuteField())){
						if (m.getDiscountRate() != mu.getExecuteValue()){
							m = updateMemberDiscountRate(m.getId(), mu.getExecuteValue());
							if (m != null)
								return m;
						}
					}
				}
			}
		}
		return m;
	}
	
	private Member updateMemberDiscountRate(int memberId, double discountRate){
		String url = "member/updatememberdiscountrate";
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		params.put("id", String.valueOf(memberId));
		params.put("discountRate", String.valueOf(discountRate));
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return null;
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMD).create();
		HttpResult<Member> result = gson.fromJson(response, new TypeToken<HttpResult<Member>>(){}.getType());
		if (!result.success){
			return null;
		}
		return result.data;
	}

	public ObjectListResult queryMemberBalance(int memberId){
		String url = "member/querymemberbalance";
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		params.put("memberId", String.valueOf(memberId));
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectListResult("get null from server for query member balance. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMDHMS).create();
		HttpResult<ArrayList<MemberBalance>> result = gson.fromJson(response, new TypeToken<HttpResult<ArrayList<MemberBalance>>>(){}.getType());
		if (!result.success){
			return new ObjectListResult("return false while query member balance. URL = " + url + ", response = "+response, false);
		}
		return new ObjectListResult(Result.OK, true, result.data);
	}
	
	public ObjectListResult queryMemberScore(int memberId){
		String url = "member/querymemberscore";
		Map<String, String> params = new HashMap<>();
		params.put("customerName", ServerProperties.MEMBERCUSTOMERNAME);
		params.put("memberId", String.valueOf(memberId));
		String response = HttpUtil.getJSONObjectByPost(ServerProperties.MEMBERCLOUDLOCATION + url, params);
		if (response == null){
			return new ObjectListResult("get null from server for query member score. URL = " + url + ", param = "+ params, false);
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMDHMS).create();
		HttpResult<ArrayList<MemberScore>> result = gson.fromJson(response, new TypeToken<HttpResult<ArrayList<MemberScore>>>(){}.getType());
		if (!result.success){
			return new ObjectListResult("return false while query member score. URL = " + url + ", response = "+response, false);
		}
		return new ObjectListResult(Result.OK, true, result.data);
	}
}
