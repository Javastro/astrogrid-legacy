/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/mySpace/client/src/java/org/astrogrid/store/adapter/aladin/AladinAdapterLoginException.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2004/11/17 16:22:53 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: AladinAdapterLoginException.java,v $
 *   Revision 1.3  2004/11/17 16:22:53  clq2
 *   nww-itn07-704
 *
 *   Revision 1.2.20.1  2004/11/16 16:47:28  nw
 *   copied aladinAdapter interfaces into a neutrally-named package.
 *   deprecated original interfaces.
 *   javadoc
 *
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
 * An exception thrown if the adapter fails to login.
 * * @deprecated use {@link org.astrogrid.store.tree} instead
 */
public class AladinAdapterLoginException
	extends AladinAdapterException
	{
	/**
	 * Public constructor, with cause and message.
	 * @param message The Exception message.
	 * @param cause   The Exception cause.
	 *
	 */
	public AladinAdapterLoginException(String message, Throwable cause)
		{
		super(message, cause) ;
		}

	}
