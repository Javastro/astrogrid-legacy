/* $Id: MessengerExceptionTest.java,v 1.2 2004/03/24 18:31:33 jdt Exp $
 * Created on Mar 14, 2004 by jdt
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.portal.cocoon.messaging;

import junit.framework.TestCase;

/**
 * Trivial tests of exception. 
 * 
 * @author jdt
 */
public final class MessengerExceptionTest extends TestCase {
    /**
     * Run the text ui
     * @param args ingored
     */
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(MessengerExceptionTest.class);
    }
    /**
     * Class to test for void MessengerException()
     */
    public void testMessengerException() {
        new MessengerException();
    }
    /**
     * Class to test for void MessengerException(String)
     */
    public void testMessengerExceptionString() {
        final Exception ex = new MessengerException("fred");
        assertEquals(ex.getMessage(), "fred");
    }
    /**
     * Class to test for void MessengerException(Throwable)
     */
    public void testMessengerExceptionThrowable() {
        final Exception ex2 = new Exception();
        final Exception ex = new MessengerException(ex2);
        assertEquals(ex.getCause(), ex2);
    }
    /**
     * Class to test for void MessengerException(String, Throwable)
     */
    public void testMessengerExceptionStringThrowable() {
        final Exception ex2 = new Exception();
        final Exception ex = new MessengerException("fred",ex2);
        assertEquals(ex.getCause(), ex2);
        assertEquals(ex.getMessage(), "fred");
    }
}


/*
 *  $Log: MessengerExceptionTest.java,v $
 *  Revision 1.2  2004/03/24 18:31:33  jdt
 *  Merge from PLGN_JDT_bz#201
 *
 *  Revision 1.1.2.1  2004/03/23 01:03:50  jdt
 *  At last, some unit tests
 *
 *  Revision 1.1  2004/03/17 21:20:12  johndavidtaylor
 *  Copied across from incubation in beanpeeler
 *
 *  Revision 1.1  2004/03/14 18:49:14  johndavidtaylor
 *  corrected comments
 *
 */