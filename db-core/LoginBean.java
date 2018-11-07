package com.softactive.editor.common.page;

import java.io.Serializable;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.softactive.core.service.AuthenticationService;

import lombok.Getter;
import lombok.Setter;


@Scope("session")
@Component
@Getter @Setter
public class LoginBean implements Serializable {
	
	private static final long serialVersionUID = -4215769352545715164L;
	
	private String username;
	private String password;
	@Autowired
	private AuthenticationService authenticationService;
	private String ip;
	private Map<String, String> servletParameterMap;
	
	@Autowired
	public LoginBean(AuthenticationService authenticationService) {		
		this.authenticationService = authenticationService;
	}
	
	public String login() {
		if (authenticationService.login(username, password)) {
			return "/pages/home.jsf?faces-redirect=true";
		} else {
			return "/pages/login.jsf?faces-redirect=true&param=authFail";
		}
	}

	public String logout() {
		authenticationService.logout();
		return "/pages/login.jsf?faces-redirect=true&param=logOut";
	}
	
}
