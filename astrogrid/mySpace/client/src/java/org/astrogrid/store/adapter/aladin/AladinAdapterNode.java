/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/mySpace/client/src/java/org/astrogrid/store/adapter/aladin/AladinAdapterNode.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/28 10:24:19 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: AladinAdapterNode.java,v $
 *   Revision 1.2  2004/09/28 10:24:19  dave
 *   Added AladinAdapter interfaces and mock implementation.
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
package org.astrogrid.store.adapter.aladin ;

/**
 * A wrapper for the AstroGrid StoreFile to make it easier to integrate into Aladin.
 *
 */
public interface AladinAdapterNode
	{
	/**
	 * Access to the node name.
	 * @return The node name.
	 *
	 */
	public String getName() ;

	/**
	 * Check if this represents a file.
	 * @return true if this represents a file.
	 *
	 */
	public boolean isFile() ;

	/**
	 * Check if this represents a container.
	 * @return true if this represents a container.
	 *
	 */
	public boolean isContainer() ;

	/**
	 * Get a list (collection) of the current child nodes.
	 * Note, you cannot add a child node by adding a node to the collection.
	 * @return An unmodifiable collection of AladinAdapterFile(s) for the child nodes.
	 *
	public Collection getChildNodes() ;
	 */

	/**
	 * Add a child to a container.
	 * @param name The container name.
     * @throws AladinAdapterSecurityException If the adapter is not logged in.
     * @throws AladinAdapterServiceException  If the service is unable to handle the request.
	 *
	public AladinAdapterFile addContainer(String name)
		throws AladinAdapterSecurityException, AladinAdapterSecurityException ;
	 */

	}
