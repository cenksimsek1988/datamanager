package com.softactive.core.object;

import com.softactive.core.utils.JdbcColumn;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@EqualsAndHashCode(of = "id", callSuper = false)
public class UpdateError extends Base{
	private static final long serialVersionUID = 3108218890104043054L;

	@JdbcColumn(field = "id")
	private Integer id;

	@JdbcColumn(field = "risk_factor_id", keyValue = JdbcColumn.VALUE)
	private Integer riskFactorId;
	
	@JdbcColumn(field = "type", keyValue = JdbcColumn.VALUE)
	private Integer type;
	
	@JdbcColumn(field = "region_name", keyValue = JdbcColumn.VALUE)
	private String regionName;

	@JdbcColumn(field = "indicator_name", keyValue = JdbcColumn.VALUE)
	private String indicatorName;

	@JdbcColumn(field = "source_id", keyValue = JdbcColumn.VALUE)
	private String sourceId;
	
	@JdbcColumn(field = "frequency_id", keyValue = JdbcColumn.VALUE)
	private String frequencyId;

	@JdbcColumn(field = "message", keyValue = JdbcColumn.VALUE)
	private String message;
}
