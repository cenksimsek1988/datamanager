package com.softactive.core.fred;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.softactive.core.manager.AbstractJsonObjectHandler;
import com.softactive.core.object.Pullable;

public abstract class AbstractFredHandler<P extends Pullable> extends AbstractJsonObjectHandler<P> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7060157568515342932L;
	public static final String RELEASES = "releases";
	public static final String ID = "id";
	public static final String NAME = "id";
	public static final String COUNT = "pageCount";
	public static final String OFFSET = "offset";
	public static final String LIMIT = "limit";
	public static final String SERIES = "seriess";

	@Override
	protected Map<String, Object> getMetaMap(JSONObject r) {
		Map<String, Object> m = new HashMap<>();
		Integer offset = r.optInt(OFFSET);
		m.put(OFFSET, offset);
		Integer limit = r.optInt(LIMIT);
		m.put(LIMIT, limit);
		Integer count = r.optInt(COUNT);
		m.put(COUNT, count);
		return m;
	}

	@Override
	protected boolean hasNext(Map<String, Object> metaMap) {
		Integer offset = (Integer) metaMap.getOrDefault(OFFSET, null);
		Integer limit = (Integer) metaMap.getOrDefault(LIMIT, null);
		Integer count = (Integer) metaMap.getOrDefault(COUNT, null);
		return (offset + limit) < count;
	}
	
	@Override
	protected JSONArray getArray(JSONObject r) throws JSONException {
		return r.optJSONArray(SERIES);
	}

	@Override
	protected JSONObject onFormatResponse(String fileName) {
		return null;
	}
}
