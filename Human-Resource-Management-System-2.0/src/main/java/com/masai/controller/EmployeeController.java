package com.masai.controller;

import java.util.List;

import javax.validation.Valid;

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

import com.masai.dto.AuthenticatedResponseDto;
import com.masai.dto.LeaveDto;
import com.masai.dto.LoginDto;
import com.masai.exception.AddressException;
import com.masai.exception.EmployeeException;
import com.masai.exception.LeaveException;
import com.masai.model.Address;
import com.masai.model.Leave;
import com.masai.service.AddressService;
import com.masai.service.EmployeeService;
import com.masai.service.LeaveService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private LeaveService leaveService;
	
	/*
	 * 
	 * Employee Login 
	 * 
	 * */
	
	
	@PostMapping("/login")
	public ResponseEntity<AuthenticatedResponseDto> login(@Valid @RequestBody LoginDto loginDto){
		
		AuthenticatedResponseDto authenticatedResponseDto = employeeService.login(loginDto);
		
		return new ResponseEntity<AuthenticatedResponseDto>(authenticatedResponseDto,HttpStatus.OK);
	}
	
	/*
	 * 
	 * Address related resource
	 * 
	 * 
	 * */
	
	@PostMapping("/address")
	public ResponseEntity<Address> addAdressHandler(@RequestBody Address address) throws EmployeeException{
		
		Address addressObj = addressService.addAddress(address);
		
		return new ResponseEntity<Address>(addressObj,HttpStatus.CREATED);
	}
	
	@PutMapping("/address/{addressId}")
	public ResponseEntity<Address> updadteAdressHandler(@PathVariable("addressId") Integer addressId,@RequestBody Address address) throws EmployeeException, AddressException{
		
		Address addressObj = addressService.updateAddress(addressId, address);
		
		return new ResponseEntity<Address>(addressObj,HttpStatus.CREATED);
	}
	
	
	@GetMapping("/address")
	public ResponseEntity<List<Address>> getAdressesHandler() throws EmployeeException, AddressException{
		
		List<Address> addresses = addressService.getAllAddresses();
		
		return new ResponseEntity<List<Address>>(addresses,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/address/{addressId}")
	public ResponseEntity<Address> deleteAdressHandler(@PathVariable("addressId") Integer addressId) throws EmployeeException, AddressException{
		
		Address addressObj = addressService.deleteAddress(addressId);
		
		return new ResponseEntity<Address>(addressObj,HttpStatus.CREATED);
	}
	
	/*
	 * 
	 * 
	 * Leave Related Resources
	 * 
	 * */
	
	@PostMapping("/leave")
	public ResponseEntity<LeaveDto> addLeaveHandler(@RequestBody Leave leave) throws EmployeeException, LeaveException{
		
		LeaveDto leaveDto = leaveService.addLeave(leave);
		
		return new ResponseEntity<LeaveDto>(leaveDto, HttpStatus.CREATED);
	}
	
	
	@PutMapping("/leave")
	public ResponseEntity<LeaveDto> updateLeaveHandler(@RequestBody Leave leave) throws EmployeeException, LeaveException{
		
		LeaveDto leaveDto = leaveService.updateLeave(leave);
		
		return new ResponseEntity<LeaveDto>(leaveDto, HttpStatus.CREATED);
	}
	
	
	@DeleteMapping("/leave")
	public ResponseEntity<LeaveDto> deleteLeaveHandler() throws EmployeeException, LeaveException{
		
		LeaveDto leaveDto = leaveService.deletePendingLeave();

		
		return new ResponseEntity<LeaveDto>(leaveDto, HttpStatus.CREATED);
		
	}
	
	
	
	@GetMapping("/leave")
	public ResponseEntity<LeaveDto> getRecentLeaveStatusHandler() throws EmployeeException, LeaveException{
		
		LeaveDto leaveDto = leaveService.checkLatestLeaveStatus();

		
		return new ResponseEntity<LeaveDto>(leaveDto, HttpStatus.CREATED);
		
	}
	
	@GetMapping("/allLeaves")
	public ResponseEntity<List<LeaveDto>> getAllLeaves() throws EmployeeException,LeaveException{
		
		List<LeaveDto> dtos = leaveService.getAllLeaves();
		
		return new ResponseEntity<List<LeaveDto>>(dtos,HttpStatus.OK);
	}

	
	
	
	
	
	
}

