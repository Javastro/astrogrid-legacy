/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/server/src/java/org/astrogrid/filestore/server/repository/RepositoryImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/21 18:11:55 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 * <cvs:log>
 *   $Log: RepositoryImpl.java,v $
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

import java.net.URL ;
import java.net.URLConnection ;

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
import org.astrogrid.filestore.common.transfer.TransferUtil ;
import org.astrogrid.filestore.common.exception.FileStoreException ;
import org.astrogrid.filestore.common.exception.FileStoreNotFoundException ;
import org.astrogrid.filestore.common.exception.FileIdentifierException ;
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
	 * Switch for our debug statements.
	 *
	 */
	protected static final boolean DEBUG_FLAG = true ;

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
	 * @throws FileIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public RepositoryContainer load(String ident)
		throws FileStoreServiceException, FileStoreNotFoundException, FileIdentifierException
		{
		//
		// Check for null ident.
		if (null == ident)
			{
			throw new FileIdentifierException(
				FileIdentifierException.NULL_IDENT_MESSAGE
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
	 * @throws FileIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public RepositoryContainer duplicate(String ident)
		throws FileStoreServiceException, FileStoreNotFoundException, FileIdentifierException
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
		try {
			TransferUtil trans = new TransferUtil(
				original.getDataInputStream(),
				duplicate.getDataOutputStream()
				) ;
			trans.transfer() ;
			}
		catch (IOException ouch)
			{
			throw new FileStoreServiceException(
				"Unable to transfer file data",
				ouch
				) ;
			}
		//
		// Save our properties.

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
			// Update the info properties.
			this.properties.setProperty(
				FileProperties.STORE_SERVICE_IDENTIFIER,
				config.getServiceIdent()
				) ;
			this.properties.setProperty(
				FileProperties.STORE_INTERNAL_IDENTIFIER,
				ident.toString()
				) ;
			//
			// Save the container info.
			this.save() ;
			}

		/**
		 * Constructor to load an existing container.
		 * @param ident The identifier of the container.
		 * @throws FileIdentifierException if the identifier is null or not valid.
		 * @throws FileStoreNotFoundException if unable to locate the file.
		 * @throws FileStoreServiceException if unable handle the request.
		 *
		 */
		public RepositoryContainerImpl(String ident)
			throws FileStoreServiceException, FileStoreNotFoundException, FileIdentifierException
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
		 *
		 */
		public File getInfoFile()
			{
			return new File(
				config.getInfoDirectory(),
				this.ident.toString() + ".inf"
				) ;
			}

		/**
		 * Generate the data file path.
		 *
		 */
		public File getDataFile()
			{
			return new File(
				config.getDataDirectory(),
				this.ident.toString() + ".dat"
				) ;
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
			try {
				this.properties.load(
					new FileInputStream(
						this.getInfoFile()
						)
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
					"Unable to load file info",
					ouch
					) ;
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
			try {
				this.properties.save(
					new FileOutputStream(
						this.getInfoFile()
						)
					) ;
				}
			catch (IOException ouch)
				{
				throw new FileStoreServiceException(
					"Unable to save file info",
					ouch
					) ;
				}
			}

		/**
		 * Delete the container file(s).
		 * @throws FileStoreNotFoundException If unable to locate the file.
		 * @throws FileStoreServiceException If unable to complete the action.
		 * @todo Add more robust error reporting.
		 *
		 */
		public void delete()
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
			try {
				this.getDataOutputStream().write(bytes) ;
				}
			catch (IOException ouch)
				{
				throw new FileStoreServiceException(
					"Unable to write file data",
					ouch
					) ;
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
			try {
				int size = (int) this.getDataFile().length() ;
				byte[] buffer = new byte[size] ;

				this.getDataInputStream().read(buffer) ;

				return buffer ;
				}
			catch (IOException ouch)
				{
				throw new FileStoreServiceException(
					"Unable to read file data",
					ouch
					) ;
				}
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
			try {
				getDataOutputStream(true).write(bytes) ;
				}
			catch (IOException ouch)
				{
				throw new FileStoreServiceException(
					"Unable to write file data",
					ouch
					) ;
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
				// Set our propeties from the URL headers.
				this.properties.setProperty(
					FileProperties.TRANSFER_SOURCE_PROPERTY,
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
				//
				// Save our container info.
				this.save() ;
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
		 * Import our data from am InputStream.
		 * @param stream The input stream to import the data from.
		 * @throws FileStoreTransferException If unable to complete the action.
		 * @throws FileStoreServiceException If unable to open the local file.
		 *
		 */
		public void importData(InputStream stream)
			throws FileStoreServiceException, FileStoreTransferException
			{
			try {
				TransferUtil trans = new TransferUtil(
					stream,
					this.getDataOutputStream()
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
			}
		}
	}
