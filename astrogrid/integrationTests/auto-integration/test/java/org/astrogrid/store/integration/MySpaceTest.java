/*$Id: MySpaceTest.java,v 1.10 2005/03/11 13:36:22 clq2 Exp $
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
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.StoreAdminClient;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.astrogrid.store.delegate.local.LocalFileStore;

/** Tests the automatically deployed myspace on VM07
 *
 *@deprecated remove this package when we get shot of myspace
 * @author mch
 */

public class MySpaceTest extends StoreClientTestHelper {

   private static User testUser;

   public static Agsl MYSPACE;

   public MySpaceTest() throws IOException {
      super();
      
   }

   public void setUp() throws IOException {
      MYSPACE= new Agsl("astrogrid:store:myspace:http://localhost:8080/astrogrid-mySpace-SNAPSHOT/services/Manager");
      //MYSPACE = ConfManager.getInstance().getMySpaceEndPoint();
      
      //MYSPACE = new Agsl("myspace:http://vm05.astrogrid.org:8080/astrogrid-mySpace/services/MySpaceManager");
      //MYSPACE = new Agsl("myspace:http://grendel12.roe.ac.uk:8080/MySpaceManager_v041/services/MySpaceManager");
      
      path = "avodemo/";
      testUser = new User("avodemo", "test.astrogrid.org", "group", "Loony");

      //make sure user exists
      try {
         StoreDelegateFactory.createAdminDelegate(testUser, MYSPACE).createUser(testUser);
      }
      catch (IOException ioe) {
 //ignore        ioe.printStackTrace();
      }
      try {
         new LocalFileStore().newFolder(path);
      }
      catch (IOException ioe) {
 //ignore        ioe.printStackTrace();
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

   /**
    * Tests uploading file from local hard disk
    */
   public void testUpload() throws IOException {

      assertUpload(StoreDelegateFactory.createDelegate(testUser, MYSPACE));
   }
   
   /** Tests making folders and paths and stuff.  A bit
    */
   public void testFolders() throws IOException {
      assertFoldersWork(StoreDelegateFactory.createDelegate(testUser, MYSPACE));
      
   }
   
   public void testMove() throws IOException
   {
      assertMove(StoreDelegateFactory.createDelegate(testUser, MYSPACE),
                 new Agsl(MYSPACE, path+COPY_TEST ));
   }

   public void testCopy() throws IOException
   {
      assertCopy(StoreDelegateFactory.createDelegate(testUser, MYSPACE),
                 new Agsl(MYSPACE, path+COPY_TEST ));
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
       junit.textui.TestRunner.run(suite());
    }

}


/*
$Log: MySpaceTest.java,v $
Revision 1.10  2005/03/11 13:36:22  clq2
with merges from filemanager

Revision 1.9.174.1  2005/03/10 19:33:16  nw
marked tests for removal once myspace is replaced by filemanager.

Revision 1.9  2004/05/24 15:53:03  mch
Fixed myspace AGSL

Revision 1.8  2004/05/21 16:41:49  mch
Fixed compile errors

Revision 1.7  2004/05/21 10:52:31  jdt
Temporary quick fixes to deal with compilation errors following

changes to Agsl constructor.

See bugs 334 and 335

Revision 1.6  2004/05/11 09:25:30  pah
make sure that the user object is created properly

Revision 1.5  2004/04/22 13:50:38  mch
Fixes to tests and more tests

Revision 1.4  2004/04/15 15:38:56  mch
Changed address to localhost myspace

Revision 1.3  2004/04/15 14:54:57  mch
Fixed some tests

Revision 1.4  2004/04/14 14:26:36  mch
Switched to factory not explicit delegate creation

Revision 1.3  2004/03/22 16:31:37  mch
Fixed tests for new StoreClient package

Revision 1.3  2004/03/22 15:01:14  mch
Copied in StoreClientTestHelper

Revision 1.1  2004/03/02 01:20:23  mch
Added Vm05 Myspace tests


*/


