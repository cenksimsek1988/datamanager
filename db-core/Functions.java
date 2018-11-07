package com.softactive.core.utils;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public final class Functions {

	private Functions() {
		
    }
	
    public static String getI18nMessage(ResourceBundle bundle, String key, String paramsAsString) {
        String message = bundle.getString(key);
    	String[] params = paramsAsString.split(",");
    	return MessageFormat.format(message, params);
    }
    
    public static String concat(String first, String second) {
    	return first + second;
    }
    
    public static String optionatmf(Double delta) {
    	if (delta==0.0) {
    		return "ATMF";
    	}else {
    		return "%" + delta + " OTM";
    	}
    }
	
}
