/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/policy/manager/Attic/PermissionManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/22 13:03:04 $</cvs:date>
 * <cvs:version>$Revision: 1.10 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PermissionManagerImpl.java,v $
 *   Revision 1.10  2004/11/22 13:03:04  jdt
 *   Merges from Comm_KMB_585
 *
 *   Revision 1.9  2004/10/29 15:50:05  jdt
 *   merges from Community_AdminInterface (bug 579)
 *
 *   Revision 1.8.18.2  2004/10/18 22:10:28  KevinBenson
 *   some bug fixes to the PermissionManager.  Also made it throw some exceptions.
 *   Made  it and GroupManagerImnpl use the Resolver objects to actually get a group(PermissionManageriMnpl)
 *   or account (GroupMember) from the other community.  Changed also for it to grab a ResourceData from the
 *   database to verifity it is in our database.  Add a few of these resolver dependencies as well.
 *   And last but not least fixed the GroupMemberData object to get rid of a few set methods so Castor
 *   will now work correctly in Windows
 *
 *   Revision 1.8.18.1  2004/10/15 10:13:51  KevinBenson
 *   adding the admin interface into a jsp fashion.  Correcting a few mistakes on the other
 *   java files.
 *
 *   Revision 1.8  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.7.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.7  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.6.54.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.policy.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.util.Vector;
import java.util.Collection ;

import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.ObjectNotFoundException ;
import org.exolab.castor.jdo.DuplicateIdentityException ;

import org.exolab.castor.persist.spi.Complex ;

import org.astrogrid.community.common.policy.data.PolicyPermission ;
import org.astrogrid.community.common.policy.data.ResourceIdent ;
import org.astrogrid.community.common.policy.data.CommunityIdent ;
import org.astrogrid.community.common.policy.data.GroupData ;
import org.astrogrid.community.common.policy.data.ResourceData ;
import org.astrogrid.community.resolver.policy.manager.PolicyManagerResolver;
import org.astrogrid.community.common.identifier.ResourceIdentifier ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;

import org.astrogrid.community.common.policy.manager.PermissionManager ;
import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate ;

import org.astrogrid.community.server.service.CommunityServiceImpl ;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

import org.astrogrid.community.resolver.exception.CommunityResolverException ;
import org.astrogrid.community.common.exception.CommunityResourceException ;

import org.astrogrid.registry.RegistryException;


public class PermissionManagerImpl
    extends CommunityServiceImpl
    implements PermissionManager
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(PermissionManagerImpl.class);

    /**
     * Public constructor, using default database configuration.
     *
     */
    public PermissionManagerImpl()
        {
        super() ;
        }

    /**
     * Public constructor, using specific database configuration.
     *
     */
    public PermissionManagerImpl(DatabaseConfiguration config)
        {
        super(config) ;
        }

    /**
     * Public constructor, using a parent service.
     *
     */
    public PermissionManagerImpl(CommunityServiceImpl parent)
        {
        super(parent) ;
        }
    
    
    /**
     * Reference to our local AccountManager.
     * The GroupManager needs access to the current AccountManagerImpl because Castor maintains an
     * in-memory cache of AccountData objects, with read-write locks.
     *
     */
    private GroupManagerImpl groupManager ;
    
    /**
     * Reference to our local AccountManager.
     * The GroupManager needs access to the current AccountManagerImpl because Castor maintains an
     * in-memory cache of AccountData objects, with read-write locks.
     *
     */
    private ResourceManagerImpl resourceManager ;
    

    /**
     * Public constructor, using a parent service and an AccountManager instance.
     * @param parent A parent CommunityServiceImpl.
     * @param accountManager An AccountManager instance.
     * The GroupManager needs access to the current AccountManagerImpl because Castor maintains an
     * in-memory cache of AccountData objects, with read-write locks.
     *
     */
    public PermissionManagerImpl(CommunityServiceImpl parent,GroupManagerImpl groupManager, ResourceManagerImpl resourceManager)
        {
        super(parent) ;
        this.groupManager = groupManager;
        this.resourceManager = resourceManager;
        }
    

    /**
     * Create a new PolicyPermission.
     *
     */
    public PolicyPermission addPermission(String resource, String group, String action)
    throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PermissionManagerImpl.addPermission()") ;
        log.debug("  resource : " + resource) ;
        log.debug("  group    : " + group) ;
        log.debug("  action   : " + action) ;

        Database         database   = null ;
        PolicyPermission permission = null ;
        //
        // Create a ResourceIdent for our Resource.
        //
        // Create a CommunityIdent for our Group.
        //CommunityIdent groupIdent = new CommunityIdent(groupName) ;
        //log.debug("  group    : " + groupIdent) ;
        
        if(null == resource) {
            throw new CommunityIdentifierException(
                    "Null resource"
                    ) ;            
        }
        
        // Check for null group.
        if (null == group)
            {
            throw new CommunityIdentifierException(
                "Null group"
                ) ;
            }
        
       //ResourceIdent resourceIdent = new ResourceIdent(resourceName) ;
       //ResourceIdentifier resourceIdent = new ResourceIdentifier(resourceName) ;        
       log.debug("  resource : " + resource) ;
                
       ResourceData rd = null;
       
       try {
           rd = resourceManager.getResource(resource);
       }catch(CommunityResourceException cre) {
           throw new CommunityPolicyException(
                   "Could not get the resource = " + resource,resource
                   ) ;
       }
        
       CommunityIvornParser cip = new CommunityIvornParser(group);
       GroupData gd = null;
       if(cip.isLocal()) {
           gd = groupManager.getGroup(group);
       }else {
           try {
               PolicyManagerResolver pmr = new PolicyManagerResolver();               
               PolicyManagerDelegate pmd = pmr.resolve(cip);
               gd = pmd.getGroup(group);
           }catch(CommunityResolverException cre) {
               throw new CommunityServiceException(
                       "Could not resolve group = " + group,
                       cre
                       ) ;
           }catch(RegistryException re) {
               throw new CommunityServiceException(
                       "Could not resolve group = " + group,
                       re
                       ) ;            
           }
       }

       //Lets just double check, but the above statements should throw
       //an exception or give is a valid non-null GroupData       
       if(gd == null) {
           throw new CommunityServiceException(
                   "Could not find group = " + group);
       }
                 
       permission = new PolicyPermission() ;
       permission.setResource(rd.getIdent()) ;
       permission.setGroup(gd.getIdent()) ;
       permission.setAction(action) ;
       permission.setStatus(PolicyPermission.STATUS_PERMISSION_GRANTED) ;
       //
       // Try adding it to the database.
       try {
        //
        // Open our database connection.
        database = this.getDatabase() ;
                        //
                        // Begin a new database transaction.
                        database.begin();
                        //
                        // Try creating the permission.
                        database.create(permission);
                        //
                        // Commit the transaction.
                        database.commit() ;
                        }
                    //
                    // If we already have an object with that ident.
