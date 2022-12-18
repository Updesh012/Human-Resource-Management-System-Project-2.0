package com.masai.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masai.Enum.EmployeeOrAdmin;
import com.masai.Enum.Role;
import com.masai.dto.AddEmployeeDto;
import com.masai.dto.GetEmployeeDto;
import com.masai.exception.DepartmentException;
import com.masai.exception.EmployeeException;
import com.masai.model.Department;
import com.masai.model.Employee;
import com.masai.repository.DepartmentRepository;
import com.masai.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService{

	@Autowired
	private EmployeeRepository employeeRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private DepartmentRepository departmentRepo;
	
	//////////////////////////////////////////////////////////////////////////////////
	
	// this method will registered employee as well as add employee in particular department. 
	
	@Override
	public AddEmployeeDto addEmployee(Integer departmentId,Employee employee) throws DepartmentException {
		
		Department department = departmentRepo.findById(departmentId)
											  .orElseThrow(() -> new DepartmentException("No department found with this Id"));
		
		department.getEmployees().add(employee);
		employee.setDepartment(department);
		
		Employee empObj = employeeRepo.save(employee);
		
		Integer employeeId = empObj.getEmployeeId();
		empObj.setEmployeeOrAdmin(EmployeeOrAdmin.EMPLOYEE);
		empObj.setUserName(empObj.getName()+""+employeeId);
		empObj.setPassword(empObj.getName()+""+employeeId);
		
		Employee employeeObj = employeeRepo.save(empObj);
		
		
		return modelMapper.map(employeeObj, AddEmployeeDto.class);
		
	}

	// this method will find the employee by using emlployee id or else it will throw user defined exception.
	
	@Override
	public GetEmployeeDto getEmployeeByEmpId(Integer employeeId) throws EmployeeException {
		
		 Employee employee = employeeRepo.findById(employeeId)
				 					     .orElseThrow(() -> new EmployeeException("Employee does not exist with this Id..."));
		 
		
		 
		 GetEmployeeDto getEmployeeDto = modelMapper.map(employee, GetEmployeeDto.class);
		 
		 getEmployeeDto.setDepartmentId(employee.getDepartment().getDepartmentId());
		 getEmployeeDto.setDepartmentName(employee.getDepartment().getDepartmentName());
		
		return getEmployeeDto; 
	}

	// this method will find the employee by using emlployee userName or else it will throw user defined exception.
	
	@Override
	public GetEmployeeDto getEmployeeByEmpUserName(String userName) throws EmployeeException {
		
		Employee employee = employeeRepo.findByUserName(userName);
		
		if(employee == null)
				throw new EmployeeException("No employee found with this userName");
		else {
			 GetEmployeeDto getEmployeeDto = modelMapper.map(employee, GetEmployeeDto.class);
			 
			 getEmployeeDto.setDepartmentId(employee.getDepartment().getDepartmentId());
			 getEmployeeDto.setDepartmentName(employee.getDepartment().getDepartmentName());
			
			return getEmployeeDto; 
		}
				
	}
	
	// this method will return all the employees which are present in database

	@Override
	public List<GetEmployeeDto> getAllEmployees() throws EmployeeException {
		
		List<Employee> employees = employeeRepo.findAll();
		
		if(employees.isEmpty())
				throw new EmployeeException("No employee present");
		else {
			
			List<GetEmployeeDto> dtoList = new ArrayList<>();
			
			for(Employee employee : employees) {
				
				 GetEmployeeDto getEmployeeDto = modelMapper.map(employee, GetEmployeeDto.class);
				 
				 getEmployeeDto.setDepartmentId(employee.getDepartment().getDepartmentId());
				 getEmployeeDto.setDepartmentName(employee.getDepartment().getDepartmentName());
				
				dtoList.add(getEmployeeDto);
				
				
			}
			
			return dtoList;
		}
	}
	
	// this method will change the role of particular employee or else throw custom exception

	@Override
	public GetEmployeeDto changeEmployeeRole(Integer employeeId,Role role) throws EmployeeException {
		
		Employee employee = employeeRepo.findById(employeeId)
				                        .orElseThrow(() -> new EmployeeException("employee does not exist with this employeeId"));
		
		employee.setRole(role);
		Employee afterRoleObject = employeeRepo.save(employee);
		
		 GetEmployeeDto getEmployeeDto = modelMapper.map(afterRoleObject, GetEmployeeDto.class);
		 
		 getEmployeeDto.setDepartmentId(employee.getDepartment().getDepartmentId());
		 getEmployeeDto.setDepartmentName(employee.getDepartment().getDepartmentName());
		
		return getEmployeeDto; 
		
	}

	// this method will the salary of particular employee or else throw custom exception
	
	@Override
	public GetEmployeeDto changeEmployeeSalary(Integer employeeId,Double salary) throws EmployeeException {
		
		Employee employee = employeeRepo.findById(employeeId)
									    .orElseThrow(() -> new EmployeeException("employee does not exist with this employeeId"));

		employee.setSalary(salary);
		Employee afterSalary = employeeRepo.save(employee);
		
		 GetEmployeeDto getEmployeeDto = modelMapper.map(afterSalary, GetEmployeeDto.class);
		 
		 getEmployeeDto.setDepartmentId(employee.getDepartment().getDepartmentId());
		 getEmployeeDto.setDepartmentName(employee.getDepartment().getDepartmentName());
		
		return getEmployeeDto; 
		
	}

	// this method will change the department of an employee or else throw custom exception like employee or department does not exist
	
	@Override
	public GetEmployeeDto changeEmployeeDepartment(Integer employeeId, Integer departmentId)
			throws EmployeeException, DepartmentException {
		
		
		Employee employee = employeeRepo.findById(employeeId)
									    .orElseThrow(() -> new EmployeeException("Employee does not exist with this employee id"));
		
		Department department = departmentRepo.findById(departmentId)
					  						  .orElseThrow(() -> new DepartmentException("Department does not exist with this department id"));
		
		employee.setDepartment(department);
		department.getEmployees().add(employee);
		
		Employee afterChangingDepartment = employeeRepo.save(employee);
		
		GetEmployeeDto getEmployeeDto = modelMapper.map(afterChangingDepartment, GetEmployeeDto.class);
		 
		getEmployeeDto.setDepartmentId(afterChangingDepartment.getDepartment().getDepartmentId());
		getEmployeeDto.setDepartmentName(afterChangingDepartment.getDepartment().getDepartmentName());
		
		return getEmployeeDto; 
		
		
	}
	
	// this method will delete the employee

	@Override
	public GetEmployeeDto deleteEmployee(Integer employeeId) throws EmployeeException {
		
		Employee employee = employeeRepo.findById(employeeId)
										.orElseThrow(() -> new EmployeeException("employee does not exist with this employee id"));		
		
		employeeRepo.delete(employee);
		
		GetEmployeeDto getEmployeeDto = modelMapper.map(employee, GetEmployeeDto.class);
		 
		getEmployeeDto.setDepartmentId(employee.getDepartment().getDepartmentId());
		getEmployeeDto.setDepartmentName(employee.getDepartment().getDepartmentName());
		
		return getEmployeeDto; 
		
	}

	
	
	
}
