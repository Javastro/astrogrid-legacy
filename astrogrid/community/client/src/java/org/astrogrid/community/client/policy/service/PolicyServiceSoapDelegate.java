/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/service/PolicyServiceSoapDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyServiceSoapDelegate.java,v $
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.2  2004/03/05 14:03:23  dave
 *   Added first client side SOAP test - SecurityServiceSoapDelegateTestCase
 *
 *   Revision 1.1.2.1  2004/03/04 16:09:37  dave
 *   Added PolicyService delegates
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.policy.service ;

import java.net.URL ;
import java.net.MalformedURLException ;

import org.astrogrid.community.common.policy.service.PolicyService ;
import org.astrogrid.community.common.policy.service.PolicyServiceService ;
import org.astrogrid.community.common.policy.service.PolicyServiceServiceLocator ;

/**
 * Soap delegate for our PolicyService service.
 *
 */
public class PolicyServiceSoapDelegate
	extends PolicyServiceCoreDelegate
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
	public PolicyServiceSoapDelegate()
		{
		super() ;
		}

	/**
	 * Public constructor, for a specific endpoint URL.
	 * @param endpoint The service endpoint URL.
	 *
	 */
	public PolicyServiceSoapDelegate(String endpoint)
		throws MalformedURLException
		{
		this(new URL(endpoint)) ;
		}

	/**
	 * Public constructor, for a specific endpoint URL.
	 * @param endpoint - The service endpoint URL.
	 *
	 */
	public PolicyServiceSoapDelegate(URL endpoint)
		{
		super() ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyServiceSoapDelegate()") ;
		if (DEBUG_FLAG) System.out.println("  URL : " + endpoint) ;
		this.setEndpoint(endpoint) ;
		}

	/**
	 * Our PolicyService locator.
	 *
	 */
	private PolicyServiceService locator = new PolicyServiceServiceLocator() ;

	/**
	 * Our endpoint address.
	 *
	 */
	private URL endpoint ;

	/**
	 * Get our endpoint address.
	 *
	 */
	public URL getEndpoint()
		{
		return this.endpoint ;
		}

	/**
	 * Set our endpoint address.
	 *
	 */
	public void setEndpoint(URL endpoint)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyServiceSoapDelegate.setEndpoint()") ;
		if (DEBUG_FLAG) System.out.println("  URL : " + endpoint) ;
		//
		// Set our endpoint address.
		this.endpoint = endpoint ;
		//
		// Reset our PolicyService reference.
		this.setPolicyService(null) ;
		//
		// If we have a valid endpoint.
		if (null != this.getEndpoint())
			{
			//
			// If we have a valid locator.
			if (null != locator)
				{
				try {
					//
					// Try getting our PolicyService service.
					this.setPolicyService(
						locator.getPolicyService(
							this.getEndpoint()
							)
						) ;
					}
				//
				// Catch anything that went BANG.
				catch (Exception ouch)
					{
					// TODO
					// Log the Exception, and throw a nicer one.
					// Unwrap RemoteExceptions.
					//
					}
				}
			//
			// If we don't have a valid locator.
			else {
				//
				// Set our service to null and log it.
				this.setPolicyService(null) ;
				}
			}
		//
		// If we don't have a valid endpoint.
		else {
			//
			// Set our service to null and log it.
			this.setPolicyService(null) ;
			}
		}
	}
