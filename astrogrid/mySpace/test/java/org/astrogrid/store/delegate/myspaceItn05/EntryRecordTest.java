package org.astrogrid.store.delegate.myspaceItn05;

import junit.framework.*;
import java.util.*;

import org.astrogrid.store.delegate.myspaceItn05.EntryRecord;

/**
 * Junit tests for the <code>EntryRecord</code> class.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 5.
 */

public class EntryRecordTest extends TestCase
{

/**
 * Standard constructor for JUnit test classes.
 */

   public EntryRecordTest (String name)
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

      EntryRecord entry = new EntryRecord(entryName, entryId, entryUri,
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

      EntryRecord entry = new EntryRecord(entryName, entryId, entryUri,
        owner, creation, expiry, size, type, permissions);

      Assert.assertTrue(!entry.isFolder() );


      type = EntryCodes.CON;

      entry = new EntryRecord(entryName, entryId, entryUri,
        owner, creation, expiry, size, type, permissions);

      Assert.assertTrue(entry.isFolder() );
   }


/**
 * Test the <code>toString</code> method.
 */

   public void testToString()
   {  Date creation = new Date(0);
      EntryRecord entry = new EntryRecord("name", 0, "file",
        "owner", creation, creation, 0, EntryCodes.UNKNOWN,
        "permissions");

      Assert.assertEquals(entry.toString(),
       "name (unknown, created 01/01/70 01:00)");
   }

/**
 * Main method to run the class.
 */

   public static void main (String[] args)
   {
//    junit.textui.TestRunner.run (EntryRecordTest.class);
      junit.swingui.TestRunner.run (EntryRecordTest.class);
   }
}
