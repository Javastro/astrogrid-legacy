/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/client/src/java/org/astrogrid/filestore/client/FileStoreMockDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:19:20 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreMockDelegate.java,v $
 *   Revision 1.4  2004/11/25 00:19:20  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.3.14.1  2004/11/04 02:33:03  dave
 *   Refactored mock delegate and config to make it easier to test filemanager with multiple filstores.
 *
 *   Revision 1.3  2004/09/17 06:57:10  dave
 *   Added commons logging to FileStore.
 *   Updated logging properties in Community.
 *   Fixed bug in AGINAB deployment.
 *   Removed MySpace tests with hard coded grendel address.
 *
 *   Revision 1.2.70.1  2004/09/17 01:08:36  dave
 *   Updated debug to use commons logging API ....
 *
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.1.2.2  2004/07/06 10:49:45  dave
 *   Added SOAP delegate
 *
 *   Revision 1.1.2.1  2004/07/06 09:16:12  dave
 *   Added delegate interface and mock implementation
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.client ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filestore.common.FileStoreConfigMock;

import org.astrogrid.filestore.common.FileStore ;
import org.astrogrid.filestore.common.FileStoreConfig ;
import org.astrogrid.filestore.common.FileStoreMock ;

/**
 * Mock implementation of the delegate interface.
 *
 */
public class FileStoreMockDelegate
	extends FileStoreCoreDelegate
	{
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FileStoreMockDelegate.class);

	/**
	 * Public constructor (creates a new FileStoreMock service with default config).
	 *
	 */
	public FileStoreMockDelegate()
		{
		this.service = new FileStoreMock() ;
		}

	/**
	 * Public constructor using a specific config.
	 *
	 */
	public FileStoreMockDelegate(FileStoreConfig config)
		{
		this.service = new FileStoreMock(config) ;
		}

	/**
	 * Public constructor using a specific ivorn.
	 *
	 */
	public FileStoreMockDelegate(Ivorn ivorn)
		{
		this(
			new FileStoreConfigMock(
				ivorn
				)
			) ;
		}

	/**
	 * Public constructor using a specific ivorn.
	 *
	 */
	public FileStoreMockDelegate(String ivorn)
		{
		this(
			new FileStoreConfigMock(
				ivorn
				)
			) ;
		}

	}

