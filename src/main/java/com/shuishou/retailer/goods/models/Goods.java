package com.shuishou.retailer.goods.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Goods {

	@Id
	@GeneratedValue
	@Column(unique = true, nullable = false)
	private int id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(nullable = false)
	private int sequence;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn
	private Category2 category2;
	
	@Column
	private String barcode;
	
	//进货价
	@Column(nullable = false, scale=2)
	private double buyPrice;
	
	@Column(nullable = false, scale = 2)
	private double sellPrice;
	
	@Column(scale = 2)
	private double memberPrice;
	
	//批发价
	@Column(nullable = false, scale=2)
	private double tradePrice;
	
	//是否积分
	@Column
	private Boolean isScore = true;
	
	//搜索码
	@Column
	private String searchCode; 
	
	@Column(nullable = false)
	private int leftAmount;

	@Column
	private String description;
	
	
	public double getTradePrice() {
		return tradePrice;
	}

	public void setTradePrice(double tradePrice) {
		this.tradePrice = tradePrice;
	}

	public Boolean isScore() {
		return isScore;
	}

	public void setScore(Boolean isScore) {
		this.isScore = isScore;
	}

	public String getSearchCode() {
		return searchCode;
	}

	public void setSearchCode(String searchCode) {
		this.searchCode = searchCode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public Category2 getCategory2() {
		return category2;
	}

	public void setCategory2(Category2 category2) {
		this.category2 = category2;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}

	public double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}

	public double getMemberPrice() {
		return memberPrice;
	}

	public void setMemberPrice(double memberPrice) {
		this.memberPrice = memberPrice;
	}

	public int getLeftAmount() {
		return leftAmount;
	}

	public void setLeftAmount(int leftAmount) {
		this.leftAmount = leftAmount;
	}

	@Override
	public String toString() {
		return "Goods [name=" + name + "]";
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
		Goods other = (Goods) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
