package com.softactive.editor.wb.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softactive.core.manager.PostTask;
import com.softactive.core.object.Indicator;
import com.softactive.core.object.MyConstants;
import com.softactive.editor.common.view.DataTable;
import com.softactive.editor.wb.manager.GlobalRiskFactorGenerator;
import com.softactive.editor.wb.manager.RegionalRiskFactorGenerator;
import com.softactive.editor.wb.requester.WorldBankIndicatorSearcher;

import lombok.Getter;
import lombok.Setter;

@Component
public class WorldBankIndicatorBean implements Serializable, MyConstants {
	private static final long serialVersionUID = 6276875329933202159L;

	// searcher
	@Getter @Setter
	@Autowired
	private WorldBankIndicatorSearcher iSearcher;
	@Getter
	@Setter
	private DataTable<Indicator> dtSearcher = new DataTable<Indicator>();
	private PostTask post = new PostTask() {
		@Override
		public void onPost() {
			dtSearcher = new DataTable<>();
		}
	};

	// Generator
	@Getter @Setter
	@Autowired
	private GlobalRiskFactorGenerator gGenerator;
	@Getter @Setter
	@Autowired
	private RegionalRiskFactorGenerator rGenerator;

	@Getter
	@Setter
	private DataTable<Indicator> dtGenerator;

	@PostConstruct
	protected void init() {
		iSearcher.setPostTask(post);
//		iSearcher.start();
	}

	// generator
	public void generate() {
		Map<String, List<Indicator>> indMap = getMappedIndicators();
		gGenerator.startForIndicators(indMap.get(SOURCE_WORLD_BANK));
		rGenerator.startForIndicators(indMap.get(SOURCE_WORLD_BANK));
	}

	public void refreshGeneratorList() {
		dtGenerator = new DataTable<Indicator>(dtSearcher.getSelected());
	}

	private Map<String, List<Indicator>> getMappedIndicators() {
		Map<String, List<Indicator>> answer = new HashMap<>();
		List<Indicator> regionalIndicators = new ArrayList<>();
		List<Indicator> globalIndicators = new ArrayList<>();
		for (Indicator i : dtGenerator.getSelected()) {
			if (i.getSourceCode().equals(SOURCE_WORLD_BANK)) {
				globalIndicators.add(i);
			} else if (i.getSourceCode().equals(SOURCE_WORLD_BANK)) {
				regionalIndicators.add(i);
			}
		}
		answer.put(SOURCE_WORLD_BANK, regionalIndicators);
		answer.put(SOURCE_WORLD_BANK, globalIndicators);
		return answer;
	}
}
