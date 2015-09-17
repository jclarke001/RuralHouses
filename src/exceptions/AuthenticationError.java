package exceptions;

public class AuthenticationError extends Exception {
    private static final long serialVersionUID = 1L;

    public AuthenticationError() {
        super();
    }

    /**
     * This exception is triggered if there is an error in the authentication
     * process; like an incorrect username or password
     *
     * @param String
     * @return None
     */
    public AuthenticationError(String s) {
        super(s);
    }
}