/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/install/src/java/org/astrogrid/community/install/ant/CommunityLoaderTask.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/30 01:40:03 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityLoaderTask.java,v $
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

import org.astrogrid.community.install.loader.CommunityLoader ;
import org.astrogrid.community.client.policy.manager.PolicyManagerSoapDelegate ;
import org.astrogrid.community.client.security.manager.SecurityManagerSoapDelegate ;

/**
 * An Ant task to load data into a Community service.
 * 
 */
public class CommunityLoaderTask
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
    public CommunityLoaderTask()
        {
        //
        // Initialise our base class.
        super() ;
        }

    /**
     * Public constructor.
     *
     */
    public CommunityLoaderTask(Task parent)
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
        if (DEBUG_FLAG) System.out.println("CommunityLoaderTask.init()");
        }

    /**
     * Our PolicyManager service endpoint.
     *
     */
    private String policyManager ;

    /**
     * Get our PolicyManager service endpoint.
     *
     */
    public String getPolicyManager()
        {
        return this.policyManager ;
        }

    /**
     * Set our PolicyManager service endpoint.
     *
     */
    public void setPolicyManager(String value)
        {
        this.policyManager = value ;
        }

    /**
     * Our SecurityManager service endpoint.
     *
     */
    private String securityManager ;

    /**
     * Get our SecurityManager service endpoint.
     *
     */
    public String getSecurityManager()
        {
        return this.securityManager ;
        }

    /**
     * Set our SecurityManager service endpoint.
     *
     */
    public void setSecurityManager(String value)
        {
        this.securityManager = value ;
        }

    /**
     * Our data file location.
     *
     */
    private String data ;

    /**
     * Get our data file location.
     *
     */
    public String getData()
        {
        return this.data ;
        }

    /**
     * Set our data file location.
     *
     */
    public void setData(String value)
        {
        this.data = value ;
        }

    /**
     * Execute our Task.
     *
     */
    public void execute()
        throws BuildException
        {
        if (DEBUG_FLAG) System.out.println("----\"----");
        if (DEBUG_FLAG) System.out.println("CommunityLoaderTask.execute()");
        if (DEBUG_FLAG) System.out.println("  Data : " + this.data);
        if (DEBUG_FLAG) System.out.println("  PolicyManager   : " + this.policyManager);
        if (DEBUG_FLAG) System.out.println("  SecurityManager : " + this.securityManager);
        //
        // Try loading our data.
        try {
            //
            // Create our data loader and SOAP delegates.
            CommunityLoader loader = new CommunityLoader(
                new PolicyManagerSoapDelegate(
                    new URL(
                        this.policyManager
                        )
                    ),
                new SecurityManagerSoapDelegate(
                    new URL(
                        this.securityManager
                        )
                    )
                ) ;
            //
            // Load our data.
            loader.load(
                new URL(
                    this.data
                    )
                ) ;
            //
            // Upload the data to our service.
            loader.upload() ;
            }
        catch (Exception ouch)
            {
            throw new BuildException(ouch) ;
            }
        }
    }

