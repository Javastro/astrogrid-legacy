/* $Id: ConfiguratorTest.java,v 1.4 2004/01/20 17:23:45 jdt Exp $
 * Created on 11-Dec-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.TestCase;

import org.astrogrid.testutils.naming.SimpleContextFactoryBuilder;
/**
 * Test the Configurator class
 * @TODO Configurator seems a bit of a tangle of static and nonstatic methods - could do with a clean up.
 * @author jdt
 *
 */
public final class ConfiguratorTest extends TestCase {
  private static Log log = LogFactory.getLog(ConfiguratorTest.class);

  /**
   * Constructor for ConfiguratorTest.
   * @param arg0 testname
   */
  public ConfiguratorTest(final String arg0) {
    super(arg0);
  }

  /**
   * Fire up the junit test ui
   * @param args ignored
   */
  public static void main(final String[] args) {
    junit.textui.TestRunner.run(ConfiguratorTest.class);
  }

  /**
    * Set up the SimpleContextFactoryBuilder
    * @throws Exception 
    * @see TestCase#setUp()
    */
  protected final void setUp() throws Exception {
    super.setUp();
    if (!NamingManager.hasInitialContextFactoryBuilder()) {
      NamingManager.setInitialContextFactoryBuilder(
        new SimpleContextFactoryBuilder());
    }

  }
  /**
   * Check that the properties file can be loaded by DummyFileConfigurator
   * @throws AstroGridException if it can't be
   */
  public void testCheckPropertiesLoadedOK() throws AstroGridException {
    //should be hunky dory
    new DummyFileConfigurator().checkPropertiesLoaded();
  }

  /**
   * Check we get an exception if there is no config file
   */
  public void testCheckPropertiesLoadedNotOK() {
    try {
      new DummyNoFileConfigurator().checkPropertiesLoaded();
      fail("Expected an AstroGridException because there is no config file");
    } catch (AstroGridException age) {
      //expected
      return;
    }
  }

  /**
   *  Check the fileTestConfig.xml file to see what we're expecting to get here.
   * 
   */
  public void testGetAPropertyFromFileConfigurator() {
    final String expect = "asdf";
    final String key = "BAR";
    final String category = "FOO";
    Configurator configurator = new DummyFileConfigurator();
    assertEquals(
      "Property didn't match",
      expect,
      DummyFileConfigurator.getProperty(
        configurator.getSubsystemAcronym(),
        key,
        category));
  }

  /**
   *  Check the fileTestConfigNoMessages.xml 
   *  There's no messages property file, so an exception will be thrown,
   *  however, it gets caught and logged and execution just continues.  Not
   * sure if this is really what we want...
   * 
   */
  public void testGetAPropertyFromFileConfiguratorNoMessages() {
    log.trace("entering testGetAPropertyFromFileConfiguratorNoMessages ");
    final String expect = "fdsa";
    final String key = "BAR";
    final String category = "FOO";
    Configurator configurator = new DummyFileConfiguratorNoMessages();
    assertEquals(
      "Property didn't match",
      expect,
      DummyFileConfigurator.getProperty(
        configurator.getSubsystemAcronym(),
        key,
        category));
    log.trace("leaving testGetAPropertyFromFileConfiguratorNoMessages");
  }

  /**
   * Tests a configurator that loads its properties from a URL stored in JNDI
   * @throws NamingException if there's problems with the naming service
   * @throws IOException if the URL isn't working
   */
  public void testGetAPropertyFromURLConfigurator()
    throws NamingException, IOException, AstroGridException {
    final String expect = "asdf";
    final String key = "BAR";
    final String category = "FOO";

    // url for testing the DummyURLConfigurator
    URL url =
      new URL("http://wiki.astrogrid.org/pub/Main/JohnTaylor/urlTestConfig.xml");
    // check the URL exists OK    
    InputStream is = url.openStream();
    is.close();

    Configurator configurator = new DummyURLConfigurator();
    String jndiName = configurator.getJNDIName();

    Context ctx = NamingManager.getInitialContext(null);
    ctx.bind(jndiName, url.toString());
    assertEquals("Object bound OK", url.toString(), ctx.lookup(jndiName));

    configurator.checkPropertiesLoaded();

    assertEquals(
      "Property didn't match",
      expect,
      DummyURLConfigurator.getProperty(
        configurator.getSubsystemAcronym(),
        key,
        category));
  }

  /**
   * Tests a configurator which is using a bad URL.  We want an exception here
   * since by placing the URL in the naming service the user clearly wanted to use it.
   * @throws NamingException problem with the naming service
   * @throws MalformedURLException our test URL is malformed
   */
  public void testBadURL() throws NamingException, MalformedURLException {
    final String expect = "asdf";
    final String key = "BAR";
    final String category = "FOO";

    Configurator configurator = new DummyCrapURLConfigurator();
    String jndiName = configurator.getJNDIName();

    Context ctx = NamingManager.getInitialContext(null);
    URL crapURL = new URL("file://C:/bollox");
    try {
      crapURL.openStream().read();
      fail(
        "If this URL " + crapURL + " exists then this test ain't gonna work");
    } catch (IOException expected) {
      // expected 
    }

    ctx.bind(jndiName, crapURL.toString());
    assertEquals("Object bound OK", crapURL.toString(), ctx.lookup(jndiName));

    try {
      new DummyCrapURLConfigurator().checkPropertiesLoaded();
      fail("Expected an AstroGridException because there is no config file at the URL supplied");
    } catch (AstroGridException age) {
      //expected
      return;
    }
  }

