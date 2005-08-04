/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/database/manager/DatabaseManagerCoreDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/08/04 09:40:11 $</cvs:date>
 * <cvs:version>$Revision: 1.8 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManagerCoreDelegate.java,v $
 *   Revision 1.8  2005/08/04 09:40:11  clq2
 *   kevin's second batch
 *
 *   Revision 1.7.100.1  2005/07/28 13:35:53  KevinBenson
 *   No longer uses resetDatabaseTables as a web service method it is not allowed anymore.
 *
 *   Revision 1.7  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.6.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.6  2004/06/18 13:45:19  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.5.32.3  2004/06/17 15:10:02  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.5.32.2  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.database.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.rmi.RemoteException ;

import org.astrogrid.community.client.service.CommunityServiceCoreDelegate ;

import org.astrogrid.community.common.database.manager.DatabaseManager ;
import org.astrogrid.community.common.exception.CommunityServiceException ;

/**
 * The core delegate code for our DatabaseManager service.
 * This acts as a wrapper for a DatabaseManager service, and handles any RemoteExceptions internally.
 *
 */
public class DatabaseManagerCoreDelegate
    extends CommunityServiceCoreDelegate
    implements DatabaseManager, DatabaseManagerDelegate
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(DatabaseManagerCoreDelegate.class);

    /**
     * Public constructor.
     *
     */
    public DatabaseManagerCoreDelegate()
        {
        }

    /**
     * Our DatabaseManager service.
     *
     */
    private DatabaseManager manager = null ;

    /**
     * Get a reference to our DatabaseManager service.
     *
     */
    protected DatabaseManager getDatabaseManager()
        {
        return this.manager ;
        }

    /**
     * Set our our DatabaseManager service.
     *
     */
    protected void setDatabaseManager(DatabaseManager manager)
        {
        this.manager = manager ;
        }

    /**
     * Get the current database name.
     * @throws CommunityServiceException If there is an server error.
     *
     */
    public String getDatabaseName()
        throws CommunityServiceException
        {
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.getDatabaseName() ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Try converting the Exception.
                serviceException(ouch) ;
                //
                // If we get this far, then we don't know what it is.
                throw new CommunityServiceException(
                    "WebService call failed - " + ouch,
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
     * Get our JDO configuration resource name.
     * @throws CommunityServiceException If there is an server error.
     *
     */
    public String getDatabaseConfigResource()
        throws CommunityServiceException
        {
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.getDatabaseConfigResource() ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Try converting the Exception.
                serviceException(ouch) ;
                //
                // If we get this far, then we don't know what it is.
                throw new CommunityServiceException(
                    "WebService call failed - " + ouch,
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
     * Get the database SQL script name.
     * @throws CommunityServiceException If there is an server error.
     *
     */
    public String getDatabaseScriptResource()
        throws CommunityServiceException
        {
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.getDatabaseScriptResource() ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Try converting the Exception.
                serviceException(ouch) ;
                //
                // If we get this far, then we don't know what it is.
                throw new CommunityServiceException(
                    "WebService call failed - " + ouch,
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
     * Get the database configuration URL.
     * @throws CommunityServiceException If there is an server error.
     *
     */
    public String getDatabaseConfigUrl()
        throws CommunityServiceException
        {
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.getDatabaseConfigUrl() ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Try converting the Exception.
                serviceException(ouch) ;
                //
                // If we get this far, then we don't know what it is.
                throw new CommunityServiceException(
                    "WebService call failed - " + ouch,
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
     * Get the database engine description.
     * @throws CommunityServiceException If there is an server error.
     *
     */
    public String getDatabaseDescription()
        throws CommunityServiceException
        {
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.getDatabaseDescription() ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Try converting the Exception.
                serviceException(ouch) ;
                //
                // If we get this far, then we don't know what it is.
                throw new CommunityServiceException(
                    "WebService call failed - " + ouch,
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
     * Check the database tables.
     * @throws CommunityServiceException If there is an server error.
     *
     */
    public boolean checkDatabaseTables()
        throws CommunityServiceException
        {
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.checkDatabaseTables() ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Try converting the Exception.
                serviceException(ouch) ;
                //
                // If we get this far, then we don't know what it is.
                throw new CommunityServiceException(
                    "WebService call failed - " + ouch,
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
     * This method is only availabe through the build and test enviornment, it is NOT available as
     * a web service interface method in the actual release.
     * @throws CommunityServiceException If there is an server error.
     *
     */
    public void resetDatabaseTables()
        throws CommunityServiceException
        {
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                this.manager.resetDatabaseTables() ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Try converting the Exception.
                serviceException(ouch) ;
                //
                // If we get this far, then we don't know what it is.
                throw new CommunityServiceException(
                    "WebService call failed - " + ouch,
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
    }
