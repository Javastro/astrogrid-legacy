/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/server/src/java/org/astrogrid/filestore/server/repository/Repository.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/08/27 22:43:15 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 * <cvs:log>
 *   $Log: Repository.java,v $
 *   Revision 1.4  2004/08/27 22:43:15  dave
 *   Updated filestore and myspace to report file size correctly.
 *
 *   Revision 1.3.38.1  2004/08/26 19:06:50  dave
 *   Modified filestore to return file size in properties.
 *
 *   Revision 1.3  2004/07/23 09:11:16  dave
 *   Merged development branch, dave-dev-200407221513, into HEAD
 *
 *   Revision 1.2.12.1  2004/07/23 02:10:58  dave
 *   Added IvornFactory and IvornParser
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

import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.file.FileProperties ;
import org.astrogrid.filestore.common.exception.FileStoreException ;
import org.astrogrid.filestore.common.exception.FileStoreNotFoundException ;
import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;
import org.astrogrid.filestore.common.exception.FileStoreTransferException ;
import org.astrogrid.filestore.common.exception.FileStoreServiceException ;

/**
 * Public interface for a file Repository.
 *
 */
public interface Repository
	{
	/**
	 * Create a new container.
	 * @param properties The FileProperties describing the imported data.
	 * @return A new file container.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public RepositoryContainer create(FileProperties properties)
		throws FileStoreServiceException ;

	/**
	 * Locate an existing container.
	 * @param ident The identifier of the container.
	 * @return The file container, if it exists.
	 * @throws FileStoreIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public RepositoryContainer load(String ident)
		throws FileStoreServiceException, FileStoreNotFoundException, FileStoreIdentifierException ;

	/**
	 * Duplicate (copy) an existing container.
	 * @param ident The identifier of the container.
	 * @return The new file container.
	 * @throws FileStoreTransferException If unable to transfer the data.
	 * @throws FileStoreIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public RepositoryContainer duplicate(String ident)
		throws FileStoreServiceException, FileStoreNotFoundException, FileStoreIdentifierException, FileStoreTransferException ;

	}
