package org.astrogrid.myspace.delegate;

import junit.framework.*;
import java.util.*;

import org.astrogrid.myspace.delegate.EntryResults;

/**
 * Junit tests for the <code>EntryResults</code> class.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 5.
 */

public class EntryResultsTest extends TestCase
{

/**
 * Standard constructor for JUnit test classes.
 */

   public EntryResultsTest (String name)
   {  super(name);
   }

/**
 * Test the <code>get</code> methods.
 */

   public void testGets()
   {  String entryName = "name";
      int    entryId = 0;
      String entryUri = "http://blue.nowhere.org/f1";
      String owner = "owner";
      Date   creationDate = new Date(0);
      Date   expiryDate = new Date(0);
      long   creation = creationDate.getTime();
      long   expiry = expiryDate.getTime();
      int    size = 1;
      int    type = EntryCodes.VOT;
      String permissions = "permissions";

      EntryResults entry = new EntryResults();

      entry.setEntryName(entryName);
      entry.setEntryId(entryId);
      entry.setEntryUri(entryUri);
      entry.setOwnerId(owner);
      entry.setCreationDate(creation);
      entry.setExpiryDate(expiry);
      entry.setSize(size);
      entry.setType(type);
      entry.setPermissionsMask(permissions);

      Assert.assertEquals(entry.getEntryName(), entryName);
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
 * Main method to run the class.
 */

   public static void main (String[] args)
   {
//    junit.textui.TestRunner.run (EntryResultsTest.class);
      junit.swingui.TestRunner.run (EntryResultsTest.class);
   }
}
