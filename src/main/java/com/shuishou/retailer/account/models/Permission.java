package com.shuishou.retailer.account.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table
public class Permission {
	@Id
	@GeneratedValue
	@Column(unique = true, nullable = false)
	private int id;
	
	@Column(nullable = false)
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public String toString(){
		return name;
	}
	
	public boolean equals(Object o){
		if (o instanceof Permission){
			return name.equals(((Permission)o).getName());
		}
		return false;
	}
	
	public int hashCode(){
		return super.hashCode();
	}
}
