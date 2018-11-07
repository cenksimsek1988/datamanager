package com.softactive.editor.re.manager;

import java.util.List;

import org.springframework.stereotype.Component;

import com.softactive.core.manager.AbstractRiskFactorGenerator;
import com.softactive.core.object.Indicator;
import com.softactive.core.object.RiskFactor;
import com.softactive.editor.re.view.ReutersRegion;

@Component
public class ReutersRiskFactorGenerator extends AbstractRiskFactorGenerator<ReutersRegion, Indicator> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7858500459799318045L;

	@Override
	protected RiskFactor getRiskFactor(RiskFactor rf, ReutersRegion r, Indicator i) {
		String isoCode = r.getIsoCode();
		Integer year = r.getYear();
		String preCode = i.getPreCode();
		String postCode = i.getPostCode();
		String rSource = i.getSubSource();
		String apiCode = isoCode + preCode + year + postCode + "=" + rSource;
		rf.setApiCode(apiCode);
		return rf;
	}

	@Override
	protected List<RiskFactor> getRiskFactorsForIndicator(List<ReutersRegion> regions, Indicator i) {
		return super.getRiskFactorsForIndicator(regions, i);
	}

	public void startForRegions(List<ReutersRegion> regions) {
		start(is.getIndicatorsBySource(SOURCE_REUTERS), regions);
	}
}
