/*
 * $Id: TokenAuthenticationServer.java,v 1.2 2003/09/15 21:51:45 pah Exp $
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

import java.util.Date;

import org.exolab.castor.jdo.ClassNotPersistenceCapableException;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.TransactionNotInProgressException;

import org.astrogrid.community.authentication.data.SecurityToken;
import org.astrogrid.community.common.db.CastorDatabaseFactory;
import org.astrogrid.community.policy.data.AccountData;



/**
 * A Singleton that manages the token authentication process.
 * @author pah
 * @version $Name:  $
 * @since iteration3
 */
public class TokenAuthenticationServer implements TokenAuthenticator {

   private Database database;

   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(
         TokenAuthenticationServer.class);

      
   private static TokenAuthenticationServer instance = null;
   /**
    * 
    */
   private TokenAuthenticationServer() {
      database = CastorDatabaseFactory.getInstance().getDatabase();
   
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
      
      AccountData theAccount = loadAccount(account);
      SecurityToken newtoken = SecurityToken.createInvalidToken(); 
      if (theAccount != null) {
         if(password != null && password.equals(theAccount.getPassword())){
            try {
               database.begin();
               newtoken = createToken(new SecurityToken(account,"login"));
            }
            catch (ClassNotPersistenceCapableException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
            catch (DuplicateIdentityException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
            catch (TransactionNotInProgressException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
            catch (PersistenceException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
            finally{
               try {
                  database.commit();
               }
               catch (TransactionNotInProgressException e1) {
                  // TODO Auto-generated catch block
                  e1.printStackTrace();
               }
               catch (TransactionAbortedException e1) {
                  // TODO Auto-generated catch block
                  e1.printStackTrace();
               }
            }
         }
      }
      return newtoken;
   }

   /* (non-Javadoc)
    * @see org.astrogrid.community.authentication.TokenAuthenticator#authenticateToken(org.astrogrid.community.authentication.data.SecurityToken)
    */
   public SecurityToken authenticateToken(SecurityToken token) {
      SecurityToken newtoken = SecurityToken.createInvalidToken();
      
      try {
         database.begin();
         newtoken = authenticateCreateToken(token);    
      }
      catch (PersistenceException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      finally{
         try {
            database.commit();
         }
         catch (TransactionNotInProgressException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
         }
         catch (TransactionAbortedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
         }
      }
      
        
        
        return newtoken;
   }

   private SecurityToken authenticateCreateToken(SecurityToken token) {
       SecurityToken newtoken = null, oldtoken;
         //do some obvious checks without consulting the database
         if(!simpleTokenCheck(token))
         {
            return SecurityToken.createInvalidToken();
         }
         // check the token in the database
         try {
            oldtoken = (SecurityToken)database.load(SecurityToken.class, token.getToken());
            if(simpleTokenCheck(oldtoken))
            {
               if (oldtoken.equals(token)) {
                  newtoken = createToken(token);
               }
               else {
                  newtoken = SecurityToken.createInvalidToken();
               }
            }
            else
            {
               newtoken = SecurityToken.createInvalidToken();
            }
            
         }
         catch (TransactionNotInProgressException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         catch (ObjectNotFoundException e) {
           newtoken = SecurityToken.createInvalidToken();
         }
         catch (LockNotGrantedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         catch (PersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         return newtoken;
      
   }

   /* (non-Javadoc)
    * @see org.astrogrid.community.authentication.TokenAuthenticator#createToken(java.lang.String, org.astrogrid.community.authentication.data.SecurityToken, java.lang.String)
    */
   public SecurityToken createToken(
      String account,
      SecurityToken token,
      String target) {
         SecurityToken newtoken = SecurityToken.createInvalidToken();
         try {
            database.begin();
            newtoken = SecurityToken.createInvalidToken();
            newtoken = authenticateCreateToken(token);
            newtoken.setTarget(target);
            token.setUsed(Boolean.TRUE);
            
         }
         catch (PersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         finally
         {
            try {
               if(simpleTokenCheck(newtoken))
               {
                  database.commit();
               }
               else{
                  database.rollback();
               }
            }
            catch (TransactionNotInProgressException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
            catch (TransactionAbortedException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
            
         }
                 return newtoken;
   }
   AccountData loadAccount(String account)
   {
      OQLQuery query;
       QueryResults results;
       boolean foundAccount;
       AccountData theAccount=null;
      
         try {
            System.err.println("database transaction ="+ database.isActive());
             database.begin();               
            System.err.println("database transaction ="+ database.isActive());
             theAccount =(AccountData) database.load(AccountData.class, account) ;
            
         }
         catch (TransactionNotInProgressException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         catch (ObjectNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         catch (LockNotGrantedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         catch (PersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         finally {
            try {
               database.commit();
            }
            catch (TransactionNotInProgressException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
            catch (TransactionAbortedException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
         }
            
         
        
      System.err.println("database transaction after commit ="+ database.isActive());

       
       return theAccount;
   }
   
   SecurityToken createToken(SecurityToken intoken) throws ClassNotPersistenceCapableException, DuplicateIdentityException, TransactionNotInProgressException, PersistenceException
   {
      SecurityToken token; 
      token = new SecurityToken(intoken);
      database.create(token);

      return token;
   }
   /**
    * Helper method to check some token paramters without consulting database. Checks to see if the token is marked as
    * being used or not and checks to see if it has expired.
    * It would probabaly be better to issue separate return status for each of the conditions separately.
    * @param token
    * @return true if the token is potentially valid
    */
   private boolean simpleTokenCheck (SecurityToken token){
      boolean retval = true;
      if (token.getUsed().booleanValue() == true) {
         logger.info(token.toString() + " is marked as used");
          retval = false;
       }
       if(token.getExpirationDate().before(new Date()))
       {
          logger.info(token.toString() + " has expired");
          retval = false;
       }
      return retval;
   }
}
