package com.masai.service;

import com.masai.dto.LeaveDto;
import com.masai.exception.EmployeeException;
import com.masai.exception.LeaveException;
import com.masai.model.Leave;

public interface LeaveService {
	
	public LeaveDto addLeave(Integer empId,Leave leave) throws EmployeeException,LeaveException;
	
	public LeaveDto updateLeave(Integer empId, Leave leave) throws EmployeeException, LeaveException;
	
	public LeaveDto deletePendingLeave(Integer empId) throws EmployeeException, LeaveException;

	public LeaveDto checkLatestLeaveStatus(Integer empId) throws EmployeeException, LeaveException;
	
	
}
