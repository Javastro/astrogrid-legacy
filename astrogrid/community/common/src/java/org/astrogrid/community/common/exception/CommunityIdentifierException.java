/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/exception/CommunityIdentifierException.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityIdentifierException.java,v $
 *   Revision 1.3  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.2.2.1  2004/03/21 06:41:41  dave
 *   Refactored to include Exception handling.
 *
 *   Revision 1.2  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.1.2.2  2004/03/19 00:18:09  dave
 *   Refactored delegate Exception handling
 *
 *   Revision 1.1.2.1  2004/03/17 13:50:23  dave
 *   Refactored Community exceptions
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
