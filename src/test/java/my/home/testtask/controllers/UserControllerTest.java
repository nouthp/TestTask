package my.home.testtask.controllers;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;

import my.home.testtask.dao.MockUserDao;
import my.home.testtask.dao.UserDao;
import my.home.testtask.entity.User;

import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

public class UserControllerTest {

	private UserController userController;
	
	public UserControllerTest() throws Exception {
		userController = new UserController();
		Field userDaoField = UserController.class.getDeclaredField("userDao");
		userDaoField.setAccessible(true);
		UserDao userDao = new MockUserDao();
		userDaoField.set(userController, userDao);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testGetUsersList() throws Exception {
		ModelAndView model = userController.getUsersList();
		List<User> users = (List<User>) model.getModel().get("users");
		assertTrue(users.size() == 2);
		for (User user : users) {
			assertTrue(user.getParentId() == 1L);
		}
	}
	
	@Test
	public void testEditUser() throws SQLException {
		ModelAndView model = userController.editUser(2L);
		User user = (User) model.getModel().get("user");
		assertTrue(user.getId() == 2L);
		assertTrue(user.getParentId() == 1L);
		
		ModelAndView model_2 = userController.editUser(null);
		User user_2 = (User) model_2.getModel().get("user");
		assertTrue(user_2.getId() == 1L);
		assertTrue(user_2.getParentId() == null);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testSaveUser() throws Exception {
		User user = new User();
		user.setParentId(1L);
		user.setLogin("login test");
		user.setPassword("password test");
		user.setName("name test");
		
		assertTrue(userController.saveUser(user).equals("index"));
		
		ModelAndView model = userController.getUsersList();
		List<User> newUsers = (List<User>) model.getModel().get("users");
		for (User newUser : newUsers) {
			if (newUser.getId() > 4L) {				
				newUsers.remove(newUser);
				assertTrue(true);
				return;
			}
		}
		assertTrue(false);
	}
	
	@Test
	public void testCreateUser() throws Exception {
		ModelAndView modelAndView = userController.createUser();
		User modelsUser = (User) modelAndView.getModel().get("user");
		assertTrue(modelsUser.getId() == null);
		assertTrue(modelsUser.getParentId() == 1L);
	}
	
}
