/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/server/src/java/org/astrogrid/filestore/server/repository/RepositoryImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/17 06:57:10 $</cvs:date>
 * <cvs:version>$Revision: 1.10 $</cvs:version>
 * <cvs:log>
 *   $Log: RepositoryImpl.java,v $
 *   Revision 1.10  2004/09/17 06:57:10  dave
 *   Added commons logging to FileStore.
 *   Updated logging properties in Community.
 *   Fixed bug in AGINAB deployment.
 *   Removed MySpace tests with hard coded grendel address.
 *
 *   Revision 1.9.2.1  2004/09/17 01:08:36  dave
 *   Updated debug to use commons logging API ....
 *
 *   Revision 1.9  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.8.14.3  2004/09/16 13:22:38  dave
 *   Fixed typo ..
 *
 *   Revision 1.8.14.2  2004/09/16 13:19:00  dave
 *   Refactored the stream close() into try catch finally blocks ....
 *
 *   Revision 1.8.14.1  2004/09/16 11:45:35  dave
 *   Added close() to output streams ....
 *
 *   Revision 1.8  2004/09/02 10:25:41  dave
 *   Updated FileStore and MySpace to handle mime type and file size.
 *   Updated Community deployment script.
 *
 *   Revision 1.7.2.1  2004/09/01 23:05:16  dave
 *   Updated filestore server to handle mime type correctly.
 *
 *   Revision 1.7  2004/08/27 22:43:15  dave
 *   Updated filestore and myspace to report file size correctly.
 *
 *   Revision 1.6.12.1  2004/08/26 19:06:50  dave
 *   Modified filestore to return file size in properties.
 *
 *   Revision 1.6  2004/08/18 19:00:01  dave
 *   Myspace manager modified to use remote filestore.
 *   Tested before checkin - integration tests at 91%.
 *
 *   Revision 1.5.10.4  2004/08/09 10:16:28  dave
 *   Added resource URL to the properties.
 *
 *   Revision 1.5.10.3  2004/08/06 22:25:06  dave
 *   Refactored bits and broke a few tests ...
 *
 *   Revision 1.5.10.2  2004/08/04 17:03:33  dave
 *   Added container to servlet
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

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URL ;
import java.net.URLConnection ;
import java.net.MalformedURLException ;

import java.io.File ;
import java.io.IOException ;
import java.io.InputStream ;
import java.io.OutputStream ;
import java.io.FileInputStream ;
import java.io.FileOutputStream ;
import java.io.FileNotFoundException ;

import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.file.FileProperties ;
import org.astrogrid.filestore.common.file.FileIdentifier ;

import org.astrogrid.filestore.common.ivorn.FileStoreIvornFactory ;

import org.astrogrid.filestore.common.transfer.TransferUtil ;
import org.astrogrid.filestore.common.exception.FileStoreException ;
import org.astrogrid.filestore.common.exception.FileStoreNotFoundException ;
import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;
import org.astrogrid.filestore.common.exception.FileStoreServiceException ;
import org.astrogrid.filestore.common.exception.FileStoreTransferException ;

/**
 * A factory class for creating and storing file containers.
 *
 */
