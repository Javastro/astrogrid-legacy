/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/manager/PolicyManagerSoapDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/08 13:42:33 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerSoapDelegate.java,v $
 *   Revision 1.3  2004/03/08 13:42:33  dave
 *   Updated Maven goals.
 *   Replaced tabs with Spaces.
 *
 *   Revision 1.2.2.1  2004/03/08 12:53:17  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.2  2004/03/05 14:03:23  dave
 *   Added first client side SOAP test - SecurityServiceSoapDelegateTestCase
 *
 *   Revision 1.1.2.1  2004/03/04 13:26:17  dave
 *   1) Added Delegate interfaces.
 *   2) Added Mock implementations.
 *   3) Added MockDelegates
 *   4) Added SoapDelegates
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.policy.manager ;

import java.net.URL ;
import java.net.MalformedURLException ;

import org.astrogrid.community.common.policy.manager.PolicyManager ;
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
     * Public constructor.
     *
     */
    public PolicyManagerSoapDelegate()
        {
        super() ;
        }

    /**
     * Public constructor, for a specific endpoint URL.
     * @param endpoint The service endpoint URL.
     *
     */
    public PolicyManagerSoapDelegate(String endpoint)
        throws MalformedURLException
        {
        this(new URL(endpoint)) ;
        }

    /**
     * Public constructor, for a specific endpoint URL.
     * @param endpoint - The service endpoint URL.
     *
     */
    public PolicyManagerSoapDelegate(URL endpoint)
        {
        super() ;
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerSoapDelegate()") ;
        if (DEBUG_FLAG) System.out.println("  URL : " + endpoint) ;
        this.setEndpoint(endpoint) ;
        }

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
     * Set our endpoint address.
     *
     */
    public void setEndpoint(URL endpoint)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerSoapDelegate.setEndpoint()") ;
        if (DEBUG_FLAG) System.out.println("  URL : " + endpoint) ;
        //
        // Set our endpoint address.
        this.endpoint = endpoint ;
        //
        // Reset our PolicyManager reference.
        this.setPolicyManager(null) ;
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
                    // Try getting our PolicyManager service.
                    this.setPolicyManager(
                        locator.getPolicyManager(
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
                // Set our manager to null and log it.
                this.setPolicyManager(null) ;
                }
            }
        //
        // If we don't have a valid endpoint.
        else {
            //
            // Set our manager to null and log it.
            this.setPolicyManager(null) ;
            }
        }
    }
