/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/server/src/java/org/astrogrid/filestore/server/repository/RepositoryContainer.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/14 13:50:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: RepositoryContainer.java,v $
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.1.2.2  2004/07/14 10:34:08  dave
 *   Reafctored server to use array of properties
 *
 *   Revision 1.1.2.1  2004/07/12 14:39:03  dave
 *   Added server repository classes
 *
 *   Revision 1.1.2.1  2004/07/08 07:31:30  dave
 *   Added container impl and tests
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.server.repository ;

import java.io.InputStream ;
import java.io.OutputStream ;

import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.file.FileProperties ;
import org.astrogrid.filestore.common.file.FileIdentifier ;
import org.astrogrid.filestore.common.exception.FileStoreServiceException ;
import org.astrogrid.filestore.common.exception.FileStoreNotFoundException ;

/**
 * Public interface for a file container.
 *
 */
public interface RepositoryContainer
	{

	/**
	 * Access to our identifier.
	 * @return The internal identifier for the file.
	 *
	 */
	public FileIdentifier ident() ;

	/**
	 * Access to our properties.
	 * @return The info for the file.
	 *
	 */
	public FileProperties properties() ;

	/**
	 * Import an array of bytes.
	 * @param bytes The array of bytes to import into the container.
	 * @throws FileStoreServiceException If unable to complete the action.
	 *
	 */
	public void importBytes(byte[] bytes)
		throws FileStoreServiceException ;

	/**
	 * Export our contents as bytes.
	 * @return An array of bytes from the container contents.
	 * @throws FileStoreNotFoundException If unable to locate the file.
	 * @throws FileStoreServiceException If unable to complete the action.
	 *
	 */
	public byte[] exportBytes()
		throws FileStoreServiceException, FileStoreNotFoundException ;

	/**
	 * Append an array of bytes.
	 * @param bytes The array of bytes to append to the container.
	 * @throws FileStoreNotFoundException If unable to locate the file.
	 * @throws FileStoreServiceException If unable to complete the action.
	 *
	 */
	public void appendBytes(byte[] bytes)
		throws FileStoreServiceException, FileStoreNotFoundException ;

	/**
	 * Delete the contents of the container.
	 * @throws FileStoreNotFoundException If unable to locate the file.
	 * @throws FileStoreServiceException If unable to complete the action.
	 *
	 */
	public void delete()
		throws FileStoreServiceException, FileStoreNotFoundException ;

	/**
	 * Get an input stream to the container contents.
	 * @throws FileStoreNotFoundException If unable to locate the file.
	 * @throws FileStoreServiceException If unable to complete the action.
	 *
	 */
	public InputStream getDataInputStream()
		throws FileStoreServiceException, FileStoreNotFoundException ;

	/**
	 * Get an output stream to the container contents.
	 * @throws FileStoreServiceException If unable to complete the action.
	 *
	 */
	public OutputStream getDataOutputStream()
		throws FileStoreServiceException ;

	}

