/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/policy/manager/Attic/PermissionManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/01/07 10:45:45 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PermissionManagerImpl.java,v $
 *   Revision 1.2  2004/01/07 10:45:45  dave
 *   Merged development branch, dave-dev-20031224, back into HEAD
 *
 *   Revision 1.1.2.2  2004/01/05 06:47:18  dave
 *   Moved policy data classes into policy.data package
 *
 *   Revision 1.1.2.1  2003/12/24 05:54:48  dave
 *   Initial Maven friendly structure (only part of the service implemented)
 *
 *   Revision 1.4  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.3  2003/10/09 01:38:30  dave
 *   Added JUnite tests for policy delegates
 *
 *   Revision 1.2  2003/09/12 12:59:17  dave
 *   1) Fixed RemoteException handling in the manager and service implementations.
 *
 *   Revision 1.1  2003/09/10 02:56:03  dave
 *   Added PermissionManager and tests
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.policy.manager ;

//import java.rmi.RemoteException ;

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

import org.exolab.castor.persist.spi.Complex ;

import org.astrogrid.community.common.policy.data.ServiceData ;
import org.astrogrid.community.common.policy.data.PolicyPermission ;
import org.astrogrid.community.common.policy.data.ResourceIdent ;
import org.astrogrid.community.common.policy.data.CommunityIdent ;

import org.astrogrid.community.common.policy.manager.PermissionManager ;
import org.astrogrid.community.server.policy.manager.DatabaseManager ;

