/*
 * $Id: FactoryTest.java,v 1.2 2003/12/03 17:41:10 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.mySpace.delegate;
import java.io.IOException;
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

public class FactoryTest extends TestCase {

   public static final String TEST_FTP_SERVER = "ftp://ftp.roe.ac.uk/pub/astrogrid";

   public void testDelegateTypes() throws IOException
   {
      assertTrue(MySpaceDelegateFactory.createDelegate(MySpaceDummyDelegate.DUMMY) instanceof MySpaceDummyDelegate);
      assertTrue(MySpaceDelegateFactory.createDelegate("http://myspace.somewhere/") instanceof MySpaceManagerDelegate);
      assertTrue(MySpaceDelegateFactory.createDelegate(TEST_FTP_SERVER) instanceof MySpaceSunFtpDelegate);
   }

   public static void main(String[] args) throws IOException {
      junit.textui.TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(FactoryTest.class);
   }

}

/*
 $Log: FactoryTest.java,v $
 Revision 1.2  2003/12/03 17:41:10  mch
 Fix to get correct suite

 Revision 1.1  2003/12/03 17:39:25  mch
 New factory/interface based myspace delegates

 Revision 1.1  2003/12/02 18:03:53  mch
 Moved MySpaceDummyDelegate

 */

