/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/database/manager/DatabaseManagerCoreDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/08 13:42:33 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManagerCoreDelegate.java,v $
 *   Revision 1.3  2004/03/08 13:42:33  dave
 *   Updated Maven goals.
 *   Replaced tabs with Spaces.
 *
 *   Revision 1.2.2.1  2004/03/08 12:53:17  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/04 17:13:30  dave
 *   Added DatabaseManager delegates
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.database.manager ;

import java.rmi.RemoteException ;

import org.astrogrid.community.common.service.data.ServiceStatusData ;

import org.astrogrid.community.common.database.manager.DatabaseManager ;

/**
 * The core delegate code for our DatabaseManager service.
 * This acts as a wrapper for a DatabaseManager service, and handles any RemoteExceptions internally.
 *
 */
public class DatabaseManagerCoreDelegate
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
     *
     */
    public String getDatabaseName()
        {
        String result = null ;
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                result = this.manager.getDatabaseName() ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        return result ;
        }

    /**
     * Get our JDO configuration resource name.
     *
     */
    public String getDatabaseConfigResource()
        {
        String result = null ;
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                result = this.manager.getDatabaseConfigResource() ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        return result ;
        }

    /**
     * Get the database SQL script name.
     *
     */
    public String getDatabaseScriptResource()
        {
        String result = null ;
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                result = this.manager.getDatabaseScriptResource() ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        return result ;
        }

    /**
     * Get the database configuration URL.
     *
     */
    public String getDatabaseConfigUrl()
        {
        String result = null ;
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                result = this.manager.getDatabaseConfigUrl() ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        return result ;
        }

    /**
     * Get the database engine description.
     *
     */
    public String getDatabaseDescription()
        {
        String result = null ;
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                result = this.manager.getDatabaseDescription() ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        return result ;
        }

    /**
     * Check the database tables.
     *
     */
    public boolean checkDatabaseTables()
        {
        boolean result = false ;
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                result = this.manager.checkDatabaseTables() ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        return result ;
        }

    /**
     * Reset our database tables.
     *
     */
    public void resetDatabaseTables()
        {
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                this.manager.getDatabaseName() ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        }

    /**
     * Service health check.
     * @return ServiceStatusData with details of the Service status.
     * TODO -refactor this to a base class
     *
     */
    public ServiceStatusData getServiceStatus()
        {
        ServiceStatusData result = null ;
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                result = this.manager.getServiceStatus() ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        return result ;
        }
    }
