/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/resolver/Attic/FileManagerDelegateResolver.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:20:27 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: FileManagerDelegateResolver.java,v $
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

import org.astrogrid.store.Ivorn ;

import org.astrogrid.registry.client.query.RegistryService ;

import org.astrogrid.filemanager.common.FileManager ;
import org.astrogrid.filemanager.common.ivorn.FileManagerIvornParser ;
import org.astrogrid.filemanager.client.FileManagerDelegate ;
import org.astrogrid.filemanager.client.FileManagerSoapDelegate ;
import org.astrogrid.filemanager.client.FileManagerMockDelegate ;
import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException ;

/**
 * Public interface for a helper class to resolve an Ivron into a service delegate.
 *
 */
public interface FileManagerDelegateResolver
    {

    /**
     * Resolve an Ivorn into a delegate.
     * @param ivorn An Ivorn containing a filestore identifier.
     * @return A FileManagerDelegate for the service.
     * @throws FileManagerIdentifierException If the identifier is not valid.
     * @throws FileManagerResolverException If unable to resolve the identifier.
     *
     */
    public FileManagerDelegate resolve(Ivorn ivorn)
        throws FileManagerIdentifierException, FileManagerResolverException ;
    }

