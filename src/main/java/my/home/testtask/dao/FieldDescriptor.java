package my.home.testtask.dao;

public class FieldDescriptor {

	private final String fieldName;
	private final String propertyName;
	private final String type;
	private final boolean isKey;

	public FieldDescriptor(String fieldName, String propertyName, String type, boolean isKey) {
		super();
		this.fieldName = fieldName;
		this.propertyName = propertyName;
		this.type = type;
		this.isKey = isKey;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public String getType() {
		return type;
	}

	public boolean isKey() {
		return isKey;
	}
}
