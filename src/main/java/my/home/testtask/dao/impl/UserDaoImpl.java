package my.home.testtask.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import my.home.testtask.dao.FieldDescriptor;
import my.home.testtask.dao.UserDao;
import my.home.testtask.entity.User;

public class UserDaoImpl extends UserDao {

	public UserDaoImpl(Connection connection) {
		super(connection);
		initFields(getTableFields());
	}

	@Override
	public List<User> getAllChildren(Long parentId) throws SQLException {
		String[] values = {parentId.toString()};
		ResultSet rs = getResultSet("WHERE parent_id = ?", values);
		return getList(rs);
	}

	@Override
	protected List<User> getList(ResultSet rs) throws SQLException {
		List<User> users = new ArrayList<User>();

		while (rs.next()) {
			User user = new User();

			user.setId(rs.getLong("id"));
			user.setParentId(rs.getLong("parent_id"));
			user.setLogin(rs.getString("login"));
			user.setPassword(rs.getString("password"));
			user.setName(rs.getString("name"));
			user.setPhone(rs.getString("phone"));

			users.add(user);
		}

		return users;
	}

	@Override
	protected List<FieldDescriptor> getTableFields() {
		List<FieldDescriptor> fields = new ArrayList<FieldDescriptor>();

		fields.add(new FieldDescriptor("id", "id", "BIGINT", true));
		fields.add(new FieldDescriptor("parent_id", "parentId", "BIGINT", false));
		fields.add(new FieldDescriptor("login", "login", "VARCHAR", false));
		fields.add(new FieldDescriptor("password", "password", "VARCHAR", false));
		fields.add(new FieldDescriptor("name", "name", "VARCHAR", false));
		fields.add(new FieldDescriptor("phone", "phone", "VARCHAR", false));
		
		return fields;
	}
	
}
