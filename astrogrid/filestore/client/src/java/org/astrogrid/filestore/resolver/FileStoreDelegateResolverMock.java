/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/client/src/java/org/astrogrid/filestore/resolver/FileStoreDelegateResolverMock.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:19:21 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: FileStoreDelegateResolverMock.java,v $
 *   Revision 1.2  2004/11/25 00:19:21  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.4  2004/11/09 17:41:36  dave
 *   Added file:// URL handling to allow server URLs to be tested.
 *   Added importInit and exportInit to server implementation.
 *   Moved remaining tests out of extended test abd removed it.
 *
 *   Revision 1.1.2.3  2004/11/04 02:33:03  dave
 *   Refactored mock delegate and config to make it easier to test filemanager with multiple filstores.
 *
 *   Revision 1.1.2.2  2004/10/21 21:00:13  dave
 *   Added mock://xyz URL handler to enable testing of transfer.
 *   Implemented importInit to the mock service and created transfer tests.
 *
 *   Revision 1.1.2.1  2004/10/19 14:56:15  dave
 *   Refactored config and resolver to enable multiple instances of mock implementation.
 *   Required to implement handling of multiple FileStore(s) in FileManager.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.resolver ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URL ;

import java.util.Map ;
import java.util.HashMap ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.registry.client.query.RegistryService ;

import org.astrogrid.filestore.common.FileStore ;
import org.astrogrid.filestore.common.ivorn.FileStoreIvornParser ;
import org.astrogrid.filestore.client.FileStoreDelegate ;
import org.astrogrid.filestore.client.FileStoreSoapDelegate ;
import org.astrogrid.filestore.client.FileStoreMockDelegate ;
import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;
import org.astrogrid.filestore.common.exception.FileStoreServiceException ;

/**
 * A resolver for mock filestores.
 *
 */
public class FileStoreDelegateResolverMock
	implements FileStoreDelegateResolver
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FileStoreDelegateResolverMock.class);

    /**
     * Public constructor.
     *
     */
    public FileStoreDelegateResolverMock()
        {
        }

	/**
	 * Our internal map of services.
	 *
	 */
	private Map map = new HashMap() ;

	/**
	 * Register a new filestore.
	 *
	 */
	public void register(FileStoreDelegate store)
		throws FileStoreServiceException
		{
		map.put(
			store.identifier(),
			store
			);
		}

    /**
     * Resolve an Ivorn into a delegate.
     * @param ivorn An Ivorn containing a filestore identifier.
     * @return A FileStoreDelegate for the service.
     * @throws FileStoreIdentifierException If the identifier is not valid.
     * @throws FileStoreResolverException If unable to resolve the identifier.
     *
     */
    public FileStoreDelegate resolve(Ivorn ivorn)
        throws FileStoreIdentifierException, FileStoreResolverException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("FileStoreDelegateResolverMock.resolve()") ;
        log.debug("  Ivorn : " + ivorn) ;
		//
		// Parse the ivorn.
		String ident = new FileStoreIvornParser(
			ivorn
			).getServiceIdent() ;
		//
		// Lookup the filestore in our map.
		if (map.containsKey(ident))
			{
			return (FileStoreDelegate) map.get(ident) ;
			}
		else {
			throw new FileStoreResolverException(
				"FileStore not found"
				) ;
			}
        }
    }

