/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/exception/CommunityServiceException.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityServiceException.java,v $
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

import org.astrogrid.store.Ivorn ;

/**
 * An exception thrown when a service fails.
 *
 */
public class CommunityServiceException
    extends CommunityException
    {

    /**
     * Public constructor.
     * This should not be used in the main code.
     * This enables Axis to re-construct the Exception on the client side by treating it as a Bean.
     *
     */
    public CommunityServiceException()
        {
        super() ;
        }

    /**
     * Public constructor.
     * @param message The Exception message.
     *
     */
    public CommunityServiceException(String message)
        {
        super(message) ;
        }

    /**
     * Public constructor.
     * @param cause The root cause of this Exception.
     *
     */
    public CommunityServiceException(Throwable cause)
        {
        super(cause) ;
        }

    /**
     * Public constructor.
     * @param message The Exception message.
     * @param cause   The root cause of this Exception.
     *
     */
    public CommunityServiceException(String message, Throwable cause)
        {
        super(message, cause) ;
        }

    /**
     * Public constructor.
     * @param message The Exception message.
     * @param ident   The identifier that caused the Exception.
     *
     */
    public CommunityServiceException(String message, String ident)
        {
        super(message, ident) ;
        }

    /**
     * Public constructor.
     * @param message The Exception message.
     * @param ident   The identifier that caused the Exception.
     * @param cause   The root cause of this Exception.
     *
     */
    public CommunityServiceException(String message, String ident, Throwable cause)
        {
        super(message, ident) ;
        }

    /**
     * Public constructor.
     * @param message The Exception message.
     * @param ivorn   The identifier that caused the Exception.
     *
     */
    public CommunityServiceException(String message, Ivorn ivorn)
        {
        super(message, ivorn) ;
        }

    /**
     * Public constructor.
     * @param message The Exception message.
     * @param ivorn   The identifier that caused the Exception.
     * @param cause   The root cause of this Exception.
     *
     */
    public CommunityServiceException(String message, Ivorn ivorn, Throwable cause)
        {
        super(message, ivorn) ;
        }

    }
