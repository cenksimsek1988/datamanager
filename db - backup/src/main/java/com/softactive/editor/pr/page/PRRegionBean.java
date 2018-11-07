package com.softactive.editor.pr.page;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softactive.core.object.MyConstants;
import com.softactive.core.object.Region;
import com.softactive.core.service.RegionService;
import com.softactive.editor.common.view.PickList;
import com.softactive.editor.common.view.ProgressBarView;

import lombok.Getter;
import lombok.Setter;

@Component
public class PRRegionBean implements Serializable, MyConstants {
	private static final long serialVersionUID = 6276875329933202159L;
	@Autowired
	private RegionService rs;
	@Getter
	@Setter
	private PickList<Region> pl;
	@Getter
	@Setter
	private ProgressBarView pbSearcher;

	@PostConstruct
	protected void init() {
		List<Region> source = rs.getRegionsBySource(SOURCE_POLITICAL_RISK, false);
		List<Region> target = rs.getRegionsBySource(SOURCE_POLITICAL_RISK, true);
		pl = new PickList<>(source, target);
	}

	public void addCountries() {
		List<Region> toAdd = pl.getList().getTarget();
		List<Region> toRemove = pl.getList().getSource();
		rs.setSource(toAdd, SOURCE_POLITICAL_RISK, true);
		rs.setSource(toRemove, SOURCE_POLITICAL_RISK, false);
	}
}
