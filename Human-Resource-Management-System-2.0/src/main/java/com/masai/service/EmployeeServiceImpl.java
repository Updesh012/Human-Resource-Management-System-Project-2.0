package com.masai.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.masai.Enum.EmployeeOrAdmin;
import com.masai.Enum.Role;
import com.masai.dto.AddEmployeeDto;
import com.masai.dto.AdminDto;
import com.masai.dto.AuthenticatedResponseDto;
import com.masai.dto.GetEmployeeDto;
import com.masai.dto.LoginDto;
import com.masai.dto.UpdateEmployeeDto;
import com.masai.dto.UpdatePasswordDto;
import com.masai.exception.DepartmentException;
import com.masai.exception.EmployeeException;
import com.masai.exception.PasswordException;
import com.masai.model.Department;
import com.masai.model.Employee;
import com.masai.repository.DepartmentRepository;
import com.masai.repository.EmployeeRepository;
import com.masai.security.TokenGenerator;

@Service
public class EmployeeServiceImpl implements EmployeeService{

	@Autowired
	private EmployeeRepository employeeRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private DepartmentRepository departmentRepo;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenGenerator tokenGenerator;
	
	@Autowired
	private PasswordEncoder encoder;
	
	//////////////////////////////////////////////////////////////////////////////////
	
	// login functionality...
	@Override
	public AuthenticatedResponseDto login(LoginDto loginDto) {
		
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = tokenGenerator.generateToken(authentication);
		
		return new AuthenticatedResponseDto(token);
	}

	
	
	
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
		empObj.setPassword(encoder.encode(empObj.getName()+""+employeeId));
		
