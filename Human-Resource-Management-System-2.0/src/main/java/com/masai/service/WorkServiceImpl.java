package com.masai.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masai.Enum.EmployeeOrAdmin;
import com.masai.Enum.WorkStatus;
import com.masai.Enum.WorkType;
import com.masai.dto.WorkDto;
import com.masai.exception.EmployeeException;
import com.masai.exception.WorkException;
import com.masai.model.Employee;
import com.masai.model.Work;
import com.masai.repository.EmployeeRepository;
import com.masai.repository.WorkRepository;

@Service
public class WorkServiceImpl implements WorkService{

	@Autowired
	private WorkRepository workRepo;

	@Autowired
	private EmployeeRepository employeeRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public String assignWork(Integer empId, Work work) throws EmployeeException {
		
		Employee employee = employeeRepo.findById(empId).orElseThrow(() -> new EmployeeException("employee does not exist with this Id"));
		
		// In single table I am storing both admin and employee that's why chicking what is the role of employee
		if(employee.getEmployeeOrAdmin() !=  EmployeeOrAdmin.EMPLOYEE)
					throw new EmployeeException("employee does not exist with this Id");
		
		// setting the others values
		work.setStatus(WorkStatus.PENDING);
		work.setWorkType(WorkType.INDIVIDUAL);
		
		employee.getWorks().add(work);
		work.getEmployees().add(employee);
		
		workRepo.save(work);
		
		return "work has been assigned";
	}

	
	
	@Override
	public WorkDto deleteWork(Integer workId) throws WorkException {
	
		Work work = workRepo.findById(workId).orElseThrow(() -> new WorkException("No work present with this ID"));
		
		workRepo.delete(work);
		
		return modelMapper.map(work, WorkDto.class);
	}



	@Override
	public WorkDto updateWork(Integer workId, Work work) throws WorkException {
	
		Work workObj = workRepo.findById(workId).orElseThrow(() -> new WorkException("No work present with this ID"));
		
		// admin can update those works which have PENDING or NOTCOMPLETED status
		if(workObj.getStatus() == WorkStatus.COMPLETED)
					throw new WorkException("You can not update now, work is COMPLETED...");
			
		work.setWorkId(workId);
		Work savedWork = workRepo.save(work);	
		
		
		return modelMapper.map(savedWork, WorkDto.class);
	}
	
	
	
	
	
}
