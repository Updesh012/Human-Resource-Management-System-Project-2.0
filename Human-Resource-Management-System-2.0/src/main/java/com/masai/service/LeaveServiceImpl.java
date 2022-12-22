package com.masai.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.masai.Enum.LeaveStatus;
import com.masai.dto.LeaveDto;
import com.masai.exception.EmployeeException;
import com.masai.exception.LeaveException;
import com.masai.model.Employee;
import com.masai.model.Leave;
import com.masai.repository.EmployeeRepository;
import com.masai.repository.LeaveRepository;

@Service
public class LeaveServiceImpl implements LeaveService{

	@Autowired
	private EmployeeRepository employeeRepo;
	
	@Autowired
	private LeaveRepository leaveRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	@Transactional
	public LeaveDto addLeave(Leave leave) throws EmployeeException, LeaveException {
		
		// getting currently loggedIn employee Id
		Integer empId = getEmployee().getEmployeeId();
	
		// this will check whether employee is exist or not
		Employee employee = employeeRepo.findById(empId)
										.orElseThrow(() -> new EmployeeException("employee does not exist with this Id"));
		
		// this will filter all the leaves where the status is PENDING
		List<Leave> leaves = employee.getLeaves()
									 .stream()
									 .filter((lea) -> lea.getStatus() == LeaveStatus.PENDING)
									 .collect(Collectors.toList());
		
		// if leaves list is not empty it means this employee has already applied for a leave
		if(!leaves.isEmpty()) {
			
			Leave leavee = leaves.get(0);
			// this will give the day difference between currentdate and leave end date
			// if diff is negative means current date is greater than leave end date
			// means now leave has expired so there is no meaning of that leave
			// that's why i am deleting that leave;
			Integer dayDiff = leaveRepo.getDaysDiff(leavee.getLeaveId());
			
			if(dayDiff <0) {
				leaveRepo.myDeleteMethod(leavee.getLeaveId());
				
			}else {
				// means leave is not expire so I am throw and saying you have already applied for an leave
				throw new LeaveException("You have already applied for a leave");
			}
			
		}
			
		// this is custom query it will find the difference between latest accepted leave request and current date if it will come negative
		// it means employee are on leave and he can not apply for leave.
		
		List<Integer> acceptedLeave = leaveRepo.findLatestAcceptedLeaveReq(empId);
		if(!acceptedLeave.isEmpty() && acceptedLeave.get(0) < 0)
								throw new LeaveException("You are on leave already, you can not apply for leave...");
		
		leave.setStatus(LeaveStatus.PENDING);
		
		employee.getLeaves().add(leave);
		leave.setEmployee(employee);
		
		Leave leaveObj = leaveRepo.save(leave);
		
		
		return modelMapper.map(leaveObj, LeaveDto.class);
		
	}

	
	// this method will update the pending leave request only
	@Override
	public LeaveDto updateLeave(Leave leave) throws EmployeeException, LeaveException {
		
		// getting currently loggedIn employee Id
		Integer empId = getEmployee().getEmployeeId();
			
		
		Employee employee = employeeRepo.findById(empId).orElseThrow(() -> new EmployeeException("employee does not exist with this Id"));
		
		// gives all the leaves of that employee
		List<Leave> getAllLeaves = employee.getLeaves();
		
		// if leaves list is empty means there is no leave and he can not update request
		if(getAllLeaves.isEmpty())
				throw new LeaveException("You have not applied for any leave");
		
		// it will filter all the leaves those has the status pending 
		List<Leave> getPendingLeave = employee.getLeaves()
				 					 		  .stream()
				 					          .filter((lea) -> lea.getStatus() == LeaveStatus.PENDING)
				 					          .collect(Collectors.toList());

		// if list is empty means admin already has applied for his leave now he can not applied
		if(getPendingLeave.isEmpty())
					throw new LeaveException("you can not update leave because admin has already responded to your leave request...");
			
		// if the flow is here means now he can update the request
		// i am sure there can be at most only one request with status pending
		int leaveId = getPendingLeave.get(0).getLeaveId();
		
		Leave leaveObj = leaveRepo.findById(leaveId).get();
		
		System.out.println(leaveObj.getLeaveId());
		leave.setLeaveId(leaveObj.getLeaveId());
		leave.setStatus(LeaveStatus.PENDING);
		
		// important line because i am not use cascade at children side
		leave.setEmployee(employee);
		Leave updateLeave = leaveRepo.save(leave);
		
		
		return modelMapper.map(updateLeave, LeaveDto.class);
	}

	
	
	
	// this will delete the leave request those have the status pending
	@Override
	@Transactional
	public LeaveDto deletePendingLeave() throws EmployeeException, LeaveException {
		
		// getting currently loggedIn employee Id
		Integer empId = getEmployee().getEmployeeId();
			
		
		Employee employee = employeeRepo.findById(empId).orElseThrow(() -> new EmployeeException("employee does not exist with this Id"));
		
		// gives all the leaves of that employee
		List<Leave> getAllLeaves = employee.getLeaves();
		
		// if leaves list is empty means there is no leave and he can not update request
		if(getAllLeaves.isEmpty())
				throw new LeaveException("You have not applied for any leave");
		
		// it will filter all the leaves those has the status pending 
		List<Leave> getPendingLeave = employee.getLeaves()
				 					 		  .stream()
				 					          .filter((lea) -> lea.getStatus() == LeaveStatus.PENDING)
				 					          .collect(Collectors.toList());

		// if list is empty means admin already has applied for his leave now he can not applied
		if(getPendingLeave.isEmpty())
					throw new LeaveException("you can not update leave because admin has already responded to your leave request...");
			
		// if the flow is here means now he can update the request
		// i am sure there can be at most only one request with status pending
		Leave leave = getPendingLeave.get(0);

		// never forget this line spend more than 6 hours
		leaveRepo.myDeleteMethod(leave.getLeaveId());
		
		
		return modelMapper.map(leave, LeaveDto.class);
	}


	
	// this method will return the latest leave status
	@Override
	@Transactional
	public LeaveDto checkLatestLeaveStatus() throws EmployeeException, LeaveException {
		
		// getting currently loggedIn employee Id
		Integer empId = getEmployee().getEmployeeId();
			
		
		Employee employee = employeeRepo.findById(empId).orElseThrow(() -> new EmployeeException("employee does not exist with this Id"));
		
		// gives all the leaves of that employee
		List<Leave> getAllLeaves = employee.getLeaves();
		
		// if leaves list is empty means there is no leave 
		if(getAllLeaves.isEmpty())
				throw new LeaveException("You have not applied for any leave");
		
		// it will filter all the leaves those has the status pending 
		List<Leave> getPendingLeave = employee.getLeaves()
				 					 		  .stream()
				 					          .filter((lea) -> lea.getStatus() == LeaveStatus.PENDING)
				 					          .collect(Collectors.toList());

		// if the list is not empty means there is a leave with pending status
		if(!getPendingLeave.isEmpty()) {
			
			Leave leave = getPendingLeave.get(0);
			// this will give the day difference between currentdate and leave end date
			// if diff is negative means current date is greater than leave end date
			// means now leave has expired so there is no meaning of that leave
			// that's why i am deleting that leave and throwing exception;
			Integer dayDiff = leaveRepo.getDaysDiff(leave.getLeaveId());
			
			if(dayDiff <0) {
				leaveRepo.myDeleteMethod(leave.getLeaveId());
				throw new LeaveException("You leave has expired, now you can apply again...");
			}
				
			// if the flow is here means admin has not responded yet.
			throw new LeaveException("Admin has not responded yet...");		
			
		}
		
		// if the flow is here it means there are no leaves with PENDING status
		// this will give me the list of Integer meaning leaveId in desending order of leaveEndDate.
		List<Integer> ids = leaveRepo.findLeaves(empId);
			
		// i am getting the first element means latest ACCEPTED or REJECTED leave
		Leave leave = leaveRepo.findById(ids.get(0)).get();
		
		// here mapping the leave into leaveDto using modelmapper
		return modelMapper.map(leave, LeaveDto.class);
		
	}
	
