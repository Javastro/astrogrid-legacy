/*
 * $Id: AuthenticationDelegateTest.java,v 1.4 2003/09/16 22:23:24 pah Exp $
 * 
 * Created on 10-Sep-2003 by pah
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.community.delegate.authentication;

import java.rmi.RemoteException;
import java.text.DateFormat;

import junit.framework.TestCase;

import org.astrogrid.community.service.authentication.data.SecurityToken;

/**
 * Rudimentaty tests for the Authentication Delegate. This must be run against an external service to work.
 * @author pah
 * @version $Name:  $
 * @since iteration3
 */
public class AuthenticationDelegateTest extends TestCase {

   private SecurityToken testinputoken;
   private SecurityToken outtoken, intoken;

   private AuthenticationDelegate delegate;

   /**
    * Constructor for AuthenticationDelegateTest.
    * @param name
    */
   public AuthenticationDelegateTest(String name) {
      super(name);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(AuthenticationDelegateTest.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      delegate = new AuthenticationDelegate();
      testinputoken = new SecurityToken();
   }

   /**
    * 
    */
   public void testAuthenticateLogin() {
      
      try {
         outtoken = delegate.authenticateLogin("test@login" , "password");
         checkToken(outtoken);
          assertEquals("account details incorrect", "test@login", outtoken.getAccount()); // test that the account has been set up properly
          assertEquals("token already used when it should be new", false, outtoken.getUsed().booleanValue()); // test that the token is usable.
          assertTrue("startdate and expiry are equal", !outtoken.getStartDate().equals(outtoken.getExpirationDate()));
          assertTrue("expiry date before start date", outtoken.getStartDate().before(outtoken.getExpirationDate()));
         System.out.println("account =" + outtoken.getAccount());
         System.out.println("expire =" + DateFormat.getInstance().format(outtoken.getExpirationDate().getTime()));
         System.out.println("target =" +outtoken.getTarget());
         System.out.println("start date= "+ DateFormat.getInstance().format(outtoken.getStartDate().getTime()));
         System.out.println("token=" + outtoken.getToken());
         // try again expecting login failure
      
         outtoken = delegate.authenticateLogin("test@login" , "wrongpassword");
        checkToken(outtoken);
         assertEquals(true, outtoken.getUsed().booleanValue()); // check that the token is marked as used....
         // try again expecting login failure
      
         outtoken = delegate.authenticateLogin("wrongaccount" , "password");
        checkToken(outtoken);
         assertEquals(true, outtoken.getUsed().booleanValue()); // check that the token is marked as used....
        }
      catch (RemoteException e) {
         throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + e);
      }
      
   }

   public void testAuthenticateToken() {
      SecurityToken outtoken;
      try {
         // login to get a token
         outtoken = delegate.authenticateLogin("test@authenticate" , "password3");
         checkToken(outtoken);
         // authenticate the token
         SecurityToken newtoken = delegate.authenticateToken(outtoken);
         checkToken(newtoken);
         assertEquals("token failed to authenticate account"+newtoken.getAccount()+"-", false, newtoken.getUsed().booleanValue());
      }
      catch (RemoteException e) {
         throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + e);
         
      }
   }

   public void testCreateToken() {
      try {
         // login to get a token
         outtoken = delegate.authenticateLogin("test@create" , "password2");
         checkToken(outtoken);
         SecurityToken newtoken = delegate.createToken("test@create", outtoken, "target");
         checkToken(newtoken);
         assertEquals("token not valid",false, newtoken.getUsed().booleanValue() );
         assertEquals("target not set properly", "target", newtoken.getTarget());
         
         // test that the old token had not been invalidated
         newtoken = delegate.authenticateToken(outtoken);
         assertEquals("login token has been incorrectly invalidated",false, newtoken.getUsed().booleanValue() );
      }
      catch (RemoteException e) {
         throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + e);
      }
     
   }
   private void checkToken(SecurityToken intoken) {
      assertNotNull(intoken);
        assertNotNull(intoken.getAccount());
        assertNotNull(intoken.getExpirationDate());
        assertNotNull(intoken.getStartDate());
        assertNotNull(intoken.getTarget());
        assertNotNull(intoken.getToken());
        assertNotNull(intoken.getUsed());      
   }

}
