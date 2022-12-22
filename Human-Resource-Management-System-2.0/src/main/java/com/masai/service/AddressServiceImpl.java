package com.masai.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
	public Address addAddress(Address address) throws EmployeeException {
		
      	Integer empId = getEmployee().getEmployeeId();
		
		
		Employee employee = employeeRepo.findById(empId)
									    .orElseThrow(() -> new EmployeeException("employee does not exist with this id"));
		
		employee.getAddresses().add(address);
		address.setEmployee(employee);
		
		return addressRepo.save(address);
		
	}

	@Override
	public List<Address> getAllAddresses() throws AddressException, EmployeeException {
		
		Integer empId = getEmployee().getEmployeeId();
		
		Employee employee = employeeRepo.findById(empId)
			    						.orElseThrow(() -> new EmployeeException("employee does not exist with this id"));

		List<Address> addresses = employee.getAddresses();
		
		if(addresses.isEmpty())
				throw new AddressException("No adress found...");
		else return addresses;
	}

	@Override
	public Address updateAddress(Integer addressId, Address address)
			throws AddressException, EmployeeException {
		
		Integer empId = getEmployee().getEmployeeId();
		
		Employee employee = employeeRepo.findById(empId)
										.orElseThrow(() -> new EmployeeException("employee does not exist with this id"));

		// for checking whether this address present in the database or not
		 addressRepo.findById(addressId).orElseThrow(() -> new AddressException("no address found with this id"));
		
		 // for checking whether this address is associated with this address or not
		List<Address> addresses = employee.getAddresses()
										  .stream()
										  .filter((add) -> add.getAddressId() == addressId)
										  .collect(Collectors.toList());
		
		if(addresses.isEmpty())
				throw new AddressException("no address found with this id");
		
		
		address.setAddressId(addresses.get(0).getAddressId());
		
		address.setEmployee(employee);
		
		return addressRepo.save(address);
		
	}

	@Override
	@Transactional
	public Address deleteAddress(Integer addressId) throws AddressException, EmployeeException {
		
		Integer empId = getEmployee().getEmployeeId();
		
		Employee employee = employeeRepo.findById(empId)
										.orElseThrow(() -> new EmployeeException("employee does not exist with this id"));

		 addressRepo.findById(addressId).orElseThrow(() -> new AddressException("no address found with this id"));

		 // for checking whether this address is associated with this address or not
		List<Address> addresses = employee.getAddresses()
										  .stream()
										  .filter((add) -> add.getAddressId() == addressId)
										  .collect(Collectors.toList());
		
		if(addresses.isEmpty())
				throw new AddressException("No address found with this Id");
		
		Address addressObj = addresses.get(0);
		
		addressRepo.myDeleteMethod(addressObj.getAddressId());
		
		return addressObj;
	}
	
	
	
	@Override
	public Employee getEmployee() {
		
		Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		UserDetails userDetails = (UserDetails)o;
		
		String username = userDetails.getUsername();
		
		return employeeRepo.findByUserName(username).orElseThrow(() -> new RuntimeException("user does not exist")); 
		
		
	}

}
