/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/store/tree/TreeClientDuplicateException.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 13:37:06 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: TreeClientDuplicateException.java,v $
 *   Revision 1.2  2005/03/11 13:37:06  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.1.2.1  2005/02/27 23:03:12  nw
 *   first cut of talking to filestore
 *
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
package org.astrogrid.store.tree;

/**
 * An exception thrown if attempting to create a duplicate entry.
 *
 *@deprecated - just here for backwards compatability. use client accessed from  {@link org.astrogrid.filemanager.client.FileManagerClientFactory} instead.
 */
public class TreeClientDuplicateException
	extends TreeClientException
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
	public TreeClientDuplicateException()
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
	public TreeClientDuplicateException(String message)
		{
		super(
			message
			) ;
		}

	}
