/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/file/FileStorePropertyFilter.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:19:27 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStorePropertyFilter.java,v $
 *   Revision 1.2  2004/11/25 00:19:27  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.2  2004/11/06 19:12:18  dave
 *   Refactored identifier properties ...
 *
 *   Revision 1.1.2.1  2004/11/02 23:20:12  dave
 *   Added property filter and changed method names to make them FileStore specific.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common.file ;

/**
 * This filter excludes properties that should be set by the local FileStore service.
 * This prevents external entities from changing the local properties.
 *
 *
 */
public class FileStorePropertyFilter
	implements PropertyFilter
	{
	/**
	 * Public constructor.
	 *
	 */
	public FileStorePropertyFilter()
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
		if (FileProperties.STORE_RESOURCE_IVORN.equals(property.getName()))
			{
			return null ;
			}
		if (FileProperties.STORE_RESOURCE_URL.equals(property.getName()))
			{
			return null ;
			}
		//
		// Return the original property.
		return property ;
		}
	}

