package exceptions;

public class PasswordsDontMatch extends Exception {
	private static final long serialVersionUID = 1L;

	public PasswordsDontMatch() {super();}

	public PasswordsDontMatch(String s) {super(s);}
}
