package com.masai.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.masai.Enum.Role;
import com.masai.dto.AddEmployeeDto;
import com.masai.dto.GetEmployeeDto;
import com.masai.exception.DepartmentException;
import com.masai.exception.EmployeeException;
import com.masai.model.Employee;
import com.masai.service.EmployeeService;



@RestController
public class AdminController {

	@Autowired
	private EmployeeService employeeService;
	
	
	// this resourse will add the employee with department
	
	@PostMapping("/employees/{deptId}")
	public ResponseEntity<AddEmployeeDto> addEmployeeHandler(@PathVariable("deptId") Integer departmentId,@RequestBody Employee employee) throws DepartmentException{
		
		AddEmployeeDto addEmployeeDto = employeeService.addEmployee(departmentId, employee);
		
		return new ResponseEntity<AddEmployeeDto>(addEmployeeDto,HttpStatus.CREATED);
	}
	
	// this resourse will find the employee by using employeeId
	
	@GetMapping("/employees/byEmployeeId/{empId}")
	public ResponseEntity<GetEmployeeDto> getEmployeeByEmpIdHandler(@PathVariable("empId") Integer employeeId) throws EmployeeException{
		
		GetEmployeeDto getEmployeeDto = employeeService.getEmployeeByEmpId(employeeId);
		
		return new ResponseEntity<GetEmployeeDto>(getEmployeeDto,HttpStatus.OK);
	}
	
	// this resourse will find the employee by using employee userName
	
	@GetMapping("/employees/byUserName/{userName}")
	public ResponseEntity<GetEmployeeDto> getEmployeeByEmpUserNameHandler(@PathVariable("userName") String userName) throws EmployeeException{
		
		GetEmployeeDto getEmployeeDto = employeeService.getEmployeeByEmpUserName(userName);
		
		return new ResponseEntity<GetEmployeeDto>(getEmployeeDto,HttpStatus.OK);
	}
	
	// this resourse will give all employees	
	
	@GetMapping("/employees")
	public ResponseEntity<List<GetEmployeeDto>> getAllEmployeesHandler() throws EmployeeException{
		
		List<GetEmployeeDto> getEmployeeDtos = employeeService.getAllEmployees();
		
		return new ResponseEntity<List<GetEmployeeDto>>(getEmployeeDtos,HttpStatus.OK);
	}
	
	
	// this resource will change the employee role
	
	@PatchMapping("/employees/setNewRole/{id}/{role}")
	public ResponseEntity<GetEmployeeDto> changeEmployeeRoleHandler(@PathVariable("id") Integer employeeId,@PathVariable("role") Role newRole) throws EmployeeException{
		
		GetEmployeeDto getEmployeeDto = employeeService.changeEmployeeRole(employeeId, newRole);
		
		return new ResponseEntity<GetEmployeeDto>(getEmployeeDto,HttpStatus.OK);
	}
	
	// this resource will set the new salary of an employee
	
	@PatchMapping("/employees/setNewSalary/{id}/{salary}")
	public ResponseEntity<GetEmployeeDto> changeEmployeeSalaryHandler(@PathVariable("id") Integer employeeId,@PathVariable("salary") Double newSalary) throws EmployeeException{
		
		GetEmployeeDto getEmployeeDto = employeeService.changeEmployeeSalary(employeeId, newSalary);
		
		return new ResponseEntity<GetEmployeeDto>(getEmployeeDto,HttpStatus.OK);
	}
	
	
	// this resource will change the department of an employee
	
	@PatchMapping("/employees/setNewDepartment/{empId}/{deptId}")
	public ResponseEntity<GetEmployeeDto> changeEmployeeDepartmentHandler(@PathVariable("empId") Integer employeeId,@PathVariable("deptId") Integer departmentId) throws EmployeeException, DepartmentException{
		
		GetEmployeeDto getEmployeeDto = employeeService.changeEmployeeDepartment(employeeId, departmentId);
		
		return new ResponseEntity<GetEmployeeDto>(getEmployeeDto,HttpStatus.OK);
	}
	
	// this resource will provide the api for deleting an employee
	
	@DeleteMapping("/employees/{empId}")
	public ResponseEntity<GetEmployeeDto> deleteEmployeeHandler(@PathVariable("empId") Integer employeeId) throws EmployeeException{
		
		GetEmployeeDto getEmployeeDto = employeeService.deleteEmployee(employeeId);
		
		return new ResponseEntity<GetEmployeeDto>(getEmployeeDto,HttpStatus.OK);
	}
	
	
}
