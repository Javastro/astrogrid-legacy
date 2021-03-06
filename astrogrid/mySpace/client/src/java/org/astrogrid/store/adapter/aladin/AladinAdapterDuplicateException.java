/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/mySpace/client/src/java/org/astrogrid/store/adapter/aladin/AladinAdapterDuplicateException.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2004/11/17 16:22:53 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: AladinAdapterDuplicateException.java,v $
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
 *   Revision 1.1.2.1  2004/09/27 22:46:53  dave
 *   Added AdapterFile interface, with input and output stream API.
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
 * An exception thrown if attempting to create a duplicate entry.
 * * @deprecated use {@link org.astrogrid.store.tree} instead
 */
public class AladinAdapterDuplicateException
	extends AladinAdapterException
	{
	/**
	 * The default Exception message.
	 *
	 */
	public static final String DEFAULT_MESSAGE = "Attempted to create duplicate node in tree" ;

	/**
	 * Public constructor, with default message.
	 *
	 */
	public AladinAdapterDuplicateException()
		{
		this(
			DEFAULT_MESSAGE
			) ;
		}

	/**
	 * Public constructor, with specific message.
	 * @param message The Exception message.
	 *
	 */
	public AladinAdapterDuplicateException(String message)
		{
		super(
			message
			) ;
		}

	}
