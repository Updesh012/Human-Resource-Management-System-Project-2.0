package com.masai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.masai.model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer>{

	public Department findByDepartmentName(String deptName);
	
}
