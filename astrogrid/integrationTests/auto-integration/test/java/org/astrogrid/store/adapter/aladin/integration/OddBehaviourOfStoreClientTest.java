/*$Id: OddBehaviourOfStoreClientTest.java,v 1.2 2004/11/11 17:54:18 clq2 Exp $
 * Created on 10-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.store.adapter.aladin.integration;

import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.adapter.aladin.IterationSixAladinAdapter;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreFile;

import java.io.FileNotFoundException;

import junit.framework.TestCase;

/** this class contains tests to work out the behaviour of StoreClient and StoreFile.
 * things learned are rolled into the implementation of AladinAdapter - so any failures in this class later
 * indicate a change in behaviour of the myspace stuff 
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Nov-2004
 *
 */
public class OddBehaviourOfStoreClientTest extends TestCase {

    /** Construct a new TestOddBehaviourOfStoreClient
     * 
     */
    public OddBehaviourOfStoreClientTest() {        
        super();
    }
    
    protected void setUp() throws Exception {
        Ivorn ivorn = CommunityAccountIvornFactory.createLocal("frog");
        IterationSixAladinAdapter i6= new IterationSixAladinAdapter();
        i6.login(ivorn,"qwerty");
        sc = i6.getStoreClient();
    }

    protected StoreClient sc;
    /** check we managed to create a client */
   public void testClient() {
       assertNotNull(sc);
   }
   
   /** getFile is broken. this test shows that you can get a StoreFile object from getFile, but it dones't contain any info */
   public void testGetFileDoesntWork() throws Exception {
       StoreFile root = sc.getFile("/frog");
       assertNotNull(root);
       assertNotNull(root.getPath());
       assertEquals("",root.getPath()); //faulty       
       assertNull(root.getName()); //faulty
       assertTrue(root.isFolder()); 
       assertFalse(root.isFile());
       StoreFile[] children = root.listFiles();
       assertNotNull(children);
       assertEquals(0,children.length); //faulty
   }
   
   /** oh, getting '/' fails with null */
   public void testGetFileDoesntWork1() throws Exception {
       
       StoreFile root = sc.getFile("/");
       assertNull(root);
       
   }
   
   /** this test shows that, although getFile() doesn't return any info, you can detect the presence of a file by whether it returns null or not */
   public void testDetectAFileWithGetFile() throws Exception {
       StoreFile there = sc.getFile("/frog/workflow");
       assertNotNull(there);
       StoreFile notThere = sc.getFile("/frog/wibble");
       assertNull(notThere);
   }
   
   
   /** maybe this is the correct behaviour */
   public void testGetFilesWithNoLeadingSlash() throws Exception {
       StoreFile container = sc.getFiles("frog");
       assertNull(container);
   }
   
   
   /** getFiles(/frog) returns a collectioin of results, containing a single result - for frog.
    * this is ok - although listFiles() returns empty 
    * also, there's a mismatch between the path you have to ask for '/frog' and the path reported from getPath() 'frog/'
    * which is bad, because querying with the results of getPath fails (see {@link #testGetFilesWithNoLeadingSlash()}) 
    * @throws Exception
    */
   public void testGetFilesDoesWork() throws Exception {
       StoreFile container = sc.getFiles("/frog"); 
       // thing returned back is a container for the search results.
       assertNotNull(container);
       assertNotNull(container.getPath());
       assertEquals("",container.getPath()); 
       assertNull(container.getName()); 
       assertTrue(container.isFolder());
       assertFalse(container.isFile());
       StoreFile[] children = container.listFiles();
       assertNotNull(children);
       assertEquals(1,children.length);
       StoreFile root= children[0];
       // anyway, here's the root.
       assertNotNull(root);
       assertNotNull(root.getPath());
       assertEquals("frog/",root.getPath());// odd it doesn't have the leading '/' - has a trailing one instead.
       assertNotNull(root.getName());
       assertEquals("frog",root.getName());
       assertTrue(root.isFolder());
       assertFalse(root.isFile());
       children = root.listFiles();
       assertNotNull(children);
       assertEquals(0,children.length); // and we've got no children - faulty.
   }

    /** this test shows that getFiles(/frog/*) fails with an exception.*/
   public void testGetFilesWithSlashWildcard() throws Exception {
       try {
           StoreFile container = sc.getFiles("/frog/*"); // faulty?
           fail("expected to throw");
       } catch (FileNotFoundException e) {
           return; // faulty
       }
   }

   /** this test shows the same behaviour for getFiles(/frog/workflow) 
    * this means that we are unable to query on anything containing a '/' */
   public void testGetFilesWithSlash() throws Exception {
       try {
            StoreFile container = sc.getFiles("/frog/workflow");
            fail("expected to throw");
       } catch(FileNotFoundException e) {
           return ; // faulty
       }
   }
   
   
   /** this test shows the best way of working (around) StoreFile  getFiles(/frog*) returns the frog folder, and all children.
    * relies on no users with a common prefix  e.g user 'pat' would get to see all user 'paticia''s files.*/
   public void testGetFilesWilthWildcard() throws Exception {
       StoreFile container = sc.getFiles("/frog*"); // weakness - relies on no users with a common prefix - e.g. user 'pat' and user 'patricia'
       assertNotNull(container);
       StoreFile[] children = container.listFiles();
       assertEquals(1,children.length); 
       StoreFile root = children[0];
       assertNotNull(root);
       assertNotNull(root.getPath());
       assertEquals("frog/",root.getPath());
       assertNotNull(root.getName());
       assertEquals("frog",root.getName());
       assertTrue(root.isFolder());
       assertFalse(root.isFile());
       children = root.listFiles();
       assertNotNull(children);
       assertTrue(children.length > 0);
       for (int i = 0; i < children.length; i++) {
           assertNotNull(children[i]);
           assertNotNull(children[i].getPath());
           assertTrue(children[i].getPath().trim().length() > 0);
           assertNotNull(children[i].getName());
           System.out.println(children[i].getName());
           assertTrue(children[i].getName().trim().length() > 0);           
           assertTrue(children[i].isFile() || children[i].isFolder());           
       }
   }
   

  /** this test shows the results of getFiles(*) - it works, although could be really inefficient */
   public void testGetFilesByRootWildcard() throws Exception {
       StoreFile container = sc.getFiles("*"); // returns entire index for myspace.        
       assertNotNull(container);
       assertNotNull(container.getPath());
       assertEquals("",container.getPath()); 
       assertNull(container.getName()); 
       assertTrue(container.isFolder());
       assertFalse(container.isFile());
       StoreFile[] children = container.listFiles();
       assertNotNull(children);
       assertTrue(children.length > 0); 
       for (int i = 0; i < children.length; i++) {
           assertNotNull(children[i]);
           assertNotNull(children[i].getPath());
           assertTrue(children[i].getPath().trim().length() > 0);
           assertNotNull(children[i].getName());
           System.out.println(children[i].getName());
           assertTrue(children[i].getName().trim().length() > 0);           
           assertTrue(children[i].isFile() || children[i].isFolder());           
       }
   }
     
    
}


/* 
$Log: OddBehaviourOfStoreClientTest.java,v $
Revision 1.2  2004/11/11 17:54:18  clq2
nww-660

Revision 1.1.2.1  2004/11/11 13:10:05  nw
tests for aladin adapter
 
*/