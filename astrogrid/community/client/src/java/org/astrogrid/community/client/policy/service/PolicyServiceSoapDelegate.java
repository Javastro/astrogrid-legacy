/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/service/PolicyServiceSoapDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyServiceSoapDelegate.java,v $
 *   Revision 1.5  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.4.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.4  2004/06/18 13:45:19  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.3.52.2  2004/06/17 15:10:03  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.3.52.1  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.policy.service ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URL ;
import java.net.MalformedURLException ;

import org.astrogrid.community.common.policy.service.PolicyServiceService ;
import org.astrogrid.community.common.policy.service.PolicyServiceServiceLocator ;

/**
 * Soap delegate for our PolicyService service.
 *
 */
public class PolicyServiceSoapDelegate
    extends PolicyServiceCoreDelegate
    implements PolicyServiceDelegate
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(PolicyServiceSoapDelegate.class);

    /**
     * Public constructor.
     *
     */
    public PolicyServiceSoapDelegate()
        {
        super() ;
        }

    /**
     * Public constructor, for a specific endpoint URL.
     * @param endpoint The service endpoint URL.
     *
     */
    public PolicyServiceSoapDelegate(String endpoint)
        throws MalformedURLException
        {
        this(new URL(endpoint)) ;
        }

    /**
     * Public constructor, for a specific endpoint URL.
     * @param endpoint - The service endpoint URL.
     *
     */
    public PolicyServiceSoapDelegate(URL endpoint)
        {
        super() ;
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyServiceSoapDelegate()") ;
        log.debug("  URL : " + endpoint) ;
        this.setEndpoint(endpoint) ;
        }

    /**
     * Our PolicyService locator.
     *
     */
    private PolicyServiceService locator = new PolicyServiceServiceLocator() ;

    /**
     * Our endpoint address.
     *
     */
    private URL endpoint ;

    /**
     * Get our endpoint address.
     *
     */
    public URL getEndpoint()
        {
        return this.endpoint ;
        }

    /**
     * Set our endpoint address.
     * @todo Better Exception handling.
     *
     */
    public void setEndpoint(URL endpoint)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyServiceSoapDelegate.setEndpoint()") ;
        log.debug("  URL : " + endpoint) ;
        //
        // Set our endpoint address.
        this.endpoint = endpoint ;
        //
        // Reset our PolicyService reference.
        this.setPolicyService(null) ;
        //
        // If we have a valid endpoint.
        if (null != this.getEndpoint())
            {
            //
            // If we have a valid locator.
            if (null != locator)
                {
                try {
                    //
                    // Try getting our PolicyService service.
                    this.setPolicyService(
                        locator.getPolicyService(
                            this.getEndpoint()
                            )
                        ) ;
                    }
                //
                // Catch anything that went BANG.
                catch (Exception ouch)
                    {
                    // TODO
                    // Log the Exception, and throw a nicer one.
                    // Unwrap RemoteExceptions.
                    //
                    }
                }
            //
            // If we don't have a valid locator.
            else {
                //
                // Set our service to null and log it.
                this.setPolicyService(null) ;
                }
            }
        //
        // If we don't have a valid endpoint.
        else {
            //
            // Set our service to null and log it.
            this.setPolicyService(null) ;
            }
        }
    }
