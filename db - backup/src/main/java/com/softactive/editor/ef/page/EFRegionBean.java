package com.softactive.editor.ef.page;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softactive.core.object.MyConstants;
import com.softactive.core.object.Region;
import com.softactive.core.service.RegionService;
import com.softactive.editor.common.view.PickList;

import lombok.Getter;
import lombok.Setter;

@Component
public class EFRegionBean implements Serializable, MyConstants {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7612018794041979310L;
	@Autowired
	private RegionService rs;
	@Getter
	@Setter
	private PickList<Region> pl;

	@PostConstruct
	protected void init() {
		List<Region> source = rs.getRegionsBySource(SOURCE_ECONOMIC_FREEDOM, false);
		List<Region> target = rs.getRegionsBySource(SOURCE_ECONOMIC_FREEDOM, true);
		pl = new PickList<>(source, target);
	}

	public void addCountries() {
		List<Region> toAdd = pl.getList().getTarget();
		List<Region> toRemove = pl.getList().getSource();
		rs.setSource(toAdd, SOURCE_ECONOMIC_FREEDOM, true);
		rs.setSource(toRemove, SOURCE_ECONOMIC_FREEDOM, false);
	}
}
