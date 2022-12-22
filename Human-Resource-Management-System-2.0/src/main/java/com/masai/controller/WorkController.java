package com.masai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masai.dto.WorkDto;
import com.masai.exception.EmployeeException;
import com.masai.exception.WorkException;
import com.masai.model.Work;
import com.masai.service.WorkService;

@RestController
@RequestMapping("/admin")
public class WorkController {

	@Autowired
	private WorkService workService;
	
	@PostMapping("/work/{empId}")
	public ResponseEntity<String> assignIndividualWork(@PathVariable("empId") Integer empId,@RequestBody Work work) throws EmployeeException{
		
		String str = workService.assignWork(empId, work);
		
		
		return new ResponseEntity<String>(str,HttpStatus.OK);
	}
	
	@DeleteMapping("/work/{workId}")
	public ResponseEntity<WorkDto> deleteWork(@PathVariable("workId") Integer workId) throws WorkException{
		
		WorkDto dto  = workService.deleteWork(workId);
		
		
		return new ResponseEntity<WorkDto>(dto,HttpStatus.OK);
	}
	
	@PutMapping("/work/{workId}")
	public ResponseEntity<WorkDto> updateWork(@PathVariable("workId") Integer workId,@RequestBody Work work) throws WorkException{
		
		WorkDto dto = workService.updateWork(workId, work);
		
		
		return new ResponseEntity<WorkDto>(dto,HttpStatus.OK);
	}
	
	
	
	
	
}
