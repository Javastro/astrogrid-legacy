/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/ivorn/CommunityAccountIvornFactory.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityAccountIvornFactory.java,v $
 *   Revision 1.3  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.2.2.2  2004/03/22 00:53:31  dave
 *   Refactored GroupManager to use Ivorn identifiers.
 *   Started removing references to CommunityManager.
 *
 *   Revision 1.2.2.1  2004/03/21 06:41:41  dave
 *   Refactored to include Exception handling.
 *
 *   Revision 1.2  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.1.2.5  2004/03/18 13:41:19  dave
 *   Added Exception handling to AccountManager
 *
 *   Revision 1.1.2.4  2004/03/16 19:51:17  dave
 *   Added Exception reporting to resolvers
 *
 *   Revision 1.1.2.3  2004/03/16 15:49:55  dave
 *   Added CommunityIvornException
 *
 *   Revision 1.1.2.2  2004/03/16 14:05:21  dave
 *   Added CommunityIvornFactoryTestCase
 *   Added CommunityAccountIvornFactoryTestCase
 *
 *   Revision 1.1.2.1  2004/03/16 11:55:15  dave
 *   Split CommunityIvornFactory into CommunityAccountIvornFactory and CommunityServiceIvornFactory.
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
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

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
