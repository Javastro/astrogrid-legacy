/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/service/PolicyServiceCoreDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/30 01:40:03 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyServiceCoreDelegate.java,v $
 *   Revision 1.6  2004/03/30 01:40:03  dave
 *   Merged development branch, dave-dev-200403242058, into HEAD
 *
 *   Revision 1.5.4.2  2004/03/28 09:11:43  dave
 *   Convert tabs to spaces
 *
 *   Revision 1.5.4.1  2004/03/28 02:00:55  dave
 *   Added database management tasks.
 *
 *   Revision 1.5  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.4.2.1  2004/03/22 02:25:35  dave
 *   Updated delegate interfaces to include Exception handling.
 *
 *   Revision 1.4  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.3.14.1  2004/03/19 00:18:09  dave
 *   Refactored delegate Exception handling
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.policy.service ;

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
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

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
                convertCommunityException(ouch) ;
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
                convertCommunityException(ouch) ;
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
