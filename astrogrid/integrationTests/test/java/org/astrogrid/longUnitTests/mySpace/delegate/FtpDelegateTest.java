/*
 * $Id: FtpDelegateTest.java,v 1.1 2004/01/23 16:26:33 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.longUnitTests.mySpace.delegate;
import java.io.IOException;

import org.astrogrid.mySpace.delegate.MySpaceSunFtpDelegate;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Tests myspace delegates.
 *
 * Temporarily in same directory as delegates until this all gets added back into
 * myspace project.
 *
 * @author M Hill
 */

public class FtpDelegateTest extends TestCase {
  /**
   * Commons-logging logger
   */
  private static org.apache.commons.logging.Log log =
    org.apache.commons.logging.LogFactory.getLog(FtpDelegateTest.class);
    
/**
 * URL of server to use for tests
 */
   public static final String TEST_FTP_SERVER = "ftp://ftp.roe.ac.uk/pub/astrogrid";

   /**
    * Test FTP Delegate
    * @throws IOException on problems connecting
    */
  public final void testFtp() throws IOException {

      MySpaceSunFtpDelegate pub = new MySpaceSunFtpDelegate(TEST_FTP_SERVER);
      //pub.setLogin("astrogrid","astrogrid");

      pub.connect();

      String url = pub.publiciseToServer("test", getClass().getResource("aFile.txt").openStream());
      log.trace("File available at '"+url+"'");
//      pub.updateFromServer(url, "");

      pub.disconnect();

      log.trace("...disconnected");
   }


   public static void main(String[] args) throws IOException {
      org.astrogrid.log.Log.logToConsole();
      junit.textui.TestRunner.run(suite());
   }
   public static Test suite() {
      return new TestSuite(FtpDelegateTest.class);
   }

}

/*
 $Log: FtpDelegateTest.java,v $
 Revision 1.1  2004/01/23 16:26:33  jdt
 Moved out of mySpace tests to  integrationTests org.astrogrid.integrationTests.mySpace.delegate.FtpDelegateTest
 This test requires network resources that might not be available on a users local machine,
 so out it goes.

 Revision 1.2  2003/12/03 17:41:23  mch
 Removed manager empty test

 Revision 1.1  2003/12/03 17:39:25  mch
 New factory/interface based myspace delegates

 Revision 1.1  2003/12/02 18:03:53  mch
 Moved MySpaceDummyDelegate

 */

