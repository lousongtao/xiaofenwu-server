package com.shuishou.retailer.member.models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shuishou.retailer.ConstantValue;

@Entity
@Table
public class Member {

	@Id
	@GeneratedValue
	@Column(nullable = false, unique = true)
	private int id;
	
	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false, unique = true)
	private String memberCard;
	
	@Column
	private String address;
	
	@Column
	private String postCode;
	
	@Column
	private String telephone;
	
	@Column(scale = 2)
	private double score;
	
	@Column(scale = 2)
	private double balanceMoney;
	
	@Column(scale = 2)
	private double discountRate = 1;
	
	@JsonFormat(pattern=ConstantValue.DATE_PATTERN_YMDHMS, timezone = "GMT+8:00")
	@Column
	private Date birth;
	
	@JsonFormat(pattern=ConstantValue.DATE_PATTERN_YMDHMS, timezone = "GMT+8:00")
	@Column(nullable = false)
	private Date createTime;
	
	@JsonFormat(pattern=ConstantValue.DATE_PATTERN_YMDHMS, timezone = "GMT+8:00")
	@Column(nullable = false)
	private Date lastModifyTime;
	
	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL, mappedBy="member")
	@OrderBy("id")
	private List<MemberScore> scores;
	
	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL, mappedBy="member")
	@OrderBy("id")
	private List<MemberConsumption> consumptions;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMemberCard() {
		return memberCard;
	}

	public void setMemberCard(String memberCard) {
		this.memberCard = memberCard;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getBalanceMoney() {
		return balanceMoney;
	}

	public void setBalanceMoney(double balanceMoney) {
		this.balanceMoney = balanceMoney;
	}

	public double getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public List<MemberScore> getScores() {
		return scores;
	}

	public void setScores(List<MemberScore> scores) {
		this.scores = scores;
	}

	public List<MemberConsumption> getConsumptions() {
		return consumptions;
	}

	public void setConsumptions(List<MemberConsumption> consumptions) {
		this.consumptions = consumptions;
	}

	@Override
	public String toString() {
		return "Member [name=" + name + ", memberCard=" + memberCard + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Member other = (Member) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
