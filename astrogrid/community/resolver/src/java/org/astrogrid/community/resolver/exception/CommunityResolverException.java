/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/exception/CommunityResolverException.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/30 01:40:03 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityResolverException.java,v $
 *   Revision 1.4  2004/03/30 01:40:03  dave
 *   Merged development branch, dave-dev-200403242058, into HEAD
 *
 *   Revision 1.3.2.1  2004/03/28 09:11:43  dave
 *   Convert tabs to spaces
 *
 *   Revision 1.3  2004/03/24 16:56:25  dave
 *   Merged development branch, dave-dev-200403231641, into HEAD
 *
 *   Revision 1.2.4.1  2004/03/24 15:19:20  dave
 *   Added check for Throwable on registry call.
 *   Added more JUnit tests.
 *
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
     * @param message The Exception message.
     * @param cause   The root cause of this Exception.
     *
     */
    public CommunityResolverException(String message, Throwable cause)
        {
        super(message, cause) ;
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
