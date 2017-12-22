package com.shuishou.retailer.goods.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * record the amount plus for goods
 * @author Administrator
 *
 */

@Entity
@Table
public class AddGoodsRecord {

	@Id
	@GeneratedValue
	@Column(unique = true, nullable = false)
	private int id;
	
	@Column
	private int goodsId;
	
	@Column
	private String goodsName;
	
	@Column
	private int amount;
	
	@Column
	private int type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "AddGoodsRecord [goodsName=" + goodsName + ", amount=" + amount + ", type=" + type + "]";
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
		AddGoodsRecord other = (AddGoodsRecord) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
