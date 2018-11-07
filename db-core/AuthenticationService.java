package com.softactive.core.service;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.softactive.core.utils.SpringUtil;

@Service("authenticationService")
public class AuthenticationService implements Serializable {

	private static final long serialVersionUID = 6065902852399011492L;

	@Autowired
	private AuthenticationManager authenticationManager;

	public boolean login(String username, String password) {
		try {
			Authentication result = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			SpringUtil.setAuthentication(result);
			return true;
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void logout() {
		SpringUtil.setAuthentication(null);
	}
}
