package com.shuishou.retailer.member.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shuishou.retailer.ConstantValue;
import com.shuishou.retailer.DataCheckException;
import com.shuishou.retailer.account.models.IUserDataAccessor;
import com.shuishou.retailer.account.models.UserData;
import com.shuishou.retailer.common.models.Configs;
import com.shuishou.retailer.common.models.IConfigsDataAccessor;
import com.shuishou.retailer.log.models.LogData;
import com.shuishou.retailer.log.services.ILogService;
import com.shuishou.retailer.member.models.IMemberConsumptionDataAccessor;
import com.shuishou.retailer.member.models.IMemberDataAccessor;
import com.shuishou.retailer.member.models.IMemberScoreDataAccessor;
import com.shuishou.retailer.member.models.Member;
import com.shuishou.retailer.member.models.MemberConsumption;
import com.shuishou.retailer.member.models.MemberScore;
import com.shuishou.retailer.views.ObjectListResult;
import com.shuishou.retailer.views.ObjectResult;
import com.shuishou.retailer.views.Result;

@Service
public class MemberService implements IMemberService{

	@Autowired
	private IMemberDataAccessor memberDA;
	
	@Autowired
	private IMemberConsumptionDataAccessor memberConsumptionDA;
	
	@Autowired
	private IMemberScoreDataAccessor memberScoreDA;
	
	@Autowired
	private IConfigsDataAccessor configsDA;
	
	@Autowired
	private ILogService logService;
	
	@Autowired
	private IUserDataAccessor userDA;
	
