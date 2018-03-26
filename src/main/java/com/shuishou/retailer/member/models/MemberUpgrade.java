package com.shuishou.retailer.member.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shuishou.retailer.ConstantValue;

@Entity
@Table(name = "member_upgrade")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MemberUpgrade {
	@Id
	@GeneratedValue
	@Column(nullable = false, unique = true)
	private int id;
	
	@Column(nullable = false)
	private String compareField;
	
	@Column(nullable=false)
	private double smallValue;
	
	@Column(nullable=false)
	private int smallRelation;
	
	@Column(nullable=false)
	private double bigValue;
	
	@Column(nullable=false)
	private int bigRelation;
	
	@Column(nullable=false)
	private String executeField;
	
	@Column(nullable=false)
	private double executeValue;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	public String getCompareField() {
		return compareField;
	}

	public void setCompareField(String compareField) {
		this.compareField = compareField;
	}

	public double getSmallValue() {
		return smallValue;
	}

	public void setSmallValue(double smallValue) {
		this.smallValue = smallValue;
	}

	public int getSmallRelation() {
		return smallRelation;
	}

	public void setSmallRelation(int smallRelation) {
		this.smallRelation = smallRelation;
	}

	public double getBigValue() {
		return bigValue;
	}

	public void setBigValue(double bigValue) {
		this.bigValue = bigValue;
	}

	public int getBigRelation() {
		return bigRelation;
	}

	public void setBigRelation(int bigRelation) {
		this.bigRelation = bigRelation;
	}

	public String getExecuteField() {
		return executeField;
	}

	public void setExecuteField(String executeField) {
		this.executeField = executeField;
	}

	public double getExecuteValue() {
		return executeValue;
	}

	public void setExecuteValue(double executeValue) {
		this.executeValue = executeValue;
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
		MemberUpgrade other = (MemberUpgrade) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MemberUpgrade [compareField=" + compareField + ", smallValue=" + smallValue
				+ ", smallRelation=" + smallRelation + ", bigValue=" + bigValue + ", bigRelation=" + bigRelation
				+ ", executeField=" + executeField + ", executeValue=" + executeValue + "]";
	}

	
	
}
