/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/resolver/Attic/FileManagerDelegateResolverImpl.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: FileManagerDelegateResolverImpl.java,v $
 *   Revision 1.3  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.2.4.2  2005/01/12 14:20:57  dave
 *   Replaced tabs with spaces ....
 *
 *   Revision 1.2.4.1  2004/12/22 07:38:36  dave
 *   Started to move towards StoreClient API ...
 *
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

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URL ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.registry.client.query.RegistryService ;

import org.astrogrid.filemanager.common.FileManager ;
import org.astrogrid.filemanager.common.ivorn.FileManagerIvornParser ;
import org.astrogrid.filemanager.client.FileManagerDelegate ;
import org.astrogrid.filemanager.client.FileManagerSoapDelegate ;
import org.astrogrid.filemanager.client.FileManagerMockDelegate ;

/**
 * A helper class to resolve an Ivron into a service delegate.
 *
 */
public class FileManagerDelegateResolverImpl
    implements FileManagerDelegateResolver
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FileManagerDelegateResolverImpl.class);

    /**
     * Public constructor, using the default registry service.
     *
     */
    public FileManagerDelegateResolverImpl()
        {
        this.resolver = new FileManagerEndpointResolverImpl() ;
        }

    /**
     * Public constructor, using a specific registry service.
     * @param registry The endpoint address for our RegistryDelegate.
     *
     */
    public FileManagerDelegateResolverImpl(URL registry)
        {
        this.resolver = new FileManagerEndpointResolverImpl(registry) ;
        }

    /**
     * Public constructor, using a specific registry delegate.
     * @param registry The registry delegate.
     *
     */
    public FileManagerDelegateResolverImpl(RegistryService registry)
        {
        this.resolver = new FileManagerEndpointResolverImpl(registry) ;
        }

    /**
     * Our endpoint resolver.
     *
     */
    private FileManagerEndpointResolver resolver ;

    /**
     * Resolve an Ivorn into a delegate.
     * @param ivorn An Ivorn containing a filemanager identifier.
     * @return A FileManagerDelegate for the service.
     * @throws FileManagerResolverException If unable to resolve the identifier.
     *
     */
    public FileManagerDelegate resolve(Ivorn ivorn)
        throws FileManagerResolverException
        {
        log.debug("") ;
        log.debug("FileManagerDelegateResolverImpl.resolve()") ;
        log.debug("  Ivorn : " + ((null != ivorn) ? ivorn.toString() : "null")) ;
        //
        // Resolve the endpoint and return a new soap delegate.
        return new FileManagerSoapDelegate(
            resolver.resolve(
                ivorn
                )
            ) ;
        }
    }

