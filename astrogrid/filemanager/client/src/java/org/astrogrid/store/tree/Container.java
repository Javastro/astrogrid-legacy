/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/store/tree/Container.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 13:37:06 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: Container.java,v $
 *   Revision 1.2  2005/03/11 13:37:06  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.1.2.1  2005/02/27 23:03:12  nw
 *   first cut of talking to filestore
 *
 *   Revision 1.2  2004/11/17 16:22:53  clq2
 *   nww-itn07-704
 *
 *   Revision 1.1.2.2  2004/11/16 17:27:58  nw
 *   tidied imports
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
 *   Revision 1.1.2.1  2004/09/24 01:36:18  dave
 *   Refactored File as Node and Container ...
 *
 *   Revision 1.1.2.2  2004/09/24 01:12:09  dave
 *   Added initial test for child nodes.
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

import java.util.Collection;

/**
 * Representation of a container in an Astrogrid Store.
 *
 *@deprecated - just here for backwards compatability. use client accessed from  {@link org.astrogrid.filemanager.client.FileManagerClientFactory} instead.
 */
public interface Container
	extends Node
	{
	/**
	 * Get a list (collection) of the current child nodes.
	 * Note, you cannot add a child node by adding a node to the collection.
	 * @return An unmodifiable collection of Node(s) for the child nodes.
	 *
	 */
	public Collection getChildNodes() ;

	/**
	 * Add a child to a container.
	 * @param name The container name.
     * @throws TreeClientDuplicateException If the container already exists.
     * @throws TreeClientServiceException   If the service is unable to handle the request.
	 *
	 */
	public Container addContainer(String name)
		throws TreeClientServiceException, TreeClientDuplicateException ;

	/**
	 * Add a file to this container.
	 * @param name The file name.
     * @throws TreeClientDuplicateException If the file already exists.
	 * @throws TreeClientServiceException   If the service is unable to handle the request.
	 *
	 */
	public File addFile(String name)
		throws TreeClientServiceException, TreeClientDuplicateException ;

	}
