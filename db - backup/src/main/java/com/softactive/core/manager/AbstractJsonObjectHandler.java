package com.softactive.core.manager;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.softactive.core.object.Pullable;

import okhttp3.Response;

public abstract class AbstractJsonObjectHandler<P extends Pullable> extends AbstractJsonHandler<P, JSONObject> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1370856627336689478L;

	protected AbstractJsonObjectHandler() {
		super();
	}

	@Override
	protected JSONObject onFormatResponse(Response r) throws JSONException, IOException {
		return new JSONObject(r.body().string());
	}

}
