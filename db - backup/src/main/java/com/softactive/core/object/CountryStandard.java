package com.softactive.core.object;

import com.softactive.core.utils.JdbcColumn;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "id", callSuper = false)
public class CountryStandard extends Base{
	private static final long serialVersionUID = 7886126440670140663L;
	@JdbcColumn(field = "id")
	private Integer id;
	@JdbcColumn(field = "name", keyValue = JdbcColumn.VALUE)
	private String name;
	@JdbcColumn(field = "standard_name", keyValue = JdbcColumn.VALUE)
	private String standardName;
	@JdbcColumn(field = "iso_code", keyValue = JdbcColumn.VALUE)
	private String isoCode;
}
