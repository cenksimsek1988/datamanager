package com.softactive.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.softactive.core.object.Region;

@Repository
public class RegionService extends AbstractDataService<Region> {
	private final String BASE_UPDATE_SQL = "update " + getTableName() + " set (";
	
	@Autowired
	RiskFactorService rfs;
	
	public RegionService() {
		super("cmn_region");
	}

	public List<Region> getListFrom(String src, String last) {
		String sql = initQuery() + " where (source_code='" + src + "' and id > '" + last + "')";
		return query(sql);
	}

	public List<Region> getRegionsBySource(String src, boolean in) {
		String sql = initQuery() + " where " + src.toLowerCase() + "=" + in;
		return query(sql);
	}

	public Region findRegionByIsoCode(String isoCode) {
		String sql = initQuery() + " where iso_code='" + isoCode + "'";
		List<Region> answer = query(sql);
		if(answer==null || answer.size()==0) {
			return null;
		}
		if (answer.size() == 1) {
			return answer.get(0);
		} else {
			delete(answer);
			return null;
		}
	}
	
	public Region findRegionById(Integer id) {
		String sql = initQuery() + " where id=" + id;
		List<Region> answer = query(sql);
		if(answer==null || answer.size()==0) {
			return null;
		}
		if (answer.size() == 1) {
			return answer.get(0);
		} else {
			delete(answer);
			return null;
		}
	}
	
	public void delete(List<Region> list) {
		String sql = getDeleteSql() + getWhereStatement(list);
		getJdbcTemplate().execute(sql);
		rfs.deleteForRegions(list);
	}
	
	@Override
	public void save(Region r) {
		Region in = findRegionByIsoCode(r.getIsoCode());
		if(in==null) {
			insert(r);
		}
	}

	public void setSource(List<Region> regions, String src, boolean include) {
		if(regions.size()==0) {
			return;
		}
		String sql = getUpdateSql() + src.toLowerCase() + "=" + include + " " + getWhereStatement(regions);
		getJdbcTemplate().execute(sql);
		if(!include) {
			rfs.deleteForRegionsAndSource(regions, src);
		}
	}
	
	public void setSource(String regionId, String src, boolean include) {
		if(regionId==null || regionId.length() < 2) {
			return;
		}
		String sql = getUpdateSql() + src.toLowerCase() + "=" + include + " where id='" + regionId + "'";
		getJdbcTemplate().execute(sql);
		if(!include) {
			rfs.deleteForRegionIdAndSource(regionId, src);
		}
	}

	protected String getWhereStatement(List<Region> list) {
		String sql = " where id in (";
		for (Region r : list) {
			sql += "'" + r.getId() + "', ";
		}
		sql = sql.substring(0, sql.length() - 2) + ")";
		return sql;
	}
	
	public List<Region> getRegionsByIds(List<String> ids) {
		String sql = initQuery() + getWhereStatementFromIds(ids);
		return query(sql);
	}
	
	protected String getWhereStatementFromIds(List<String> list) {
		String sql = " where id in (";
		for (String s : list) {
			sql += "'" + s + "', ";
		}
		sql = sql.substring(0, sql.length() - 2) + ")";
		return sql;
	}

	public boolean exists(Region r) {
		return findRegionByIsoCode(r.getIsoCode()) != null;
	}

	public void updateDelicate(List<Region> list) {
		for (Region r : list) {
			updateDelicate(r);
		}
	}

	public void updateDelicate(Region r) {
		if (!exists(r)) {
			save(r);
			return;
		}
		String sql = BASE_UPDATE_SQL;
		if (r.getLandlocked() != null) {
			sql += "landlocked=" + r.getLandlocked() + ", ";
		}
		if (r.getCurrencyCode() != null) {
			sql += "currency_code=" + r.getCurrencyCode() + ", ";
		}
		if (r.getCurrency() != null) {
			sql += "currency=" + r.getCurrency() + ", ";
		}
		if (r.getName() != null) {
			sql += "name=" + r.getName() + ", ";
		}
		if (r.getDistance() != null) {
			sql += "distance=" + r.getDistance() + ", ";
		}
		if (r.getAdminRegionCode() != null) {
			sql += "admin_region_code=" + r.getAdminRegionCode() + ", ";
		}
		if (r.getCapital() != null) {
			sql += "capital=" + r.getCapital() + ", ";
		}
		if (r.getGroupCode() != null) {
			sql += "group_code=" + r.getGroupCode() + ", ";
		}
		if (r.getIncomeCode() != null) {
			sql += "income_code=" + r.getIncomeCode() + ", ";
		}
		if (r.getLegalOriginCode() != null) {
			sql += "legal_origin_code=" + r.getLegalOriginCode() + ", ";
		}
		if (r.getLendingCode() != null) {
			sql += "lending_code=" + r.getLendingCode() + ", ";
		}
		if (r.getSubContinentId() != null) {
			sql += "sub_continent_id=" + r.getSubContinentId() + ", ";
		}
		if (r.getRe() != null) {
			sql += "re=" + r.getRe() + ", ";
		}
		if (r.getEf() != null) {
			sql += "ef=" + r.getEf() + ", ";
		}
		if (r.getPr() != null) {
			sql += "pr=" + r.getPr() + ", ";
		}
		if (r.getAggregate() != null) {
			sql += "aggregate=" + r.getAggregate() + ", ";
		}
		if (r.getWb() != null) {
			sql += "wr=" + r.getWb() + ", ";
		}
		if (r.getFr() != null) {
			sql += "fo=" + r.getFr() + ", ";
		}
		sql = sql.substring(0, sql.length() - 2);
		sql += ") where id=" + r.getId() + "";
		getJdbcTemplate().execute(sql);
	}

	public List<Region> getOnlyCountries() {
		String sql = initQuery() + " where aggregate=0";
		return query(sql);
	}

	public void setSource(Region region, String src, boolean include) {
		String sql = getUpdateSql() + src.toLowerCase() + "=" + include + " where id=" + region.getId() + "";
		getJdbcTemplate().execute(sql);		
	}
}
