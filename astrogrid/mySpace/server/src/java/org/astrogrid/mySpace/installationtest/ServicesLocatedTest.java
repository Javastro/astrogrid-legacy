/* $Id: ServicesLocatedTest.java,v 1.2 2004/06/14 23:08:52 jdt Exp $
 * Created on 21-Jan-2004 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.mySpace.installationtest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.mySpace.mySpaceManager.MMC;

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
   *  Check that the MySpaceManagerURL is a valid URL.  Note that the method
   * does not check that it is a valid webservice, just that it is a live URL
   */
  public final void testMySpaceManagerURL() {
    final String endPoint = MMC.getProperty(MMC.mySpaceManagerLoc, MMC.CATLOG);
    checkURL(endPoint);
  }

  /**
   *  Check that the MySpaceManagerURLs are valid URLs.  Note that the method
   * does not check that it is a valid webservice, just that it is a live URL
   */
  public final void testMySpaceManagerURLs() {
    final String endPointList =
      MMC.getProperty(MMC.MYSPACEMANAGERURLs, MMC.CATLOG);
    assert endPointList != null;
    final String[] endPoints = endPointList.split(",");
    assert endPoints != null;
    for (int i = 0; i < endPoints.length; ++i) {
      String endPoint = endPoints[i];
      checkURL(endPoint);
    }
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
        "The end point " + endPoint + " is not a valid URL, or is unreachable.  Check your configuration");
    }
  }

}

/*
*$Log: ServicesLocatedTest.java,v $
*Revision 1.2  2004/06/14 23:08:52  jdt
*Merge from branches
*ClientServerSplit_JDT
*and
*MySpaceClientServerSplit_JDT
*
*MySpace now split into a client/delegate jar
*astrogrid-myspace-<version>.jar
*and a server/manager war
*astrogrid-myspace-server-<version>.war
*
*Revision 1.1.2.1  2004/06/14 22:33:21  jdt
*Split into delegate jar and server war.  
*Delegate: astrogrid-myspace-SNAPSHOT.jar
*Server/Manager: astrogrid-myspace-server-SNAPSHOT.war
*
*Package names unchanged.
*If you regenerate the axis java/wsdd/wsdl files etc you'll need
*to move some files around to ensure they end up in the client
*or the server as appropriate.
*As of this check-in the tests/errors/failures is 162/1/22 which
*matches that before the split.
*
*Revision 1.1  2004/03/04 19:27:59  jdt
*Package name changed to lower case to satisfy coding standards.  mea culpa - didn't read the Book.  Tx Martin.
*
*Revision 1.2  2004/01/23 15:22:04  jdt
**** empty log message ***
*
*Revision 1.1  2004/01/21 17:02:43  jdt
*Initial commit
*
*/