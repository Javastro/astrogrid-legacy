/* $Id: ConfiguratorTest.java,v 1.1 2003/12/11 18:19:10 jdt Exp $
 * Created on 11-Dec-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;

import junit.framework.TestCase;

import org.astrogrid.testutils.naming.SimpleContextFactoryBuilder;
/**
 * Test the Configurator class
 * @TODO Configurator seems a bit of a tangle of static and nonstatic methods - could do with a clean up.
 * @author jdt
 *
 */
public final class ConfiguratorTest extends TestCase {

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
   * Tests a configurator that loads its properties from a URL stored in JNDI
   * @throws NamingException if there's problems with the naming service
   * @throws IOException if the URL isn't working
   */
  public void testGetAPropertyFromURLConfigurator() throws NamingException, IOException   {
    final String expect = "asdf";
    final String key = "BAR";
    final String category = "FOO";
    
    // url for testing the DummyURLConfigurator
    URL url = new URL("http://astrogrid1.jb.man.ac.uk:8080/jdtTestResources/urlTestConfig.xml");
    // check the URL exists OK    
    InputStream is = url.openStream();
    is.close();
    
    Configurator configurator = new DummyURLConfigurator();
    String jndiName = configurator.getJNDIName();

    Context ctx = NamingManager.getInitialContext(null);
    ctx.bind(jndiName, url);
    assertEquals("Object bound OK", url, ctx.lookup(jndiName));
        
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
  public void testBadURL() throws NamingException, MalformedURLException   {
    final String expect = "asdf";
    final String key = "BAR";
    final String category = "FOO";
    
    Configurator configurator = new DummyCrapURLConfigurator();
    String jndiName = configurator.getJNDIName();

    Context ctx = NamingManager.getInitialContext(null);
    URL crapURL = new URL("file://C:/bollox");
    try {
      crapURL.openStream().read();
      fail("If this URL "+crapURL+" exists then this test ain't gonna work");
    } catch (IOException expected ) {
      // expected 
    }
    
    ctx.bind(jndiName, crapURL);
    assertEquals("Object bound OK", crapURL, ctx.lookup(jndiName));
        
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
  public void testGetAPropertyFromURLConfiguratorButURLDontExistInJNDI() throws NamingException   {
    final String expect = "asdf";
    final String key = "BAR";
    final String category = "FOO";
    
    Configurator configurator = new DummyCrapJNDINameConfigurator();
    String jndiName = configurator.getJNDIName();

    Context ctx = NamingManager.getInitialContext(null);
    try {
      System.out.println(ctx.lookup(jndiName));
      fail("Expect an exception because the name shouldn't be bound in the InitialContext");
    } catch(NamingException expected) {
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

}

/*
*$Log: ConfiguratorTest.java,v $
*Revision 1.1  2003/12/11 18:19:10  jdt
*New files to test the modifications to the Configurator which allow it
*to load properties files from URLs
*
*/