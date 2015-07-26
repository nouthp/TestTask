package my.home.testtask.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import my.home.testtask.dao.impl.UserDaoImpl;
import my.home.testtask.entity.User;

public class MockUserDao extends UserDaoImpl {
	private List<User> users;
	private AtomicLong sequence = new AtomicLong(5);

	public MockUserDao() {
		super(null);
		users = getTestUsers();
	}

	@Override
	public List<User> getAll() throws SQLException {
		return users;
	}
	
	@Override
	public User getById(Long id) throws SQLException {
		for (User user : users) {
			if (user.getId() == id) {
				return user;
			}
		}
		return null;
	}
	
	@Override
	public void create(User user) throws Exception {
		user.setId(sequence.getAndIncrement());
		users.add(user);
	}
	
	@Override
	public void update(User userNew) throws Exception {
		for (int i = 0; i<users.size(); i++) {
			if (users.get(i).getId() == userNew.getId()) {
				users.set(i, userNew);
				return;
			}
		}
	}
	
	@Override
	public List<User> getAllChildren(Long parentId) {
		List<User> childrenUsers = new ArrayList<User>();
		for (User user : users) {
			if (user.getParentId() == parentId) {
				childrenUsers.add(user);
			}
		}
		return childrenUsers;
	}

	private List<User> getTestUsers() {
		User userRoot = new User();
		userRoot.setId(1L);
		userRoot.setLogin("root");
		userRoot.setPassword("rootPass");
		userRoot.setName("Root user");
		userRoot.setPhone("466-25-98");

		User userChild_1 = new User();
		userChild_1.setId(2L);
		userChild_1.setParentId(userRoot.getId());
		userChild_1.setLogin("user_1");
		userChild_1.setPassword("pass");
		userChild_1.setName("User One");
		userChild_1.setPhone("415-45-98");

		User userChild_2 = new User();
		userChild_2.setId(3L);
		userChild_2.setParentId(userRoot.getId());
		userChild_2.setLogin("user_2");
		userChild_2.setPassword("pass");
		userChild_2.setName("User Two");
		userChild_2.setPhone("447-66-87");

		User userOther = new User();
		userOther.setId(4L);
		userOther.setLogin("user");
		userOther.setPassword("pass");
		userOther.setName("User Other");
		userOther.setPhone("325-96-54");

		List<User> users = new ArrayList<User>();
		users.add(userRoot);
		users.add(userChild_1);
		users.add(userChild_2);
		users.add(userOther);

		return users;
	}

}