// TODO
// The only reason to treat this differently is that we might one day report it differently to the client.
                    catch (DuplicateIdentityException ouch)
                        {
                        //
                        // Log the exception.
                        logException(ouch, "PermissionManagerImpl.addPermission()") ;
                        //
                        // Set the response to null.
                        permission = null ;
                        
                        //
                        // Cancel the database transaction.
                        rollbackTransaction(database) ;                        
                        
                        //
                        // Throw a new Exception.
                        throw new CommunityPolicyException(
                            "Duplicate Group already exists",
                            group
                            ) ;                        
// TODO
// Need to rollback transaction
                        }
                    //
                    // If anything else went bang.
                    catch (Exception ouch)
                        {
                        //
                        // Log the exception.
                        logException(ouch, "PermissionManagerImpl.addPermission()") ;
                        //
                        // Set the response to null.
                        permission = null ;
                        
                        rollbackTransaction(database) ;
                        
                        throw new CommunityServiceException(
                                "Database transaction failed for permission",
                                group,
                                ouch
                                ) ;                        
// TODO
// Need to rollback transaction
                        }
                    //
                    // Close our database connection.
                    finally
                        {
                        closeConnection(database) ;
                        }
        // TODO
        // Need to return something to the client.
        // Possible a new DataObject ... ?
        log.debug("----\"----") ;
        return permission ;
        }

    /**
     * Request a PolicyPermission.
     *
     */
    public PolicyPermission getPermission(String resourceName, String groupName, String action)
    throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PermissionManagerImpl.getPermission()") ;
        log.debug("  resource : " + resourceName) ;
        log.debug("  group    : " + groupName) ;
        log.debug("  action   : " + action) ;

        Database         database = null ;
        PolicyPermission result   = null ;
        //
        // Create a ResourceIdent for our Resource.
        //ResourceIdent resourceIdent = new ResourceIdent(resourceName) ;
        //log.debug("  resource : " + resourceIdent) ;
        //
        // Create a CommunityIdent for our Group.
        //CommunityIdent groupIdent = new CommunityIdent(groupName) ;
        //log.debug("  group    : " + groupIdent) ;
        
        ResourceData rd = null;
        
        try {
            rd = resourceManager.getResource(resourceName);
        }catch(CommunityResourceException cre) {
            throw new CommunityPolicyException(
                    "Could not get the resource = " + resourceName,resourceName
                    ) ;
        }
        
        CommunityIvornParser cip = new CommunityIvornParser(groupName);
        
        
                    //
                    // Create the database key.
                    Complex key = new Complex(
                        new Object[]
                            {
                            rd.getIdent(),
                            cip.getAccountIdent(),
                            action
                            }
                        ) ;
                    //
                    // Try loading the PolicyPermission from the database.
                    try {
                        //
                        // Open our database connection.
                        database = this.getDatabase() ;
                        //
                        // Begin a new database transaction.
                        database.begin();
                        //
                        // Load the PolicyPermission from the database.
                        result = (PolicyPermission) database.load(PolicyPermission.class, key) ;
                        //
                        // Commit the transaction.
                        database.commit() ;
                        }
                    //
                    // If we couldn't find the object.
