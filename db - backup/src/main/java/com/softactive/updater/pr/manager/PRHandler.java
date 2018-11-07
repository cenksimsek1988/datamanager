package com.softactive.updater.pr.manager;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import com.softactive.core.object.Indicator;
import com.softactive.updater.common.manager.AbstractExcelPriceHandler;

import okhttp3.Response;

@Component
public class PRHandler extends AbstractExcelPriceHandler {
	private static final long serialVersionUID = -526130472012738712L;
	public static final String PR_FILE_NAME = "C:\\\\Users\\\\riskactive\\\\Desktop\\\\GRWA\\\\excels\\pr.xlsx";
	
	@PostConstruct
	public void init() {
		dateIndex = 5;
		regionCodeIndex = 3;
		map = getMap();
	}

	protected Map<Integer, Indicator> getMap() {
		if(map == null) {
			map = new HashMap<>();
			List<Indicator> prIndicators = is.getIndicatorsBySource(SOURCE_POLITICAL_RISK);
			for(Indicator i:prIndicators) {
				map.put(i.getExcelColumnIndex(), i);
			}
		}
//		map.put(11, "PR:priq");
//		map.put(6, "PR:cp");
//		map.put(7, "PR:prcl");
//		map.put(7, "PR:da");
//		map.put(9, "PR:pg");
//		map.put(10, "PR:pa");
		return map;
	}

	@Override
	protected Date resolveDate(String dateString) {
		return Date.valueOf(dateString + "-12-31");
	}

	@Override
	protected String getFileName(Response r) {
		return PR_FILE_NAME;
	}

	@Override
	protected int getArrayStartIndex() {
		return 1;
	}

	@Override
	protected int getSheetIndex() {
		return 0;
	}

	@Override
	protected Map<String, Object> getMetaMap(Workbook r) {
		return null;
	}

	@Override
	protected boolean hasNext(Map<String, Object> metaMap) {
		return false;
	}
}
