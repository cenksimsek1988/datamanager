package com.softactive.core.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import com.softactive.core.object.MyConstants;
import com.softactive.core.object.Save;

@Repository
public class SaveService extends AbstractDataService<Save> {

	public SaveService() {
		super("save");
	}

	public Save find(String id) throws EmptyResultDataAccessException {
		try {
			return queryForObject(initQuery() + " where id=? ", new Object[] { id });
		} catch (EmptyResultDataAccessException e) {
			Save s = new Save();
			s.setId(MyConstants.SAVE_WORLD_BANK_RISK_FACTOR);
			return s;
		}
	}

	@Override
	public void save(Save t) {
		
	}
}
