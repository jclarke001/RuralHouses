package exceptions;

public class RemoteDesyncException extends Exception {
	private static final long serialVersionUID = 1L;

	public RemoteDesyncException() {super();}

	public RemoteDesyncException(String s) {super(s);}
}
