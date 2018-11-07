package com.softactive.updater.wb.manager;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.softactive.core.manager.MyException;
import com.softactive.core.object.Price;
import com.softactive.core.object.RiskFactor;
import com.softactive.core.wb.AbstractWorldBankHandler;

@Component
public class WorldBankPriceHandler extends AbstractWorldBankHandler<Price>{
	private static final long serialVersionUID = 7934081590980387905L;
	public static final String VALUE = "value";
	public static final String DATE = "date";
	private Date lastDate = TOO_EARLY;

	@Override
	protected Price getObject(JSONObject o, Map<String, Object> requestParams) throws MyException {
		Price p = new Price();
		RiskFactor rf = (RiskFactor)requestParams.get(PARAM_RISK_FACTOR);
		p.setRiskFactorId(rf.getId());
		String dateString = resolveValidString(o, DATE, null);
		String frq = getFrequencyId(dateString);
		requestParams.put(PARAM_FREQUENCY_ID, frq);
		p.setDataDate(getDate(dateString, frq));
		Date date = resolveValidDate(o, DATE);
		if(date.after(lastDate)) {
			lastDate = date;
		}
		p.setDataDate(date);
		p.setPrice(resolveValidDouble(o, VALUE));
		ps.save(p);
		return p;
	}

	protected Date getDate(String dateString, String frequencyId) throws MyException {
		String year = dateString.substring(0, 4);
		String month = getMonth(frequencyId, dateString);
		String day = getDay(Integer.valueOf(month), Integer.valueOf(year));
		String adjustedDateString = year + "-" + month + "-" + day;
		return resolveValidDate(adjustedDateString);
	}

	private String getMonth(String frequencyId, String dateString) {
		switch(frequencyId) {
		case FREQUENCY_MONTHLY: return dateString.substring(5, 7);
		case FREQUENCY_QUARTERLY:
			String period = dateString.substring(5, 6);
			return String.valueOf(Integer.valueOf(period) * 3);
		case FREQUENCY_ANNUAL: return "12";
		default: return null;
		}
	}

	private String getDay(int month, int year) {
		String day = "31";
		if (month == 2) {
			day = "28";
			if (year % 4 == 0 && year % 100 != 0) {
				day = "29";
			}
			if (year % 400 == 0 && year % 4000 != 0) {
				day = "29";
			}
		} else if (month == 4 | month == 6 | month == 9 | month == 11) {
			day = "30";
		}
		return day;
	}

	private String getFrequencyId(String dateString) throws MyException {
		switch(dateString.length()) {
		case 4: return FREQUENCY_ANNUAL;
		case 6: return FREQUENCY_QUARTERLY;
		case 7: return FREQUENCY_MONTHLY;
		default: throw new MyException("Cannot resolve frequency from String: " + dateString + ". ");
		}
	}

	@Override
	public void onListSuccesfullyParsed(List<Price> list, Map<String, Object> sharedParams) {
		RiskFactor rf = (RiskFactor)sharedParams.get(PARAM_RISK_FACTOR);
		Date updateDate = (Date)sharedParams.get(PARAM_UPDATE_DATE);
		rf.setUpdateDate(updateDate);
		String frq = (String)sharedParams.get(PARAM_FREQUENCY_ID);
		rf.setFrequencyCode(frq);
		rfs.save(rf);
		System.out.println("new price data is added to DB.");
	}

	@Override
	public void onError(Map<String, Object> sharedParams) {
		saveUpdateError(sharedParams);
	}
}
