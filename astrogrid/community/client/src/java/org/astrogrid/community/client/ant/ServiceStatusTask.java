/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/ant/ServiceStatusTask.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/20 21:11:05 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ServiceStatusTask.java,v $
 *   Revision 1.2  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.1.2.3  2004/02/19 21:09:26  dave
 *   Refactored ServiceStatusData into a common package.
 *   Refactored CommunityServiceImpl constructor to take a parent service.
 *   Refactored default database for CommunityServiceImpl
 *
 *   Revision 1.1.2.2  2004/02/16 15:20:54  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.1.2.1  2004/02/14 22:24:09  dave
 *   Test toolkit for the install and tomcat sub-projects
 *
 * </cvs:log>
 *
 *
 */
package org.astrogrid.community.client.ant ;

import java.net.URL;

import org.apache.tools.ant.Task ;
import org.apache.tools.ant.BuildException ;

import org.astrogrid.community.common.service.data.ServiceStatusData ;

import org.astrogrid.community.common.policy.service.PolicyService ;
import org.astrogrid.community.common.policy.service.PolicyServiceService ;
import org.astrogrid.community.common.policy.service.PolicyServiceServiceLocator ;

/**
 * An Ant task to check our service status.
 * 
 */
public class ServiceStatusTask
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
    public ServiceStatusTask()
        {
        //
        // Initialise our base class.
        super() ;
        }

    /**
     * Public constructor.
     *
     */
    public ServiceStatusTask(Task parent)
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
        if (DEBUG_FLAG) log("----\"----");
        if (DEBUG_FLAG) log("ServiceStatusTask.init()");
        }

    /**
     * The service type to test.
     *
     */
    private String type ;

    /**
     * Get the service type.
     *
     */
    public String getType()
        {
        return this.type ;
        }

    /**
     * Set the service type.
     *
     */
    public void setType(String value)
        {
        this.type = value ;
        }

    /**
     * The service endpoint address.
     *
     */
    private String address ;

    /**
     * Get the service endpoint.
     *
     */
    public String getAddress()
        {
        return this.address ;
        }

    /**
     * Set the service endpoint address.
     *
     */
    public void setAddress(String value)
        {
        this.address = value ;
        }

    /**
     * Execute our Task.
     *
     */
    public void execute()
        throws BuildException
        {
        if (DEBUG_FLAG) log("----\"----");
        if (DEBUG_FLAG) log("ServiceStatusTask.execute()");
        if (DEBUG_FLAG) log("  Address : " + this.getAddress());
        //
        // Assume a policy service for now ...
        try {
            //
            // Create our service locator.
            PolicyServiceService locator = new PolicyServiceServiceLocator();
            //
            // Create our URL.
            URL endpoint = new URL(address) ;
            //
            // Create our service.
            PolicyService service = locator.getPolicyService(endpoint);
            //
            // Get the service status.
            ServiceStatusData status = service.getServiceStatus() ;
            //
            // Print out the status.
            log("Service status") ;
            log("  Config path   : " + status.getConfigPath()) ;
            log("  Database name : " + status.getDatabaseName()) ;
/*
 *
 *
 */
            }
        catch (Exception ouch)
            {
            throw new BuildException(ouch) ;
            }
        }
    }

