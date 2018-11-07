package com.softactive.core.object;

import java.sql.Date;

import com.softactive.core.utils.JdbcColumn;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "id", callSuper = false)
public class RiskFactor extends Base{
	private static final long serialVersionUID = -908150266455822668L;
	@JdbcColumn(field = "region_id", keyValue = JdbcColumn.KEY)
	private Integer regionId;
	@JdbcColumn(field = "indicator_id", keyValue = JdbcColumn.KEY)
	private Integer indicatorCode;
	@JdbcColumn(field = "id")
	private Integer id;
	@JdbcColumn(field = "source_code", keyValue = JdbcColumn.VALUE)
	private String dataSourceCode;
	@JdbcColumn(field = "api_code", keyValue = JdbcColumn.VALUE)
	private String apiCode;
	@JdbcColumn(field = "frequency_code", keyValue = JdbcColumn.KEY)
	private String frequencyCode;
	@JdbcColumn(field = "update_date", keyValue = JdbcColumn.VALUE)
	private Date updateDate;
	@JdbcColumn(field = "start_date", keyValue = JdbcColumn.VALUE)
	private Date startDate;
	@JdbcColumn(field = "end_date", keyValue = JdbcColumn.VALUE)
	private Date endDate;
}
