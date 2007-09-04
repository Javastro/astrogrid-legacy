/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/install/src/java/org/astrogrid/community/install/ant/CommunityRegistryTask.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2007/09/04 15:16:40 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityRegistryTask.java,v $
 *   Revision 1.6  2007/09/04 15:16:40  clq2
 *   KMB brach changes for PAL
 *
 *   Revision 1.5.218.1  2007/08/16 12:22:49  KevinBenson
 *   small change to use some changes made to the registry client dealing with 0.1 and 1.0 contracts and now an abstract super class in the reg client.
 *
 *   Revision 1.5  2004/07/23 11:56:09  KevinBenson
 *   Small change to use the new RegistryUpdate delegate and new REgistryUpdate endpoint
 *
 *   Revision 1.4  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.3.18.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
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
import org.astrogrid.registry.client.RegistryDelegateFactory ;

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
            /*
            RegistryAdminService registry = new RegistryAdminService(
                new URL(
                    this.registry
                    )
                ) ;
                */
            RegistryAdminService registry = RegistryDelegateFactory.createAdmin(
              new URL(
                 this.registry
              )
            );
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

