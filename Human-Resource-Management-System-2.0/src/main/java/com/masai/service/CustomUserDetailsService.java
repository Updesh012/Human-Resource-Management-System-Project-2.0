package com.masai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.masai.model.Employee;
import com.masai.repository.EmployeeRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private EmployeeRepository employeeRepo;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		Employee user = employeeRepo.findByUserName(username)
									.orElseThrow(() -> new UsernameNotFoundException("user does not exist with this user name "+ username));
		
		return new CustomUserDetails(user);
		
	}

}