	// this method will give all leaves of a particular user 
	@Override
	public List<LeaveDto> getAllLeaves() throws EmployeeException, LeaveException {
		
		// getting currently loggedIn employee Id
		Integer empId = getEmployee().getEmployeeId();
					
		Employee employee = employeeRepo.findById(empId).orElseThrow(() -> new EmployeeException("employee does not exist with this Id"));
				
		// gives all the leaves of that employee
		List<Leave> getAllLeaves = employee.getLeaves();
		
		if(getAllLeaves.isEmpty())
					throw new LeaveException("No leaves found...");
		
	


		
		List<LeaveDto> dtoList = new ArrayList<>();
		
		for(Leave leave : getAllLeaves) {
			dtoList.add(modelMapper.map(leave, LeaveDto.class));
		}
		
		return dtoList;
		
	}
	
	
	
	@Override
	public Employee getEmployee() {
		
		Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		UserDetails userDetails = (UserDetails)o;
		
		String username = userDetails.getUsername();
		
		return employeeRepo.findByUserName(username).orElseThrow(() -> new RuntimeException("user does not exist")); 
		
		
	}
	


	@Override
	public List<LeaveDto> getAllLeavesHistory() throws LeaveException {
		
		List<Leave> leaves = leaveRepo.findAll();
		
		if(leaves.isEmpty()) throw new LeaveException("No leave Found...");
		
		List<LeaveDto> dtoList = new ArrayList<>();
		
		for(Leave leave : leaves) {
			dtoList.add(modelMapper.map(leave, LeaveDto.class));
		}
		
		return dtoList;
	}


