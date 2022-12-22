package com.masai.service;

import java.util.List;

import com.masai.exception.AddressException;
import com.masai.exception.EmployeeException;
import com.masai.model.Address;
import com.masai.model.Employee;

public interface AddressService {

	public Employee getEmployee();
	
	public Address addAddress(Address address) throws EmployeeException;
	
	public List<Address> getAllAddresses() throws AddressException,EmployeeException;
	
	public Address updateAddress(Integer addressId ,Address address) throws AddressException,EmployeeException;
	
	public Address deleteAddress(Integer addressId) throws AddressException,EmployeeException;
	
}
