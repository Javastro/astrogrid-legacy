/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/resolver/Attic/FileManagerDelegateResolverMock.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/02/10 12:44:10 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: FileManagerDelegateResolverMock.java,v $
 *   Revision 1.5  2005/02/10 12:44:10  jdt
 *   Merge from dave-dev-200502010902
 *
 *   Revision 1.4.2.1  2005/02/01 16:10:53  dave
 *   Updated FileManagerClient and factory to support full mock services ..
 *
 *   Revision 1.4  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.3.2.1  2005/01/22 07:54:16  dave
 *   Refactored delegate into a separate package ....
 *
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

import java.util.Map ;
import java.util.HashMap ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.registry.client.query.RegistryService ;

import org.astrogrid.filemanager.common.FileManager ;
import org.astrogrid.filemanager.common.ivorn.FileManagerIvornParser ;

import org.astrogrid.filemanager.common.exception.FileManagerServiceException ;
import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException ;

import org.astrogrid.filemanager.client.delegate.FileManagerDelegate ;
import org.astrogrid.filemanager.client.delegate.FileManagerMockDelegate ;

/**
 * A resolver for mock filemanagers.
 *
 */
public class FileManagerDelegateResolverMock
    implements FileManagerDelegateResolver
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FileManagerDelegateResolverMock.class);

    /**
     * Public constructor.
     *
     */
    public FileManagerDelegateResolverMock()
        {
        }

    /**
     * Our internal map of services.
     *
     */
    private Map map = new HashMap() ;

    /**
     * Register a new filemanager.
     *
     */
    public void register(FileManagerDelegate delegate)
        throws FileManagerServiceException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("FileManagerDelegateResolverMock.register()") ;
        //
        // Parse the ivorn.
        Ivorn  ivorn = delegate.getServiceIvorn();
        String ident = null ;
        try {
            ident = new FileManagerIvornParser(
                ivorn
                ).getServiceIdent() ;
            }
        catch (FileManagerIdentifierException ouch)
            {
            throw new FileManagerServiceException(
                "Unable to parse service ivorn : " + ivorn.toString()
                ) ;
            }
		//
		// Add the delegate to our map.
        map.put(
            ident,
            delegate
            );
        }

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
        log.debug("----\"----") ;
        log.debug("FileManagerDelegateResolverMock.resolve()") ;
        log.debug("  Ivorn : " + ivorn) ;
        if (null == ivorn)
            {
            throw new IllegalArgumentException(
                "Null service ivorn"
                );
            }
        //
        // Parse the ivorn.
        String ident = null ;
        try {
            ident = new FileManagerIvornParser(
                ivorn
                ).getServiceIdent() ;
            }
        catch (FileManagerIdentifierException ouch)
            {
            throw new FileManagerResolverException(
                "Unable to parse service ivorn : " + ivorn.toString()
                ) ;
            }
        //
        // Lookup the filemanager in our map.
        if (map.containsKey(ident))
            {
            return (FileManagerDelegate) map.get(ident) ;
            }
        else {
            throw new FileManagerResolverException(
                "FileManager not found"
                ) ;
            }
        }
    }

