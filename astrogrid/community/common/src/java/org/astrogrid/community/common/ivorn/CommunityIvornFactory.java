/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/ivorn/CommunityIvornFactory.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.8 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityIvornFactory.java,v $
 *   Revision 1.8  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.7.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.7  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.6.32.2  2004/06/17 14:50:01  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.6.32.1  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.ivorn ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

/**
 * A factory for generating Community related Ivorn identifiers.
 * @todo Need better integration with the MockIvorn factory.
 *
 */
public class CommunityIvornFactory
    {
    /**
     * Our debug logger.
     *
     */
	private static Log log = LogFactory.getLog(CommunityIvornFactory.class);

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
        return createIdent(
        	community,
        	resource,
        	null,
        	null,
        	null
        	) ;
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
        return createIdent(
        	community,
        	resource,
        	path,
        	null,
        	null
        	) ;
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
        return createIdent(
        	community,
        	resource,
        	path,
        	null,
        	fragment
        	) ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityIvornFactory.createIdent()") ;
        log.debug("  Community : " + community) ;
        log.debug("  Resource  : " + resource) ;
        log.debug("  Path      : " + path) ;
        log.debug("  Query     : " + query) ;
        log.debug("  Fragment  : " + fragment) ;
        //
        // Check for null params.
        if (null == community)
        	{
        	throw new CommunityIdentifierException(
        		"Null Community identifier"
        		) ;
        	}
        if (null == resource)
        	{
        	throw new CommunityIdentifierException(
        		"Null Resource identifier"
        		) ;
        	}
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
        log.debug("  Result    : " + buffer.toString()) ;
        //
        // Return the new string.
        return buffer.toString() ;
        }
    }
