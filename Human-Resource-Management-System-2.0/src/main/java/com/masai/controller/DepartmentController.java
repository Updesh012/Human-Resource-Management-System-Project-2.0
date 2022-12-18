package com.masai.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.masai.dto.DepartmentDto;
import com.masai.exception.DepartmentException;
import com.masai.model.Department;
import com.masai.service.DepartmentService;

@RestController
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;
	
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
	
}
