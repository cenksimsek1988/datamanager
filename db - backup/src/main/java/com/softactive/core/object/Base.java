package com.softactive.core.object;

import java.io.Serializable;

import org.apache.commons.beanutils.PropertyUtils;

import lombok.Getter;

@Getter
public abstract class Base implements MyConstants, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5093023564597618979L;

	public Base() {
	}

	public Object getValue(String fieldName) {
		try {
			return PropertyUtils.getProperty(this, fieldName);
		} catch (Exception e) {
			return null;
		}
	}

}