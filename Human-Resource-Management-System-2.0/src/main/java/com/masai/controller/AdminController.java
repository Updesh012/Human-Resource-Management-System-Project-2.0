package com.masai.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masai.Enum.EmployeeOrAdmin;
import com.masai.Enum.LeaveStatus;
import com.masai.Enum.Role;
import com.masai.dto.AddEmployeeDto;
import com.masai.dto.AdminDto;
import com.masai.dto.AuthenticatedResponseDto;
import com.masai.dto.DepartmentDto;
import com.masai.dto.GetEmployeeDto;
import com.masai.dto.GroupWorkDto;
import com.masai.dto.LeaveDto;
import com.masai.dto.LoginDto;
import com.masai.dto.UpdatePasswordDto;
import com.masai.dto.WorkDto;
import com.masai.exception.DepartmentException;
import com.masai.exception.EmployeeException;
import com.masai.exception.LeaveException;
import com.masai.exception.PasswordException;
import com.masai.exception.WorkException;
import com.masai.model.Department;
import com.masai.model.Employee;
import com.masai.model.Work;
import com.masai.repository.EmployeeRepository;
import com.masai.service.DepartmentService;
import com.masai.service.EmployeeService;
import com.masai.service.LeaveService;
import com.masai.service.WorkService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private LeaveService leaveService;
	

	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private EmployeeRepository employeeRepo;
	
	@Autowired
	private WorkService workService;
	

	/*
	 * 
	 * Admin Login and profile related resources
	 * 
	 * */
	
	@GetMapping("/register")
	public ResponseEntity<String> addAdmin() {
		
		Optional<Employee> opt = employeeRepo.findByUserName("admin1");

		if(!opt.isPresent()) {
			// first admin is hardcoded...

			Employee admin = new Employee();
			admin.setName("admin");
			admin.setUserName("admin1");
			admin.setSalary(100000.0);
			admin.setRole(null);
			admin.setPassword(encoder.encode("admin1"));
			admin.setEmployeeOrAdmin(EmployeeOrAdmin.ADMIN);
			
			employeeRepo.save(admin);
		}
		
		return new ResponseEntity<String>("admin has been registered",HttpStatus.CREATED);
	}
	
	
	
	

	// admin login resource
	@PostMapping("/login")
	public ResponseEntity<AuthenticatedResponseDto> login(@Valid @RequestBody LoginDto loginDto) {

		AuthenticatedResponseDto authenticatedResponseDto = employeeService.login(loginDto);

		return new ResponseEntity<AuthenticatedResponseDto>(authenticatedResponseDto, HttpStatus.OK);
	}
	
	
	@GetMapping("/viewProfile")
	public ResponseEntity<AdminDto> viewProfile() {

		AdminDto getAdminDto = employeeService.viewProfileAdmin();

		return new ResponseEntity<AdminDto>(getAdminDto, HttpStatus.OK);
	}
	
	@PostMapping("/registerAdmin")
	public ResponseEntity<AdminDto> registerAdmin(@RequestBody Employee employee) {

		AdminDto getAdminDto = employeeService.registerAdmin(employee);

		return new ResponseEntity<AdminDto>(getAdminDto, HttpStatus.OK);
	}
	
	
	@PutMapping("/updatePassword")
	public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordDto dto) throws PasswordException{
		
		String message = employeeService.updatePassword(dto);
		
		return new ResponseEntity<String>(message,HttpStatus.OK);
	}

	
	
	
	/*
	 * 
	 * Employee Related Resources
	 * 
	 * */

	// this resourse will registered the employee with department

	@PostMapping("/employees/{deptId}")
	public ResponseEntity<AddEmployeeDto> registerEmployeeHandler(@PathVariable("deptId") Integer departmentId,
			@RequestBody Employee employee) throws DepartmentException {

		AddEmployeeDto addEmployeeDto = employeeService.addEmployee(departmentId, employee);

		return new ResponseEntity<AddEmployeeDto>(addEmployeeDto, HttpStatus.CREATED);
	}

	// this resourse will find the employee by using employeeId

	@GetMapping("/employees/byEmployeeId/{empId}")
	public ResponseEntity<GetEmployeeDto> getEmployeeByEmpIdHandler(@PathVariable("empId") Integer employeeId)
			throws EmployeeException {

		GetEmployeeDto getEmployeeDto = employeeService.getEmployeeByEmpId(employeeId);

		return new ResponseEntity<GetEmployeeDto>(getEmployeeDto, HttpStatus.OK);
	}

	// this resourse will find the employee by using employee userName

	@GetMapping("/employees/byUserName/{userName}")
	public ResponseEntity<GetEmployeeDto> getEmployeeByEmpUserNameHandler(@PathVariable("userName") String userName)
			throws EmployeeException {

		GetEmployeeDto getEmployeeDto = employeeService.getEmployeeByEmpUserName(userName);

		return new ResponseEntity<GetEmployeeDto>(getEmployeeDto, HttpStatus.OK);
	}

	// this resourse will give all employees

	@GetMapping("/employees")
	public ResponseEntity<List<GetEmployeeDto>> getAllEmployeesHandler() throws EmployeeException {

		List<GetEmployeeDto> getEmployeeDtos = employeeService.getAllEmployees();

		return new ResponseEntity<List<GetEmployeeDto>>(getEmployeeDtos, HttpStatus.OK);
	}

	// this resource will change the employee role

	@PatchMapping("/employees/setNewRole/{id}/{role}")
	public ResponseEntity<GetEmployeeDto> changeEmployeeRoleHandler(@PathVariable("id") Integer employeeId,
			@PathVariable("role") Role newRole) throws EmployeeException {

		GetEmployeeDto getEmployeeDto = employeeService.changeEmployeeRole(employeeId, newRole);

		return new ResponseEntity<GetEmployeeDto>(getEmployeeDto, HttpStatus.OK);
	}

	// this resource will set the new salary of an employee

	@PatchMapping("/employees/setNewSalary/{id}/{salary}")
	public ResponseEntity<GetEmployeeDto> changeEmployeeSalaryHandler(@PathVariable("id") Integer employeeId,
			@PathVariable("salary") Double newSalary) throws EmployeeException {

		GetEmployeeDto getEmployeeDto = employeeService.changeEmployeeSalary(employeeId, newSalary);

		return new ResponseEntity<GetEmployeeDto>(getEmployeeDto, HttpStatus.OK);
	}

	// this resource will change the department of an employee

	@PatchMapping("/employees/setNewDepartment/{empId}/{deptId}")
	public ResponseEntity<GetEmployeeDto> changeEmployeeDepartmentHandler(@PathVariable("empId") Integer employeeId,
			@PathVariable("deptId") Integer departmentId) throws EmployeeException, DepartmentException {

		GetEmployeeDto getEmployeeDto = employeeService.changeEmployeeDepartment(employeeId, departmentId);

		return new ResponseEntity<GetEmployeeDto>(getEmployeeDto, HttpStatus.OK);
	}

	// this resource will provide the api for deleting an employee

	@DeleteMapping("/employees/{empId}")
	public ResponseEntity<GetEmployeeDto> deleteEmployeeHandler(@PathVariable("empId") Integer employeeId)
			throws EmployeeException {

		GetEmployeeDto getEmployeeDto = employeeService.deleteEmployee(employeeId);

		return new ResponseEntity<GetEmployeeDto>(getEmployeeDto, HttpStatus.OK);
	}
	
	/*
	 * 
	 * Department Related Resource
	 * 
	 * 
	 * */
	
	// this resource for adding department.
	@PostMapping("/departments")
	public ResponseEntity<DepartmentDto> addDepartmentHandler(@RequestBody Department department) throws DepartmentException{
		
		DepartmentDto departmentDto = departmentService.addDepartment(department);
		
		return new ResponseEntity<DepartmentDto>(departmentDto, HttpStatus.CREATED);
	}
	
	
	
	//	this resource for updating existing department.
	@PutMapping("/departments/{id}")
	public ResponseEntity<DepartmentDto> updateDepartmentNameHandler(@PathVariable("id") Integer deptId,@RequestBody Department department) throws DepartmentException{
		
		DepartmentDto departmentDto = departmentService.updateDepartmentName(deptId, department);
		
		return new ResponseEntity<DepartmentDto>(departmentDto,HttpStatus.OK);
	}
	
	// this resource for deleting the existing department
	@DeleteMapping("/departments/{id}")
	public ResponseEntity<DepartmentDto> deleteDepartmentHandler(@PathVariable("id") Integer deptId) throws DepartmentException{
		
		DepartmentDto departmentDto = departmentService.deleteDepartment(deptId);
		
		return new ResponseEntity<DepartmentDto>(departmentDto,HttpStatus.OK);
	}
	
	// this resource will return the particular department
	
	@GetMapping("/departments/{id}")
	public ResponseEntity<DepartmentDto> getDepartmentByIdHandler(@PathVariable("id") Integer deptId) throws DepartmentException{
		
		DepartmentDto departmentDto = departmentService.getDepartmentById(deptId);
		
		return new ResponseEntity<DepartmentDto>(departmentDto,HttpStatus.OK);
	}
	
	// this resource will return all the department
	
	@GetMapping("/departments")
	public ResponseEntity<List<DepartmentDto>> getAllDepartmentsHandler() throws DepartmentException{
		
		List<DepartmentDto> departments = departmentService.getAllDepartments();
		
		return new ResponseEntity<List<DepartmentDto>>(departments,HttpStatus.OK);
	}
	
	// this resource will return the department
	
	@GetMapping("/departments/name/{name}")
	public ResponseEntity<DepartmentDto> getDepartmentByNameHandler(@PathVariable("name") String deptName) throws DepartmentException{
		
		DepartmentDto departments = departmentService.getDepartmentByName(deptName);
		
		return new ResponseEntity<DepartmentDto>(departments,HttpStatus.OK);
	}
	
	
	// this resource will return all the departments after sorting them in alphabatical order
	
	@GetMapping("/departments/sortByNameAsc")
	public ResponseEntity<List<DepartmentDto>> getDepartmentsInAlphabaticalOrderHandler() throws DepartmentException{
		
		List<DepartmentDto> departments = departmentService.getDepartmentsInAlphabaticalOrder();
		
		return new ResponseEntity<List<DepartmentDto>>(departments,HttpStatus.OK);
	}
	
	
	// this resource will return all the departments after sorting them in Reverse alphabatical order
	
	@GetMapping("/departments/sortByNameDesc")
	public ResponseEntity<List<DepartmentDto>> getDepartmentsInReverseAlphabaticalOrderHandler() throws DepartmentException{
		
		List<DepartmentDto> departments = departmentService.getDepartmentsInReverseAlphabaticalOrder();
		
		return new ResponseEntity<List<DepartmentDto>>(departments,HttpStatus.OK);
	}
	
	
	/*
	 * 
	 * Leave Related Resource
	 * 
	 * */
	
	@GetMapping("/allLeaves")
	public ResponseEntity<List<LeaveDto>> getAllLeaves() throws LeaveException {
		
		List<LeaveDto> dtos = leaveService.getAllLeavesHistory();
		
		return new ResponseEntity<List<LeaveDto>>(dtos,HttpStatus.OK);
	}
	
	@GetMapping("/allLeaves/{empId}")
	public ResponseEntity<List<LeaveDto>> getLeavesOfParticularEmployee(@PathVariable("empId") Integer empId) throws LeaveException, EmployeeException {
		
		List<LeaveDto> dtos = leaveService.getLeavesOfParticularEmployee(empId);
		
		return new ResponseEntity<List<LeaveDto>>(dtos,HttpStatus.OK);
	}
	
	@GetMapping("/allPendingLeaves")
	public ResponseEntity<List<LeaveDto>> getPendingLeaves() throws LeaveException {
		
		List<LeaveDto> dtos = leaveService.getPendingLeaves();
		
		return new ResponseEntity<List<LeaveDto>>(dtos,HttpStatus.OK);
	}
	
	@PatchMapping("/response/{leaveId}/{status}")
	public ResponseEntity<LeaveDto> responseToLeave(@PathVariable("leaveId") Integer leaveId,@PathVariable("status") LeaveStatus status) throws LeaveException{
		
		LeaveDto dto = leaveService.responseToLeave(leaveId, status);
		
		return new ResponseEntity<LeaveDto>(dto,HttpStatus.OK);
	}
	
	/*
	 * 
	 * work related functionality
	 * 
	 * */
	
	@PostMapping("/work/{empId}")
	public ResponseEntity<String> assignIndividualWork(@PathVariable("empId") Integer empId,@RequestBody Work work) throws EmployeeException{
		
		String str = workService.assignWork(empId, work);
		
		
		return new ResponseEntity<String>(str,HttpStatus.OK);
	}
	
	@DeleteMapping("/work/{workId}")
	public ResponseEntity<WorkDto> deleteWork(@PathVariable("workId") Integer workId) throws WorkException{
		
		WorkDto dto  = workService.deleteWork(workId);
		
		
		return new ResponseEntity<WorkDto>(dto,HttpStatus.OK);
	}
	
	@PutMapping("/work/{workId}")
	public ResponseEntity<WorkDto> updateWork(@PathVariable("workId") Integer workId,@RequestBody Work work) throws WorkException{
		
		WorkDto dto = workService.updateWork(workId, work);
		
		
		return new ResponseEntity<WorkDto>(dto,HttpStatus.OK);
	}
	
	@PostMapping("/work")
	public ResponseEntity<String> assignGroupWork(@RequestBody GroupWorkDto dto) throws EmployeeException{
		
		String str = workService.groupWork(dto);
		
		
		return new ResponseEntity<String>(str,HttpStatus.OK);
	}
	
	@GetMapping("/allWorks")
	public ResponseEntity<List<WorkDto>> getAllWorks() throws WorkException{
		
		List<WorkDto> dtos = workService.getAllWorks();
		
		return new ResponseEntity<List<WorkDto>>(dtos,HttpStatus.OK);
	}
	
	@GetMapping("/allIndividualWorks")
	public ResponseEntity<List<WorkDto>> getAllIndividualWorks() throws WorkException{
		
		List<WorkDto> dtos = workService.getAllIndividualWorks();
		
		return new ResponseEntity<List<WorkDto>>(dtos,HttpStatus.OK);
	}
	
	@GetMapping("/allGroupWorks")
	public ResponseEntity<List<WorkDto>> getAllGroupWorks() throws WorkException{
		
		List<WorkDto> dtos = workService.getAllGroupWorks();
		
		return new ResponseEntity<List<WorkDto>>(dtos,HttpStatus.OK);
	}
	
	@GetMapping("/allPendingWorks")
	public ResponseEntity<List<WorkDto>> getAllPendingWorks() throws WorkException{
		
		List<WorkDto> dtos = workService.getAllPendingWorks();
		
		return new ResponseEntity<List<WorkDto>>(dtos,HttpStatus.OK);
	}
	
	@GetMapping("/allIndividualPendingWorks")
	public ResponseEntity<List<WorkDto>> getAllIndividualPendingWorks() throws WorkException{
		
		List<WorkDto> dtos = workService.getAllIndividualPendingWorks();
		
		return new ResponseEntity<List<WorkDto>>(dtos,HttpStatus.OK);
	}
	
	@GetMapping("/allGroupPendingWorks")
	public ResponseEntity<List<WorkDto>> getAllGroupPendingWorks() throws WorkException{
		
		List<WorkDto> dtos = workService.getAllGroupPendingWorks();
		
		return new ResponseEntity<List<WorkDto>>(dtos,HttpStatus.OK);
	}
	
	@GetMapping("/allCompletedWorks")
	public ResponseEntity<List<WorkDto>> getAllCompletedWorks() throws WorkException{
		
		List<WorkDto> dtos = workService.getAllCompletedWorks();
		
		return new ResponseEntity<List<WorkDto>>(dtos,HttpStatus.OK);
	}
	
	@GetMapping("/allIndividualCompletedWorks")
	public ResponseEntity<List<WorkDto>> getAllIndividualCompletedWorks() throws WorkException{
		
		List<WorkDto> dtos = workService.getAllIndividualCompletedWorks();
		
		return new ResponseEntity<List<WorkDto>>(dtos,HttpStatus.OK);
	}
	
	@GetMapping("/allGroupCompletedWorks")
	public ResponseEntity<List<WorkDto>> getAllGroupCompletedWorks() throws WorkException{
		
		List<WorkDto> dtos = workService.getAllGroupCompletedWorks();
		
		return new ResponseEntity<List<WorkDto>>(dtos,HttpStatus.OK);
	}
	
	@GetMapping("/allInCompletedWorks")
	public ResponseEntity<List<WorkDto>> getAllInCompletedWorks() throws WorkException{
		
		List<WorkDto> dtos = workService.getAllInCompletedWorks();
		
		return new ResponseEntity<List<WorkDto>>(dtos,HttpStatus.OK);
	}
	
	@GetMapping("/allIndividualInCompletedWorks")
	public ResponseEntity<List<WorkDto>> getAllIndividualInCompletedWorks() throws WorkException{
		
		List<WorkDto> dtos = workService.getAllIndividualInCompletedWorks();
		
		return new ResponseEntity<List<WorkDto>>(dtos,HttpStatus.OK);
	}
	
	@GetMapping("/allGroupInCompletedWorks")
	public ResponseEntity<List<WorkDto>> getAllGroupInCompletedWorks() throws WorkException{
		
		List<WorkDto> dtos = workService.getAllGroupInCompletedWorks();
		
		return new ResponseEntity<List<WorkDto>>(dtos,HttpStatus.OK);
	}
	
	

}
