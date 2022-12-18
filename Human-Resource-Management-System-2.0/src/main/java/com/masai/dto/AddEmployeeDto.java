package com.masai.dto;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.masai.Enum.Gender;
import com.masai.Enum.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.masai.Enum.EmployeeOrAdmin;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddEmployeeDto {
	
	private Integer employeeId;
	
	private String name;
	
	
	private String userName;
	
	private String email;
	
	
	private String password;
	
	@Enumerated(EnumType.STRING)
	private EmployeeOrAdmin EmployeeOrAdmin;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	private Double salary;
	
	private LocalDate dateOfBirth;
	
	private LocalDate joiningDate;
	
	
	
	

}
