package com.softactive.editor.re.view;

import com.softactive.core.object.Indicator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReutersIndicator extends Indicator {
	private static final long serialVersionUID = 1854838460438445134L;
	private String preCode;
	private String postCode;
	private String rSource;
	private int year;

	public ReutersIndicator(Indicator i) {
		setId(i.getId());
	}

}