// TODO
// The only reason to treat this differently is that we might one day report it differently to the client.
                    catch (ObjectNotFoundException ouch)
                        {
                        //
                        // Log the exception.
                        logException(ouch, "PermissionManagerImpl.getPermission()") ;
                        //
                        // Set the response to null.
                        result = null ;
// TODO
// Need to rollback transaction
                        }
                    //
                    // If anything else went bang.
                    catch (Exception ouch)
                        {
                        //
                        // Log the exception.
                        logException(ouch, "PermissionManagerImpl.getPermission()") ;
                        //
                        // Set the response to null.
                        result = null ;
// TODO
// Need to rollback transaction
                        }
                    //
                    // Close our database connection.
                    finally
                        {
                        closeConnection(database) ;
                        }
        // TODO
        // Need to return something to the client.
        // Possible a new DataObject ... ?
        log.debug("----\"----") ;
        return result ;
        }

    /**
     * Update a PolicyPermission.
     *
     */
    public PolicyPermission setPermission(PolicyPermission permission)
    throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PermissionManagerImpl.setPermission()") ;
        log.debug("  resource : " + permission.getResource()) ;
        log.debug("  group    : " + permission.getGroup()) ;
        log.debug("  action   : " + permission.getAction()) ;
        
        System.out.println("PermissionManagerImpl.setPermission()") ;
        System.out.println("  resource : " + permission.getResource()) ;
        System.out.println("  group    : " + permission.getGroup()) ;
        System.out.println("  action   : " + permission.getAction()) ;        
        

        Database         database = null ;
        PolicyPermission result   = null ;
        //

        
        ResourceData rd = null;
        
        try {
            rd = resourceManager.getResource(permission.getResource());
        }catch(CommunityResourceException cre) {
            throw new CommunityPolicyException(
                    "Could not get the resource = " + permission.getResource(),permission.getResource()
                    ) ;
        }
        
        CommunityIvornParser cip = new CommunityIvornParser(permission.getGroup());
                    //
                    // Create the database key.
                    Complex key = new Complex(
                        new Object[]
                            {
                            rd.getIdent(),
                            cip.getAccountIdent(),
                            permission.getAction()
                            }
                        ) ;
                    //
                    // Try update the database.
                    try {
                        //
                        // Open our database connection.
                        database = this.getDatabase() ;
                        //
                        // Begin a new database transaction.
                        database.begin();
                        //
                        // Load the PolicyPermission from the database.
                        result = (PolicyPermission) database.load(PolicyPermission.class, key) ;
                        //
                        // Update the PolicyPermission data.
                        result.setStatus(permission.getStatus()) ;
                        result.setReason(permission.getReason()) ;
                        //
                        // Commit the transaction.
                        database.commit() ;
                        }
                    //
                    // If we couldn't find the object.
