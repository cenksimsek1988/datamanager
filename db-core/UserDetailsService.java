package com.softactive.core.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.softactive.core.object.ApiUser;
import com.softactive.core.object.User;
import com.softactive.core.utils.SpringUtil;

public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService, Serializable {
	
	private static final long serialVersionUID = 3443802682185014485L;
	
	@Autowired UserService userService;
	@Autowired BCryptPasswordEncoder passwordEncoder;
	@Autowired UserRoleService userRoleService;
	
	private Map<String, ApiUser> usernameApiUserMap;
	private Map<String, User> usernameUserMap;
	private Map<String, List<String>> userAuthMap;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		String apiUserHashCode = passwordEncoder.encode(username);
		if (usernameApiUserMap!=null && usernameApiUserMap.containsKey(username)) {
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_HOME"));
			ApiUser apiUser = usernameApiUserMap.get(username);
			org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(
					apiUser.getEmail(),
					apiUserHashCode,
					enabled,
					accountNonExpired,
					credentialsNonExpired,
					accountNonLocked,
					grantedAuthorities);
			addUserAuth(apiUser.getEmail(), apiUser.getProject_codes());
			return user;
		}
		User u = userService.find(username);
		if (u!=null) {
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_HOME"));
			org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(
					u.getUserCode(), u.getUserPassword(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuthorities);
			addUserAuth(u.getUserCode(), userRoleService.listRoleCodes(u.getUserCode()));
			addLoginUser(u);
			return user;
		}else {
			throw new UsernameNotFoundException("No such user found!");
		}
	}
	
	private void addUserAuth(String username, List<String> authList) {
		if (userAuthMap==null) {
			userAuthMap = new HashMap<String, List<String>>();
		}
		userAuthMap.put(username, authList);
	}
	
	public void addLoginUser(User user) {
		if (usernameUserMap==null) {
			usernameUserMap = new HashMap<String, User>();
		}
		usernameUserMap.put(user.getUserCode(), user);
	}
	
	public void addApiUser(ApiUser apiUser) {
		if (usernameApiUserMap==null) {
			usernameApiUserMap = new HashMap<String, ApiUser>();
		}
		usernameApiUserMap.put(apiUser.getEmail(), apiUser);
	}
	
	public boolean isSubProjectAuthenticated(String subProjectCode) {
		String user = SpringUtil.getLoggedUser();
		return userAuthMap.get(user)!=null && userAuthMap.get(user).contains(subProjectCode);
	}
	
	public String getPrettyUserName() {
		String username = SpringUtil.getLoggedUser();
		if (usernameApiUserMap!=null && usernameApiUserMap.containsKey(username)) {
			return usernameApiUserMap.get(username).getFirstname() + " " + usernameApiUserMap.get(username).getLastname();
		}else if (usernameUserMap!=null && usernameUserMap.containsKey(username)) {
			return usernameUserMap.get(username).getFullname();
		}
		return null;
	}
}
