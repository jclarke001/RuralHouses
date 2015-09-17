package exceptions;

public class CorruptedDatabase extends Exception {

	private static final long serialVersionUID = 1L;

	public CorruptedDatabase() {super();}

	public CorruptedDatabase(String s) {super(s);}
}
