/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/policy/service/Attic/PolicyServiceImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/20 21:11:05 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyServiceImpl.java,v $
 *   Revision 1.5  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.4.2.2  2004/02/19 21:09:27  dave
 *   Refactored ServiceStatusData into a common package.
 *   Refactored CommunityServiceImpl constructor to take a parent service.
 *   Refactored default database for CommunityServiceImpl
 *
 *   Revision 1.4.2.1  2004/02/16 15:20:54  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.4  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.2.4.8  2004/02/06 16:14:17  dave
 *   Removed import java.rmi.Remote
 *
 *   Revision 1.2.4.7  2004/02/06 15:42:12  dave
 *   Fixes to pass JUnit tests.
 *
 *   Revision 1.2.4.6  2004/02/06 13:51:11  dave
 *   Fixed missing import
 *
 *   Revision 1.2.4.5  2004/02/06 13:49:09  dave
 *   Moved CommunityManagerBase into server.common.CommunityServer.
 *   Moved getServiceStatus into server.common.CommunityServer.
 *   Moved JUnit tests to match.
 *
 *   Revision 1.2.4.4  2004/01/27 07:43:03  dave
 *   Removed old DatabaseManager code
 *
 *   Revision 1.2.4.3  2004/01/27 06:46:19  dave
 *   Refactored PermissionManagerImpl and added initial JUnit tests
 *
 *   Revision 1.2.4.2  2004/01/27 06:16:20  dave
 *   Removed calls to GroupManagerImpl.init()
 *
 *   Revision 1.2.4.1  2004/01/26 23:23:23  dave
 *   Changed CommunityManagerImpl to use the new DatabaseManager.
 *   Moved rollback and close into CommunityManagerBase.
 *
 *   Revision 1.2  2004/01/07 10:45:45  dave
 *   Merged development branch, dave-dev-20031224, back into HEAD
 *
 *   Revision 1.1.2.2  2004/01/05 06:47:18  dave
 *   Moved policy data classes into policy.data package
 *
 *   Revision 1.1.2.1  2003/12/24 05:54:48  dave
 *   Initial Maven friendly structure (only part of the service implemented)
 *
 *   Revision 1.7  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.6  2003/09/17 19:47:21  dave
 *   1) Fixed classnotfound problems in the build.
 *   2) Added the JUnit task to add the initial accounts and groups.
 *   3) Got the build to work together with the portal.
 *   4) Fixed some bugs in the Account handling.
 *
 *   Revision 1.5  2003/09/13 02:18:52  dave
 *   Extended the jConfig configuration code.
 *
 *   Revision 1.4  2003/09/12 12:59:17  dave
 *   1) Fixed RemoteException handling in the manager and service implementations.
 *
 *   Revision 1.3  2003/09/11 03:15:06  dave
 *   1) Implemented PolicyService internals - no tests yet.
 *   2) Added getLocalAccountGroups and getRemoteAccountGroups to PolicyManager.
 *   3) Added remote access to groups.
 *
 *   Revision 1.2  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
 *
 *   Revision 1.1  2003/09/03 15:23:33  dave
 *   Split API into two services, PolicyService and PolicyManager
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.policy.service ;

import java.rmi.RemoteException ;

import org.astrogrid.community.common.policy.data.GroupMemberData ;
import org.astrogrid.community.common.policy.data.PolicyPermission  ;
import org.astrogrid.community.common.policy.data.PolicyCredentials ;
import org.astrogrid.community.common.policy.data.CommunityIdent ;

import org.astrogrid.community.common.policy.service.PolicyService ;

import org.astrogrid.community.server.policy.manager.GroupManagerImpl ;
import org.astrogrid.community.server.policy.manager.CommunityManagerImpl ;
import org.astrogrid.community.server.policy.manager.PermissionManagerImpl ;

import org.astrogrid.community.server.common.CommunityServiceImpl ;
import org.astrogrid.community.server.database.DatabaseConfiguration ;

