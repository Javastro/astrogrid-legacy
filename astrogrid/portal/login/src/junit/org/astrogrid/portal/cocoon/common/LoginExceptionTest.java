/* $Id: LoginExceptionTest.java,v 1.3 2004/03/25 15:34:47 jdt Exp $
 * Created on Mar 25, 2004 by jdt
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.portal.cocoon.common;

import junit.framework.TestCase;

/**
 * Test for LoginException.
 * 
 * @author jdt
 */
public final class LoginExceptionTest extends TestCase {
    /**
     * Fire up the text ui
     * @param args ignored
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(LoginExceptionTest.class);
    }
    /**
     * Class to test for void LoginException()
     */
    public void testLoginException() {
        Exception e = new LoginException();
    }
    /**
     * Class to test for void LoginException(String)
     */
    public void testLoginExceptionString() {
        Exception e = new LoginException("NeilHamilton");
        assertEquals("NeilHamilton",e.getMessage());
    }
    /**
     * Class to test for void LoginException(String, Throwable)
     */
    public void testLoginExceptionStringThrowable() {
        Exception e1 = new LoginException("NeilHamilton");
        Exception e2 = new LoginException(e1);
        assertEquals("NeilHamilton",e1.getMessage());
        assertEquals(e1,e2.getCause());
    }
    /**
     * Class to test for void LoginException(Throwable)
     */
    public void testLoginExceptionThrowable() {
        Exception e1 = new LoginException("NeilHamilton");
        Exception e2 = new LoginException("JonathonAitken",e1);
        assertEquals("NeilHamilton",e1.getMessage());
        assertEquals("JonathonAitken",e2.getMessage());
        assertEquals(e1,e2.getCause());
    }
}


/*
 *  $Log: LoginExceptionTest.java,v $
 *  Revision 1.3  2004/03/25 15:34:47  jdt
 *  Some refactoring of the debugging and added unit tests.
 *
 *  Revision 1.1  2004/03/25 15:18:40  jdt
 *  Some refactoring of the debugging and added unit tests.
 *
 */