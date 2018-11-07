package com.softactive.core.wb;

import java.util.Map;

import com.softactive.core.manager.AbstractJsonArrayRequester;
import com.softactive.core.object.Pullable;

import okhttp3.HttpUrl;

public abstract class AbstractWorldBankRequester<P extends Pullable> extends AbstractJsonArrayRequester<P, P> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6716841241535220357L;
	public static final String PAGE = "page";
	public static final String PER_PAGE = "per_page";
	public static String perPage = "9999";
	public static final String FORMAT = "format";
	public static String format = "json";
	public static final String MRV = "MRV";
	public static String updateMRV = "2";
	public static String historicalMRV = "999999";
	public static final String FREQUENCY = "frequency";
	public static final String SEARCH_TEXT = "search_text";
	

	@Override
	protected HttpUrl onSetParameters(HttpUrl.Builder builder, Map<String, Object> sharedParams) {
		builder.addQueryParameter(PAGE, String.valueOf(pageIndex + 1));
		builder.addQueryParameter(PER_PAGE, perPage);
		builder.addQueryParameter(FORMAT, format);
		return builder.build();
	}
}
