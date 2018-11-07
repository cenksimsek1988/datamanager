package com.softactive.editor.ef.page;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softactive.core.object.MyConstants;
import com.softactive.editor.ef.manager.EFRiskFactorGenerator;

@Component
public class EFRiskFactorBean implements Serializable, MyConstants {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6295188190265032881L;
	@Autowired
	private EFRiskFactorGenerator rGenerator;

	public void generate() {
		rGenerator.start();
	}
}
