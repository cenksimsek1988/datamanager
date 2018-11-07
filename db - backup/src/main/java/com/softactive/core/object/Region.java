package com.softactive.core.object;

import com.softactive.core.utils.JdbcColumn;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@EqualsAndHashCode(of = "id", callSuper = false)
public class Region extends Pullable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2931783654283404298L;

	@JdbcColumn(field = "id")
	private Integer id;

	@JdbcColumn(field = "name", keyValue = JdbcColumn.VALUE)
	private String name;

	@JdbcColumn(field = "code_3", keyValue = JdbcColumn.VALUE)
	private String code3;

	@JdbcColumn(field = "currency", keyValue = JdbcColumn.VALUE)
	private String currency;

	@JdbcColumn(field = "currency_code", keyValue = JdbcColumn.VALUE)
	private String currencyCode;

	@JdbcColumn(field = "landlocked", keyValue = JdbcColumn.VALUE)
	private Boolean landlocked;

	@JdbcColumn(field = "distance", keyValue = JdbcColumn.VALUE)
	private Integer distance;

	@JdbcColumn(field = "group_code", keyValue = JdbcColumn.VALUE)
	private String groupCode;

	@JdbcColumn(field = "sub_continent_id", keyValue = JdbcColumn.VALUE)
	private Integer subContinentId;

	@JdbcColumn(field = "legal_origin_code", keyValue = JdbcColumn.VALUE)
	private String legalOriginCode;

	@JdbcColumn(field = "region_code", keyValue = JdbcColumn.VALUE)
	private String regionCode;
	@JdbcColumn(field = "admin_region_code", keyValue = JdbcColumn.VALUE)
	private String adminRegionCode;

	@JdbcColumn(field = "income_code", keyValue = JdbcColumn.VALUE)
	private String incomeCode;

	@JdbcColumn(field = "capital", keyValue = JdbcColumn.VALUE)
	private String capital;

	@JdbcColumn(field = "lending_code", keyValue = JdbcColumn.VALUE)
	private String lendingCode;

	@JdbcColumn(field = "fr", keyValue = JdbcColumn.VALUE)
	private Boolean fr;

	@JdbcColumn(field = "pr", keyValue = JdbcColumn.VALUE)
	private Boolean pr;

	@JdbcColumn(field = "wb", keyValue = JdbcColumn.VALUE)
	private Boolean wb;

	@JdbcColumn(field = "ef", keyValue = JdbcColumn.VALUE)
	private Boolean ef;

	@JdbcColumn(field = "us", keyValue = JdbcColumn.VALUE)
	private Boolean us;

	@JdbcColumn(field = "re", keyValue = JdbcColumn.VALUE)
	private Boolean re;

	@JdbcColumn(field = "iso_code", keyValue = JdbcColumn.VALUE)
	private String isoCode;

	@JdbcColumn(field = "aggregate", keyValue = JdbcColumn.VALUE)
	private Boolean aggregate;
}
