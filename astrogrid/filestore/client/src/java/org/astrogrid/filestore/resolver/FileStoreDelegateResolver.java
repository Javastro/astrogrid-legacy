/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/client/src/java/org/astrogrid/filestore/resolver/FileStoreDelegateResolver.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/08/18 19:00:01 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: FileStoreDelegateResolver.java,v $
 *   Revision 1.3  2004/08/18 19:00:01  dave
 *   Myspace manager modified to use remote filestore.
 *   Tested before checkin - integration tests at 91%.
 *
 *   Revision 1.2.8.1  2004/07/28 03:00:17  dave
 *   Refactored resolver constructors and added mock ivorn
 *
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
public class FileStoreDelegateResolver
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Public constructor, using the default registry service.
     *
     */
    public FileStoreDelegateResolver()
        {
		this.resolver = new FileStoreEndpointResolver() ;
        }

    /**
     * Public constructor, using a specific registry service.
     * @param registry The endpoint address for our RegistryDelegate.
     *
     */
    public FileStoreDelegateResolver(URL registry)
        {
		this.resolver = new FileStoreEndpointResolver(registry) ;
        }

    /**
     * Public constructor, using a specific registry delegate.
     * @param registry The registry delegate.
     *
     */
    public FileStoreDelegateResolver(RegistryService registry)
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
     *
     */
    public FileStoreDelegate resolve(Ivorn ivorn)
        throws FileStoreIdentifierException, FileStoreResolverException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("FileStoreDelegateResolver.resolve()") ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
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

