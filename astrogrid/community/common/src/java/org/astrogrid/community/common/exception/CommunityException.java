/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/exception/CommunityException.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityException.java,v $
 *   Revision 1.5  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.4.2.1  2004/03/21 06:41:41  dave
 *   Refactored to include Exception handling.
 *
 *   Revision 1.4  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.3.2.6  2004/03/19 04:51:46  dave
 *   Updated resolver with new Exceptions
 *
 *   Revision 1.3.2.5  2004/03/19 00:18:09  dave
 *   Refactored delegate Exception handling
 *
 *   Revision 1.3.2.4  2004/03/18 13:41:19  dave
 *   Added Exception handling to AccountManager
 *
 *   Revision 1.3.2.3  2004/03/17 13:50:23  dave
 *   Refactored Community exceptions
 *
 *   Revision 1.3.2.2  2004/03/16 19:51:17  dave
 *   Added Exception reporting to resolvers
 *
 *   Revision 1.3.2.1  2004/03/16 15:49:55  dave
 *   Added CommunityIvornException
 *
 *   Revision 1.3  2004/03/15 07:49:30  dave
 *   Merged development branch, dave-dev-200403121536, into HEAD
 *
 *   Revision 1.2.2.1  2004/03/12 17:42:09  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.2  2004/03/12 15:22:17  dave
 *   Merged development branch, dave-dev-200403101018, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/10 15:29:28  dave
 *   Added CommunityException base class.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.exception ;

import org.astrogrid.store.Ivorn ;

/**
 * Base class for our Exceptions.
 * This means that client applications can catch a generic CommunityException if they need to.
 * <br>
 * According to the Axis documentation, this must either extend RemoteException, or have get set methods like a Bean.
 * This implementation goes for the Bean approach for now, but this may change.
 *
 */
public class CommunityException
    extends Exception
    {
    /**
     * Protected constructor.
     * This enables Axis to re-construct the Exception on the client side by treating it as a Bean.
     * <br>
     * This isn't really deprecated, I just don't want you to use it.
     * Marking it as deprecated means that any compiled code that uses this will get a warning.
     * The Axis Bean-Serialiser invokes this at run time, and won't get a warning.
     * @deprecated
     *
     */
    protected CommunityException()
        {
        super() ;
        }

    /**
     * Protected constructor.
     *
     */
    protected CommunityException(String message)
        {
        super(message) ;
        }

    /**
     * Protected constructor.
     * @param cause The root cause of this Exception.
     *
     */
    protected CommunityException(Throwable cause)
        {
        super(cause) ;
        }

    /**
     * Protected constructor.
     * @param message The Exception message.
     * @param cause   The root cause of this Exception.
     *
     */
    protected CommunityException(String message, Throwable cause)
        {
        super(message, cause) ;
        }

    /**
     * Public constructor.
     * @param message The Exception message.
     * @param ident   The identifier that caused the Exception.
     *
     */
    protected CommunityException(String message, String ident)
        {
        super(message) ;
        this.ident = ident ;
        }

    /**
     * Public constructor.
     * @param message The Exception message.
     * @param ident   The identifier that caused the Exception.
     * @param cause   The root cause of this Exception.
     *
     */
    protected CommunityException(String message, String ident, Throwable cause)
        {
        super(message, cause) ;
        this.ident = ident ;
        }

    /**
     * The Exception message.
     * This enables Axis to re-construct the Exception on the client side by treating it as a Bean.
     *
     */
    private String message ;

    /**
     * Access to the message.
     * This enables Axis to re-construct the Exception on the client side by treating it as a Bean.
     *
     */
    public String getMessage()
        {
        if (null != this.message)
            {
            return this.message ;
            }
        else {
            return super.getMessage() ;
            }
        }

    /**
     * Access to the message.
     * This enables Axis to re-construct the Exception on the client side by treating it as a Bean.
     * @param message The Exception message.
     * <br>
     * This isn't really deprecated, I just don't want you to use it.
     * Marking it as deprecated means that any compiled code that uses this will get a warning.
     * The Axis Bean-Serialiser invokes this at run time, and won't get a warning.
     * @deprecated
     *
     */
    public void setMessage(String message)
        {
        this.message = message ;
        }

    /**
     * The identifier that caused the Exception.
     *
     */
    private String ident ;

    /**
     * Access to the identifier.
     *
     */
    public String getIdent()
        {
        return this.ident ;
        }

    /**
     * Access to the identifier.
     * This is required to allow Axis to re-construct the Exception on the client side by treating it as a Bean.
     * @param ident The identifier that caused the Exception.
     * <br>
     * This isn't really deprecated, I just don't want you to use it.
     * Marking it as deprecated means that any compiled code that uses this will get a warning.
     * The Axis Bean-Serialiser invokes this at run time, and won't get a warning.
     * @deprecated
     *
     */
    public void setIdent(String ident)
        {
        this.ident = ident ;
        }

    /**
     * Protected constructor.
     * @param message The Exception message.
     * @param ivorn   The identifier that caused the Exception.
     *
     */
    protected CommunityException(String message, Ivorn ivorn)
        {
        super(message) ;
        if (null != ivorn)
            {
            this.ident = ivorn.toString() ;
            }
        }

    /**
     * Protected constructor.
     * @param message The Exception message.
     * @param ivorn   The identifier that caused the Exception.
     * @param cause   The root cause of this Exception.
     *
     */
    protected CommunityException(String message, Ivorn ivorn, Throwable cause)
        {
        super(message, cause) ;
        if (null != ivorn)
            {
            this.ident = ivorn.toString() ;
            }
        }
    }
