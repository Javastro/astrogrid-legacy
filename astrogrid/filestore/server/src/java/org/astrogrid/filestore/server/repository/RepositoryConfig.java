/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/server/src/java/org/astrogrid/filestore/server/repository/Attic/RepositoryConfig.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/14 13:50:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: RepositoryConfig.java,v $
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

/**
 * Configuration interface for a repository.
 *
 */
public interface RepositoryConfig
	{
	/**
	 * The service identifier.
	 *
	 */
	public String getServiceIdent() ;

	/**
	 * The repository data directory.
	 *
	 */
	public File getDataDirectory() ;

	/**
	 * The repository info directory.
	 *
	 */
	public File getInfoDirectory() ;

	}



