package com.softactive.editor.pr.page;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softactive.core.object.MyConstants;
import com.softactive.core.service.IndicatorService;
import com.softactive.editor.pr.view.PRIndicatorDataTable;

import lombok.Getter;
import lombok.Setter;

@Component
public class PRIndicatorBean implements Serializable, MyConstants {
	private static final long serialVersionUID = 6276875329933202159L;
	@Autowired
	private IndicatorService is;
	@Getter
	@Setter
	private PRIndicatorDataTable dt;

	@PostConstruct
	protected void init() {
		dt = new PRIndicatorDataTable(is.getIndicatorsBySource(SOURCE_POLITICAL_RISK));
	}

	public void addIndicator() {
		dt.onAddNew();
	}

	public void save() {
		is.save(dt.getSource());
	}
}
