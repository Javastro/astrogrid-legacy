/*
 * $Id: SimpleAuthenticator.java,v 1.1 2003/08/19 00:04:38 pah Exp $
 * 
 * Created on 18-Aug-2003 by pah
 *
 * Copyright ©2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.community.authentication;

/**
 * @author pah
 * @version $Name:  $
 * @since iteration3
 */
public class SimpleAuthenticator {
   
   /**
    * Performs an authentication with a simple username password pair.
    * @param userName
    * @param password
    * @return a token that can be stored in the session scope of the calling web application, so that the password does not have to be stored there.
    */
   public String authenticateLogin(String userName,String password) {return "TestValue";}
   
   /**
    * Checks whether a login token returned by @link authenticateLogin is still valid.
    * @param token
    * @return whether the login token is still valid.
    */
   public boolean authenticateToken(String token) { return true;}
   


}
