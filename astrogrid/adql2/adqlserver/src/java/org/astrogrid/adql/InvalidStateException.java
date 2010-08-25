/**
 * 
 */
package org.astrogrid.adql;

public class InvalidStateException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7350174430511056737L;

	/**
     * @param message
     * @param cause
     */
    public InvalidStateException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public InvalidStateException(String message) {
        super(message);
    }

}