/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/test/org/astrogrid/filemanager/common/Attic/FileManagerPropertyFilterTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:20:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerPropertyFilterTestCase.java,v $
 *   Revision 1.2  2004/11/25 00:20:29  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.4  2004/11/06 20:03:17  dave
 *   Implemented ImportInit and ExportInit using properties
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

import junit.framework.TestCase ;

import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.file.FileProperties ;
import org.astrogrid.filestore.common.file.PropertyFilter ;

/**
 * A JUnit test case for the file store property filter.
 *
 */
public class FileManagerPropertyFilterTestCase
	extends TestCase
	{

	/**
	 * Our target filter.
	 *
	 */
	private PropertyFilter filter ;

	/**
	 * Setup our test.
	 *
	 */
	public void setUp()
		{
		filter = new FileManagerPropertyFilter() ;
		}

	/**
	 * Check we created our filter.
	 *
	 */
	public void testCreated()
		throws Exception
		{
		assertNotNull(
			this.filter
			) ;
		}

	/**
	 * Check that the manager properties are filtered.
	 *
	 */
	public void testFilterReserved()
		{
		assertNull(
			filter.filter(
				new FileProperty(
					FileManagerProperties.MANAGER_RESOURCE_IVORN,
					"albert"
					)
				)
			);
		}

	/**
	 * Check that the filestore properties are not filtered.
	 *
	 */
	public void testFilterFileStoreReserved()
		{
		assertNotNull(
			filter.filter(
				new FileProperty(
					FileProperties.STORE_RESOURCE_IVORN,
					"albert"
					)
				)
			);
		assertNotNull(
			filter.filter(
				new FileProperty(
					FileProperties.STORE_RESOURCE_URL,
					"albert"
					)
				)
			);
		}

	/**
	 * Check that a test property is not filtered.
	 *
	 */
	public void testFilterNormal()
		{
		assertNotNull(
			filter.filter(
				new FileProperty(
					"test.property",
					"albert"
					)
				)
			);
		}

	}
