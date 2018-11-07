package com.softactive.editor.fred.manager;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softactive.core.fred.AbstractFredRequester;
import com.softactive.core.object.Indicator;
import com.softactive.core.object.Region;

import okhttp3.HttpUrl;

@Component
public class FredIndicatorSearcher extends AbstractFredRequester<Indicator, FredIndicator> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5279016158869632132L;

//	public static final String FRED_INDICATOR_URL = "https://api.stlouisfed.org/fred/series/search";
	public static final String FRED_INDICATOR_URL = "https://api.stlouisfed.org/fred/tags/series";
	public static final String TAG_NATION = "nation";


	@Autowired
	private FredIndicatorHandler handler;
	
	@PostConstruct
	private void init() {
		setHandler(handler);
	}

	@Override
	public void start() {
		super.start();
		request();
	}

	@Override
	protected HttpUrl onSetParameters(HttpUrl.Builder builder, Map<String, Object> sharedParams) {
		Region r = (Region) sharedParams.get(PARAM_REGION);
		builder.addQueryParameter(TAGS, TAG_NATION + ";" + r.getName());
		System.out.println(TAGS + ": " + r.getName());
		return super.onSetParameters(builder, sharedParams);
	}

	@Override
	protected String onSetUrl(Map<String, Object> sharedParams) {
		return FRED_INDICATOR_URL;
	}
}
