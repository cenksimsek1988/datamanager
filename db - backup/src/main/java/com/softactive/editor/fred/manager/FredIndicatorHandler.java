package com.softactive.editor.fred.manager;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softactive.core.fred.AbstractFredHandler;
import com.softactive.core.manager.MyException;
import com.softactive.core.object.CountryStandard;
import com.softactive.core.object.Indicator;
import com.softactive.core.object.MyError;
import com.softactive.core.object.Region;
import com.softactive.core.service.CountryStandardService;
import com.softactive.core.service.IndicatorService;

import lombok.Getter;
import lombok.Setter;

@Component
public class FredIndicatorHandler extends AbstractFredHandler<FredIndicator> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7359219173523588951L;
	public static final String FREQUENCY_SHORT = "frequency_short";
	public static final String NOTES = "notes";
	public static final String SEASONAL_ADJUSTMENT = "seasonal_adjustment_short";
	public static final String UNIT = "units_short";
	public static final String TITLE = "title";
	public static final String ID = "id";
	public static final String ADJUSTMENT = "seasonal_adjustment_short";
	public static final String START = "observation_start";
	public static final String END = "observation_end";
	public static final List<String> SOURCES = Arrays.asList(SOURCE_FRED, SOURCE_FRED);
	public static final String FRED_ANNUAL = "A";
	public static final String FOR = " for ";
	public static final String IN = " in ";
	public static final String PR = ", P.R.: ";
	public static final String U_S = "U.S. ";
	public static final String TO = " to ";
	public static final String FROM = " from ";
	public static final String[] PREPOSITIONS = new String[] {
			FOR,
			IN,
			TO,
			FROM,
			PR
	};

	@Getter @Setter
	private List<FredRegion> errorRegionList = new ArrayList<FredRegion>();

	@Autowired
	private FredRegionalRiskFactorGenerator gen;


	@Autowired
	private IndicatorService is;

	@Autowired
	private CountryStandardService css;

	private String getBaseName(String title, Region region) {
		if(region.getIsoCode().equals(CODE_US)) {
			if(title.startsWith(U_S)) {
				return title.substring(U_S.length());
			}
		}
		String answer = getBaseName(title, region.getName());
		if(answer==null) {
			List<CountryStandard> altRegions = css.getCountryStandardsByIsoCode(region.getIsoCode());
			for(CountryStandard alt:altRegions) {
				answer = getBaseName(title, alt.getName());
				if(answer!=null) {
					break;
				}
			}
		}
		if(answer==null) {
			answer = title;
		}
		return answer;
	}

	private String getBaseName(String title, String regionName) {
		String titleAllSmall = title.toLowerCase();
		String toCut = null;
		String answer = null;
		String regionNameAllSmall = regionName.toLowerCase();
		for(String prep:PREPOSITIONS) {
			toCut = prep + regionNameAllSmall;
			if(titleAllSmall.contains(toCut)) {
				int toCutStart = titleAllSmall.lastIndexOf(toCut);
				int toCutEnd = toCutStart + toCut.length();
				answer = title.substring(0, toCutStart);
				if(toCutEnd < title.length()) {
					answer += title.substring(toCutEnd);
				}
				return answer;
			}
		}
		return null;
	}

	@Override
	protected FredIndicator getObject(JSONObject o, Map<String, Object> requestParams) throws MyException {
		String fullName = resolveValidString(o, TITLE, null);
		Region r = (Region)requestParams.get(PARAM_REGION);
		String name = getBaseName(fullName, r);
		String apiCode = String.valueOf(o.opt(ID));
		Indicator in = is.findByNameAndSource(name, SOURCE_FRED);
		FredIndicator item = new FredIndicator();
		if(in!=null) {
			item = FredIndicator.getFredIndicator(in);
		}
		item.setApiCode(apiCode);
		Date start = Date.valueOf((String) o.opt(START));
		item.setStartDate(start);
		Date end = Date.valueOf((String) o.opt(END));
		item.setEndDate(end);	
		if (in==null) {
			item.setName(name);
			String descripton = String.valueOf(o.opt(NOTES));
			item.setDescription(descripton);
			String unit = String.valueOf(o.opt(UNIT));
			item.setUnit(unit);
			item.setSourceCode(SOURCE_FRED);
			String fredFrq = String.valueOf(o.opt(FREQUENCY_SHORT));
			setFrequencies(item, fredFrq);
			String adj = String.valueOf(o.opt(ADJUSTMENT));
			item.setAdjustmentId(adj);
			//			System.out.println("indicator id: " + item.getId());
			//			Indicator toInsert = FredIndicator.getIndicator(item);
			int id = is.insert(item);
			item.setId(id);
		}
		return getMinimizedFredIndicator(item);
	}

	private FredIndicator getMinimizedFredIndicator(FredIndicator i) {
		i.setAdjustmentId(null);
		i.setDefaultTerm(null);
		i.setDescription(null);
		i.setName(null);
		i.setPostCode(null);
		i.setPreCode(null);
		i.setSubSource(null);
		i.setUnit(null);
		return i;
	}

	private void setFrequencies(Indicator i, String frq) {
		if(frq == FRED_ANNUAL) {
			i.setFrequencyId(FREQUENCY_ANNUAL);
		} else {
			i.setFrequencyId(frq);
		}
	}

	@Override
	public void onListSuccesfullyParsed(List<FredIndicator> list, Map<String, Object> requestParams) {
		Region r = (Region)requestParams.get(PARAM_REGION);
		rs.setSource(r, SOURCE_FRED, true);
		gen.start(list, r);
	}

	@Override
	public void onError(Map<String, Object> requestParams) {
		Region r = (Region)requestParams.get(PARAM_REGION);
		FredRegion fr = new FredRegion(r);
		MyError er = (MyError) requestParams.get(PARAM_ERROR);
		fr.setMsg(er.getResult());
		errorRegionList.add(fr);
	}
}
