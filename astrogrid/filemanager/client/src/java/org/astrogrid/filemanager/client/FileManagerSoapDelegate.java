/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/Attic/FileManagerSoapDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerSoapDelegate.java,v $
 *   Revision 1.3  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.2.4.2  2005/01/12 14:20:57  dave
 *   Replaced tabs with spaces ....
 *
 *   Revision 1.2.4.1  2004/12/22 07:38:36  dave
 *   Started to move towards StoreClient API ...
 *
 *   Revision 1.2  2004/11/25 00:20:29  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.1  2004/11/18 14:39:32  dave
 *   Added SOAP delegate, RemoteException decoding and test case.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.client ;

import java.net.URL;
import java.net.MalformedURLException;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.filemanager.common.FileManager;
import org.astrogrid.filemanager.common.FileManagerConfig;
import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory;

import org.astrogrid.filemanager.common.FileManagerService;
import org.astrogrid.filemanager.common.FileManagerServiceLocator;


import org.astrogrid.filestore.resolver.FileStoreDelegateResolver ;

/**
 * The core implementation for the FileManager delegate.
 *
 */
public class FileManagerSoapDelegate
    extends FileManagerCoreDelegate
    implements FileManagerDelegate
    {
    /**
     * Our debug logger.
     *
     */
    protected static Log log = LogFactory.getLog(FileManagerSoapDelegate.class);

    /**
     * Our FileStore service locator.
     *
     */
    private static FileManagerService locator = new FileManagerServiceLocator() ;

    /**
     * Public constructor, for a specific endpoint URL.
     * @param endpoint The service endpoint URL.
     * @todo Trap MalformedURLException.
     *
     */
    public FileManagerSoapDelegate(String endpoint)
        throws MalformedURLException
        {
        this(
            new URL(
                endpoint
                )
            ) ;
        }

    /**
     * Public constructor, for a specific endpoint URL.
     * @param endpoint The service endpoint URL.
     * @todo Better Exception handling.
     *
     */
    public FileManagerSoapDelegate(URL endpoint)
        {
        super(
            wrapper(
                endpoint
                )
            ) ;
        }

    /**
     * Locate a SOAP client for an endpoint URL.
     *
     */
    private static FileManager wrapper(URL endpoint)
        {
        log.debug("") ;
        log.debug("FileManagerSoapDelegate.wrapper(URL)") ;
        log.debug("  URL : '" + endpoint + "'") ;
        //
        // Check for null param.
        if (null == endpoint)
            {
            throw new IllegalArgumentException(
                "Null endpoint"
                ) ;
            }
        //
        // Try locate our service instance.
        try {
            return locator.getFileManager(
                endpoint
                ) ;
            }
        //
        // Catch anything that went BANG.
        catch (Exception ouch)
            {
            // TODO
            // Log the Exception, and throw a nicer one.
            // Unwrap RemoteExceptions.
            //
            return null ;
            }
        }
    }
