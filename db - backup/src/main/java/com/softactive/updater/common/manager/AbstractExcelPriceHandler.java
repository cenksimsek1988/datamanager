package com.softactive.updater.common.manager;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;

import com.softactive.core.manager.AbstractExcelHandler;
import com.softactive.core.manager.MyException;
import com.softactive.core.object.Indicator;
import com.softactive.core.object.Price;
import com.softactive.core.object.Region;
import com.softactive.core.object.RiskFactor;
import com.softactive.core.service.IndicatorService;
import com.softactive.core.service.PriceService;

public abstract class AbstractExcelPriceHandler extends AbstractExcelHandler<Price> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1073563475246636507L;
	@Autowired
	private PriceService ps;
	@Autowired
	protected IndicatorService is;
	protected Map<Integer, Indicator> map = null;

	@Override
	protected List<Price> onEachRow(Row row) {
		String regionCode = null;
		Integer regionId = null;
		List<Price> list = new ArrayList<>();
		try {
			regionCode = row.getCell(regionCodeIndex).getStringCellValue();
			Region r = rs.findRegionByIsoCode(regionCode);
			if(r!=null) {
				regionId = r.getId();
			} else {
				System.out.println("Couldnt get region with region code: " + regionCode);
			}
		} catch (NullPointerException e) {
			System.out.println("Couldnt get region code from specific row: "+row.getRowNum()+ " and column: "+regionCodeIndex);
			return null;
		}
		Date dataDate = resolveDate(String.valueOf((int) row.getCell(dateIndex).getNumericCellValue()));
		System.out.println("Region Code: " + regionCode);
		Iterator<Integer> keys = map.keySet().iterator();
		while (keys.hasNext()) {
			Integer key = keys.next();
			Indicator indicator = map.get(key);
			System.out.println(indicator.getName());
			RiskFactor rf = rfs.findRiskFactorByIndicatorRegionAndFrequency(indicator.getId(), regionId, indicator.getFrequencyId());
			Double value = getDoubleValue(row.getCell(key));
			if (value == null) {
				continue;
			}
			Price p = null;
			try {
				p = getObject(null, null);
			} catch (MyException e) {
				e.printStackTrace();
			}
			p.setDataDate(dataDate);
			p.setRiskFactorId(rf.getId());
			p.setPrice(value);
			ps.save(p);
			list.add(p);
		}
		return list;
	}
	@Override
	protected Price getObject(Iterator<Cell> o, Map<String, Object> requestParams) throws MyException {
		return new Price();
	}
}
