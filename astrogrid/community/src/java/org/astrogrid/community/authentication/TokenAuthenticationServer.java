/*
 * $Id: TokenAuthenticationServer.java,v 1.1 2003/09/10 20:48:17 pah Exp $
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

package org.astrogrid.community.authentication;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDO;
import org.exolab.castor.mapping.Mapping;

import org.astrogrid.community.authentication.data.SecurityToken;

/**
 * A Singleton that manages the token authentication process.
 * @author pah
 * @version $Name:  $
 * @since iteration3
 */
public class TokenAuthenticationServer implements TokenAuthenticator {

   private Database database;

   private JDO jdo;

      
   private static TokenAuthenticationServer instance = null;
   /**
    * 
    */
   private TokenAuthenticationServer() {
//      // Load our object mapping.
//       mapping = new Mapping(getClass().getClassLoader());
//       mapping.loadMapping(MAPPING_CONFIG_PROPERTY);
//
//       //
//       // Create our JDO engine.
//       jdo = new JDO();
//       jdo.setLogWriter(logger);
//       jdo.setConfiguration(DATABASE_CONFIG_PROPERTY);
//       jdo.setDatabaseName(DATABASE_NAME_PROPERTY);
//       //
//       // Create our database connection.
//       database = jdo.getDatabase();
//     
   
   }

   public static TokenAuthenticationServer getInstance()
   {
      if (instance == null) {
         instance = new TokenAuthenticationServer();
         
      }

      return instance;
   }
   
   /* (non-Javadoc)
    * @see org.astrogrid.community.authentication.TokenAuthenticator#authenticateLogin(java.lang.String, java.lang.String)
    */
   public SecurityToken authenticateLogin(String account, String password) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("TokenAuthenticationManager.authenticateLogin() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.community.authentication.TokenAuthenticator#authenticateToken(org.astrogrid.community.authentication.data.SecurityToken)
    */
   public SecurityToken authenticateToken(SecurityToken token) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("TokenAuthenticationManager.authenticateToken() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.community.authentication.TokenAuthenticator#createToken(java.lang.String, org.astrogrid.community.authentication.data.SecurityToken, java.lang.String)
    */
   public SecurityToken createToken(
      String account,
      SecurityToken token,
      String target) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("TokenAuthenticationManager.createToken() not implemented");
   }

}
