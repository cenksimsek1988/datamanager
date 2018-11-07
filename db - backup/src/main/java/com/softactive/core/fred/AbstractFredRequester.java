package com.softactive.core.fred;

import java.util.Map;

import com.softactive.core.manager.AbstractJsonObjectRequester;
import com.softactive.core.object.Pullable;

import okhttp3.HttpUrl;

public abstract class AbstractFredRequester<P extends Pullable, C extends P> extends AbstractJsonObjectRequester<P, C> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3042736686519856842L;
	public static int limit = 1000;
	public static final String LIMIT = "limit";
	public static final String OFFSET = "offset";
	public static final String FILE_TYPE = "file_type";
	public static final String SERIES_ID = "series_id";
	public static final String OBSERVATION_START = "observation_start";
	public static String file_type = "json";
	public static final String API_KEY = "api_key";
	public static String api_key = "db59c4f42d19668474fc0c871ff72093";
	public static final String SEARCH_TEXT = "search_text";
	public static final String TAGS = "tag_names";

	@Override
	protected HttpUrl onSetParameters(HttpUrl.Builder builder, Map<String, Object> sharedParams) {
		builder.addQueryParameter(LIMIT, String.valueOf(limit));
		builder.addQueryParameter(OFFSET, String.valueOf(pageIndex * limit));
		builder.addQueryParameter(API_KEY, api_key);
		builder.addQueryParameter(FILE_TYPE, file_type);
		return builder.build();
	}

	
}
