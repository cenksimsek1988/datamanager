package com.softactive.editor.wb.manager;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softactive.core.manager.MyException;
import com.softactive.core.object.Indicator;
import com.softactive.core.service.IndicatorService;
import com.softactive.core.wb.AbstractWorldBankHandler;

import lombok.Getter;
import lombok.Setter;

@Component
public class WorldBankIndicatorHandler extends AbstractWorldBankHandler<Indicator> {
	private static final long serialVersionUID = -1436987979164143111L;
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String UNIT = "unit";

	public static final String SOURCE = "source";
	public static final String SOURCE_SHORT_NAME = "value";

	public static final String SOURCE_NOTE = "sourceNote";
	public static final String SOURCE_FULL_NAME = "sourceOrganization";
	
	public static final String TOPIC = "topic";


	public static final String TAG = "topics";
	public static final String TAG_NAME = "value";
	public static final String TAG_ID = "id";

	public static final String INDICATOR_TYPE_WORLD = "WLD";
	public static final String INDICATOR_TYPE_REGION = "RGN";
	
	public static final List<String> SOURCES = Arrays.asList(SOURCE_WORLD_BANK, SOURCE_WORLD_BANK);
	
	@Autowired
	private IndicatorService is;
	@Getter @Setter
	@Autowired
	private RegionalRiskFactorGenerator rGenerator;

	@Override
	protected Indicator getObject(JSONObject jo, Map<String, Object> requestParams) throws MyException {
		String apiCode = resolveValidString(jo, ID, null);
		Indicator in = is.findByApiCodeAndSources(apiCode, SOURCES);
		if(in!=null) {
			return in;
		}
		Indicator item = new Indicator();
		item.setApiCode(apiCode);
		item.setName(resolveValidString(jo, NAME, null));
		item.setUnit(resolveValidString(jo, UNIT, null));

		// trying to guess a default source code
		JSONObject sjo = resolveValidJSONObject(jo, SOURCE);
		String sourceId = optValidString(sjo, ID, 2);
		if(sourceId != null) {
			item.setSubSource(sourceId);
		}
		JSONObject tjo = resolveValidJSONObject(jo, TOPIC);
		String topicId = optValidString(tjo, ID, 2);
		if(topicId != null) {
			Integer topicIdInt = optValidIntegerId(topicId);
			item.setCategoryId(topicIdInt);
		}
		String description = optValidString(tjo, SOURCE_NOTE, null);
		if(description != null) {
			item.setDescription(description);
		}
		item.setSourceCode(SOURCE_WORLD_BANK);
		item.setFrequencyId(FREQUENCY_ANNUAL);
		int id = is.insert(item);
		item.setId(id);
		return item;
	}
	
	public void generate(List<Indicator> indicators) {
		rGenerator.startForIndicators(indicators);
	}

	@Override
	public void onListSuccesfullyParsed(List<Indicator> list, Map<String, Object> requestParams) {
		generate(list);		
	}
}
