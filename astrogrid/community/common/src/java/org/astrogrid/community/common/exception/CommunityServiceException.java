/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/exception/CommunityServiceException.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityServiceException.java,v $
 *   Revision 1.2  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.1.2.5  2004/03/19 00:18:09  dave
 *   Refactored delegate Exception handling
 *
 *   Revision 1.1.2.4  2004/03/18 13:41:19  dave
 *   Added Exception handling to AccountManager
 *
 *   Revision 1.1.2.3  2004/03/17 13:50:23  dave
 *   Refactored Community exceptions
 *
 *   Revision 1.1.2.2  2004/03/17 01:08:48  dave
 *   Added AccountNotFoundException
 *   Added DuplicateAccountException
 *   Added InvalidIdentifierException
 *
 *   Revision 1.1.2.1  2004/03/16 19:52:31  dave
 *   Added Exception reporting to resolvers
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
