/*$Id: LocalFileStoreTest.java,v 1.1 2004/03/01 22:35:09 mch Exp $
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
import java.io.InputStream;
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.store.Agsl;

/** Tests the dummy delegate
 * @author M Hill
 *
 */
public class LocalFileStoreTest extends TestCase {
  /**
   * Commons-logging logger
   */
  private static org.apache.commons.logging.Log log =
    org.apache.commons.logging.LogFactory.getLog(LocalFileStoreTest.class);

   private static final String USER = "Me";
   private static final String COMMUNITY ="Us";
   private static final String CERTIFIED = "Loony";

   private static final String TESTSTORE = "astrogrid:store:file://LocalFileStoreTest";

   private static final String TESTSTORE2 = "astrogrid:store:file://LocalFileStoreTest2";
   
   private static final String SOURCE_TEST = "TestOpsFile.txt";

   public void testStoreAccess() throws IOException
   {
      //create store connection
      LocalFileStore store1 = new LocalFileStore(new Agsl(TESTSTORE));

      //what happens if you try again - should just open connection to same place
      LocalFileStore store2 = new LocalFileStore(new Agsl(TESTSTORE));

      //create file in one
      store1.putString("This is just a test file for "+this.getClass(), SOURCE_TEST, false);

      //see if you can get it
      assertFileExists(store1, SOURCE_TEST);

      //in both
      assertFileExists(store2, SOURCE_TEST);

      assertEquals(store1.getUrl(SOURCE_TEST), store2.getUrl(SOURCE_TEST));

      //should not be the same
      LocalFileStore store3 = new LocalFileStore(new Agsl(TESTSTORE2));
      
      //get rid of it if it's already there
      StoreFile f = store3.getFile(SOURCE_TEST);
      if (f != null) {
         store3.delete(SOURCE_TEST);
      }
      assertNull(store3.getFile(SOURCE_TEST));  //check it;s not there
      
      //create file in different one
      store3.putString("This is just a test file for "+this.getClass(), SOURCE_TEST, false);

      //see if you can get it
      assertFileExists(store3, SOURCE_TEST);
      
      //check it's not the same url as the other stores
      assertNotSame(store1.getUrl(SOURCE_TEST), store3.getUrl(SOURCE_TEST));
   }

   /** Tests getFile etc - if these are failing the rest of the tests might
    * not make much sense.  This is a bit of a combined one anyway... */
   public void testGetFile() throws IOException
   {
      //create store connection
      LocalFileStore store = new LocalFileStore(new Agsl(TESTSTORE));
      
      //create file
      store.putString("This is just a test file for "+this.getClass(), SOURCE_TEST, false);

      StoreFile f = store.getFile(SOURCE_TEST);
      assertNotNull(f);
      assertEquals(SOURCE_TEST, f.getName());

      store.delete(SOURCE_TEST);
      
      assertNull(store.getFile(SOURCE_TEST));
   }

   /** Tests making folders and paths and stuff.  A bit
    */
   public void testFolders() throws IOException {
      //create store connection
      LocalFileStore store = new LocalFileStore(new Agsl(TESTSTORE));
      
      store.newFolder("NewFolder");

      //create file in new folder
      store.putString("This is just a test file for "+this.getClass(), "NewFolder/NewFile.txt", false);
      
      StoreFile f = store.getFile("NewFolder/NewFile.txt");
      assertNotNull(f);
      assertEquals("NewFile.txt", f.getName());
      assertEquals("NewFolder", f.getParent().getName());
      assertNull(f.getParent().getParent());
      assertEquals("NewFolder/NewFile.txt", f.getPath());
      
      store.delete("NewFolder/NewFile.txt");
      
      assertNull(store.getFile("NewFolder/NewFile.txt"));
      
   }
   
   
   /** Sets up for each operation test */
   //not quite happy with this...
   private LocalFileStore prepareStore() throws IOException
   {
      //create store connection
      LocalFileStore store = new LocalFileStore(new Agsl(TESTSTORE));
      
      //see if it's already there
      StoreFile f = store.getFile(SOURCE_TEST);
      if (f == null) {
         //test normal save
         store.putString("Test file for copying, etc - should have been deleted", SOURCE_TEST,false);
         //check it's there now
         assertFileExists(store, SOURCE_TEST);
      }
      
      return store;
      
   }
   
   /** Checks that a file exists in the given store at the given path.  Will
    * actually throw a FileNotFoundException rather than an assertion error if
    * it can be URLd but not found */
   private void assertFileExists(LocalFileStore store, String path) throws IOException {
      URL url = store.getUrl(path);
      assertNotNull(url);
      InputStream in = url.openStream();
      assertNotNull(in);
      in.close();
   }
   
   public void testMove() throws IOException
   {
      //create store connection
      LocalFileStore store = prepareStore();

      //test move
      /* broken? */
      store.move(SOURCE_TEST, new Agsl(TESTSTORE+"#testRename"));

      //make sure old one is dead
      StoreFile f = store.getFile(SOURCE_TEST);
      assertNull(f);

      //make sure new one exists
      assertFileExists(store, "testRename");
   }

   public void testCopy() throws IOException
   {
      //create store connection
      LocalFileStore store = prepareStore();

      //do copy
      store.copy(SOURCE_TEST, new Agsl(TESTSTORE+"#testCopy"));

      //make sure new one exists
      assertFileExists(store, SOURCE_TEST);

      //make sure new one exists
      assertFileExists(store, "testCopy");
   }
   
   
   public void testDelete() throws IOException {
      LocalFileStore store = prepareStore();
      
      //do delete
      store.delete(SOURCE_TEST);
      
      //make sure it's gone
      StoreFile f = store.getFile(SOURCE_TEST);
      assertNull(f);
   }
   
   public void testCrossStoreCopy() throws IOException {

      LocalFileStore store = prepareStore();
      
      //do copy to different localfilestore
      store.copy(SOURCE_TEST, new Agsl(TESTSTORE2+"#TargetStoreCopy"));

      //check it's there
      LocalFileStore targetstore = new LocalFileStore(new Agsl(TESTSTORE2));
      
      assertFileExists(targetstore, "TargetStoreCopy");
      
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

