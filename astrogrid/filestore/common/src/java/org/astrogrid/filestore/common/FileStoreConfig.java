/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/FileStoreConfig.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:19:19 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreConfig.java,v $
 *   Revision 1.2  2004/11/25 00:19:19  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.1  2004/10/19 14:56:15  dave
 *   Refactored config and resolver to enable multiple instances of mock implementation.
 *   Required to implement handling of multiple FileStore(s) in FileManager.
 *
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
package org.astrogrid.filestore.common ;

import java.net.URL ;
import java.io.File ;
import org.astrogrid.store.Ivorn ;

import org.astrogrid.filestore.common.exception.FileStoreServiceException ;

/**
 * Public interface for a FileStore configuration.
 *
 */
public interface FileStoreConfig
	{
	/**
	 * The local service ivorn.
	 * @throws FileStoreServiceException if unable to read the property.
	 *
	 */
	public Ivorn getServiceIvorn()
		throws FileStoreServiceException ;

	/**
	 * The local service URL.
	 * @throws FileStoreServiceException if unable to read the property.
	 *
	 */
	public URL getServiceUrl()
		throws FileStoreServiceException ;

	/**
	 * Generate a resource ivorn.
	 * @param ident - the resource identifier.
	 * @throws FileStoreServiceException if unable to read the property.
	 *
	 */
	public Ivorn getResourceIvorn(String ident)
		throws FileStoreServiceException ;

	/**
	 * Generate a resource URL.
	 * @param ident - the resource identifier.
	 * @throws FileStoreServiceException if unable to read the property.
	 *
	 */
	public URL getResourceUrl(String ident)
		throws FileStoreServiceException ;

	/**
	 * The repository data directory.
	 * @throws FileStoreServiceException if unable to read the property.
	 *
	 */
	public File getDataDirectory()
		throws FileStoreServiceException ;

	/**
	 * The repository info directory.
	 * @throws FileStoreServiceException if unable to read the property.
	 *
	 */
	public File getInfoDirectory()
		throws FileStoreServiceException ;

	}