public class PolicyServiceImpl
    extends CommunityServiceImpl
    implements PolicyService
    {
    /**
     * Switch for our debug statements.
     *
     */
    protected static final boolean DEBUG_FLAG = true ;

    /**
     * Our GroupManager.
     *
     */
    private GroupManagerImpl groupManager = new GroupManagerImpl() ;

    /**
     * Our CommunityManager.
     *
     */
    private CommunityManagerImpl communityManager = new CommunityManagerImpl() ;

    /**
     * Our PermissionManager
     *
     */
    private PermissionManagerImpl permissionManager = new PermissionManagerImpl() ;

    /**
     * Public constructor, using default database configuration.
     *
     */
    public PolicyServiceImpl()
        {
        super() ;
        //
        // Configure our local managers.
        configLocalManagers() ;
        }

    /**
     * Public constructor, using specific database configuration.
     *
     */
    public PolicyServiceImpl(DatabaseConfiguration config)
        {
        super(config) ;
        //
        // Configure our local managers.
        configLocalManagers() ;
        }

    /**
     * Public constructor, using a parent service.
     *
     */
    public PolicyServiceImpl(CommunityServiceImpl parent)
        {
        super(parent) ;
        }

    /**
     * Set our database configuration.
     * This makes it easier to run JUnit tests with a different database configurations.
     * This calls our base class method and then updates all of our local managers.
     *
     */
    public void setDatabaseConfiguration(DatabaseConfiguration config)
        {
        //
        // Call our base class method.
        super.setDatabaseConfiguration(config) ;
        //
        // Configure our local managers.
        configLocalManagers() ;
        }

    /**
     * Configure our local managers.
     * This calls setDatabaseConfiguration on all of our local managers.
     * We need this in a separate method to initialise the local managers after they are created.
     *
     */
    private void configLocalManagers()
        {
        //
        // Configure our local managers.
        if (null != groupManager) groupManager.setDatabaseConfiguration(this.getDatabaseConfiguration()) ;
//        if (null != accountManager) accountManager.setDatabaseConfiguration(this.getDatabaseConfiguration()) ;
//        if (null != resourceManager) resourceManager.setDatabaseConfiguration(this.getDatabaseConfiguration()) ;
        if (null != communityManager) communityManager.setDatabaseConfiguration(this.getDatabaseConfiguration()) ;
        if (null != permissionManager) permissionManager.setDatabaseConfiguration(this.getDatabaseConfiguration()) ;
        }

    /**
     * Confirm access permissions
     *
     */
    public PolicyPermission checkPermissions(PolicyCredentials credentials, String resource, String action)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyServiceImpl.checkPermissions()") ;
        //
        // Check for null params.
        if (null == credentials) return null ;
        if (null == resource) return null ;
        if (null == action) return null ;
        //
        // Trim spaces.
        resource = resource.trim() ;
        action = action.trim() ;
        //
        // Check for empty params.
        if (resource.length() == 0) return null ;
        if (action.length() == 0) return null ;
        //
        // Get the credential details.
        String group   = credentials.getGroup() ;
        String account = credentials.getAccount() ;
        //
        // Check for null params.
        if (null == group) return null ;
        if (null == account) return null ;

        if (DEBUG_FLAG) System.out.println("  Credentials") ;
        if (DEBUG_FLAG) System.out.println("    Group   : " + group)   ;
        if (DEBUG_FLAG) System.out.println("    Account : " + account) ;
        if (DEBUG_FLAG) System.out.println("  Resource") ;
        if (DEBUG_FLAG) System.out.println("    Name    : " + resource) ;
        if (DEBUG_FLAG) System.out.println("    Action  : " + action)   ;

        //
        // Check to see if the group has permission for the action.
        PolicyPermission permission = permissionManager.getPermission(resource, group, action) ;
        //
        // If we got a result.
        if (null != permission)
            {
            if (DEBUG_FLAG) System.out.println("PASS : Permission found") ;
            //
            // If the permission is valid.
            if (permission.isValid())
                {
                if (DEBUG_FLAG) System.out.println("PASS : Permission is valid") ;
                //
                // Check the credentials.
                PolicyCredentials checked = checkMembership(credentials) ;
                //
                // If the credentials are valid.
                if (checked.isValid())
                    {
                    if (DEBUG_FLAG) System.out.println("PASS : Credentials are valid") ;
                    }
                //
                // If the credentials are not valid.
                else {
                    if (DEBUG_FLAG) System.out.println("FAIL : Credentials not valid") ;
                    permission.setStatus(PolicyPermission.STATUS_CREDENTIALS_INVALID) ;
                    permission.setReason(PolicyPermission.REASON_CREDENTIALS_INVALID) ;
                    }
                }
            //
            // If the permission is not granted.
            else {
                if (DEBUG_FLAG) System.out.println("FAIL : Permission not valid") ;
                }
            }
        //
        // If we didn't get a result.
        else {
            if (DEBUG_FLAG) System.out.println("FAIL : No permission found") ;
/*
 * I can't remember why we return an object here.
 * Returning an object means that this service behaves differently to the rest.
            //
            // Create a dummy permission.
            permission = new PolicyPermission() ;
            permission.setResource(resource) ;
            permission.setGroup(group) ;
            permission.setAction(action) ;
            permission.setStatus(PolicyPermission.STATUS_PERMISSION_UNKNOWN) ;
            permission.setReason("Permission not found") ;
 *
 */
            }
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return permission ;
        }

    /**
     * Confirm group membership.
     *
     */
    public PolicyCredentials checkMembership(PolicyCredentials credentials)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyServiceImpl.checkMembership()") ;

        //
        // Set the status to unknown.
        credentials.setStatus(PolicyCredentials.STATUS_NOT_KNOWN) ;
        credentials.setReason("No reason given") ;

        //
        // Get CommunityIdents for the account and group.
        CommunityIdent group   = new CommunityIdent(credentials.getGroup()) ;
        CommunityIdent account = new CommunityIdent(credentials.getAccount()) ;

        if (DEBUG_FLAG) System.out.println("  Credentials") ;
        if (DEBUG_FLAG) System.out.println("    Group   : " + group) ;
        if (DEBUG_FLAG) System.out.println("    Account : " + account) ;
        //
        // If the group is local.
        if (group.isLocal())
            {
            if (DEBUG_FLAG) System.out.println("PASS : Group is local") ;
            //
            // See if there is a membership record.
            GroupMemberData membership = groupManager.getGroupMember(account, group) ;
            //
            // If there is a membership record.
            if (null != membership)
                {
                if (DEBUG_FLAG) System.out.println("PASS : Account is a member of Group") ;
                //
                // Update the credentials.
                credentials.setStatus(PolicyCredentials.STATUS_VALID) ;
                credentials.setReason("Account IS a member of Group") ;
                }
            //
            // If there is no membership record.
            else {
                if (DEBUG_FLAG) System.out.println("FAIL : Account is not a member of Group") ;
                //
                // Update the credentials.
                credentials.setStatus(PolicyCredentials.STATUS_NOT_VALID) ;
                credentials.setReason("Account is NOT a member of Group") ;
                }
            }
        //
        // If the group is not local.
        else {
            if (DEBUG_FLAG) System.out.println("PASS : Group is remote") ;
            //
            // Get a service for the remote community.
            PolicyService remote = communityManager.getPolicyService(group.getCommunity()) ;
            //
            // If we got a remote service.
            if (null != remote)
                {
                if (DEBUG_FLAG) System.out.println("PASS : Found remote service") ;
                //
                // Try asking the remote manager.
                PolicyCredentials result = null ;
                try {
                    result = remote.checkMembership(credentials) ;
                    }
                //
                // Catch a remote Exception from the SOAP call.
                catch (RemoteException ouch)
                    {
                    if (DEBUG_FLAG) System.out.println("FAIL : Remote service call failed.") ;
                    result = null ;
                    }
                //
                // If we got a result.
                if (null != result)
                    {
                    if (DEBUG_FLAG) System.out.println("PASS : Remote service responded") ;
                    //
                    // If the result is valid.
                    if (result.isValid())
                        {
                        if (DEBUG_FLAG) System.out.println("PASS : Remote response is valid") ;
                        //
                        // Update the credentials.
                        credentials.setStatus(result.getStatus()) ;
                        credentials.setReason(result.getReason()) ;
                        }
                    //
                    // If the result is not valid.
                    else {
                        if (DEBUG_FLAG) System.out.println("FAIL : Remote response is not valid") ;
                        //
                        // Update the credentials.
                        credentials.setStatus(result.getStatus()) ;
                        credentials.setReason(result.getReason()) ;
                        }
                    }
                //
                // If we didn't get a result.
                else {
                    if (DEBUG_FLAG) System.out.println("PASS : Remote service returned null") ;
                    //
                    // Update the credentials.
                    credentials.setStatus(PolicyCredentials.STATUS_NOT_VALID) ;
                    credentials.setReason("No response from community service") ;
                    }
                }
            //
            // If we didn't get a remote service.
            else {
                if (DEBUG_FLAG) System.out.println("FAIL : Unknown remote service") ;
                //
                // Update the credentials.
                credentials.setStatus(PolicyCredentials.STATUS_NOT_VALID) ;
                credentials.setReason("Unknown community service") ;
                }
            }

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return credentials ;
        }
    }
