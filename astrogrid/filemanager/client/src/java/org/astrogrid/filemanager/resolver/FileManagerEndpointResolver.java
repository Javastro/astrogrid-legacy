/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/resolver/FileManagerEndpointResolver.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:20:27 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: FileManagerEndpointResolver.java,v $
 *   Revision 1.2  2004/11/25 00:20:27  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.1  2004/11/18 16:06:11  dave
 *   Added delegate resolver and tests ....
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.resolver ;

import java.net.URL ;
import java.net.MalformedURLException ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.RegistryDelegateFactory ;
import org.astrogrid.registry.client.query.RegistryService ;

import org.astrogrid.filemanager.common.ivorn.FileManagerIvornParser ;
import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory ;

import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException ;


/**
 * Public interface for a helper class to resolve an Ivron into a service endpoint.
 *
 */
public interface FileManagerEndpointResolver
    {

    /**
     * Resolve an Ivorn into a service endpoint.
     * @param ivorn An Ivorn containing a filemanager identifier.
     * @return The endpoint address for the service.
     * @throws FileManagerIdentifierException If the identifier is not valid.
     * @throws FileManagerResolverException If unable to resolve the identifier.
     *
     */
    public URL resolve(Ivorn ivorn)
        throws FileManagerIdentifierException, FileManagerResolverException ;

    /**
     * Resolve an Ivorn parser into a service endpoint.
     * @param parser A FileManagerIvornParser containing the Filestore identifier.
     * @return The endpoint address for the service.
     * @throws FileManagerIdentifierException If the identifier is not valid.
     * @throws FileManagerResolverException If unable to resolve the identifier.
     *
     */
    public URL resolve(FileManagerIvornParser parser)
        throws FileManagerIdentifierException, FileManagerResolverException ;

    }

