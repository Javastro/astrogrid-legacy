/* $Id: JesExceptionTest.java,v 1.1 2003/10/21 17:37:34 jdt Exp $
 * Created on 21-Oct-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.jes;

import junit.framework.TestCase;

/**
 * Tests the JesException class
 * @author jdt
 *
 */
public class JesExceptionTest extends TestCase {

  /**
   * Constructor for JesExceptionTest.
   * @param arg0 see superclass
   */
  public JesExceptionTest(final String arg0) {
    super(arg0);
  }

  /**
   * fire up the junit text ui
   * @param args ignored
   */
  public static void main(final String[] args) {
    junit.textui.TestRunner.run(JesExceptionTest.class);
  }

  /**
   * A common message we'll use in all the tests
   */
  private org.astrogrid.i18n.AstroGridMessage message;
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
    message = new org.astrogrid.i18n.AstroGridMessage("Test Message");
    exception = new Exception("daft exception");
  }
  /**
   * Test version 1 of the ctor
   *
   */
  public final void testConstructor1() {
    JesException jesException = new JesException(message);
    assertEquals(
      "Should get the same AstroGridMessage back it was created with",
      jesException.getAstroGridMessage(),
      message);
  }
  /**
   * Test version2 of the ctor
   *
   */
  public final void testConstructor2() {
    JesException jesException = new JesException(message, exception);
    assertEquals(
      "Should get the same AstroGridMessage back it was created with",
      jesException.getAstroGridMessage(),
      message);
    assertEquals(
      "Should get the same Exception back it was created with",
      jesException.getCause(),
      exception);
  }

}

/*
*$Log: JesExceptionTest.java,v $
*Revision 1.1  2003/10/21 17:37:34  jdt
*Some simple test classes to get the ball rolling....
*
*/