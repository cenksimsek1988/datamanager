package com.softactive.updater.fred.manager;

import java.util.Calendar;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softactive.core.fred.AbstractFredRequester;
import com.softactive.core.object.Price;
import com.softactive.core.object.RiskFactor;

import okhttp3.HttpUrl;

@Component
public class FredPriceRequester extends AbstractFredRequester<Price, Price> {
	private static final long serialVersionUID = 5183120191052310413L;
	public static final String FRED_PRICE_URL = "https://api.stlouisfed.org/fred/series/observations";
	@Autowired
	private FredPriceHandler handler;
	
	@PostConstruct
	private void init() {
		setHandler(handler);
	}

	@Override
	protected HttpUrl onSetParameters(HttpUrl.Builder builder, Map<String, Object> sharedParams) {
		RiskFactor rf = (RiskFactor) sharedParams.get(PARAM_RISK_FACTOR);
		builder.addQueryParameter(SERIES_ID, rf.getApiCode());
		if(rf.getUpdateDate()!=null) {
			builder.addQueryParameter(OBSERVATION_START, getStartDate(rf));
		}
		return super.onSetParameters(builder, sharedParams);
	}
	
	private String getStartDate(RiskFactor rf) {
		Calendar start = Calendar.getInstance();
		start.setTime(rf.getUpdateDate());
		switch(rf.getFrequencyCode()) {
		case FREQUENCY_5_YEAR:
			start.add(Calendar.YEAR, -5);
			break;
		case FREQUENCY_ANNUAL:
			start.add(Calendar.YEAR, -1);
			break;
		case FREQUENCY_QUARTERLY:
			start.add(Calendar.MONTH, -3);
			break;
		case FREQUENCY_MONTHLY:
			start.add(Calendar.MONTH, -1);
			break;
		case FREQUENCY_WEEKLY:
			start.add(Calendar.WEEK_OF_YEAR, -1);
			break;
		case FREQUENCY_DAILY:
			start.add(Calendar.DAY_OF_YEAR, -1);
			break;
		}
		int year = start.get(Calendar.YEAR);
		return year + "-" + getMonth(start) + "-" + getDay(start);
	}
	
	private String getMonth(Calendar c) {
		int month = c.get(Calendar.MONTH);
		int pseudoMonth = month + 100;
		return String.valueOf(pseudoMonth).substring(1);
	}
	
	private String getDay(Calendar c) {
		int day = c.get(Calendar.DAY_OF_MONTH);
		int pseudoDay = day + 100;
		return String.valueOf(pseudoDay).substring(1);
	}

	@Override
	protected String onSetUrl(Map<String, Object> sharedParams) {
		return FRED_PRICE_URL;
	}
}
