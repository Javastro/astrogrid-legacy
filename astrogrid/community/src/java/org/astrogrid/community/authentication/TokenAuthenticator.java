/*
 * $Id: TokenAuthenticator.java,v 1.1 2003/09/10 20:48:17 pah Exp $
 * 
 * Created on 05-Sep-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 * 
 */ 

package org.astrogrid.community.authentication;

import org.astrogrid.community.authentication.data.SecurityToken;

/**
 * The basic token based authentication mechanism. This provides the methods that are set out in
 * <a href="http://wiki.astrogrid.org/bin/view/Astrogrid/AuthenticationItn3Final">AuthenticationItn3Final</a>
 * 
 * @author pah
 * @version $Name:  $
 * @since iteration3
 */
public interface TokenAuthenticator {
   
   /**
    * Performs an authentication with a simple username password pair.
    * @param account Normally a uniqueusername@community ex: kmb@mssl. Not necessarily a username it could be a system name registry@mssl 
    * @param password
    * @return a token that can be stored in the session scope of the calling web application, so that the password does not have to be stored there.
    */
   public SecurityToken authenticateLogin(String account,String password) ;
   
   /**
    * Checks whether a login token returned by @link #authenticateLogin is still valid. It returns a new one-time token.
    * @param token
    * @return whether the login token is still valid.
    */
   public SecurityToken authenticateToken(SecurityToken token);
   

   /**
    * 
    * @param account
    * @param token
    * @param target - A way of making the token unique to a particular service
    * @return a new security token
    */
   public SecurityToken createToken(String account, SecurityToken token, String target);

}
