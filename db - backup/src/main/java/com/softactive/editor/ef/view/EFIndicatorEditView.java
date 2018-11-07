package com.softactive.editor.ef.view;

import java.util.List;

import com.softactive.core.object.Indicator;
import com.softactive.editor.common.view.DataTable;

public class EFIndicatorEditView extends DataTable<Indicator> {
	public EFIndicatorEditView(List<Indicator> source) {
		super(source);
	}

	public void onAddNew() {
		Indicator newIndicator = new Indicator();
		newIndicator.setSourceCode(SOURCE_ECONOMIC_FREEDOM);
		source.add(newIndicator);
	}
}
