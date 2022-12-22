package com.masai.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.masai.Enum.WorkStatus;
import com.masai.Enum.WorkType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Work {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	public Integer workId;
	
	private String name;
	
	private String description;
	
	@CreationTimestamp
	@JsonIgnore
	private LocalDate startDate;
	
	private LocalDate deadLine;
	
	@JsonIgnore
	@Enumerated(EnumType.STRING)
	private WorkType workType;
	
	@JsonIgnore
	@Enumerated(EnumType.STRING)
	private WorkStatus status;
	
	@ManyToMany
	@JsonIgnore
	private List<Employee> employees = new ArrayList<>();
	
}
