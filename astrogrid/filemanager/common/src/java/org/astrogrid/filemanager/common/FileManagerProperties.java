/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/FileManagerProperties.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:20:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerProperties.java,v $
 *   Revision 1.2  2004/11/25 00:20:29  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.8  2004/11/11 17:53:10  dave
 *   Removed Node interface from the server side ....
 *
 *   Revision 1.1.2.7  2004/11/10 17:00:11  dave
 *   Moving the manager API towards property based rather than node based ...
 *
 *   Revision 1.1.2.6  2004/11/06 20:03:17  dave
 *   Implemented ImportInit and ExportInit using properties
 *
 *   Revision 1.1.2.5  2004/11/05 05:58:31  dave
 *   Refactored the properties handling in importInitEx() ..
 *
 *   Revision 1.1.2.4  2004/11/05 02:23:45  dave
 *   Refactored identifiers are properties ...
 *
 *   Revision 1.1.2.3  2004/11/04 02:33:38  dave
 *   Refactored test to include multiple filestores ...
 *
 *   Revision 1.1.2.2  2004/11/02 23:40:08  dave
 *   Fixed typos and bugs ...
 *
 *   Revision 1.1.2.1  2004/11/02 23:21:22  dave
 *   Added FileManagerProperties and filter ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filemanager.common.ivorn.FileManagerIvornParser ;
import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException ;

import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.file.FileProperties ;
import org.astrogrid.filestore.common.file.PropertyFilter ;

/**
 * A wrapper for the FileStore properties to include the FileManager properties.
 *
 */
public class FileManagerProperties
	extends FileProperties
	{

	/**
	 * The property key for the resource (node) ivorn.
	 *
	 */
	public static final String MANAGER_RESOURCE_IVORN  = "org.astrogrid.filemanager.resource.ivorn" ;

	/**
	 * The property key for the parent (node) ivorn.
	 *
	 */
	public static final String MANAGER_PARENT_IVORN  = "org.astrogrid.filemanager.parent.ivorn" ;

	/**
	 * The property key for the resource (node) type.
	 *
	 */
	public static final String MANAGER_RESOURCE_TYPE  = "org.astrogrid.filemanager.resource.type" ;

	/**
	 * The property key for the resource (node) name.
	 *
	 */
	public static final String MANAGER_RESOURCE_NAME  = "org.astrogrid.filemanager.resource.name" ;

	/**
	 * Type name for a data node.
	 *
	 */
	public static final String DATA_NODE_TYPE = "org.astrogrid.filemanager.node.type.data" ;

	/**
	 * Type name for a container node.
	 *
	 */
	public static final String CONTAINER_NODE_TYPE = "org.astrogrid.filemanager.node.type.container" ;

	/**
	 * Public constructor.
	 *
	 */
	public FileManagerProperties()
		{
		super() ;
		}

	/**
	 * Public constructor from an array of values.
	 * @param array An array of property values.
	 *
	 */
	public FileManagerProperties(FileProperty[] array)
		{
		super(array);
		}

	/**
	 * Public constructor from another set of properties.
	 * @param that The other set of properties.
	 *
	 */
	public FileManagerProperties(FileProperties that)
		{
		super(that) ;
		}

	/**
	 * Get the resource ivorn.
	 * @throws FileManagerIdentifierException if the resource identifier is invalid.
	 *
	 */
	public Ivorn getManagerResourceIvorn()
		throws FileManagerIdentifierException
		{
		String ivorn = this.getProperty(
			MANAGER_RESOURCE_IVORN
			) ;
		if (null != ivorn)
			{
			try {
				return new Ivorn(
					ivorn
					) ;
				}
			catch (Exception ouch)
				{
				throw new FileManagerIdentifierException(
					ivorn
					);
				}
			}
		else {
			return null ;
			}
		}

	/**
	 * Set the resource ivorn.
	 *
	 */
	public void setManagerResourceIvorn(Ivorn ivorn)
		{
		if (null != ivorn)
			{
			this.setManagerResourceIvorn(
				ivorn.toString()
				) ;
			}
		else {
			this.setProperty(
				MANAGER_RESOURCE_IVORN,
				null
				) ;
			}
		}

	/**
	 * Set the resource ivorn.
	 *
	 */
	public void setManagerResourceIvorn(String ivorn)
		{
		if (null != ivorn)
			{
			this.setProperty(
				MANAGER_RESOURCE_IVORN,
				ivorn
				) ;
			}
		else {
			this.setProperty(
				MANAGER_RESOURCE_IVORN,
				null
				) ;
			}
		}

	/**
	 * Get the service ivorn.
	 * @throws FileManagerIdentifierException if the resource identifier is invalid.
	 *
	 */
	public Ivorn getManagerServiceIvorn()
		throws FileManagerIdentifierException
		{
		String ivorn = this.getProperty(
			MANAGER_RESOURCE_IVORN
			) ;
		if (null != ivorn)
			{
			return new FileManagerIvornParser(
				ivorn
				).getServiceIvorn() ;
			}
		else {
			return null ;
			}
		}

	/**
	 * Get the resource identifier.
	 * @throws FileManagerIdentifierException if the resource identifier is invalid.
	 *
	 */
	public String getManagerResourceIdent()
		throws FileManagerIdentifierException
		{
		String ivorn = this.getProperty(
			MANAGER_RESOURCE_IVORN
			) ;
		if (null != ivorn)
			{
			return new FileManagerIvornParser(
				ivorn
				).getResourceIdent() ;
			}
		else {
			return null ;
			}
		}

	/**
	 * Get the parent resource ivorn.
	 * @throws FileManagerIdentifierException if the resource identifier is invalid.
	 *
	 */
	public Ivorn getManagerParentIvorn()
		throws FileManagerIdentifierException
		{
		String ivorn = this.getProperty(
			MANAGER_PARENT_IVORN
			) ;
		if (null != ivorn)
			{
			try {
				return new Ivorn(
					ivorn
					) ;
				}
			catch (Exception ouch)
				{
				throw new FileManagerIdentifierException(
					ivorn
					);
				}
			}
		else {
			return null ;
			}
		}

	/**
	 * Set the parent resource ivorn.
	 * @param ivorn The parent resource ivorn.
	 *
	 */
	protected void setManagerParentIvorn(Ivorn ivorn)
		{
		if (null != ivorn)
			{
			this.setProperty(
				MANAGER_PARENT_IVORN,
				ivorn.toString()
				) ;
			}
		else {
			this.setProperty(
				MANAGER_PARENT_IVORN,
				null
				) ;
			}
		}

	/**
	 * Set the parent resource ivorn.
	 * @param ivorn The parent resource ivorn.
	 *
	 */
	protected void setManagerParentIvorn(String ivorn)
		{
		this.setProperty(
			MANAGER_PARENT_IVORN,
			ivorn
			) ;
		}

	/**
	 * Get the resource name.
	 *
	 */
	public String getManagerResourceName()
		{
		return this.getProperty(
			MANAGER_RESOURCE_NAME
			) ;
		}

	/**
	 * Set the resource (node) name.
	 *
	 */
	protected void setManagerResourceName(String name)
		{
		this.setProperty(
			MANAGER_RESOURCE_NAME,
			name
			) ;
		}

	/**
	 * Get the resource (node) type.
	 *
	 */
	public String getManagerResourceType()
		{
		return this.getProperty(
			MANAGER_RESOURCE_TYPE
			) ;
		}

	/**
	 * Set the resource (node) type.
	 *
	 */
	protected void setManagerResourceType(String type)
		{
		this.setProperty(
			MANAGER_RESOURCE_TYPE,
			type
			) ;
		}

	}

