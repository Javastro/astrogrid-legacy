/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/service/PolicyServiceCoreDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.8 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyServiceCoreDelegate.java,v $
 *   Revision 1.8  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.7.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.7  2004/06/18 13:45:19  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.6.32.3  2004/06/17 15:10:03  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.6.32.2  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.policy.service ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.rmi.RemoteException ;

import org.astrogrid.community.common.policy.data.PolicyPermission ;
import org.astrogrid.community.common.policy.data.PolicyCredentials ;

import org.astrogrid.community.common.policy.service.PolicyService ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

import org.astrogrid.community.client.service.CommunityServiceCoreDelegate ;

/**
 * The core delegate for our PolicyService service.
 * This acts as a wrapper for a PolicyService service, and converts RemoteExceptions into CommunityServiceException.
 *
 */
public class PolicyServiceCoreDelegate
    extends CommunityServiceCoreDelegate
    implements PolicyService, PolicyServiceDelegate
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(PolicyServiceCoreDelegate.class);

    /**
     * Public constructor.
     *
     */
    public PolicyServiceCoreDelegate()
        {
        }

    /**
     * Our PolicyService service.
     *
     */
    private PolicyService service = null ;

    /**
     * Get a reference to our PolicyService service.
     *
     */
    protected PolicyService getPolicyService()
        {
        return this.service ;
        }

    /**
     * Set our our PolicyService service.
     *
     */
    protected void setPolicyService(PolicyService service)
        {
        this.setCommunityService(service) ;
        this.service = service ;
        }

    /**
     * Confirm permissions.
     * @param credentials The credentials, containing the account and group identifiers.
     * @param resource The resource identifier.
     * @param action The action you want to perform.
     * @return A PolicyPermission object confirming the permission.
     * @throws CommunityIdentifierException If one of the identifiers is invalid.
     * @throws CommunityPolicyException If there is no matching permission.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public PolicyPermission checkPermissions(PolicyCredentials credentials, String resource, String action)
        throws CommunityServiceException, CommunityPolicyException, CommunityIdentifierException
        {
        //
        // If we have a valid service reference.
        if (null != this.service)
            {
            //
            // Try calling the service method.
            try {
                return this.service.checkPermissions(credentials, resource, action) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Try converting the Exception.
                policyException(ouch) ;
                serviceException(ouch) ;
                identifierException(ouch) ;
                //
                // If we get this far, then we don't know what it is.
                throw new CommunityServiceException(
                    "WebService call failed - " + ouch,
                    ouch
                    ) ;
                }
            }
        //
        // If we don't have a valid service.
        else {
            throw new CommunityServiceException(
                "Service not initialised"
                ) ;
            }
        }

    /**
     * Confirm membership.
     * @param credentials The credentials, containing the account and group identifiers.
     * @return A PolicyCredentials object confirming the membership.
     * @throws CommunityIdentifierException If one of the identifiers is invalid.
     * @throws CommunityPolicyException If there is no matching permission.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public PolicyCredentials checkMembership(PolicyCredentials credentials)
        throws CommunityServiceException, CommunityPolicyException, CommunityIdentifierException
        {
        //
        // If we have a valid service reference.
        if (null != this.service)
            {
            //
            // Try calling the service method.
            try {
                return this.service.checkMembership(credentials) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Try converting the Exception.
                policyException(ouch) ;
                serviceException(ouch) ;
                identifierException(ouch) ;
                //
                // If we get this far, then we don't know what it is.
                throw new CommunityServiceException(
                    "WebService call failed - " + ouch,
                    ouch
                    ) ;
                }
            }
        //
        // If we don't have a valid service.
        else {
            throw new CommunityServiceException(
                "Service not initialised"
                ) ;
            }
        }
    }
