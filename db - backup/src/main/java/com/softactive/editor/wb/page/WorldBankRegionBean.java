package com.softactive.editor.wb.page;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softactive.core.object.MyConstants;
import com.softactive.core.object.Region;
import com.softactive.core.service.RegionService;
import com.softactive.editor.common.view.DataTable;
import com.softactive.editor.common.view.PickList;
import com.softactive.editor.wb.manager.RegionalRiskFactorGenerator;

import lombok.Getter;
import lombok.Setter;

@Component
public class WorldBankRegionBean implements Serializable, MyConstants {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2004696082868214632L;

	// searcher
	@Getter
	@Setter
	private PickList<Region> pl;

	// Generator
	@Autowired
	private RegionService rs;

	@Getter @Setter
	@Autowired
	private RegionalRiskFactorGenerator rGenerator;

	@Getter
	@Setter
	private DataTable<Region> dtGenerator = new DataTable<>();

	@PostConstruct
	protected void init() {
		List<Region> source = rs.getRegionsBySource(SOURCE_WORLD_BANK, false);
		List<Region> target = rs.getRegionsBySource(SOURCE_WORLD_BANK, true);
		pl = new PickList<>(source, target);
	}

	private void addCountries() {
		List<Region> toAdd = pl.getList().getTarget();
		List<Region> toRemove = pl.getList().getSource();
		rs.setSource(toAdd, SOURCE_WORLD_BANK, true);
		rs.setSource(toRemove, SOURCE_WORLD_BANK, false);
	}

	// generator
	public void refreshGeneratorList() {
		dtGenerator = new DataTable<Region>(pl.getList().getTarget());
		addCountries();
	}

	public void generate() {
		rGenerator.startForRegions(dtGenerator.getSource());
	}
}
