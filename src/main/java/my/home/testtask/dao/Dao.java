package my.home.testtask.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import my.home.testtask.entity.Entity;


public abstract class Dao<E extends Entity> {

	private final String tableName;
	private List<FieldDescriptor> fields;
	
	protected final Connection connection;

	protected Dao(Connection connection, String tableName) {
		this.connection = connection;
		this.tableName = tableName;
	}

	protected abstract List<E> getList(ResultSet rs) throws SQLException;
	protected abstract List<FieldDescriptor> getTableFields();
	
	protected void initFields(List<FieldDescriptor> fields) {
		this.fields = fields;
	}
	
	public List<E> getAll() throws SQLException {
		ResultSet rs = getResultSet();
		return getList(rs);
	}
	
	public E getById(Long id) throws SQLException {
		String[] values = {"" + id};
		ResultSet rs = getResultSet("WHERE id = ?", values);

		return getEntity(rs);
	}
	
	public void create(E entity) throws Exception {
		String sql = getInsertQuery();
		PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		
		insertValueMapper(preparedStatement, entity);
		preparedStatement.executeUpdate();
		
		ResultSet rs = preparedStatement.getGeneratedKeys(); 
		rs.next();
		entity.setId(rs.getLong(1));
	}
	
	public void update(E entity) throws Exception {
		String sql = getUpdateQuery();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		
		updateValueMapper(preparedStatement, entity);
		preparedStatement.executeUpdate();
	}

	protected String getSelectQuery(String condition) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT ");
		sqlBuilder.append(getCommaSeparatedFields());
		sqlBuilder.append(" FROM ");
		sqlBuilder.append(tableName);
		
		if (condition != null) {
			sqlBuilder.append(" ");
			sqlBuilder.append(condition);
		}
		sqlBuilder.append(";");
			
		return sqlBuilder.toString();
	}
	
	protected String getInsertQuery() {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("INSERT INTO ");
		sqlBuilder.append(tableName);
		sqlBuilder.append(" (");
		sqlBuilder.append(getCommaSeparatedFields());
		sqlBuilder.append(") VALUES (?");
		for (int i = 0; i < fields.size() - 1; i++) {
			sqlBuilder.append(", ?");
		}
		sqlBuilder.append(");");
		return sqlBuilder.toString();
	}
	
	protected String getUpdateQuery() {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("UPDATE ");
		sqlBuilder.append(tableName);
		sqlBuilder.append(" SET ");
		sqlBuilder.append(getQuestionmarkSeparatedFields());
		sqlBuilder.append(" WHERE id = ?");
		return sqlBuilder.toString();
	}
	
	protected ResultSet getResultSet() throws SQLException {
		String[] values = {};
		return getResultSet("", values);
	}
	
	protected ResultSet getResultSet(String condition, String[] values) throws SQLException {
		String sql = getSelectQuery(condition);
		PreparedStatement statement = connection.prepareStatement(sql);
		
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				statement.setString(i + 1, values[i]);
			}
		}
		
		return statement.executeQuery();
	}
	
	protected E getEntity(ResultSet rs) throws SQLException {
		List<E> entities = getList(rs);
		if (entities.size() == 1) {
			return entities.get(0);
		}
		if (entities.size() > 1) {
			throw new SQLException("There are more than one entity");
		}
		return null;
	}
	
	protected String getSeparatedFields(boolean includeKey, String suffix) {
		boolean first = true;
		StringBuilder result = new StringBuilder();
		for (FieldDescriptor fieldDescriptor : fields) {
			if (!includeKey && fieldDescriptor.isKey()) {
				continue;
			}
			if (first) {
				first = false;
			} else {
				result.append(", ");
			}
			result.append(tableName);
			result.append(".");
			result.append(fieldDescriptor.getFieldName());
			result.append(suffix);
		}
		return result.toString();
	}
	
	private void insertValueMapper(PreparedStatement statement, Entity entity) throws Exception {
		valueMapper(statement, entity, true);
	}
	
	private void updateValueMapper(PreparedStatement statement, Entity entity) throws Exception {
		valueMapper(statement, entity, false);
	}
	
	private void valueMapper(PreparedStatement statement, Entity entity, boolean includeKey) throws Exception {
		Class<? extends Entity> entityClass = entity.getClass();
		int shift = 1;
		
		for (int i = 0; i < fields.size(); i++) {

			Field fild = entityClass.getDeclaredField(fields.get(i).getPropertyName());
			fild.setAccessible(true);
			Object obj = fild.get(entity);
			if (fields.get(i).isKey() && !includeKey) {
				statement.setObject(fields.size(), obj);
				shift = 0;
			} else {
				switch (fields.get(i).getType()) {
				case "ENUM":
					statement.setString(i + shift, obj.toString());
					break;
				case "BOOLEAN":
					statement.setObject(i + shift, new Boolean(obj.toString()) ? 1 : 0);
					break;
				default:
					statement.setObject(i + shift, obj);
				}
			}
		}
	}
	
	private String getCommaSeparatedFields() {
		return getSeparatedFields(true, "");
	}
	
	private String getQuestionmarkSeparatedFields() {
		return getSeparatedFields(false, " = ?");
	}
	
}
