/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/database/manager/DatabaseManagerSoapDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManagerSoapDelegate.java,v $
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.2  2004/03/05 14:03:23  dave
 *   Added first client side SOAP test - SecurityServiceSoapDelegateTestCase
 *
 *   Revision 1.1.2.1  2004/03/04 17:13:30  dave
 *   Added DatabaseManager delegates
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.database.manager ;

import java.net.URL ;
import java.net.MalformedURLException ;

import org.astrogrid.community.common.database.manager.DatabaseManager ;
import org.astrogrid.community.common.database.manager.DatabaseManagerService ;
import org.astrogrid.community.common.database.manager.DatabaseManagerServiceLocator ;

/**
 * Soap delegate for our DatabaseManager service.
 *
 */
public class DatabaseManagerSoapDelegate
	extends DatabaseManagerCoreDelegate
	implements DatabaseManagerDelegate
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
	public DatabaseManagerSoapDelegate()
		{
		super() ;
		}

	/**
	 * Public constructor, for a specific endpoint URL.
	 * @param endpoint The service endpoint URL.
	 *
	 */
	public DatabaseManagerSoapDelegate(String endpoint)
		throws MalformedURLException
		{
		this(new URL(endpoint)) ;
		}

	/**
	 * Public constructor, for a specific endpoint URL.
	 * @param endpoint - The service endpoint URL.
	 *
	 */
	public DatabaseManagerSoapDelegate(URL endpoint)
		{
		super() ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("DatabaseManagerSoapDelegate()") ;
		if (DEBUG_FLAG) System.out.println("  URL : " + endpoint) ;
		this.setEndpoint(endpoint) ;
		}

	/**
	 * Our DatabaseManager locator.
	 *
	 */
	private DatabaseManagerService locator = new DatabaseManagerServiceLocator() ;

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
		if (DEBUG_FLAG) System.out.println("DatabaseManagerSoapDelegate.setEndpoint()") ;
		if (DEBUG_FLAG) System.out.println("  URL : " + endpoint) ;
		//
		// Set our endpoint address.
		this.endpoint = endpoint ;
		//
		// Reset our DatabaseManager reference.
		this.setDatabaseManager(null) ;
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
					// Try getting our DatabaseManager service.
					this.setDatabaseManager(
						locator.getDatabaseManager(
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
				this.setDatabaseManager(null) ;
				}
			}
		//
		// If we don't have a valid endpoint.
		else {
			//
			// Set our manager to null and log it.
			this.setDatabaseManager(null) ;
			}
		}
	}
