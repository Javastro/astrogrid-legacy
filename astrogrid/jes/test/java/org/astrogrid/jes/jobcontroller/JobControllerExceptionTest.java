/* $Id: JobControllerExceptionTest.java,v 1.2 2003/11/08 22:00:51 anoncvs Exp $
 * Created on 08-Nov-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.jes.jobcontroller;
import junit.framework.TestCase;
import org.astrogrid.i18n.AstroGridMessage;
import org.astrogrid.jes.JesException;
/**
 * Trivial tests to mave the Clover coverage numbers look good!
 * @author john
 *
 */
public class JobControllerExceptionTest extends TestCase {
    /**
     * Constructor for JobControllerExceptionTest.
     * @param arg0 Test name
     */
    public JobControllerExceptionTest(String arg0) {
        super(arg0);
    }
    /**
     * Launch the JUnit text UI
     * @param args ignored
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(JobControllerExceptionTest.class);
    }
    /**
     * Just check that the constructors wrap up the args properly.
     * What else can you do?
     */
    public final void testConstructor() {
        AstroGridMessage message = new AstroGridMessage("goodbye cruel world");
        Exception exception = new Exception("I take exception");
        JesException je1 = new JobControllerException(message);
        JesException je2 = new JobControllerException(message, exception);
        assertEquals(
            "wrapped message doesn't match",
            message,
            je1.getAstroGridMessage());
        assertEquals(
            "wrapped message doesn't match",
            message,
            je2.getAstroGridMessage());
        assertEquals(
            "wrapped exception doesn't match",
            exception,
            je2.getCause());
    }
}
/*
 * $Log: JobControllerExceptionTest.java,v $
 * Revision 1.2  2003/11/08 22:00:51  anoncvs
 * Annoying $Id$ tag fixed
 *
 * Revision 1.1  2003/11/08 21:59:38  anoncvs
 * Initial commit
 *
 */
