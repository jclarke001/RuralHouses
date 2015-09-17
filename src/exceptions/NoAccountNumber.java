package exceptions;

public class NoAccountNumber extends Exception {

	private static final long serialVersionUID = 1L;

	public NoAccountNumber() {super();}

	public NoAccountNumber(String s) {super(s);}
}
