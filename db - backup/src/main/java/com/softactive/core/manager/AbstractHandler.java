package com.softactive.core.manager;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;

import com.softactive.core.object.Indicator;
import com.softactive.core.object.MyConstants;
import com.softactive.core.object.MyError;
import com.softactive.core.object.Pullable;
import com.softactive.core.object.Region;
import com.softactive.core.object.RiskFactor;
import com.softactive.core.object.UpdateError;
import com.softactive.core.service.IndicatorService;
import com.softactive.core.service.PriceService;
import com.softactive.core.service.RegionService;
import com.softactive.core.service.RiskFactorService;
import com.softactive.core.service.SaveService;
import com.softactive.core.service.UpdateErrorService;

import lombok.Getter;
import lombok.Setter;
import okhttp3.Response;

public abstract class AbstractHandler<P extends Pullable, RESPONSE, ARRAY, RAWITEM> implements MyConstants, Serializable {
	private static final long serialVersionUID = -7865217654203065084L;
	public static final String TYPE_STRING = "String";
	public static final String TYPE_INTEGER_ID = "IntegerId";
	public static final String TYPE_DOUBLE = "double";
	public static final String TYPE_DATE = "Date";

	public static final String SAVE_WORLD_BANK_INDICATOR_STATUS = "indicator";
	public static final String SAVE_RISK_FACTOR_STATUS = "risk_factor";
	public static final String SAVE_HISTORICAL_SATUS = "historical";
	public static final String SAVE_UPDATE_STATUS = "update";

	private ParameterWrapper sharedParameterWrapper;
	
	public ParameterWrapper getSharedParameterWrapper() {
		return sharedParameterWrapper;
	}
	
	public void setSharedParameterWrapper(ParameterWrapper sharedParamWrapper) {
		sharedParameterWrapper = sharedParamWrapper;
	}

	@Getter @Setter
	private PostTask post;

	@Autowired
	protected RiskFactorService rfs;
	@Autowired
	protected SaveService ss;
	@Autowired
	protected RegionService rs;
	@Autowired
	protected IndicatorService is;
	@Autowired
	protected PriceService ps;
	@Autowired
	private UpdateErrorService ues;
	protected Map<String, Object> metaMap;
	@Setter
	@Getter
	private Integer progress = null;

	protected abstract boolean handle(Response response, @Nullable Map<String, Object> sharedParams);
	
	protected void saveUpdateError(Map<String, Object> sharedParams) {
		RiskFactor rf = (RiskFactor)sharedParams.get(PARAM_RISK_FACTOR);
		MyError e = (MyError)sharedParams.get(PARAM_ERROR);
		saveUpdateError(rf, e.getResult(), e.getType());
		rf.setUpdateDate(resolveValidDate(Calendar.getInstance()));
		rfs.save(rf);
	}

	protected Date resolveValidDate(Calendar c) {
		String dateString = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DAY_OF_MONTH);
		return Date.valueOf(dateString);
	}

	protected void saveUpdateError(RiskFactor rf, String msg, int errorType) {
		UpdateError ue = new UpdateError();
		Region r = rs.findRegionById(rf.getRegionId());
		Indicator i = is.findIndicatorById(rf.getIndicatorCode());
		ue.setRiskFactorId(rf.getId());
		ue.setIndicatorName(i.getName());
		ue.setRegionName(r.getName());
		ue.setSourceId(rf.getDataSourceCode());
		ue.setFrequencyId(rf.getFrequencyCode());
		ue.setMessage(msg);
		ue.setType(errorType);
		ues.save(ue);
	}

	public void onListSuccesfullyParsed(List<P> list, Map<String, Object> requestParams) {}

	public void onError(Map<String, Object> sharedParams) {
		printError(sharedParams);
	}

	protected abstract RESPONSE onFormatResponse(final Response r) throws JSONException, IOException;

	protected abstract Map<String, Object> getMetaMap(final RESPONSE r) throws JSONException;

	protected abstract boolean hasNext(Map<String, Object> metaMap);

	protected abstract ARRAY getArray(RESPONSE r)  throws JSONException;

	protected abstract List<P> getList(ARRAY array, Map<String, Object> sharedParams) throws MyException;

	protected void printError(Map<String, Object> sharedParams) {
		MyError er = (MyError) sharedParams.get(PARAM_ERROR);
		System.out.println("type: " + er.getType() + ". message: " + er.getResult());
	}

	protected Integer resolveValidIntegerId(String s) throws MyException {
		Integer answer = null;
		try {
			answer = Integer.valueOf(s);
		} catch (NumberFormatException e){
			throw new MyException("Integer value cannot be converted from string: " + s);
		}
		return answer;
	}

	protected Integer optValidIntegerId(String s) {
		try {
			return resolveValidIntegerId(s);
		} catch (MyException e) {
			return null;
		}
	}

	protected String resolveValidString(String s, Integer expectedLenght) throws MyException{
		if (s == null) {
			throw new MyException("string value is null: ");
		}
		if (s.length()==0) {
			throw new MyException("string value is empty: ");
		}
		if (s.equalsIgnoreCase("NA") | s.equalsIgnoreCase("null")) {
			throw new MyException("string value is hardcoded null: ");
		}
		if (expectedLenght != null) {
			if (s.length() != expectedLenght) {
				throw new MyException("string value lenght is not as expected. expectedlenght: " + expectedLenght + ". ");
			}
			String x = "";
			for (int i = 0; i < expectedLenght; i++) {
				x += "X";
			}
			if (s.equalsIgnoreCase(x)) {
				throw new MyException("string value seems as null hardcoded. value: " + s);
			}
		}
		return s;
	}

	protected abstract RESPONSE onFormatResponse(String fileName);
	
	protected abstract P getObject(RAWITEM o, Map<String, Object> requestParams) throws MyException;

	public void clear() {
		progress = null;
	}

}
