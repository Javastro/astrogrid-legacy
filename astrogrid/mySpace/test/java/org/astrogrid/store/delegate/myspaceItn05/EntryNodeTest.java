package org.astrogrid.store.delegate.myspaceItn05;

import junit.framework.*;
import java.util.*;
import java.lang.reflect.Array;

import org.astrogrid.store.delegate.StoreFile;
import org.astrogrid.store.delegate.myspaceItn05.EntryResults;
import org.astrogrid.store.delegate.myspaceItn05.EntryRecord;


/**
 * Junit tests for the <code>EntryNode</code> class.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 5.
 */

public class EntryNodeTest extends TestCase
{

/**
 * Standard constructor for JUnit test classes.
 */

   public EntryNodeTest (String name)
   {  super(name);
   }

/**
 * Test the <code>get</code> methods.
 */

   public void testGets()
   {  String entryName = "/acd/con1/name";
      int    entryId = 0;
      String entryUri = "http://blue.nowhere.org/f1";
      String owner = "owner";
      Date   creation = new Date(0);
      Date   expiry = new Date(0);
      int    size = 1;
      int    type = EntryCodes.VOT;
      String permissions = "permissions";

      EntryNode entry = new EntryNode(entryName, entryId, entryUri,
        owner, creation, expiry, size, type, permissions);

      Assert.assertEquals(entry.getName(), entryName);
      Assert.assertEquals(entry.getPath(), "/acd/con1/");
      Assert.assertEquals(entry.getEntryId(), entryId);
      Assert.assertEquals(entry.getEntryUri(), entryUri );
      Assert.assertEquals(entry.getOwnerId(), owner);
      Assert.assertEquals(entry.getCreationDate(), creation);
      Assert.assertEquals(entry.getExpiryDate(), expiry);
      Assert.assertEquals(entry.getSize(), size);
      Assert.assertEquals(entry.getType(), type);
      Assert.assertEquals(entry.getPermissionsMask(), permissions);
      Assert.assertEquals(entry.getShortName(), "/name");
      Assert.assertEquals(entry.getParent(), null);
   }

/**
 * Test the <code>isFolder</code> method.
 */

   public void testIsFolder()
   {  String entryName = "/acd/con1/name";
      int    entryId = 0;
      String entryUri = "http://blue.nowhere.org/f1";
      String owner = "owner";
      Date   creation = new Date(0);
      Date   expiry = new Date(0);
      int    size = 1;
      int    type = EntryCodes.VOT;
      String permissions = "permissions";

      EntryNode entry = new EntryNode(entryName, entryId, entryUri,
        owner, creation, expiry, size, type, permissions);

      Assert.assertTrue(!entry.isFolder() );


      type = EntryCodes.CON;

      entry = new EntryNode(entryName, entryId, entryUri,
        owner, creation, expiry, size, type, permissions);

      Assert.assertTrue(entry.isFolder() );
   }


/**
 * Test the <code>toString</code> method.
 */

   public void testToString()
   {  Date creation = new Date(0);
      EntryNode entry = new EntryNode("name", 0, "file",
        "owner", creation, creation, 0, EntryCodes.CON,
        "permissions");

      Assert.assertEquals(entry.toString(), "name (container)");
   }

/**
 * Create a complex, hierarchical <code>EntryNode</code> with children 
 * and check the methods for getting parents and children.
 */

   public void testHierarchy()
   {
//
//   Create an ArrayList of EntryResults from which the hierarchical
//   EntryNode will be created.

      ArrayList entries = new ArrayList();

      String entryName = "/acd/name1";
      int    entryId = 0;
      String entryUri = "dummy";
      String owner = "owner";
      Date   creationDate = new Date(0);
      Date   expiryDate = new Date(0);
      long   creation = creationDate.getTime();
      long   expiry = expiryDate.getTime();
      int    size = 1;
      int    type = EntryCodes.VOT;
      String permissions = "permissions";

      EntryResults entry1 = new EntryResults();

      entry1.setEntryName(entryName);
      entry1.setEntryId(entryId);
      entry1.setEntryUri(entryUri);
      entry1.setOwnerId(owner);
      entry1.setCreationDate(creation);
      entry1.setExpiryDate(expiry);
      entry1.setSize(size);
      entry1.setType(type);
      entry1.setPermissionsMask(permissions);

      entries.add(entry1);


      entryName = "/acd/name2";
      type = EntryCodes.VOT;

      EntryResults entry2 = new EntryResults();

      entry2.setEntryName(entryName);
      entry2.setEntryId(entryId);
      entry2.setEntryUri(entryUri);
      entry2.setOwnerId(owner);
      entry2.setCreationDate(creation);
      entry2.setExpiryDate(expiry);
      entry2.setSize(size);
      entry2.setType(type);
      entry2.setPermissionsMask(permissions);

      entries.add(entry2);

//
//   Create the EntryNode and  perform tests.

      EntryNode node = new EntryNode("/acd/*", entries);

      Assert.assertEquals(node.getParent(), null);
      Assert.assertEquals(node.getType(),  EntryCodes.CON);

      StoreFile[] children = node.listFiles();
      int numChildren = Array.getLength(children);
      Assert.assertEquals(numChildren, 2);

      EntryNode firstChild = (EntryNode)children[0];

      Assert.assertEquals(firstChild.getName(), "/acd/name1");
      Assert.assertEquals(firstChild.getType(),  EntryCodes.VOT);

      EntryNode parent = (EntryNode)firstChild.getParent();
      Assert.assertEquals(parent, node);
   }


/**
 * Main method to run the class.
 */

   public static void main (String[] args)
   {
//    junit.textui.TestRunner.run (EntryNodeTest.class);
      junit.swingui.TestRunner.run (EntryNodeTest.class);
   }
}
