/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/ivorn/CommunityIvornFactory.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityIvornFactory.java,v $
 *   Revision 1.5  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.4.2.2  2004/03/22 00:53:31  dave
 *   Refactored GroupManager to use Ivorn identifiers.
 *   Started removing references to CommunityManager.
 *
 *   Revision 1.4.2.1  2004/03/20 06:54:11  dave
 *   Added addAccount(AccountData) to PolicyManager et al.
 *   Added XML loader for AccountData.
 *
 *   Revision 1.4  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.3.2.3  2004/03/18 13:41:19  dave
 *   Added Exception handling to AccountManager
 *
 *   Revision 1.3.2.2  2004/03/16 14:05:21  dave
 *   Added CommunityIvornFactoryTestCase
 *   Added CommunityAccountIvornFactoryTestCase
 *
 *   Revision 1.3.2.1  2004/03/16 11:55:15  dave
 *   Split CommunityIvornFactory into CommunityAccountIvornFactory and CommunityServiceIvornFactory.
 *
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

import org.astrogrid.store.Ivorn ;
import org.astrogrid.common.ivorn.MockIvorn ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

/**
 * A factory for generating Community related Ivorn identifiers.
 * @todo Need better integration with the MockIvorn factory.
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
     * Create a new ident.
     * @param  community The Community ident, with no extra fields.
     * @param  resource  The resource name.
     * @return A new identifer.
     * @throws CommunityIdentifierException if the community or resource identifiers are null.
     *
     */
    protected static String createIdent(String community, String resource)
        throws CommunityIdentifierException
        {
        return createIdent(community, resource, null, null, null) ;
        }

    /**
     * Create a Community ident.
     * @param  community The Community ident, with no extra fields.
     * @param  resource  The resource name.
     * @param  path      The URI path, added after the resource.
     * @return A new identifer.
     * @throws CommunityIdentifierException if the community or resource identifiers are null.
     *
     */
    protected static String createIdent(String community, String resource, String path)
        throws CommunityIdentifierException
        {
        return createIdent(community, resource, path, null, null) ;
        }

    /**
     * Create a Community ident.
     * @param  community The Community ident, with no extra fields.
     * @param  resource  The resource name.
     * @param  path      The URI path, added after the resource.
     * @param  fragment  The URI fragment, added after the path.
     * @return A new identifer.
     * @throws CommunityIdentifierException if the community or resource identifiers are null.
     *
     */
    protected static String createIdent(String community, String resource, String path, String fragment)
        throws CommunityIdentifierException
        {
        return createIdent(community, resource, path, null, fragment) ;
        }

    /**
     * Create a Community ident.
     * @param  community The Community ident, with no extra fields.
     * @param  resource  The resource name.
     * @param  path      The URI path, added after the resource.
     * @param  query     The URI query string, added after the path.
     * @param  fragment  The URI fragment, added after the query.
     * @return A new identifer.
     * @throws CommunityIdentifierException if the community or resource identifiers are null.
     *
     */
    protected static String createIdent(String community, String resource, String path, String query, String fragment)
        throws CommunityIdentifierException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityIvornFactory.createIdent()") ;
        if (DEBUG_FLAG) System.out.println("  Community : " + community) ;
        if (DEBUG_FLAG) System.out.println("  Resource  : " + resource) ;
        if (DEBUG_FLAG) System.out.println("  Path      : " + path) ;
        if (DEBUG_FLAG) System.out.println("  Query     : " + query) ;
        if (DEBUG_FLAG) System.out.println("  Fragment  : " + fragment) ;
        //
        // Check for null params.
        if (null == community) { throw new CommunityIdentifierException("Null Community identifier") ; }
        if (null == resource)  { throw new CommunityIdentifierException("Null Resource identifier") ; }
        //
        // Put it all together.
        StringBuffer buffer = new StringBuffer() ;
		//
		// If the community identifier isn't an Ivorn yet.
		if (false == community.startsWith(Ivorn.SCHEME))
			{
	        buffer.append(Ivorn.SCHEME) ;
	        buffer.append("://") ;
			}
        buffer.append(community) ;
        buffer.append("/") ;
        buffer.append(resource) ;
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
        if (DEBUG_FLAG) System.out.println("  Result    : " + buffer.toString()) ;
        //
        // Return the new string.
        return buffer.toString() ;
        }
    }
