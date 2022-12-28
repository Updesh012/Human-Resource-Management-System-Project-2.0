package com.masai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.masai.model.Work;

@Repository
public interface WorkRepository extends JpaRepository<Work, Integer>{

	@Query("SELECT TIME_TO_SEC(TIMEDIFF(deadLine,current_date()))/86400 FROM Work WHERE workId = ?1")
	public Integer getDaysDiff(Integer workId); 
	
	
}
