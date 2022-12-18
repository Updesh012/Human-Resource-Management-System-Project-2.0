package com.masai.service;

import java.util.List;

import com.masai.exception.AddressException;
import com.masai.exception.EmployeeException;
import com.masai.model.Address;

public interface AddressService {

	public Address addAddress(Integer empId, Address address) throws EmployeeException;
	
	public List<Address> getAllAddresses(Integer empId) throws AddressException,EmployeeException;
	
	public Address updateAddress(Integer empId,Integer addressId ,Address address) throws AddressException,EmployeeException;
	
	public Address deleteAddress(Integer empId, Integer addressId) throws AddressException,EmployeeException;
	
}
