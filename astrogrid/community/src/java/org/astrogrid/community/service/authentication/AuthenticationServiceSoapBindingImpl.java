/*
 * $Id: AuthenticationServiceSoapBindingImpl.java,v 1.2 2003/09/16 11:07:51 pah Exp $
 * 
 * Created on 08-Sep-2003 by pah
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.community.service.authentication;

import java.util.Calendar;


import org.astrogrid.community.authentication.TokenAuthenticationServer;
import org.astrogrid.community.authentication.data.SecurityToken;

/**
 * AuthenticationService Implementation. This implements the service by calling the @link org.astrogrid.community.authentication.TokenAuthenticationServer .
 * 
 * @author pah
 * @version $Name:  $
 * @since iteration3
 */
public class AuthenticationServiceSoapBindingImpl
   implements org.astrogrid.community.service.authentication.TokenAuthenticator {

   private static TokenAuthenticationServer server = TokenAuthenticationServer.getInstance();

   public org.astrogrid.community.service.authentication.data.SecurityToken authenticateLogin(
      java.lang.String account,
      java.lang.String password)
      throws java.rmi.RemoteException {
      SecurityToken rettoken = server.authenticateLogin(account, password);
      return rettoken.createSoapToken();
   }

   public org.astrogrid.community.service.authentication.data.SecurityToken authenticateToken(org.astrogrid.community.service.authentication.data.SecurityToken token)
      throws java.rmi.RemoteException {
         SecurityToken intoken = SecurityToken.createFromSoapToken(token);
         SecurityToken outtoken = server.authenticateToken(intoken);
         
         return outtoken.createSoapToken();
         
 
   }

   public org.astrogrid.community.service.authentication.data.SecurityToken createToken(
      java.lang.String account,
      org.astrogrid.community.service.authentication.data.SecurityToken token,
      java.lang.String target)
      throws java.rmi.RemoteException {
         SecurityToken intoken = SecurityToken.createFromSoapToken(token);
         SecurityToken outoken = server.createToken(account, intoken, target);
         return outoken.createSoapToken();

   }
   

 
}
