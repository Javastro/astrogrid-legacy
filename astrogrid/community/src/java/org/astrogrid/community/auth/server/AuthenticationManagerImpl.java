/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/auth/server/Attic/AuthenticationManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: KevinBenson $</cvs:author>
 * <cvs:date>$Date: 2003/09/08 16:02:47 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AuthenticationManagerImpl.java,v $
 *   Revision 1.1  2003/09/08 16:02:47  KevinBenson
 *   finally an authentication piece that is building.
 *
 *   Revision 1.1  2003/09/08 11:01:35  KevinBenson
 *   A check in of the Authentication authenticateToken roughdraft and some changes to the groudata and community data
 *   along with an AdministrationDelegate
 *
 *   Revision 1.3  2003/09/04 23:33:05  dave
 *   Implemented the core account manager methods - needs data object to return results
 *
 *   Revision 1.2  2003/09/03 15:23:33  dave
 *   Split API into two services, PolicyService and PolicyManager
 *
 *   Revision 1.1  2003/09/03 06:39:13  dave
 *   Rationalised things into one set of SOAP stubs and one set of data objects for both client and server.
 *
 *   Revision 1.1  2003/08/28 17:33:56  dave
 *   Initial policy prototype
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.auth.server;

import org.exolab.castor.jdo.JDO;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.PersistenceException ;
import org.exolab.castor.jdo.ObjectNotFoundException ;
import org.exolab.castor.jdo.DatabaseNotFoundException ;
import org.exolab.castor.jdo.DuplicateIdentityException ;
import org.exolab.castor.jdo.TransactionNotInProgressException ;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException ;

import org.exolab.castor.util.Logger;
import java.io.IOException;

//import org.astrogrid.community.common.util.Community;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;

import org.astrogrid.community.delegate.policy.AdministrationDelegate;
import org.astrogrid.community.delegate.authentication.AuthenticationDelegate2;
import org.astrogrid.community.policy.data.CommunityData;

import org.exolab.castor.persist.spi.Complex ;
import org.astrogrid.community.policy.data.PolicyCredentials;
import org.astrogrid.community.policy.data.ServiceData;

import java.rmi.RemoteException;
import org.astrogrid.community.auth.data.SecurityToken;
import java.sql.Timestamp;


public class AuthenticationManagerImpl implements AuthenticationManager  {

   /**
    * Switch for our debug statements.
    *
    */
   protected static final boolean DEBUG_FLAG = true ;

   /**
    * The name of our system property to read the location of our JDO mapping from.
    *
    */
   private static final String MAPPING_CONFIG_PROPERTY = "org.astrogrid.policy.server.mapping" ;

   /**
    * The name of the system property to read the location of our database config.
    *
    */
   private static final String DATABASE_CONFIG_PROPERTY = "org.astrogrid.policy.server.database.config" ;

   /**
    * The name of the system property to read our database name from.
    *
    */
   private static final String DATABASE_NAME_PROPERTY = "org.astrogrid.policy.server.database.name" ;

   /**
    * Our log writer.
    *
    */
   private Logger logger = null ;

   /**
    * Our config files path.
    *
    */
   private String config = "" ;

   /**
    * Our JDO and XML mapping.
    *
    */
   private Mapping mapping = null ;

   /**
    * Our JDO engine.
    *
    */
   private JDO jdo = null ;

   /**
    * Our database connection.
    *
    */
   private Database database = null ;


   /**
    * Public constructor.
    *
    */
   public AuthenticationManagerImpl()
      {
      if (DEBUG_FLAG) System.out.println("") ;
      if (DEBUG_FLAG) System.out.println("----\"----") ;
      if (DEBUG_FLAG) System.out.println("AccountManagerImpl()") ;
      //
      // Initialise our service.
      this.init() ;

      if (DEBUG_FLAG) System.out.println("----\"----") ;
      }

   /**
    * Initialise our service.
    *
    */
   protected void init()
      {
      if (DEBUG_FLAG) System.out.println("") ;
      if (DEBUG_FLAG) System.out.println("----\"----") ;
      if (DEBUG_FLAG) System.out.println("AccountManagerImpl.init()") ;

      if (DEBUG_FLAG) System.out.println("    Mapping  : " + System.getProperty(MAPPING_CONFIG_PROPERTY)) ;
      if (DEBUG_FLAG) System.out.println("    Database : " + System.getProperty(DATABASE_CONFIG_PROPERTY)) ;
      if (DEBUG_FLAG) System.out.println("    Database : " + System.getProperty(DATABASE_NAME_PROPERTY)) ;

      try {
         //
         // Create our log writer.
         logger = new Logger(System.out).setPrefix("castor");
         //
         // Load our object mapping.
         mapping = new Mapping(getClass().getClassLoader());
         mapping.loadMapping(System.getProperty(MAPPING_CONFIG_PROPERTY));

         //
         // Create our JDO engine.
         jdo = new JDO();
         jdo.setLogWriter(logger);
         jdo.setConfiguration(System.getProperty(DATABASE_CONFIG_PROPERTY));
         jdo.setDatabaseName(System.getProperty(DATABASE_NAME_PROPERTY));
         //
         // Create our database connection.
         database = jdo.getDatabase();
         }
// TODO
// Need to do something with these ??
//
      catch(IOException ouch)
         {
         if (DEBUG_FLAG) System.out.println("IOException during initialisation.") ;
         if (DEBUG_FLAG) System.out.println(ouch) ;
         }

      catch(DatabaseNotFoundException ouch)
         {
         if (DEBUG_FLAG) System.out.println("DatabaseNotFoundException during initialisation.") ;
         if (DEBUG_FLAG) System.out.println(ouch) ;
         }

      catch(PersistenceException ouch)
         {
         if (DEBUG_FLAG) System.out.println("PersistenceException during initialisation.") ;
         if (DEBUG_FLAG) System.out.println(ouch) ;
         }

      catch(MappingException ouch)
         {
         if (DEBUG_FLAG) System.out.println("MappingException during initialisation.") ;
         if (DEBUG_FLAG) System.out.println(ouch) ;
         }

      if (DEBUG_FLAG) System.out.println("----\"----") ;
      if (DEBUG_FLAG) System.out.println("") ;
      }


