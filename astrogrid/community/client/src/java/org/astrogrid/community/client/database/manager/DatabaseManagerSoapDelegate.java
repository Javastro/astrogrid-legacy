/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/database/manager/DatabaseManagerSoapDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManagerSoapDelegate.java,v $
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
 *   Revision 1.4.38.2  2004/06/17 15:10:02  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.4.38.1  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.database.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URL ;
import java.net.MalformedURLException ;

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
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(DatabaseManagerSoapDelegate.class);

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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseManagerSoapDelegate()") ;
        log.debug("  URL : " + endpoint) ;
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
     * @todo Empty catch throws away Exceptions. Need better Exception handling.
     *
     */
    public void setEndpoint(URL endpoint)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseManagerSoapDelegate.setEndpoint()") ;
        log.debug("  URL : " + endpoint) ;
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
