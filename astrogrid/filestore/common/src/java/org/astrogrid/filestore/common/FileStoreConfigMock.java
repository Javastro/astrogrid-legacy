/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/FileStoreConfigMock.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:19:19 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreConfigMock.java,v $
 *   Revision 1.2  2004/11/25 00:19:19  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.3  2004/11/04 02:33:03  dave
 *   Refactored mock delegate and config to make it easier to test filemanager with multiple filstores.
 *
 *   Revision 1.1.2.2  2004/10/29 15:54:50  dave
 *   Added exportInit to mock implementation ...
 *   Added UrlGetRequest to pass into exportInit ...
 *   Added test for exportInit and UrlGetRequest ...
 *
 *   Revision 1.1.2.1  2004/10/19 14:56:15  dave
 *   Refactored config and resolver to enable multiple instances of mock implementation.
 *   Required to implement handling of multiple FileStore(s) in FileManager.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common ;

import java.io.File ;

import java.net.URL ;
import java.net.URISyntaxException ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filestore.common.ivorn.FileStoreIvornFactory ;
import org.astrogrid.filestore.common.exception.FileStoreServiceException ;

import org.astrogrid.filestore.common.transfer.mock.Handler ;

/**
 * Public interface for a FileStore configuration.
 *
 */
public class FileStoreConfigMock
	implements FileStoreConfig
	{
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FileStoreConfigMock.class);

	/**
	 * The default service Ivorn.
	 *
	 */
	public static final String MOCK_SERVICE_IVORN = "ivo://org.astrogrid.mock/filestore" ;

	/**
	 * The default service URL.
	 *
	 */
	public static final String MOCK_SERVICE_URL   = "mock://" ;


	/**
	 * Public constructor.
	 *
	 */
	public FileStoreConfigMock()
		{
		this(
			MOCK_SERVICE_IVORN
			);
		}

	/**
	 * Public constructor.
	 *
	 */
	public FileStoreConfigMock(String ivorn)
		{
		//
		// Initialise our mock URL handler.
		Handler.register() ;
		//
		// Initialise our ivorn.
		setServiceIvorn(ivorn) ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public FileStoreConfigMock(Ivorn ivorn)
		{
		//
		// Initialise our mock URL handler.
		Handler.register() ;
		//
		// Initialise our ivorn.
		setServiceIvorn(ivorn) ;
		}

	/**
	 * Our local service ivorn.
	 *
	 */
	private Ivorn ivorn ;

	/**
	 * Access to the local service ivorn.
	 *
	 */
	public Ivorn getServiceIvorn()
		{
		return this.ivorn ;
		}

	/**
	 * Set the local service ivorn.
	 *
	 */
	public void setServiceIvorn(Ivorn ivorn)
		{
		this.ivorn = ivorn ;
		}

	/**
	 * Set the local service ivorn.
	 *
	 */
	public void setServiceIvorn(String ident)
		{
		try {
			this.ivorn = new Ivorn(
				ident
				) ;
			}
		catch (Exception ouch)
			{
			log.debug("Unable to configure service ivorn");
			}
		}

	/**
	 * The local service URL.
	 * @throws FileStoreServiceException if unable to read the property.
	 *
	 */
	public URL getServiceUrl()
		throws FileStoreServiceException
		{
		try {
			return new URL(
				MOCK_SERVICE_URL
				) ;
			}
		catch (Exception ouch)
			{
			return null ;
			}
		}

	/**
	 * Generate a resource ivorn.
	 * @param ident - the resource identifier.
	 * @throws FileStoreServiceException if unable to read the property.
	 *
	 */
	public Ivorn getResourceIvorn(String ident)
		throws FileStoreServiceException
		{
		try {
			return FileStoreIvornFactory.createIvorn(
				ivorn.toString(),
				ident
				) ;
			}
		catch (Throwable ouch)
			{
			return null ;
			}
		}

	/**
	 * Generate a resource URL.
	 * @param ident - the resource identifier.
	 * @throws FileStoreServiceException if unable to read the property.
	 *
	 */
	public URL getResourceUrl(String ident)
		throws FileStoreServiceException
		{
		try {
			return new URL(
				(MOCK_SERVICE_URL + "/" + ident)
				) ;
			}
		catch (Exception ouch)
			{
			return null ;
			}
		}

	/**
	 * The repository data directory.
	 * @throws FileStoreServiceException if unable to read the property.
	 *
	 */
	public File getDataDirectory()
		throws FileStoreServiceException
		{
		return null ;
		}

	/**
	 * The repository info directory.
	 * @throws FileStoreServiceException if unable to read the property.
	 *
	 */
	public File getInfoDirectory()
		throws FileStoreServiceException
		{
		return null ;
		}

	}



