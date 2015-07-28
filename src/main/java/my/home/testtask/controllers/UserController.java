package my.home.testtask.controllers;

import java.sql.SQLException;
import java.util.List;

import my.home.testtask.dao.DaoFactory;
import my.home.testtask.dao.MockUserDao;
import my.home.testtask.dao.UserDao;
import my.home.testtask.dao.impl.UserDaoImpl;
import my.home.testtask.entity.User;
import my.home.testtask.exceptions.NoDBTestTaskException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
	private UserDao userDao;
	private String errorMsg;
	private long idCurrentUser = 1L; // Will be get from session after
										// authorization implement

	public UserController() {
		try {
			userDao = (UserDao) DaoFactory.getInstance().createEntity(UserDaoImpl.class);
		} catch (NoDBTestTaskException e) {
			userDao = null;
			errorMsg = e.getMessage();
		}
	}

	@RequestMapping("/users")
	public ModelAndView getUsersList() throws SQLException {
		if (userDao == null) {
			return new ModelAndView("redirect:error");
		}
		ModelAndView model = new ModelAndView("users");
		List<User> users = userDao.getAllChildren(idCurrentUser);
		model.addObject("users", users);
		return model;
	}

	@RequestMapping("/edit")
	public ModelAndView editUser(@RequestParam(value = "id", required = false) Long id) throws SQLException {
		if (userDao == null) {
			return new ModelAndView("redirect:error");
		}
		ModelAndView model = new ModelAndView("editUser");
		if (id == null) {
			id = idCurrentUser;
		}
		User user = userDao.getById(id);
		if (user == null) {
			user = new User();
			user.setId(id);
		}
		model.addObject("user", user);
		return model;
	}

	@RequestMapping("/save")
	public String saveUser(@ModelAttribute("user") User user) throws Exception {
		// Will be change after authorization implement
		if (user.getId() == null || userDao.getById(idCurrentUser) == null) {
			userDao.create(user);
		} else {
			userDao.update(user);
		}
		return "index";
	}

	@RequestMapping("/create")
	public ModelAndView createUser() throws Exception {
		ModelAndView model = new ModelAndView("editUser");
		User user = new User();
		user.setParentId(idCurrentUser);
		model.addObject("user", user);
		return model;
	}
	
	@RequestMapping("/test")
	public ModelAndView test() {
		userDao = new MockUserDao();
		return new ModelAndView("redirect:");
	}
	
	@RequestMapping("/error")
	public ModelAndView error() {
		if (userDao != null) {
			return new ModelAndView("redirect:");
		}
		ModelAndView errorModel = new ModelAndView("error");
		errorModel.addObject("errorMessage", errorMsg);
		return errorModel;
	}
	
}