  /**
   * If the configurator supplied a JNDI name for a URL, but that name doesn't
   * exist in the naming service, then it falls back to looking for a local file on the classpath.
   * This allows the user the choice of where to put the properties file.
   * @throws NamingException if there's a problem storing the URL in the naming service
   */
  public void testGetAPropertyFromURLConfiguratorButURLDontExistInJNDI()
    throws NamingException {
    final String expect = "asdf";
    final String key = "BAR";
    final String category = "FOO";

    Configurator configurator = new DummyCrapJNDINameConfigurator();
    String jndiName = configurator.getJNDIName();

    Context ctx = NamingManager.getInitialContext(null);
    try {
      System.out.println(ctx.lookup(jndiName));
      fail("Expect an exception because the name shouldn't be bound in the InitialContext");
    } catch (NamingException expected) {
      //OK
    }

    assertEquals(
      "Property didn't match",
      expect,
      DummyCrapJNDINameConfigurator.getProperty(
        configurator.getSubsystemAcronym(),
        key,
        category));
  }

  /**
   *  Can we set a property in the configuration?
   */
  public void testSetAProperty() {
    final String expect = "summit else";
    final String key = "BAR";
    final String category = "FOO";
    Configurator configurator = new DummyFileConfigurator();

    final String oldValue =
      Configurator.getProperty(
        configurator.getSubsystemAcronym(),
        key,
        category);

    Configurator.setProperty(
      configurator.getSubsystemAcronym(),
      key,
      category,
      expect);

    assertNotEqual(
      "Property hasn't been changed",
      oldValue,
      Configurator.getProperty(
        configurator.getSubsystemAcronym(),
        key,
        category));

    assertEquals(
      "Property didn't match",
      expect,
      DummyFileConfigurator.getProperty(
        configurator.getSubsystemAcronym(),
        key,
        category));
  }

  /**
   *  Get a template property - should return "" as no template file supplied
   */
  public void testGetATemplateProperty() {
    
    final String key = "TEMPLATE.BLURGH";
    final String category = "FOO";
    Configurator configurator = new DummyFileConfigurator();

    
    final String result =  Configurator.getProperty(
        configurator.getSubsystemAcronym(),
        key,
        category);
      
    assertEquals(result,"foo");  

  }
  
  /**
   *  Set a template property should result in a fail, since it's not currently supported.
   */
  public void testSetATemplateProperty() {
    final String expect = "summit else";
    final String key = "TEMPLATE.BAR";
    final String category = "FOO";
    Configurator configurator = new DummyFileConfigurator();

    try {
      Configurator.setProperty(
        configurator.getSubsystemAcronym(),
        key,
        category,
        expect);
      fail("Excepted an UnsupportedOperationException");
    } catch (UnsupportedOperationException uoe) {
      return; //expected 
    }
  }

  /**
   * Try to export the file
   * @throws AstroGridException on IO problems
   */
  public void testSave() throws AstroGridException {
    Configurator configurator = new DummyFileConfigurator();
    configurator.save();
    File file = new File("fileTestConfig.xml");
    assertTrue("saved file doesn't exist", file.exists());
    assertTrue("failed to delete file", file.delete());
  }

  /**
   *  Try saving to some stupid file name in the hope of provoking an exception
   */
  public void testFailedToSave() {
    Configurator configurator = new DummyFileConfigurator();
    String fileName = "***** &^£%$hope this is an impossible filename";
    File file = new File(fileName);
    try {
      configurator.save(fileName);
      file.delete();
      fail("Expected an exception");
    } catch (AstroGridException e) {
      return; //expected
    }
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

  /**
   *  Simple that the DummyFileConfigurator used in other tests initialises correctly.
   */
  public void testDummyFileConfigurator() {
    Configurator configurator = new DummyFileConfigurator();
    assertEquals("fileTestConfig.xml", configurator.getConfigFileName());
  }

}

/*
*$Log: ConfiguratorTest.java,v $
*Revision 1.4  2004/01/20 17:23:45  jdt
*Added unit tests for full clover coverage and fixed bugs.
*
*Revision 1.3  2004/01/06 16:24:11  jdt
*updated a broken URL
*
*Revision 1.2  2003/12/11 18:55:20  jdt
*Adapted the configurator to look for the properties file at a URL supplied in a
*JNDI lookup service.
*
*Revision 1.1  2003/12/11 18:19:10  jdt
*New files to test the modifications to the Configurator which allow it
*to load properties files from URLs
*
*/