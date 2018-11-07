package com.softactive.editor.common.page;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.softactive.core.object.Internationalization;

@Component("languageBean")
@Scope("session")
public class LanguageBean implements Serializable{

	private static final long serialVersionUID = -3111230751486513850L;
	
	private String localeCode;
	private List<Internationalization> internList;

	@PostConstruct
	public void init(){
		internList = Lists.newArrayList(Internationalization.values());
	}
	
	public String getLocaleCode() {
		if (localeCode==null){
			setLocaleCode(Internationalization.TR.getLocale().getLanguage());
		}
		return localeCode;
	}
	
	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
		FacesContext.getCurrentInstance().getViewRoot().setLocale(Internationalization.getInternatianalizationByCode(localeCode).getLocale());
	}
	
	public List<Internationalization> getInternList() {
		return internList;
	}
	
	public void setInternList(List<Internationalization> internList) {
		this.internList = internList;
	}
	
}
