package com.softactive.core.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.softactive.core.object.UserRole;

@Repository
public class UserRoleService extends AbstractDataService<UserRole>{

	public UserRoleService() {
		super("cmn_user_role");
	}
	
	public List<UserRole> list(String userCode) {
		return query(initQuery() + " where user_code=?", new Object[] {userCode});
	}
	
	public List<String> listRoleCodes(String userCode){
		List<String> returnList = new ArrayList<String>();
		for (UserRole role : list(userCode)) {
			returnList.add(role.getRole());
		}
		return returnList;
	}

	@Override
	public void save(UserRole t) {
		insert(t);
	}

}
