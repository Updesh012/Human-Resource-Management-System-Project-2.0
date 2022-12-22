package com.masai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.masai.model.Work;

@Repository
public interface WorkRepository extends JpaRepository<Work, Integer>{

}
