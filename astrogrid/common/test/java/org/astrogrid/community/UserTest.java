/* $Id: UserTest.java,v 1.3 2004/02/17 03:45:06 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid.community;


import java.io.IOException;
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.community.common.util.CommunityMessage;

/**
 * Tests the common community classes - ie User and community snippet processors
 *
 */

public class UserTest extends TestCase
{

   /**
    * Tests snippet processing.  Tests it against itself
    *
    */
   public void testSnippet()
   {
      String snippet = CommunityMessage.getMessage("SomeToken","me@community","agroup");
      
      assertEquals("me@community", CommunityMessage.getAccount(snippet));
//      assertEquals("me", CommunityMessage.getIndividual(snippet));
//      assertEquals("community", CommunityMessage.getCommunity(snippet));
      assertEquals("agroup", CommunityMessage.getGroup(snippet));
      assertEquals("SomeToken", CommunityMessage.getToken(snippet));

      //check that message maker barfs if bad vo reference given
      try {
         snippet = CommunityMessage.getMessage("SomeToken","agroup","mecommunist");
         fail("Should have complained that vo reference didn't contain '@'");
      } catch (AssertionError ae) {} //ignore - should happen
         
      //check that parser barfs if bad vo reference given
      snippet = CommunityMessage.getMessage("SomeToken","me@community","agroup");
      snippet = snippet.replaceAll("me@community", "mecommunist");
      try {
//         CommunityMessage.getIndividual(snippet);
         fail("Should have complained that vo reference didn't contain '@'");
      } catch (AssertionError ae) {} //ignore - should happen
            
   }

   /**
    * Tests user representation
    */
   public void testUser()
   {
      //make user from snippet
      String snippet = CommunityMessage.getMessage("SomeToken","me@community","agroup");
      
      User me = new User(snippet);
      
//      assertEquals("me", me.getIndividual());
      assertEquals("community", me.getCommunity());
      assertEquals("agroup", me.getGroup());
      assertEquals("SomeToken", me.getToken());
//      assertEquals("me@community", me.getIvoRef());
      
      snippet = CommunityMessage.getMessage("SomeToken","me@community","anothergroup");
      
      User meToo = new User(snippet);
      
      assertTrue(me.equals(meToo));
   }


    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(UserTest.class);
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
$Log: UserTest.java,v $
Revision 1.3  2004/02/17 03:45:06  mch
Fixes for Pauls revert-to-doesnt-break-everyones User

Revision 1.2  2004/02/14 17:26:20  mch
Fixed CVS tags

 */
