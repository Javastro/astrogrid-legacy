/*$Id: StoreClientTestHelper.java,v 1.1 2004/03/14 13:23:14 mch Exp $
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import junit.framework.TestCase;
import org.astrogrid.community.User;
import org.astrogrid.io.Piper;
import org.astrogrid.store.Agsl;

/** Provides a load of tests for doing things to StoreClients.  Subclasses/
 * implementations are written against particular store clients.  See LocalFileStoreClient
 * for example
 *
 * @author M Hill
 *
 */
public abstract class StoreClientTest extends TestCase {

   protected static String path = "";
   
   protected static final String SOURCE_TEST = "TestOpsFile.txt";
   protected static final String COPY_TEST = "copiedFile.txt";
   protected static final String MOVE_TEST = "movedFile.txt";
   
   
   protected static final String SOURCE_CONTENTS = "Contents of test file for copying, etc";
   
   /** Provide three instances of StoreClient, the first two referring to teh
    * same service.  Will check that same files can be read from the first two
    * but not the third
    */
   public void assertStoreAccess(StoreClient aStore, StoreClient sameStore, StoreClient differentStore) throws IOException
   {
      
      //create file in one
      aStore.putString("This is just a test file for "+this.getClass(), path+SOURCE_TEST, false);

      //see if you can get it
      assertFileExists(aStore, path+SOURCE_TEST);

      //in both
      assertFileExists(sameStore, path+SOURCE_TEST);

      assertEquals(aStore.getUrl(path+SOURCE_TEST), sameStore.getUrl(path+SOURCE_TEST));

      //get rid of it if it's already there
      StoreFile f = differentStore.getFile(path+SOURCE_TEST);
      if (f != null) {
         differentStore.delete(path+SOURCE_TEST);
      }
      assertNull(differentStore.getFile(path+SOURCE_TEST));  //check it;s not there
      
      //create file in different one
      differentStore.putString("This is just a test file for "+this.getClass(), path+SOURCE_TEST, false);

      //see if you can get it
      assertFileExists(differentStore, path+SOURCE_TEST);
      
      //check it's not the same url as the other stores
      assertNotSame(aStore.getUrl(path+SOURCE_TEST), differentStore.getUrl(path+SOURCE_TEST));
   }

   /** Tests getFile etc - if these are failing the rest of the tests might
    * not make much sense.  This is a bit of a combined one anyway... */
   public void assertGetFileWorks(StoreClient store) throws IOException
   {
      //create file
      store.putString("This is just a test file for "+this.getClass(), path+SOURCE_TEST, false);

      StoreFile f = store.getFile(path+SOURCE_TEST);
      assertNotNull(f);
      assertEquals(path+SOURCE_TEST, f.getName());

      store.delete(path+SOURCE_TEST);
      
      assertNull(store.getFile(path+SOURCE_TEST));
   }

   /** Tests making folders and paths and stuff.  A bit
    */
   public void assertFoldersWork(StoreClient store) throws IOException {
      
      store.newFolder(path+"NewFolder");

      //create file in new folder
      store.putString("This is just a test file for "+this.getClass(), "NewFolder/NewFile.txt", false);
      
      StoreFile f = store.getFile(path+"NewFolder/NewFile.txt");
      assertNotNull(f);
      assertEquals("NewFile.txt", f.getName());
      assertEquals("NewFolder", f.getParent().getName());
      if (path.length() == 0) { assertNull(f.getParent().getParent()); }
      assertEquals(path+"NewFolder/NewFile.txt", f.getPath());
      
      store.delete(path+"NewFolder/NewFile.txt");
      
      assertNull(store.getFile(path+"NewFolder/NewFile.txt"));
      
   }
   
   
   /** Sets up for each operation test */
   //not quite happy with this...
   private void prepareForOp(StoreClient store) throws IOException
   {
      //see if it's already there
      StoreFile f = store.getFile(path+SOURCE_TEST);
      if (f == null) {
         //test normal save
         store.putString(SOURCE_CONTENTS, path+SOURCE_TEST,false);
         //check it's there now
         assertFileExists(store, path+SOURCE_TEST);
      }
   }
   
   /** Checks that a file exists in the given store at the given path.  Will
    * actually throw a FileNotFoundException rather than an assertion error if
    * it can be URLd but not found */
   private void assertFileExists(StoreClient store, String path) throws IOException {
      URL url = store.getUrl(path);
      assertNotNull(url);
      InputStream in = url.openStream();
      assertNotNull(in);
      in.close();
   }
   
   public void assertMove(StoreClient store, Agsl target) throws IOException
   {
      prepareForOp(store);
      //test move
      /* broken? */
      store.move(path+SOURCE_TEST, target);

      //target store may not be same as source store
      StoreClient targetStore = StoreDelegateFactory.createDelegate(User.ANONYMOUS, target);

      //make sure old one is dead
      StoreFile f = store.getFile(path+SOURCE_TEST);
      assertNull(f);

      //make sure new one exists
      assertFileExists(targetStore, target.getPath());
      
      //make sure contents are the same
      InputStream in = targetStore.getUrl(target.getPath()).openStream();
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      Piper.bufferedPipe(in, out);
      in.close();
      out.close();
      assertEquals(SOURCE_CONTENTS, out.toString());
    
      //delete moved file
      targetStore.delete(target.getPath());
   }

   public void assertCopy(StoreClient store, Agsl target) throws IOException
   {
      prepareForOp(store);
      //do copy
      store.copy(path+SOURCE_TEST, target);

      //make sure old one exists
      assertFileExists(store, path+SOURCE_TEST);

      //target store may not be same as source store
      StoreClient targetStore = StoreDelegateFactory.createDelegate(User.ANONYMOUS, target);

      //make sure new one exists
      assertFileExists(targetStore, target.getFilename());
      
      //make sure contents are the same
      InputStream in = targetStore.getUrl(target.getPath()).openStream();
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      Piper.bufferedPipe(in, out);
      in.close();
      out.close();
      assertEquals(SOURCE_CONTENTS, out.toString());

      //delete copied file
      targetStore.delete(target.getPath());
      
   }
   
   
   public void assertDelete(StoreClient store) throws IOException {
      prepareForOp(store);

      //test normal save
      store.putString("Test file for copying, etc - should have been deleted", path+SOURCE_TEST,false);
      StoreFile f = store.getFile(path+SOURCE_TEST);
      assertNotNull(f);
      
      //do delete
      store.delete(path+SOURCE_TEST);
      
      //make sure it's gone
      f = store.getFile(path+SOURCE_TEST);
      assertNull(f);
   }
   
}


/*
$Log: StoreClientTestHelper.java,v $
Revision 1.1  2004/03/14 13:23:14  mch
Renamed StoreClientTestHelper so that it doesn't get used directly for testing

Revision 1.3  2004/03/02 11:55:56  mch
Fixed move and copy bugs

Revision 1.2  2004/03/02 01:25:39  mch
Minor fixes

Revision 1.1  2004/03/01 23:44:10  mch
Factored out common myspace tests


*/

