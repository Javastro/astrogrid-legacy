/* $Id: JESTest.java,v 1.7 2004/01/29 15:44:00 jdt Exp $
 * Created on 27-Oct-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.jes;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.AstroGridException;
import org.astrogrid.Configurator;

/**
 * Test that JES loads up the properties correctly
 * @author jdt
 *
 */
public class JESTest extends TestCase {

  /**
   * Constructor for JESTest.
   * @param arg0 Test name
   */
  public JESTest(final String arg0) {
    super(arg0);
  }
  /**
   * Launch the text ui
   * @param args ignored
   */
  public static void main(final String[] args) {
    junit.textui.TestRunner.run(JESTest.class);
  }

  /**
   * Load up a JES
   * This will throw an exception if the properties files aren't available, so
   * automatically tests that they're being picked up.
   * @see TestCase#setUp()
   */
  protected final void setUp() throws Exception {
    super.setUp();
  }

  /** 
   * clear up after a test
   * @see junit.framework.TestCase#tearDown()
   */
  protected final void tearDown() throws Exception {
    super.tearDown();
  }

  /**
  * Test that the singleton pattern has been implemented correctly - Don't 
  * know how to do this yet.
  * Current test is a bit silly - you'd have to really get it wrong to fail 
  * this.  What I really want is a test that fails when double-checked locking
  * is used: see http://www.javaworld.com/jw-02-2001/jw-0209-double.html 
  * @MINOR wonder why JES is a singleton since pretty much all the important
  * methods are static anyway.
   */
  public final void testSingleton() {
    JES jes = JES.getInstance();
    assertEquals("Should only ever be one JES", jes, JES.getInstance());
  }
  /**
   * Test value
   */
  private static final String EXPECTED_VERSION_VALUE = "1.2";
  /**
   * Try getting the property "VERSION"
   *
   */
  public final void testGetVersion() {
    String value = JES.getProperty("VERSION", "GENERAL");
    assertEquals(
      "Property VERSION should be " + EXPECTED_VERSION_VALUE,
      EXPECTED_VERSION_VALUE,
      value);
  }

  /**
   * When a requested property doesn't exist we expect the empty string back.
   *
   */
  public final void testPropertyNoExist() {
    String value = JES.getProperty("asdl;fj", "ald;adlsfj");
    assertEquals("Property value should be empty string", "", value);
  }

  /**
   * Make sure that the config files have loaded
   * @throws AstroGridException if the config file isn't loaded
   */

  public final void testCanWeFindTheTestConfigFiles()
    throws AstroGridException {
    JES jes = JES.getInstance();
    jes.checkPropertiesLoaded();
  }

  /**
   * We've been having problems with this particular property - check we can
   * find it:
   *
   */
  public final void testSubmitJobResponseTemplateProperty() {
    String response =
      JES.getProperty(
        JES.CONTROLLER_SUBMIT_JOB_RESPONSE_TEMPLATE,
        JES.CONTROLLER_CATEGORY);
    log.debug("Response is " + response);
    assertNotNull(
      "This property should be present in the config file",
      response);
  }
  /**
   * Log
   */
  private static Log log = LogFactory.getLog(JESTest.class);
  /**
   * The configurator has this bizarre API, where if the property you request
   * begins with  "TEMPLATE.", then instead of returning the property value,
   * it treats the property value as a file name and attempts to load it and 
   * return the contents.
   * This test verifies that should the file not exist then we get some decent
   * exception or null response.
   */
  public final void testNoTemplateFile() {
    provokeTemplatePropertyProblem("TEMPLATE.TEST1");
    //this property exists, but the file doesn't
  }

  /**
   * The configurator has this bizarre API, where if the property you request
   * begins with  "TEMPLATE.", then instead of returning the property value,
   * it treats the property value as a file name and attempts to load it and 
   * return the contents.
   * This test verifies that should the property not exist then we get some decent
   * exception or null response (currently you just get a string of garbage back)
   * @TODO enter this in bugzilla
   */
  public final void testNoTemplateFileProperty() {
    provokeTemplatePropertyProblem("TEMPLATE.TEST2");
    //this property does not exist
  }
  /**
   * See testNoTemplateFile and testNoTemplateFileProperty
   * @param property property (template filename) to look for
   */
  private void provokeTemplatePropertyProblem(final String property) {
    try {
      String response = JES.getProperty(property, JES.CONTROLLER_CATEGORY);

      assertNull(
        "The file does not exist, therefore a null response would be good",
        response);
      //but an exception would be better
    } catch (Exception e) {
      return; //An exception would be enough to pass this test
    }
  }

  /**
   *  Can we set a property in the configuration?
   */
  public final void testSetAProperty() {
    final String expect = "summit else";
    final String key = Configurator.GENERAL_VERSION_NUMBER;
    final String category = Configurator.GENERAL_CATEGORY;

    final String oldValue = JES.getProperty(key, category);

    JES.setProperty(key, category, expect);

    assertNotEqual(
      "Property hasn't been changed",
      oldValue,
      JES.getProperty(key, category));

    assertEquals(
      "Property didn't match",
      expect,
      JES.getProperty(key, category));
  }

  /**
   * A method I'd like to have in TestCase
   * @param msg error msg 
   * @param string1 to compare with...
   * @param string2 ...this
   */
  public static void assertNotEqual(
    final String msg,
    final String string1,
    final String string2) {
    assertFalse(msg, string1.equals(string2));
  }
}

/*
*$Log: JESTest.java,v $
*Revision 1.7  2004/01/29 15:44:00  jdt
*removed logging statement that was causing chaos by outputting all sorts of crap.
*
*Revision 1.6  2004/01/20 17:59:08  jdt
*reformat
*
*Revision 1.5  2004/01/20 17:58:28  jdt
*new test for new method
*
*Revision 1.4  2003/11/14 17:24:29  jdt
*A few new tests on TEMPLATE properties, and removed stuff trying to test
*what happens when there's no config file.  Too difficult for too little gain.
*
*Revision 1.3  2003/10/29 12:09:17  jdt
*Some minor tidying to satisfy the coding standards.
*
*Revision 1.2  2003/10/28 12:28:53  jdt
*Better way of putting the test config files in place, using the build.xml file.
*
*Revision 1.1  2003/10/27 18:47:52  jdt
*Initial commits: files to test the JES config stuff.
*
*/