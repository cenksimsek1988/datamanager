package com.softactive.core.service;

import java.util.List;

import com.softactive.core.object.Pullable;

public abstract class OrderableService<P extends Pullable> extends AbstractDataService<P> {

	public OrderableService(String tableName) {
		super(tableName);
	}

	public abstract List<P> getListFrom(String src, Integer last);
}