/*
 * $Id: LoginException.java,v 1.2 2004/03/24 18:31:33 jdt Exp $
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
    public LoginException(String arg0) {
        super(arg0);
    }

    /**
     * Constructor
     * @param arg0 description
     * @param arg1 cause
     */
    public LoginException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Constructor
     * @param arg0 cause
     */
    public LoginException(Throwable arg0) {
        super(arg0);
    }

}


/*
 *  $Log: LoginException.java,v $
 *  Revision 1.2  2004/03/24 18:31:33  jdt
 *  Merge from PLGN_JDT_bz#201
 *
 *  Revision 1.1.2.1  2004/03/23 16:47:01  jdt
 *  Substantial refactoring, especially in the way that logging
 *  is done and that messages are passed back.  Problems
 *  are now thrown as exceptions. Let's hope that there's a 
 *  graceful way of dealing with them!
 *
 */