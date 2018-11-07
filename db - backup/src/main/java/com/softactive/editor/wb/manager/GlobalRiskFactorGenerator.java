package com.softactive.editor.wb.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.softactive.core.object.Indicator;
import com.softactive.core.object.Region;
import com.softactive.core.wb.AbstractWBRiskFactorGenerator;

@Component
public class GlobalRiskFactorGenerator extends AbstractWBRiskFactorGenerator {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -1584454287052142531L;

	public void startForIndicators(List<Indicator> list) {
		Region rGl = new Region();
		rGl.setIsoCode(CODE_WORLD);
		List<Region> globalRegion = new ArrayList<>();
		globalRegion.add(rGl);
		start(list, globalRegion);
	}
}
