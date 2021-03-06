/*$Id: LocalFileStoreTest.java,v 1.2 2004/06/14 23:08:53 jdt Exp $
 * Created on 05-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.store.delegate;

import java.io.IOException;
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.local.LocalFileStore;

/** Tests the dummy delegate
 * @author M Hill
 *
 */
public class LocalFileStoreTest extends StoreClientTestHelper {

   
   private static final String TESTSTORE = "file://LocalFileStoreTest";

   private static final String TESTSTORE2 = "file://LocalFileStoreTest2";
   
   public void testStoreAccess() throws IOException
   {
      assertStoreAccess(new LocalFileStore(new Agsl(new URL(TESTSTORE))),
                        new LocalFileStore(new Agsl(new URL(TESTSTORE))),
                        new LocalFileStore(new Agsl(new URL(TESTSTORE2))));
   }

   /** Tests getFile etc - if these are failing the rest of the tests might
    * not make much sense.  This is a bit of a combined one anyway... */
   public void testGetFile() throws IOException
   {
      assertGetFileWorks(new LocalFileStore(new Agsl(new URL(TESTSTORE))));
   }

   /** Tests making folders and paths and stuff.  A bit
    */
   public void testFolders() throws IOException {
      assertFoldersWork(new LocalFileStore(new Agsl(new URL(TESTSTORE))));
   }
   
   
   public void testMove() throws IOException
   {
      //test move on same server
      assertMove(new LocalFileStore(new Agsl(new URL(TESTSTORE))), new Agsl(new URL(TESTSTORE), MOVE_TEST));
      
      //test move between servers
      assertMove(new LocalFileStore(new Agsl(new URL(TESTSTORE))), new Agsl(new URL(TESTSTORE2), MOVE_TEST));
   }

   public void testCopy() throws IOException
   {
      //test move on same server
      assertCopy(new LocalFileStore(new Agsl(new URL(TESTSTORE))), new Agsl(new URL(TESTSTORE), COPY_TEST));
      
      //test move between servers
      assertCopy(new LocalFileStore(new Agsl(new URL(TESTSTORE))), new Agsl(new URL(TESTSTORE2), COPY_TEST));
   }
   
   
   public void testDelete() throws IOException {
      assertDelete(new LocalFileStore(new Agsl(new URL(TESTSTORE))));
   }
   
    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(LocalFileStoreTest.class);
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
$Log: LocalFileStoreTest.java,v $
Revision 1.2  2004/06/14 23:08:53  jdt
Merge from branches
ClientServerSplit_JDT
and
MySpaceClientServerSplit_JDT

MySpace now split into a client/delegate jar
astrogrid-myspace-<version>.jar
and a server/manager war
astrogrid-myspace-server-<version>.war

Revision 1.1.2.1  2004/06/14 22:33:21  jdt
Split into delegate jar and server war.  
Delegate: astrogrid-myspace-SNAPSHOT.jar
Server/Manager: astrogrid-myspace-server-SNAPSHOT.war

Package names unchanged.
If you regenerate the axis java/wsdd/wsdl files etc you'll need
to move some files around to ensure they end up in the client
or the server as appropriate.
As of this check-in the tests/errors/failures is 162/1/22 which
matches that before the split.

Revision 1.6  2004/05/19 16:24:33  mch
Properly typed Agsl creation, some fixes to tests

Revision 1.5  2004/03/14 13:23:14  mch
Renamed StoreClientTestHelper so that it doesn't get used directly for testing

Revision 1.4  2004/03/08 13:46:25  mch
Fixed imports

Revision 1.3  2004/03/02 01:25:39  mch
Minor fixes

Revision 1.2  2004/03/01 23:44:10  mch
Factored out common myspace tests

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

