/*$Id: MySpaceTest.java,v 1.4 2004/04/14 14:26:36 mch Exp $
 * Created on 05-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.integrationtest.store;

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.astrogrid.community.User;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.astrogrid.store.delegate.local.LocalFileStore;

/** Tests the automatically deployed myspace on VM07
 *
 * @author mch
 */

public class MySpaceTest extends StoreClientTestHelper {

   private static User testUser;

   public static Agsl MYSPACE;

   public MySpaceTest() throws IOException {
      super();
      
   }

   public void setUp() throws IOException {
      MYSPACE= new Agsl("myspace:http://grendel12.roe.ac.uk:8080/MySpaceManager/services/Manager");
      //MYSPACE = ConfManager.getInstance().getMySpaceEndPoint();
      
      //MYSPACE = new Agsl("myspace:http://vm05.astrogrid.org:8080/astrogrid-mySpace/services/MySpaceManager");
      //MYSPACE = new Agsl("myspace:http://grendel12.roe.ac.uk:8080/MySpaceManager_v041/services/MySpaceManager");
      
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
Revision 1.4  2004/04/14 14:26:36  mch
Switched to factory not explicit delegate creation

Revision 1.3  2004/03/22 16:31:37  mch
Fixed tests for new StoreClient package

Revision 1.3  2004/03/22 15:01:14  mch
Copied in StoreClientTestHelper

Revision 1.1  2004/03/02 01:20:23  mch
Added Vm05 Myspace tests


*/

