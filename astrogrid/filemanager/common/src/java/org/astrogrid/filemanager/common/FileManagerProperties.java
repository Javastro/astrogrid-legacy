/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/FileManagerProperties.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/12/16 17:25:49 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerProperties.java,v $
 *   Revision 1.3  2004/12/16 17:25:49  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.1.2.12  2004/12/06 13:29:02  dave
 *   Added initial code for move location ....
 *
 *   Revision 1.1.2.11  2004/12/03 13:27:52  dave
 *   Core of internal move is in place ....
 *
 *   Revision 1.1.2.10  2004/12/02 19:11:54  dave
 *   Added move name and parent to manager ...
 *
 *   Revision 1.1.2.9  2004/11/29 18:05:07  dave
 *   Refactored methods names ....
 *   Added stubs for delete, copy and move.
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

import java.util.Iterator;

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
	 * The property key for the store service ivorn.
	 *
	 */
	public static final String MANAGER_LOCATION_IVORN  = "org.astrogrid.filemanager.location.ivorn" ;

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
	public void setManagerParentIvorn(Ivorn ivorn)
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
	public void setManagerParentIvorn(String ivorn)
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
	public void setManagerResourceName(String name)
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
	public void setManagerResourceType(String type)
		{
		this.setProperty(
			MANAGER_RESOURCE_TYPE,
			type
			) ;
		}

	/**
	 * Get the filestore storage location.
	 *
	 */
	public Ivorn getManagerLocationIvorn()
		throws FileManagerIdentifierException
		{
		String ivorn = this.getProperty(
			MANAGER_LOCATION_IVORN
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
	 * Set the filestore storage location.
	 * @param ivorn The filestore location.
	 *
	 */
	public void setManagerLocationIvorn(Ivorn ivorn)
		{
		if (null != ivorn)
			{
			this.setProperty(
				MANAGER_LOCATION_IVORN,
				ivorn.toString()
				) ;
			}
		else {
			this.setProperty(
				MANAGER_LOCATION_IVORN,
				null
				) ;
			}
		}

	/**
	 * Create a new set of properties containing the differences between this set and another.
	 * This compares the properties in the target set and only transfers those which have
	 * different values to those in the current set.
	 * @param that The other set to compare with.
	 * @return A new set containing only the properties that have been changed.
	 *
	 */
	public FileManagerProperties difference(FileManagerProperties that)
		{
		if (null == that)
			{
			throw new IllegalArgumentException(
				"Null properties"
				);
			}
		//
		// Create a new set of properties.
		FileManagerProperties results = new FileManagerProperties();
		//
		// Get an iterator for the other properties.
		Iterator iter = that.getProperties().keySet().iterator();
		//
		// Check each of the other properties.
		while (iter.hasNext())
			{
			String key   = (String) iter.next();
			String current = this.getProperty(key);
			String changed = that.getProperty(key);
			//
			// If the changed property is not null.
			if (null != changed)
				{
				//
				// If our property is null.
				if (null == current)
					{
					results.setProperty(
						key,
						changed
						);
					}
				//
				// If our property is not null.
				else {
					//
					// If values are different.
					if (!current.equals(changed))
						{
						results.setProperty(
							key,
							changed
							);
						}
					}
				}
			}
		//
		// Return the new set of properties.
		return results ;
		}
	}

