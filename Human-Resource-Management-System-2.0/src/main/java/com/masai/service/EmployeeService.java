package com.masai.service;

import java.util.List;

import com.masai.Enum.Role;
import com.masai.dto.AddEmployeeDto;
import com.masai.dto.GetEmployeeDto;
import com.masai.exception.DepartmentException;
import com.masai.exception.EmployeeException;
import com.masai.model.Employee;

public interface EmployeeService {

	public AddEmployeeDto addEmployee(Integer departmentId,Employee employee) throws DepartmentException;
	
	public GetEmployeeDto getEmployeeByEmpId(Integer employeeId) throws EmployeeException;
	
	public GetEmployeeDto getEmployeeByEmpUserName(String userName) throws EmployeeException;
	
	public List<GetEmployeeDto> getAllEmployees() throws EmployeeException;
	
	public GetEmployeeDto changeEmployeeRole(Integer employeeId,Role newRole) throws EmployeeException;
	
	public GetEmployeeDto changeEmployeeSalary(Integer employeeId,Double newSalary) throws EmployeeException;
	
	public GetEmployeeDto changeEmployeeDepartment(Integer employeeId,Integer newDepartmentId) throws EmployeeException,DepartmentException;
	
	public GetEmployeeDto deleteEmployee(Integer employeeId) throws EmployeeException;
	
}
