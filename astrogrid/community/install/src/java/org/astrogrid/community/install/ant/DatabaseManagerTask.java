/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/install/src/java/org/astrogrid/community/install/ant/DatabaseManagerTask.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/08/04 09:40:11 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManagerTask.java,v $
 *   Revision 1.4  2005/08/04 09:40:11  clq2
 *   kevin's second batch
 *
 *   Revision 1.3.182.1  2005/07/28 13:35:53  KevinBenson
 *   No longer uses resetDatabaseTables as a web service method it is not allowed anymore.
 *
 *   Revision 1.3  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.2.32.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
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
            }
        catch (Exception ouch)
            {
            throw new BuildException(ouch) ;
            }
        }
    }

