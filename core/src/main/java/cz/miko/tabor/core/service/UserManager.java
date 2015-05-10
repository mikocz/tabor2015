package cz.miko.tabor.core.service;

import cz.miko.tabor.core.dao.UserMapper;
import cz.miko.tabor.core.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Service
public class UserManager {

	@Autowired(	)
	private UserMapper userMapper;

	public List<User> getUsers() {
		return userMapper.getUsers();
	}
}
