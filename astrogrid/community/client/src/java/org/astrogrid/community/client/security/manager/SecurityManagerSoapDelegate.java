/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/security/manager/SecurityManagerSoapDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerSoapDelegate.java,v $
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.2  2004/03/05 14:03:23  dave
 *   Added first client side SOAP test - SecurityServiceSoapDelegateTestCase
 *
 *   Revision 1.1.2.1  2004/03/04 08:57:10  dave
 *   Started work on the install xdocs.
 *   Started work on the Security delegates.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.security.manager ;

import java.net.URL ;
import java.net.MalformedURLException ;

import org.astrogrid.community.common.security.manager.SecurityManager ;
import org.astrogrid.community.common.security.manager.SecurityManagerService ;
import org.astrogrid.community.common.security.manager.SecurityManagerServiceLocator ;

/**
 * Soap delegate for our SecurityManager service.
 *
 */
public class SecurityManagerSoapDelegate
	extends SecurityManagerCoreDelegate
	implements SecurityManagerDelegate
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
	public SecurityManagerSoapDelegate()
		{
		super() ;
		}

	/**
	 * Public constructor, for a specific endpoint URL.
	 * @param endpoint The service endpoint URL.
	 *
	 */
	public SecurityManagerSoapDelegate(String endpoint)
		throws MalformedURLException
		{
		this(new URL(endpoint)) ;
		}

	/**
	 * Public constructor, for a specific endpoint URL.
	 * @param endpoint - The service endpoint URL.
	 *
	 */
	public SecurityManagerSoapDelegate(URL endpoint)
		{
		super() ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("SecurityManagerSoapDelegate()") ;
		if (DEBUG_FLAG) System.out.println("  URL : " + endpoint) ;
		this.setEndpoint(endpoint) ;
		}

	/**
	 * Our SecurityManager locator.
	 *
	 */
	private SecurityManagerService locator = new SecurityManagerServiceLocator() ;

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
		if (DEBUG_FLAG) System.out.println("SecurityManagerSoapDelegate.setEndpoint()") ;
		if (DEBUG_FLAG) System.out.println("  URL : " + endpoint) ;
		//
		// Set our endpoint address.
		this.endpoint = endpoint ;
		//
		// Reset our SecurityManager reference.
		this.setSecurityManager(null) ;
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
					// Try getting our SecurityManager service.
					this.setSecurityManager(
						locator.getSecurityManager(
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
				// Set our manager to null and log it.
				this.setSecurityManager(null) ;
				}
			}
		//
		// If we don't have a valid endpoint.
		else {
			//
			// Set our manager to null and log it.
			this.setSecurityManager(null) ;
			}
		}
	}
