package com.shuishou.retailer.indent.view;

import java.util.Date;

public class GoodsSellRecord {
	private String soldTime;
	private String member;
	private String payWay;
	private int amount;
	private double soldPrice;
	public String getSoldTime() {
		return soldTime;
	}
	public void setSoldTime(String soldTime) {
		this.soldTime = soldTime;
	}
	public String getMember() {
		return member;
	}
	public void setMember(String member) {
		this.member = member;
	}
	public String getPayWay() {
		return payWay;
	}
	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public double getSoldPrice() {
		return soldPrice;
	}
	public void setSoldPrice(double soldPrice) {
		this.soldPrice = soldPrice;
	}
}
