/*
 * $Id: TokenAuthenticationServerTest.java,v 1.2 2003/09/16 22:23:24 pah Exp $
 * 
 * Created on 12-Sep-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.community.authentication;

import junit.framework.AssertionFailedError;

import org.astrogrid.community.authentication.data.SecurityToken;
import org.astrogrid.community.common.db.HsqlDBInMemTestCase;
import org.astrogrid.community.policy.data.AccountData;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration3
 */
public class TokenAuthenticationServerTest extends HsqlDBInMemTestCase {

   private TokenAuthenticationServer tokenserver;
   private SecurityToken intoken, outtoken;

   /**
    * Constructor for TokenAuthenticationServerTest.
    * @param name
    */
   public TokenAuthenticationServerTest(String name) {
      super(name);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(TokenAuthenticationServerTest.class);
   }
   

   final public void testAuthenticateLogin() {
      outtoken = tokenserver.authenticateLogin("test@login" , "password");
      checkToken(outtoken);
      assertEquals("account details incorrect", "test@login", outtoken.getAccount()); // test that the account has been set up properly
      assertEquals("token already used when it should be new", false, outtoken.getUsed().booleanValue()); // test that the token is usable.
      
      // try again expecting login failure
      
      outtoken = tokenserver.authenticateLogin("test@login" , "wrongpassword");
      checkToken(outtoken);
      assertEquals(true, outtoken.getUsed().booleanValue()); // check that the token is marked as used....
      
      // try again expecting login failure
      
      outtoken = tokenserver.authenticateLogin("badaccount" , "password");
      checkToken(outtoken);
      assertEquals(true, outtoken.getUsed().booleanValue()); // check that the token is marked as used....
     
      
    
   }

   final public void testAuthenticateToken() {
      // login to get a token
      outtoken = tokenserver.authenticateLogin("test@authenticate" , "password3");
      checkToken(outtoken);
      // authenticate the token
      SecurityToken newtoken = tokenserver.authenticateToken(outtoken);
      checkToken(newtoken);
      assertEquals("token failed to authenticate", false, newtoken.getUsed().booleanValue());

   }
   
   public final void testTokenCreation(){
      // login to get a token
      outtoken = tokenserver.authenticateLogin("test@create" , "password2");
      checkToken(outtoken);
      SecurityToken newtoken = tokenserver.createToken("test@create", outtoken, "target");
      checkToken(newtoken);
      assertEquals("token not valid",false, newtoken.getUsed().booleanValue() );
      assertEquals("target not set properly", "target", newtoken.getTarget());
      
      // test that the old token has not been invalidated
      newtoken = tokenserver.authenticateToken(outtoken);
      assertEquals("login token not still valid",false, newtoken.getUsed().booleanValue() );
     
     
   }

   
   final public void testLoadAccount()
   {
      AccountData account = tokenserver.loadAccount("test@login");
      assertNotNull("no account object found", account);
      assertEquals("password string not as expected", "password", account.getPassword());
    
     
   }
  /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      tokenserver = TokenAuthenticationServer.getInstance();
      assertNotNull(tokenserver);
      
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
