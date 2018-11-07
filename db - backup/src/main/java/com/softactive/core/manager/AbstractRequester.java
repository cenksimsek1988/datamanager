package com.softactive.core.manager;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.softactive.core.object.MyConstants;
import com.softactive.core.object.MyError;
import com.softactive.core.object.Pullable;
import com.softactive.core.service.IndicatorService;
import com.softactive.core.service.RegionService;

import lombok.Getter;
import lombok.Setter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public abstract class AbstractRequester<DAO extends Pullable, CUSTOM extends DAO> implements Callback, MyConstants, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4263228814206806463L;
	@Getter	@Setter
	protected AbstractHandler<CUSTOM, ?, ?, ?> handler;
	protected OkHttpClient client;
	protected int pageIndex = 0;
	protected int pageCount = 0;
	@Autowired
	protected RegionService rs;
	@Autowired
	protected IndicatorService is;
	@Getter	@Setter
	private PostTask post;
	@Getter @Setter
	private ParameterWrapper sharedParamWrapper;
	@Setter
	private Integer progress = null;
	@Getter @Setter
	private Error error;

	protected AbstractRequester() {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.connectTimeout(30, TimeUnit.SECONDS);
		builder.readTimeout(30, TimeUnit.SECONDS);
		builder.writeTimeout(30, TimeUnit.SECONDS);
		builder.retryOnConnectionFailure(true);
		client = builder.build();
	}
	
	public void clear() {
		pageIndex = 0;
		progress = null;
		handler.clear();
	}
	
	public void stop() {
	}

	@PostConstruct
	private void init() {
		pageIndex = 0;
		progress = null;
	}

	public Integer getProgress() {
		if (progress == null) {
			progress = 0;
		}
		if(pageCount==0) {
			return 0;
		}
		return 100 * pageIndex / pageCount;
	}

	public void setPostTask(PostTask post) {
		this.post = post;
	}

	public void start() {
		progress = null;
		request();
	}

	protected abstract HttpUrl onSetParameters(HttpUrl.Builder urlBuilder, Map<String, Object> sharedParams);

	@Override
	public void onResponse(Call call, Response response)  {
		boolean hasNext;
		Map<String, Object> params = null;
		if(sharedParamWrapper!=null) {
			params = sharedParamWrapper.getParams();
		}
		hasNext = handler.handle(response, params);
		if (hasNext) {
			pageIndex++;
			request();
		} else {
			pageIndex = 0;
			pageCount = 0;
			if (post != null) {
				post.onPost();
			}
		}
	}

	@Override
	public void onFailure(Call call, IOException e) {
		Map<String, Object> params = null;
		if(sharedParamWrapper!=null) {
			params = sharedParamWrapper.getParams();
		}
		MyError error = new MyError(ERROR_CONNECTION, e.getMessage());
		params.put(PARAM_ERROR, error);
		handler.onError(params);
		request();
	}
	
	public Request onRequestBuilding(Request.Builder builder) {
		return builder.build();
	}
	
	abstract protected String onSetUrl(Map<String, Object> sharedParams);

	protected void request() {
		System.out.println("requesting:");
		String url = onSetUrl(sharedParamWrapper.getParams());
		if(url == null) {
			clear();
			return;
		}
		HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
		HttpUrl finalUrl = onSetParameters(urlBuilder, sharedParamWrapper.getParams());
		Request.Builder requestBuilder = new Request.Builder().url(finalUrl).addHeader("charset", "utf-8");
		Request request = onRequestBuilding(requestBuilder);
		System.out.println(finalUrl);
		client.newCall(request).enqueue(this);
	}
}