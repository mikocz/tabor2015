package cz.miko.tabor.core.service;

import cz.miko.tabor.core.dao.UserMapper;
import cz.miko.tabor.core.event.CrudOperation;
import cz.miko.tabor.core.event.EntityUpdateEvent;
import cz.miko.tabor.core.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Service
public class UserManager extends AbstractManager {

	@Autowired()
	private UserMapper userMapper;

	public List<User> getUsers() {
		return userMapper.getUsers(null);
	}

	public List<User> getUsers(String orderBy) {
		Map<String, Object> params = new HashMap<>();
		params.put("orderBy", orderBy);
		return userMapper.getUsers(params);
	}

	public void storeUser(@NotNull User user) {
		if (user.getId() == null) {
			userMapper.insertUser(user);
			getApplicationEventPublisher().publishEvent(new EntityUpdateEvent(this,User.class, CrudOperation.INSERT, user));
		} else {
			userMapper.updateUser(user);
			getApplicationEventPublisher().publishEvent(new EntityUpdateEvent(this, User.class, CrudOperation.DELETE, user));
		}
	}

	public void deleteUserById(int userId) {
		userMapper.deleteUserById(userId);
	}
}
