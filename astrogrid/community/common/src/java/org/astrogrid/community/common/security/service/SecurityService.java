/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/security/service/SecurityService.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityService.java,v $
 *   Revision 1.5  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.4.18.2  2004/03/23 16:28:49  dave
 *   Fixed JavaDocs.
 *
 *   Revision 1.4.18.1  2004/03/22 15:31:10  dave
 *   Added CommunitySecurityException.
 *   Updated SecurityManager and SecurityService to use Exceptions.
 *
 *   Revision 1.4  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.3.2.2  2004/03/02 15:29:35  dave
 *   Working round Castor problem with PasswordData - objects remain in database cache
 *
 *   Revision 1.3.2.1  2004/02/27 16:22:14  dave
 *   Added SecurityService interface, mock and test
 *
 *   Revision 1.3  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.2.2.2  2004/02/19 21:09:26  dave
 *   Refactored ServiceStatusData into a common package.
 *   Refactored CommunityServiceImpl constructor to take a parent service.
 *   Refactored default database for CommunityServiceImpl
 *
 *   Revision 1.2.2.1  2004/02/16 15:20:54  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.1.2.4  2004/02/06 16:19:05  dave
 *   Replaced import java.rmi.Remote
 *   Replaced import java.rmi.RemoteException
 *
 *   Revision 1.1.2.3  2004/02/06 16:15:49  dave
 *   Removed import java.rmi.RemoteException
 *
 *   Revision 1.1.2.2  2004/02/06 16:14:17  dave
 *   Removed import java.rmi.Remote
 *
 *   Revision 1.1.2.1  2004/01/30 03:21:23  dave
 *   Added initial code for SecurityManager and SecurityService
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.security.service ;

import java.rmi.Remote ;
import java.rmi.RemoteException ;

import org.astrogrid.community.common.security.data.SecurityToken ;

import org.astrogrid.community.common.service.CommunityService ;

import org.astrogrid.community.common.exception.CommunityServiceException  ;
import org.astrogrid.community.common.exception.CommunitySecurityException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException  ;

/**
 * Public interface for our SecurityService.
 *
 */
public interface SecurityService
    extends Remote, CommunityService
    {

    /**
     * Check an Account password.
     * @param account  The account ident.
     * @param password The account password.
     * @return A valid SecurityToken if the ident and password are valid.
     * @throws CommunitySecurityException If the security check fails.
     * @throws CommunityServiceException If there is an internal error in service.
     * @throws CommunityIdentifierException If the account identifier is invalid.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public SecurityToken checkPassword(String account, String pass)
        throws RemoteException, CommunityServiceException, CommunitySecurityException, CommunityIdentifierException ;

    /**
     * Validate a SecurityToken.
     * Validates a token, and creates a new tokens issued to the same account.
     * @param token The token to validate.
     * @return A new SecurityToken if the original was valid.
     * @throws CommunitySecurityException If the security check fails.
     * @throws CommunityServiceException If there is an internal error in service.
     * @throws CommunityIdentifierException If the token is invalid.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public SecurityToken checkToken(SecurityToken token)
        throws RemoteException, CommunityServiceException, CommunitySecurityException, CommunityIdentifierException ;

    /**
     * Split a SecurityToken.
     * Validates a token, and then creates a new set of tokens issued to the same account.
     * @param token The token to validate.
     * @param count The number of new tokens required.
     * @return An array of new SecurityToken(s).
     * @throws CommunitySecurityException If the security check fails.
     * @throws CommunityServiceException If there is an internal error in service.
     * @throws CommunityIdentifierException If the token is invalid.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public Object[] splitToken(SecurityToken token, int count)
        throws RemoteException, CommunityServiceException, CommunitySecurityException, CommunityIdentifierException ;

    }
