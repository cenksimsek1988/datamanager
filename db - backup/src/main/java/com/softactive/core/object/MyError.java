package com.softactive.core.object;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MyError {
	String result;
	int type;
	
	public MyError(int type, String result) {
		this.type = type;
		this.result = result;
	}
}
