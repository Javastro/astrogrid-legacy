/*$Id: MySpaceTest.java,v 1.1 2004/03/02 01:20:23 mch Exp $
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

import org.astrogrid.store.delegate.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.astrogrid.community.User;
import org.astrogrid.store.Agsl;

/** Tests the automatically deployed myspace on VM07
 *
 * @author mch
 */

public class MySpaceTest extends StoreClientTest {

   private static final String USER = "Me";
   private static final String COMMUNITY ="Us";
   private static final String CERTIFIED = "Loony";

   public static final String MYSPACE = "http://vm05.astrogrid.org:8080/astrogrid-mySpace/services/MySpaceManager";

   public void testStoreAccess() throws IOException
   {
      assertStoreAccess(new MySpaceIt04Delegate(User.ANONYMOUS, MYSPACE), new MySpaceIt04Delegate(User.ANONYMOUS, MYSPACE), new LocalFileStore());
   }

   /** Tests getFile etc - if these are failing the rest of the tests might
    * not make much sense.  This is a bit of a combined one anyway... */
   public void testGetFile() throws IOException
   {
      assertGetFileWorks(new MySpaceIt04Delegate(User.ANONYMOUS, MYSPACE));
   }

   /** Tests making folders and paths and stuff.  A bit
    */
   public void testFolders() throws IOException {
      assertFoldersWork(new MySpaceIt04Delegate(User.ANONYMOUS, MYSPACE));
      
   }
   
   public void testMove() throws IOException
   {
      assertMove(new MySpaceIt04Delegate(User.ANONYMOUS, MYSPACE), new Agsl(MYSPACE, path+COPY_TEST ));
   }

   public void testCopy() throws IOException
   {
      assertCopy(new MySpaceIt04Delegate(User.ANONYMOUS, MYSPACE), new Agsl(MYSPACE, path+COPY_TEST));
   }
   
   public void testDelete() throws IOException {
      assertDelete(new MySpaceIt04Delegate(User.ANONYMOUS, MYSPACE));
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
Revision 1.1  2004/03/02 01:20:23  mch
Added Vm05 Myspace tests

Revision 1.1  2004/03/01 22:35:09  mch
Tests for StoreClient

Revision 1.4  2004/03/01 16:51:10  mch
Removed test for equality between local filespaces

Revision 1.3  2004/01/23 15:22:27  jdt
added logging

Revision 1.2  2003/12/03 17:39:25  mch
New factory/interface based store delegates

Revision 1.1  2003/09/22 17:36:09  mch
tests for storeDumnmyDelegate

*/

