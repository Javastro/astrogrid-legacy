/* $Id: JESTest.java,v 1.1 2003/10/27 18:47:52 jdt Exp $
 * Created on 27-Oct-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.jes;

import java.io.File;
import java.io.IOException;

import org.astrogrid.AstroGridException;

import junit.framework.TestCase;

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

  private JES jes;
  /**
   * Load up a JES
   * This will throw an exception if the properties files aren't available, so
   * automatically tests that they're being picked up.
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
    moveFile(CONFIG_FILE_1_FM, CONFIG_FILE_1_TO);
    moveFile(CONFIG_FILE_2_FM, CONFIG_FILE_2_TO);
    jes = JES.getInstance();

  }

  // These config files sit in the test directory and need to be moved to the
  // classpath for Maven.  Not very satisfactory approach this.
  private final static String FILE1 = "ASTROGRID_jesconfig.xml";
  private final static String FILE2 = "ASTROGRID_jesmessages.properties";
  private final static String DIR_FM = "test//java";
  private final static String DIR_TO = "target//test-classes";
  //reckon the compile will inline these Strings and do something clever with 
  //StringBuffers
  private final static File CONFIG_FILE_1_FM = new File(DIR_FM + "//" + FILE1);
  private final static File CONFIG_FILE_1_TO = new File(DIR_TO + "//" + FILE1);
  private final static File CONFIG_FILE_2_FM = new File(DIR_FM + "//" + FILE2);
  private final static File CONFIG_FILE_2_TO = new File(DIR_TO + "//" + FILE2);

  private void moveFile(File from, File to) throws IOException {
    boolean isFileMoved = from.renameTo(to);
    if (!isFileMoved)
      throw new IOException("Unable to move test config file");
  }

  protected void tearDown() throws Exception {
    super.tearDown();
    moveFile(CONFIG_FILE_1_TO, CONFIG_FILE_1_FM);
    moveFile(CONFIG_FILE_2_TO, CONFIG_FILE_2_FM);
  }

  /**
  * Test that the singleton pattern has been implemented correctly - Don't 
  * know how to do this yet.
  * Current test is a bit silly - you'd have to really get it wrong to fail 
  * this.  What I really want is a test that fails when double-checked locking
  * is used: see http://www.javaworld.com/jw-02-2001/jw-0209-double.html 
  * @TODO wonder why JES is a singleton since pretty much all the important
  * methods are static anyway.
   */
  public void testSingleton() {
    assertEquals("Should only ever be one JES", jes, JES.getInstance());
  }

  /**
   * Try getting the property "VERSION"
   *
   */
  public void testGetVersion() {
    String value = JES.getProperty("VERSION", "GENERAL");
    assertEquals("Property VERSION should be 1.2", "1.2", value);
  }

  public void testPropertyNoExist() {
    String value = JES.getProperty("asdl;fj", "ald;adlsfj");
    assertEquals("Property value should be empty string", "", value);
  }


  private final static File TEMP = new File("temp");
  /**
   * Make sure that things fall over properly if the config files aren't there.
   * Currently test won't work, because once the properties are loaded, that's
   * it - need to see if we can force a reload.
   */
  public void testNoConfigFile() throws IOException  {
    //testMissingConfigFile(CONFIG_FILE_1_TO);
  }
  private void tryMissingConfigFile(final File file) throws IOException {
    moveFile(file, TEMP);
    try {
      jes.checkPropertiesLoaded();
      fail("An AstrogGridException should have been thrown due to a missing config file");
    } catch (AstroGridException aex) {
      //expect this to happen
    } finally {
      moveFile(TEMP, file);
    }
  }

  public void testCanWeFindTheTestConfigFiles() throws IOException, AstroGridException {
    assertTrue("can't find the test config file", CONFIG_FILE_1_TO.exists());
    assertTrue("can't find the test config file", CONFIG_FILE_2_TO.exists());
    //and again
    jes.checkPropertiesLoaded();
  }
}

/*
*$Log: JESTest.java,v $
*Revision 1.1  2003/10/27 18:47:52  jdt
*Initial commits: files to test the JES config stuff.
*
*/