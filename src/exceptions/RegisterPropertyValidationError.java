package exceptions;

public class RegisterPropertyValidationError extends Exception {
	private static final long serialVersionUID = 1L;

	public RegisterPropertyValidationError() {
	    super();
	}
	/**This exception is triggered if there exists an overlapping offer
	 *@param String
	 *@return None
	 */
	public RegisterPropertyValidationError(String s) {
	    super(s);
	}
}
