package com.shuishou.retailer.statistics.views;

public class StatItem {
	public String itemName;
	public double totalPrice;
	public double paidPrice;
	public int soldAmount;
	public StatItem(String itemName){
		this.itemName = itemName;
	}
}
