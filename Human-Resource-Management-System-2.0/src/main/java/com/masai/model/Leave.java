package com.masai.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.ManyToAny;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.masai.Enum.LeaveStatus;

import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

@Entity
@Table(name = "Leave_Table")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Leave {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Integer leaveId;
	
	private String reason;
	
	@Enumerated(EnumType.STRING)
	@JsonIgnore
	private LeaveStatus status;
	
	
	
	private LocalDate leaveStartDate;
	
	

//	@Future(message = "Leave end date must be in future And format must be dd/mm/yyyy")
	private LocalDate leaveEndDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Employee employee;

	@Override
	public String toString() {
		return "Leave [leaveId=" + leaveId + ", reason=" + reason + ", status=" + status + ", leaveStartDate="
				+ leaveStartDate + ", leaveEndDate=" + leaveEndDate + "]";
	}
	
	
}
