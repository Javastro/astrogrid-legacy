/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/FileStoreMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/19 23:42:07 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreMock.java,v $
 *   Revision 1.3  2004/07/19 23:42:07  dave
 *   Merged development branch, dave-dev-200407151443, into HEAD
 *
 *   Revision 1.2.4.1  2004/07/19 19:40:28  dave
 *   Debugged and worked around Axis Exception handling
 *
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.1.2.7  2004/07/13 16:37:29  dave
 *   Refactored common and client to use an array of FileProperties (more SOAP friendly)
 *
 *   Revision 1.1.2.6  2004/07/12 14:39:03  dave
 *   Added server repository classes
 *
 *   Revision 1.1.2.5  2004/07/07 14:54:35  dave
 *   Changed DataInfo to File Info (leaves room to use DataInfo for the more abstract VoStore interface).
 *
 *   Revision 1.1.2.4  2004/07/07 14:32:43  dave
 *   Changed DataIdentifier to FileIdentifier
 *
 *   Revision 1.1.2.3  2004/07/07 14:24:14  dave
 *   Changed internal class DataConrainer to FileContainer
 *
 *   Revision 1.1.2.2  2004/07/07 11:55:54  dave
 *   Fixed byte array compare in tests
 *
 *   Revision 1.1.2.1  2004/07/05 04:50:29  dave
 *   Created initial FileStore components
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common ;

import java.util.Map ;
import java.util.HashMap ;

//import java.rmi.RemoteException  ;
//import org.apache.axis.AxisFault ;

import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.file.FileProperties ;
import org.astrogrid.filestore.common.file.FileIdentifier ;
import org.astrogrid.filestore.common.transfer.TransferInfo ;
import org.astrogrid.filestore.common.exception.FileStoreException ;
import org.astrogrid.filestore.common.exception.FileStoreNotFoundException ;
import org.astrogrid.filestore.common.exception.FileIdentifierException ;

/**
 * A mock implementation of the store service.
 *
 */
