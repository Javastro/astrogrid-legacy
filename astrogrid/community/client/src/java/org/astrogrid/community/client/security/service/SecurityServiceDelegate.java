/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/security/service/SecurityServiceDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceDelegate.java,v $
 *   Revision 1.5  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.4.2.3  2004/03/23 16:28:49  dave
 *   Fixed JavaDocs.
 *
 *   Revision 1.4.2.2  2004/03/23 15:51:46  dave
 *   Added CommunityTokenResolver and CommunityPasswordResolver.
 *
 *   Revision 1.4.2.1  2004/03/22 16:47:55  dave
 *   Updated SecurityManagerDelegate to include Exceptions.
 *   Updated SecurityServiceDelegate to include Exceptions.
 *
 *   Revision 1.4  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.3.14.1  2004/03/19 00:18:09  dave
 *   Refactored delegate Exception handling
 *
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
 *   Revision 1.1.2.1  2004/03/04 08:57:10  dave
 *   Started work on the install xdocs.
 *   Started work on the Security delegates.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.security.service ;

import org.astrogrid.community.common.security.data.SecurityToken ;

import org.astrogrid.community.client.service.CommunityServiceDelegate ;

import org.astrogrid.community.common.exception.CommunityServiceException  ;
import org.astrogrid.community.common.exception.CommunitySecurityException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException  ;

/**
 * Public interface for our SecurityService delegate.
 * This mirrors the SecurityService interface, without the RemoteExceptions.
 *
 */
public interface SecurityServiceDelegate
    extends CommunityServiceDelegate
    {
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
        throws CommunityServiceException, CommunitySecurityException, CommunityIdentifierException ;

    /**
     * Validate a SecurityToken.
     * Validates a token, and creates a new token issued to the same account.
     * @param token The token to validate.
     * @return A new SecurityToken if the original was valid.
     * @throws CommunitySecurityException If the security check fails.
     * @throws CommunityServiceException If there is an internal error in service.
     * @throws CommunityIdentifierException If the token is invalid.
     *
     */
    public SecurityToken checkToken(SecurityToken token)
        throws CommunityServiceException, CommunitySecurityException, CommunityIdentifierException ;

    /**
     * Split a SecurityToken.
     * Validates a token, and then creates a new set of tokens issued to the same account.
     * @param token The token to validate.
     * @param count The number of new tokens required.
     * @return An array of new SecurityToken(s).
     * @throws CommunitySecurityException If the security check fails.
     * @throws CommunityServiceException If there is an internal error in service.
     * @throws CommunityIdentifierException If the token is invalid.
     *
     */
    public Object[] splitToken(SecurityToken token, int count)
        throws CommunityServiceException, CommunitySecurityException, CommunityIdentifierException ;

    }
