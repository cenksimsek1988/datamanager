package com.softactive.editor.fred.manager;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;

import org.apache.commons.beanutils.PropertyUtils;

import com.softactive.core.object.Indicator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@EqualsAndHashCode(callSuper = true)
public class FredIndicator extends Indicator {
	private static final long serialVersionUID = 4802707390502579327L;
	private Date startDate;
	private Date endDate;
	
	public FredIndicator(Indicator in) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		PropertyUtils.copyProperties(this, in);
	}

	public FredIndicator() {}
	
	public static Indicator getIndicator(FredIndicator fi) {
		Indicator i = new Indicator();
		i.setAdjustmentId(fi.getAdjustmentId());
		i.setApiCode(fi.getApiCode());
		i.setDefaultTerm(fi.getDefaultTerm());
		i.setDescription(fi.getDescription());
		i.setFrequencyId(fi.getFrequencyId());
		i.setId(fi.getId());
		i.setName(fi.getName());
		i.setPostCode(fi.getPostCode());
		i.setPreCode(fi.getPreCode());
		i.setSourceCode(fi.getSourceCode());
		i.setSubSource(fi.getSourceCode());
		i.setUnit(fi.getUnit());
		return i;
	}
	
	public static FredIndicator getFredIndicator(Indicator i) {
		FredIndicator fi = new FredIndicator();
		fi.setAdjustmentId(i.getAdjustmentId());
		fi.setApiCode(i.getApiCode());
		fi.setDefaultTerm(i.getDefaultTerm());
		fi.setDescription(i.getDescription());
		fi.setFrequencyId(i.getFrequencyId());
		fi.setId(i.getId());
		fi.setName(i.getName());
		fi.setPostCode(i.getPostCode());
		fi.setPreCode(i.getPreCode());
		fi.setSourceCode(i.getSourceCode());
		fi.setSubSource(i.getSourceCode());
		fi.setUnit(i.getUnit());
		return fi;
	}
}
