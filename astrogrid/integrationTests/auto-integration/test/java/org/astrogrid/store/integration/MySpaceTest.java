/*$Id: MySpaceTest.java,v 1.2 2004/04/15 14:32:34 jdt Exp $
 * Created on 05-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.store.integration;

import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.community.User;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.astrogrid.store.delegate.local.LocalFileStore;

/** Tests the automatically deployed myspace 
 *
 * @author mch 
 */

public final class MySpaceTest extends StoreClientTestHelper {
    /**
     * Commons logger
     */
    private static final org.apache.commons.logging.Log log =
        org.apache.commons.logging.LogFactory.getLog(MySpaceTest.class);

   private static User testUser;

   public static Agsl MYSPACE;

   public MySpaceTest() throws IOException {
      super();
      
   }

   public void setUp() throws IOException {
      final String myspaceServiceEndPoint = SimpleConfig.getSingleton().getString("org.astrogrid.myspace.endpoint");
      MYSPACE= new Agsl("myspace:"+myspaceServiceEndPoint);
      log.debug("MYSPACE: " +MYSPACE);  
      testUser = new User("avodemo", "test.astrogrid.org", "Loony");
      
      try {
         StoreDelegateFactory.createAdminDelegate(testUser, MYSPACE).createUser(testUser);
      }
      catch (IOException ioe) {
         ioe.printStackTrace();
      }
      try {
         StoreClient client = StoreDelegateFactory.createDelegate(testUser, MYSPACE);
//         client.newFolder("avodemo@test.astrogrid.org");
//         client.newFolder("avodemo@test.astrogrid.org/serv1");
         client.newFolder("avodemo@test.astrogrid.org/serv1/mch");
      }
      catch (IOException ioe) {
         ioe.printStackTrace();
      }
   }
   
   public void testStoreAccess() throws IOException
   {
      assertStoreAccess(StoreDelegateFactory.createDelegate(testUser, MYSPACE),
                        StoreDelegateFactory.createDelegate(testUser, MYSPACE),
                        new LocalFileStore());
   }

   /** Tests getFile etc - if these are failing the rest of the tests might
    * not make much sense.  This is a bit of a combined one anyway... */
   public void testGetFile() throws IOException
   {
      assertGetFileWorks(StoreDelegateFactory.createDelegate(testUser, MYSPACE));
   }

   /** Tests making folders and paths and stuff.  A bit
    */
   public void testFolders() throws IOException {
      assertFoldersWork(StoreDelegateFactory.createDelegate(testUser, MYSPACE));
      
   }
   
   public void testMove() throws IOException
   {
      assertMove(StoreDelegateFactory.createDelegate(testUser, MYSPACE),
                 new Agsl(MYSPACE.getEndpoint(), path+COPY_TEST ));
   }

   public void testCopy() throws IOException
   {
      assertCopy(StoreDelegateFactory.createDelegate(testUser, MYSPACE),
                 new Agsl(MYSPACE.getEndpoint(), path+COPY_TEST ));
   }
   
   public void testDelete() throws IOException {
      assertDelete(StoreDelegateFactory.createDelegate(testUser, MYSPACE));
   }
   
    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(MySpaceTest.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       path = "avodemo@test.astrogrid.org/serv1/mch/";
       
       junit.textui.TestRunner.run(suite());
    }

}


/*
$Log: MySpaceTest.java,v $
Revision 1.2  2004/04/15 14:32:34  jdt
Now picks up location of local myspace

Revision 1.1  2004/04/15 13:39:39  jdt
Moved from integrationTests to auto-integration

Revision 1.1  2004/04/15 13:35:38  jdt
Moved from integrationTests to auto-integration
They'll break the build momentarily....

Revision 1.4  2004/04/14 14:26:36  mch
Switched to factory not explicit delegate creation

Revision 1.3  2004/03/22 16:31:37  mch
Fixed tests for new StoreClient package

Revision 1.3  2004/03/22 15:01:14  mch
Copied in StoreClientTestHelper

Revision 1.1  2004/03/02 01:20:23  mch
Added Vm05 Myspace tests


*/

