package com.masai.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
		work.setLeaderId(empId);
		
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
		work.setLeaderId(workObj.getLeaderId());
		
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
		
		// here I am checking that leader Is belongs to list of employees id or Not
		if(!employeesId.contains(dto.getLeaderId()))throw new EmployeeException("Please Pass valid leader id") ;
		
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
		dto.getWork().setLeaderId(dto.getLeaderId());
		
		for(int id : employeesId) {
			Employee employee = employeeRepo.findById(id).get();
			employee.getWorks().add(dto.getWork());
			dto.getWork().getEmployees().add(employee);
		}
		
		
		workRepo.save(dto.getWork());
		
		return "Work has been assigned...";
	}



	@Override
	public List<WorkDto> getAllWorks() throws WorkException {
		
		List<Work> works = workRepo.findAll();
		
		if(works.isEmpty()) throw new WorkException("No work present...");
		
		List<WorkDto> worksDto = new ArrayList<>();
		
		for(Work work : works) {
			
			worksDto.add(modelMapper.map(work,WorkDto.class));
		}
		
		return worksDto;
	}



	@Override
	public List<WorkDto> getAllIndividualWorks() throws WorkException {
		
		List<Work> allWorks = workRepo.findAll();
		
		List<Work> individualWorks = allWorks.stream()
											 .filter((w) -> w.getWorkType() == WorkType.INDIVIDUAL)
											 .collect(Collectors.toList());
		
		if(individualWorks.isEmpty()) throw new WorkException("No works found...");
		
		List<WorkDto> worksDto = new ArrayList<>();
		
		for(Work work : individualWorks) {
			
			worksDto.add(modelMapper.map(work,WorkDto.class));
		}
		
		return worksDto;
		
	}



	@Override
	public List<WorkDto> getAllGroupWorks() throws WorkException {
	
		List<Work> allWorks = workRepo.findAll();
		
		List<Work> GroupWorks = allWorks.stream()
										.filter((w) -> w.getWorkType() == WorkType.GROUP)
										.collect(Collectors.toList());
		
		if(GroupWorks.isEmpty()) throw new WorkException("No works found...");
		
		List<WorkDto> worksDto = new ArrayList<>();
		
		for(Work work : GroupWorks) {
			
			worksDto.add(modelMapper.map(work,WorkDto.class));
		}
		
		return worksDto;
		
		
	}



	@Override
	public List<WorkDto> getAllPendingWorks() throws WorkException {
	
		List<Work> allWorks = workRepo.findAll();
		
		List<Work> pendingWorks = allWorks.stream()
										  .filter((w) -> w.getStatus() == WorkStatus.PENDING)
				                          .collect(Collectors.toList());

		
		
		if(pendingWorks.isEmpty()) throw new WorkException("No work present...");
		
		List<WorkDto> worksDto = new ArrayList<>();
		
		for(Work work : pendingWorks) {
			
			worksDto.add(modelMapper.map(work,WorkDto.class));
		}
		
		return worksDto;
		
	}



	@Override
	public List<WorkDto> getAllIndividualPendingWorks() throws WorkException {
		
		List<Work> allWorks = workRepo.findAll();
		
		List<Work> individualPendingWorks = allWorks.stream()
													.filter((w) -> w.getStatus() == WorkStatus.PENDING && w.getWorkType() == WorkType.INDIVIDUAL)
													.collect(Collectors.toList());

		
		
		if(individualPendingWorks.isEmpty()) throw new WorkException("No work present...");
		
		List<WorkDto> worksDto = new ArrayList<>();
		
		for(Work work : individualPendingWorks) {
			
			worksDto.add(modelMapper.map(work,WorkDto.class));
		}
		
		return worksDto;
		
	}



	@Override
	public List<WorkDto> getAllGroupPendingWorks() throws WorkException {
		
		List<Work> allWorks = workRepo.findAll();
		
		List<Work> GroupPendingWorks = allWorks.stream()
													.filter((w) -> w.getStatus() == WorkStatus.PENDING && w.getWorkType() == WorkType.GROUP)
													.collect(Collectors.toList());

		
		
		if(GroupPendingWorks.isEmpty()) throw new WorkException("No work present...");
		
		List<WorkDto> worksDto = new ArrayList<>();
		
		for(Work work : GroupPendingWorks) {
			
			worksDto.add(modelMapper.map(work,WorkDto.class));
		}
		
		return worksDto;
		
		
	}



	@Override
	public List<WorkDto> getAllCompletedWorks() throws WorkException {
		
		List<Work> allWorks = workRepo.findAll();
		
		List<Work> CompletedWorks = allWorks.stream()
										    .filter((w) -> w.getStatus() == WorkStatus.COMPLETED)
				                            .collect(Collectors.toList());

		
		
		if(CompletedWorks.isEmpty()) throw new WorkException("No work present...");
		
		List<WorkDto> worksDto = new ArrayList<>();
		
		for(Work work : CompletedWorks) {
			
			worksDto.add(modelMapper.map(work,WorkDto.class));
		}
		
		return worksDto;
		
	}



	@Override
	public List<WorkDto> getAllIndividualCompletedWorks() throws WorkException {
		
		List<Work> allWorks = workRepo.findAll();
		
		List<Work> individualCompletedWorks = allWorks.stream()
													  .filter((w) -> w.getStatus() == WorkStatus.COMPLETED && w.getWorkType() == WorkType.INDIVIDUAL)
													  .collect(Collectors.toList());

		
		
		if(individualCompletedWorks.isEmpty()) throw new WorkException("No work present...");
		
		List<WorkDto> worksDto = new ArrayList<>();
		
		for(Work work : individualCompletedWorks) {
			
			worksDto.add(modelMapper.map(work,WorkDto.class));
		}
		
		return worksDto;
	}



	@Override
	public List<WorkDto> getAllGroupCompletedWorks() throws WorkException {
		
		List<Work> allWorks = workRepo.findAll();
		
		List<Work> GroupCompletedWorks = allWorks.stream()
												 .filter((w) -> w.getStatus() == WorkStatus.COMPLETED && w.getWorkType() == WorkType.GROUP)
												 .collect(Collectors.toList());

		
		
		if(GroupCompletedWorks.isEmpty()) throw new WorkException("No work present...");
		
		List<WorkDto> worksDto = new ArrayList<>();
		
		for(Work work : GroupCompletedWorks) {
			
			worksDto.add(modelMapper.map(work,WorkDto.class));
		}
		
		return worksDto;
		
		
	}



	@Override
	public List<WorkDto> getAllInCompletedWorks() throws WorkException {
		
		List<Work> allWorks = workRepo.findAll();
		
		List<Work> inCompletedWorks = allWorks.stream()
										      .filter((w) -> w.getStatus() == WorkStatus.NOTCOMPLETED)
				                              .collect(Collectors.toList());

		
		
		if(inCompletedWorks.isEmpty()) throw new WorkException("No work present...");
		
		List<WorkDto> worksDto = new ArrayList<>();
		
		for(Work work : inCompletedWorks) {
			
			worksDto.add(modelMapper.map(work,WorkDto.class));
		}
		
		return worksDto;
		
	}



	@Override
	public List<WorkDto> getAllIndividualInCompletedWorks() throws WorkException {
		
		List<Work> allWorks = workRepo.findAll();
		
		List<Work> individualInCompletedWorks = allWorks.stream()
													    .filter((w) -> w.getStatus() == WorkStatus.NOTCOMPLETED && w.getWorkType() == WorkType.INDIVIDUAL)
													    .collect(Collectors.toList());

		
		
		if(individualInCompletedWorks.isEmpty()) throw new WorkException("No work present...");
		
		List<WorkDto> worksDto = new ArrayList<>();
		
		for(Work work : individualInCompletedWorks) {
			
			worksDto.add(modelMapper.map(work,WorkDto.class));
		}
		
		return worksDto;
	}



	@Override
	public List<WorkDto> getAllGroupInCompletedWorks() throws WorkException {
		
		List<Work> allWorks = workRepo.findAll();
		
		List<Work> GroupInCompletedWorks = allWorks.stream()
												   .filter((w) -> w.getStatus() == WorkStatus.NOTCOMPLETED && w.getWorkType() == WorkType.GROUP)
												   .collect(Collectors.toList());

		
		
		if(GroupInCompletedWorks.isEmpty()) throw new WorkException("No work present...");
		
		List<WorkDto> worksDto = new ArrayList<>();
		
		for(Work work : GroupInCompletedWorks) {
			
			worksDto.add(modelMapper.map(work,WorkDto.class));
		}
		
		return worksDto;
	}



	@Override
	public List<WorkDto> getAllEmployeeWork() throws EmployeeException, WorkException {
		
		Employee employee = getEmployee();
		
		List<Work> works = employee.getWorks();
		
		if(works.isEmpty()) throw new WorkException("No work found...");
		
		List<WorkDto> workDtos = new ArrayList<>();
		
		// here I am checking for those work in which current date is greater than their deadline and I am setting
		// NOTCOMPLETED from those work which has the status PENDING
		for(Work work : works) {
			
			Integer dayDiff = workRepo.getDaysDiff(work.getWorkId());
			if(dayDiff < 0 && work.getStatus() == WorkStatus.PENDING) {
				work.setStatus(WorkStatus.NOTCOMPLETED);

				workRepo.save(work);
			}
			
			WorkDto dto = modelMapper.map(work, WorkDto.class);
			workDtos.add(dto);
		}
		
		
		return workDtos;
	}
	
	@Override
	public String changeStatusToCompleted(Integer workId) throws EmployeeException, WorkException {
	
		Employee employee = getEmployee();
		
		workRepo.findById(workId).orElseThrow(() -> new WorkException("No work present with this work ID"));
		
		// here I am checking whether this work is associated with current employee or not 
		List<Work> works = employee.getWorks()
								   .stream()
								   .filter((w) -> w.getWorkId() == workId)
								   .collect(Collectors.toList());
		
		if(works.isEmpty()) throw new WorkException("No work present with this work ID");
		
		Work work = works.get(0);
		
		// Here I am checking whether Employee is Leader or not because Only leader can change the workStatus
		if(employee.getEmployeeId() != work.getLeaderId())
						throw new EmployeeException("You are not the Leader, you can not change the status...");
		
		if(work.getStatus() == WorkStatus.NOTCOMPLETED)
						throw new WorkException("Deadline Has passed, now you can not change status...");
		
		work.setStatus(WorkStatus.COMPLETED);
		workRepo.save(work);
		
		return "Marked as Completed...";
	}
	
	
	
	
	
	@Override
	public Employee getEmployee() {
		
		Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		UserDetails userDetails = (UserDetails)o;
		
		String username = userDetails.getUsername();
		
		return employeeRepo.findByUserName(username).orElseThrow(() -> new RuntimeException("user does not exist")); 
		
		
	}



	
	
}