	@Override
	@Transactional
	public ObjectResult addMember(int userId, String name, String memberCard, String address, String postCode,
			String telephone, Date birth, double discountRate) {
		Member m = new Member();
		m.setName(name);
		m.setMemberCard(memberCard);
		if (address != null)
			m.setAddress(address);
		if (postCode != null)
			m.setPostCode(postCode);
		if (telephone != null)
			m.setTelephone(telephone);
		if (birth != null)
			m.setBirth(birth);
		m.setDiscountRate(discountRate);
		m.setCreateTime(new Date());
		m.setLastModifyTime(new Date());
		memberDA.save(m);
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.MEMBER_CHANGE.toString(), "User " + selfUser + " add member : " + m);
		return new ObjectResult(Result.OK, true, m);
	}

	@Override
	@Transactional
	public ObjectResult updateMember(int userId, int id, String name, String memberCard, String address,
			String postCode, String telephone, Date birth, double discountRate) {
		Member m = memberDA.getMemberById(id);
		if (m == null)
			return new ObjectResult("cannot find member by id "+ id, false, null);
		m.setName(name);
		m.setMemberCard(memberCard);
		m.setAddress(address);
		m.setPostCode(postCode);
		m.setTelephone(telephone);
		m.setBirth(birth);
		m.setDiscountRate(discountRate);
		m.setLastModifyTime(new Date());
		memberDA.save(m);
		UserData selfUser = userDA.getUserById(userId);
		
		logService.write(selfUser, LogData.LogType.MEMBER_CHANGE.toString(), "User " + selfUser + " update member info to name : " + name
				+ ", memberCard : " + memberCard + ", address : " + address + ", postCode : "+ postCode + ", telephone : "+ telephone
				+ ", birth" + (birth == null ? "": ConstantValue.DFYMD.format(birth)));
		return new ObjectResult(Result.OK, true, m);
	}
	
	@Override
	@Transactional
	public ObjectResult updateMemberScore(int userId, int id, double newScore) {
		Member m = memberDA.getMemberById(id);
		
		if (m == null)
			return new ObjectResult("cannot find member by id "+ id, false, null);
		
		double oldScore = m.getScore();
		m.setScore(newScore);
		m.setLastModifyTime(new Date());
		memberDA.save(m);
		UserData selfUser = userDA.getUserById(userId);
		
		logService.write(selfUser, LogData.LogType.MEMBER_CHANGE.toString(), "User " + selfUser + " update member score "+ oldScore +" to " + newScore);
		return new ObjectResult(Result.OK, true, m);
	}
	
	@Override
	@Transactional
	public ObjectResult updateMemberBalance(int userId, int id, double newBalance) {
		Member m = memberDA.getMemberById(id);
		
		if (m == null)
			return new ObjectResult("cannot find member by id "+ id, false, null);
		
		double oldBalance = m.getBalanceMoney();
		m.setBalanceMoney(newBalance);
		m.setLastModifyTime(new Date());
		memberDA.save(m);
		UserData selfUser = userDA.getUserById(userId);
		
		logService.write(selfUser, LogData.LogType.MEMBER_CHANGE.toString(), "User " + selfUser + " update member balance "+ oldBalance +" to " + newBalance);
		return new ObjectResult(Result.OK, true, m);
	}
	
	@Override
	@Transactional
	public ObjectResult memberRecharge(int userId, int id, double rechargeValue) {
		Member m = memberDA.getMemberById(id);
		
		if (m == null)
			return new ObjectResult("cannot find member by id "+ id, false, null);
		
		double oldBalance = m.getBalanceMoney();
		m.setBalanceMoney(m.getBalanceMoney() + rechargeValue);
		m.setLastModifyTime(new Date());
		memberDA.save(m);
		UserData selfUser = userDA.getUserById(userId);
		
		logService.write(selfUser, LogData.LogType.MEMBER_CHANGE.toString(), "User " + selfUser + " recharge member. old balance = "+ oldBalance +", recharge to " + m.getBalanceMoney());
		return new ObjectResult(Result.OK, true, m);
	}

	@Override
	@Transactional
	public ObjectResult deleteMember(int userId, int id) {
		Member m = memberDA.getMemberById(id);
		if (m == null)
			return new ObjectResult("cannot find member by id "+ id, false, null);
		memberDA.delete(m);
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.MEMBER_CHANGE.toString(), "User " + selfUser + " delete member  : " + m.getName());
		return new ObjectResult(Result.OK, true);
	}

	@Override
	@Transactional
	public ObjectListResult queryMember(String name, String memberCard, String address, String postCode, String telephone) {
//		int count = memberDA.queryMemberCount(name, memberCard, address, postCode, telephone);
//		if (count >= 300)
//			return new ObjectListResult("Record is over 300, please change the filter", false, null, count);
		List<Member> members = memberDA.queryMember(name, memberCard, address, postCode, telephone);
		return new ObjectListResult(Result.OK, true, members, 0);
	}
	
	@Override
	@Transactional
	public ObjectListResult queryAllMember() {
		List<Member> members = memberDA.queryAllMember();
		return new ObjectListResult(Result.OK, true, members, members.size());
	}

	@Override
	@Transactional
	public ObjectListResult queryPurchase(int userId, int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public ObjectListResult queryRecharge(int userId, int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public ObjectListResult queryScore(int userId, int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public ObjectResult recordMemberConsumption(String memberCard, double consumptionPrice) throws DataCheckException {
		Member member = memberDA.getMemberByCard(memberCard);
		if (member == null){
			throw new DataCheckException("cannot find member by card " + memberCard);
		}
		Date time = new Date();
		boolean byScore = false;
		double scorePerDollar = 0;
		boolean byDeposit = false;
		String branchName = "";
		List<Configs> configs = configsDA.queryConfigs();
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
		if (byScore && scorePerDollar > 0){
			MemberScore ms = new MemberScore();
			ms.setDate(time);
			ms.setAmount(scorePerDollar * consumptionPrice);
			ms.setPlace(branchName);
			if (consumptionPrice > 0)
				ms.setType(ConstantValue.MEMBERSCORE_CONSUM);
			else 
				ms.setType(ConstantValue.MEMBERSCORE_REFUND);
			ms.setMember(member);
			memberScoreDA.save(ms);
			member.setScore(member.getScore() + ms.getAmount());
			member.setLastModifyTime(time);
			memberDA.save(member);
		}
		if (byDeposit){
			if (member.getBalanceMoney() < consumptionPrice){
				throw new DataCheckException("Meber's balance is not enought to pay");
			}
			MemberConsumption mc = new MemberConsumption();
			mc.setAmount(consumptionPrice);
			mc.setDate(time);
			mc.setMember(member);
			mc.setPlace(branchName);
			if (consumptionPrice > 0)
				mc.setType(ConstantValue.MEMBERDEPOSIT_CONSUM);
			else 
				mc.setType(ConstantValue.MEMBERDEPOSIT_REFUND);
			
			memberConsumptionDA.save(mc);
			member.setBalanceMoney(member.getBalanceMoney() - consumptionPrice);
			member.setLastModifyTime(time);
			memberDA.save(member);
		}
		return new ObjectResult(Result.OK, true);
	}

}
