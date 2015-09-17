package domain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import exceptions.NoDigestAlgorithm;

public class Account extends DomainObject {

	private static final long serialVersionUID = 1L;
	private User user;
	private String username;
	private UserType type;
	byte salt[] = new byte[20];
	byte hash[] = new byte[32];

	public Account(long id, User user, String username, String password,
			UserType type) throws NoDigestAlgorithm {
		super(id);
		this.user = user;
		this.username = username;
		this.type = type;
		changePassword(password);
	}

	public void changePassword(String password) throws NoDigestAlgorithm {
		SecureRandom random = new SecureRandom();
		random.nextBytes(this.salt);

		if (password == null)
			password = "";

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(this.salt);
			md.update(password.getBytes());
			this.hash = md.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new NoDigestAlgorithm("SHA-256 is not implemented");
		}
	}

	public boolean checkPassword(String password) throws NoDigestAlgorithm {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(this.salt);
			md.update(password.getBytes());
			byte result[] = md.digest();
			for (int i = 0; i < this.hash.length; i++)
				if (this.hash[i] != result[i])
					return false;
			return true;

		} catch (NoSuchAlgorithmException e) {
			throw new NoDigestAlgorithm("SHA-256 is not implemented");
		}
	}

	public static List<Byte> generateToken() {
		byte token[] = new byte[64];
		SecureRandom random = new SecureRandom();
		random.nextBytes(token);

		List<Byte> list = new ArrayList<Byte>();
		for (byte b : token)
			list.add(b);
		return list;
	}

	public User getUser() {
		return user;
	}

	public String getUsername() {
		return username;
	}

	public UserType getType() {
		return type;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isCustomer() {
		return type == UserType.CUSTOMER;
	}

	public boolean isOwner() {
		return type == UserType.OWNER;
	}

	public boolean isAdmin() {
		return type == UserType.ADMINISTRATOR;
	}

	public String toString() {
		return this.username + this.user.toString();
	}
}
