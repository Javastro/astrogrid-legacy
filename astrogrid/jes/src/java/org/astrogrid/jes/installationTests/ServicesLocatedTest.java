/* $Id: ServicesLocatedTest.java,v 1.1 2004/01/21 17:51:34 jdt Exp $
 * Created on 21-Jan-2004 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.jes.installationTests;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.jes.JES;

/**
 * @author jdt
 *
 * Check that the web services defined in the config file exist
 */
public class ServicesLocatedTest extends TestCase {
  /**
   * Log
   */
  private static Log log = LogFactory.getLog(ServicesLocatedTest.class);
  /**
   * Constructor for ServicesLocatedTest.
   * @param arg0 test name
   */
  public ServicesLocatedTest(final String arg0) {
    super(arg0);
  }

  /**
   * Fire up the text ui
   * @param args ignored
   */
  public static void main(final String[] args) {
    junit.textui.TestRunner.run(ServicesLocatedTest.class);
  }

  /**
   *  Check that the Monitor is a valid URL.  Note that the method
   * does not check that it is a valid webservice, just that it is a live URL
   */
  public final void testMonitorURL() {
    checkService(JES.MONITOR_URL, JES.MONITOR_CATEGORY);
  }

  /**
   *  Check that the Controller is a valid URL.  Note that the method
   * does not check that it is a valid webservice, just that it is a live URL
   */
  public final void testControllerURL() {
    checkService(JES.CONTROLLER_URL, JES.CONTROLLER_CATEGORY);
  }

  /**
   *  Check that the Scheduler is a valid URL.  Note that the method
   * does not check that it is a valid webservice, just that it is a live URL
   */
  public final void testSchedulerURL() {
    checkService(JES.SCHEDULER_URL, JES.SCHEDULER_CATEGORY);
  }

  /**
   *  Check that the Tools have valids URL.  Note that the method
   * does not check that it is a valid webservice, just that it is a live URL
   */
  public final void testToolsURLs() {
    final String[] tools =
      { "QueryTool", "DataFederation", "SExtractor", "HyperZ" };
    for (int i = 0; i < tools.length; ++i) {
      log.debug("Testing location of tool " + tools[i]);
      checkService(JES.TOOLS_LOCATION + tools[i], JES.TOOLS_CATEGORY);
    }
  }

  /**
   * Utility method to save typing
   * @param key key in JES of URL to check
   * @param category category in JES of URL to check
   */
  private void checkService(final String key, final String category) {
    log.debug("looking for property "+category+":"+key);
    final String endPoint = JES.getProperty(key, category);
    checkURL(endPoint);
  }

  /**
   * Given a URL, check that it is reachable 
   * @param endPoint the URL
   */
  private final void checkURL(final String endPoint) {
    assert endPoint != null;
    log.debug("Obtained end point " + endPoint);
    try {
      URL url = new URL(endPoint);
      InputStream is = url.openStream();
      is.close();
    } catch (IOException e) {
      log.debug(e);
      fail(
        "The end point "
          + endPoint
          + " is not a valid URL, or is unreachable.  Check your configuration");
    }
  }

}

/*
*$Log: ServicesLocatedTest.java,v $
*Revision 1.1  2004/01/21 17:51:34  jdt
*Some installation tests, and some bug fixes of configuration.jsp and some broken links
*
*Revision 1.1  2004/01/21 17:02:43  jdt
*Initial commit
*
*/