package com.softactive.updater.ef.manager;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import com.softactive.core.object.Indicator;
import com.softactive.updater.common.manager.AbstractExcelPriceHandler;

import lombok.Getter;
import lombok.Setter;
import okhttp3.Response;

@Component
@Getter @Setter
public class EFHandler extends AbstractExcelPriceHandler {
	private static final long serialVersionUID = 2204598106977384686L;
	public static final String EF_FILE_NAME = "C:\\\\Users\\\\riskactive\\\\Desktop\\\\GRWA\\\\excels\\ef.xlsx";
	
	@PostConstruct
	public void init() {
		dateIndex = 0;
		regionCodeIndex = 2;
		map = getMap();
	}
	
	
	private Map<Integer, Indicator> getMap() {
		if(map == null) {
			map = new HashMap<>();
			List<Indicator> prIndicators = is.getIndicatorsBySource(SOURCE_POLITICAL_RISK);
			for(Indicator i:prIndicators) {
				map.put(i.getExcelColumnIndex(), i);
			}
		}
		return map;
	}

//	@Override
//	protected Map<Integer, String> getMap() {
//		Map<Integer, String> map = new HashMap<>();
//		map.put(5, "EF:gc");
//		map.put(6, "EF:ts");
//		map.put(7, "EF:gei");
//		map.put(8, "EF:tmitr");
//		map.put(9, "EF:tmiptr");
//		map.put(10, "EF:gs");
//		map.put(11, "EF:ji");
//		map.put(12, "EF:ic");
//		map.put(13, "EF:ppr");
//		map.put(14, "EF:mi");
//		map.put(15, "EF:lsi");
//		map.put(16, "EF:lec");
//		map.put(17, "EF:rrsrp");
//		map.put(18, "EF:rp");
//		map.put(19, "EF:bcc");
//		map.put(20, "EF:lspr");
//		map.put(21, "EF:mgm1");
//		map.put(22, "EF:sdi");
//		map.put(23, "EF:inf");
//		map.put(24, "EF:fofcba");
//		map.put(25, "EF:sm");
//		map.put(26, "EF:rtt");
//		map.put(27, "EF:mt");
//		map.put(28, "EF:sdtr");
//		map.put(29, "EF:nttb");
//		map.put(30, "EF:ccie");
//		map.put(31, "EF:rtb");
//		map.put(32, "EF:bme");
//		map.put(33, "EF:foir");
//		map.put(34, "EF:cc");
//		map.put(35, "EF:ffv");
//		map.put(36, "EF:cmcp");
//		map.put(37, "EF:itf");
//		map.put(38, "EF:bo");
//		map.put(39, "EF:psc");
//		map.put(40, "EF:ircn");
//		map.put(41, "EF:cmr");
//		map.put(42, "EF:hrmw");
//		map.put(43, "EF:hfr");
//		map.put(44, "EF:ccb");
//		map.put(45, "EF:hr");
//		map.put(46, "EF:mcwd");
//		map.put(47, "EF:csc");
//		map.put(48, "EF:lmr");
//		map.put(49, "EF:ar");
//		map.put(50, "EF:bc");
//		map.put(51, "EF:sb");
//		map.put(52, "EF:epbf");
//		map.put(53, "EF:lr");
//		map.put(54, "EF:tc");
//		map.put(55, "EF:br");
//		map.put(56, "EF:rgl");
//		map.put(57, "EF:ef");
//		return map;
//	}

	@Override
	protected Date resolveDate(String dateString) {
		return Date.valueOf(dateString + "-12-31");
	}

	@Override
	protected String getFileName(Response r) {
//		return r.header("filename");
		return EF_FILE_NAME;
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
