package org.astrogrid.community.client.delegate ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URL;

import org.astrogrid.community.common.policy.data.PolicyCredentials;
import org.astrogrid.community.common.policy.data.PolicyPermission;

import org.astrogrid.community.common.config.CommunityConfig;

import org.astrogrid.community.common.policy.service.PolicyService ;
import org.astrogrid.community.common.policy.service.PolicyServiceService ;
import org.astrogrid.community.common.policy.service.PolicyServiceServiceLocator ;
/**
 *
 *
 * This API is going to be replaced during iter 5, and will be deprecated in iter 6.
 *
 */
public class PolicyServiceDelegate
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(PolicyServiceDelegate.class);

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


    /**
     * Public constructor deals with getting our service (link) to the webservice.
     *
     */
    public PolicyServiceDelegate()
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyServiceDelegate()") ;
        try
            {
            service = getService(CommunityConfig.getServiceUrl());
            }
        catch(Exception e) {
            e.printStackTrace();
            service = null;
            }
        log.debug("----\"----") ;
        log.debug("") ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("setUp()") ;
        log.debug("  Target : '" + targetEndPoint + "'") ;

        if ((null == targetEndPoint) || (targetEndPoint.length() <= 0))
            {
            targetEndPoint = CommunityConfig.getServiceUrl() ;
            }
        log.debug("  Target : '" + targetEndPoint + "'") ;

        PolicyServiceService locator = null;
        PolicyService service = null;
        //
        // Create our service locator.
        locator = new PolicyServiceServiceLocator();

        //
        // Create our service.
        service = locator.getPolicyService(new URL(targetEndPoint));

        log.debug("----\"----") ;
        log.debug("") ;
        return service;
        }
    }