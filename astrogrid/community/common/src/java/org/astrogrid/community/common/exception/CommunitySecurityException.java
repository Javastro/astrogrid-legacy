/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/exception/CommunitySecurityException.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunitySecurityException.java,v $
 *   Revision 1.2  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/22 15:31:10  dave
 *   Added CommunitySecurityException.
 *   Updated SecurityManager and SecurityService to use Exceptions.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.exception ;

import org.astrogrid.store.Ivorn ;

/**
 * A base class for security Exceptions.
 *
 */
public class CommunitySecurityException
    extends CommunityException
    {

    /**
     * Public constructor.
     * This should not be used in the main code.
     * This enables Axis to re-construct the Exception on the client side by treating it as a Bean.
     *
     */
    public CommunitySecurityException()
        {
        super() ;
        }

    /**
     * Public constructor.
     * @param message The Exception message.
     *
     */
    public CommunitySecurityException(String message)
        {
        super(
            message
            ) ;
        }

    /**
     * Public constructor.
     * @param message The Exception message.
     * @param ident The identifier that caused the Exception.
     *
     */
    public CommunitySecurityException(String message, String ident)
        {
        super(
            message,
            ident
            ) ;
        }

    /**
     * Public constructor.
     * @param message The Exception message.
     * @param account The identifier that caused the Exception.
     *
     */
    public CommunitySecurityException(String message, Ivorn ident)
        {
        super(
            message,
            ident
            ) ;
        }
    }

