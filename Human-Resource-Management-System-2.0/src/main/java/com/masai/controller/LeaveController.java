package com.masai.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masai.dto.LeaveDto;
import com.masai.exception.EmployeeException;
import com.masai.exception.LeaveException;
import com.masai.model.Leave;
import com.masai.repository.LeaveRepository;
import com.masai.service.LeaveService;

@RestController
@RequestMapping("/leaves")
public class LeaveController {

	@Autowired
	private LeaveService leaveService;
	
	@Autowired
	private LeaveRepository leaveRepo;
	
	
	@PostMapping("/{empId}")
	public ResponseEntity<LeaveDto> addLeaveHandler(@PathVariable("empId") Integer empId,@RequestBody Leave leave) throws EmployeeException, LeaveException{
		
		LeaveDto leaveDto = leaveService.addLeave(empId, leave);
		
		return new ResponseEntity<LeaveDto>(leaveDto, HttpStatus.CREATED);
	}
	
	
	@PutMapping("/{empId}")
	public ResponseEntity<LeaveDto> updateLeaveHandler(@PathVariable("empId") Integer empId,@RequestBody Leave leave) throws EmployeeException, LeaveException{
		
		LeaveDto leaveDto = leaveService.updateLeave(empId, leave);
		
		return new ResponseEntity<LeaveDto>(leaveDto, HttpStatus.CREATED);
	}
	
	
	@DeleteMapping("/{empId}")
	public ResponseEntity<LeaveDto> deleteLeaveHandler(@PathVariable("empId") Integer empId) throws EmployeeException, LeaveException{
		
		LeaveDto leaveDto = leaveService.deletePendingLeave(empId);

		
		return new ResponseEntity<LeaveDto>(leaveDto, HttpStatus.CREATED);
		
	}
	
	
	
	@GetMapping("/{empId}")
	public ResponseEntity<LeaveDto> getRecentLeaveStatusHandler(@PathVariable("empId") Integer empId) throws EmployeeException, LeaveException{
		
		LeaveDto leaveDto = leaveService.checkLatestLeaveStatus(empId);

		
		return new ResponseEntity<LeaveDto>(leaveDto, HttpStatus.CREATED);
		
	}

	
	
	
	
	
	@GetMapping("checking")
	public List<Integer> check(){
		
		int x = leaveRepo.getDaysDiff(21);
		System.out.println(x);
		return null;
	}
	
	
	
	
}
