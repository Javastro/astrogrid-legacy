/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/security/service/SecurityServiceDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/08 13:42:33 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceDelegate.java,v $
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

import org.astrogrid.community.common.service.data.ServiceStatusData ;

import org.astrogrid.community.common.security.data.SecurityToken ;
import org.astrogrid.community.common.security.service.SecurityService ;

/**
 * Public interface for our SecurityService delegate.
 * This extends the SecurityService interface, without the RemoteExceptions.
 *
 */
public interface SecurityServiceDelegate
    extends SecurityService
    {
    /**
     * Check an Account password.
     * @param account - The account ident.
     * @param pass - The account password.
     * @return A valid SecurityToken if the ident and password are valid.
     *
     */
    public SecurityToken checkPassword(String account, String pass) ;

    /**
     * Validate a SecurityToken.
     * Validates a token, and creates a new tokens issued to the same account.
     * Note, this uses the original token, which now becomes invalid.
     * The client should use the new token for subsequent calls to the service.
     * @param - The token to validate.
     * @return A new SecurityToken if the original was valid.
     *
     */
    public SecurityToken checkToken(SecurityToken token) ;

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
    public Object[] splitToken(SecurityToken token, int count) ;

    /**
     * Service health check.
     * @return ServiceStatusData with details of the Service status.
     *
     */
    public ServiceStatusData getServiceStatus() ;

    }
