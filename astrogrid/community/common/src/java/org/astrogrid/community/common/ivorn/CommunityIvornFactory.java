/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/ivorn/CommunityIvornFactory.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/15 07:49:30 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityIvornFactory.java,v $
 *   Revision 1.3  2004/03/15 07:49:30  dave
 *   Merged development branch, dave-dev-200403121536, into HEAD
 *
 *   Revision 1.2.2.4  2004/03/15 07:27:15  dave
 *   Fixed refactored constructor for MockIvorn
 *
 *   Revision 1.2.2.3  2004/03/13 17:57:20  dave
 *   Remove RemoteException(s) from delegate interfaces.
 *   Protected internal API methods.
 *
 *   Revision 1.2.2.2  2004/03/13 16:08:08  dave
 *   Added CommunityAccountResolver and CommunityEndpointResolver.
 *
 *   Revision 1.2.2.1  2004/03/12 17:42:09  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.2  2004/03/12 15:22:17  dave
 *   Merged development branch, dave-dev-200403101018, into HEAD
 *
 *   Revision 1.1.2.2  2004/03/12 13:44:43  dave
 *   Moved MockIvornFactory to MockIvorn
 *
 *   Revision 1.1.2.1  2004/03/12 00:46:25  dave
 *   Added CommunityIvornFactory and CommunityIvornParser.
 *   Added MockIvorn (to be moved to common project).
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.ivorn ;

import java.net.URISyntaxException ;
import org.astrogrid.store.Ivorn ;

import org.astrogrid.common.ivorn.MockIvorn ;

/**
 * A factory for generating Community Ivorn identifiers.
 * @TODO - Combine this woth the MockIvorn ....
 *
 */
public class CommunityIvornFactory
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Create a community Ivorn.
     * @param ident   The Community ident, with no extra fields.
     * @param account The Account name, without the Community ident.
     *
     */
    public static Ivorn createIvorn(String ident, String account)
        throws URISyntaxException
        {
        return new Ivorn(
            createIdent(ident, account)
            ) ;
        }

    /**
     * Create a mock community Ivorn.
     * @param ident   The Community ident, with no extra fields.
     * @param account The Account name, without the Community ident.
     *
     */
    public static Ivorn createMock(String ident, String account)
        throws URISyntaxException
        {
        return new MockIvorn(
            ident,
            account
            ) ;
        }

    /**
     * Create a Community ident.
     * @param ident   The Community ident, with no extra fields.
     * @param account The Account name, without the Community ident.
     *
     */
    protected static String createIdent(String ident, String account)
        {
        return createIdent(ident, account, null, null, null) ;
        }

    /**
     * Create a Community ident.
     * @param ident   The Community ident, with no extra fields.
     * @param account The Account name, without the Community ident.
     * @param path    The path, added after the ident.
     *
     */
    protected static String createIdent(String ident, String account, String path)
        {
        return createIdent(ident, account, path, null, null) ;
        }

    /**
     * Create a Community ident.
     * @param ident     The Community ident, with no extra fields.
     * @param account   The Account name, without the Community ident.
     * @param path      The path, added after the ident.
     * @param fragment  The URI fragment string.
     *
     */
    protected static String createIdent(String ident, String account, String path, String fragment)
        {
        return createIdent(ident, account, path, null, fragment) ;
        }

    /**
     * Create a Community ident.
     * @param ident     The Community ident, with no extra fields.
     * @param account   The Account name, without the Community ident.
     * @param path      The path, added after the ident.
     * @param query     The URI query string.
     * @param fragment  The URI fragment string.
     *
     */
    protected static String createIdent(String ident, String account, String path, String query, String fragment)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityIvornFactory.createIdent()") ;
        if (DEBUG_FLAG) System.out.println("  Ident    : " + ident) ;
        if (DEBUG_FLAG) System.out.println("  Account  : " + account) ;
        if (DEBUG_FLAG) System.out.println("  Path     : " + path) ;
        if (DEBUG_FLAG) System.out.println("  Query    : " + query) ;
        if (DEBUG_FLAG) System.out.println("  Fragment : " + fragment) ;
        //
        // Check for null params.
        if (null == ident)   { throw new IllegalArgumentException("Null Community identifier") ; }
        if (null == account) { throw new IllegalArgumentException("Null Account identifier") ; }
        //
        // Put it all together.
        StringBuffer buffer = new StringBuffer() ;

        buffer.append(Ivorn.SCHEME) ;
        buffer.append("://") ;

        buffer.append(ident) ;
        buffer.append("/") ;
        buffer.append(account) ;
        if (null != path)
            {
            buffer.append("/") ;
            buffer.append(path) ;
            }
        if (null != query)
            {
            buffer.append("?") ;
            buffer.append(query) ;
            }
        if (null != fragment)
            {
            buffer.append("#") ;
            buffer.append(fragment) ;
            }
        if (DEBUG_FLAG) System.out.println("  Result   : " + buffer.toString()) ;
        //
        // Return the new string.
        return buffer.toString() ;
        }
    }
