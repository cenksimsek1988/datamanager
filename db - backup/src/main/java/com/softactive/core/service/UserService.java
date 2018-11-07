package com.softactive.core.service;

import org.springframework.stereotype.Repository;

import com.softactive.core.object.User;

@Repository
public class UserService extends AbstractDataService<User>{

	public UserService() {
		super("cmn_user");
	}
	
	public User find(String userCode) {
		return queryForObject(initQuery() + " where user_code=?", new Object[] {userCode});
	}

	@Override
	public void save(User t) {
		User in = find(t.getId());
		if(in==null) {
			insert(t);
		} else {
			t.setId(in.getId());
			update(t);
		}
	}

}
