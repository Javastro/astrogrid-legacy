/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/FileStoreMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/08/27 22:43:15 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreMock.java,v $
 *   Revision 1.7  2004/08/27 22:43:15  dave
 *   Updated filestore and myspace to report file size correctly.
 *
 *   Revision 1.6.12.2  2004/08/26 19:41:47  dave
 *   Updated tests to check import URL size.
 *
 *   Revision 1.6.12.1  2004/08/26 19:06:50  dave
 *   Modified filestore to return file size in properties.
 *
 *   Revision 1.6  2004/08/18 19:00:01  dave
 *   Myspace manager modified to use remote filestore.
 *   Tested before checkin - integration tests at 91%.
 *
 *   Revision 1.5.10.3  2004/08/09 10:16:28  dave
 *   Added resource URL to the properties.
 *
 *   Revision 1.5.10.2  2004/08/06 22:25:06  dave
 *   Refactored bits and broke a few tests ...
 *
 *   Revision 1.5.10.1  2004/08/02 14:54:15  dave
 *   Trying to get integration tests to run
 *
 *   Revision 1.5  2004/07/23 09:11:16  dave
 *   Merged development branch, dave-dev-200407221513, into HEAD
 *
 *   Revision 1.4.6.1  2004/07/23 02:10:58  dave
 *   Added IvornFactory and IvornParser
 *
 *   Revision 1.4  2004/07/21 18:11:55  dave
 *   Merged development branch, dave-dev-200407201059, into HEAD
 *
 *   Revision 1.3.2.2  2004/07/21 16:28:16  dave
 *   Added content properties and tests
 *
 *   Revision 1.3.2.1  2004/07/20 19:10:40  dave
 *   Refactored to implement URL import
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

import java.io.IOException  ;
import java.io.InputStream  ;
import java.io.OutputStream ;
import java.io.ByteArrayOutputStream ;

import java.net.URL ;
import java.net.URLConnection ;
import java.net.MalformedURLException ;

import java.util.Map ;
import java.util.HashMap ;

import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.file.FileProperties ;
import org.astrogrid.filestore.common.file.FileIdentifier ;

import org.astrogrid.filestore.common.ivorn.FileStoreIvornFactory ;

import org.astrogrid.filestore.common.transfer.TransferUtil ;
import org.astrogrid.filestore.common.transfer.TransferProperties ;

