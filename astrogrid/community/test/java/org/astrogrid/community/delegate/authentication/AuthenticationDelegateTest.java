/*
 * $Id: AuthenticationDelegateTest.java,v 1.2 2003/09/15 21:51:45 pah Exp $
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

//TODO needs more tests
   private SecurityToken testinputoken;

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
      
      SecurityToken outtoken;
      try {
         outtoken = delegate.authenticateLogin("account", "password");
      }
      catch (RemoteException e) {
         throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + e);
      }
      assertEquals("test@nowhere", outtoken.getAccount());
      
      System.out.println("ident " + outtoken.getIdent());
      System.out.println("account" + outtoken.getAccount());
      System.out.println("expire " + DateFormat.getInstance().format(outtoken.getExpirationDate().getTime()));
      System.out.println("target" +outtoken.getTarget());
      System.out.println("today date "+ DateFormat.getInstance().format(outtoken.getTodaysDate().getTime()));
      System.out.println("token" + outtoken.getToken());
   }

   public void testAuthenticateToken() {
      SecurityToken outtoken;
      try {
          outtoken = delegate.authenticateToken(testinputoken);
      }
      catch (RemoteException e) {
         throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + e);
         
      }
      assertEquals("test@nowhere", outtoken.getAccount());
   }

   public void testCreateToken() {
   }

}
