package com.softactive.core.object;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = { "userCode", "role" }, callSuper = false)
public class UserRole extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4375790241264237740L;
	private String userCode;
	private String role;

}