		Employee employeeObj = employeeRepo.save(empObj);
		
		
		return modelMapper.map(employeeObj, AddEmployeeDto.class);
		
	}

	// this method will find the employee by using emlployee id or else it will throw user defined exception.
	
	@Override
	public GetEmployeeDto getEmployeeByEmpId(Integer employeeId) throws EmployeeException {
		
		 Employee employee = employeeRepo.findById(employeeId)
				 					     .orElseThrow(() -> new EmployeeException("Employee does not exist with this Id..."));
		 
		 if(employee.getEmployeeOrAdmin() != EmployeeOrAdmin.EMPLOYEE)
			 						throw new EmployeeException("Employee does not exist with this Id...");
		 
		 GetEmployeeDto getEmployeeDto = modelMapper.map(employee, GetEmployeeDto.class);
		 
		 getEmployeeDto.setDepartmentId(employee.getDepartment().getDepartmentId());
		 getEmployeeDto.setDepartmentName(employee.getDepartment().getDepartmentName());
		
		return getEmployeeDto; 
	}

	// this method will find the employee by using emlployee userName or else it will throw user defined exception.
	
	@Override
	public GetEmployeeDto getEmployeeByEmpUserName(String userName) throws EmployeeException {
		
		Employee employee = employeeRepo.findByUserName(userName).get();
		
		if(employee == null)
				throw new EmployeeException("No employee found with this userName");
		else {
			
		if(employee.getEmployeeOrAdmin() != EmployeeOrAdmin.EMPLOYEE)
					throw new EmployeeException("Employee does not exist with this Id...");

			
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
				
				if(employee.getEmployeeOrAdmin() != EmployeeOrAdmin.ADMIN) {
				
					GetEmployeeDto getEmployeeDto = modelMapper.map(employee, GetEmployeeDto.class);
					 
					getEmployeeDto.setDepartmentId(employee.getDepartment().getDepartmentId());
					getEmployeeDto.setDepartmentName(employee.getDepartment().getDepartmentName());
					
					dtoList.add(getEmployeeDto);
					
					
				}
				 
				
			}
			
			return dtoList;
		}
	}
	
	// this method will change the role of particular employee or else throw custom exception

	@Override
	public GetEmployeeDto changeEmployeeRole(Integer employeeId,Role role) throws EmployeeException {
		
		Employee employee = employeeRepo.findById(employeeId)
				                        .orElseThrow(() -> new EmployeeException("employee does not exist with this employeeId"));
		
		if(employee.getEmployeeOrAdmin() != EmployeeOrAdmin.EMPLOYEE)
					throw new EmployeeException("Employee does not exist with this Id...");

		
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

		if(employee.getEmployeeOrAdmin() != EmployeeOrAdmin.EMPLOYEE)
				throw new EmployeeException("Employee does not exist with this Id...");

		
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
		
		if(employee.getEmployeeOrAdmin() != EmployeeOrAdmin.EMPLOYEE)
								throw new EmployeeException("Employee does not exist with this Id...");

		
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
		
		if(employee.getEmployeeOrAdmin() != EmployeeOrAdmin.EMPLOYEE)
						throw new EmployeeException("Employee does not exist with this Id...");

		
		employeeRepo.delete(employee);
		
		GetEmployeeDto getEmployeeDto = modelMapper.map(employee, GetEmployeeDto.class);
		 
		getEmployeeDto.setDepartmentId(employee.getDepartment().getDepartmentId());
		getEmployeeDto.setDepartmentName(employee.getDepartment().getDepartmentName());
		
		return getEmployeeDto; 
		
	}




	@Override
	public GetEmployeeDto viewProfile() {
		
		Employee employee = getEmployee();
		
		return modelMapper.map(employee, GetEmployeeDto.class);
		
	}


	@Override
	public GetEmployeeDto updateEmployee(UpdateEmployeeDto dto) {
		
		Employee employee = getEmployee();
		
		employee.setName(dto.getName());
		employee.setEmail(dto.getEmail());
		employee.setGender(dto.getGender());
		employee.setDateOfBirth(dto.getDateOfBirth());
		
		Employee newEmployee = employeeRepo.save(employee);
		
		return modelMapper.map(newEmployee,GetEmployeeDto.class);
		
	}

	
	@Override
	public String updatePassword(UpdatePasswordDto dto) throws PasswordException {
		
		
		
		Employee employee = getEmployee();
		
		
		
		
		if(!dto.getNewPassword().equals(dto.getConfirmPassword()))
						throw new PasswordException("confirm password and new password need to be same");
		
		if(encoder.matches(dto.getNewPassword(), employee.getPassword()))
						throw new PasswordException("New password need to be different");
		
		Employee emp = employeeRepo.findByPassword(encoder.encode(dto.getNewPassword()));
		
		if(emp != null)
				throw new PasswordException("this password is not available");
		
		employee.setPassword(encoder.encode(dto.getNewPassword()));
		employeeRepo.save(employee);
		
		return "Password has been changed";
	}
	
	@Override
	public AdminDto viewProfileAdmin() {
		
		Employee admin = getEmployee();
		
		return modelMapper.map(admin,AdminDto.class);
	}
	
	

	@Override
	public AdminDto registerAdmin(Employee admin) {
		
		Employee savedAdmin = employeeRepo.save(admin);
		
		admin.setEmployeeId(savedAdmin.getEmployeeId());
		admin.setUserName(admin.getName() + savedAdmin.getEmployeeId());
		admin.setPassword(encoder.encode(admin.getName() + savedAdmin.getEmployeeId()));
		admin.setEmployeeOrAdmin(EmployeeOrAdmin.ADMIN);
		
		Employee completeSavedAdmin = employeeRepo.save(admin);
		
		return modelMapper.map(completeSavedAdmin, AdminDto.class);
	}


	
	

	@Override
	public Employee getEmployee() {
		
		Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		System.out.println(o);
		if(o.equals("anonymousUser")) throw new RuntimeException("Please login first...");
		
		UserDetails userDetails = (UserDetails)o;
		
		
		String username = userDetails.getUsername();
		
		
		return employeeRepo.findByUserName(username).orElseThrow(() -> new RuntimeException("user does not exist")); 
		
		
	}







	








	

	
	
	
	
}
