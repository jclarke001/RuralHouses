package exceptions;

public class InvalidToken extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidToken() {
        super();
    }

    /**
     * This exception is triggered if there is an error in functions that use
     * tokens: like using a token not corresponding with a user, or using an
     * inexistent token.
     *
     * @param String
     * @return None
     */
    public InvalidToken(String s) {
        super(s);
    }
}