package com.shuishou.retailer.account.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="User_Permission")
public class UserPermission {

	@Id
	@GeneratedValue
	@Column(nullable = false, unique = true)
	private long id;
	
	@JsonIgnore
	@ManyToOne(optional = false)
	//@Column(name = "user_id")
	private UserData user;
	
	@ManyToOne(optional = false)
	//@Column(name = "permission_id")
	private Permission permission;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
	
	public UserData getUser() {
		return user;
	}

	public void setUser(UserData user) {
		this.user = user;
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	public String toString(){
		return user.getUsername() + " " + permission.getName();
	}
	
	public boolean equals(Object o ){
		if (o instanceof UserPermission){
			return id == ((UserPermission)o).getId();
		}
		return false;
	}
	
	public int hashCode(){
		return super.hashCode();
	}
}
