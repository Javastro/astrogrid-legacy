/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/ant/ServiceStatusTask.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:19 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ServiceStatusTask.java,v $
 *   Revision 1.3  2004/06/18 13:45:19  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.2.60.1  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
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

