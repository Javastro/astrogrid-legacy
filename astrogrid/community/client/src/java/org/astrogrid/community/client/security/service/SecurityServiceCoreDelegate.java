/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/security/service/SecurityServiceCoreDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.9 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceCoreDelegate.java,v $
 *   Revision 1.9  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.8.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.8  2004/06/18 13:45:19  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.7.32.3  2004/06/17 15:10:03  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.7.32.2  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.security.service ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.rmi.RemoteException ;

import org.astrogrid.community.common.security.data.SecurityToken ;
import org.astrogrid.community.common.security.service.SecurityService ;

import org.astrogrid.community.client.service.CommunityServiceCoreDelegate ;

import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunitySecurityException   ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

/**
 * The core delegate code for our SecurityService service.
 * This acts as a wrapper for a SecurityService service, and handles any RemoteExceptions internally.
 *
 */
public class SecurityServiceCoreDelegate
    extends CommunityServiceCoreDelegate
    implements SecurityService, SecurityServiceDelegate
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(SecurityServiceCoreDelegate.class);

    /**
     * Public constructor.
     *
     */
    public SecurityServiceCoreDelegate()
        {
        }

    /**
     * Our SecurityService service.
     *
     */
    private SecurityService service = null ;

    /**
     * Get a reference to our SecurityService service.
     *
     */
    protected SecurityService getSecurityService()
        {
        return this.service ;
        }

    /**
     * Set our our SecurityService service.
     *
     */
    protected void setSecurityService(SecurityService service)
        {
        this.setCommunityService(service) ;
        this.service = service ;
        }

    /**
     * Check an Account password.
     * @param account  The account ident.
     * @param password The account password.
     * @return A valid SecurityToken if the ident and password are valid.
     * @throws CommunitySecurityException If the security check fails.
     * @throws CommunityServiceException If there is an internal error in service.
     * @throws CommunityIdentifierException If the account identifier is invalid.
     *
     */
    public SecurityToken checkPassword(String account, String pass)
        throws CommunityServiceException, CommunitySecurityException, CommunityIdentifierException
        {
        //
        // If we have a valid service reference.
        if (null != this.service)
            {
            //
            // Try calling the service method.
            try {
                return this.service.checkPassword(account, pass) ;
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

    /**
     * Validate a SecurityToken.
     * Validates a token, and creates a new tokens issued to the same account.
     * @param token The token to validate.
     * @return A new SecurityToken if the original was valid.
     * @throws CommunitySecurityException If the security check fails.
     * @throws CommunityServiceException If there is an internal error in service.
     * @throws CommunityIdentifierException If the token is invalid.
     * @todo This should make the original token invalid IF it has been used.
     *
     */
    public SecurityToken checkToken(SecurityToken token)
        throws CommunityServiceException, CommunitySecurityException, CommunityIdentifierException
        {
        //
        // If we have a valid service reference.
        if (null != this.service)
            {
            //
            // Try calling the service method.
            try {
                return this.service.checkToken(token) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Set the token staus.
                token.setStatus(SecurityToken.INVALID_TOKEN) ;
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

    /**
     * Split a SecurityToken.
     * Validates a token, and then creates a new set of tokens issued to the same account.
     * @throws CommunitySecurityException If the security check fails.
     * @throws CommunityServiceException If there is an internal error in service.
     * @throws CommunityIdentifierException If the token is invalid.
     * @todo This should make the original token invalid
     * @todo This should make the original token invalid IF it has been used.
     *
     */
    public Object[] splitToken(SecurityToken token, int count)
        throws CommunityServiceException, CommunitySecurityException, CommunityIdentifierException
        {
        //
        // If we have a valid service reference.
        if (null != this.service)
            {
            //
            // Try calling the service method.
            try {
                return this.service.splitToken(token, count) ;
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
