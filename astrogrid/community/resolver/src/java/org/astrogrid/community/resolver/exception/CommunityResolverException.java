/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/exception/CommunityResolverException.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityResolverException.java,v $
 *   Revision 1.5  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.4.32.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
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
