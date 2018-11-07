package com.softactive.editor.re.page;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softactive.core.object.MyConstants;
import com.softactive.core.object.Region;
import com.softactive.core.service.RegionService;
import com.softactive.editor.common.view.PickList;
import com.softactive.editor.re.view.ReutersRegion;

import lombok.Getter;
import lombok.Setter;

@Component
public class ReutersRegionBean implements Serializable, MyConstants {
	/**
	 * 
	 */
	private static final long serialVersionUID = 529701482771625933L;
	@Autowired
	private RegionService rs;
	@Getter
	@Setter
	private PickList<ReutersRegion> pl;

	@PostConstruct
	protected void init() {
		List<ReutersRegion> source = ReutersRegion.getReutersRegionList(rs.getRegionsBySource(SOURCE_REUTERS, false));
		List<ReutersRegion> target = ReutersRegion.getReutersRegionList(rs.getRegionsBySource(SOURCE_REUTERS, true));
		pl = new PickList<>(source, target);
	}

	public void addCountries() {
		List<Region> toAdd = ReutersRegion.getRegionList(pl.getList().getTarget());
		List<Region> toRemove = ReutersRegion.getRegionList(pl.getList().getSource());
		rs.setSource(toAdd, SOURCE_REUTERS, true);
		rs.setSource(toRemove, SOURCE_REUTERS, false);
	}
}
