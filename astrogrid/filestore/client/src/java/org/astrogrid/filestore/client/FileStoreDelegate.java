/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/client/src/java/org/astrogrid/filestore/client/FileStoreDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/19 23:42:07 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreDelegate.java,v $
 *   Revision 1.3  2004/07/19 23:42:07  dave
 *   Merged development branch, dave-dev-200407151443, into HEAD
 *
 *   Revision 1.2.4.1  2004/07/19 19:40:28  dave
 *   Debugged and worked around Axis Exception handling
 *
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.1.2.5  2004/07/13 16:37:29  dave
 *   Refactored common and client to use an array of FileProperties (more SOAP friendly)
 *
 *   Revision 1.1.2.4  2004/07/12 14:39:03  dave
 *   Added server repository classes
 *
 *   Revision 1.1.2.3  2004/07/08 07:31:30  dave
 *   Added container impl and tests
 *
 *   Revision 1.1.2.2  2004/07/07 14:54:35  dave
 *   Changed DataInfo to File Info (leaves room to use DataInfo for the more abstract VoStore interface).
 *
 *   Revision 1.1.2.1  2004/07/06 09:16:12  dave
 *   Added delegate interface and mock implementation
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.client ;

import java.rmi.Remote ;
import java.rmi.RemoteException ;

import org.astrogrid.filestore.common.FileStore ;
import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.transfer.TransferInfo ;
import org.astrogrid.filestore.common.exception.FileStoreException ;
import org.astrogrid.filestore.common.exception.FileStoreNotFoundException ;
import org.astrogrid.filestore.common.exception.FileIdentifierException ;
import org.astrogrid.filestore.common.exception.FileStoreServiceException ;

/**
 * Public interface for the file store delegate.
 * This mirrors the service interface, without the remote exceptions.
 *
 */
public interface FileStoreDelegate
	{
	/**
	 * Get the service identifier - used for testing.
	 * @return The ivo identifier for this service.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public String identifier()
		throws FileStoreServiceException ;

	/**
	 * Import (store) a string of data.
	 * @param properties An array of FileProperties describing the imported data.
	 * @param data The string of data to store.
	 * @return An array of FileProperties describing the imported data.
	 * @throws FileStoreException if the data string is null.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public FileProperty[] importString(FileProperty[] properties, String data)
		throws FileStoreServiceException, FileStoreException ;

	/**
	 * Import (store) a byte array of data.
	 * @param properties An array of FileProperties describing the imported data.
	 * @param data The byte array of data to store.
	 * @return An array of FileProperties describing the imported data.
	 * @throws FileStoreException if the data string is null.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public FileProperty[] importBytes(FileProperty[] properties, byte[] data)
		throws FileStoreServiceException, FileStoreException ;

	/**
	 * Append a string to existing data.
	 * @param ident The internal identifier of the target.
	 * @param data The string to append.
	 * @return An array of FileProperties describing the imported data.
	 * @throws FileIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreException if the string is null.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public FileProperty[] appendString(String ident, String data)
		throws FileStoreServiceException, FileIdentifierException, FileStoreNotFoundException, FileStoreException ;

	/**
	 * Append an array of bytes to existing data.
	 * @param ident The internal identifier of the target.
	 * @param data The bytes to append.
	 * @return An array of FileProperties describing the imported data.
	 * @throws FileIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreException if the array is null.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public FileProperty[] appendBytes(String ident, byte[] data)
		throws FileStoreServiceException, FileIdentifierException, FileStoreNotFoundException, FileStoreException ;

	/**
	 * Export (read) the contents of a file as a string.
	 * @param ident The internal identifier of the target file.
	 * @return The contents of a data object as a string.
	 * @throws FileIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public String exportString(String ident)
		throws FileStoreServiceException, FileIdentifierException, FileStoreNotFoundException ;

	/**
	 * Export (read) the contents of a file as an array of bytes.
	 * @param ident The internal identifier of the target file.
	 * @return The contents of a data object as a string.
	 * @throws FileIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public byte[] exportBytes(String ident)
		throws FileStoreServiceException, FileIdentifierException, FileStoreNotFoundException ;

	/**
	 * Create a local duplicate (copy) of a file.
	 * @param ident The internal identifier of the target file.
	 * @param properties An optional array of FileProperties describing the copy.
	 * @return An array of FileProperties describing the copied data.
	 * @throws FileIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public FileProperty[] duplicate(String ident, FileProperty[] properties)
		throws FileStoreServiceException, FileIdentifierException, FileStoreNotFoundException ;

	/**
	 * Delete a file.
	 * @param ident The internal identifier of the target file.
	 * @return An array of FileProperties describing the deleted data.
	 * @throws FileIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public FileProperty[] delete(String ident)
		throws FileStoreServiceException, FileIdentifierException, FileStoreNotFoundException ;

	/**
	 * Get the local meta data information for a file.
	 * @param ident The internal identifier of the target file.
	 * @return An array of FileProperties describing the data.
	 * @throws FileIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public FileProperty[] properties(String ident)
		throws FileStoreServiceException, FileIdentifierException, FileStoreNotFoundException ;

	/**
	 * Prepare to receive a file from a remote source.
	 * @param info A TransferInfo object describing the transfer.
	 * @return A new TransferInfo describing the transfer.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public TransferInfo importInit(TransferInfo info)
		throws FileStoreServiceException ;

	/**
	 * Import a file from a remote source.
	 * @param info A TransferInfo object describing the transfer.
	 * @return A new TransferInfo describing the transfer.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public TransferInfo importData(TransferInfo info)
		throws FileStoreServiceException ;

	/**
	 * Prepare to send a file to a remote destination.
	 * @param info A TransferInfo object describing the transfer.
	 * @return A new TransferInfo describing the transfer.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public TransferInfo exportInit(TransferInfo info)
		throws FileStoreServiceException ;

	/**
	 * Export a file to a remote destination.
	 * @param info A TransferInfo object describing the transfer.
	 * @return A new TransferInfo describing the transfer.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public TransferInfo exportData(TransferInfo info)
		throws FileStoreServiceException ;

	/**
	 * Throw a FileIdentifierException, useful for debugging the transfer of Exceptions via SOAP.
	 * @throws FileIdentifierException unpacking it from the RemoteException when invoked via a SOAP call.
	 * @throws FileStoreServiceException if the service was unable to handle the request.
	 *
	 */
	public void throwIdentifierException()
		throws FileIdentifierException, FileStoreServiceException ;

	}

