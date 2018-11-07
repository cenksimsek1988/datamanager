package com.softactive.core.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.softactive.core.object.CountryStandard;

@Repository
public class CountryStandardService extends AbstractDataService<CountryStandard>{

	protected CountryStandardService() {
		super("country_standard");
	}
	
	public List<CountryStandard> getCountryStandardsByIsoCode(String isoCode) {
		String sql = initQuery() + " where iso_code='" + isoCode + "'";
		return query(sql);
	}
	
	public List<CountryStandard> getCountryStandardsByStandardNames(List<String> names) {
		if(names == null || names.size()<1){
			return null;
		}
		String sql = initQuery() + " where standard_name in (" + getWhereStatement(names);
		return query(sql);
	}
	
	public List<CountryStandard> getCountryStandardsByStandardIsoCodes(List<String> isoCodes) {
		if(isoCodes == null || isoCodes.size()<1){
			return null;
		}
		String sql = initQuery() + " where iso_code in (" + getWhereStatement(isoCodes);
		return query(sql);
	}
	
	public List<CountryStandard> getAlternativeCountryStandardsByStandardNames(List<String> names) {
		if(names == null || names.size()<1){
			return null;
		}
		String sql = initQuery() + " where standard_name in (" + getWhereStatement(names);
		return query(sql);
	}
	
	protected String getWhereStatement(List<String> list) {
		String sql = "";
		for (String s : list) {
			sql += "'" + s + "', ";
		}
		sql = sql.substring(0, sql.length() - 2) + ")";
		return sql;
	}

	@Override
	public void save(CountryStandard t) {
		CountryStandard in = find(t.getId());
		if(in==null) {
			insert(t);
		} else {
			t.setId(in.getId());
			update(t);
		}
	}
}
