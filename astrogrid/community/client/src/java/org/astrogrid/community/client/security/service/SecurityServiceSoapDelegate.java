/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/security/service/SecurityServiceSoapDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceSoapDelegate.java,v $
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
package org.astrogrid.community.client.security.service ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URL ;
import java.net.MalformedURLException ;

import org.astrogrid.community.common.security.service.SecurityServiceService ;
import org.astrogrid.community.common.security.service.SecurityServiceServiceLocator ;

/**
 * Soap delegate for our SecurityService service.
 *
 */
public class SecurityServiceSoapDelegate
    extends SecurityServiceCoreDelegate
    implements SecurityServiceDelegate
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(SecurityServiceSoapDelegate.class);

    /**
     * Public constructor.
     *
     */
    public SecurityServiceSoapDelegate()
        {
        super() ;
        }

    /**
     * Public constructor, for a specific endpoint URL.
     * @param endpoint The service endpoint URL.
     *
     */
    public SecurityServiceSoapDelegate(String endpoint)
        throws MalformedURLException
        {
        this(new URL(endpoint)) ;
        }

    /**
     * Public constructor, for a specific endpoint URL.
     * @param endpoint The service endpoint URL.
     *
     */
    public SecurityServiceSoapDelegate(URL endpoint)
        {
        super() ;
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("SecurityServiceSoapDelegate()") ;
        log.debug("  URL : " + endpoint) ;
        this.setEndpoint(endpoint) ;
        }

    /**
     * Our SecurityService locator.
     *
     */
    private SecurityServiceService locator = new SecurityServiceServiceLocator() ;

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
        log.debug("SecurityServiceSoapDelegate.setEndpoint()") ;
        log.debug("  URL : " + endpoint) ;
        //
        // Set our endpoint address.
        this.endpoint = endpoint ;
        //
        // Reset our SecurityService reference.
        this.setSecurityService(null) ;
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
                    // Try getting our SecurityService service.
                    this.setSecurityService(
                        locator.getSecurityService(
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
            }
        }
    }