   /**
    * Service health check.
    *
    */
   public String authenticateToken(String account,String token,String target)
      throws RemoteException {
         String newToken = null;
            //
            // Create our OQL query.
            QueryResults results = null;
            boolean foundToken = false;
            SecurityToken newSecurityToken = null;

            try {
               database.begin();               
               OQLQuery query = database.getOQLQuery(
                   "SELECT securitytoken FROM org.astrogrid.community.authentication.data.SecurityTokent securitytokent WHERE securitytoken.token = $1"
                   );
               //
               // Bind our query param.
               query.bind(token);
               //
               // Execute our query.
               results = query.execute();
               foundToken = results.hasMore();
            }catch (PersistenceException ouch)
               {
               if (DEBUG_FLAG) System.out.println("") ;
               if (DEBUG_FLAG) System.out.println("  ----") ;
               if (DEBUG_FLAG) System.out.println("PersistenceException in authenticateToken()") ;

               
               if (DEBUG_FLAG) System.out.println("  ----") ;
               if (DEBUG_FLAG) System.out.println("") ;
               return null;
            }

            // Iterate through our results.
            if(foundToken) {
               //
               // Get the next SecurityToken object.
               SecurityToken secToken = null;
               try {
                  secToken = (SecurityToken) results.next();
               }catch (PersistenceException ouch) {
                  if (DEBUG_FLAG) System.out.println("") ;
                  if (DEBUG_FLAG) System.out.println("  ----") ;
                  if (DEBUG_FLAG) System.out.println("PersistenceException in authenticateToken()") ;
   
                  
                  if (DEBUG_FLAG) System.out.println("  ----") ;
                  if (DEBUG_FLAG) System.out.println("") ;
                  return null;
               }

               

               if(!target.equals(secToken.getTarget())) {
                  System.out.println("Target does not match lets throw an exception;");
               }
                
               if(!secToken.getExpirationDate().after(new Timestamp(System.currentTimeMillis())) ) {
                  System.out.println("expiration date is before the current time throw exception");
               }
                
               if(secToken.getUsed().booleanValue()) {
                  System.out.println("throw an exception this token has already been used.");
               }
               if(secToken.getAccount().equals(account)) {
                  System.out.println("throw an exception this token is being authenticated by another account");
               }
               //
               System.out.println("----") ;
               System.out.println("SecurityToken :") ;
               System.out.println("  ToString        : " + secToken.toString()) ;
               System.out.println("----") ;
               
                
                

                try {
                   newSecurityToken = new SecurityToken(null,secToken.getAccount(),secToken.getTarget());
                   database.create(newSecurityToken);
                   newToken = newSecurityToken.getToken();
                   secToken.setUsed(new Boolean(true));
                   database.commit();
                   database.close();
                }catch(Exception e) {
                   
                }
             
                
            }else {
               //No token has been found.
               String accountCommName = "mssl.ucl.ac.uk";//Community.getCommunityNameFromString(account);
               String localCommName = "mssl.ucl.ac.uk";//Community.getCommunityName();
               String targetCommName = "mssl.ucl.ac.uk";//Community.getCommunityNameFromString(target);
               String commUrl = null;
               SecurityToken st = new SecurityToken();
               
               if(localCommName.equals(accountCommName)) {
                  System.out.println("throw exception token cannot be found");
               }else {
                  AdministrationDelegate adminDelegate = new AdministrationDelegate();
                  CommunityData commData = null;
                  try {
                     commData = adminDelegate.getCommunity(accountCommName);
                  }catch(Exception e) {
                     
                  }
                  
                  
                  if(commData == null) {
                     if(targetCommName != null && !localCommName.equals(targetCommName)) {
                        try {
                           commData = adminDelegate.getCommunity(targetCommName);
                        }catch(Exception e) {
                           
                        }
                             
                     }                     
                  }
                  if(commData != null) {
                     commUrl = commData.getManagerUrl();
                  }

                  if(commUrl != null) {
                     st.setAccount(account);
                     st.setTarget(target);
                     st.setUsed(new Boolean(true));
                     st.setToken(token);
                     try {
                        database.create(st);
                   
                                             AuthenticationDelegate2 authDelegate = new AuthenticationDelegate2();
                                             authDelegate.setCommunityURL(commUrl);
                                             newToken = authDelegate.authenticateToken(account,token,target);
                   
                                             st.setToken(newToken);
                                             st.setTarget(localCommName);
                                             st.setUsed(new Boolean(false));
                                             database.create(st);
                        
                     }catch(Exception e) {
                        
                     }
                     
                     
                     try {
                        database.commit();
                        database.close();
                     }catch(Exception e) {
                        
                     }
                                   
                  }
                  
               }
               return newToken;
            }
      return " ";   
      }
      
}