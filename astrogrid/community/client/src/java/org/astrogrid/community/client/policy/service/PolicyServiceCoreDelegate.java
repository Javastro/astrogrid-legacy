/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/service/PolicyServiceCoreDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyServiceCoreDelegate.java,v $
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
     * Confirm access permissions.
     *
     */
    public PolicyPermission checkPermissions(PolicyCredentials credentials, String resource, String action)
        {
        PolicyPermission result = null ;
        //
        // If we have a valid service reference.
        if (null != this.service)
            {
            //
            // Try calling the service method.
            try {
                result = this.service.checkPermissions(credentials, resource, action) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        return result ;
        }

    /**
     * Confirm group membership.
     *
     */
    public PolicyCredentials checkMembership(PolicyCredentials credentials)
        {
        PolicyCredentials result = null ;
        //
        // If we have a valid service reference.
        if (null != this.service)
            {
            //
            // Try calling the service method.
            try {
                result = this.service.checkMembership(credentials) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        return result ;
        }
    }
