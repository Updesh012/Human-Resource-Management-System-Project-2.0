package com.masai.service;

import java.util.List;

import com.masai.dto.DepartmentDto;
import com.masai.exception.DepartmentException;
import com.masai.model.Department;

public interface DepartmentService {

	public DepartmentDto addDepartment(Department department) throws DepartmentException;
	
	public DepartmentDto updateDepartmentName(Integer deptId, Department department) throws DepartmentException;
	
	public DepartmentDto deleteDepartment(Integer deptId) throws DepartmentException;
	
	public DepartmentDto getDepartmentById(Integer deptId) throws DepartmentException;
	
	public List<DepartmentDto> getAllDepartments() throws DepartmentException;
	
	public DepartmentDto getDepartmentByName(String deptName) throws DepartmentException;
	
	public List<DepartmentDto> getDepartmentsInAlphabaticalOrder() throws DepartmentException;
	
	public List<DepartmentDto> getDepartmentsInReverseAlphabaticalOrder() throws DepartmentException;
	
}
