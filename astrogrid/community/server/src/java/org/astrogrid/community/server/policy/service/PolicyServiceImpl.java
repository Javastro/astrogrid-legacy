/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/policy/service/Attic/PolicyServiceImpl.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/08/12 16:08:47 $</cvs:date>
 * <cvs:version>$Revision: 1.12 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyServiceImpl.java,v $
 *   Revision 1.12  2005/08/12 16:08:47  clq2
 *   com-jl-1315
 *
 *   Revision 1.11.110.1  2005/07/26 11:30:19  jl99
 *   Tightening up of unit tests for the server subproject
 *
 *   Revision 1.11  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.10.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.10  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.9.32.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.policy.service ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.rmi.RemoteException ;

import org.astrogrid.community.common.policy.data.GroupMemberData ;
import org.astrogrid.community.common.policy.data.PolicyPermission  ;
import org.astrogrid.community.common.policy.data.PolicyCredentials ;

import org.astrogrid.community.common.policy.service.PolicyService ;

import org.astrogrid.community.server.policy.manager.GroupManagerImpl ;
import org.astrogrid.community.server.policy.manager.CommunityManagerImpl ;
import org.astrogrid.community.server.policy.manager.PermissionManagerImpl ;

import org.astrogrid.community.server.service.CommunityServiceImpl ;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

// TODO remove these
import org.astrogrid.community.common.policy.data.CommunityIdent ;

