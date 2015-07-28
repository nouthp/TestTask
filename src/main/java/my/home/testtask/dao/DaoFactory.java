package my.home.testtask.dao;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import my.home.testtask.entity.Entity;
import my.home.testtask.exceptions.NoDBTestTaskException;

public class DaoFactory {

	private static DaoFactory instance;

	private static String user;
	private static String password;
	private static String url;
	private static String driver;
	private static Connection connection;

	private DaoFactory() throws NoDBTestTaskException {
		String configFileName = "config.properties";
		
		try {
			Properties prop = new Properties();
			prop.load(DaoFactory.class.getClassLoader().getResourceAsStream(configFileName));

			user = prop.getProperty("user");
			password = prop.getProperty("password");
			url = prop.getProperty("url");
			driver = prop.getProperty("driver");

			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			throw new NoDBTestTaskException("Can not get DB connection");
		} catch (ClassNotFoundException e) {
			throw new NoDBTestTaskException("Can not find DB driver");
		} catch (Exception e) {
			String configFilePath = DaoFactory.class.getClassLoader().getResource("/").toString() + configFileName;
			throw new NoDBTestTaskException("Can not find " + configFilePath);
		}

	}

	public Dao<? extends Entity> createEntity(Class<? extends Dao<? extends Entity>> classDao)
			throws NoDBTestTaskException {

		Dao<? extends Entity> entityInstance = null;

		try {
			Constructor<? extends Dao<? extends Entity>> constructor = classDao
					.getConstructor(java.sql.Connection.class);
			entityInstance = (Dao<? extends Entity>) constructor.newInstance(connection);
		} catch (Exception e) {
			throw new NoDBTestTaskException("Undefined class name " + classDao.getName());
		}

		return entityInstance;
	}

	synchronized public static DaoFactory getInstance() throws NoDBTestTaskException {
		if (instance != null) {
			return instance;
		}
		instance = new DaoFactory();

		return instance;
	}

}
