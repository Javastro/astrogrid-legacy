/* $Id: ConfFileLocatedTest.java,v 1.4 2004/01/04 18:34:46 jdt Exp $
 * Created on 28-Dec-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.mySpace.installationTests;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.AstroGridException;
import org.astrogrid.mySpace.mySpaceManager.MMC;
import org.astrogrid.mySpace.mySpaceServer.MSC;

import junit.framework.TestCase;

/**
 * This test ensures that MySpace can find its configuration file
 * @author john taylor
 * @TODO do we require tests to located the template files?
 *
 */
public class ConfFileLocatedTest extends TestCase {
  /** Logger */
  private static Log log = LogFactory.getLog(ConfFileLocatedTest.class);
  /**
   * Constructor for ConfFileLocatedTest.
   * @param arg0 test name
   */
  public ConfFileLocatedTest(final String arg0) {
    super(arg0);
  }
  /**
   * Fire up the text ui.
   * @param args ignored
   */
  public static void main(final String[] args) {
    junit.textui.TestRunner.run(ConfFileLocatedTest.class);
  }

  /** 
   * There's one file for MySpaceManager
   * @throws AstroGridException if trouble finding config file
   */
  public final void testGotMySpaceManagerConfig() throws AstroGridException {
    MMC.getInstance().checkPropertiesLoaded();
  }

  /** 
   * There's one file for MySpaceServer
   * @throws AstroGridException if trouble finding config file
   */

  public final void testGotMySpaceServerConfig() throws AstroGridException {
    MSC.getInstance().checkPropertiesLoaded();
  }

  /**
   * The *.properties files should be on the classpath
   * @throws IOException if there's trouble
   */
  public final void testGotPropertiesFiles() throws IOException {
    checkProperties(MMC.getProperty("INSTALLATION.BASENAME", "MESSAGES"));
    checkProperties(MSC.getProperty("INSTALLATION.BASENAME", "MESSAGES"));
  }
  
  /**
   * Look for the messages properties file
   * @param baseName the properties file will be \<baseName>.properties
   * @throws IOException if there's trouble
   */
  private void checkProperties(final String baseName) throws IOException {
    log.debug("Checking properties file: "+baseName);
    Properties messages = new Properties();
    final String messageFile = "/" + baseName + ".properties";
    InputStream is = this.getClass().getResourceAsStream(messageFile);
    assertNotNull(messageFile + " file not found", is);
    messages.load(is);
  }
}
