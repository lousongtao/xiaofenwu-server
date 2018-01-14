package com.shuishou.retailer.goods.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class PackageBind {

	@Id
	@GeneratedValue
	@Column(unique = true, nullable = false)
	private int id;
	
	@ManyToOne
	@JoinColumn
	private Goods bigPackage;
	
	@ManyToOne
	@JoinColumn
	private Goods smallPackage;
	
	@Column(nullable = false)
	private int rate;//一个大包装包含几个小包装

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Goods getBigPackage() {
		return bigPackage;
	}

	public void setBigPackage(Goods bigPackage) {
		this.bigPackage = bigPackage;
	}

	public Goods getSmallPackage() {
		return smallPackage;
	}

	public void setSmallPackage(Goods smallPackage) {
		this.smallPackage = smallPackage;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
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
		PackageBind other = (PackageBind) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PackageBind [id=" + id + ", bigPackage=" + bigPackage.getName() + ", smallPackage=" + smallPackage.getName() 
			+ ", rate=" + rate + "]";
	}
	
	
}
