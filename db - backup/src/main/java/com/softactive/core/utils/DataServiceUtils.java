package com.softactive.core.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.softactive.core.object.Base;
import com.softactive.core.service.AbstractDataService;

@Lazy
@Component
public class DataServiceUtils {

	@Autowired
	List<AbstractDataService<?>> dataServices;

	public AbstractDataService<?> getDataServiceFromJdbcObject(Class<?> type) {
		for (AbstractDataService<? extends Base> service : dataServices) {
			if (type.getName().equalsIgnoreCase(service.getParametrizedTypeOfT().getName())) {
				return service;
			}
		}
		return null;
	}

}
