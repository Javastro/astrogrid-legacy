/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/client/src/java/org/astrogrid/filestore/resolver/FileStoreDelegateResolverImpl.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:19:21 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: FileStoreDelegateResolverImpl.java,v $
 *   Revision 1.2  2004/11/25 00:19:21  jdt
 *   Merge from dave-dev-200410061224-200411221626
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

import org.astrogrid.store.Ivorn ;

import org.astrogrid.registry.client.query.RegistryService ;

import org.astrogrid.filestore.common.FileStore ;
import org.astrogrid.filestore.common.ivorn.FileStoreIvornParser ;
import org.astrogrid.filestore.client.FileStoreDelegate ;
import org.astrogrid.filestore.client.FileStoreSoapDelegate ;
import org.astrogrid.filestore.client.FileStoreMockDelegate ;
import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;

/**
 * A helper class to resolve an Ivron into a service delegate.
 *
 */
public class FileStoreDelegateResolverImpl
	implements FileStoreDelegateResolver
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FileStoreDelegateResolverImpl.class);

    /**
     * Public constructor, using the default registry service.
     *
     */
    public FileStoreDelegateResolverImpl()
        {
		this.resolver = new FileStoreEndpointResolverImpl() ;
        }

    /**
     * Public constructor, using a specific registry service.
     * @param registry The endpoint address for our RegistryDelegate.
     *
     */
    public FileStoreDelegateResolverImpl(URL registry)
        {
		this.resolver = new FileStoreEndpointResolverImpl(registry) ;
        }

    /**
     * Public constructor, using a specific registry delegate.
     * @param registry The registry delegate.
     *
     */
    public FileStoreDelegateResolverImpl(RegistryService registry)
        {
		this.resolver = new FileStoreEndpointResolverImpl(registry) ;
		}

	/**
	 * Our endpoint resolver.
	 *
	 */
	private FileStoreEndpointResolver resolver ;

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
        log.debug("FileStoreDelegateResolverImpl.resolve()") ;
        log.debug("  Ivorn : " + ivorn) ;
		//
		// Create a parser to check the ivorn.
		FileStoreIvornParser parser =
			new FileStoreIvornParser(
				ivorn
				) ;
		//
		// If this is a mock ivorn.
		if (parser.isMock())
			{
			return new FileStoreMockDelegate() ;
			}
		//
		// Resolve the endpoint and return a new soap delegate.
		else {
			return new FileStoreSoapDelegate(
				resolver.resolve(
					ivorn
					)
				) ;
			}
        }
    }

