/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/security/service/SecurityServiceCoreDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceCoreDelegate.java,v $
 *   Revision 1.4  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.3.14.1  2004/03/19 00:18:09  dave
 *   Refactored delegate Exception handling
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.security.service ;

import java.rmi.RemoteException ;

import org.astrogrid.community.common.security.data.SecurityToken ;
import org.astrogrid.community.common.security.service.SecurityService ;

import org.astrogrid.community.client.service.CommunityServiceCoreDelegate ;

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
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

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
     * @param account - The account ident.
     * @param pass - The account password.
     * @return A valid SecurityToken if the ident and password are valid.
     *
     */
    public SecurityToken checkPassword(String account, String pass)
        {
        SecurityToken result = null ;
        //
        // If we have a valid service reference.
        if (null != this.service)
            {
            //
            // Try calling the service method.
            try {
                result = this.service.checkPassword(account, pass) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        return result ;
        }

    /**
     * Validate a SecurityToken.
     * Validates a token, and creates a new tokens issued to the same account.
     * Note, this uses the original token, which now becomes invalid.
     * The client should use the new token for subsequent calls to the service.
     * @param - The token to validate.
     * @return A new SecurityToken if the original was valid.
     *
     */
    public SecurityToken checkToken(SecurityToken token)
        {
        SecurityToken result = null ;
        //
        // If we have a valid service reference.
        if (null != this.service)
            {
            //
            // Try calling the service method.
            try {
                result = this.service.checkToken(token) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        return result ;
        }

    /**
     * Split a SecurityToken.
     * Validates a token, and then creates a new set of tokens issued to the same account.
     * Note, this uses the original token, which now becomes invalid.
     * The client should use the first token in the array for subsequent calls to the service.
     * @param - The token to validate.
     * @param - The number of new tokens required.
     * @return An array of new tokens.
     *
     */
    public Object[] splitToken(SecurityToken token, int count)
        {
        Object[] result = null ;
        //
        // If we have a valid service reference.
        if (null != this.service)
            {
            //
            // Try calling the service method.
            try {
                result = this.service.splitToken(token, count) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        return result ;
        }
    }