public class FileStoreMock
	implements FileStore
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	protected static final boolean DEBUG_FLAG = true ;

	/**
	 * Ivorn identifier for the mock service.
	 *
	 */
	public static final String MOCK_SERVICE_IDENT = "ivo://org.astrogrid.mock/filestore" ;

	/**
	 * Internal Map of data objects.
	 *
	 */
	protected static Map map = new HashMap() ;

	/**
	 * Inner class to contain data.
	 * Although the server will probably have something similar, this is internal to to
	 * the implementation, and so I havn't added it to the common classes.
	 *
	 */
	protected class ContainerMock
		{
		/**
		 * Public constructor.
		 *
		 */
		public ContainerMock(FileProperty[] props)
			{
			this(
				new FileProperties(
					props
					)
				);
			}

		/**
		 * Public constructor.
		 *
		 */
		private ContainerMock(FileProperties props)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("----\"----") ;
			if (DEBUG_FLAG) System.out.println("ContainerMock()") ;
			//
			// Assign a new identifier.
			this.ident = new FileIdentifier() ;
			if (DEBUG_FLAG) System.out.println("  Ident : '" + ident + "'") ;
			//
			// Register this container.
			map.put(
				this.ident(),
				this
				) ;
			//
			// Create our properties.
			this.properties = new FileProperties(props) ;
			//
			// Update our internal properties.
			this.properties.setProperty(
				FileProperties.STORE_SERVICE_IDENTIFIER,
				MOCK_SERVICE_IDENT
				) ;
			this.properties.setProperty(
				FileProperties.STORE_INTERNAL_IDENTIFIER,
				this.ident()
				) ;
			}

		/**
		 * Public copy constructor.
		 *
		 */
		public ContainerMock(ContainerMock that, FileProperties props)
			{
			//
			// Initialise our container.
			this(props) ;
			//
			// Make a copy of the data.
			this.data = (byte[]) that.data.clone() ;
			}

		/**
		 * Internal identifier.
		 *
		 */
		private FileIdentifier ident ;

		/**
		 * Access to our identifier.
		 *
		 */
		public String ident()
			{
			return this.ident.toString() ;
			}

		/**
		 * Internal data information.
		 *
		 */
		private FileProperties properties ;

		/**
		 * Access to our properties.
		 *
		 */
		public FileProperties properties()
			{
			return this.properties ;
			}

		/**
		 * Internal byte array of data.
		 *
		 */
		protected byte[] data ;

		/**
		 * Import an array of bytes.
		 *
		 */
		public void importBytes(byte[] bytes)
			{
			this.data = bytes ;
			}

		/**
		 * Export our contents as bytes.
		 *
		 */
		public byte[] exportBytes()
			{
			return this.data ;
			}

		/**
		 * Append an array of bytes.
		 *
		 */
		public void appendBytes(byte[] extra)
			{
			//
			// Keep a reference to our original bytes.
			byte[] prev = this.data ;
			//
			// Create a new byte array big enough for both.
			this.data = new byte[prev.length + extra.length] ;
			//
			// Transfer our original bytes.
			int i = 0 ;
			for ( ; i < prev.length ; i++)
				{
				this.data[i] = prev[i] ;
				}
			//
			// Transfer our extra bytes.
			for (int j = 0 ; j < extra.length ; i++, j++)
				{
				this.data[i] = extra[j] ;
				}
			}
		}

	/**
	 * Get the service identifier - used for testing.
	 * @return The ivo identifier for this service.
	 *
	 */
	public String identifier()
		{
		return MOCK_SERVICE_IDENT ;
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
		throws FileStoreException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreMock.importString()") ;
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
	 *
	 */
	public FileProperty[] importBytes(FileProperty[] properties, byte[] data)
		throws FileStoreException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreMock.importBytes()") ;
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
		ContainerMock container = new ContainerMock(
			properties
			) ;
		//
		// Store the data in our container.
		container.importBytes(
			data
			) ;
		//
		// Return the container properties.
		return container.properties().toArray() ;
		}

	/**
	 * Append a string to existing data.
	 * @param ident The internal identifier of the target.
	 * @param data The string to append.
	 * @return An array of FileProperties describing the imported data.
	 * @throws FileIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreException if the string is null.
	 *
	 */
	public FileProperty[] appendString(String ident, String data)
		throws FileIdentifierException, FileStoreNotFoundException, FileStoreException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreMock.appendString()") ;
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
	 * @throws FileStoreException if the data array is null.
	 *
	 */
	public FileProperty[] appendBytes(String ident, byte[] data)
		throws FileIdentifierException, FileStoreNotFoundException, FileStoreException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreMock.appendString()") ;
		//
		// Check for null data.
		if (null == data)
			{
			throw new FileStoreException(
				"Null data"
				) ;
			}
		//
		// Check for null ident.
		if (null == ident)
			{
			throw new FileIdentifierException() ;
			}
		//
		// Try to locate a matching container.
		ContainerMock container = (ContainerMock) map.get(ident) ;
		//
		// If we found a matching container.
		if (null != container)
			{
			//
			// Append the data to our container.
			container.appendBytes(
				data
				) ;
			//
			// Return the container properties.
			return container.properties().toArray() ;
			}
		//
		// If we didn't find a container.
		else {
			throw new FileStoreNotFoundException(
				ident
				) ;
			}
		}

	/**
	 * Export (read) the contents of a file as a string.
	 * @param ident The internal identifier of the target file.
	 * @return The contents of a data object as a string.
	 * @throws FileIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 *
	 */
	public String exportString(String ident)
		throws FileIdentifierException, FileStoreNotFoundException
		{
		return new String(
			this.exportBytes(
				ident
				)
			) ;
		}

	/**
	 * Export (read) the contents of a file as a string.
	 * @param ident The internal identifier of the target file.
	 * @return The contents of a data object as a string.
	 * @throws FileIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 *
	 */
	public byte[] exportBytes(String ident)
		throws FileIdentifierException, FileStoreNotFoundException
		{
		//
		// Check for null ident.
		if (null == ident)
			{
			throw new FileIdentifierException() ;
			}
		//
		// Try to locate a matching container.
		ContainerMock container = (ContainerMock) map.get(ident) ;
		//
		// If we found a matching container.
		if (null != container)
			{
			//
			// Return the container contents.
			return container.exportBytes() ;
			}
		//
		// If we didn't find a container.
		else {
			throw new FileStoreNotFoundException(
				ident
				) ;
			}
		}

	/**
	 * Create a local duplicate (copy) of a file.
	 * @param ident The internal identifier of the target file.
	 * @param properties An optional array of FileProperties describing the copy.
	 * @return An array of FileProperties describing the copied data.
	 * @throws FileIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 *
	 */
	public FileProperty[] duplicate(String ident, FileProperty[] properties)
		throws FileIdentifierException, FileStoreNotFoundException
		{
		//
		// Check for null ident.
		if (null == ident)
			{
			throw new FileIdentifierException() ;
			}
		//
		// Try to locate a matching container.
		ContainerMock original = (ContainerMock) map.get(ident) ;
		//
		// If we found a matching container.
		if (null != original)
			{
			//
			// Merge the new info with the original.
			FileProperties merged = new FileProperties(
				original.properties()
				) ;
			merged.merge(properties) ;
			//
			// Create a new container.
			ContainerMock duplicate = new ContainerMock(
				original,
				merged
				) ;
			//
			// Return the container properties.
			return duplicate.properties().toArray() ;
			}
		//
		// If we didn't find a container.
		else {
			throw new FileStoreNotFoundException(
				ident
				) ;
			}
		}

	/**
	 * Delete a file.
	 * @param ident The internal identifier of the target file.
	 * @return An array of FileProperties describing the deleted data.
	 * @throws FileIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 *
	 */
	public FileProperty[] delete(String ident)
		throws FileIdentifierException, FileStoreNotFoundException
		{
		//
		// Check for null ident.
		if (null == ident)
			{
			throw new FileIdentifierException() ;
			}
		//
		// Try to locate a matching container.
		ContainerMock container = (ContainerMock) map.get(ident) ;
		//
		// If we found a matching container.
		if (null != container)
			{
			//
			// Remove the container from our map.
			map.remove(container.ident()) ;
			//
			// Return the container properties.
			return container.properties().toArray() ;
			}
		//
		// If we didn't find a container.
		else {
			throw new FileStoreNotFoundException(
				ident
				) ;
			}
		}

	/**
	 * Get the meta data information for a file.
	 * @param ident The internal identifier of the target file.
	 * @return An array of FileProperties describing the data.
	 * @throws FileIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
     * @throws RemoteException If the WebService call fails.
	 *
	 */
	public FileProperty[] properties(String ident)
		throws FileIdentifierException, FileStoreNotFoundException
		{
		//
		// Check for null ident.
		if (null == ident)
			{
			throw new FileIdentifierException() ;
			}
		//
		// Try to locate a matching container.
		ContainerMock container = (ContainerMock) map.get(ident) ;
		//
		// If we found a matching container.
		if (null != container)
			{
			//
			// Return the container properties.
			return container.properties().toArray() ;
			}
		//
		// If we didn't find a container.
		else {
			throw new FileStoreNotFoundException(
				ident
				) ;
			}
		}

	/**
	 * Prepare to receive a data object from a remote source.
	 * @param info A TransferInfo object describing the transfer.
	 * @return A new TransferInfo describing the transfer.
	 *
	 */
	public TransferInfo importInit(TransferInfo info)
		{
		return info ;
		}

	/**
	 * Import a data object from a remote source.
	 * @param info A TransferInfo object describing the transfer.
	 * @return A new TransferInfo describing the transfer.
	 *
	 */
	public TransferInfo importData(TransferInfo info)
		{
		return info ;
		}

	/**
	 * Prepare to send a data object to a remote destination.
	 * @param info A TransferInfo object describing the transfer.
	 * @return A new TransferInfo describing the transfer.
	 *
	 */
	public TransferInfo exportInit(TransferInfo info)
		{
		return info ;
		}

	/**
	 * Export a data object to a remote destination.
	 * @param info A TransferInfo object describing the transfer.
	 * @return A new TransferInfo describing the transfer.
	 *
	 */
	public TransferInfo exportData(TransferInfo info)
		{
		return info ;
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

