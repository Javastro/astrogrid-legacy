/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/ivorn/CommunityServiceIvornFactory.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityServiceIvornFactory.java,v $
 *   Revision 1.3  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.2.2.1  2004/03/21 06:41:41  dave
 *   Refactored to include Exception handling.
 *
 *   Revision 1.2  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.1.2.4  2004/03/18 13:41:19  dave
 *   Added Exception handling to AccountManager
 *
 *   Revision 1.1.2.3  2004/03/16 19:51:17  dave
 *   Added Exception reporting to resolvers
 *
 *   Revision 1.1.2.2  2004/03/16 14:29:50  dave
 *   Added CommunityServiceIvornFactoryTestCase.
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
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

/**
 * A factory for generating Community Service identifiers.
 *
 */
public class CommunityServiceIvornFactory
    extends CommunityIvornFactory
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Create a community service Ivorn.
     * @param community The Community ident, with no extra fields.
     * @param service   The Java class of the service interface we want.
     * @return A new Ivorn, or null if the Community ident is null.
     * @throws CommunityIdentifierException If the identifiers are not valid.
     *
     */
    public static Ivorn createIvorn(String community, Class service)
        throws CommunityIdentifierException
        {
        if (null == community) { throw new CommunityIdentifierException("Null Community identifier") ; }
        if (null == service)   { throw new CommunityIdentifierException("Null service type") ; }
        try {
            return new Ivorn(
                createIdent(
                    community,
                    service.getName()
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
     * Create a mock service Ivorn.
     * @param community The Community ident, with no extra fields.
     * @param service   The Java class of the service interface we want.
     * @return A new Ivorn, or null if the Community ident is null.
     * @throws CommunityIdentifierException If the identifiers are not valid.
     *
     */
    public static Ivorn createMock(String community, Class service)
        throws CommunityIdentifierException
        {
        if (null == community) { throw new CommunityIdentifierException("Null Community identifier") ; }
        if (null == service)   { throw new CommunityIdentifierException("Null service type") ; }
        try {
            return new MockIvorn(
                createIdent(
                    community,
                    service.getName()
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
    }
