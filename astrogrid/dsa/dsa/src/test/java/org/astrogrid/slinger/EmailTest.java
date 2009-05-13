/*
 * $Id: EmailTest.java,v 1.1 2009/05/13 13:21:04 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger;

import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import sun.net.www.protocol.mailto.MailToURLConnection;
import java.io.IOException;


/**
 */

public class EmailTest extends TestCase {

   public void setUp() {
      System.getProperties().put("mail.host", "mail.roe.ac.uk");
   }
   
   public void testEmailUrl() throws Exception {
      new URL("mailto:mch@roe.ac.uk");
   }
   
   /** Naughty test that actually sends an email - will only work at the ROE */
   /*
   public void testEmailSend()  throws IOException {
      URL u = new URL("mailto:mch@roe.ac.uk");
      URLConnection c = null;
      try {
         c = u.openConnection();
      } catch (IOException e) {
         fail(e+" connecting to mail server.  Ignore this error if not run at the ROE");
      }
      MailToURLConnection mc = (MailToURLConnection) c;
      c.setDoOutput(true);
      OutputStream o = c.getOutputStream();
      o.write("Unit testing email URL".getBytes());
      o.close();
   }
   */
   
   
   /**
    * Assembles and returns a test suite made up of all the testXxxx() methods
    * of this class.
    */
   public static Test suite() {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(EmailTest .class);
   }

  
   /**
    * Runs the test case.
    */
   public static void main(String args[]) {
      junit.textui.TestRunner.run(suite());
   }

}



