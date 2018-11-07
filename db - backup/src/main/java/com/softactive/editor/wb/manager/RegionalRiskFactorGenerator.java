package com.softactive.editor.wb.manager;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

import com.softactive.core.object.Indicator;
import com.softactive.core.object.Region;
import com.softactive.core.wb.AbstractWBRiskFactorGenerator;

@Component
public class RegionalRiskFactorGenerator extends AbstractWBRiskFactorGenerator implements Serializable{
	private static final long serialVersionUID = -54707498213071787L;

	public void startForIndicators(List<Indicator> list) {
		List<Region> worldBankRegions = rs.getRegionsBySource(SOURCE_WORLD_BANK, true);
		start(list, worldBankRegions);
	}

	public void startForRegions(List<Region> list) {
		List<Indicator> worldBankIndicators = is.getIndicatorsBySource(SOURCE_WORLD_BANK);
		start(worldBankIndicators, list);
	}
}
