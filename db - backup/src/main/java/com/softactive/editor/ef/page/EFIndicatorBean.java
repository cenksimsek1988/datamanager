package com.softactive.editor.ef.page;

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
public class EFIndicatorBean implements Serializable, MyConstants {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5985157771582263818L;
	@Autowired
	private IndicatorService is;
	@Getter
	@Setter
	private PRIndicatorDataTable dt;

	@PostConstruct
	protected void init() {
		dt = new PRIndicatorDataTable(is.getIndicatorsBySource(SOURCE_ECONOMIC_FREEDOM));
	}

	public void addIndicator() {
		dt.onAddNew();
	}

	public void save() {
		is.save(dt.getSource());
	}
}
