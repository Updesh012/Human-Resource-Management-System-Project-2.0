package com.masai.dto;

import java.time.LocalDate;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.masai.Enum.WorkStatus;
import com.masai.Enum.WorkType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkDto {

public Integer workId;
	
	private String name;
	
	private String description;
	
	
	
	private LocalDate startDate;
	
	private LocalDate deadLine;
	
	
	@Enumerated(EnumType.STRING)
	private WorkType workType;
	
	
	@Enumerated(EnumType.STRING)
	private WorkStatus status;
	
	
}
