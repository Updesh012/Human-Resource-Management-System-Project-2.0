package com.masai.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masai.exception.AddressException;
import com.masai.exception.EmployeeException;
import com.masai.model.Address;
import com.masai.model.Employee;
import com.masai.repository.AddressRepository;
import com.masai.repository.EmployeeRepository;

@Service
public class AddressServiceImpl implements AddressService{

	@Autowired
	private EmployeeRepository employeeRepo;
	
	@Autowired
	private AddressRepository addressRepo;
	
	// this method will add the address.
	
	@Override
	public Address addAddress(Integer empId, Address address) throws EmployeeException {
		
		Employee employee = employeeRepo.findById(empId)
									    .orElseThrow(() -> new EmployeeException("employee does not exist with this id"));
		
		employee.getAddresses().add(address);
		address.setEmployee(employee);
		
		return addressRepo.save(address);
		
	}

	@Override
	public List<Address> getAllAddresses(Integer empId) throws AddressException, EmployeeException {
		
		Employee employee = employeeRepo.findById(empId)
			    						.orElseThrow(() -> new EmployeeException("employee does not exist with this id"));

		List<Address> addresses = addressRepo.findAll();
		
		if(addresses.isEmpty())
				throw new AddressException("No adress found...");
		else return addresses;
	}

	@Override
	public Address updateAddress(Integer empId, Integer addressId, Address address)
			throws AddressException, EmployeeException {
		
		Employee employee = employeeRepo.findById(empId)
										.orElseThrow(() -> new EmployeeException("employee does not exist with this id"));

		Address addressObj = addressRepo.findById(addressId)
										.orElseThrow(() -> new AddressException("no address found with this id"));
		
		address.setAddressId(addressObj.getAddressId());
		
		address.setEmployee(employee);
		
		return addressRepo.save(address);
		
	}

	@Override
	public Address deleteAddress(Integer empId, Integer addressId) throws AddressException, EmployeeException {
		
		Employee employee = employeeRepo.findById(empId)
										.orElseThrow(() -> new EmployeeException("employee does not exist with this id"));

		Address addressObj = addressRepo.findById(addressId)
										.orElseThrow(() -> new AddressException("no address found with this id"));

		addressRepo.delete(addressObj);
		
		return addressObj;
	}
	
	

}
