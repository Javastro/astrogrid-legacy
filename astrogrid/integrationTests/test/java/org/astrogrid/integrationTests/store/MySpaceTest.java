/*$Id: MySpaceTest.java,v 1.3 2004/03/22 15:01:14 mch Exp $
 * Created on 05-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.integrationTests.store;

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.astrogrid.community.User;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.LocalFileStore;
import org.astrogrid.store.delegate.MySpaceIt04Delegate;
import org.astrogrid.integrationTests.common.ConfManager;

/** Tests the automatically deployed myspace on VM07
 *
 * @author mch
 */

public class MySpaceTest extends StoreClientTestHelper {

   private static User testUser;

   public static String MYSPACE;

   public MySpaceTest() throws IOException {
      super();
      
      MYSPACE = ConfManager.getInstance().getMySpaceEndPoint();//"http://vm05.astrogrid.org:8080/astrogrid-mySpace/services/MySpaceManager";
      testUser = new User("Me", "Us", "Loony");
   }
   
   public void testStoreAccess() throws IOException
   {
      assertStoreAccess(new MySpaceIt04Delegate(testUser, MYSPACE), new MySpaceIt04Delegate(testUser, MYSPACE), new LocalFileStore());
   }

   /** Tests getFile etc - if these are failing the rest of the tests might
    * not make much sense.  This is a bit of a combined one anyway... */
   public void testGetFile() throws IOException
   {
      assertGetFileWorks(new MySpaceIt04Delegate(testUser, MYSPACE));
   }

   /** Tests making folders and paths and stuff.  A bit
    */
   public void testFolders() throws IOException {
      assertFoldersWork(new MySpaceIt04Delegate(testUser, MYSPACE));
      
   }
   
   public void testMove() throws IOException
   {
      assertMove(new MySpaceIt04Delegate(testUser, MYSPACE), new Agsl(MYSPACE, path+COPY_TEST ));
   }

   public void testCopy() throws IOException
   {
      assertCopy(new MySpaceIt04Delegate(testUser, MYSPACE), new Agsl(MYSPACE, path+COPY_TEST));
   }
   
   public void testDelete() throws IOException {
      assertDelete(new MySpaceIt04Delegate(testUser, MYSPACE));
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
Revision 1.3  2004/03/22 15:01:14  mch
Copied in StoreClientTestHelper

Revision 1.1  2004/03/02 01:20:23  mch
Added Vm05 Myspace tests


*/

