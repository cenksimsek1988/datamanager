package com.softactive.core.object;

import com.softactive.core.utils.JdbcColumn;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "id", callSuper = false)
public class Save extends Base {
	// WB names

	/**
	 * 
	 */
	private static final long serialVersionUID = 3288020036820801615L;
	@JdbcColumn(field = "id", keyValue = JdbcColumn.KEY)
	protected String id;
	@JdbcColumn(field = "last_index", keyValue = JdbcColumn.VALUE)
	protected String last;

}