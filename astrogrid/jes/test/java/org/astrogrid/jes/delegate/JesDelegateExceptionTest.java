/* $Id: JesDelegateExceptionTest.java,v 1.2 2003/12/19 11:29:42 jl99 Exp $
 * Created on 21-Oct-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.jes.delegate;

import junit.framework.TestCase;

/**
 * Tests the JesDelegateException class
 * @author jdt
 *
 */
public class JesDelegateExceptionTest extends TestCase {

  /**
   * Constructor for JesDelegateExceptionTest.
   * @param arg0 see superclass
   */
  public JesDelegateExceptionTest(final String arg0) {
    super(arg0);
  }

  /**
   * fire up the junit text ui
   * @param args ignored
   */
  public static void main(final String[] args) {
    junit.textui.TestRunner.run(JesDelegateExceptionTest.class);
  }

  /** 
   * A common root exception we'll use in all the tests
   */
  private Exception exception;
  /**
   * Initialise the test
   * @throws Exception see superclass
   */
  public final void setUp() throws Exception {
    super.setUp();
    exception = new Exception("daft exception");
  }
  /**
   * Test   the ctor
   *
   */
  public final void testConstructor() {
    JesDelegateException jesDelegateException =
      new JesDelegateException(exception);
//    assertEquals(
//      "Should get null since we didn't supply an AstroGridMessage",
//      jesDelegateException.getAstroGridMessage(),
//      null);
    assertEquals(
      "Should get the same exception back it was created with",
      jesDelegateException.getCause(),
      exception);
  }

}

/*
*$Log: JesDelegateExceptionTest.java,v $
*Revision 1.2  2003/12/19 11:29:42  jl99
*Removed test dependency on AstrogridMessage
*
*Revision 1.1  2003/10/21 17:37:34  jdt
*Some simple test classes to get the ball rolling....
*
*/