public class PermissionManagerImpl
    implements PermissionManager
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
    public PermissionManagerImpl()
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
                        // Begin a new database transaction.
                        database.begin();
                        //
                        // Try creating it in the database.
                        database.create(permission);
                        }
                    //
                    // If we already have an object with that ident.
                    catch (DuplicateIdentityException ouch)
                        {
                        if (DEBUG_FLAG) System.out.println("") ;
                        if (DEBUG_FLAG) System.out.println("  ----") ;
                        if (DEBUG_FLAG) System.out.println("DuplicateIdentityException in addPermission()") ;

                        //
                        // Set the response to null.
                        permission = null ;

                        if (DEBUG_FLAG) System.out.println("  ----") ;
                        if (DEBUG_FLAG) System.out.println("") ;
                        }
                    //
                    // If anything else went bang.
                    catch (Exception ouch)
                        {
                        if (DEBUG_FLAG) System.out.println("") ;
                        if (DEBUG_FLAG) System.out.println("  ----") ;
                        if (DEBUG_FLAG) System.out.println("Generic exception in addPermission()") ;

                        //
                        // Set the response to null.
                        permission = null ;

                        if (DEBUG_FLAG) System.out.println("  ----") ;
                        if (DEBUG_FLAG) System.out.println("") ;
                        }
                    //
                    // Commit the transaction.
                    finally
                        {
                        try {
                            if (null != permission)
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
                            if (DEBUG_FLAG) System.out.println("Generic exception in addPermission() finally clause") ;

                            //
                            // Set the response to null.
                            permission = null ;

                            if (DEBUG_FLAG) System.out.println("  ----") ;
                            if (DEBUG_FLAG) System.out.println("") ;
                            }
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

        PolicyPermission result = null ;
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
                        // Begin a new database transaction.
                        database.begin();
                        //
                        // Load the PolicyPermission from the database.
                        result = (PolicyPermission) database.load(PolicyPermission.class, key) ;
                        }
                    //
                    // If we couldn't find the object.
                    catch (ObjectNotFoundException ouch)
                        {
                        if (DEBUG_FLAG) System.out.println("") ;
                        if (DEBUG_FLAG) System.out.println("  ----") ;
                        if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in getPermission()") ;

                        //
                        // Set the response to null.
                        result = null ;

                        if (DEBUG_FLAG) System.out.println("  ----") ;
                        if (DEBUG_FLAG) System.out.println("") ;
                        }
                    //
                    // If anything else went bang.
                    catch (Exception ouch)
                        {
                        if (DEBUG_FLAG) System.out.println("") ;
                        if (DEBUG_FLAG) System.out.println("  ----") ;
                        if (DEBUG_FLAG) System.out.println("Generic exception in getPermission()") ;

                        //
                        // Set the response to null.
                        result = null ;

                        if (DEBUG_FLAG) System.out.println("  ----") ;
                        if (DEBUG_FLAG) System.out.println("") ;
                        }
                    //
                    // Commit the transaction.
                    finally
                        {
                        try {
                            if (null != result)
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
                            if (DEBUG_FLAG) System.out.println("Generic exception in getPermission() finally clause") ;

                            //
                            // Set the response to null.
                            result = null ;

                            if (DEBUG_FLAG) System.out.println("  ----") ;
                            if (DEBUG_FLAG) System.out.println("") ;
                            }
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

        PolicyPermission result = null ;
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
                        // Begin a new database transaction.
                        database.begin();
                        //
                        // Load the PolicyPermission from the database.
                        result = (PolicyPermission) database.load(PolicyPermission.class, key) ;
                        //
                        // Update the PolicyPermission data.
                        result.setStatus(permission.getStatus()) ;
                        result.setReason(permission.getReason()) ;
                        }
                    //
                    // If we couldn't find the object.
                    catch (ObjectNotFoundException ouch)
                        {
                        if (DEBUG_FLAG) System.out.println("") ;
                        if (DEBUG_FLAG) System.out.println("  ----") ;
                        if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in setPermission()") ;

                        //
                        // Set the response to null.
                        result = null ;

                        if (DEBUG_FLAG) System.out.println("  ----") ;
                        if (DEBUG_FLAG) System.out.println("") ;
                        }
                    //
                    // If anything else went bang.
                    catch (Exception ouch)
                        {
                        if (DEBUG_FLAG) System.out.println("") ;
                        if (DEBUG_FLAG) System.out.println("  ----") ;
                        if (DEBUG_FLAG) System.out.println("Generic exception in setPermission()") ;

                        //
                        // Set the response to null.
                        result = null ;

                        if (DEBUG_FLAG) System.out.println("  ----") ;
                        if (DEBUG_FLAG) System.out.println("") ;
                        }
                    //
                    // Commit the transaction.
                    finally
                        {
                        try {
                            if (null != result)
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
                            if (DEBUG_FLAG) System.out.println("Generic exception in setPermission() finally clause") ;

                            //
                            // Set the response to null.
                            result = null ;

                            if (DEBUG_FLAG) System.out.println("  ----") ;
                            if (DEBUG_FLAG) System.out.println("") ;
                            }
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

        PolicyPermission result = null ;
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
                    // Begin a new database transaction.
                    database.begin();
                    //
                    // Load the PolicyPermission from the database.
                    result = (PolicyPermission) database.load(PolicyPermission.class, key) ;
                    //
                    // Dete the PolicyPermission data.
                    database.remove(result) ;
                    }
                //
                // If we couldn't find the object.
                catch (ObjectNotFoundException ouch)
                    {
                    if (DEBUG_FLAG) System.out.println("") ;
                    if (DEBUG_FLAG) System.out.println("  ----") ;
                    if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in delPermission()") ;

                    //
                    // Set the response to null.
                    result = null ;

                    if (DEBUG_FLAG) System.out.println("  ----") ;
                    if (DEBUG_FLAG) System.out.println("") ;
                    }
                //
                // If anything else went bang.
                catch (Exception ouch)
                    {
                    if (DEBUG_FLAG) System.out.println("") ;
                    if (DEBUG_FLAG) System.out.println("  ----") ;
                    if (DEBUG_FLAG) System.out.println("Generic exception in delPermission()") ;

                    //
                    // Set the response to null.
                    result = null ;

                    if (DEBUG_FLAG) System.out.println("  ----") ;
                    if (DEBUG_FLAG) System.out.println("") ;
                    }
                //
                // Commit the transaction.
                finally
                    {
                    try {
                        if (null != result)
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
                        if (DEBUG_FLAG) System.out.println("Generic exception in delPermission() finally clause") ;

                        //
                        // Set the response to null.
                        result = null ;

                        if (DEBUG_FLAG) System.out.println("  ----") ;
                        if (DEBUG_FLAG) System.out.println("") ;
                        }
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
