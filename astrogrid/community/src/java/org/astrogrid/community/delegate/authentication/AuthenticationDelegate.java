/*
 * $Id: AuthenticationDelegate.java,v 1.2 2003/09/10 20:48:17 pah Exp $
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

import javax.xml.rpc.ServiceException;

import org.astrogrid.community.delegate.authentication.TokenAuthenticator;
import org.astrogrid.community.service.authentication.data.SecurityToken;

/**
 * First Attempt delegate to hide some of the token authenticator soap stuff. 
 * A better delegate would hide more and be more tuned towards the needs of its users.
 * 
 * @author pah
 * @version $Name:  $
 * @since iteration3
 */
public class AuthenticationDelegate implements TokenAuthenticator {

   private AuthenticationServiceSoapBindingStub binding;

   /**
    * 
    */
   public AuthenticationDelegate() {
      try {
         binding = (AuthenticationServiceSoapBindingStub)new TokenAuthenticatorServiceLocator().getAuthenticationService();
      }
      catch (ServiceException e) {
         if (e.getLinkedCause() != null) {
            e.getLinkedCause().printStackTrace(); //TODO log this....
         }
         //TODO throw an exception?
      }
      binding.setTimeout(60000);
   }

   /* (non-Javadoc)
    * @see org.astrogrid.community.service.authentication.TokenAuthenticator#authenticateLogin(java.lang.String, java.lang.String)
    */
   public SecurityToken authenticateLogin(String account, String password)
      throws RemoteException {
         SecurityToken token =
         binding.authenticateLogin(account, password);
        return token;
   }

   /* (non-Javadoc)
    * @see org.astrogrid.community.service.authentication.TokenAuthenticator#authenticateToken(org.astrogrid.community.service.authentication.data.SecurityToken)
    */
   public SecurityToken authenticateToken(SecurityToken token)
      throws RemoteException {
         SecurityToken rettoken = binding.authenticateToken(token);
         return rettoken;
   }

   /* (non-Javadoc)
    * @see org.astrogrid.community.service.authentication.TokenAuthenticator#createToken(java.lang.String, org.astrogrid.community.service.authentication.data.SecurityToken, java.lang.String)
    */
   public SecurityToken createToken(
      String account,
      SecurityToken token,
      String target)
      throws RemoteException {
        SecurityToken rettoken = binding.createToken(account, token, target);
        return rettoken;
   }

}
