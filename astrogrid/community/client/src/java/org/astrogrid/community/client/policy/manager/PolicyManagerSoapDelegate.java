/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/manager/PolicyManagerSoapDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:19 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerSoapDelegate.java,v $
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
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerSoapDelegate()") ;
        if (DEBUG_FLAG) System.out.println("  URL : " + endpoint) ;
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