	@Override
	public List<LeaveDto> getLeavesOfParticularEmployee(Integer empId) throws LeaveException, EmployeeException {
		
		Employee employee = employeeRepo.findById(empId)
										.orElseThrow(() -> new EmployeeException("Employee does not exist with this Id"));
		
		List<Leave> leaves = employee.getLeaves();
		
		if(leaves.isEmpty()) throw new LeaveException("No leaves Found...");
		
		List<LeaveDto> dtoList = new ArrayList<>();
		
		for(Leave leave : leaves) {
			dtoList.add(modelMapper.map(leave, LeaveDto.class));
		}
		
		return dtoList;
		
	}


	@Override
	public List<LeaveDto> getPendingLeaves() throws LeaveException {
		
		List<LeaveDto> dtoList = getAllLeavesHistory();
		
		if(dtoList.isEmpty()) throw new LeaveException("no leaves found...");
		
		return dtoList.stream()
					  .filter((dto) -> dto.getStatus() == LeaveStatus.PENDING)
					  .collect(Collectors.toList());
		
	}


	@Override
	@Transactional
	public LeaveDto responseToLeave(Integer leaveId,LeaveStatus status) throws LeaveException {
	
		Leave leave = leaveRepo.findById(leaveId).orElseThrow(() -> new LeaveException("No leave found with this Id"));
		
		if(leave.getStatus() != LeaveStatus.PENDING)
						throw new LeaveException("You have already responded...");
		
		Integer dayDiff = leaveRepo.getDaysDiff(leave.getLeaveId());
		
		if(dayDiff <0) {
			leaveRepo.myDeleteMethod(leave.getLeaveId());
			throw new LeaveException("leave has expired...");
		}
		
		leave.setStatus(status);
		
		leaveRepo.save(leave);
		
		return modelMapper.map(leave,LeaveDto.class);
	}


		
		
	
	

	
	
	
	
	

}
