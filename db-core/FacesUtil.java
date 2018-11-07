package com.softactive.core.utils;

import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.context.RequestContext;


public class FacesUtil {

	private static String EXCEPTION_BUNDLE = "exp";
	private static String MESSAGES_BUNDLE = "msg";
	private static String INFORMATION_BUNDLE = "info";

	public static String getGeneralBundleKey(String bundleName, String key) {
		return FacesContext
				.getCurrentInstance()
				.getApplication()
				.getResourceBundle(FacesContext.getCurrentInstance(),
						bundleName).getString(key);
	}

	public static String getExceptionMessage(String key) {
		String bundleName = EXCEPTION_BUNDLE;
		return FacesContext
				.getCurrentInstance()
				.getApplication()
				.getResourceBundle(FacesContext.getCurrentInstance(),
						bundleName).getString(key);
	}

	public static String getMessagesMessage(String key) {
		String bundleName = MESSAGES_BUNDLE;
		return FacesContext
				.getCurrentInstance()
				.getApplication()
				.getResourceBundle(FacesContext.getCurrentInstance(),
						bundleName).getString(key);
	}

	public static String getInformationMessage(String key) {
		String bundleName = INFORMATION_BUNDLE;
		return FacesContext
				.getCurrentInstance()
				.getApplication()
				.getResourceBundle(FacesContext.getCurrentInstance(),
						bundleName).getString(key);
	}

	public static void addErrorMessage(Exception exp) {
		String message = getExceptionMessage(exp);
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
		context.addMessage("", facesMessage);
	}

	public static void addInfoMessage(String key, Object... parameters) {
		String message = Utils.formatMessage(getInformationMessage(key), parameters);
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, message, null);
		context.addMessage("", facesMessage);
	}
	
	public static void addWarnMessage(String key, Object... parameters) {
		String message = Utils.formatMessage(getInformationMessage(key), parameters);
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, message, null);
		context.addMessage("", facesMessage);
	}

	public static void addErrorMessage(String key, Object... parameters) {
		String message = Utils.formatMessage(getInformationMessage(key),
				parameters);
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
		context.addMessage("", facesMessage);
	}

	private static String getExceptionMessage(Exception exp) {

		String message = exp.getMessage();
		while (exp.getCause() != null) {
			exp = (Exception) exp.getCause();

			message += " <br> ";
			message += exp.getMessage();
		}

		return message;

	}
	
	public static void addCallBackParam(String name, Object value){
		 RequestContext.getCurrentInstance().addCallbackParam(name, value);
	}

	public static void putApplicationMapValue(String key, Object value) {
		FacesContext.getCurrentInstance().getExternalContext().getApplicationMap()
				.put(key, value);
	}

	public static void removeApplicationMapValue(String key) {
		FacesContext.getCurrentInstance().getExternalContext().getApplicationMap()
				.remove(key);
	}

	public static Object getApplicationMapValue(String key) {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getApplicationMap().get(key);
	}
	
	public static void putSessionMapValue(String key, Object value) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.put(key, value);
	}

	public static void removeSessionMapValue(String key) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.remove(key);
	}

	public static Object getSessionMapValue(String key) {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get(key);
	}

	public static String getRequestParameter(String name) {
		return (String) FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get(name);
	}

	public static String getAbsolutePath(String relativePath) {
		ServletContext sc = (ServletContext) FacesContext.getCurrentInstance()
				.getExternalContext().getContext();
		return sc.getRealPath(relativePath);
	}

	public static void clearComponent(UIComponent comp) {
		for (UIComponent child : comp.getChildren()) {
			if (child instanceof EditableValueHolder)
				clearComponent((EditableValueHolder) child);
			if (child.getChildCount() > 0) {
				clearComponent(child);
			}
		}
	}

	public static void clearComponent(EditableValueHolder comp) {
		comp.setSubmittedValue(null);
		comp.setValue(null);
		comp.setLocalValueSet(false);
		comp.setValid(true);
	}

}
