/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/exception/CommunityIdentifierException.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityIdentifierException.java,v $
 *   Revision 1.4  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.3.36.1  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.exception ;

import java.net.URISyntaxException ;
import org.astrogrid.store.Ivorn ;

/**
 * An exception thrown when an invalid identifier is encountered.
 *
 */
public class CommunityIdentifierException
    extends CommunityException
    {

    /**
     * Public constructor.
     * This should not be used in the main code.
     * This enables Axis to re-construct the Exception on the client side by treating it as a Bean.
     *
     */
    public CommunityIdentifierException()
        {
        super() ;
        }

    /**
     * Public constructor.
     * @param message The Exception message.
     *
     */
    public CommunityIdentifierException(String message)
        {
        super(message) ;
        }

    /**
     * Public constructor.
     * @param cause The root cause of this Exception.
     *
     */
    public CommunityIdentifierException(URISyntaxException cause)
        {
        super(cause) ;
        }

    /**
     * Public constructor.
     * @param message The Exception message.
     * @param cause   The root cause of this Exception.
     *
     */
    public CommunityIdentifierException(String message, URISyntaxException cause)
        {
        super(message, cause) ;
        }

    /**
     * Public constructor.
     * @param message The Exception message.
     * @param ident   The identifier that caused the Exception.
     *
     */
    public CommunityIdentifierException(String message, String ident)
        {
        super(message, ident) ;
        }

    /**
     * Public constructor.
     * @param message The Exception message.
     * @param ivorn   The identifier that caused the Exception.
     *
     */
    public CommunityIdentifierException(String message, Ivorn ivorn)
        {
        super(message, ivorn) ;
        }

    }
