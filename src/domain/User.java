package domain;

public abstract class User extends DomainObject {

	private static final long serialVersionUID = 1L;
	private String name;

	public User(long id, String name) {
		super(id);
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}