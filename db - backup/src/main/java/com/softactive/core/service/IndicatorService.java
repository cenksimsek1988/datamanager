package com.softactive.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.softactive.core.object.Indicator;

@Repository
public class IndicatorService extends OrderableService<Indicator> {
	@Autowired
	private RiskFactorService rfs;
	public IndicatorService() {
		super("cmn_indicator");
	}

	@Override
	public void save(Indicator i) {
		Indicator in = findIndicatorById(i.getId());
		if(in==null) {
			insert(i);
		} else {
			update(i);
		}
	}
	
	public static Map<String, Object> getMap(Indicator i){
		Map<String, Object> answer = new HashMap<String, Object>();
		answer.put("adj_id", i.getAdjustmentId());
		answer.put("api_code", i.getApiCode());
		answer.put("def_term", i.getDefaultTerm());
		answer.put("description", i.getDescription());
		answer.put("frequency_id", i.getFrequencyId());
		System.out.println("mapa giren id: " + i.getId());
		answer.put("id", i.getId());
		answer.put("name", i.getName());
		answer.put("post_code", i.getPostCode());
		answer.put("pre_code", i.getPreCode());
		answer.put("source_code", i.getSourceCode());
		answer.put("sub_source", i.getSourceCode());
		answer.put("unit", i.getUnit());
		return answer;
	}
	
	@Override
	public void save(List<Indicator> indicators) {
		for(Indicator i:indicators) {
			Indicator in = findIndicatorById(i.getId());
			if(in==null) {
				insert(i);
			} else {
				update(i);
			}
		}
	}

	public Indicator findByApiCodeAndSources(String apiCode, List<String> sources) {
		String sql = initQuery() + " where api_code='" + apiCode + "' and source_code in ('";
		for(String src:sources) {
			sql += src + "','";
		}
		sql = sql.substring(0, sql.length()-2);
		sql += ")";
		List<Indicator> answer = query(sql);
		if(answer==null || answer.size()==0) {
			return null;
		}
		if (answer.size() == 1) {
			return answer.get(0);
		} else {
			System.out.println("found multiple indicators for indicator: " + apiCode + " pageCount: " + answer.size());
			delete(answer);
			query(sql);
		}
		return null;
	}
	
	public Indicator findByNameAndSource(String name, String source) {
		name = escapeChars(name);
		String sql = initQuery() + " where name='" + name + "' and source_code ='" + source + "'";
		List<Indicator> answer = query(sql);
		if(answer==null || answer.size()==0) {
			return null;
		}
		if (answer.size() == 1) {
			return answer.get(0);
		} else {
			System.out.println("found multiple indicators for indicator: " + name + " pageCount: " + answer.size());
			delete(answer);
		}
		return null;
	}
	
	private String escapeChars(String original) {
		return original.replaceAll("'", "\\\\'");
	}
	
	public void delete(List<Indicator> list) {
		String sql = getDeleteSql() + getWhereStatement(list);
		getJdbcTemplate().execute(sql);
		rfs.deleteForIndicators(list);
	}
	
	protected String getWhereStatement(List<Indicator> list) {
		String sql = " where id in ";
		for (Indicator i : list) {
			sql += i.getId() + ", ";
		}
		sql = sql.substring(0, sql.length() - 2) + ")";
		return sql;
	}

	public Indicator findIndicatorById(Integer id) {
		String sql = initQuery() + " where id=" + id;
		List<Indicator> answer = query(sql);
		if (answer.size() == 1) {
			return answer.get(0);
		}
		return null;
	}

	public List<Indicator> getListFrom(String src, Integer last) {
		String sql = initQuery() + " where (source_code='" + src + "' and id > " + last + ")";
		return query(sql);
	}

	public List<Indicator> getListFrom(String src, String frq, Integer last) {
		String sql = initQuery() + " where (source_code='" + src + "' and frequency_code='" + frq + "' and id > "
				+ last + ")";
		return query(sql);
	}

	public List<Indicator> getIndicatorsBySource(String src) {
		String sql = initQuery() + " where source_code='" + src + "'";
		return query(sql);
	}

	public List<Indicator> getIndicatorsBySourceCodeAndFrequency(String src, String frq) {
		String sql = initQuery() + " where (source_code='" + src + "' and frequency_code='" + frq + "')";
		return query(sql);
	}
}
