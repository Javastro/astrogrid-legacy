/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/install/src/java/org/astrogrid/community/install/ant/DatabaseManagerTask.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/30 01:40:03 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManagerTask.java,v $
 *   Revision 1.2  2004/03/30 01:40:03  dave
 *   Merged development branch, dave-dev-200403242058, into HEAD
 *
 *   Revision 1.1.2.4  2004/03/29 11:44:54  dave
 *   Fixed bugs in Ant tasks
 *
 *   Revision 1.1.2.3  2004/03/28 09:11:43  dave
 *   Convert tabs to spaces
 *
 *   Revision 1.1.2.2  2004/03/28 08:37:10  dave
 *   Fixes to test data
 *
 *   Revision 1.1.2.1  2004/03/28 02:00:55  dave
 *   Added database management tasks.
 *
 * </cvs:log>
 *
 *
 */
package org.astrogrid.community.install.ant ;

import java.net.URL ;

import org.apache.tools.ant.Task ;
import org.apache.tools.ant.BuildException ;

import org.astrogrid.community.client.database.manager.DatabaseManagerSoapDelegate ;

/**
 * An Ant task to control the Community DatabaseManager service.
 * 
 */
public class DatabaseManagerTask
    extends Task
    {
    /**
     * Our debug flag.
     *
     */
    private static final boolean DEBUG_FLAG = true ;

    /**
     * Public constructor.
     *
     */
    public DatabaseManagerTask()
        {
        //
        // Initialise our base class.
        super() ;
        }

    /**
     * Public constructor.
     *
     */
    public DatabaseManagerTask(Task parent)
        {
        //
        // Initialise our base class.
        super() ;
        //
        // Set our project.
        setProject(parent.getProject()) ;
        }

    /**
     * Initialise our Task.
     *
     */
    public void init()
        throws BuildException
        {
        if (DEBUG_FLAG) System.out.println("----\"----");
        if (DEBUG_FLAG) System.out.println("DatabaseManagerTask.init()");
        }

    /**
     * Our DatabaseManager service endpoint.
     *
     */
    private String endpoint ;

    /**
     * Get our DatabaseManager service endpoint.
     *
     */
    public String getEndpoint()
        {
        return this.endpoint ;
        }

    /**
     * Set our DatabaseManager service endpoint.
     *
     */
    public void setEndpoint(String value)
        {
        this.endpoint = value ;
        }

    /**
     * The action we want to perform.
     *
     */
    private String action ;

    /**
     * Get the action we want to perform.
     *
     */
    public String getAction()
        {
        return this.action ;
        }

    /**
     * Set the action we want to perform.
     *
     */
    public void setAction(String value)
        {
        this.action = value ;
        }

    /**
     * Execute our Task.
     *
     */
    public void execute()
        throws BuildException
        {
        if (DEBUG_FLAG) System.out.println("----\"----");
        if (DEBUG_FLAG) System.out.println("DatabaseManagerTask.execute()");
        if (DEBUG_FLAG) System.out.println("  Action   : " + this.action);
        if (DEBUG_FLAG) System.out.println("  Endpoint : " + this.endpoint);
        //
        // Check for null action.
        if (null == action)
            {
            throw new BuildException(
                "No action specified"
                ) ;
            }
        //
        // Check for null endpoint.
        if (null == endpoint)
            {
            throw new BuildException(
                "No service endpoint specified"
                ) ;
            }
        //
        // Try performing our action.
        try {
            //
            // Create our SOAP delegate.
            DatabaseManagerSoapDelegate delegate = new DatabaseManagerSoapDelegate(
                new URL(this.endpoint)
                ) ;
            //
            // Check the database status.
            if ("debug".equals(action))
                {
                if (DEBUG_FLAG) System.out.println("  Database name   : " + delegate.getDatabaseName());
                if (DEBUG_FLAG) System.out.println("  Database config : " + delegate.getDatabaseConfigUrl());
                if (DEBUG_FLAG) System.out.println("  Database script : " + delegate.getDatabaseScriptResource());
                }
            //
            // Check the database tables.
            if ("status".equals(action))
                {
                boolean status = delegate.checkDatabaseTables() ;
                if (DEBUG_FLAG) System.out.println("  Database tables healthy : " + status);
                }
            //
            // Reset the database tables.
            if ("reset".equals(action))
                {
                if (DEBUG_FLAG) System.out.println("  Resetting database tables ....");
                delegate.resetDatabaseTables() ;
                boolean status = delegate.checkDatabaseTables() ;
                if (DEBUG_FLAG) System.out.println("  Database tables healthy : " + status);
                }
            }
        catch (Exception ouch)
            {
            throw new BuildException(ouch) ;
            }
        }
    }

