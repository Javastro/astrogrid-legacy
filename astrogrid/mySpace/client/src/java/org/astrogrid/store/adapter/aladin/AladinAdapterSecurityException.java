/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/mySpace/client/src/java/org/astrogrid/store/adapter/aladin/AladinAdapterSecurityException.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/28 10:24:19 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: AladinAdapterSecurityException.java,v $
 *   Revision 1.2  2004/09/28 10:24:19  dave
 *   Added AladinAdapter interfaces and mock implementation.
 *
 *   Revision 1.1.2.1  2004/09/23 16:32:02  dave
 *   Added better Exception handling ....
 *   Added initial mock container ....
 *   Added initial root container tests ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.store.adapter.aladin ;

/**
 * An exception thrown if the adapter attempts to access something it is not allowed to.
 *
 */
public class AladinAdapterSecurityException
	extends AladinAdapterException
	{
	/**
	 * Public constructor, with cause and message.
	 * @param message The Exception message.
	 *
	 */
	public AladinAdapterSecurityException(String message)
		{
		super(message, null) ;
		}

	/**
	 * Public constructor, with cause and message.
	 * @param message The Exception message.
	 * @param cause   The Exception cause.
	 *
	 */
	public AladinAdapterSecurityException(String message, Throwable cause)
		{
		super(message, cause) ;
		}

	}
