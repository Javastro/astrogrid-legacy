/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/manager/PolicyManagerSoapDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.8 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerSoapDelegate.java,v $
 *   Revision 1.8  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.7.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.7  2004/06/18 13:45:19  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.6.32.3  2004/06/17 15:10:03  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.6.32.2  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.policy.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URL ;
import java.net.MalformedURLException ;

import org.astrogrid.community.common.policy.manager.PolicyManagerService ;
import org.astrogrid.community.common.policy.manager.PolicyManagerServiceLocator ;

/**
 * Soap delegate for our PolicyManager service.
 *
 */
public class PolicyManagerSoapDelegate
    extends PolicyManagerCoreDelegate
    implements PolicyManagerDelegate
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(PolicyManagerSoapDelegate.class);

    /**
     * Our PolicyManager locator.
     *
     */
    private PolicyManagerService locator = new PolicyManagerServiceLocator() ;

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
     * Public constructor, for a specific endpoint URL.
     * @param endpoint The service endpoint URL.
     * @todo Trap null param.
     * @todo Trap MalformedURLException.
     *
     */
    public PolicyManagerSoapDelegate(String endpoint)
        throws MalformedURLException
        {
        this(new URL(endpoint)) ;
        }

    /**
     * Public constructor, for a specific endpoint URL.
     * @param endpoint The service endpoint URL.
     * @todo Better Exception handling.
     * @todo Trap null param.
     *
     */
    public PolicyManagerSoapDelegate(URL endpoint)
        {
        super() ;
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyManagerSoapDelegate()") ;
        log.debug("  URL : " + endpoint) ;
        //
        // Check for null param.
        if (null == endpoint)
            {
            throw new IllegalArgumentException(
                "Null endpoint"
                ) ;
            }
        //
        // Set our endpoint address.
        this.endpoint = endpoint ;
        //
        // Try getting our PolicyManager.
        try {
            this.setPolicyManager(
                locator.getPolicyManager(
                    this.endpoint
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
