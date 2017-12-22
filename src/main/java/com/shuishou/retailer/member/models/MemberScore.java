package com.shuishou.retailer.member.models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "member_score")
public class MemberScore {
	@Id
	@GeneratedValue
	@Column(nullable = false, unique = true)
	private int id;
	
	@Column(nullable=false, scale = 2)
	private double amount;
	
	@Column(nullable=false)
	private Date date;
	
	@Column(nullable=false)
	private int type;
	
	@JsonIgnore
	@ManyToOne
	private Member member;
	
	/**
	 * record the consume shop location
	 */
	@Column(nullable=false)
	private String place;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
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
		MemberScore other = (MemberScore) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MemberScore [date=" + date + ", place=" + place + ", amount=" + amount + ", type=" + type + "]";
	}
	
	
}
