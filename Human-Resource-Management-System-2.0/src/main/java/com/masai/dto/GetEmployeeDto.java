package com.masai.dto;

import java.time.LocalDate;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.masai.Enum.EmployeeOrAdmin;
import com.masai.Enum.Gender;
import com.masai.Enum.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetEmployeeDto {

	private Integer employeeId;
	
	private String name;
	
	private String email;
	
	private Integer departmentId;
	
	private String departmentName;
	
	private String userName;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	private Double salary;
	
	private LocalDate dateOfBirth;
	
	private LocalDate joiningDate;
	
	
}
