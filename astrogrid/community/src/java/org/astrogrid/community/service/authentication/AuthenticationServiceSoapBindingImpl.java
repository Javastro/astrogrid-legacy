/*
 * $Id: AuthenticationServiceSoapBindingImpl.java,v 1.1 2003/09/10 20:48:17 pah Exp $
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

import org.astrogrid.community.service.authentication.data.SecurityToken;

/**
 * Initial AuthenticationService Implementation. This is currently simply returning a static object with fixed values
 * 
 * @author pah
 * @version $Name:  $
 * @since iteration3
 */
public class AuthenticationServiceSoapBindingImpl
   implements org.astrogrid.community.service.authentication.TokenAuthenticator {
   private static SecurityToken testToken = new SecurityToken();

   static {
      testToken.setAccount("test@nowhere");
      testToken.setIdent("testIdent");
      testToken.setTarget("testTarget");

   }
   public SecurityToken authenticateLogin(
      java.lang.String account,
      java.lang.String password)
      throws java.rmi.RemoteException {
      setToken();
      return testToken;
   }

   public SecurityToken authenticateToken(SecurityToken token)
      throws java.rmi.RemoteException {
      setToken();
      return testToken;
   }

   public SecurityToken createToken(
      java.lang.String account,
      SecurityToken token,
      java.lang.String target)
      throws java.rmi.RemoteException {
      setToken();
      return testToken;
   }

   private void setToken() {
      Calendar cal = Calendar.getInstance();
      testToken.setToken(
         testToken.getAccount() + String.valueOf(System.currentTimeMillis()));
      testToken.setTodaysDate(cal);
      Calendar exp = (Calendar)cal.clone();
      exp.add(Calendar.HOUR_OF_DAY, 8);
      testToken.setExpirationDate(exp);

   }

}
