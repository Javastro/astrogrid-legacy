/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/database/manager/DatabaseManagerCoreDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/30 01:40:03 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManagerCoreDelegate.java,v $
 *   Revision 1.5  2004/03/30 01:40:03  dave
 *   Merged development branch, dave-dev-200403242058, into HEAD
 *
 *   Revision 1.4.6.2  2004/03/28 09:11:43  dave
 *   Convert tabs to spaces
 *
 *   Revision 1.4.6.1  2004/03/28 02:00:55  dave
 *   Added database management tasks.
 *
 *   Revision 1.4  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.3.14.2  2004/03/19 03:31:20  dave
 *   Changed AccountManagerMock to recognise DatabaseManager reset()
 *
 *   Revision 1.3.14.1  2004/03/19 00:18:09  dave
 *   Refactored delegate Exception handling
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.database.manager ;

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
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

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
                convertServiceException(ouch) ;
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
                convertServiceException(ouch) ;
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
                convertServiceException(ouch) ;
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
                convertServiceException(ouch) ;
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
                convertServiceException(ouch) ;
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
                convertServiceException(ouch) ;
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
     * Reset our database tables.
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
                convertServiceException(ouch) ;
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
