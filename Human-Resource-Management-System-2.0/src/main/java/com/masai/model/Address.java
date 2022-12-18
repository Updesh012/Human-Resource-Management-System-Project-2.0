package com.masai.model;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer addressId;
	
	
	private String houseNumber;
	
	private String colony;
	
	@NotBlank(message = "pincode is must")
	private String pincode;
	
	@NotBlank(message = "city name is must")
	private String city;
	
	@NotBlank(message = "state name is must")
	private String state;
	
	
	@ManyToOne
	@JsonIgnore
	private Employee employee;
	
	
}