// TODO
// The only reason to treat this differently is that we might one day report it differently to the client.
                    catch (ObjectNotFoundException ouch)
                        {
                        //
                        // Log the exception.
                        logException(ouch, "PermissionManagerImpl.setPermission()") ;
                        //
                        // Set the response to null.
                        result = null ;
// TODO
// Need to rollback transaction
                        }
                    //
                    // If anything else went bang.
                    catch (Exception ouch)
                        {
                        //
                        // Log the exception.
                        logException(ouch, "PermissionManagerImpl.setPermission()") ;
                        //
                        // Set the response to null.
                        result = null ;
// TODO
// Need to rollback transaction
                        }
                    //
                    // Close our database connection.
                    finally
                        {
                        closeConnection(database) ;
                        }
                //

        // TODO
        // Need to return something to the client.
        // Possible a new DataObject ... ?
        log.debug("----\"----") ;
        return result ;
        }

    /**
     * Delete a PolicyPermission.
     *
     */
    public boolean delPermission(String resource, String group, String action)
    throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PermissionManagerImpl.delPermission()") ;
        log.debug("  resource : " + resource) ;
        log.debug("  group    : " + group) ;
        log.debug("  action   : " + action) ;

        Database         database = null ;
        PolicyPermission result   = null ;
        
        //
        // Create a ResourceIdent for our Resource.
        //
        // Create a CommunityIdent for our Group.
        //CommunityIdent groupIdent = new CommunityIdent(groupName) ;
        //log.debug("  group    : " + groupIdent) ;
        
        if(null == resource) {
            throw new CommunityIdentifierException(
                    "Null resource"
                    ) ;            
        }
        
        // Check for null group.
        if (null == group)
            {
            throw new CommunityIdentifierException(
                "Null group"
                ) ;
            }
        
       //ResourceIdent resourceIdent = new ResourceIdent(resourceName) ;
       //ResourceIdentifier resourceIdent = new ResourceIdentifier(resourceName) ;        
       log.debug("  resource : " + resource) ;
        
        
       ResourceData rd = null;
       
       try {
           rd = resourceManager.getResource(resource);
       }catch(CommunityResourceException cre) {
           throw new CommunityPolicyException(
                   "Could not get the resource = " + resource,resource
                   ) ;
       }
        
        
        //
        // Create a ResourceIdent for our Resource.
        //ResourceIdent resourceIdent = new ResourceIdent(resourceName) ;
        //log.debug("  resource : " + resourceIdent) ;
        //
        // Create a CommunityIdent for our Group.
        //CommunityIdent groupIdent = new CommunityIdent(groupName) ;
        //log.debug("  group    : " + groupIdent) ;

                Complex key = new Complex(
                    new Object[]
                        {
                        rd.getIdent(),
                        group,
                        action
                        }
                    ) ;
                //
                // Try update the database.
                try {
                    //
                    // Open our database connection.
                    database = this.getDatabase() ;
                    //
                    // Begin a new database transaction.
                    database.begin();
                    //
                    // Load the PolicyPermission from the database.
                    result = (PolicyPermission) database.load(PolicyPermission.class, key) ;
                    //
                    // Dete the PolicyPermission data.
                    database.remove(result) ;
                    //
                    // Commit the transaction.
                    database.commit() ;
                }
                //
                // If we couldn't find the object.
                // The only reason to treat this differently is that we might one day report it differently to the client.
                catch (ObjectNotFoundException ouch)
                    {
                    //
                    // Log the exception.
                    logException(ouch, "PermissionManagerImpl.delPermission()") ;
                    
//                  Cancel the database transaction.
                    rollbackTransaction(database) ;
                    //
                    // Throw a new Exception.
                    throw new CommunityPolicyException(
                        "Permission not found",
                        (resource + "|" + group + "|" + action)
                        ) ;
               }
                //
                // If anything else went bang.
                catch (Exception ouch)
                    {
                    //
                    // Log the exception.
                    logException(ouch, "PermissionManagerImpl.delPermission()") ;
                    
                    rollbackTransaction(database) ;
                    
                    throw new CommunityServiceException(
                            "Database transaction failed for permission",
                            group,
                            ouch
                            ) ;                         
                    
                }
                //
                // Close our database connection.
                finally
                    {
                    closeConnection(database) ;
                    }
        // TODO
        // Need to return something to the client.
        // Possible a new DataObject ... ?
        log.debug("----\"----") ;
        return (null != result) ;
        }
    
    /**
     * Request a list of Resources.
     * @return An array of ResourceData objects.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @todo Return empty array rather than null.
     *
     */
    public Object[] getPermissions()
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PermissionManagerImpl.getPermissions()") ;
        //
        // Try to query the database.
        Object[] array    = null ;
        Database database = null ;
        try {
            //
            // Open our database connection.
            database = this.getDatabase() ;
            //
            // Begin a new database transaction.
            database.begin();
            //
            // Create our OQL query.
            OQLQuery query = database.getOQLQuery(
                "SELECT permissions FROM org.astrogrid.community.common.policy.data.PolicyPermission permissions"
                );
            //
            // Execute our query.
            QueryResults results = query.execute();
            //
            // Transfer our results to a vector.
            Collection collection = new Vector() ;
            while (results.hasMore())
                {
                collection.add(
                    (PolicyPermission)results.next()
                    ) ;
                }
            //
            // Convert it into an array.
            array = collection.toArray() ;
            //
            // Commit the transaction.
            database.commit() ;
            }
        //
        // If anything went bang.
        catch (Exception ouch)
            {
            //
            // Log the exception.
            logException(ouch, "PermissionMangerImpl.getResources()") ;
            //
            // Set the response to null.
            array = null ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            }
        //
        // Close our database connection.
        finally
            {
            closeConnection(database) ;
            }
        //
        // Return the array.
        return array ;
        }
    
    
    }
