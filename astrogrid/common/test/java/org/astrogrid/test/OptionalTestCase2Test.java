/* $Id: OptionalTestCase2Test.java,v 1.1 2004/01/26 17:37:04 jdt Exp $
 * Created on 26-Jan-2004 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;

/**
 * @author jdt
 *
 * A few more tests of OptionTestCase
 */
public class OptionalTestCase2Test extends TestCase {
  /**
   * Commons-logging logger
   */
  private static org.apache.commons.logging.Log log =
    org.apache.commons.logging.LogFactory.getLog(OptionalTestCase2Test.class);

  /**
   * Constructor for OptionalTestCase2Test.
   * @param arg0 test name
   */
  public OptionalTestCase2Test(final String arg0) {
    super(arg0);
  }

  /**
   * fire up the text ui
   * @param args ignored
   */
  public static void main(final String[] args) {
    junit.textui.TestRunner.run(OptionalTestCase2Test.class);

    junit.textui.TestRunner.run(OptionalTestCase2Test.MyDisabledTest.class);
  }

  /**
  * JUnit test DisableBySystemProperties
  * Written by jdt, 26-Jan-2004
  */
  public final void testDisableBySystemProperties() {
    log.trace("testDisableBySystemProperties");
    System.setProperty(
      "optional.test.skip.org.astrogrid.test.OptionalTestCase2Test$MyDisabledTest2",
      "true");
    TestResult tr = new TestResult();
    OptionalTestCase test = new MyDisabledTest2();
    assertTrue(test.isDisabled());
    test.run(tr);
    assertEquals(0, tr.errorCount());
    assertEquals(0, tr.failureCount());

  }
  /**
  * JUnit test DisableByDefault
  * Written by jdt, 26-Jan-2004
  */
  public final void testDisableByDefault() {
    log.trace("testDisableByDefault");
    OptionalTestCase.setDisabledByDefault(true);
    TestResult tr = new TestResult();
    Test test = new MyDisabledTest();
    test.run(tr);
    assertEquals(0, tr.errorCount());
    assertEquals(0, tr.failureCount());
  }

  /**
   *  Just check that our test TestClass does indeed fail, if it's enabled
   *
   */
  public final void testDisabledClassNotDisabledByDefault() {
    log.trace("testDisabledClassThrowsAnException");
    TestResult tr = new TestResult();
    OptionalTestCase test = new MyDisabledTest();
    assertFalse(test.isDisabled());
    test.run(tr);
    assertEquals(0, tr.errorCount());
    assertEquals(1, tr.failureCount());
  }
  /** 
   * A member test class which should never get run
   * @author jdt
   */
  public static class MyDisabledTest extends OptionalTestCase {
    /**
     * ctor
     */
    public MyDisabledTest() {
      super();
    }

    /**
     * ctor 
     * @param arg0 test name
     */
    public MyDisabledTest(final String arg0) {
      super(arg0);
    }

    /**
    * JUnit test Disabled
    * This test will always fail
    * Written by jdt, 26-Jan-2004
    */
    public final void testDisabled() {
      fail("This shouldn't get run");
    }
  }

  /**
   * Just the same as MyDisabledTest.  Two identical classes required because once
   * I've disabled this test "by system properties", I can't remove the property from
   * system.
   * @author jdt
   *
   * To change the template for this generated type comment go to
   * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
   */
  public static class MyDisabledTest2 extends MyDisabledTest {

  }

  /** standard junit set up
   * @throws Exception from the superclass
   * @see junit.framework.TestCase#setUp()
   */
  protected final void setUp() throws Exception {
    super.setUp();
    //default behaviour unless we specify otherwise
    OptionalTestCase.setDisabledByDefault(false);
  }

}

/*
*$Log: OptionalTestCase2Test.java,v $
*Revision 1.1  2004/01/26 17:37:04  jdt
*New tests
*
*/