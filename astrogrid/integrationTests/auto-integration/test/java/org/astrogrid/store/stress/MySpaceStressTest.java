/*$Id: MySpaceStressTest.java,v 1.1 2005/01/14 15:37:46 jdt Exp $
 * Created on 05-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.store.stress;

import java.io.IOException;

import junit.framework.TestCase;

import org.astrogrid.community.User;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.astrogrid.store.delegate.StoreFile;
import org.astrogrid.store.delegate.local.LocalFileStore;

/** 
 * Quick and dirty test to put a lot of files into mySpace
 *
 * @author jdt
 */

public class MySpaceStressTest extends TestCase {

   private static User testUser;

   public static Agsl MYSPACE;

private String path;

   public MySpaceStressTest() throws IOException {
      super();
      
   }
   protected static final String SOURCE_TEST = "OneOfManyMediumFiles";
   protected static final String COPY_TEST = "copiedFile.txt";
   protected static final String MOVE_TEST = "movedFile.txt";
   protected static final String SOURCE_CONTENTS =  "Contents of test file for copying, etc";
   private static final String TEST_USER="frog";
    
   /**
    * @TODO - this must be refactored to get the correct test machine!
    */
   public void setUp() throws IOException {
      MYSPACE= new Agsl("astrogrid:store:myspace:http://127.0.0.1:8888/astrogrid-mySpace-SNAPSHOT/services/Manager");
      //MYSPACE = ConfManager.getInstance().getMySpaceEndPoint();
      
      //MYSPACE = new Agsl("myspace:http://vm05.astrogrid.org:8080/astrogrid-mySpace/services/MySpaceManager");
      //MYSPACE = new Agsl("myspace:http://grendel12.roe.ac.uk:8080/MySpaceManager_v041/services/MySpaceManager");
      
      path = TEST_USER+"/";
      testUser = new User(TEST_USER, "test.astrogrid.org", "group", "Loony");

      //make sure user exists
      try {
         StoreDelegateFactory.createAdminDelegate(testUser, MYSPACE).createUser(testUser);
      }
      catch (IOException ioe) {
        //might barf if the user already exists, but that's OK
         ioe.printStackTrace();
      }
      bigString = new StringBuffer(SOURCE_CONTENTS);
      while (bigString.length()<20*1024) {
        System.out.println(bigString.length());
       bigString.append(bigString); 
      }

   
   }
   


   private final int  MAX=3500;

private StringBuffer bigString;
   public void testStickFileIn() throws IOException
   {
      StoreClient aStore  = StoreDelegateFactory.createDelegate(testUser, MYSPACE);
      
      
      //create file in one
      for (int i =0;i<MAX;++i) {
      	byte[] bytes = bigString.toString().getBytes();
        System.out.print("Putting file "+i+"...");
		aStore.putBytes(bytes, 0, bytes.length, path+SOURCE_TEST+Integer.toString(i), false);
        System.out.println("...done");
      }

      //see if you can get it
    //  assertFileExists(aStore, path+SOURCE_TEST);

      //in both
   //   assertFileExists(sameStore, path+SOURCE_TEST);

  //    assertEquals(aStore.getFile(path+SOURCE_TEST).getPath(), sameStore.getFile(path+SOURCE_TEST).getPath());


      
   }

 


}


/*
$Log: MySpaceStressTest.java,v $
Revision 1.1  2005/01/14 15:37:46  jdt
emergency myspace stress test



*/


