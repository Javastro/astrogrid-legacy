/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/policy/manager/Attic/PermissionManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PermissionManagerImpl.java,v $
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

//import java.rmi.RemoteException ;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.ObjectNotFoundException ;
import org.exolab.castor.jdo.DuplicateIdentityException ;

import org.exolab.castor.persist.spi.Complex ;

import org.astrogrid.community.common.policy.data.PolicyPermission ;
import org.astrogrid.community.common.policy.data.ResourceIdent ;
import org.astrogrid.community.common.policy.data.CommunityIdent ;

import org.astrogrid.community.common.policy.manager.PermissionManager ;

import org.astrogrid.community.server.service.CommunityServiceImpl ;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;

public class PermissionManagerImpl
    extends CommunityServiceImpl
    implements PermissionManager
    {
    /**
     * Switch for our debug statements.
     *
     */
    protected static final boolean DEBUG_FLAG = true ;

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
     * Create a new PolicyPermission.
     *
     */
    public PolicyPermission addPermission(String resourceName, String groupName, String action)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PermissionManagerImpl.addPermission()") ;
        if (DEBUG_FLAG) System.out.println("  resource : " + resourceName) ;
        if (DEBUG_FLAG) System.out.println("  group    : " + groupName) ;
        if (DEBUG_FLAG) System.out.println("  action   : " + action) ;

        Database         database   = null ;
        PolicyPermission permission = null ;
        //
        // Create a ResourceIdent for our Resource.
        ResourceIdent resourceIdent = new ResourceIdent(resourceName) ;
        if (DEBUG_FLAG) System.out.println("  resource : " + resourceIdent) ;
        //
        // Create a CommunityIdent for our Group.
        CommunityIdent groupIdent = new CommunityIdent(groupName) ;
        if (DEBUG_FLAG) System.out.println("  group    : " + groupIdent) ;
        //
        // If the resource ident is valid.
        if (resourceIdent.isValid())
            {
            //
            // If the resource ident is local.
            if (resourceIdent.isLocal())
                {
                //
                // If the group ident is valid.
                if (groupIdent.isValid())
                    {
                    //
                    // Create our new PolicyPermission.
                    permission = new PolicyPermission() ;
                    permission.setResource(resourceIdent.toString()) ;
                    permission.setGroup(groupIdent.toString()) ;
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
// TODO
// Need to rollback transaction
                        }
                    //
                    // Close our database connection.
                    finally
                        {
                        closeConnection(database) ;
                        }
                    }
                //
                // If the group ident is not valid.
                else {
                    //
                    // Set the response to null.
                    permission = null ;
                    }
                }
            //
            // If the resource is not local.
            else {
                //
                // Set the response to null.
                permission = null ;
                }
            }
            //
            // If the resource is not valid.
        else {
            //
            // Set the response to null.
            permission = null ;
            }

        // TODO
        // Need to return something to the client.
        // Possible a new DataObject ... ?
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return permission ;
        }

    /**
     * Request a PolicyPermission.
     *
     */
    public PolicyPermission getPermission(String resourceName, String groupName, String action)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PermissionManagerImpl.getPermission()") ;
        if (DEBUG_FLAG) System.out.println("  resource : " + resourceName) ;
        if (DEBUG_FLAG) System.out.println("  group    : " + groupName) ;
        if (DEBUG_FLAG) System.out.println("  action   : " + action) ;

        Database         database = null ;
        PolicyPermission result   = null ;
        //
        // Create a ResourceIdent for our Resource.
        ResourceIdent resourceIdent = new ResourceIdent(resourceName) ;
        if (DEBUG_FLAG) System.out.println("  resource : " + resourceIdent) ;
        //
        // Create a CommunityIdent for our Group.
        CommunityIdent groupIdent = new CommunityIdent(groupName) ;
        if (DEBUG_FLAG) System.out.println("  group    : " + groupIdent) ;

        //
        // If the resource ident is valid.
        if (resourceIdent.isValid())
            {
            //
            // If the resource ident is local.
            if (resourceIdent.isLocal())
                {
                //
                // If the group ident is valid.
                if (groupIdent.isValid())
                    {
                    //
                    // Create the database key.
                    Complex key = new Complex(
                        new Object[]
                            {
                            resourceIdent.toString(),
                            groupIdent.toString(),
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
                    }
                //
                // If the group ident is not valid.
                else {
                    //
                    // Set the response to null.
                    result = null ;
                    }
                }
            //
            // If the resource is not local.
            else {
                //
                // Set the response to null.
                result = null ;
                }
            }
            //
            // If the resource is not valid.
        else {
            //
            // Set the response to null.
            result = null ;
            }

        // TODO
        // Need to return something to the client.
        // Possible a new DataObject ... ?
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return result ;
        }

    /**
     * Update a PolicyPermission.
     *
     */
    public PolicyPermission setPermission(PolicyPermission permission)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PermissionManagerImpl.setPermission()") ;
        if (DEBUG_FLAG) System.out.println("  resource : " + permission.getResource()) ;
        if (DEBUG_FLAG) System.out.println("  group    : " + permission.getGroup()) ;
        if (DEBUG_FLAG) System.out.println("  action   : " + permission.getAction()) ;

        Database         database = null ;
        PolicyPermission result   = null ;
        //
        // Create a ResourceIdent for our Resource.
        ResourceIdent resourceIdent = new ResourceIdent(permission.getResource()) ;
        if (DEBUG_FLAG) System.out.println("  resource : " + resourceIdent) ;
        //
        // Create a CommunityIdent for our Group.
        CommunityIdent groupIdent = new CommunityIdent(permission.getGroup()) ;
        if (DEBUG_FLAG) System.out.println("  group    : " + groupIdent) ;

        //
        // If the resource ident is valid.
        if (resourceIdent.isValid())
            {
            //
            // If the resource ident is local.
            if (resourceIdent.isLocal())
                {
                //
                // If the group ident is valid.
                if (groupIdent.isValid())
                    {
                    //
                    // Create the database key.
                    Complex key = new Complex(
                        new Object[]
                            {
                            resourceIdent.toString(),
                            groupIdent.toString(),
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
                    }
                //
                // If the group ident is not valid.
                else {
                    //
                    // Set the response to null.
                    result = null ;
                    }
                }
            //
            // If the resource is not local.
            else {
                //
                // Set the response to null.
                result = null ;
                }
            }
            //
            // If the resource is not valid.
        else {
            //
            // Set the response to null.
            result = null ;
            }

        // TODO
        // Need to return something to the client.
        // Possible a new DataObject ... ?
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return result ;
        }

    /**
     * Delete a PolicyPermission.
     *
     */
    public boolean delPermission(String resourceName, String groupName, String action)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PermissionManagerImpl.delPermission()") ;
        if (DEBUG_FLAG) System.out.println("  resource : " + resourceName) ;
        if (DEBUG_FLAG) System.out.println("  group    : " + groupName) ;
        if (DEBUG_FLAG) System.out.println("  action   : " + action) ;

        Database         database = null ;
        PolicyPermission result   = null ;
        //
        // Create a ResourceIdent for our Resource.
        ResourceIdent resourceIdent = new ResourceIdent(resourceName) ;
        if (DEBUG_FLAG) System.out.println("  resource : " + resourceIdent) ;
        //
        // Create a CommunityIdent for our Group.
        CommunityIdent groupIdent = new CommunityIdent(groupName) ;
        if (DEBUG_FLAG) System.out.println("  group    : " + groupIdent) ;

        //
        // If the resource ident is valid.
        if (resourceIdent.isValid())
            {
            //
            // If the resource ident is local.
            if (resourceIdent.isLocal())
                {
                //
                // Create the database key.
                Complex key = new Complex(
                    new Object[]
                        {
                        resourceIdent.toString(),
                        groupIdent.toString(),
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
// TODO
// The only reason to treat this differently is that we might one day report it differently to the client.
                catch (ObjectNotFoundException ouch)
                    {
                    //
                    // Log the exception.
                    logException(ouch, "PermissionManagerImpl.delPermission()") ;
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
                    logException(ouch, "PermissionManagerImpl.delPermission()") ;
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
                }
            //
            // If the resource is not local.
            else {
                //
                // Set the response to null.
                result = null ;
                }
            }
            //
            // If the resource is not valid.
        else {
            //
            // Set the response to null.
            result = null ;
            }

        // TODO
        // Need to return something to the client.
        // Possible a new DataObject ... ?
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return (null != result) ;
        }
    }
