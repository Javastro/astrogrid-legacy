/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/ResourceManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/10 00:08:45 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ResourceManagerImpl.java,v $
 *   Revision 1.2  2003/09/10 00:08:45  dave
 *   Added getGroupMembers, ResourceIdent and JUnit tests for ResourceManager
 *
 *   Revision 1.1  2003/09/09 19:13:32  KevinBenson
 *   New resource managerr stuff
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server ;

import java.rmi.RemoteException ;

import java.util.Vector ;
import java.util.Collection ;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.PersistenceException ;
import org.exolab.castor.jdo.ObjectNotFoundException ;
import org.exolab.castor.jdo.DatabaseNotFoundException ;
import org.exolab.castor.jdo.DuplicateIdentityException ;
import org.exolab.castor.jdo.TransactionNotInProgressException ;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException ;

import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.ResourceData ;
import org.astrogrid.community.policy.data.ResourceIdent ;
import org.astrogrid.community.policy.data.CommunityConfig ;

public class ResourceManagerImpl
   implements ResourceManager
   {
   /**
    * Switch for our debug statements.
    *
    */
   protected static final boolean DEBUG_FLAG = true ;

   /**
    * Our database manager.
    *
    */
   private DatabaseManager databaseManager ;

   /**
    * Our database connection.
    *
    */
   private Database database ;

   /**
    * Public constructor.
    *
    */
   public ResourceManagerImpl()
      {
      }

   /**
    * Initialise our manager.
    *
    */
   public void init(DatabaseManager databaseManager)
      {
      //
      // Keep a reference to our database connection.
      this.databaseManager = databaseManager ;
      this.database = databaseManager.getDatabase() ;
      }

   /**
    * Create a new Resource.
    *
    */
   public ResourceData addResource(String name)
      throws RemoteException
      {
      if (DEBUG_FLAG) System.out.println("") ;
      if (DEBUG_FLAG) System.out.println("----\"----") ;
      if (DEBUG_FLAG) System.out.println("ResourceManagerImpl.addResource()") ;
      if (DEBUG_FLAG) System.out.println("  name  : " + name) ;

      ResourceData resource = null ;
      //
      // Create a ResourceIdent for our Resource.
      ResourceIdent ident = new ResourceIdent(name) ;
      if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;
      //
      // If the ident is valid.
      if (ident.isValid())
         {
         //
         // If the ident is local.
         if (ident.isLocal())
            {
            //
            // Create our new Resource object.
            resource = new ResourceData(ident.toString()) ;
            //
            //
            // Try performing our transaction.
            try {
               //
               // Begin a new database transaction.
               database.begin();
               //
               // Try creating the resource in the database.
               database.create(resource);
            }
            //
            // If we already have an object with that ident.
            catch (DuplicateIdentityException ouch)
               {
               if (DEBUG_FLAG) System.out.println("") ;
               if (DEBUG_FLAG) System.out.println("  ----") ;
               if (DEBUG_FLAG) System.out.println("DuplicateIdentityException in addResource()") ;

               resource = null ;

               if (DEBUG_FLAG) System.out.println("  ----") ;
               if (DEBUG_FLAG) System.out.println("") ;
               }
            //
            // If anything else went bang.
            catch (Exception ouch)
               {
               if (DEBUG_FLAG) System.out.println("") ;
               if (DEBUG_FLAG) System.out.println("  ----") ;
               if (DEBUG_FLAG) System.out.println("Exception in addResource()") ;

               //
               // Set the response to null.
               resource = null ;

               if (DEBUG_FLAG) System.out.println("  ----") ;
               if (DEBUG_FLAG) System.out.println("") ;
               }
            //
            // Commit the transaction.
            finally
               {
               try {
                  if (null != resource)
                     {
                     database.commit() ;
                     }
                  else {
                     database.rollback() ;
                     }
                  }
               catch (Exception ouch)
                  {
                  if (DEBUG_FLAG) System.out.println("") ;
                  if (DEBUG_FLAG) System.out.println("  ----") ;
                  if (DEBUG_FLAG) System.out.println("Exception in addResource() finally clause") ;

                  resource = null ;

                  if (DEBUG_FLAG) System.out.println("  ----") ;
                  if (DEBUG_FLAG) System.out.println("") ;
                  }
               }
            }
         //
         // If the ident is not local.
         else {
            resource = null ;
            }
         }
         //
         // If the ident is not valid.
      else {
         //
         resource = null ;
         }

      // TODO
      // Need to return something to the client.
      // Possible a new DataObject ... ResourceResult ?
      //

      if (DEBUG_FLAG) System.out.println("----\"----") ;
      return resource ;
      }

   /**
    * Request an Resource details.
    *
    */
   public ResourceData getResource(String name)
      throws RemoteException
      {
      if (DEBUG_FLAG) System.out.println("") ;
      if (DEBUG_FLAG) System.out.println("----\"----") ;
      if (DEBUG_FLAG) System.out.println("ResourceManagerImpl.getResource()") ;
      if (DEBUG_FLAG) System.out.println("  name  : " + name) ;

      ResourceData resource = null ;
      //
      // Create a ResourceIdent for our Resource.
      ResourceIdent ident = new ResourceIdent(name) ;
      if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;
      //
      // If the ident is valid.
      if (ident.isValid())
         {
         //
         // If the ident is local.
         if (ident.isLocal())
            {
            try {
               //
               // Begin a new database transaction.
               database.begin();
               //
               // Load the Resource from the database.
               resource = (ResourceData) database.load(ResourceData.class, ident.toString()) ;
               }
            //
            // If we couldn't find the object.
            catch (ObjectNotFoundException ouch)
               {
               if (DEBUG_FLAG) System.out.println("") ;
               if (DEBUG_FLAG) System.out.println("  ----") ;
               if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in getResource()") ;

               //
               // Set the response to null.
               resource = null ;

               if (DEBUG_FLAG) System.out.println("  ----") ;
               if (DEBUG_FLAG) System.out.println("") ;
               }
            //
            // If anything else went bang.
            catch (Exception ouch)
               {
               if (DEBUG_FLAG) System.out.println("") ;
               if (DEBUG_FLAG) System.out.println("  ----") ;
               if (DEBUG_FLAG) System.out.println("Exception in getResource()") ;

               //
               // Set the response to null.
               resource = null ;

               if (DEBUG_FLAG) System.out.println("  ----") ;
               if (DEBUG_FLAG) System.out.println("") ;
               }
            //
            // Commit the transaction.
            finally
               {
               try {
                  if (null != resource)
                     {
                     database.commit() ;
                     }
                  else {
                     database.rollback() ;
                     }
                  }
               catch (Exception ouch)
                  {
                  if (DEBUG_FLAG) System.out.println("") ;
                  if (DEBUG_FLAG) System.out.println("  ----") ;
                  if (DEBUG_FLAG) System.out.println("Exception in getResource() finally clause") ;

                  //
                  // Set the response to null.
                  resource = null ;

                  if (DEBUG_FLAG) System.out.println("  ----") ;
                  if (DEBUG_FLAG) System.out.println("") ;
                  }
               }
            }
         //
         // If the ident is not local.
         else {
            //
            // Set the response to null.
            resource = null ;
            }
         }
         //
         // If the ident is not valid.
      else {
         //
         // Set the response to null.
         resource = null ;
         }

      if (DEBUG_FLAG) System.out.println("----\"----") ;
      return resource ;
      }

   /**
    * Update an Resource details.
    *
    */
   public ResourceData setResource(ResourceData resource)
      throws RemoteException
      {
      if (DEBUG_FLAG) System.out.println("") ;
      if (DEBUG_FLAG) System.out.println("----\"----") ;
      if (DEBUG_FLAG) System.out.println("ResourceManagerImpl.setResource()") ;
      if (DEBUG_FLAG) System.out.println("  Resource") ;
      if (DEBUG_FLAG) System.out.println("    ident : " + resource.getIdent()) ;
      if (DEBUG_FLAG) System.out.println("    desc  : " + resource.getDescription()) ;

      //
      // Get the resource ident.
      ResourceIdent ident = new ResourceIdent(resource.getIdent()) ;
      //
      // If the ident is valid.
      if (ident.isValid())
         {
         //
         // If the ident is local.
         if (ident.isLocal())
            {
            //
            // Try update the database.
            try {
               //
               // Begin a new database transaction.
               database.begin();
               //
               // Load the Resource from the database.
               ResourceData data = (ResourceData) database.load(ResourceData.class, resource.getIdent()) ;
               //
               // Update the resource data.
               data.setDescription(resource.getDescription()) ;
               }
            //
            // If we couldn't find the object.
            catch (ObjectNotFoundException ouch)
               {
               if (DEBUG_FLAG) System.out.println("") ;
               if (DEBUG_FLAG) System.out.println("  ----") ;
               if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in setResource()") ;

               //
               // Set the response to null.
               resource = null ;

               if (DEBUG_FLAG) System.out.println("  ----") ;
               if (DEBUG_FLAG) System.out.println("") ;
               }
            //
            // If anything else went bang.
            catch (Exception ouch)
               {
               if (DEBUG_FLAG) System.out.println("") ;
               if (DEBUG_FLAG) System.out.println("  ----") ;
               if (DEBUG_FLAG) System.out.println("Exception in setResource()") ;

               //
               // Set the response to null.
               resource = null ;

               if (DEBUG_FLAG) System.out.println("  ----") ;
               if (DEBUG_FLAG) System.out.println("") ;
               }
            //
            // Commit the transaction.
            finally
               {
               try {
                  if (null != resource)
                     {
                     database.commit() ;
                     }
                  else {
                     database.rollback() ;
                     }
                  }
               catch (Exception ouch)
                  {
                  if (DEBUG_FLAG) System.out.println("") ;
                  if (DEBUG_FLAG) System.out.println("  ----") ;
                  if (DEBUG_FLAG) System.out.println("Exception in setResource() finally clause") ;

                  //
                  // Set the response to null.
                  resource = null ;

                  if (DEBUG_FLAG) System.out.println("  ----") ;
                  if (DEBUG_FLAG) System.out.println("") ;
                  }
               }
            }
         //
         // If the ident is not local.
         else {
            //
            // Set the response to null.
            resource = null ;
            }
         }
         //
         // If the ident is not valid.
      else {
         //
         // Set the response to null.
         resource = null ;
         }

      if (DEBUG_FLAG) System.out.println("----\"----") ;
      return resource ;
      }

   /**
    * Request a list of Resources.
    *
    */
   public Object[] getResourceList()
      throws RemoteException
      {
      if (DEBUG_FLAG) System.out.println("") ;
      if (DEBUG_FLAG) System.out.println("----\"----") ;
      if (DEBUG_FLAG) System.out.println("ResourceManagerImpl.getResourceList()") ;

      //
      // Try to query the database.
      Object[] array = null ;
      try {
         //
         // Begin a new database transaction.
         database.begin();
         //
         // Create our OQL query.
         OQLQuery query = database.getOQLQuery(
            "SELECT resources FROM org.astrogrid.community.policy.data.ResourceData resources"
            );
         //
         // Execute our query.
         QueryResults results = query.execute();
         //
         // Transfer our results to a vector.
         Collection collection = new Vector() ;
         while (results.hasMore())
            {
            collection.add(results.next()) ;
            }
         //
         // Convert it into an array.
         array = collection.toArray() ;
         }
      //
      // If anything went bang.
      catch (Exception ouch)
         {
         if (DEBUG_FLAG) System.out.println("") ;
         if (DEBUG_FLAG) System.out.println("  ----") ;
         if (DEBUG_FLAG) System.out.println("Exception in getResourceList()") ;

         //
         // Set the response to null.
         array = null ;

         if (DEBUG_FLAG) System.out.println("  ----") ;
         if (DEBUG_FLAG) System.out.println("") ;
         }
      //
      // Commit the transaction.
      finally
         {
         try {
            if (null != array)
               {
               database.commit() ;
               }
            else {
               database.rollback() ;
               }
            }
         catch (Exception ouch)
            {
            if (DEBUG_FLAG) System.out.println("") ;
            if (DEBUG_FLAG) System.out.println("  ----") ;
            if (DEBUG_FLAG) System.out.println("Exception in getResourceList() finally clause") ;

            //
            // Set the response to null.
            array = null ;

            if (DEBUG_FLAG) System.out.println("  ----") ;
            if (DEBUG_FLAG) System.out.println("") ;
            }
         }

      if (DEBUG_FLAG) System.out.println("----\"----") ;
      return array ;
      }

   /**
    * Delete an Resource.
    *
    */
   public boolean delResource(String name)
      throws RemoteException
      {
      if (DEBUG_FLAG) System.out.println("") ;
      if (DEBUG_FLAG) System.out.println("----\"----") ;
      if (DEBUG_FLAG) System.out.println("ResourceManagerImpl.delResource()") ;
      if (DEBUG_FLAG) System.out.println("  name  : " + name) ;

      //
      // Create a ResourceIdent for our Resource.
      ResourceIdent ident = new ResourceIdent(name) ;
      if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;
      //
      // If the ident is valid.
      if (ident.isValid())
         {
         //
         // If the ident is local.
         if (ident.isLocal())
            {
            ResourceData resource = null ;
            try {
               //
               // Begin a new database transaction.
               database.begin();
               //
               // Load the Resource and Group from the database.
               resource = (ResourceData) database.load(ResourceData.class, ident.toString()) ;
               //
               // Delete the Resource and Group together.
               database.remove(resource) ;
//
// TODO
// Should remove the Resource even if the Group does not exist.
//
               }
            //
            // If we couldn't find the object.
            catch (ObjectNotFoundException ouch)
               {
               if (DEBUG_FLAG) System.out.println("") ;
               if (DEBUG_FLAG) System.out.println("  ----") ;
               if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in delResource()") ;

               //
               // Set the response to null.
               resource = null ;

               if (DEBUG_FLAG) System.out.println("  ----") ;
               if (DEBUG_FLAG) System.out.println("") ;
               }
            //
            // If anything else went bang.
            catch (Exception ouch)
               {
               if (DEBUG_FLAG) System.out.println("") ;
               if (DEBUG_FLAG) System.out.println("  ----") ;
               if (DEBUG_FLAG) System.out.println("Exception in delResource()") ;

               //
               // Set the response to null.
               resource = null ;

               if (DEBUG_FLAG) System.out.println("  ----") ;
               if (DEBUG_FLAG) System.out.println("") ;
               }
            //
            // Commit the transaction.
            finally
               {
               try {
                  if (null != resource)
                     {
                     database.commit() ;
                     }
                  else {
                     database.rollback() ;
                     }
                  }
               catch (Exception ouch)
                  {
                  if (DEBUG_FLAG) System.out.println("") ;
                  if (DEBUG_FLAG) System.out.println("  ----") ;
                  if (DEBUG_FLAG) System.out.println("Exception in delResource() finally clause") ;

                  //
                  // Set the response to null.
                  resource = null ;

                  if (DEBUG_FLAG) System.out.println("  ----") ;
                  if (DEBUG_FLAG) System.out.println("") ;
                  }
               }
            }
         //
         // If the ident is not local.
         else {
            //
            // Set the response to null.
            //
            }
         }
         //
         // If the ident is not valid.
      else {
         //
         // Set the response to null.
         //
         }

      if (DEBUG_FLAG) System.out.println("----\"----") ;

      return true ;
      }

   }
