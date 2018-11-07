package com.softactive.editor.pr.page;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softactive.core.object.MyConstants;
import com.softactive.editor.ef.manager.EFRiskFactorGenerator;

@Component
public class PRRiskFactorBean implements Serializable, MyConstants {
	private static final long serialVersionUID = -184329787567297839L;

	@Autowired
	private EFRiskFactorGenerator rGenerator;

	public void generate() {
		rGenerator.start();
	}
}
