/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/ivorn/CommunityAccountIvornFactory.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityAccountIvornFactory.java,v $
 *   Revision 1.5  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.4.32.2  2004/06/17 14:50:01  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.4.32.1  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.ivorn ;

import java.net.URISyntaxException ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.common.ivorn.MockIvorn ;

import org.astrogrid.community.common.exception.CommunityServiceException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

/**
 * A factory for generating Community Account identifiers.
 *
 */
public class CommunityAccountIvornFactory
    extends CommunityIvornFactory
    {
    /**
     * Create a local Community Account Ivorn.
     * @param account   The Account name, without the Community ident.
     * @throws CommunityIdentifierException If the identifiers are not valid.
     * @throws CommunityServiceException If the local Community identifier is not set.
     *
     */
    public static Ivorn createLocal(String account)
        throws CommunityServiceException, CommunityIdentifierException
        {
        return createIvorn(
            CommunityIvornParser.getLocalIdent(),
            account
            ) ;
        }

    /**
     * Create a local Community Account Ivorn.
     * @param account   The Account name, without the Community ident.
     * @param resource  The resource, added after the account.
     * @throws CommunityIdentifierException If the identifiers are not valid.
     * @throws CommunityServiceException If the local Community identifier is not set.
     *
     */
    public static Ivorn createLocal(String account, String resource)
        throws CommunityServiceException, CommunityIdentifierException
        {
        return createIvorn(
            CommunityIvornParser.getLocalIdent(),
            account,
            resource
            ) ;
        }

    /**
     * Create a Community Account Ivorn.
     * @param community The Community ident, with no extra fields.
     * @param account   The Account name, without the Community ident.
     * @throws CommunityIdentifierException If the identifiers are not valid.
     *
     */
    public static Ivorn createIvorn(String community, String account)
        throws CommunityIdentifierException
        {
        try {
            return new Ivorn(
                createIdent(
                    community,
                    account
                    )
                ) ;
            }
        catch (URISyntaxException ouch)
            {
            throw new CommunityIdentifierException(
                ouch
                ) ;
            }
        }

    /**
     * Create a Community Account Ivorn.
     * @param community The Community ident, with no extra fields.
     * @param account   The Account name, without the Community ident.
     * @param resource  The resource, added after the account.
     * @throws CommunityIdentifierException If the identifiers are not valid.
     *
     */
    public static Ivorn createIvorn(String community, String account, String resource)
        throws CommunityIdentifierException
        {
        try {
            return new Ivorn(
                createIdent(
                    community,
                    account,
                    resource
                    )
                ) ;
            }
        catch (URISyntaxException ouch)
            {
            throw new CommunityIdentifierException(
                ouch
                ) ;
            }
        }

    /**
     * Create a mock Community Account Ivorn.
     * @param community The Community ident, with no extra fields.
     * @param account   The Account name, without the Community ident.
     * @throws CommunityIdentifierException If the identifiers are not valid.
     *
     */
    public static Ivorn createMock(String community, String account)
        throws CommunityIdentifierException
        {
        try {
            return new MockIvorn(
                community,
                account
                ) ;
            }
        catch (URISyntaxException ouch)
            {
            throw new CommunityIdentifierException(
                ouch
                ) ;
            }
        }
    }
