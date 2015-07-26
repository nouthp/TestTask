package my.home.testtask.dao;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import my.home.testtask.entity.Entity;


public class DaoFactory {

	private static DaoFactory instance;

	private static String user;
	private static String password;
	private static String url;
	private static String driver;
	private static Connection connection;

	private DaoFactory() throws Exception {
		try {

			Properties prop = new Properties();
			prop.load(DaoFactory.class.getClassLoader().getResourceAsStream("config.properties"));

			user = prop.getProperty("user");
			password = prop.getProperty("password");
			url = prop.getProperty("url");
			driver = prop.getProperty("driver");

			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			throw new Exception("Can't create dao factory instance");
		}
	}

	public Dao<? extends Entity> createEntity(Class<? extends Dao<? extends Entity>> classDao) throws Exception {

		Dao<? extends Entity> entityInstance = null;
		
		try {
			Constructor<? extends Dao<? extends Entity>> constructor = classDao.getConstructor(java.sql.Connection.class);
			entityInstance = (Dao<? extends Entity>) constructor.newInstance(connection);
		} catch (Exception e) {
			throw new Exception("Undefined class name " + classDao.getName());
		}

		return entityInstance;
	}

	synchronized public static DaoFactory getInstance() throws Exception {
		if (instance != null) {
			return instance;
		}

		try {
			instance = new DaoFactory();
		} catch (SQLException e) {
			throw new Exception("Undefined class driver connection " + driver);
		}

		return instance;
	}

}
