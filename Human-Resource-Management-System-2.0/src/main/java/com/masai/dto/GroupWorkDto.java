package com.masai.dto;

import java.util.List;

import com.masai.model.Work;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupWorkDto {

	private List<Integer> employeesId;
	
	private Work work;
	
	private Integer leaderId;
	
}
