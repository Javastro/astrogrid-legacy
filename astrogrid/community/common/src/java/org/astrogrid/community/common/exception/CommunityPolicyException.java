/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/exception/CommunityPolicyException.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityPolicyException.java,v $
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

