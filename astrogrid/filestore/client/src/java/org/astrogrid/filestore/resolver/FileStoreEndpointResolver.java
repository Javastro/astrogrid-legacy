/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/client/src/java/org/astrogrid/filestore/resolver/FileStoreEndpointResolver.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:19:21 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: FileStoreEndpointResolver.java,v $
 *   Revision 1.6  2004/11/25 00:19:21  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.5.14.1  2004/10/19 14:56:15  dave
 *   Refactored config and resolver to enable multiple instances of mock implementation.
 *   Required to implement handling of multiple FileStore(s) in FileManager.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.resolver ;

import java.net.URL ;
import java.net.MalformedURLException ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.RegistryDelegateFactory ;
import org.astrogrid.registry.client.query.RegistryService ;

import org.astrogrid.filestore.common.ivorn.FileStoreIvornParser ;
import org.astrogrid.filestore.common.ivorn.FileStoreIvornFactory ;

import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;


/**
 * Public interface for a helper class to resolve an Ivron into a service endpoint.
 *
 */
public interface FileStoreEndpointResolver
    {

    /**
     * Resolve an Ivorn into a service endpoint.
     * @param ivorn An Ivorn containing a filestore identifier.
     * @return The endpoint address for the service.
     * @throws FileStoreIdentifierException If the identifier is not valid.
     * @throws FileStoreResolverException If unable to resolve the identifier.
     *
     */
    public URL resolve(Ivorn ivorn)
        throws FileStoreIdentifierException, FileStoreResolverException ;

    /**
     * Resolve an Ivorn parser into a service endpoint.
     * @param parser A FileStoreIvornParser containing the Filestore identifier.
     * @return The endpoint address for the service.
     * @throws FileStoreIdentifierException If the identifier is not valid.
     * @throws FileStoreResolverException If unable to resolve the identifier.
     *
     */
    public URL resolve(FileStoreIvornParser parser)
        throws FileStoreIdentifierException, FileStoreResolverException ;

    }

