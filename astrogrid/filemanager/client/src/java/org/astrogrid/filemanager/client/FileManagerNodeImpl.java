/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/Attic/FileManagerNodeImpl.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:20:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerNodeImpl.java,v $
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

import org.astrogrid.filestore.common.file.FileProperty;
import org.astrogrid.filemanager.common.FileManagerProperties;
import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException;

/**
 * Client implementation of the FileManagerNode interface.
 *
 */
public class FileManagerNodeImpl
	implements FileManagerNode
	{

	/**
	 * Protected constructor from an array of file properties.
	 *
	 */
	protected FileManagerNodeImpl(FileProperty[] properties)
		{
		this(
			new FileManagerProperties(
				properties
				)
			);
		}

	/**
	 * Protected constructor from a file magare properties map.
	 *
	 */
	protected FileManagerNodeImpl(FileManagerProperties properties)
		{
		this.properties = properties ;
		}

	/**
	 * Our node properties.
	 *
	 */
	private FileManagerProperties properties ;

	/**
	 * Get the node ivorn.
	 *
	 */
	public Ivorn getIvorn()
		throws FileManagerIdentifierException
		{
		return properties.getManagerResourceIvorn();
		}

	/**
	 * Get the node name.
	 *
	 */
	public String getName()
		{
		return properties.getManagerResourceName();
		}

	/**
	 * Check if this represents a file.
	 * @return true if this represents a file.
	 *
	 */
	public boolean isFile()
		{
		return FileManagerProperties.DATA_NODE_TYPE.equals(
			properties.getManagerResourceType()
			) ;
		}

	/**
	 * Check if this represents a container.
	 * @return true if this represents a container.
	 *
	 */
	public boolean isContainer()
		{
		return FileManagerProperties.CONTAINER_NODE_TYPE.equals(
			properties.getManagerResourceType()
			) ;
		}

	/**
	 * Add a container node.
	 * @param name The container name.
	 * @return A new node for the container.
	 *
	 */
	public FileManagerNode addContainer(String name)
		{
		return null ;
		}



	}
