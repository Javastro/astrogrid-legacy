/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/install/src/java/org/astrogrid/community/install/ant/CommunityRegistryTask.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/04/01 07:09:39 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityRegistryTask.java,v $
 *   Revision 1.2  2004/04/01 07:09:39  dave
 *   Merged development branch, dave-dev-200403300547, into HEAD
 *
 *   Revision 1.1.2.1  2004/04/01 06:50:36  dave
 *   Added install registry test data.
 *   Updated resolver doecs.
 *
 * </cvs:log>
 *
 *
 */
package org.astrogrid.community.install.ant ;

import java.io.File ;
import java.net.URL ;

import org.apache.tools.ant.Task ;
import org.apache.tools.ant.BuildException ;

import org.astrogrid.registry.client.admin.RegistryAdminService ;

/**
 * An Ant task to register a Community service woth a remote Registry.
 * 
 */
public class CommunityRegistryTask
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
    public CommunityRegistryTask()
        {
        //
        // Initialise our base class.
        super() ;
        }

    /**
     * Public constructor.
     *
     */
    public CommunityRegistryTask(Task parent)
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
        if (DEBUG_FLAG) System.out.println("CommunityRegistryTask.init()");
        }

    /**
     * Our Registry service endpoint.
     *
     */
    private String registry ;

    /**
     * Get our Registry service endpoint.
     *
     */
    public String getRegistry()
        {
        return this.registry ;
        }

    /**
     * Set our Registry service endpoint.
     *
     */
    public void setRegistry(String value)
        {
        this.registry = value ;
        }

    /**
     * Our Registry data file.
     *
     */
    private String data ;

    /**
     * Get our Registry data file.
     *
     */
    public String getData()
        {
        return this.data ;
        }

    /**
     * Set our Registry data file.
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
        if (DEBUG_FLAG) System.out.println("CommunityRegistryTask.execute()");
        if (DEBUG_FLAG) System.out.println("  Data     : " + this.data);
        if (DEBUG_FLAG) System.out.println("  Registry : " + this.registry);
        //
        // Try registering our services.
        try {
			//
			// Create our RegistryAdminService client.
			RegistryAdminService registry = new RegistryAdminService(
				new URL(
					this.registry
					)
				) ;
			//
			// Send the document to the service.
			registry.updateFromFile(
				new File(
					this.data
					)
				) ;
            }
        catch (Exception ouch)
            {
            throw new BuildException(ouch) ;
            }
        }
    }

