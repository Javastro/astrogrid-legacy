/* $Id: OptionalTestCaseTest.java,v 1.1 2004/01/26 15:53:20 jdt Exp $
 * Created on 26-Jan-2004 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.test;

/**
 * @author jdt
 *
 * Quick check of the new OptionalTestCase
 */
public class OptionalTestCaseTest extends OptionalTestCase {
  {
    OptionalTestCase.setTestDisabled(
      "org.astrogrid.test.OptionalTestCaseTest",
      true);
  }
  /**
   * Constructor for OptionalTestCaseTest.
   * @param arg0 test name
   */
  public OptionalTestCaseTest(final String arg0) {
    super(arg0);
  }

  /**
   * run the text ui
   * @param args ignored
   */
  public static void main(final String[] args) {
    junit.textui.TestRunner.run(OptionalTestCaseTest.class);
  }

  /**
   * JUnit test Disabled
   * Written by jdt, 26-Jan-2004
   */
  public final void testDisabled() {
    fail("this test should be disabled");

  }

}

/*
*$Log: OptionalTestCaseTest.java,v $
*Revision 1.1  2004/01/26 15:53:20  jdt
*New tests
*
*/