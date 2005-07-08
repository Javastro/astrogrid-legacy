/*
 * 
 * This software is published under the terms of the AstroGrid Software License
 * version 1.2, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.astrogrid.portal.home.acting;

/**
 * Exception thrown by ChangePasswordAction
 */
public class ChangePasswordException extends Exception {
    /**
     * Constructor
     * 
     */
    public ChangePasswordException() {
        super();
    }
    
    /**
     * Constructor
     * @param arg0 description
     */
    public ChangePasswordException(final String arg0) {
        super(arg0);
    }

    /**
     * Constructor
     * @param arg0 description
     * @param arg1 cause
     */
    public ChangePasswordException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Constructor
     * @param arg0 cause
     */
    public ChangePasswordException(final Throwable arg0) {
        super(arg0);
    }

}