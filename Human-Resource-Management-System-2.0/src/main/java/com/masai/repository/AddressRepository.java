package com.masai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masai.model.Address;

public interface AddressRepository extends JpaRepository<Address, Integer>{

}
