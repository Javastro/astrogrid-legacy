package org.eurovotech.quaestor;

public class QuaestorException
        extends Exception {

    // The following is required, in order to suppress a -Xlint warning, since this
    // class extends HttpServlet.  Since I don't serialise anything, I don't think this matters,
    // but I might as well consistently keep this as a number based on the CVS revision number:
    // major*1000+minor, when I remember.
    private static final long serialVersionUID = 1002L;

    /** 
     * Constructs a new exception with null as its detail message.
     */
    public QuaestorException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     */
    public QuaestorException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     */
    public QuaestorException(String message, Throwable cause) {
        super(message, cause);
    }
}
