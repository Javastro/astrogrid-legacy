package org.eurovotech.quaestor;

public class QuaestorException
        extends Exception {

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
