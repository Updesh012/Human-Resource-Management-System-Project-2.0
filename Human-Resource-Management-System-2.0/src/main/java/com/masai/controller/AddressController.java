package com.masai.controller;

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

import com.masai.exception.AddressException;
import com.masai.exception.EmployeeException;
import com.masai.model.Address;
import com.masai.service.AddressService;

@RestController
@RequestMapping("/address")
public class AddressController {

	@Autowired
	private AddressService addressService;
	
	@PostMapping("/{empId}")
	public ResponseEntity<Address> addAdressHandler(@PathVariable("empId") Integer empId,@RequestBody Address address) throws EmployeeException{
		
		Address addressObj = addressService.addAddress(empId, address);
		
		return new ResponseEntity<Address>(addressObj,HttpStatus.CREATED);
	}
	
	@PutMapping("/{empId}/{addressId}")
	public ResponseEntity<Address> updadteAdressHandler(@PathVariable("empId") Integer empId,@PathVariable("addressId") Integer addressId,@RequestBody Address address) throws EmployeeException, AddressException{
		
		Address addressObj = addressService.updateAddress(empId, addressId, address);
		
		return new ResponseEntity<Address>(addressObj,HttpStatus.CREATED);
	}
	
	
	@GetMapping("/{empId}")
	public ResponseEntity<List<Address>> getAdressesHandler(@PathVariable("empId") Integer empId) throws EmployeeException, AddressException{
		
		List<Address> addresses = addressService.getAllAddresses(empId);
		
		return new ResponseEntity<List<Address>>(addresses,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{empId}/{addressId}")
	public ResponseEntity<Address> deleteAdressHandler(@PathVariable("empId") Integer empId,@PathVariable("addressId") Integer addressId) throws EmployeeException, AddressException{
		
		Address addressObj = addressService.deleteAddress(empId, addressId);
		
		return new ResponseEntity<Address>(addressObj,HttpStatus.CREATED);
	}
	
	
	
}
