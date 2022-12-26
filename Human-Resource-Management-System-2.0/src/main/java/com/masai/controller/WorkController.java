package com.masai.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masai.dto.GroupWorkDto;
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
	
	/*
	 * 
	 * work related functionality
	 * 
	 * */
	
	
}
