/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/mySpace/client/src/java/org/astrogrid/store/tree/TreeClientException.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2004/11/17 16:22:53 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: TreeClientException.java,v $
 *   Revision 1.2  2004/11/17 16:22:53  clq2
 *   nww-itn07-704
 *
 *   Revision 1.1.2.1  2004/11/16 16:47:28  nw
 *   copied aladinAdapter interfaces into a neutrally-named package.
 *   deprecated original interfaces.
 *   javadoc
 *
 *   Revision 1.2  2004/09/28 10:24:19  dave
 *   Added AladinAdapter interfaces and mock implementation.
 *
 *   Revision 1.1.2.2  2004/09/27 22:46:53  dave
 *   Added AdapterFile interface, with input and output stream API.
 *
 *   Revision 1.1.2.1  2004/09/23 16:32:01  dave
 *   Added better Exception handling ....
 *   Added initial mock container ....
 *   Added initial root container tests ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.store.tree;

/**
 * A base class for all our exceptions.
 * This allows the client to catch all our exceptions using one catch statement.
 *
 */
public class TreeClientException
	extends Exception
	{
	/**
	 * Public constructor, with message.
	 * @param message The Exception message.
	 *
	 */
	public TreeClientException(String message)
		{
		super(message) ;
		}

	/**
	 * Public constructor, with cause and message.
	 * @param message The Exception message.
	 * @param cause   The Exception cause.
	 *
	 */
	public TreeClientException(String message, Throwable cause)
		{
		super(message, cause) ;
		}

	}
