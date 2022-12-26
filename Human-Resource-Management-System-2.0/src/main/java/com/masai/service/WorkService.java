package com.masai.service;

import com.masai.dto.GroupWorkDto;
import com.masai.dto.WorkDto;
import com.masai.exception.EmployeeException;
import com.masai.exception.WorkException;
import com.masai.model.Work;

public interface WorkService {

	public String assignWork(Integer empId,Work work) throws EmployeeException;
	
	public WorkDto deleteWork(Integer workId) throws WorkException;
	
	public WorkDto updateWork(Integer workId,Work work) throws WorkException;
	
	public String groupWork(GroupWorkDto dto) throws EmployeeException;
	
}
