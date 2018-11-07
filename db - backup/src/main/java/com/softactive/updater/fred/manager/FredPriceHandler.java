package com.softactive.updater.fred.manager;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.softactive.core.fred.AbstractFredHandler;
import com.softactive.core.manager.MyException;
import com.softactive.core.object.Price;
import com.softactive.core.object.RiskFactor;

@Component
public class FredPriceHandler extends AbstractFredHandler<Price> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5613352456103868158L;
	public static final String DATE = "date";
	public static final String VALUE = "value";
	private Date lastDate = TOO_EARLY;

	@Override
	protected Price getObject(JSONObject o, Map<String, Object> requestParams) throws MyException{
		Price p = new Price();
		RiskFactor rf = (RiskFactor)requestParams.get(PARAM_RISK_FACTOR);
		p.setRiskFactorId(rf.getId());
		Date date = resolveValidDate(o, DATE);
		if(date.after(lastDate)) {
			lastDate = date;
		}
		p.setDataDate(date);
		p.setPrice(resolveValidDouble(o, VALUE));
		ps.save(p);
		return p;
	}

	@Override
	public void onListSuccesfullyParsed(List<Price> list, Map<String, Object> requestParams) {
		RiskFactor rf = (RiskFactor)requestParams.get(PARAM_RISK_FACTOR);
		rf.setUpdateDate(lastDate);
		rfs.save(rf);
		lastDate = TOO_EARLY;
	}

	@Override
	public void onError(Map<String, Object> sharedParams) {
		saveUpdateError(sharedParams);
	}
}
