/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/server/src/junit/org/astrogrid/filestore/server/FileStoreImplTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/12/13 00:51:56 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreImplTestCase.java,v $
 *   Revision 1.4  2004/12/13 00:51:56  jdt
 *   merge from FLS_JDT_861
 *
 *   Revision 1.3.2.1  2004/12/09 11:54:53  jdt
 *   Made file:// windows-friendly.
 *
 *   Revision 1.3  2004/11/25 00:19:27  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.2.84.3  2004/11/09 17:41:36  dave
 *   Added file:// URL handling to allow server URLs to be tested.
 *   Added importInit and exportInit to server implementation.
 *   Moved remaining tests out of extended test abd removed it.
 *
 *   Revision 1.2.84.2  2004/11/01 16:25:08  dave
 *   Experimenting with import and export tests for the server impl.
 *
 *   Revision 1.2.84.1  2004/10/26 15:50:48  dave
 *   Extended support for testing PUT transfers ...
 *
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.1.2.1  2004/07/12 14:39:03  dave
 *   Added server repository classes
 *
 *   Revision 1.1.2.1  2004/07/05 04:50:29  dave
 *   Created initial FileStore components
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.server ;

import java.net.URL ;
import org.astrogrid.filestore.common.exception.FileStoreServiceException ;

import org.astrogrid.filestore.common.FileStoreTest ;
import org.astrogrid.filestore.common.FileStoreConfig ;
import org.astrogrid.filestore.common.transfer.mock.Handler ;

/**
 * A JUnit test case for the store service.
 * @todo Needs refactoring befoer we can run extended tests ....
 *
 */
public class FileStoreImplTestCase
	extends FileStoreTest
	{


	/**
	 * Setup our test.
	 *
	 */
	public void setUp()
		{
		//
		// Initialise our mock URL handler.
		Handler.register() ;
		//
		// Create our test config.
		FileStoreConfig config = new FileStoreConfigImpl()
			{
			/**
			 * Generate a resource URL.
			 * @param ident - the resource identifier.
			 * @throws FileStoreServiceException if unable to read the property.
			 *
			 */
			public URL getResourceUrl(String ident)
				throws FileStoreServiceException
				{
				try {
					return new URL(
						"file:///" + 
						this.getDataDirectory()
						+ "/" + ident + ".dat"
						) ;
					}
				catch (Throwable ouch)
					{
					throw new FileStoreServiceException(
						"Unable to generate resource URL.",
						ouch
						) ;
					}
				}
			} ;

		//
		// Create our target filestore.
		this.target = new FileStoreImpl(
			config
			) ;
		}
	}
