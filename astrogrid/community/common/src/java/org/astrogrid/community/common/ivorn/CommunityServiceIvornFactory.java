/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/ivorn/CommunityServiceIvornFactory.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityServiceIvornFactory.java,v $
 *   Revision 1.4  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.3.36.2  2004/06/17 14:50:01  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.3.36.1  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
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
