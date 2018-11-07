package com.softactive.core.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.softactive.core.object.UpdateError;

@Repository
public class UpdateErrorService extends AbstractDataService<UpdateError> {

	protected UpdateErrorService() {
		super("update_error");
	}

	public UpdateError findByRiskFactorId(Integer rfId) {
		String sql = initQuery() + " where risk_factor_id=" + rfId + " LIMIT 1";
		List<UpdateError> list = query(sql);
		if(list==null || list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}

	@Override
	public
	void save(UpdateError t) {
		UpdateError in = findByRiskFactorId(t.getRiskFactorId());
		if(in!=null) {
			delete(in.getId());
			t.setId(in.getId());
		}
		insert(t);
	}
}
