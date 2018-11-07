package com.softactive.core.utils;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class SpringUtil implements ApplicationContextAware {
	
	private static ApplicationContext ctx = null;
	
	
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		SpringUtil.ctx = arg0;
	}
	
	public static Object findBean(String beanName) {
		return ctx.getBean(beanName);
	}
	
	public static Object findBean(String beanName, Object... args) {
		return  ctx.getBean(beanName, args);
	}
	
	public static void setAuthentication(Authentication authenticate) {
		SecurityContextHolder.getContext().setAuthentication(authenticate);
	}
	
	public static String getLoggedUser() {
        return SecurityContextHolder.getContext().getAuthentication()==null ? "" : ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

}