/*
 * $Id: LoginException.java,v 1.3 2004/03/27 11:17:05 jdt Exp $
 * Created on Mar 23, 2004 by jdt Copyright (C) AstroGrid. All rights reserved.
 * 
 * This software is published under the terms of the AstroGrid Software License
 * version 1.2, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.astrogrid.portal.cocoon.common;

/**
 * Exception thrown by LoginAction
 * @author jdt
 */
public class LoginException extends Exception {
    /**
     * Constructor
     * 
     */
    public LoginException() {
        super();
    }
    
    /**
     * Constructor
     * @param arg0 description
     */
    public LoginException(final String arg0) {
        super(arg0);
    }

    /**
     * Constructor
     * @param arg0 description
     * @param arg1 cause
     */
    public LoginException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Constructor
     * @param arg0 cause
     */
    public LoginException(final Throwable arg0) {
        super(arg0);
    }

}


/*
 *  $Log: LoginException.java,v $
 *  Revision 1.3  2004/03/27 11:17:05  jdt
 *  Checkstyle
 *
 *  Revision 1.2  2004/03/24 18:31:33  jdt
 *  Merge from PLGN_JDT_bz#201
 *
 *
 */