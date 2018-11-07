package com.softactive.editor.fred.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softactive.core.manager.ParameterWrapper;
import com.softactive.core.manager.PostTask;
import com.softactive.core.object.CountryStandard;
import com.softactive.core.object.MyConstants;
import com.softactive.core.object.Region;
import com.softactive.core.service.CountryStandardService;
import com.softactive.core.service.RegionService;
import com.softactive.editor.fred.manager.FredIndicatorHandler;
import com.softactive.editor.fred.manager.FredIndicatorSearcher;
import com.softactive.editor.fred.manager.FredRegion;

import lombok.Getter;
import lombok.Setter;

@Component
public class FredIndicatorBean implements MyConstants, Serializable {
	private static final long serialVersionUID = 6276875329933202159L;

	// for searcher
	@Getter @Setter
	@Autowired
	private FredIndicatorSearcher iSearcher;
	@Autowired
	private RegionService rs;
	@Autowired
	private CountryStandardService css;
	private PostTask post = new PostTask() {
		@Override
		public void onPost() {
			index++;
			requestForNextRegion();
		}
	};
	private int index = 0;
	private int count = 0;
	private int round = 1;
	@Setter
	private Integer progress = null;
	
	public Integer getProgress() {
		if(progress==null) {
			progress = 0;
		}
		if(count==0) {
			return 0;
		} else {
			return 100 * index / count;
		}
	}


	@PostConstruct
	protected void init() {
		List<Region> regions = rs.getOnlyCountries();
		count = regions.size();
		iSearcher.setSharedParamWrapper(getRegionParamWrapper(regions));
		iSearcher.setPost(post);
	}

	private ParameterWrapper getRegionParamWrapper(List<Region> regions) {
		return new ParameterWrapper() {
			@Override
			public final Map<String, Object> getParams() {
				return getRegionParameters(regions);			
			}
		};
	}

	private final Map<String, Object> getRegionParameters(List<Region> regions) {
		Map<String, Object> params = new HashMap<String, Object>();
		final Region current = regions.get(index);
		params.put(PARAM_REGION, current);
		return params;
	}

	private void requestForNextRegion() {
		if(index < count) {
			iSearcher.start();
		} else {
			index = 0;
			count = 0;
			onRoundComplete();
		}
	}

	private void onRoundComplete() {
		round++;
		List<Region> remaining = getAltRegions(((FredIndicatorHandler)iSearcher.getHandler()).getErrorRegionList());
		if(remaining == null) {
			System.out.println("totally finished");
			System.out.println(((FredIndicatorHandler)iSearcher.getHandler()).getErrorRegionList());
		}
		count = remaining.size();
		if(round == 2) {
			if(count > 0) {
				iSearcher.setSharedParamWrapper(getRegionParamWrapper(remaining));
				iSearcher.start();
			}
		} else if(round == 2 && count == index) {
			
		}
	}

	private List<Region> getAltRegions(List<FredRegion> errorRegionList){
		List<String> names = new ArrayList<>();
		for(Region r:errorRegionList) {
			names.add(r.getName());
		}
		List<CountryStandard> standards = css.getCountryStandardsByStandardNames(names);
		List<Region> regions = new ArrayList<>();
		if(standards==null) {
			return null;
		}
		for(CountryStandard alt:standards) {
			Region r = rs.findRegionByIsoCode(alt.getIsoCode());
			r.setName(alt.getName());
			regions.add(r);
		}
		return regions;
	}

	public void requestIndicators() {
		iSearcher.start();
	}
}
