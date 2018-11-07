package com.softactive.core.object;

import java.util.Locale;

import lombok.Getter;

@Getter
public enum Internationalization {
	
	TR("tr", "common.label.language.turkish", new Locale("tr","TR")),
	EN("en", "common.label.language.english", Locale.ENGLISH);
	
	private String code;
	private String messageCode;
	private Locale locale;
	
	private Internationalization(String code, String messageCode, Locale locale){
		this.code = code;
		this.messageCode = messageCode;
		this.locale = locale;
	}
	
	public static Internationalization getInternatianalizationByCode(String code){
		for (Internationalization i : values()){
			if (code.startsWith(i.getCode()) || i.getCode().startsWith(code)){
				return i;
			}
		}
		return null;
	}
}
