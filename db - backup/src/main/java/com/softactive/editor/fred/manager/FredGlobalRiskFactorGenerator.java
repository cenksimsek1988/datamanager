package com.softactive.editor.fred.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.softactive.core.manager.AbstractRiskFactorGenerator;
import com.softactive.core.object.Region;

@Component
public class FredGlobalRiskFactorGenerator extends AbstractRiskFactorGenerator<Region, FredIndicator> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -163220447218710917L;

	public void start(List<FredIndicator> indicators) {
		Region rGl = new Region();
		rGl.setIsoCode(CODE_WORLD);
		List<Region> globalRegion = new ArrayList<>();
		globalRegion.add(rGl);
		start(indicators, globalRegion);
	}
}
