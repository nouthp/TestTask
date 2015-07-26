package my.home.testtask.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import my.home.testtask.entity.User;

public abstract class UserDao extends Dao<User> {

	protected UserDao(Connection connection) {
		super(connection, "users");
	}

	public abstract List<User> getAllChildren(Long parentId) throws SQLException;
	
}
