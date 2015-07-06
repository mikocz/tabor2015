package cz.miko.tabor.core.dao;

import cz.miko.tabor.core.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public interface UserMapper {

	List<User> getUsers(Map<String,Object> params);

	void insertUser(User user);

	void updateUser(User user);

	void deleteUserById(@Param("id")int userId);
}
