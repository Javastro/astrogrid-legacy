/*
 * $Id: FactoryTest.java,v 1.1 2004/03/01 22:35:09 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.store.delegate;

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.community.User;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;
import org.astrogrid.mySpace.delegate.MySpaceDummyDelegate;
import org.astrogrid.mySpace.delegate.MySpaceManagerDelegate;
import org.astrogrid.mySpace.delegate.MySpaceSunFtpDelegate;
import org.astrogrid.store.Agsl;


/**
 * Tests store clients
 *
 * @author M Hill
 */

public class FactoryTest extends TestCase {

   public static final String TEST_FTP_SERVER = "ftp://ftp.roe.ac.uk/pub/astrogrid";

   public void testClientFactory() throws IOException
   {
      //test passing in a reference to a file to get the right store
//      assertTrue(StoreDelegateFactory.createDelegate(User.ANONYMOUS, new Agsl("astrogrid:store:file://storename#path/path/path/file.ext")) instanceof LocalFileStore);
//      assertTrue(StoreDelegateFactory.createDelegate(User.ANONYMOUS, new Agsl("astrogrid:store:ftp://a.server.authority/service#path/path/file.ext")) instanceof FtpStore);
//      assertTrue(StoreDelegateFactory.createDelegate(User.ANONYMOUS, new Agsl("astrogrid:store:myspace:http//a.delegate/endpoint#path/path/file.ext")) instanceof LocalFileStore);

      //test passing in a reference just to the service
      assertTrue(StoreDelegateFactory.createDelegate(User.ANONYMOUS, new Agsl("astrogrid:store:file://")) instanceof LocalFileStore);
      assertTrue(StoreDelegateFactory.createDelegate(User.ANONYMOUS, new Agsl("astrogrid:store:ftp://a.server.authority/path/path/file.ext")) instanceof FtpStore);
      assertTrue(StoreDelegateFactory.createDelegate(User.ANONYMOUS, new Agsl("astrogrid:store:myspace:http://a.delegate/endpoint#path/path/file.ext")) instanceof MySpaceIt04ServerDelegate);
   }

   public void testVoSpaceResolver() throws IOException
   {
      //need to set up configs so that we can resolve without a registry/community service
      
      
      
      //call resolver
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
 Revision 1.1  2004/03/01 22:35:09  mch
 Tests for StoreClient

 Revision 1.2  2003/12/03 17:41:10  mch
 Fix to get correct suite

 Revision 1.1  2003/12/03 17:39:25  mch
 New factory/interface based myspace delegates

 Revision 1.1  2003/12/02 18:03:53  mch
 Moved MySpaceDummyDelegate

 */

