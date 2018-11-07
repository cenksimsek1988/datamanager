package com.softactive.editor.pr.manager;

import org.springframework.stereotype.Component;

import com.softactive.core.manager.AbstractRiskFactorGenerator;
import com.softactive.core.object.Indicator;
import com.softactive.core.object.Region;

@Component
public class PRRiskFactorGenerator extends AbstractRiskFactorGenerator<Region, Indicator> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9076856539762251033L;

	public void start() {
		start(is.getIndicatorsBySource(SOURCE_POLITICAL_RISK), rs.getRegionsBySource(SOURCE_POLITICAL_RISK, true));
	}

}
