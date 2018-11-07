package com.softactive.core.manager;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;

import com.softactive.core.object.Pullable;

import okhttp3.Response;

public abstract class AbstractJsonArrayHandler<P extends Pullable> extends AbstractJsonHandler<P, JSONArray> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 444721503233273186L;

	protected AbstractJsonArrayHandler() {
		super();
	}

	@Override
	protected JSONArray onFormatResponse(Response r) throws JSONException, IOException {
		return new JSONArray(r.body().string());
	}

	@Override
	protected JSONArray onFormatResponse(String fileName) {
		return null;
	}
}
