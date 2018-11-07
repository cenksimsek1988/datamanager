package com.softactive.updater.common.bean;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softactive.core.manager.ParameterWrapper;
import com.softactive.core.manager.PostTask;
import com.softactive.core.object.MyConstants;
import com.softactive.core.object.RiskFactor;
import com.softactive.core.service.RiskFactorService;
import com.softactive.updater.fred.manager.FredPriceRequester;
import com.softactive.updater.wb.manager.WorldBankPriceRequester;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Component
public class RequesterBean implements MyConstants{
	private PostTask wbPost = new PostTask() {
		@Override
		public void onPost() {
			wbpr.start();
		}
	};
	private PostTask frPost = new PostTask() {
		@Override
		public void onPost() {
			fpr.start();
		}
	};

	@Autowired
	private WorldBankPriceRequester wbpr;
	@Autowired
	private FredPriceRequester fpr;
	@Autowired
	private RiskFactorService rfs;
	private int wbFrqIndex = 0;
	private int frFrqIndex = 0;

	@PostConstruct
	protected void init() {
		wbpr.setSharedParamWrapper(getWBRiskFactorParamWrapper());
		wbpr.setPost(wbPost);
		fpr.setSharedParamWrapper(getFredRiskFactorParamWrapper());
		fpr.setPost(frPost);
	}

	public void update() {
		System.out.println("attemps to update");
		wbpr.start();
//		fpr.start();
	}

	private ParameterWrapper getWBRiskFactorParamWrapper() {
		return new ParameterWrapper() {
			@Override
			public final Map<String, Object> getParams() {
				return getWBRiskFactorParam();			
			}
		};
	}

	private ParameterWrapper getFredRiskFactorParamWrapper() {
		return new ParameterWrapper() {
			@Override
			public final Map<String, Object> getParams() {
				return getFredRiskFactorParam();		
			}
		};
	}

	private final Map<String, Object> getWBRiskFactorParam(){
		Map<String, Object> params = new HashMap<String, Object>();
		RiskFactor rf = getWBUpdateRiskFactor();
		if(rf==null) {
			System.out.println("For World Bank: Everything is done, need a rest till tomorrow..");
			return null;
		}
		params.put(PARAM_RISK_FACTOR, rf);
		return params;
	}

	private final Map<String, Object> getFredRiskFactorParam(){
		Map<String, Object> params = new HashMap<String, Object>();
		RiskFactor rf = getFRUpdateRiskFactor();
		if(rf==null) {
			System.out.println("For FRED: Everything is done, need a rest till tomorrow..");
			return null;
		}
		params.put(PARAM_RISK_FACTOR, rf);
		return params;
	}

	private RiskFactor getFRUpdateRiskFactor() {
		RiskFactor rf = null;
		while(frFrqIndex < FREQUENCIES.length) {
			rf = rfs.pickRiskFactorBySourceAndFrequency(SOURCE_FRED, FREQUENCIES[frFrqIndex]);
			if(rf==null) {
				frFrqIndex++;
				return getFRUpdateRiskFactor();
			} else {
				break;
			}
		}
		return rf;
	}

	private RiskFactor getWBUpdateRiskFactor() {
		RiskFactor rf = null;
		while(wbFrqIndex < FREQUENCIES.length) {
			rf = rfs.pickRiskFactorBySourceAndFrequency(SOURCE_WORLD_BANK, FREQUENCIES[wbFrqIndex]);
			if(rf==null) {
				wbFrqIndex++;
				return getWBUpdateRiskFactor();
			} else {
				break;
			}
		}
		return rf;
	}
}
