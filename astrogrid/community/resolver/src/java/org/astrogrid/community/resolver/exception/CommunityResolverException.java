/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/exception/CommunityResolverException.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:15 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityResolverException.java,v $
 *   Revision 1.2  2004/03/19 14:43:15  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.1.2.2  2004/03/19 04:51:46  dave
 *   Updated resolver with new Exceptions
 *
 *   Revision 1.1.2.1  2004/03/16 19:53:21  dave
 *   Added Exception reporting to resolvers
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver.exception ;

import java.net.URL ;
import org.astrogrid.store.Ivorn ;

import org.astrogrid.community.common.exception.CommunityException ;

/**
 * An exception thrown when a resolver can't resolve an Ivorn, URL or URI.
 *
 */
public class CommunityResolverException
    extends CommunityException
    {

	/**
	 * Public constructor.
	 * @param message The Exception message.
	 *
	 */
	public CommunityResolverException(String message)
		{
		super(message) ;
		}

	/**
	 * Public constructor.
	 * @param message  The Exception message.
	 * @param ivorn    The Service ivorn identifier.
	 *
	 */
	public CommunityResolverException(String message, Ivorn ivorn)
		{
		super(message, ivorn) ;
		}

	/**
	 * Public constructor.
	 * @param message  The Exception message.
	 * @param endpoint The Service endpoint url.
	 *
	 */
	public CommunityResolverException(String message, URL url)
		{
		super(message, url.toString()) ;
		}
    }
