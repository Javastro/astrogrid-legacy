/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/server/src/java/org/astrogrid/filestore/server/FileStoreImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/21 18:11:55 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreImpl.java,v $
 *   Revision 1.4  2004/07/21 18:11:55  dave
 *   Merged development branch, dave-dev-200407201059, into HEAD
 *
 *   Revision 1.3.2.1  2004/07/21 16:28:16  dave
 *   Added content properties and tests
 *
 *   Revision 1.3  2004/07/19 23:42:07  dave
 *   Merged development branch, dave-dev-200407151443, into HEAD
 *
 *   Revision 1.2.4.1  2004/07/19 19:40:28  dave
 *   Debugged and worked around Axis Exception handling
 *
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.1.2.3  2004/07/14 10:34:08  dave
 *   Reafctored server to use array of properties
 *
 *   Revision 1.1.2.2  2004/07/12 14:39:03  dave
 *   Added server repository classes
 *
 *   Revision 1.1.2.1  2004/07/08 07:31:30  dave
 *   Added container impl and tests
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.server ;

import java.net.URL ;
import java.net.MalformedURLException ;

import java.io.IOException ;

import java.util.Map ;
import java.util.HashMap ;

import java.rmi.RemoteException  ;
import org.apache.axis.AxisFault ;

import org.astrogrid.filestore.common.FileStore ;
import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.file.FileProperties ;
import org.astrogrid.filestore.common.file.FileIdentifier ;
import org.astrogrid.filestore.common.transfer.TransferProperties ;
import org.astrogrid.filestore.common.exception.FileStoreException ;
import org.astrogrid.filestore.common.exception.FileStoreNotFoundException ;
import org.astrogrid.filestore.common.exception.FileIdentifierException ;
import org.astrogrid.filestore.common.exception.FileStoreServiceException ;
import org.astrogrid.filestore.common.exception.FileStoreTransferException ;

import org.astrogrid.filestore.server.repository.Repository ;
import org.astrogrid.filestore.server.repository.RepositoryImpl ;
import org.astrogrid.filestore.server.repository.RepositoryConfig ;
import org.astrogrid.filestore.server.repository.RepositoryConfigImpl ;
import org.astrogrid.filestore.server.repository.RepositoryContainer ;

/**
 * The main server implementation of the store service.
 *
 */
