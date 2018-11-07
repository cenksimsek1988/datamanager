package com.softactive.editor.wb.requester;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softactive.core.object.Indicator;
import com.softactive.core.wb.AbstractWorldBankRequester;
import com.softactive.editor.wb.manager.WorldBankIndicatorHandler;

@Component
public class WorldBankIndicatorSearcher extends AbstractWorldBankRequester<Indicator> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -250754457223681390L;

	public static final String WORLD_BANK_INDICATOR_URL = "http://api.worldbank.org/v2/indicators";

	@Autowired
	WorldBankIndicatorHandler handler;

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
	protected String onSetUrl(Map<String, Object> sharedParams) {
		return WORLD_BANK_INDICATOR_URL;
	}
}
