package com.softactive.core.object;

import java.util.List;

import lombok.Data;

@Data
public class ApiUser {

	private Integer id;
	private String firstname;
	private String lastname;
	private String email;
	private String status;
	private String role;
	private String company;
	private String group_id;
	private String country;
	private String city;
	private String phone;
	private String job_function;
	private List<String> project_codes;
	
}
