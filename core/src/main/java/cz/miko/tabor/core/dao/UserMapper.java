package cz.miko.tabor.core.dao;

import cz.miko.tabor.core.model.User;

import java.util.List;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public interface UserMapper {

	List<User> getUsers();
}
