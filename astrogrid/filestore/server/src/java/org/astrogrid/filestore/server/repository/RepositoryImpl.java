/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/server/src/java/org/astrogrid/filestore/server/repository/RepositoryImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/14 13:50:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: RepositoryImpl.java,v $
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
import org.astrogrid.filestore.common.exception.FileStoreException ;
import org.astrogrid.filestore.common.exception.FileStoreNotFoundException ;
import org.astrogrid.filestore.common.exception.FileIdentifierException ;
import org.astrogrid.filestore.common.exception.FileStoreServiceException ;

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
	 * @param info An optional info block describing the file contents.
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
				"Null identifier"
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
		// Copy the data from the original.
		// (code copied from the Apache Ant tools).
		try {
			InputStream  in  = original.getDataInputStream() ;
			OutputStream out = duplicate.getDataOutputStream() ;
			try {
				byte[] buffer = new byte[COPY_BUFFER_SIZE];
				int count = 0;
				do {
					out.write(buffer, 0, count);
					count = in.read(buffer, 0, buffer.length);
					}
				while (count != -1);
				}
			finally {
				if (null != out)
					{
					out.close() ;
					}
				if (null != in)
					{
					in.close() ;
					}
				}
			}
		catch (IOException ouch)
			{
			throw new FileStoreServiceException(
				"Unable to transfer file data",
				ouch
				) ;
			}
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
			// Create a our properties.
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
		 * @throws FileStoreServiceException If unable to complete the action.
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
		 * @throws FileStoreServiceException If unable to complete the action.
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
		 * @throws FileStoreServiceException If unable to complete the action.
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
					"Unable to open data file",
					ouch
					) ;
				}
			}
		}
	}
