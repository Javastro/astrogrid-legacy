/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/FileManagerNode.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:20:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerNode.java,v $
 *   Revision 1.2  2004/11/25 00:20:29  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.2  2004/11/16 03:26:14  dave
 *   Added initial tests for adding accounts, containers and files ...
 *
 *   Revision 1.1.2.1  2004/11/13 01:41:26  dave
 *   Created initial client API ....
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.client ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException;

/**
 * The public API for a FileManager node.
 *
 */
public interface FileManagerNode
	{

	/**
	 * Get the node ivorn.
	 *
	 */
	public Ivorn getIvorn()
		throws FileManagerIdentifierException;

	/**
	 * Get the node name.
	 *
	 */
	public String getName();

	/**
	 * Check if this represents a file.
	 * @return true if this node represents a file.
	 *
	 */
	public boolean isFile() ;

	/**
	 * Check if this represents a container.
	 * @return true if this node represents a container.
	 *
	 */
	public boolean isContainer() ;

	/**
	 * Add a container node.
	 * @param name The container name.
	 * @return A new node for the container.
	 *
	 */
	public FileManagerNode addContainer(String name) ;

	}
