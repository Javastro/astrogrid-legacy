/* $Id: OptionalTestCase3Test.java,v 1.1 2004/01/26 15:53:20 jdt Exp $
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
* Try to disable this test using system properties
 */
public class OptionalTestCase3Test extends OptionalTestCase {
  {
    System.setProperty(
      "optional.test.skip.org.astrogrid.test.OptionalTestCase3Test",
      "true");
  }
  /**
   * Constructor for OptionalTestCase3Test.
   * @param arg0 test name
   */
  public OptionalTestCase3Test(final String arg0) {
    super(arg0);
  }

  /**
   * Fire up the text ui
   * @param args ignored
   */
  public static void main(final String[] args) {
    junit.textui.TestRunner.run(OptionalTestCase3Test.class);
  }
  /**
  * JUnit test Disabled
  * Written by jdt, 26-Jan-2004
  */
  public final void testDisabled() {
    fail("This test should have been disabled");
  }

}

/*
*$Log: OptionalTestCase3Test.java,v $
*Revision 1.1  2004/01/26 15:53:20  jdt
*New tests
*
*/