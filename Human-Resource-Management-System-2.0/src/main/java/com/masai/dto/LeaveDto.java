package com.masai.dto;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.CreatedDate;

import com.masai.Enum.LeaveStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LeaveDto {

	private Integer leaveId;
	
	private String reason;
	
	@Enumerated(EnumType.STRING)
	private LeaveStatus status;
	
	private LocalDate leaveStartDate;
	
	private LocalDate leaveEndDate;
	
	
	
	
}
