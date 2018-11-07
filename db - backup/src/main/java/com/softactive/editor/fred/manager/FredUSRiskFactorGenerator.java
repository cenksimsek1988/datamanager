package com.softactive.editor.fred.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.softactive.core.manager.AbstractRiskFactorGenerator;
import com.softactive.core.object.Region;

@Component
public class FredUSRiskFactorGenerator extends AbstractRiskFactorGenerator<Region, FredIndicator> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8241607309722725108L;

	public void start(List<FredIndicator> indicators) {
		Region rUs = new Region();
		rUs.setIsoCode(CODE_US);
		List<Region> usRegion = new ArrayList<>();
		usRegion.add(rUs);
		start(indicators, usRegion);
	}
}