/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/FileManagerPropertyFilter.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:20:27 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerPropertyFilter.java,v $
 *   Revision 1.2  2004/11/25 00:20:27  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.3  2004/11/05 02:23:45  dave
 *   Refactored identifiers are properties ...
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

import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.file.PropertyFilter ;

/**
 * This filter excludes properties that should be set by the local FileManager service.
 * This prevents external entities from changing the local properties.
 *
 *
 */
public class FileManagerPropertyFilter
	implements PropertyFilter
	{
	/**
	 * Public constructor.
	 *
	 */
	public FileManagerPropertyFilter()
		{
		}

	/**
	 * Filter a property.
	 * @param property The property to filter.
	 * @return The filtered property value, or null if the whole property has been filtered out.
	 *
	 */
	public FileProperty filter(FileProperty property)
		{
		if (FileManagerProperties.MANAGER_RESOURCE_IVORN.equals(property.getName()))
			{
			return null ;
			}
		//
		// Return the original property.
		return property ;
		}
	}

