package com.softactive.core.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.softactive.core.object.Price;
import com.softactive.core.object.RiskFactor;

@Repository
public class PriceService extends AbstractDataService<Price> {

	public PriceService() {
		super("prc_price");
	}
	
	@Override
	public void save(Price p) {
		Price in = findByRiskFactorIdAndDate(p.getRiskFactorId(), p.getDataDate());
		if(in==null) {
			insert(p);
		} else {
			p.setId(in.getId());
			update(p);
		}
	}
	
	public Price findByRiskFactorIdAndDate(Integer rfId, Date date) {
		String sql = initQuery() + " where risk_factor_id=" + rfId +
				" and data_date='" + date + "'";
		List<Price> answer = query(sql);
		if (answer.size() == 1) {
			return answer.get(0);
		}
		return null;
	}
	
	public void deleteByRiskFactor(List<RiskFactor> riskFactors) {
		String sql = getDeleteSql() + " where risk_factor_id " + getWhereStatementForRiskFactors(riskFactors);
		getJdbcTemplate().execute(sql);
	}
	
	private String getWhereStatementForRiskFactors(List<RiskFactor> riskFactors) {
		String sql = " in ('";
		for(RiskFactor rf:riskFactors) {
			sql += rf.getId() + "','";
		}
		sql = sql.substring(0,sql.length()-2);
		sql += ")";
		return sql;
	}
}
