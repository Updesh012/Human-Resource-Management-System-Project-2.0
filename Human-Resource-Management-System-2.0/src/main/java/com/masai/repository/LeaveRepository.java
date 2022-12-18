package com.masai.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.masai.model.Leave; 

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Integer>{

	
	@Query("SELECT DATEDIFF(CURRENT_DATE(), leaveEndDate) FROM Leave WHERE employee_employee_id = ?1 AND status = 'ACCEPTED' ORDER BY leaveEndDate DESC")
	public List<Integer> findLatestAcceptedLeaveReq(Integer empid);
	
	@Query("SELECT leaveId FROM Leave WHERE employee_employee_id = ?1 AND (status = 'ACCEPTED' OR status = 'REJECTED') ORDER BY leaveEndDate DESC")
	public List<Integer> findLeaves(Integer empId);

	@Modifying
	@Query("DELETE FROM Leave WHERE leaveId = ?1")
	public void myDeleteMethod(Integer leaveId);

	@Query("SELECT TIME_TO_SEC(TIMEDIFF(leaveEndDate,current_date()))/86400 FROM Leave WHERE leaveId = ?1")
	public Integer getDaysDiff(Integer leaveId); 
	
	
}
