package com.masai.service;

import java.util.List;

import com.masai.Enum.LeaveStatus;
import com.masai.dto.LeaveDto;
import com.masai.exception.EmployeeException;
import com.masai.exception.LeaveException;
import com.masai.model.Employee;
import com.masai.model.Leave;

public interface LeaveService {
	
	public Employee getEmployee();
	
	public LeaveDto addLeave(Leave leave) throws EmployeeException,LeaveException;
	
	public LeaveDto updateLeave(Leave leave) throws EmployeeException, LeaveException;
	
	public LeaveDto deletePendingLeave() throws EmployeeException, LeaveException;

	public LeaveDto checkLatestLeaveStatus() throws EmployeeException, LeaveException;
	
	public List<LeaveDto> getAllLeaves() throws EmployeeException,LeaveException;
	
	public List<LeaveDto> getAllLeavesHistory() throws LeaveException;
	
	public List<LeaveDto> getLeavesOfParticularEmployee(Integer empId) throws LeaveException,EmployeeException;
	
	public List<LeaveDto> getPendingLeaves() throws LeaveException;
	
	public LeaveDto responseToLeave(Integer leaveId,LeaveStatus status) throws LeaveException;
	
}
