/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/server/src/java/org/astrogrid/filestore/server/repository/Attic/RepositoryConfigImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/14 13:50:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: RepositoryConfigImpl.java,v $
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

import org.astrogrid.config.Config ;
import org.astrogrid.config.SimpleConfig ;
import org.astrogrid.config.PropertyNotFoundException ;

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
	public static final String STORE_SERVICE_IDENTIFIER = "org.astrogrid.filestore.service" ;

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
	 * The service identifier.
	 *
	 */
	public String getServiceIdent()
		{
		return (String) config.getProperty(
			STORE_SERVICE_IDENTIFIER
			) ;
		}

	/**
	 * The repository data directory.
	 *
	 */
	public File getDataDirectory()
		{
		return new File(
			(String) config.getProperty(
				STORE_SERVICE_ROOT
				)
			) ;
		}

	/**
	 * The repository info directory.
	 *
	 */
	public File getInfoDirectory()
		{
		return new File(
			(String) config.getProperty(
				STORE_SERVICE_ROOT
				)
			) ;
		}
	}



