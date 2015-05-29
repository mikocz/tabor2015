package cz.miko.tabor.core.service;

import cz.miko.tabor.core.dao.UserMapper;
import cz.miko.tabor.core.event.UserCreatedEvent;
import cz.miko.tabor.core.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Service
public class UserManager implements ApplicationEventPublisherAware {

	@Setter
	@Getter(AccessLevel.PROTECTED)
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired()
	private UserMapper userMapper;

	public List<User> getUsers() {
		return userMapper.getUsers();
	}

	public void storeUser(@NotNull User user) {
		if (user.getId() == null) {
			userMapper.insertUser(user);
			getApplicationEventPublisher().publishEvent(new UserCreatedEvent(this,user));
		} else {
			userMapper.updateUser(user);
		}
	}

	public void deleteUserById(int userId) {
		userMapper.deleteUserById(userId);
	}
}
