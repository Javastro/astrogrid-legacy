package org.astrogrid.community.delegate.policy;

import java.net.URL;

import org.astrogrid.community.common.CommunityConfig;

import org.astrogrid.community.policy.data.PolicyCredentials;
import org.astrogrid.community.policy.data.PolicyPermission;

import org.astrogrid.community.policy.server.PolicyService ;
import org.astrogrid.community.policy.server.PolicyServiceService ;
import org.astrogrid.community.policy.server.PolicyServiceServiceLocator ;

public class PolicyServiceDelegate
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static final boolean DEBUG_FLAG = true ;

    /**
     * Reference to our PolicyManager stub.
     *
     */
    private PolicyService service = null ;

    /*
     * The permissions returned by the last call to checkPermissions().
     *
     */
    PolicyPermission perm = null;

    //
    // Load our community config.
    static
        {
        CommunityConfig.loadConfig() ;
        }

    /**
     * Public constructor deals with getting our service (link) to the webservice.
     *
     */
    public PolicyServiceDelegate()
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyServiceDelegate()") ;
        try
            {
            service = getService(CommunityConfig.getServiceUrl());
            }
        catch(Exception e) {
            e.printStackTrace();
            service = null;
            }
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Wrapper method, with account and group identifiers exposed as Strings.
     *
     */
    public boolean checkPermissions(String account,String group, String resource, String action)
        throws Exception
        {
        return checkPermissions(new PolicyCredentials(account,group),resource,action);
        }

    /**
     * Call checkPermissions() on our service.
     * @return false if the check fails or we can't reach the service.
     */
    public boolean checkPermissions(PolicyCredentials credentials, String resource, String action) throws Exception {
        //
        // Set our permissions to null.
        perm = null ;
        //
        // Check if we have a service reference.
        if (null != service)
            {
            perm = service.checkPermissions(credentials,resource,action);
            }
        //
        // Return true if we got a result, and the permissions are valid.
        return (null != perm) ? perm.isValid() : false ;
        }

    /**
     * Access to the PolicyPermission recived by the last call to checkPermissions().
     *
     */
    public PolicyPermission getPolicyPermission()
        {
        return this.perm;
        }

    /**
     * Return our service object which is our link to the webservice.
     * @return
     * @throws Exception
     */
    private PolicyService getService(String targetEndPoint)
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("setUp()") ;
        if (DEBUG_FLAG) System.out.println("  Target : '" + targetEndPoint + "'") ;

        if ((null == targetEndPoint) || (targetEndPoint.length() <= 0))
            {
            targetEndPoint = CommunityConfig.getServiceUrl() ;
            }
        if (DEBUG_FLAG) System.out.println("  Target : '" + targetEndPoint + "'") ;

        PolicyServiceService locator = null;
        PolicyService service = null;
        //
        // Create our service locator.
        locator = new PolicyServiceServiceLocator();

        //
        // Create our service.
        service = locator.getPolicyService(new URL(targetEndPoint));

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        return service;
        }
    }