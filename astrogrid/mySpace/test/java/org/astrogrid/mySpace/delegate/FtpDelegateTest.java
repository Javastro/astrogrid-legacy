/*
 * $Id: FtpDelegateTest.java,v 1.5 2004/01/26 12:55:27 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.mySpace.delegate;
import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.test.OptionalTestCase;


/**
 * Tests myspace delegates.
 *
 * Temporarily in same directory as delegates until this all gets added back into
 * myspace project.
 *
 * @author M Hill
 */

public class FtpDelegateTest extends OptionalTestCase {
  static {
//  This test should only be enabled when running on a machine with access to the ftp server
//  This must be called before the superclass gets constucted, hence its odd placement here
    OptionalTestCase.setDisabledByDefault(true); 
  }
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
 Revision 1.5  2004/01/26 12:55:27  jdt
 adapted to use Noel's rather spiffing OptionalTestCase.  This test is now disabled by default, 
 but will be re-enabled on maven, should we get an FTP server we can use.

 Revision 1.4  2004/01/26 11:53:59  jdt
 Restored

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

