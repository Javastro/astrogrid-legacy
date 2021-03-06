/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/FileStoreMock.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 08:20:56 $</cvs:date>
 * <cvs:version>$Revision: 1.13 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreMock.java,v $
 *   Revision 1.13  2005/03/11 08:20:56  clq2
 *   filestore-nww-965
 *
 *   Revision 1.12.8.1  2005/03/01 10:58:31  nw
 *   changed prefix from 'mock://' to 'mock:'
 *   the original prefix wouldn't go through the filemanager tests
 *   (as the URI class complains that the key isn't a valid hostname).
 *   removing the // means its just a URI, rather than a standard-form URI,
 *   which is implied to be locatable.
 *
 *   Revision 1.12  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.11.6.2  2005/01/15 03:46:50  dave
 *   Added initial tests for create and modified date(s) ..
 *
 *   Revision 1.11.6.1  2005/01/14 21:01:33  dave
 *   Added FileStoreDateFormat utility to handle dates in ISO format ....
 *   Fixed the file:/// problem on Unix ...
 *
 *   Revision 1.11  2004/12/16 17:25:49  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.9.14.11  2004/12/11 06:00:12  dave
 *   Updates to support FileManager copy ....
 *
 *   Revision 1.9.14.10  2004/12/08 17:54:55  dave
 *   Added update to FileManager client and server side ...
 *
 *   Revision 1.9.14.9  2004/12/08 01:58:51  dave
 *   Adde filestore location to filemanager move ...
 *
 *   Revision 1.9.14.8  2004/11/06 19:12:18  dave
 *   Refactored identifier properties ...
 *
 *   Revision 1.9.14.7  2004/11/02 23:20:12  dave
 *   Added property filter and changed method names to make them FileStore specific.
 *
 *   Revision 1.9.14.6  2004/10/29 15:54:50  dave
 *   Added exportInit to mock implementation ...
 *   Added UrlGetRequest to pass into exportInit ...
 *   Added test for exportInit and UrlGetRequest ...
 *
 *   Revision 1.9.14.5  2004/10/29 13:21:58  dave
 *   Added InputStream wrapper ...
 *
 *   Revision 1.9.14.4  2004/10/26 11:13:11  dave
 *   Changed transfer properties 'source' to 'location', makes more sense for PUT transfers.
 *
 *   Revision 1.9.14.3  2004/10/21 21:00:13  dave
 *   Added mock://xyz URL handler to enable testing of transfer.
 *   Implemented importInit to the mock service and created transfer tests.
 *
 *   Revision 1.9.14.2  2004/10/19 14:56:15  dave
 *   Refactored config and resolver to enable multiple instances of mock implementation.
 *   Required to implement handling of multiple FileStore(s) in FileManager.
 *
 *   Revision 1.9.14.1  2004/10/15 03:53:04  dave
 *   Changed WSDD deployment to 'Application' to allow multiple mock instances.
 *
 *   Revision 1.9  2004/09/17 06:57:10  dave
 *   Added commons logging to FileStore.
 *   Updated logging properties in Community.
 *   Fixed bug in AGINAB deployment.
 *   Removed MySpace tests with hard coded grendel address.
 *
 *   Revision 1.8.16.1  2004/09/17 01:08:36  dave
 *   Updated debug to use commons logging API ....
 *
 *   Revision 1.8  2004/09/02 10:25:41  dave
 *   Updated FileStore and MySpace to handle mime type and file size.
 *   Updated Community deployment script.
 *
 *   Revision 1.7.2.1  2004/09/01 02:58:07  dave
 *   Updated to use external mime type for imported files.
 *
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

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.io.IOException  ;
import java.io.InputStream  ;
import java.io.OutputStream ;
import java.io.ByteArrayInputStream ;
import java.io.ByteArrayOutputStream ;

import java.net.URL ;
import java.net.URLConnection ;
import java.net.MalformedURLException ;

import java.util.Map ;
import java.util.HashMap ;
import java.util.Date ;

import org.astrogrid.filestore.common.file.FileIdentifier ;
import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.file.FileProperties ;
import org.astrogrid.filestore.common.file.FileStorePropertyFilter ;

import org.astrogrid.filestore.common.ivorn.FileStoreIvornParser ;

import org.astrogrid.filestore.common.transfer.TransferUtil ;
import org.astrogrid.filestore.common.transfer.UrlPutTransfer ;
import org.astrogrid.filestore.common.transfer.UrlGetTransfer ;
import org.astrogrid.filestore.common.transfer.TransferProperties ;

import org.astrogrid.filestore.common.exception.FileStoreException ;
import org.astrogrid.filestore.common.exception.FileStoreServiceException ;
import org.astrogrid.filestore.common.exception.FileStoreNotFoundException ;
import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;
import org.astrogrid.filestore.common.exception.FileStoreTransferException ;

import org.astrogrid.filestore.common.transfer.mock.Handler ;
import org.astrogrid.filestore.common.transfer.mock.Connector ;

/**
 * A mock implementation of the store service.
 *
 */
public class FileStoreMock
	implements FileStore
	{
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FileStoreMock.class);

	/**
	 * Our configuration properties.
	 *
	 */
	private FileStoreConfig config ;

	/**
	 * Public constructor, using default values.
	 *
	 */
	public FileStoreMock()
		{
		this(
			new FileStoreConfigMock()
			);
		}

	/**
	 * Public constructor, with a specific config.
	 *
	 */
	public FileStoreMock(FileStoreConfig config)
		{
		this.config = config ;
		}

	/**
	 * Internal Map of data objects.
	 *
	 */
	protected Map map = new HashMap() ;

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
		 * @throws FileStoreServiceException If unable to handle the request.
		 *
		 */
		public ContainerMock(FileProperty[] props)
			throws FileStoreServiceException
			{
			this(
				new FileProperties(
					props
					)
				);
			}

		/**
		 * Public constructor.
		 * @param props The file properties.
		 * @throws FileStoreServiceException If unable to handle the request.
		 *
		 */
		private ContainerMock(FileProperties props)
			throws FileStoreServiceException
			{
			log.debug("") ;
			log.debug("FileStoreMock.ContainerMock(FileProperties)") ;
			//
			// Assign a new identifier.
			this.ident = new FileIdentifier() ;
			log.debug("  Ident : '" + ident + "'") ;
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
		 * @throws FileStoreServiceException If unable to handle the request.
		 *
		 */
		public ContainerMock(ContainerMock that, FileProperties props)
			throws FileStoreServiceException
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
		 * The file create data.
		 *
		 */
		private Date created = new Date() ;

		/**
		 * The file modified data.
		 *
		 */
		private Date modified = new Date() ;

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
		 * Our internal byte array of data.
		 *
		 */
		protected byte[] data ;

		/**
		 * Access to the data size.
		 *
		 */
		public long getSize()
			{
			log.debug("") ;
			log.debug("FileStoreMock.ContainerMock.getSize()") ;
			long size = 0L ;
			if (null != this.data)
				{
				size = this.data.length ;
				}
			log.debug("  Size : " + String.valueOf(size)) ;
			return size ;
			}

		/**
		 * Import an array of bytes.
		 * @throws FileStoreServiceException If unable to handle the request.
		 *
		 */
		public void importBytes(byte[] bytes)
			throws FileStoreServiceException
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
		 * @throws FileStoreServiceException If unable to handle the request.
		 *
		 */
		public void appendBytes(byte[] extra)
			throws FileStoreServiceException
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
		 * @throws IOException If unable to import the data.
		 * @throws FileStoreServiceException If unable to handle the request.
		 *
		 */
		public void importData(URL url)
			throws FileStoreServiceException, IOException
			{
			log.debug("") ;
			log.debug("FileStoreMock.ContainerMock.importData(URL)") ;
			log.debug("  URL : " + url.toString()) ;
			//
			// Open a connection to the URL.
			URLConnection connection = url.openConnection() ;
			//
			// Set the source URL property.
			this.properties.setProperty(
				FileProperties.TRANSFER_SOURCE_URL,
				url.toString()
				) ;
			//
			// If the content type type hasn't been set yet.
			// Set it from the URL connection.
			if (null == this.properties.getProperty(
				FileProperties.MIME_TYPE_PROPERTY)
				)
				{
				this.properties.setProperty(
					FileProperties.MIME_TYPE_PROPERTY,
					connection.getContentType()
					) ;
				}
			//
			// If the content encoding hasn't been set yet.
			// Set it from the URL connection.
			if (null == this.properties.getProperty(
				FileProperties.MIME_ENCODING_PROPERTY)
				)
				{
				this.properties.setProperty(
					FileProperties.MIME_ENCODING_PROPERTY,
					connection.getContentEncoding()
					) ;
				}
			//
			// Transfer the data from the URL.
			this.importData(
				connection.getInputStream()
				) ;
			}

		/**
		 * Import our data from an InputStream.
		 * @throws IOException If unable to import the data.
		 * @throws FileStoreServiceException If unable to handle the request.
		 *
		 */
		public void importData(InputStream in)
			throws FileStoreServiceException, IOException
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
		 * @throws FileStoreServiceException If unable to handle the request.
		 *
		 */
		public void update()
			throws FileStoreServiceException
			{
			log.debug("") ;
			log.debug("FileStoreMock.ContainerMock.update()") ;
			//
			// Update the resource ivorn.
			this.properties.setStoreResourceIvorn(
				config.getResourceIvorn(
					this.ident()
					)
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
			// ** Update the service URL - deprecated **
			this.properties.setProperty(
				FileProperties.STORE_RESOURCE_URL,
				config.getResourceUrl(
					this.ident()
					).toString()
				) ;
			//
			// Create our date formatter.
			FileStoreDateFormat formatter = new FileStoreDateFormat();
			//
			// Set the create date.
			this.properties.setProperty(
				FileProperties.FILE_CREATED_DATE_PROPERTY,
				formatter.format(
					created
					)
				) ;
			//
			// Update the modified date.
			modified = new Date() ;
			//
			// Set the modified date.
			this.properties.setProperty(
				FileProperties.FILE_MODIFIED_DATE_PROPERTY,
				formatter.format(
					modified
					)
				) ;
			}

		/**
		 * Create a mock URL for this container.
		 * @todo The Connector should contain the URL.
		 *
		 */
		public URL getMockUrl()
			{
			log.debug("") ;
			log.debug("FileStoreMock.ContainerMock.getMockUrl()") ;
			log.debug("  Ident : " + this.ident()) ;
			URL url = null ;
			try {
				//
				// Initialise our mock URL handler.
				Handler.register() ;
				//
				// Create a new mock URL for this container.
				url = new URL(
					"mock:" + this.ident()
					) ;
				log.debug("  URL   : " + url.toString()) ;
				}
			catch (Exception ouch)
				{
				log.warn("Exception while creating URL") ;
				log.warn("  Exception : " + ouch) ;
				}
			return url ;
			}

		/**
		 * Create a connector for our mock URL.
		 * @todo The Connector should contain the URL.
		 * @todo The Connector should handle appends.
		 *
		 */
		public Connector getMockConnector()
			{
			log.debug("") ;
			log.debug("FileStoreMock.ContainerMock.getMockConnector()") ;
			log.debug("  Ident : " + this.ident()) ;
			//
			// Create our connector.
			Connector connector = new Connector()
				{
				public InputStream getInputStream()
					{
					log.debug("") ;
					log.debug("FileStoreMock.ContainerMock.Connector.getInputStream()") ;
					return new ByteArrayInputStream(
						data
						);
					}
				public OutputStream getOutputStream()
					{
					log.debug("") ;
					log.debug("FileStoreMock.ContainerMock.Connector.getOutputStream()") ;
					return new ByteArrayOutputStream()
						{
						public void close()
							throws IOException
							{
							log.debug("") ;
							log.debug("FileStoreMock.ContainerMock.Connector.OutputStream.close()") ;
							super.close() ;
							data = this.toByteArray() ;
							}
						} ;
					}
				} ;
			//
			// Register our connector.
			Handler.addConnector(
				this.getMockUrl(),
				connector
				);
			//
			// Return our new connector.
			return connector ;
			}
		}

	/**
	 * Get the service identifier - used for testing.
	 * @return The ivo identifier for this service.
	 * @throws FileStoreServiceException
	 *
	 */
	public String identifier()
		throws FileStoreServiceException
		{
		return config.getServiceIvorn().toString() ;
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
		log.debug("") ;
		log.debug("----\"----") ;
		log.debug("FileStoreMock.importString()") ;
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
	 * @throws FileStoreServiceException If unable to handle the request.
	 *
	 */
	public FileProperty[] importBytes(FileProperty[] properties, byte[] data)
		throws FileStoreServiceException, FileStoreException
		{
		log.debug("") ;
		log.debug("----\"----") ;
		log.debug("FileStoreMock.importBytes()") ;
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
	 * @throws FileStoreServiceException If unable to handle the request.
	 *
	 */
	public FileProperty[] appendString(String ident, String data)
		throws FileStoreServiceException, FileStoreIdentifierException, FileStoreNotFoundException, FileStoreException
		{
		log.debug("") ;
		log.debug("----\"----") ;
		log.debug("FileStoreMock.appendString()") ;
		log.debug("  Ident : " + ident) ;
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
	 * @throws FileStoreServiceException If unable to handle the request.
	 *
	 */
	public FileProperty[] appendBytes(String ident, byte[] data)
		throws FileStoreServiceException, FileStoreIdentifierException, FileStoreNotFoundException, FileStoreException
		{
		log.debug("") ;
		log.debug("----\"----") ;
		log.debug("FileStoreMock.appendBytes()") ;
		log.debug("  Ident : " + ident) ;
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
	 * @throws FileStoreServiceException If unable to handle the request.
	 *
	 */
	public String exportString(String ident)
		throws FileStoreServiceException, FileStoreIdentifierException, FileStoreNotFoundException
		{
		log.debug("") ;
		log.debug("----\"----") ;
		log.debug("FileStoreMock.exportString()") ;
		log.debug("  Ident : " + ident) ;
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
	 * @throws FileStoreServiceException If unable to handle the request.
	 *
	 */
	public byte[] exportBytes(String ident)
		throws FileStoreServiceException, FileStoreIdentifierException, FileStoreNotFoundException
		{
		log.debug("") ;
		log.debug("----\"----") ;
		log.debug("FileStoreMock.exportBytes()") ;
		log.debug("  Ident : " + ident) ;
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
	 * @throws FileStoreServiceException If unable to handle the request.
	 *
	 */
	public FileProperty[] duplicate(String ident, FileProperty[] properties)
		throws FileStoreServiceException, FileStoreIdentifierException, FileStoreNotFoundException
		{
		log.debug("") ;
		log.debug("----\"----") ;
		log.debug("FileStoreMock.duplicate()") ;
		log.debug("  Ident : " + ident) ;
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
			merged.merge(
				properties,
				new FileStorePropertyFilter()
				) ;
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
	 * @throws FileStoreServiceException If unable to handle the request.
	 *
	 */
	public FileProperty[] delete(String ident)
		throws FileStoreServiceException, FileStoreIdentifierException, FileStoreNotFoundException
		{
		log.debug("") ;
		log.debug("----\"----") ;
		log.debug("FileStoreMock.delete()") ;
		log.debug("  Ident : " + ident) ;
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
	 * @throws FileStoreServiceException If unable to handle the request.
	 *
	 */
	public FileProperty[] properties(String ident)
		throws FileStoreServiceException, FileStoreIdentifierException, FileStoreNotFoundException
		{
		log.debug("") ;
		log.debug("----\"----") ;
		log.debug("FileStoreMock.properties()") ;
		log.debug("  Ident : " + ident) ;
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
			// Update the properties.
			container.update();
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
	 * @throws FileStoreServiceException If unable to handle the request.
	 * @throws FileStoreTransferException If the input transfer properties are not valid.
	 *
	 */
	public TransferProperties importInit(TransferProperties transfer)
		throws FileStoreServiceException, FileStoreTransferException
		{
		log.debug("") ;
		log.debug("----\"----") ;
		log.debug("FileStoreMock.importInit()") ;
		//
		// Check for null transfer properties.
		if (null == transfer)
			{
			throw new FileStoreTransferException(
				"Null transfer properties"
				) ;
			}
		//
		// Create a new container.
		ContainerMock container = new ContainerMock(
			transfer.getFileProperties()
			) ;
		//
		// Get a connector for the container.
		Connector connector = container.getMockConnector() ;
		//
		// Get the container URL.
		URL url = container.getMockUrl() ;
		//
		// Set the transfer URL.
		transfer.setLocation(url.toString()) ;
		transfer.setProtocol("http") ;
		transfer.setMethod("PUT") ;
		//
		// Update the transfer properties.
		transfer.setFileProperties(
			container.properties().toArray()
			);
		//
		// Return the new transfer properties.
		return transfer ;
		}

	/**
	 * Import a data object from a remote source.
	 * @param transfer A TransferProperties object describing the transfer.
	 * @return A new TransferProperties describing the transfer.
	 * @throws FileStoreTransferException if the transfer properties are null.
	 * @throws FileStoreServiceException If unable to handle the request.
	 *
	 */
	public TransferProperties importData(TransferProperties transfer)
		throws FileStoreServiceException, FileStoreTransferException
		{
		log.debug("") ;
		log.debug("----\"----") ;
		log.debug("FileStoreMock.importData()") ;
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
		if (null == transfer.getLocation())
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
					transfer.getLocation()
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
	 * @throws FileStoreTransferException if the transfer properties are null.
	 * @throws FileStoreNotFoundException If unable to locate the target object.
	 * @throws FileStoreServiceException If unable to handle the request.
	 *
	 */
	public TransferProperties exportInit(TransferProperties transfer)
		throws FileStoreServiceException, FileStoreNotFoundException, FileStoreTransferException
		{
		log.debug("") ;
		log.debug("----\"----") ;
		log.debug("FileStoreMock.exportInit()") ;
		//
		// Check for null transfer properties.
		if (null == transfer)
			{
			throw new FileStoreTransferException(
				"Null transfer properties"
				) ;
			}
		//
		// Get the transfer file properties.
		FileProperties properties = new FileProperties(
			transfer.getFileProperties()
			) ;
		//
		// Get the file identifier.
		String ident = null ;
		try {
			ident = properties.getStoreResourceIdent();
			}
		catch (FileStoreIdentifierException ouch)
			{
			throw new FileStoreNotFoundException(
				"Unable to parse resource identifier"
				) ;
			}
		log.debug("  Ident : " + ident) ;
		//
		// Try to locate our container.
		ContainerMock container = (ContainerMock) map.get(ident) ;
		//
		// If we found a matching container.
		if (null != container)
			{
			//
			// Register our mock URL connector.
			container.getMockConnector() ;
			//
			// Create a new transfer properties.
			transfer = new UrlGetTransfer(
				container.getMockUrl(),
				container.properties()
				) ;
			//
			// Return the new transfer properties.
			return transfer ;
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

