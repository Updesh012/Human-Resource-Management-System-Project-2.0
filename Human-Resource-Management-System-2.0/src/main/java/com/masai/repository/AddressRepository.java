package com.masai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.masai.model.Address;

public interface AddressRepository extends JpaRepository<Address, Integer>{

	@Modifying
	@Query("DELETE FROM Address WHERE addressId = ?1")
	public void myDeleteMethod(Integer AddressId);

	
	
}
