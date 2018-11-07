package com.softactive.core.wb;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.softactive.core.manager.AbstractJsonArrayHandler;
import com.softactive.core.object.Pullable;

public abstract class AbstractWorldBankHandler<P extends Pullable> extends AbstractJsonArrayHandler<P> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3398406810984198050L;
	public static final Date DUMMY_DATE = Date.valueOf("0001-01-01");
	public static final String LAST_UPDATED = "lastupdated";
	public static final String PAGES = "pages";
	public static final String PAGE = "page";
	public static final String PER_PAGE = "per_page";
	public static final String TOTAL = "total";

	@Override
	protected Map<String, Object> getMetaMap(JSONArray r) throws JSONException {
		JSONObject o = r.getJSONObject(0);
		Map<String, Object> map = new HashMap<>();
		map.put(PAGE, o.getInt(PAGE));
		map.put(PAGES, o.getInt(PAGES));
		map.put(LAST_UPDATED, o.opt(LAST_UPDATED));
		map.put(PER_PAGE, o.getInt(PER_PAGE));
		map.put(TOTAL, o.getInt(TOTAL));
		return map;
	}

	@Override
	protected boolean hasNext(Map<String, Object> metaMap) {
		return (int) metaMap.get(PAGE) < (int) metaMap.get(PAGES);
	}

	@Override
	protected JSONArray getArray(JSONArray r) throws JSONException {
		return r.getJSONArray(1);
	}
}
