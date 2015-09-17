package exceptions;

public class NoDigestAlgorithm extends Exception {

    private static final long serialVersionUID = 1L;

    public NoDigestAlgorithm() {
        super();
    }

    /**
     * This exception is triggered when the digest algorithm required to
     * authenticate the user is not implemented in the java library
     *
     * @param String
     * @return None
     */
    public NoDigestAlgorithm(String s) {
        super(s);
    }
}