/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/client/src/java/org/astrogrid/filestore/resolver/FileStoreDelegateResolver.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/23 15:17:30 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: FileStoreDelegateResolver.java,v $
 *   Revision 1.2  2004/07/23 15:17:30  dave
 *   Merged development branch, dave-dev-200407231013, into HEAD
 *
 *   Revision 1.1.2.1  2004/07/23 15:04:46  dave
 *   Added delegate resolver and tests
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.resolver ;

import java.net.URL ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.registry.RegistryException;

import org.astrogrid.filestore.common.FileStore ;
import org.astrogrid.filestore.client.FileStoreDelegate ;
import org.astrogrid.filestore.client.FileStoreSoapDelegate ;
import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;

/**
 * A helper class to resolve an Ivron into a service delegate.
 *
 */
public class FileStoreDelegateResolver
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Public constructor, using the default Registry service.
     *
     */
    public FileStoreDelegateResolver()
        {
		this.resolver = new FileStoreEndpointResolver() ;
        }

    /**
     * Public constructor, for a specific Registry service.
     * @param registry The endpoint address for our RegistryDelegate.
     *
     */
    public FileStoreDelegateResolver(URL registry)
        {
		this.resolver = new FileStoreEndpointResolver(registry) ;
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
     * @throws RegistryException If the Registry is unable to handle the identifier.
     *
     */
    public FileStoreDelegate resolve(Ivorn ivorn)
        throws RegistryException, FileStoreIdentifierException, FileStoreResolverException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("FileStoreDelegateResolver.resolve()") ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
		//
		// Resolve the endpoint and return a new soap delegate.
		return new FileStoreSoapDelegate(
			resolver.resolve(ivorn)
			) ;
        }
    }

