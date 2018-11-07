package com.softactive.core.object;

import com.softactive.core.utils.JdbcColumn;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "id", callSuper = false)
public class Indicator extends Pullable {
	private static final long serialVersionUID = 897553036426179530L;
	@JdbcColumn(field = "name", keyValue = JdbcColumn.KEY)
	protected String name;
	@JdbcColumn(field = "id")
	protected Integer id;
	@JdbcColumn(field = "api_code", keyValue = JdbcColumn.VALUE)
	protected String apiCode;
	@JdbcColumn(field = "unit", keyValue = JdbcColumn.VALUE)
	protected String unit;
	@JdbcColumn(field = "description", keyValue = JdbcColumn.VALUE)
	protected String description;
	@JdbcColumn(field = "source_code", keyValue = JdbcColumn.KEY)
	private String sourceCode;
	@JdbcColumn(field = "frequency_id", keyValue = JdbcColumn.KEY)
	private String frequencyId;
	@JdbcColumn(field = "adj_id", keyValue = JdbcColumn.KEY)
	private String adjustmentId;
	@JdbcColumn(field = "pre_code", keyValue = JdbcColumn.VALUE)
	private String preCode;
	@JdbcColumn(field = "def_term", keyValue = JdbcColumn.VALUE)
	private String defaultTerm;
	@JdbcColumn(field = "post_code", keyValue = JdbcColumn.VALUE)
	private String postCode;
	@JdbcColumn(field = "sub_source", keyValue = JdbcColumn.VALUE)
	private String subSource;
	@JdbcColumn(field = "excel_column_index", keyValue = JdbcColumn.VALUE)
	private Integer excelColumnIndex;
	@JdbcColumn(field = "category_id", keyValue = JdbcColumn.VALUE)
	protected Integer categoryId;
}