import org.astrogrid.filestore.common.exception.FileStoreException ;
import org.astrogrid.filestore.common.exception.FileStoreNotFoundException ;
import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;
import org.astrogrid.filestore.common.exception.FileStoreTransferException ;

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
	 * Ivorn for the mock service.
	 *
	 */
	public static final String MOCK_SERVICE_IVORN = "ivo://org.astrogrid.mock/filestore" ;

	/**
	 * URL for the mock service.
	 *
	 */
	public static final String MOCK_SERVICE_URL   = "http://mock.astrogrid.org/filestore" ;

	/**
	 * Ident for the mock service.
	 *
	 */
	public static final String MOCK_SERVICE_IDENT = "org.astrogrid.mock/filestore" ;

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
			// Update our properties.
			this.update() ;
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
			//
			// Update our properties.
			this.update() ;
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
		 * Access to the data size.
		 *
		 */
		public int getSize()
			{
			if (null != this.data)
				{
				return this.data.length ;
				}
			else {
				return 0 ;
				}
			}

		/**
		 * Import an array of bytes.
		 *
		 */
		public void importBytes(byte[] bytes)
			{
			//
			// Make a copy of the data.
			this.data = (byte[]) bytes.clone() ;
			//
			// Update our properties.
			this.update() ;
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
			//
			// Update our properties.
			this.update() ;
			}

		/**
		 * Import our data from a URL.
		 *
		 */
		public void importData(URL url)
			throws IOException
			{
			//
			// Open a connection to the URL.
			URLConnection connection = url.openConnection() ;
			//
			// Set our propeties from the URL headers.
			this.properties.setProperty(
				FileProperties.TRANSFER_SOURCE_URL,
				url.toString()
				) ;
			this.properties.setProperty(
				FileProperties.MIME_TYPE_PROPERTY,
				connection.getContentType()
				) ;
			this.properties.setProperty(
				FileProperties.MIME_ENCODING_PROPERTY,
				connection.getContentEncoding()
				) ;
			//
			// Transfer the data from the URL.
			this.importData(
				connection.getInputStream()
				) ;
			}

		/**
		 * Import our data from am InputStream.
		 *
		 */
		public void importData(InputStream in)
			throws IOException
			{
			//
			// Create an output stream to transfer the data to.
			ByteArrayOutputStream out = new ByteArrayOutputStream() ;
			//
			// Transfer the data.
			TransferUtil trans = new TransferUtil(in, out) ;
			trans.transfer() ;
			//
			// Convert into a byte array.
			this.data = out.toByteArray() ;
			//
			// Update our properties.
			this.update() ;
			}

		/**
		 * Update the container properties.
		 *
		 */
		public void update()
			{
			//
			// Update the service ivorn.
			this.properties.setProperty(
				FileProperties.STORE_SERVICE_IVORN,
				MOCK_SERVICE_IVORN
				) ;
			//
			// Update the resource ident.
			this.properties.setProperty(
				FileProperties.STORE_RESOURCE_IDENT,
				this.ident()
				) ;
			//
			// Update the resource size.
			this.properties.setProperty(
				FileProperties.CONTENT_SIZE_PROPERTY,
				String.valueOf(
					this.getSize()
					)
				) ;
			//
			// Update the resource ivorn.
			try {
				this.properties.setProperty(
					FileProperties.STORE_RESOURCE_IVORN,
					FileStoreIvornFactory.createIdent(
						MOCK_SERVICE_IDENT,
						this.ident()
						)
					) ;
				}
			catch (FileStoreIdentifierException ouch)
				{
				this.properties.setProperty(
					FileProperties.STORE_RESOURCE_IVORN,
					null
					) ;
				}
			//
			// Update the service URL.
			try {
				this.properties.setProperty(
					FileProperties.STORE_RESOURCE_URL,
					new URL(
						MOCK_SERVICE_URL + "/" + this.ident()
						).toString()
					) ;
				}
			catch (MalformedURLException ouch)
				{
				this.properties.setProperty(
					FileProperties.STORE_RESOURCE_URL,
					null
					) ;
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
		return MOCK_SERVICE_IVORN ;
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
	 * @throws FileStoreIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreException if the string is null.
	 *
	 */
	public FileProperty[] appendString(String ident, String data)
		throws FileStoreIdentifierException, FileStoreNotFoundException, FileStoreException
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
	 * @throws FileStoreIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreException if the data array is null.
	 *
	 */
	public FileProperty[] appendBytes(String ident, byte[] data)
		throws FileStoreIdentifierException, FileStoreNotFoundException, FileStoreException
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
			throw new FileStoreIdentifierException() ;
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
	 * @throws FileStoreIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 *
	 */
	public String exportString(String ident)
		throws FileStoreIdentifierException, FileStoreNotFoundException
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
	 * @throws FileStoreIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 *
	 */
	public byte[] exportBytes(String ident)
		throws FileStoreIdentifierException, FileStoreNotFoundException
		{
		//
		// Check for null ident.
		if (null == ident)
			{
			throw new FileStoreIdentifierException() ;
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
	 * @throws FileStoreIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 *
	 */
	public FileProperty[] duplicate(String ident, FileProperty[] properties)
		throws FileStoreIdentifierException, FileStoreNotFoundException
		{
		//
		// Check for null ident.
		if (null == ident)
			{
			throw new FileStoreIdentifierException() ;
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
	 * @throws FileStoreIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 *
	 */
	public FileProperty[] delete(String ident)
		throws FileStoreIdentifierException, FileStoreNotFoundException
		{
		//
		// Check for null ident.
		if (null == ident)
			{
			throw new FileStoreIdentifierException() ;
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
	 * @throws FileStoreIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
     * @throws RemoteException If the WebService call fails.
	 *
	 */
	public FileProperty[] properties(String ident)
		throws FileStoreIdentifierException, FileStoreNotFoundException
		{
		//
		// Check for null ident.
		if (null == ident)
			{
			throw new FileStoreIdentifierException() ;
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
	 * @param transfer A TransferProperties object describing the transfer.
	 * @return A new TransferProperties describing the transfer.
	 *
	 */
	public TransferProperties importInit(TransferProperties transfer)
		{
		return transfer ;
		}

	/**
	 * Import a data object from a remote source.
	 * @param transfer A TransferProperties object describing the transfer.
	 * @return A new TransferProperties describing the transfer.
	 * @throws FileStoreTransferException if the transfer properties are null.
	 *
	 */
	public TransferProperties importData(TransferProperties transfer)
		throws FileStoreTransferException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreMock.importData()") ;
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
		ContainerMock container = new ContainerMock(
			transfer.getFileProperties()
			) ;
		//
		// Transfer the data.
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
		catch (IOException ouch)
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
	 * @param transfer A TransferProperties object describing the transfer.
	 * @return A new TransferProperties describing the transfer.
	 *
	 */
	public TransferProperties exportInit(TransferProperties transfer)
		{
		return transfer ;
		}

	/**
	 * Export a data object to a remote destination.
	 * @param transfer A TransferProperties object describing the transfer.
	 * @return A new TransferProperties describing the transfer.
	 *
	 */
	public TransferProperties exportData(TransferProperties transfer)
		{
		return transfer ;
		}

	/**
	 * Throw a FileStoreIdentifierException, useful for debugging the transfer of Exceptions via SOAP.
	 * @throws FileStoreIdentifierException as requested.
	 *
	 */
	public void throwIdentifierException()
		throws FileStoreIdentifierException
		{
		throw new FileStoreIdentifierException(
			"TEST FAULT",
			"TEST IDENT"
			) ;
		}

	}

