/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/service/CommunityServiceCoreDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityServiceCoreDelegate.java,v $
 *   Revision 1.6  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.5.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.5  2004/06/18 13:45:19  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.4.32.3  2004/06/17 15:10:03  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.4.32.2  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.service ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.rmi.RemoteException ;

import org.astrogrid.community.common.service.CommunityService ;
import org.astrogrid.community.common.service.data.ServiceStatusData ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunitySecurityException   ;
import org.astrogrid.community.common.exception.CommunityResourceException   ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

/**
 * The core delegate for our service delegates.
 * This acts as a wrapper for a CommunityService, and converts RemoteExceptions into CommunityServiceException.
 * @see CommunityService
 *
 */
public class CommunityServiceCoreDelegate
    implements CommunityService, CommunityServiceDelegate
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(CommunityServiceCoreDelegate.class);

    /**
     * Public constructor.
     *
     */
    public CommunityServiceCoreDelegate()
        {
        }

    /**
     * Our CommunityService.
     *
     */
    private CommunityService service = null ;

    /**
     * Get a reference to our CommunityService.
     *
     */
    protected CommunityService getCommunityService()
        {
        return this.service ;
        }

    /**
     * Set our our CommunityService.
     *
     */
    protected void setCommunityService(CommunityService service)
        {
        this.service = service ;
        }

    /**
     * Service health check.
     * @return ServiceStatusData with details of the Service status.
     * @throws CommunityServiceException If there is an server error.
     *
     */
    public ServiceStatusData getServiceStatus()
        throws CommunityServiceException
        {
        //
        // If we have a valid service reference.
        if (null != this.service)
            {
            //
            // Try calling the service method.
            try {
                return this.service.getServiceStatus() ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                throw new CommunityServiceException(
                    "WebService call failed",
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
     * A converter utility to unpack a CommunityServiceException from a RemoteException.
     * @throws CommunityServiceException If the RemoteException cause was a CommunityServiceException.
     *
     */
    public void serviceException(RemoteException ouch)
        throws CommunityServiceException
        {
        //
        // If the remote Exception has a cause.
        if (ouch.getCause() != null)
            {
            //
            // If the cause is a CommunityServiceException.
            if (ouch.getCause() instanceof CommunityServiceException)
                {
                //
                // Re-throw the CommunityServiceException.
                throw (CommunityServiceException) ouch.getCause() ;
                }
            }
        }

    /**
     * A converter utility to unpack a CommunityIdentifierException from a RemoteException.
     * @throws CommunityIdentifierException If the RemoteException cause was a CommunityIdentifierException.
     *
     */
    public void identifierException(RemoteException ouch)
        throws CommunityIdentifierException
        {
        //
        // If the remote Exception has a cause.
        if (ouch.getCause() != null)
            {
            //
            // If the cause is a CommunityIdentifierException.
            if (ouch.getCause() instanceof CommunityIdentifierException)
                {
                //
                // Re-throw the CommunityIdentifierException.
                throw (CommunityIdentifierException) ouch.getCause() ;
                }
            }
        }

    /**
     * A converter utility to unpack a CommunitySecurityException from a RemoteException.
     * @throws CommunitySecurityException If the RemoteException cause was a CommunitySecurityException.
     *
     */
    public void securityException(RemoteException ouch)
        throws CommunitySecurityException
        {
        //
        // If the remote Exception has a cause.
        if (ouch.getCause() != null)
            {
            //
            // If the cause is a CommunitySecurityException.
            if (ouch.getCause() instanceof CommunitySecurityException)
                {
                //
                // Re-throw the CommunitySecurityException.
                throw (CommunitySecurityException) ouch.getCause() ;
                }
            }
        }

    /**
     * A converter utility to unpack a CommunityResourceException from a RemoteException.
     * @throws CommunityResourceException If the RemoteException cause was a CommunitySecurityException.
     *
     */
    public void resourceException(RemoteException ouch)
        throws CommunityResourceException
        {
        //
        // If the remote Exception has a cause.
        if (ouch.getCause() != null)
            {
            //
            // If the cause is a CommunityResourceException.
            if (ouch.getCause() instanceof CommunityResourceException)
                {
                //
                // Re-throw the CommunityResourceException.
                throw (CommunityResourceException) ouch.getCause() ;
                }
            }
        }

    /**
     * A converter utility to unpack a CommunityPolicyException from a RemoteException.
     * @throws CommunityPolicyException If the RemoteException cause was a CommunityPolicyException.
     *
     */
    public void policyException(RemoteException ouch)
        throws CommunityPolicyException
        {
        //
        // If the remote Exception has a cause.
        if (ouch.getCause() != null)
            {
            //
            // If the cause is a CommunityPolicyException.
            if (ouch.getCause() instanceof CommunityPolicyException)
                {
                //
                // Re-throw the CommunityResourceException.
                throw (CommunityPolicyException) ouch.getCause() ;
                }
            }
        }

    /**
     * A converter utility to unpack one of the CommunityExceptions from a RemoteException.
     * @throws CommunityPolicyException If the RemoteException cause was a CommunityPolicyException.
     * @throws CommunityServiceException If the RemoteException cause was a CommunityServiceException.
     * @throws CommunityIdentifierException If the RemoteException cause was a CommunityIdentifierException.
     * @throws CommunityResourceException If the RemoteException cause was a CommunityResourceException.
     * @todo Need to handle java.net.ConnectException
     * @todo Need to handle org.astrogrid.config.PropertyNotFoundException
     *
    public void convertCommunityException(RemoteException ouch)
        throws CommunityServiceException, CommunityIdentifierException, CommunityResourceException, CommunityPolicyException
        {
        //
        // If the remote Exception has a cause.
        if (ouch.getCause() != null)
            {
            //
            // If the cause is a CommunityServiceException.
            if (ouch.getCause() instanceof CommunityServiceException)
                {
                //
                // Re-throw the CommunityServiceException.
                throw (CommunityServiceException) ouch.getCause() ;
                }
            //
            // If the cause is a CommunityIdentifierException.
            if (ouch.getCause() instanceof CommunityIdentifierException)
                {
                //
                // Re-throw the CommunityIdentifierException.
                throw (CommunityIdentifierException) ouch.getCause() ;
                }
            //
            // If the cause is a CommunityResourceException.
            if (ouch.getCause() instanceof CommunityResourceException)
                {
                //
                // Re-throw the CommunityIdentifierException.
                throw (CommunityResourceException) ouch.getCause() ;
                }
            //
            // If the cause is a CommunityPolicyException.
            if (ouch.getCause() instanceof CommunityPolicyException)
                {
                //
                // Re-throw the CommunityPolicyException.
                throw (CommunityPolicyException) ouch.getCause() ;
                }
            }
        }
     */
    }
