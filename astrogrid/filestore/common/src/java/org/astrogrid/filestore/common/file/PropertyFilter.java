/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/file/PropertyFilter.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:19:27 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: PropertyFilter.java,v $
 *   Revision 1.2  2004/11/25 00:19:27  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.1  2004/11/02 23:20:12  dave
 *   Added property filter and changed method names to make them FileStore specific.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common.file ;

/**
 * The public interface for a properties filter.
 * Filters are used to prevent properties set by the local system from being changed by an external entity.
 *
 *
 */
public interface PropertyFilter
	{
	/**
	 * Filter a property.
	 * @param property The property to filter.
	 * @return The filtered property value, or null if the whole property has been filtered out.
	 *
	 */
	public FileProperty filter(FileProperty property) ;

	}
