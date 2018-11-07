package com.softactive.editor.re.page;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softactive.core.object.MyConstants;
import com.softactive.core.service.IndicatorService;
import com.softactive.editor.re.view.ReutersIndicatorDataTable;

import lombok.Getter;
import lombok.Setter;

@Component
public class ReutersIndicatorBean implements Serializable, MyConstants {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2920641229010638737L;
	@Autowired
	private IndicatorService is;
	@Getter
	@Setter
	private ReutersIndicatorDataTable dt;

	@PostConstruct
	protected void init() {
		dt = new ReutersIndicatorDataTable(is.getIndicatorsBySource(SOURCE_REUTERS));
	}

	public void addIndicator() {
		dt.onAddNew();
	}

	public void save() {
		is.save(dt.getSource());
	}
}
