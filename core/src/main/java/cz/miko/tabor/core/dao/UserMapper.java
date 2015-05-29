package cz.miko.tabor.core.dao;

import cz.miko.tabor.core.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public interface UserMapper {

	List<User> getUsers();

	void insertUser(User user);

	void updateUser(User user);

	void deleteUserById(@Param("id")int userId);
}
