package com.shuishou.retailer.member.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shuishou.retailer.ConstantValue;

@Entity
@Table(name = "member_balance")
public class MemberBalance {
	@Id
	@GeneratedValue
	@Column(nullable = false, unique = true)
	private int id;
	
	/**
	 * record the consume shop location
	 */
	@Column(nullable=false)
	private String place;
	
	@Column(nullable=false, scale = 2)
	private double amount;
	
	@JsonFormat(pattern=ConstantValue.DATE_PATTERN_YMDHMS, timezone = "GMT+8:00")
	@Column(nullable=false)
	private Date date;
	
	@Column(nullable=false)
	private int type;
	
	@JsonIgnore
	@ManyToOne
	private Member member;

	/**
	 * 修改后客户最新的余额
	 */
	@Column(nullable=false)
	private double newValue;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public double getNewValue() {
		return newValue;
	}

	public void setNewValue(double newValue) {
		this.newValue = newValue;
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
		MemberBalance other = (MemberBalance) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MemberBalance [date=" + date + ", place=" + place + ", amount=" + amount + ", type=" + type + "]";
	}
	
	
}
