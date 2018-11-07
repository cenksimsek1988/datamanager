package com.softactive.core.object;

import java.io.Serializable;
import java.util.Date;

import com.softactive.core.utils.JdbcColumn;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "userCode", callSuper = false)
public class User extends Base implements Serializable {

	private static final long serialVersionUID = -4164594316884525175L;

	@JdbcColumn(field = "id")
	private Integer id;
	@JdbcColumn(field = "user_code", keyValue = JdbcColumn.KEY)
	private String userCode;
	@JdbcColumn(field = "full_name", keyValue = JdbcColumn.VALUE)
	private String fullname;
	@JdbcColumn(field = "company_name", keyValue = JdbcColumn.VALUE)
	private String companyName;
	@JdbcColumn(field = "email_address", keyValue = JdbcColumn.VALUE)
	private String emailAddress;
	@JdbcColumn(field = "statu", keyValue = JdbcColumn.VALUE)
	private String statu;
	@JdbcColumn(field = "user_password", keyValue = JdbcColumn.VALUE)
	private String userPassword;
	@JdbcColumn(field = "user_type", keyValue = JdbcColumn.VALUE)
	private String userType;
	@JdbcColumn(field = "update_date", keyValue = JdbcColumn.VALUE)
	private Date updatedDate;

}