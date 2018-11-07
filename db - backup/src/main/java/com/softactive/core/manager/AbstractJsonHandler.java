package com.softactive.core.manager;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.softactive.core.object.MyError;
import com.softactive.core.object.Pullable;
import com.softactive.core.object.RiskFactor;

import okhttp3.Response;

public abstract class AbstractJsonHandler<P extends Pullable, RESPONSE>
extends AbstractHandler<P, RESPONSE, JSONArray, JSONObject> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3758384580767903932L;

	public boolean handle(Response response, @Nullable Map<String, Object> sharedParams){
		RESPONSE r = null;
		List<P> additionalList = null;
		try {
			r = onFormatResponse(response);
		} catch (JSONException e) {
			RiskFactor rf = (RiskFactor)sharedParams.get(PARAM_RISK_FACTOR);
			sharedParams.put(PARAM_ERROR, new MyError(ERROR_RESPONSE_FORMAT, "indicator id: " +
					rf.getIndicatorCode() + " region id: " + rf.getRegionId() + ". "));
			printError(sharedParams);
			//			onError(sharedParams);
			return false;
		} catch (IOException e1) {
			MyError error = new MyError(ERROR_INVALID_RESPONSE_BODY, response.toString());
			sharedParams.put(PARAM_ERROR, error);
			onError(sharedParams);
			return false;
		}
		try {
			metaMap = getMetaMap(r);
		} catch (JSONException e) {
			MyError error = new MyError(ERROR_INVALID_METADATA_FORMAT, r.toString());
			sharedParams.put(PARAM_ERROR, error);
			onError(sharedParams);
			return false;
		}
		JSONArray array = null;
		try {
			array = getArray(r);
		} catch (JSONException e) {
			MyError error = new MyError(ERROR_INVALID_DATA_ARRAY_FORMAT, r.toString());
			sharedParams.put(PARAM_ERROR, error);
			onError(sharedParams);
			return false;
		}
		additionalList = getList(array, sharedParams);
		if(additionalList!=null && additionalList.size()>0) {
			onListSuccesfullyParsed(additionalList, sharedParams);
		} else {
			onError(sharedParams);
			return false;
		}
		return hasNext(metaMap);
	}

	@Override
	protected List<P> getList(JSONArray array, Map<String, Object> sharedParams) {
		List<P> list = new ArrayList<>();
		if(array==null) {
			MyError er = new MyError(ERROR_NULL_JSON_ARRAY, "JSON Array is not found. ");
			sharedParams.put(PARAM_ERROR, er);
			return list;
		}
		if (array.length()==0) {
			MyError er = new MyError(ERROR_EMPTY_JSON_ARRAY, array.toString());
			sharedParams.put(PARAM_ERROR, er);
			return list;
		}
		for (int i = 0; i < array.length(); i++) {
			P p = null;
			JSONObject jo = null;
			try {
				jo = array.getJSONObject(i);
			} catch (JSONException e1) {
				MyError er = new MyError(ERROR_INVALID_JSON_OBJECT_FORMAT, array.toString());
				sharedParams.put(PARAM_ERROR, er);
				return list;
			}
			try {
				p = getObject(jo, sharedParams);
			} catch (MyException e) {
				MyError er = new MyError(ERROR_INVALID_VALUE, e.getMsg() + "/n" + jo.toString());
				sharedParams.put(PARAM_ERROR, er);
			}
			if (p != null) {
				list.add(p);
			}
		}
		return list;
	}

	protected Object resolveValidValue(JSONObject jo, String key, String type, @Nullable Integer expectedLenght) throws MyException {
		switch (type) {
		case TYPE_STRING:
			return resolveValidString(jo, key, expectedLenght);
		case TYPE_INTEGER_ID:
			return resolveValidIntegerId(jo, key, expectedLenght);
		default:
			System.out.println("Cannot get valid value: Type is uknown: " + type);
			return null;
		}
	}

	protected int resolveValidInteger(JSONObject jo, String key) throws MyException {
		Integer i = null;
		try {
			i = jo.getInt(key);
		} catch (JSONException e) {
			throw new MyException("No Integer value (key: " + key + ") found in JSON Object. ");
		}
		return i;
	}

	protected JSONObject resolveValidJSONObject(JSONObject jo, String key) throws MyException {
		try {
			return jo.getJSONObject(key);
		} catch (JSONException e) {
			throw new MyException("No JSON Object value found in JSON Object with key: " + key + ". ");
		}
	}

	protected String optValidString(JSONObject jo, String key, @Nullable Integer expectedLenght) throws MyException {
		String s;
		try {
			s = jo.getString(key);
		} catch (JSONException e) {
			throw new MyException("No String value found in JSON Object with key: " + key + ". ");
		}
		try {
			return resolveValidString(s, expectedLenght);
		} catch (MyException e){
			return null;
		}
	}

	protected String resolveValidString(JSONObject jo, String key, @Nullable Integer expectedLenght) throws MyException {
		String s;
		try {
			s = jo.getString(key);
		} catch (JSONException e) {
			throw new MyException("No String value found in JSON Object with key: " + key + ". ");
		}
		try {
			s = resolveValidString(s, expectedLenght);
		} catch (MyException e){
			e.setMsg(e.getMessage() + " (key: " + key + ") ");
			throw e;
		}
		return s;
	}

	protected Integer optValidIntegerId(JSONObject jo, String key, @Nullable Integer expectedLenght) throws MyException {
		Integer i = resolveValidInteger(jo, key);
		if (i < 0) {
			return null;
		}
		if (expectedLenght != null) {
			if (i >= (10 ^ expectedLenght)) {
				return null;
			}
		}
		return i;
	}

	protected Integer resolveValidIntegerId(JSONObject jo, String key, @Nullable Integer expectedLenght) throws MyException {
		Integer i = resolveValidInteger(jo, key);
		if (i < 0) {
			throw new MyException("Integer id (key: " + key + ") value is negative. value: " + i + ". ");
		}
		if (expectedLenght != null) {
			if (i >= (10 ^ expectedLenght)) {
				throw new MyException("Integer id (key: " + key + ") value is longer then expected. expected lenght: " + expectedLenght +
						". value: " + i + ". ");
			}
		}
		return i;
	}
	protected Date resolveValidDate(JSONObject jo, String key) throws MyException {
		String s = null;
		Date date = null;
		try {
			s = jo.getString(key);
		} catch (JSONException e) {
			throw new MyException("No Date value (key: " + key + ") found in JSON Object. ");
		}
		try {
			date = resolveValidDate(s);
		} catch (MyException e) {
			e.setMsg(e.getMessage() + "(key: " + key + ")");
			throw e;
		}
		return date;
	}

	protected Date resolveValidDate(String dateString) throws MyException {
		Date date = null;
		try {
			date = Date.valueOf(dateString);
		} catch (IllegalArgumentException e) {
			throw new MyException("Date value is not well formed. value: " + dateString + ". ");
		}
		return date;
	}



	protected double resolveValidDouble(JSONObject jo, String key) throws MyException {
		Double value = null;
		try {
			value = jo.getDouble(key);
		} catch (JSONException e) {
			throw new MyException("Double value (key: " + key + ") is not found.");
		}
		return value;
	}
}
