/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/server/src/java/org/astrogrid/filestore/server/repository/Attic/RepositoryConfigImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/08/18 19:00:01 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: RepositoryConfigImpl.java,v $
 *   Revision 1.3  2004/08/18 19:00:01  dave
 *   Myspace manager modified to use remote filestore.
 *   Tested before checkin - integration tests at 91%.
 *
 *   Revision 1.2.22.2  2004/08/09 10:16:28  dave
 *   Added resource URL to the properties.
 *
 *   Revision 1.2.22.1  2004/08/06 22:25:06  dave
 *   Refactored bits and broke a few tests ...
 *
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
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

import java.net.URL ;
import java.net.URLConnection ;
import java.net.MalformedURLException ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.config.Config ;
import org.astrogrid.config.SimpleConfig ;
import org.astrogrid.config.PropertyNotFoundException ;

import org.astrogrid.filestore.common.ivorn.FileStoreIvornFactory ;

import org.astrogrid.filestore.common.exception.FileStoreServiceException ;

/**
 * Configuration implementation using the AstroGrid Config.
 *
 */
public class RepositoryConfigImpl
	implements RepositoryConfig
	{
	/**
	 * The config property key for our service identifier.
	 *
	 */
	public static final String STORE_SERVICE_IVORN = "org.astrogrid.filestore.service.ivorn" ;

	/**
	 * The config property key for our filestore URL.
	 *
	 */
	public static final String STORE_FILESTORE_URL = "org.astrogrid.filestore.service.url" ;

	/**
	 * The config property key for our file root.
	 * @todo Refactor to use a container
	 *
	 */
	public static final String STORE_SERVICE_ROOT = "org.astrogrid.filestore.repository" ;

	/**
	 * Reference to our AstroGrid config.
	 *
	 */
	private Config config ;

	/**
	 * Public constructor, using the default AstroGrid config.
	 *
	 */
	public RepositoryConfigImpl()
		{
		this(
			SimpleConfig.getSingleton()
			) ;
		}

	/**
	 * Public constructor, using a specific config.
	 * @param config Reference to the config to use.
	 *
	 */
	public RepositoryConfigImpl(Config config)
		{
		this.config = config ;
		}

	/**
	 * Access to the local service ivorn.
	 * @throws FileStoreServiceException if unable to read the property.
	 *
	 */
	public Ivorn getServiceIvorn()
		throws FileStoreServiceException
		{
		try {
			return new Ivorn(
				(String) config.getProperty(
					STORE_SERVICE_IVORN
					)
				) ;
			}
		catch (Throwable ouch)
			{
			throw new FileStoreServiceException(
				"Unable to read service ivorn from config.",
				ouch
				) ;
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
				(String) config.getProperty(
					STORE_FILESTORE_URL
					)
				) ;
			}
		catch (Throwable ouch)
			{
			throw new FileStoreServiceException(
				"Unable to generate service URL.",
				ouch
				) ;
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
				(String) config.getProperty(
					STORE_SERVICE_IVORN
					),
				ident
				) ;
			}
		catch (Throwable ouch)
			{
			throw new FileStoreServiceException(
				"Unable to generate resource ivorn.",
				ouch
				) ;
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
				(String) config.getProperty(
					STORE_FILESTORE_URL
					)
				+ "/" + ident
				) ;
			}
		catch (Throwable ouch)
			{
			throw new FileStoreServiceException(
				"Unable to generate resource URL.",
				ouch
				) ;
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
		try {
			return new File(
				(String) config.getProperty(
					STORE_SERVICE_ROOT
					)
				) ;
			}
		catch (Throwable ouch)
			{
			throw new FileStoreServiceException(
				"Unable to read data directory from config.",
				ouch
				) ;
			}
		}

	/**
	 * The repository info directory.
	 * @throws FileStoreServiceException if unable to read the property.
	 *
	 */
	public File getInfoDirectory()
		throws FileStoreServiceException
		{
		try {
			return new File(
				(String) config.getProperty(
					STORE_SERVICE_ROOT
					)
				) ;
			}
		catch (Throwable ouch)
			{
			throw new FileStoreServiceException(
				"Unable to read info directory from config.",
				ouch
				) ;
			}
		}
	}



