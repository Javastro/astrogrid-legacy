/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/service/CommunityServiceCoreDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityServiceCoreDelegate.java,v $
 *   Revision 1.3  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.2.2.1  2004/03/22 16:47:55  dave
 *   Updated SecurityManagerDelegate to include Exceptions.
 *   Updated SecurityServiceDelegate to include Exceptions.
 *
 *   Revision 1.2  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/19 00:18:09  dave
 *   Refactored delegate Exception handling
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.service ;

import java.rmi.RemoteException ;

import org.astrogrid.community.common.service.CommunityService ;
import org.astrogrid.community.common.service.data.ServiceStatusData ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunitySecurityException   ;
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
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

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
	public void convertServiceException(RemoteException ouch)
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
	public void convertIdentifierException(RemoteException ouch)
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
	public void convertSecurityException(RemoteException ouch)
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
	 * A converter utility to unpack one of the CommunityExceptions from a RemoteException.
	 * @throws CommunityPolicyException If the RemoteException cause was a CommunityPolicyException.
	 * @throws CommunityServiceException If the RemoteException cause was a CommunityServiceException.
	 * @throws CommunityIdentifierException If the RemoteException cause was a CommunityIdentifierException.
	 *
	 */
	public void convertCommunityException(RemoteException ouch)
		throws CommunityPolicyException, CommunityServiceException, CommunityIdentifierException
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
				// Re-throw the CommunityPolicyException.
				throw (CommunityPolicyException) ouch.getCause() ;
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
			// If the cause is a CommunityServiceException.
			if (ouch.getCause() instanceof CommunityServiceException)
				{
				//
				// Re-throw the CommunityServiceException.
				throw (CommunityServiceException) ouch.getCause() ;
				}
			}
		}
    }
