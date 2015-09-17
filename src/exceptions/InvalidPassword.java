package exceptions;

public class InvalidPassword extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidPassword() {super();}

	public InvalidPassword(String s) {super(s);}
}
