package com.cg.scb.dto;

import com.cg.scb.entity.Customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Admin")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Admin
{
	
	@Id
	String username;
	@Column
	String name;
	@Column
	String password;
	
	
	@Override
	public String toString() {
		return "Login [username=" + username + ", password=" + password + "]";
	}
	
}

