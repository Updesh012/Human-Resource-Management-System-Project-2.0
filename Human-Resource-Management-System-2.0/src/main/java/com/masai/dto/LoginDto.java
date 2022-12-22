package com.masai.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

	@NotBlank(message = "userName is mandatory...")
	private String userName;
	
	@NotBlank(message = "password is mandatory...")
	private String password;
	
	
	
}
