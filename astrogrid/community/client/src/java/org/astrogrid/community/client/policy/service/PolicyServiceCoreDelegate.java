/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/service/PolicyServiceCoreDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/08 13:42:33 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyServiceCoreDelegate.java,v $
 *   Revision 1.3  2004/03/08 13:42:33  dave
 *   Updated Maven goals.
 *   Replaced tabs with Spaces.
 *
 *   Revision 1.2.2.1  2004/03/08 12:53:17  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/04 16:09:37  dave
 *   Added PolicyService delegates
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.policy.service ;

import java.rmi.RemoteException ;

import org.astrogrid.community.common.policy.data.PolicyPermission ;
import org.astrogrid.community.common.policy.data.PolicyCredentials ;

import org.astrogrid.community.common.policy.service.PolicyService ;

import org.astrogrid.community.common.service.data.ServiceStatusData ;

/**
 * The core delegate code for our PolicyService service.
 * This acts as a wrapper for a PolicyService service, and handles any RemoteExceptions internally.
 *
 */
public class PolicyServiceCoreDelegate
    implements PolicyServiceDelegate
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

    /**
     * Service health check.
     * @return ServiceStatusData with details of the Service status.
     * TODO -refactor this to a base class
     *
     */
    public ServiceStatusData getServiceStatus()
        {
        ServiceStatusData result = null ;
        //
        // If we have a valid service reference.
        if (null != this.service)
            {
            //
            // Try calling the service method.
            try {
                result = this.service.getServiceStatus() ;
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