public class PolicyServiceImpl
    extends CommunityServiceImpl
    implements PolicyService
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(PolicyServiceImpl.class);

    /**
     * Our GroupManager.
     *
     */
    private GroupManagerImpl groupManager ;

    /**
     * Our CommunityManager.
     *
     */
    private CommunityManagerImpl communityManager ;

    /**
     * Our PermissionManager
     *
     */
    private PermissionManagerImpl permissionManager ;

    /**
     * Public constructor, using default database configuration.
     *
     */
    public PolicyServiceImpl()
        {
        super() ;
        //
        // Initialise our local managers.
        initManagers() ;
        }

    /**
     * Public constructor, using specific database configuration.
     *
     */
    public PolicyServiceImpl(DatabaseConfiguration config)
        {
        super(config) ;
        //
        // Initialise our local managers.
        initManagers() ;
        }

    /**
     * Public constructor, using a parent service.
     *
     */
    public PolicyServiceImpl(CommunityServiceImpl parent)
        {
        super(parent) ;
        //
        // Initialise our local managers.
        initManagers() ;
        }

    /**
     * Initialise our local managers, passing a reference to 'this' as their parent.
     *
     */
    private void initManagers()
        {
        groupManager = new GroupManagerImpl(this) ;
        communityManager = new CommunityManagerImpl(this) ;
        permissionManager = new PermissionManagerImpl(this) ;
        }

    /**
     * Confirm access permissions
     * @todo Refactor to use Ivorn identifiers. 
     *
     */
    public PolicyPermission checkPermissions(PolicyCredentials credentials, String resource, String action)
        throws CommunityServiceException, CommunityPolicyException, CommunityIdentifierException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyServiceImpl.checkPermissions()") ;
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

        log.debug("  Credentials") ;
        log.debug("    Group   : " + group)   ;
        log.debug("    Account : " + account) ;
        log.debug("  Resource") ;
        log.debug("    Name    : " + resource) ;
        log.debug("    Action  : " + action)   ;

        //
        // Check to see if the group has permission for the action.
        PolicyPermission permission = permissionManager.getPermission(resource, group, action) ;
        //
        // If we got a result.
        if (null != permission)
            {
            log.debug("PASS : Permission found") ;
            //
            // If the permission is valid.
            if (permission.isValid())
                {
                log.debug("PASS : Permission is valid") ;
                //
                // Check the credentials.
                PolicyCredentials checked = checkMembership(credentials) ;
                //
                // If the credentials are valid.
                if (checked.isValid())
                    {
                    log.debug("PASS : Credentials are valid") ;
                    }
                //
                // If the credentials are not valid.
                else {
                    log.debug("FAIL : Credentials not valid") ;
                    permission.setStatus(PolicyPermission.STATUS_CREDENTIALS_INVALID) ;
                    permission.setReason(PolicyPermission.REASON_CREDENTIALS_INVALID) ;
                    }
                }
            //
            // If the permission is not granted.
            else {
                log.debug("FAIL : Permission not valid") ;
                }
            }
        //
        // If we didn't get a result.
        else {
            log.debug("FAIL : No permission found") ;
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
        log.debug("----\"----") ;
        return permission ;
        }

    /**
     * Confirm group membership.
     *
     */
    public PolicyCredentials checkMembership(PolicyCredentials credentials)
        throws CommunityServiceException, CommunityPolicyException, CommunityIdentifierException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyServiceImpl.checkMembership()") ;

        //
        // Set the status to unknown.
        credentials.setStatus(PolicyCredentials.STATUS_NOT_KNOWN) ;
        credentials.setReason("No reason given") ;

        //
        // Get CommunityIdents for the account and group.
        CommunityIdent group   = new CommunityIdent(credentials.getGroup()) ;
        CommunityIdent account = new CommunityIdent(credentials.getAccount()) ;

        log.debug("  Credentials") ;
        log.debug("    Group   : " + group) ;
        log.debug("    Account : " + account) ;
        //
        // If the group is local.
        if (group.isLocal())
            {
            log.debug("PASS : Group is local") ;
            //
            // See if there is a membership record.
// TODO refacot to use Ivorn
            GroupMemberData membership = groupManager.getGroupMember(
                account.toString(),
                group.toString()
                ) ;
            //
            // If there is a membership record.
            if (null != membership)
                {
                log.debug("PASS : Account is a member of Group") ;
                //
                // Update the credentials.
                credentials.setStatus(PolicyCredentials.STATUS_VALID) ;
                credentials.setReason("Account IS a member of Group") ;
                }
            //
            // If there is no membership record.
            else {
                log.debug("FAIL : Account is not a member of Group") ;
                //
                // Update the credentials.
                credentials.setStatus(PolicyCredentials.STATUS_NOT_VALID) ;
                credentials.setReason("Account is NOT a member of Group") ;
                }
            }
        //
        // If the group is not local.
        else {
            log.debug("PASS : Group is remote") ;
            //
            // Get a service for the remote community.
            PolicyService remote = communityManager.getPolicyService(group.getCommunity()) ;
            //
            // If we got a remote service.
            if (null != remote)
                {
                log.debug("PASS : Found remote service") ;
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
                    log.debug("FAIL : Remote service call failed.") ;
                    result = null ;
                    }
                //
                // If we got a result.
                if (null != result)
                    {
                    log.debug("PASS : Remote service responded") ;
                    //
                    // If the result is valid.
                    if (result.isValid())
                        {
                        log.debug("PASS : Remote response is valid") ;
                        //
                        // Update the credentials.
                        credentials.setStatus(result.getStatus()) ;
                        credentials.setReason(result.getReason()) ;
                        }
                    //
                    // If the result is not valid.
                    else {
                        log.debug("FAIL : Remote response is not valid") ;
                        //
                        // Update the credentials.
                        credentials.setStatus(result.getStatus()) ;
                        credentials.setReason(result.getReason()) ;
                        }
                    }
                //
                // If we didn't get a result.
                else {
                    log.debug("PASS : Remote service returned null") ;
                    //
                    // Update the credentials.
                    credentials.setStatus(PolicyCredentials.STATUS_NOT_VALID) ;
                    credentials.setReason("No response from community service") ;
                    }
                }
            //
            // If we didn't get a remote service.
            else {
                log.debug("FAIL : Unknown remote service") ;
                //
                // Update the credentials.
                credentials.setStatus(PolicyCredentials.STATUS_NOT_VALID) ;
                credentials.setReason("Unknown community service") ;
                }
            }

        log.debug("----\"----") ;
        return credentials ;
        }
    }
