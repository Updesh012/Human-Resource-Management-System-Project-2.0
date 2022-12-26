package com.masai.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masai.Enum.EmployeeOrAdmin;
import com.masai.Enum.WorkStatus;
import com.masai.Enum.WorkType;
import com.masai.dto.GroupWorkDto;
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
			
		// setting the values 
		work.setWorkId(workId);
		// if the deadline date is not same means he change the deadline and Status should be PENDING 
		if(work.getDeadLine() == workObj.getDeadLine()) {
			work.setStatus(workObj.getStatus());
		}else {
			work.setStatus(WorkStatus.PENDING);
		}
		work.setStartDate(workObj.getStartDate());
		work.setWorkType(workObj.getWorkType());
		
		// here I am not using casCade on work side that's why we have to set the employee in work side
		List<Employee> employees = workObj.getEmployees();
		work.getEmployees().addAll(employees);
		// saving the work
		Work savedWork = workRepo.save(work);	
		
		
		return modelMapper.map(savedWork, WorkDto.class);
	}



	@Override
	public String groupWork(GroupWorkDto dto) throws EmployeeException {
		
		// here I am checking whether all the employeesId is present in the list is available in 
		// database or not
		List<Integer> employeesId = dto.getEmployeesId();
		for(int id : employeesId) {
			Optional<Employee> opt = employeeRepo.findById(id);
			
			if(!opt.isPresent()) throw new EmployeeException("Please pass valid employees ID");
			
			if(opt.get().getEmployeeOrAdmin() == EmployeeOrAdmin.ADMIN) throw new EmployeeException("Please pass valid employees ID");
		}
		
		// here I am using hashmap for checking the frequency of employee id 
		// if frequecy of any employee Id is > 1 means that employee present more than one times in list
		// and we can not assign the same work more than one times to same employee.
		HashMap<Integer,Integer> hm = new HashMap<>();
		
		for(Integer id : employeesId) {
			
			if(hm.get(id) == null) {
				hm.put(id,1);
			}else {
				throw new EmployeeException("Please remove duplicates employee Id...");
			}
			
		}
		// setting some other details
		dto.getWork().setWorkType(WorkType.GROUP);
		dto.getWork().setStatus(WorkStatus.PENDING);
		
		for(int id : employeesId) {
			Employee employee = employeeRepo.findById(id).get();
			employee.getWorks().add(dto.getWork());
			dto.getWork().getEmployees().add(employee);
		}
		
		
		workRepo.save(dto.getWork());
		
		return "Work has been assigned...";
	}
	
	
	
	
	
}