public class FileStoreImpl
	implements FileStore
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	protected static final boolean DEBUG_FLAG = true ;

	/**
	 * Public constructor.
	 *
	 */
	public FileStoreImpl()
		{
		this(
			new RepositoryConfigImpl()
			) ;
		}

	/**
	 * Public constructor.
	 * @param config The local repository configuration.
	 *
	 */
	public FileStoreImpl(RepositoryConfig config)
		{
		this(
			config,
			new RepositoryImpl(
				config
				)
			) ;
		}

	/**
	 * Public constructor.
	 * @param config The local repository configuration.
	 * @param repository A local file repository.
	 *
	 */
	public FileStoreImpl(RepositoryConfig config, Repository repository)
		{
		this.config = config ;
		this.repository = repository ;
		}

	/**
	 * Reference to our configuration.
	 *
	 */
	private RepositoryConfig config ;

	/**
	 * Reference to our repository.
	 *
	 */
	private Repository repository ;

	/**
	 * Get the service identifier - used for testing.
	 * @return The ivo identifier for this service.
	 *
	 */
	public String identifier()
		{
		return config.getServiceIdent() ;
		}

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
		throws FileStoreServiceException, FileStoreException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreImpl.importString()") ;
		if (DEBUG_FLAG) System.out.println("TOAD") ;
		//
		// Check for null data.
		if (null == data)
			{
			throw new FileStoreException(
				"Null data"
				) ;
			}
		//
		// Convert to bytes and import that.
		return this.importBytes(
			properties,
			data.getBytes()
			) ;
		}

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
		throws FileStoreServiceException, FileStoreException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreImpl.importBytes()") ;
		//
		// Check for null data.
		if (null == data)
			{
			throw new FileStoreException(
				"Null data"
				) ;
			}
		//
		// Create a new container.
		RepositoryContainer container = repository.create(
			new FileProperties(
				properties
				)
			) ;
		//
		// Import our data into the container.
		container.importBytes(
			data
			) ;
		//
		// Return the container properties.
		return container.properties().toArray() ;
		}

	/**
	 * Append a string to existing file.
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
		throws FileStoreServiceException, FileIdentifierException, FileStoreNotFoundException, FileStoreException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreImpl.appendString()") ;
		//
		// Check for null ident.
		if (null == ident)
			{
			throw new FileIdentifierException(
				FileIdentifierException.NULL_IDENT_MESSAGE
				) ;
			}
		//
		// Check for null data.
		if (null == data)
			{
			throw new FileStoreException(
				"Null data"
				) ;
			}
		//
		// Convert to bytes and append that.
		return this.appendBytes(
			ident,
			data.getBytes()
			) ;
		}

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
		throws FileStoreServiceException, FileIdentifierException, FileStoreNotFoundException, FileStoreException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreImpl.appendBytes()") ;
		//
		// Check for null ident.
		if (null == ident)
			{
			throw new FileIdentifierException(
				FileIdentifierException.NULL_IDENT_MESSAGE
				) ;
			}
		//
		// Check for null data.
		if (null == data)
			{
			throw new FileStoreException(
				"Null data"
				) ;
			}
		//
		// Locate the container.
		RepositoryContainer container = repository.load(ident) ;
		//
		// Append the data to our container.
		container.appendBytes(
			data
			) ;
		//
		// Return the container properties.
		return container.properties().toArray() ;
		}

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
		throws FileStoreServiceException, FileIdentifierException, FileStoreNotFoundException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreImpl.exportString()") ;
		return new String(
			this.exportBytes(
				ident
				)
			) ;
		}

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
		throws FileStoreServiceException, FileIdentifierException, FileStoreNotFoundException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreImpl.exportBytes()") ;
		//
		// Check for null ident.
		if (null == ident)
			{
			throw new FileIdentifierException(
				FileIdentifierException.NULL_IDENT_MESSAGE
				) ;
			}
		return repository.load(ident).exportBytes() ;
		}

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
		throws FileStoreServiceException, FileIdentifierException, FileStoreNotFoundException
		{
		//
		// Check for null ident.
		if (null == ident)
			{
			throw new FileIdentifierException(
				FileIdentifierException.NULL_IDENT_MESSAGE
				) ;
			}
		return repository.duplicate(ident).properties().toArray() ;
		}

	/**
	 * Delete a file.
	 * @param ident The internal identifier of the target file.
	 * @return An array of FileProperties describing the deleted data.
	 * @throws FileIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
     * @throws RemoteException If the WebService call fails.
	 *
	 */
	public FileProperty[] delete(String ident)
		throws FileStoreServiceException, FileIdentifierException, FileStoreNotFoundException
		{
		//
		// Locate the container.
		RepositoryContainer container = repository.load(ident) ;
		//
		// Try to delete the container.
		container.delete() ;
		//
		// Return the original properties.
		return container.properties().toArray() ;
		}

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
		throws FileStoreServiceException, FileIdentifierException, FileStoreNotFoundException
		{
		//
		// Locate the container and return the info.
		return repository.load(ident).properties().toArray() ;
		}

	/**
	 * Prepare to receive a data object from a remote source.
	 * @param transfer A TransferProperties describing the transfer.
	 * @return A new TransferProperties describing the transfer.
	 *
	 */
	public TransferProperties importInit(TransferProperties transfer)
		{
		return transfer ;
		}

	/**
	 * Import a data object from a remote source.
	 * @param transfer A TransferProperties describing the transfer.
	 * @return A new TransferProperties describing the transfer.
	 * @throws FileStoreServiceException if there is an error in the service.
	 * @throws FileStoreTransferException if there is an error in the transfer.
	 *
	 */
	public TransferProperties importData(TransferProperties transfer)
		throws FileStoreServiceException, FileStoreTransferException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreImpl.importData()") ;
		//
		// Check for null transfer properties.
		if (null == transfer)
			{
			throw new FileStoreTransferException(
				"Null transfer properties"
				) ;
			}
		//
		// Check for null URL.
		if (null == transfer.getSource())
			{
			throw new FileStoreTransferException(
				"Null transfer source"
				) ;
			}
		//
		// Create a new container.
		RepositoryContainer container = repository.create(
			new FileProperties(
				transfer.getFileProperties()
				)
			) ;
		//
		// Transfer the data into our container.
		try {
			container.importData(
				new URL(
					transfer.getSource()
					)
				) ;
			}
		catch (MalformedURLException ouch)
			{
			throw new FileStoreTransferException(
				ouch
				) ;
			}
		//
		// Add the updated file properties.
		transfer.setFileProperties(
			container.properties().toArray()
			) ;
		//
		// Return the transfer properties.
		return transfer ;
		}

	/**
	 * Prepare to send a data object to a remote destination.
	 * @param transfer A TransferProperties describing the transfer.
	 * @return A new TransferProperties describing the transfer.
	 *
	 */
	public TransferProperties exportInit(TransferProperties transfer)
		{
		return transfer ;
		}

	/**
	 * Export a data object to a remote destination.
	 * @param transfer A TransferProperties describing the transfer.
	 * @return A new TransferProperties describing the transfer.
	 *
	 */
	public TransferProperties exportData(TransferProperties transfer)
		{
		return transfer ;
		}

	/**
	 * Throw a FileIdentifierException, useful for debugging the transfer of Exceptions via SOAP.
	 * @throws FileIdentifierException as requested.
	 *
	 */
	public void throwIdentifierException()
		throws FileIdentifierException
		{
		throw new FileIdentifierException(
			"TEST FAULT",
			"TEST IDENT"
			) ;
		}

	}

