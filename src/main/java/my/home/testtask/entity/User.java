package my.home.testtask.entity;

public class User implements Entity {
	
	private Long id;
	private Long parentId;
	private String login;
	private String password;
	private String name;
	private String phone;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("id: ");
		builder.append(id);
		builder.append("; parentId: ");
		builder.append(parentId);
		builder.append("; login: ");
		builder.append(login);
		builder.append("; password: ");
		builder.append(password);
		builder.append("; name: ");
		builder.append(name);
		builder.append("; phone: ");
		builder.append(phone);
		
		return builder.toString();
	}

}
