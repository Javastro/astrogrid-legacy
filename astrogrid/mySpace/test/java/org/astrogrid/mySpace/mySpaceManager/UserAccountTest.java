package org.astrogrid.mySpace.mySpaceManager;

import junit.framework.*;

import org.astrogrid.mySpace.mySpaceManager.UserAccount;

/**
 * Junit tests for the <code>UserAccount</code> class.
 *
 * @author A C Davenhall (Edinburgh)
 * @since Iteration 3.
 * @version Iteration 5.
 */

public class UserAccountTest extends TestCase
{

/**
 * Standard constructor for JUnit test classes.
 */

   public UserAccountTest (String name)
   {  super(name);
   }

/**
 * Test the <code>getUserAGrId</code> method.
 */

   public void testGetUserAGrId()
   {  UserAccount user = new UserAccount("jb", "jbcommunity", "any");
      Assert.assertEquals(user.getUserAGrId(), "jb");
   }

/**
 * Test the <code>getBaseContainer</code> method.
 */

   public void testGetBaseContainer()
   {  UserAccount user = new UserAccount("jb", "jbcommunity", "any");
      Assert.assertEquals(user.getBaseContainer(), "/jb");
   }

/**
 * Test the <code>checkAuthorisation</code> method.  Note that in
 * Iteration 5 this method is hard-coded to return `true', so the
 * test is unlikely to fail.
 */

   public void testCheckAuthorisation()
   {  UserAccount user = new UserAccount("jb", "jbcommunity", "any");
      Assert.assertTrue(user.checkAuthorisation(UserAccount.READ,
        "jb", "permissions") );
   }

/**
 * Test the <code>checkSystemAuthorisation</code> method.
 */

   public void testCheckSystemAuthorisation()
   {  UserAccount user = new UserAccount("jb", "jbcommunity", "any");
      Assert.assertTrue(user.checkSystemAuthorisation(UserAccount.READ) );
   }

/**
 * Test the <code>checkCanModifyUsers</code> method.  Note that in
 * Iteration 5 this method is hard-coded to return `true', so the
 * test is unlikely to fail.
 */

   public void testCheckCanModifyUsers()
   {  UserAccount user = new UserAccount("jb", "jbcommunity", "any");
      Assert.assertTrue(user.checkCanModifyUsers() );
   }

/**
 * Test the <code>toString</code> method.
 */

   public void testToString()
   {  UserAccount user = new UserAccount("jb", "jbcommunity", "any",
        "J.Bloggs");
      Assert.assertEquals(user.toString(), "jb (J.Bloggs)");
   }

/**
 * Main method to run the class.
 */

   public static void main (String[] args)
   {
//    junit.textui.TestRunner.run (UserAccountTest.class);
      junit.swingui.TestRunner.run (UserAccountTest.class);
   }
}