/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/exception/CommunityPolicyException.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityPolicyException.java,v $
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

import org.astrogrid.store.Ivorn ;

/**
 * A base class for policy Exceptions.
 *
 */
public class CommunityPolicyException
    extends CommunityException
    {

    /**
     * Public constructor.
     * This should not be used in the main code.
     * This enables Axis to re-construct the Exception on the client side by treating it as a Bean.
     *
     */
    public CommunityPolicyException()
        {
        super() ;
        }

    /**
     * Public constructor.
     * @param message The Exception message.
     * @param account The Account identifier that caused the Exception.
     *
     */
    public CommunityPolicyException(String message, String account)
        {
        super(
            message,
            account
            ) ;
        }

    /**
     * Public constructor.
     * @param message The Exception message.
     * @param account The Account identifier that caused the Exception.
     *
     */
    public CommunityPolicyException(String message, Ivorn account)
        {
        super(
            message,
            account
            ) ;
        }

    }

