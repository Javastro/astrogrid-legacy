/*
 * $Id: MySpaceDelegateTest.java,v 1.2 2003/12/03 18:24:28 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.mySpace.delegate;

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate;


/**
 * Tests myspace delegates.
 *
 * Temporarily in same directory as delegates until this all gets added back into
 * myspace project.
 *
 * @author M Hill
 */

public class MySpaceDelegateTest extends TestCase {

   public static final String TEST_FTP_SERVER = "ftp://ftp.roe.ac.uk/pub/astrogrid";

   public void testDelegateTypes() throws IOException
   {
      assertTrue(MySpaceDelegateFactory.createDelegate(MySpaceDummyDelegate.DUMMY) instanceof MySpaceDummyDelegate);
      assertTrue(MySpaceDelegateFactory.createDelegate("http://myspace.somewhere/") instanceof MySpaceManagerDelegate);
      assertTrue(MySpaceDelegateFactory.createDelegate(TEST_FTP_SERVER) instanceof MySpaceSunFtpDelegate);
   }

   public void testFtp() throws IOException {

      MySpaceSunFtpDelegate pub = new MySpaceSunFtpDelegate(TEST_FTP_SERVER);
      //pub.setLogin("astrogrid","astrogrid");

      pub.connect();

      String url = pub.publiciseToServer("test", getClass().getResource("aFile.txt").openStream());
      org.astrogrid.log.Log.trace("File available at '"+url+"'");
//      pub.updateFromServer(url, "");

      pub.disconnect();

      org.astrogrid.log.Log.trace("...disconnected");
   }

   public void testMySpace() throws Exception {
      MySpaceManagerDelegate myspace = new MySpaceManagerDelegate();
      //erm
   }


   public static void main(String[] args) throws IOException {
      org.astrogrid.log.Log.logToConsole();
      junit.textui.TestRunner.run(suite());
   }
   public static Test suite() {
      return new TestSuite(MySpaceDelegateTest.class);
   }

}

/*
 $Log: MySpaceDelegateTest.java,v $
 Revision 1.2  2003/12/03 18:24:28  mch
 Temporary add to make a self-contained jar for warehouse testing

 Revision 1.1  2003/12/02 18:03:53  mch
 Moved MySpaceDummyDelegate

 */

