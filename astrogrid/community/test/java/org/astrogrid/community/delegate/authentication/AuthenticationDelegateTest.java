/*
 * $Id: AuthenticationDelegateTest.java,v 1.3 2003/09/16 11:07:51 pah Exp $
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

   public void testAuthenticateLogin() {
      
      try {
         outtoken = delegate.authenticateLogin("test@nowhere" , "password");
          assertNotNull(outtoken);
          assertEquals("account details incorrect", "test@nowhere", outtoken.getAccount()); // test that the account has been set up properly
          assertEquals("token already used when it should be new", false, outtoken.getUsed().booleanValue()); // test that the token is usable.
      
          // try again expecting login failure
      
          outtoken = delegate.authenticateLogin("test@nowhere" , "wrongpassword");
          assertNotNull(outtoken);
          assertEquals(true, outtoken.getUsed().booleanValue()); // check that the token is marked as used....
        }
      catch (RemoteException e) {
         throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + e);
      }
      assertEquals("test@nowhere", outtoken.getAccount());
      
      System.out.println("account" + outtoken.getAccount());
      System.out.println("expire " + DateFormat.getInstance().format(outtoken.getExpirationDate().getTime()));
      System.out.println("target" +outtoken.getTarget());
      System.out.println("start date "+ DateFormat.getInstance().format(outtoken.getStartDate().getTime()));
      System.out.println("token" + outtoken.getToken());
   }

   public void testAuthenticateToken() {
      SecurityToken outtoken;
      try {
         // login to get a token
         outtoken = delegate.authenticateLogin("test@nowhere" , "password");
         assertNotNull(outtoken);
         // authenticate the token
         SecurityToken newtoken = delegate.authenticateToken(outtoken);
         assertNotNull(newtoken);
         assertEquals("token failed to authenticate", false, newtoken.getUsed().booleanValue());
      }
      catch (RemoteException e) {
         throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + e);
         
      }
      assertEquals("test@nowhere", outtoken.getAccount());
   }

   public void testCreateToken() {
      try {
         // login to get a token
         outtoken = delegate.authenticateLogin("test@nowhere" , "password");
         assertNotNull(outtoken);
         SecurityToken newtoken = delegate.createToken("test@nowhere", outtoken, "target");
         assertNotNull(newtoken);
         assertEquals("token not valid",false, newtoken.getUsed().booleanValue() );
         assertEquals("target not set properly", "target", newtoken.getTarget());
         
         // test that the old token has been invalidated
         newtoken = delegate.authenticateToken(outtoken);
         assertEquals("old token still valid",true, newtoken.getUsed().booleanValue() );
      }
      catch (RemoteException e) {
         throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + e);
      }
     
   }

}
