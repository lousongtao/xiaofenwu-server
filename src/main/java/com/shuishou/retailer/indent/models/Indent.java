package com.shuishou.retailer.indent.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shuishou.retailer.ConstantValue;

@Entity
@Table(indexes = {@Index(name = "idx_starttime", columnList = "createtime"), 
		@Index(name="idx_membercard", columnList = "member_card"),
		@Index(name="idx_indentcode", columnList = "indent_code")})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Indent {

	@Id
	@GeneratedValue
	@Column(nullable = false, unique = true)
	private int id;
	
	@JsonFormat(pattern=ConstantValue.DATE_PATTERN_YMDHMS, timezone="GMT+8:00")
	@Column(nullable = false)
	private Date createTime;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="indent")
	private List<IndentDetail> items;
	
	@Column(name = "total_price", nullable = false, precision = 8, scale = 2)
	private double totalPrice;
	
	@Column(name ="paid_price", precision = 8, scale = 2)
	private double paidPrice;//实际付款金额
	
	@Column(name = "pay_way")
	private String payWay;//付款方式
	
	@Column
	private String discountTemplate;
	
	@Column(name = "member_card")
	private String memberCard;
	
	@Column(name="indent_code", nullable = false)
	private String indentCode;
	
	@Column
	private String operator;//操作人
	
	@JsonIgnore
	@OneToOne(cascade=CascadeType.ALL)
	private Indent originIndent;//record the original order if this order is from preorder
	
	/**
	 * 调整的价格, 有时候直接给客人折扣个零头, 或者打个折, 导致indentdetail中各项相加跟实际付款价格不一致. 用这个字段记录
	 */
	@Column
	private double adjustPrice;
	
	/**
	 * 区分 普通订单, 预购单, 退款单
	 */
	@Column(nullable = false, columnDefinition="int default("+ConstantValue.INDENT_TYPE_ORDER+")")
	private int indentType;
	
	

	public double getAdjustPrice() {
		return adjustPrice;
	}

	public void setAdjustPrice(double adjustPrice) {
		this.adjustPrice = adjustPrice;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getIndentCode() {
		return indentCode;
	}

	public void setIndentCode(String indentCode) {
		this.indentCode = indentCode;
	}

	public int getIndentType() {
		return indentType;
	}

	public void setIndentType(int indentType) {
		this.indentType = indentType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public String getDiscountTemplate() {
		return discountTemplate;
	}

	public void setDiscountTemplate(String discountTemplate) {
		this.discountTemplate = discountTemplate;
	}

	public String getMemberCard() {
		return memberCard;
	}

	public void setMemberCard(String memberCard) {
		this.memberCard = memberCard;
	}


	public Indent getOriginIndent() {
		return originIndent;
	}

	public void setOriginIndent(Indent originIndent) {
		this.originIndent = originIndent;
	}

	public double getPaidPrice() {
		return paidPrice;
	}

	public String getFormatPaidPrice(){
		return String.format("%.2f", paidPrice);
	}
	
	public void setPaidPrice(double paidPrice) {
		this.paidPrice = Double.parseDouble(String.format(ConstantValue.FORMAT_DOUBLE, paidPrice));
	}

	public List<IndentDetail> getItems() {
		return items;
	}

	public void setItems(List<IndentDetail> items) {
		this.items = items;
	}
	
	public void addItem(IndentDetail detail){
		if (items == null)
			items = new ArrayList<IndentDetail>();
		items.add(detail);
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public String getFormatTotalPrice(){
		return String.format("%.2f", totalPrice);
	}
	
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	
	@Override
	public String toString() {
		return "Order [totalPrice=" + totalPrice + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Indent other = (Indent) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
