// package org.astrogrid.mySpace.mySpaceStatus;

import junit.framework.*;

import org.astrogrid.mySpace.mySpaceManager.UserAccount;

/**
 * Junit tests for the <code>UserAccount</code> class.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 2.
 */

public class TestUserAccount extends TestCase
{

/**
 * Standard constructor for JUnit test classes.
 */

   public TestUserAccount (String name)
   {  super(name);
   }

/**
 * Test the <code>checkAuthentication</code> method.  Note that in
 * Iteration 2 this method is hard-coded to return `true', so the
 * test is unlikely to fail.
 */

   public void testCheckAuthentication()
   {  UserAccount user = new UserAccount("jb", "jbcommunity");
      Assert.assertTrue(user.checkAuthentication() );
   }

/**
 * Test the <code>checkAuthorisation</code> method.  Note that in
 * Iteration 2 this method is hard-coded to return `true', so the
 * test is unlikely to fail.
 */

   public void testCheckAuthorisation()
   {  UserAccount user = new UserAccount("jb", "jbcommunity");
      Assert.assertTrue(user.checkAuthorisation(UserAccount.READ,
        "jb", "permissions") );
   }

/**
 * Test the <code>toString</code> method.
 */

   public void testToString()
   {  UserAccount user = new UserAccount("jb", "jbcommunity", "J.Bloggs");
      Assert.assertEquals(user.toString(), "jb:jbcommunity (J.Bloggs)");
   }

/**
 * Main method to run the class.
 */

   public static void main (String[] args)
   {
//    junit.textui.TestRunner.run (TestUserAccount.class);
      junit.swingui.TestRunner.run (TestUserAccount.class);
   }
}