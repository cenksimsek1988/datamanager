package com.softactive.editor.common.page;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softactive.core.manager.PostTask;
import com.softactive.core.object.MyConstants;
import com.softactive.core.object.Region;
import com.softactive.core.service.RegionService;
import com.softactive.editor.common.view.DataTable;
import com.softactive.editor.wb.manager.WorldBankRegionHandler;
import com.softactive.editor.wb.requester.WorldBankRegionSearcher;

import lombok.Getter;
import lombok.Setter;

@Component
public class RegionBean implements Serializable, MyConstants {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7089023679490231774L;

	// searcher
	@Autowired
	private WorldBankRegionSearcher rRequester;

	// Generator
	@Autowired
	private RegionService rs;
	@Getter
	@Setter
	private DataTable<Region> dt;

	private PostTask post = new PostTask() {
		@Override
		public void onPost() {
			dt = new DataTable<Region>(((WorldBankRegionHandler)rRequester.getHandler()).getRegions());
		};
	};

	@PostConstruct
	protected void init() {
		rRequester.setPostTask(post);
//		rRequester.start();
	}

	public void save() {
		rs.updateDelicate(dt.getSelected());
	}
}
