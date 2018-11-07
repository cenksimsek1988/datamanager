package com.softactive.editor.fred.manager;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

import com.softactive.core.object.Region;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FredRegion extends Region {
	private static final long serialVersionUID = -6983809749075325881L;
	private String msg;
	
	public FredRegion(Region r) {
		try {
			PropertyUtils.copyProperties(this, r);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
}
