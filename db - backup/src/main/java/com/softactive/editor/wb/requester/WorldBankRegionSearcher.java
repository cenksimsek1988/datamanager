package com.softactive.editor.wb.requester;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softactive.core.object.Region;
import com.softactive.core.service.RegionService;
import com.softactive.core.wb.AbstractWorldBankRequester;
import com.softactive.editor.wb.manager.WorldBankRegionHandler;

@Component
public class WorldBankRegionSearcher extends AbstractWorldBankRequester<Region> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8775488992413017849L;
	public static final String WORLD_BANK_REGION_URL = "http://api.worldbank.org/v2/countries";
	@Autowired
	WorldBankRegionHandler handler;
	@Autowired
	RegionService rs;

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
		return WORLD_BANK_REGION_URL;
	}
}
