/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/security/manager/SecurityManagerCoreDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:19 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerCoreDelegate.java,v $
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
package org.astrogrid.community.client.security.manager ;

import java.rmi.RemoteException ;

import org.astrogrid.community.common.security.manager.SecurityManager ;
import org.astrogrid.community.client.service.CommunityServiceCoreDelegate ;

import org.astrogrid.community.common.exception.CommunityServiceException  ;
import org.astrogrid.community.common.exception.CommunitySecurityException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException  ;

/**
 * The core delegate code for our SecurityManager service.
 * This acts as a wrapper for a SecurityManager service, and handles any RemoteExceptions internally.
 *
 */
public class SecurityManagerCoreDelegate
    extends CommunityServiceCoreDelegate
    implements SecurityManager, SecurityManagerDelegate
    {
    /**
     * Public constructor.
     *
     */
    protected SecurityManagerCoreDelegate()
        {
        }

    /**
     * Our SecurityManager service.
     *
     */
    private SecurityManager manager = null ;

    /**
     * Get a reference to our SecurityManager service.
     *
     */
    protected SecurityManager getSecurityManager()
        {
        return this.manager ;
        }

    /**
     * Set our our SecurityManager service.
     *
     */
    protected void setSecurityManager(SecurityManager manager)
        {
        this.setCommunityService(manager) ;
        this.manager = manager ;
        }

    /**
     * Set an Account password.
     * @param account  The account ident.
     * @param password The account password.
     * @return True if the password was set.
     * @throws CommunitySecurityException If the password change fails.
     * @throws CommunityServiceException If there is an internal error in service.
     * @throws CommunityIdentifierException If the account identifier is invalid.
     *
     */
    public boolean setPassword(String ident, String value)
        throws CommunityServiceException, CommunitySecurityException, CommunityIdentifierException
        {
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.setPassword(ident, value) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Try converting the Exception.
                serviceException(ouch) ;
                securityException(ouch) ;
                identifierException(ouch) ;
                //
                // If we get this far, then we don't know what it is.
                throw new CommunityServiceException(
                    "WebService call failed - " + ouch,
                    ouch
                    ) ;
                }
            }
        //
        // If we don't have a valid service.
        else {
            throw new CommunityServiceException(
                "Service not initialised"
                ) ;
            }
        }
    }
