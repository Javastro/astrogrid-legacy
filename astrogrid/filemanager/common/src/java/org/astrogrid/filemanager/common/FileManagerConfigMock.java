/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/FileManagerConfigMock.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:20:27 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerConfigMock.java,v $
 *   Revision 1.2  2004/11/25 00:20:27  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.3  2004/11/05 02:23:45  dave
 *   Refactored identifiers are properties ...
 *
 *   Revision 1.1.2.2  2004/11/04 02:33:38  dave
 *   Refactored test to include multiple filestores ...
 *
 *   Revision 1.1.2.1  2004/10/21 21:08:52  dave
 *   Added config interface and mock implementation.
 *   Partial implementation of data import into FileStore via direct URL transfer.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filemanager.common.exception.FileManagerServiceException ;

/**
 * Mock implementation of the FileManager configuration.
 *
 */
public class FileManagerConfigMock
	implements FileManagerConfig
	{
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FileManagerConfigMock.class);

	/**
	 * Our manager ivorn.
	 *
	 */
	private Ivorn filemanager ;

	/**
	 * Our default FileStore Ivorn.
	 *
	 */
	private Ivorn filestore ;

	/**
	 * Public constructor.
	 *
	 */
	public FileManagerConfigMock(Ivorn filemanager, Ivorn filestore)
		{
		this.filemanager = filemanager ;
		this.filestore   = filestore ;
		}

	/**
	 * Get the manager ivorn.
	 * @throws FileManagerServiceException If unable to read the configuration.
	 *
	 */
	public Ivorn getFileManagerIvorn()
		{
		return this.filemanager ;
		}

	/**
	 * Get the default FileStore Ivorn.
	 * @throws FileManagerServiceException
	 *
	 */
	public Ivorn getFileStoreIvorn()
		{
		return this.filestore ;
		}

	}