public class RepositoryImpl
	implements Repository
	{
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(RepositoryImpl.class);

	/**
	 * The size of our copy buffer (8k bytes).
	 *
	 */
	private static final int COPY_BUFFER_SIZE = 8 * 1024 ;

	/**
	 * Public constructor.
	 *
	 */
	public RepositoryImpl(RepositoryConfig config)
		{
		if (null == config)
			{
			throw new IllegalArgumentException(
				"Null repository config"
				) ;
			}
		this.config = config ;
		}

	/**
	 * Reference to our repository config.
	 *
	 */
	protected RepositoryConfig config ;

	/**
	 * Factory method to create a new container.
	 * @param properties An optional array of FileProperty(ies) describing the file contents.
	 * @return A new file container.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public RepositoryContainer create(FileProperty[] properties)
		throws FileStoreServiceException
		{
		return create(
			new FileProperties(
				properties
				)
			) ;
		}

	/**
	 * Factory method to create a new container.
	 * @param properties An optional FileProperties describing the file contents.
	 * @return A new file container.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public RepositoryContainer create(FileProperties properties)
		throws FileStoreServiceException
		{
		//
		// Create the new container.
		return new RepositoryContainerImpl(
			properties
			) ;
		}

	/**
	 * Locate an existing container.
	 * @param ident The identifier of the container.
	 * @return The file container, if it exists.
	 * @throws FileStoreIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
	 * @todo Use FileIdentifier to ensure the ident is valid (which it don't yet).
	 *
	 */
	public RepositoryContainer load(String ident)
		throws FileStoreServiceException, FileStoreNotFoundException, FileStoreIdentifierException
		{
		//
		// Check for null ident.
		if (null == ident)
			{
			throw new FileStoreIdentifierException(
				FileStoreIdentifierException.NULL_IDENT_MESSAGE
				) ;
			}
		//
		// Return a container using the ident.
		return new RepositoryContainerImpl(
			ident
			) ;
		}

	/**
	 * Copy an existing container.
	 * @param ident The identifier of the container.
	 * @return The new file container.
	 * @throws FileStoreIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreTransferException If unable to transfer the data.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public RepositoryContainer duplicate(String ident)
		throws FileStoreServiceException, FileStoreNotFoundException, FileStoreIdentifierException, FileStoreTransferException
		{
		//
		// Try to load the existing container.
		RepositoryContainer original = new RepositoryContainerImpl(
			ident
			) ;
		//
		// Create the new container with the same properties.
		RepositoryContainer duplicate = new RepositoryContainerImpl(
			original.properties()
			) ;
		//
		// Transfer the data.
		duplicate.importData(
			original.getDataInputStream()
			) ;
		//
		// Return the new container.
		return duplicate ;
		}

	/**
	 * Inner class to implement a container.
	 *
	 */
	protected class RepositoryContainerImpl
		implements RepositoryContainer
		{
		/**
		 * The file identifier.
		 *
		 */
		private FileIdentifier ident ;

		/**
		 * Access to our identifier.
		 *
		 */
		public FileIdentifier ident()
			{
			return this.ident ;
			}

		/**
		 * The file properties.
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
		 * Constructor to create a new container.
		 * @param properties The file properties.
		 * @throws FileStoreServiceException If unable create the container.
		 *
		 */
		public RepositoryContainerImpl(FileProperties properties)
			throws FileStoreServiceException
			{
			//
			// Create a copy of the properties.
			this.properties = new FileProperties(
				properties
				) ;
			//
			// Create a new identifier.
			this.ident = new FileIdentifier() ;
			//
			// Update and save the properties.
			this.update() ;
			}

		/**
		 * Constructor to load an existing container.
		 * @param ident The identifier of the container.
		 * @throws FileStoreIdentifierException if the identifier is null or not valid.
		 * @throws FileStoreNotFoundException if unable to locate the file.
		 * @throws FileStoreServiceException if unable handle the request.
		 *
		 */
		public RepositoryContainerImpl(String ident)
			throws FileStoreServiceException, FileStoreNotFoundException, FileStoreIdentifierException
			{
			//
			// Create our identifier.
			this.ident = new FileIdentifier(ident) ;
			//
			// Create a our properties.
			this.properties = new FileProperties() ;
			//
			// Load the properties.
			this.load() ;
			}

		/**
		 * Generate the properties file path.
		 * @throws FileStoreServiceException if unable handle the request.
		 *
		 */
		public File getInfoFile()
			throws FileStoreServiceException
			{
			return new File(
				config.getInfoDirectory(),
				this.ident.toString() + ".inf"
				) ;
			}

		/**
		 * Generate the data file path.
		 * @throws FileStoreServiceException if unable handle the request.
		 *
		 */
		public File getDataFile()
			throws FileStoreServiceException
			{
			return new File(
				config.getDataDirectory(),
				this.ident.toString() + ".dat"
				) ;
			}

		/**
		 * Get the data file size.
		 *
		 */
		public long getDataFileSize()
			{
			try {
				return getDataFile().length() ;
				}
			catch (FileStoreServiceException ouch)
				{
				return 0 ;
				}
			}

		/**
		 * Update the properties.
		 *
		 */
		public void update()
			throws FileStoreServiceException
			{
			//
			// Create our properties.
			if (null == this.properties)
				{
				this.properties = new FileProperties() ;
				}
			//
			// Set the resource ident.
			this.properties.setProperty(
				FileProperties.STORE_RESOURCE_IDENT,
				ident.toString()
				) ;
			//
			// Set the resource size.
			this.properties.setProperty(
				FileProperties.CONTENT_SIZE_PROPERTY,
				String.valueOf(
					this.getDataFileSize()
					)
				) ;
			//
			// Try setting the config properties.
			try {
				//
				// Set the service ivorn.
				this.properties.setProperty(
					FileProperties.STORE_SERVICE_IVORN,
					config.getServiceIvorn().toString()
					) ;
				//
				// Set the resource ivorn.
				this.properties.setProperty(
					FileProperties.STORE_RESOURCE_IVORN,
					config.getResourceIvorn(
						ident.toString()
						).toString()
					) ;
				//
				// Set the resource URL.
				this.properties.setProperty(
					FileProperties.STORE_RESOURCE_URL,
					config.getResourceUrl(
						ident.toString()
						).toString()
					) ;
				}
			//
			// Save the properties.
			finally {
				this.save() ;
				}
			}

		/**
		 * Load the file properties.
		 * @throws FileStoreServiceException If unable to load the info.
		 * @throws FileStoreNotFoundException If unable to locate the file.
		 *
		 */
		protected void load()
			throws FileStoreServiceException, FileStoreNotFoundException
			{
			InputStream input = null ;
			try {
				input = new FileInputStream(
					this.getInfoFile()
					) ;
				this.properties.load(
					input
					) ;
				}
			catch (FileNotFoundException ouch)
				{
				throw new FileStoreNotFoundException(
					this.ident.toString()
					) ;
				}
			catch (IOException ouch)
				{
				throw new FileStoreServiceException(
					"Unable to load info file",
					ouch
					) ;
				}
			finally
				{
				if (null != input)
					{
					try {
						input.close() ;
						}
					catch (IOException ouch)
						{
						log.warn("----") ;
						log.warn("RepositoryContainerImpl.load") ;
						log.warn("Failed to close input stream") ;
						log.warn("----") ;
						}
					}
				}
			}

		/**
		 * Save the file properties.
		 * @throws FileStoreServiceException If unable to save the info.
		 *
		 */
		protected void save()
			throws FileStoreServiceException
			{
			OutputStream output = null ;
			try {
				output = new FileOutputStream(
					this.getInfoFile()
					) ;
				this.properties.save(
					output
					) ;
				}
			catch (IOException ouch)
				{
				throw new FileStoreServiceException(
					"Unable to save info file",
					ouch
					) ;
				}
			finally
				{
				if (null != output)
					{
					try {
						output.close() ;
						}
					catch (IOException ouch)
						{
						log.warn("----") ;
						log.warn("RepositoryContainerImpl.save") ;
						log.warn("Failed to close output stream") ;
						log.warn("----") ;
						}
					}
				}
			}

		/**
		 * Delete the container file(s).
		 * @throws FileStoreNotFoundException If unable to locate the file.
		 * @throws FileStoreServiceException If unable to complete the action.
		 *
		 */
		public void delete()
			throws FileStoreServiceException
			{
			this.getDataFile().delete() ;
			this.getInfoFile().delete() ;
			}

		/**
		 * Import an array of bytes.
		 * @param bytes The array of bytes to import into the container.
		 * @throws FileStoreServiceException If unable to complete the action.
		 *
		 */
		public void importBytes(byte[] bytes)
			throws FileStoreServiceException
			{
			//
			// Transfer the data.
			OutputStream output = null ;
			try {
				output = this.getDataOutputStream() ;
				output.write(bytes) ;
				}
			catch (IOException ouch)
				{
				throw new FileStoreServiceException(
					"Unable to write to data file",
					ouch
					) ;
				}
			finally
				{
				//
				// Update our properties.
				this.update() ;
				//
				// Close our output stream.
				try {
					if (null != output)
						{
						output.close() ;
						}
					}
				catch (IOException ouch)
					{
					log.warn("----") ;
					log.warn("RepositoryContainerImpl.importBytes") ;
					log.warn("Failed to close output stream") ;
					log.warn("----") ;
					}
				}
			}

		/**
		 * Export our contents as bytes.
		 * @return An array of bytes from the container contents.
		 * @throws FileStoreNotFoundException If unable to locate the file.
		 * @throws FileStoreServiceException If unable to complete the action.
		 * @todo Deprecate this soon, as it potentially allocates a HUGE buffer in memory.
		 *
		 */
		public byte[] exportBytes()
			throws FileStoreServiceException, FileStoreNotFoundException
			{
			int size = (int) this.getDataFile().length() ;
			byte[] buffer = new byte[size] ;
			InputStream input = null ;
			try {
				input = this.getDataInputStream() ;
				input.read(buffer) ;
				}
			catch (IOException ouch)
				{
				throw new FileStoreServiceException(
					"Unable to read file data",
					ouch
					) ;
				}
			finally
				{
				try {
					if (null != input)
						{
						input.close() ;
						}
					}
				catch (IOException ouch)
					{
					log.warn("----") ;
					log.warn("RepositoryContainerImpl.exportBytes") ;
					log.warn("Failed to close input stream") ;
					log.warn("----") ;
					}
				}
			return buffer ;
			}

		/**
		 * Append an array of bytes.
		 * @param bytes The array of bytes to append to the container.
		 * @throws FileStoreNotFoundException If unable to locate the file.
		 * @throws FileStoreServiceException If unable to complete the action.
		 *
		 */
		public void appendBytes(byte[] bytes)
			throws FileStoreServiceException, FileStoreNotFoundException
			{
			//
			// Transfer the data.
			OutputStream output = null ;
			try {
				output = this.getDataOutputStream(true) ;
				output.write(bytes) ;
				output.close() ;
				}
			catch (IOException ouch)
				{
				throw new FileStoreServiceException(
					"Unable to write data file",
					ouch
					) ;
				}
			finally
				{
				//
				// Update our properties.
				this.update() ;
				//
				// Close our output stream.
				try {
					if (null != output)
						{
						output.close() ;
						}
					}
				catch (IOException ouch)
					{
					log.warn("----") ;
					log.warn("RepositoryContainerImpl.appendBytes") ;
					log.warn("Failed to close output stream") ;
					log.warn("----") ;
					}
				}
			}

		/**
		 * Get an input stream to the container contents.
		 * @throws FileStoreNotFoundException If unable to locate the file.
		 * @throws FileStoreServiceException If unable to open the file.
		 *
		 */
		public InputStream getDataInputStream()
			throws FileStoreServiceException, FileStoreNotFoundException
			{
			try {
				return new FileInputStream(
					this.getDataFile()
					) ;
				}
			catch (FileNotFoundException ouch)
				{
				throw new FileStoreNotFoundException(
					this.ident.toString()
					) ;
				}
			catch (IOException ouch)
				{
				throw new FileStoreServiceException(
					"Unable to open data file",
					ouch
					) ;
				}
			}

		/**
		 * Get an output stream to the container contents.
		 * @throws FileStoreServiceException If unable to open the file.
		 *
		 */
		public OutputStream getDataOutputStream()
			throws FileStoreServiceException
			{
			return getDataOutputStream(false) ;
			}

		/**
		 * Get an output stream to the container contents.
		 * @param append True if the output stream should append to the end of the file.
		 * @throws FileStoreServiceException If unable to open the file.
		 *
		 */
		public OutputStream getDataOutputStream(boolean append)
			throws FileStoreServiceException
			{
			try {
				return new FileOutputStream(
					this.getDataFile(),
					append
					) ;
				}
			catch (IOException ouch)
				{
				throw new FileStoreServiceException(
					"Unable to open local data file",
					ouch
					) ;
				}
			}

		/**
		 * Import our data from a URL.
		 * @param url The URL to import the data from.
		 * @throws FileStoreTransferException If unable to complete the action.
		 * @throws FileStoreServiceException If unable to open the local file.
		 *
		 */
		public void importData(URL url)
			throws FileStoreServiceException, FileStoreTransferException
			{
			try {
				//
				// Open a connection to the URL.
				URLConnection connection = url.openConnection() ;
				//
				// Set the source URL.
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
				InputStream input = null ;
				try {
					input = connection.getInputStream() ;
					this.importData(
						input
						) ;
					}
				finally
					{
					try {
						if (null != input)
							{
							input.close() ;
							}
						}
					catch (IOException ouch)
						{
						log.warn("----") ;
						log.warn("RepositoryContainerImpl.importData") ;
						log.warn("Failed to close input stream") ;
						log.warn("----") ;
						}
					}
				}
			catch (IOException ouch)
				{
				throw new FileStoreTransferException(
					"Unable to open URL source",
					ouch
					) ;
				}
			}

		/**
		 * Import our data from an InputStream.
		 * @param stream The input stream to import the data from.
		 * @throws FileStoreTransferException If unable to complete the action.
		 * @throws FileStoreServiceException If unable to open the local file.
		 *
		 */
		public void importData(InputStream input)
			throws FileStoreServiceException, FileStoreTransferException
			{
			//
			// Transfer the data.
			OutputStream output = null ;
			try {
				output = this.getDataOutputStream() ;
				TransferUtil trans = new TransferUtil(
					input,
					output
					) ;
				trans.transfer() ;
				}
			catch (IOException ouch)
				{
				throw new FileStoreTransferException(
					"Encountered error while transferring data",
					ouch
					) ;
				}
			finally
				{
				//
				// Close the output stream.
				try {
					if (null != output)
						{
						output.close() ;
						}
					}
				catch (IOException ouch)
					{
					log.warn("----") ;
					log.warn("RepositoryContainerImpl.importData") ;
					log.warn("Failed to close output stream") ;
					log.warn("----") ;
					}
				//
				// Update our properties.
				this.update() ;
				}
			}

		/**
		 * Export our data to an OutputStream.
		 * @param stream The output stream to export the data to.
		 * @throws FileStoreTransferException If unable to complete the action.
		 * @throws FileStoreServiceException If unable to open the local file.
		 *
		 */
		public void exportData(OutputStream output)
			throws FileStoreServiceException, FileStoreNotFoundException, FileStoreTransferException
			{
			InputStream input = null ;
			try {
				input = this.getDataInputStream() ;
				TransferUtil trans = new TransferUtil(
					input,
					output
					) ;
				trans.transfer() ;
				}
			catch (IOException ouch)
				{
				throw new FileStoreTransferException(
					"Encountered error while transferring data",
					ouch
					) ;
				}
			finally
				{
				//
				// Close our input stream.
				try {
					if (null != input)
						{
						input.close() ;
						}
					}
				catch (IOException ouch)
					{
					log.warn("----") ;
					log.warn("RepositoryContainerImpl.exportData") ;
					log.warn("Failed to close input stream") ;
					log.warn("----") ;
					}
				}
			}
		}
	}
