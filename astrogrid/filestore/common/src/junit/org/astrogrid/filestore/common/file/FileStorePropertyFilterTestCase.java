/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/junit/org/astrogrid/filestore/common/file/FileStorePropertyFilterTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:19:27 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStorePropertyFilterTestCase.java,v $
 *   Revision 1.2  2004/11/25 00:19:27  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.3  2004/11/06 19:12:18  dave
 *   Refactored identifier properties ...
 *
 *   Revision 1.1.2.2  2004/11/02 23:40:45  dave
 *   Added the rest of the reserved properties to the filter test ...
 *
 *   Revision 1.1.2.1  2004/11/02 23:20:12  dave
 *   Added property filter and changed method names to make them FileStore specific.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common.file ;

import junit.framework.TestCase ;

/**
 * A JUnit test case for the file store property filter.
 *
 */
public class FileStorePropertyFilterTestCase
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
		filter = new FileStorePropertyFilter() ;
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
	 * Check that a reserved property is filtered.
	 *
	 */
	public void testFilterReserved()
		{
		assertNull(
			filter.filter(
				new FileProperty(
					FileProperties.STORE_RESOURCE_IVORN,
					"albert"
					)
				)
			);
		assertNull(
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
