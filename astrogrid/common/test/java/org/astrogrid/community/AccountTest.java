/* $Id: AccountTest.java,v 1.2 2004/02/17 12:31:42 mch Exp $
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
 * Tests some of the common community classes - ie Account and community snippet processors
 *
 */

public class AccountTest extends TestCase
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
//      try {
//         snippet = CommunityMessage.getMessage("SomeToken","agroup","mecommunist");
//         fail("Should have complained that vo reference didn't contain '@'");
//      } catch (AssertionError ae) {} //ignore - should happen
         
      //check that parser barfs if bad vo reference given
      snippet = CommunityMessage.getMessage("SomeToken","me@community","agroup");
      snippet = snippet.replaceAll("me@community", "mecommunist");
//      try {
//         CommunityMessage.getIndividual(snippet);
//         fail("Should have complained that vo reference didn't contain '@'");
//      } catch (AssertionError ae) {} //ignore - should happen
            
   }

   /**
    * Tests user representation
    */
   public void testAccount()
   {
      //make user from snippet
      String snippet = CommunityMessage.getMessage("SomeToken","me@community","agroup");
      
      Account me = new Account(snippet);
      
      assertEquals("me", me.getIndividual());
      assertEquals("community", me.getCommunity());
      assertEquals("agroup", me.getGroup());
      assertEquals("SomeToken", me.getToken());
      assertEquals("me@community", me.getAstrogridId());
      
      snippet = CommunityMessage.getMessage("SomeToken","me@community","anothergroup");
      
      Account meToo = new Account(snippet);
      
      assertTrue(me.equals(meToo));
   }


    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(AccountTest.class);
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
$Log: AccountTest.java,v $
Revision 1.2  2004/02/17 12:31:42  mch
Fixes for removed snippet assertions

Revision 1.1  2004/02/16 22:44:08  mch
Tests for (datacenter) account/user representation

Revision 1.2  2004/02/14 17:26:20  mch
Fixed CVS tags

 */
