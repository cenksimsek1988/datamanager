package com.softactive.core.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.softactive.core.object.Indicator;
import com.softactive.core.object.MyConstants;
import com.softactive.core.object.Region;
import com.softactive.core.object.RiskFactor;
import com.softactive.core.service.IndicatorService;
import com.softactive.core.service.RegionService;
import com.softactive.core.service.RiskFactorService;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractRiskFactorGenerator<R extends Region, I extends Indicator> implements MyConstants, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7225464527078250382L;
	@Autowired
	protected RiskFactorService rfs;
	@Autowired
	protected IndicatorService is;
	@Autowired
	protected RegionService rs;
	@Getter
	private int indicatorIndex = 0;
	@Getter
	private int indicatorCount = 0;
	@Setter
	private Integer progress = null;

	public void start(List<I> indicators, List<R> regions) {
		for (Region r : regions) {
			rs.save(r);
		}
		indicatorCount = indicators.size();
		for (I i : indicators) {
			rfs.save(getRiskFactorsForIndicator(regions, i));
			is.save(i);
			indicatorIndex++;
		}
	}

	public void start(List<I> indicators, R r) {
		List<R> regions = new ArrayList<>();
		regions.add(r);
		start(indicators, regions);
	}

	public Integer getProgress() {
		if (progress == null) {
			progress = 0;
		}
		if(indicatorCount == 0) {
			return 0;
		}
		progress = 100 * indicatorIndex / indicatorCount;
		return progress;
	}

	protected RiskFactor getRiskFactor(RiskFactor rf, R r, I i) {
		return rf;
	}

	protected List<RiskFactor> getRiskFactorsForIndicator(List<R> regions, I i) {
		List<RiskFactor> list = new ArrayList<>();
		for (R r : regions) {
			RiskFactor rf = new RiskFactor();
			String apiCode = i.getApiCode();
			rf.setApiCode(apiCode);
			String sourceCode = i.getSourceCode();
			rf.setDataSourceCode(sourceCode);
			if(i.getId()==null) {
				System.out.println("MyError: indicator id is null");
			}
			rf.setIndicatorCode(i.getId());
			if(i.getFrequencyId() == null) {
				i.setFrequencyId(FREQUENCY_ANNUAL);
			}
			rf.setFrequencyCode(i.getFrequencyId());
			rf.setRegionId(r.getId());
			list.add(getRiskFactor(rf, r, i));
		}
		return list;
	}
}
