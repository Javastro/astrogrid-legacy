/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/client/src/java/org/astrogrid/filestore/client/FileStoreSoapDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/19 23:42:07 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreSoapDelegate.java,v $
 *   Revision 1.3  2004/07/19 23:42:07  dave
 *   Merged development branch, dave-dev-200407151443, into HEAD
 *
 *   Revision 1.2.4.1  2004/07/19 19:40:28  dave
 *   Debugged and worked around Axis Exception handling
 *
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.1.2.1  2004/07/06 10:49:45  dave
 *   Added SOAP delegate
 *
 *   Revision 1.1.2.1  2004/07/06 09:16:12  dave
 *   Added delegate interface and mock implementation
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.client ;

import java.net.URL ;
import java.net.MalformedURLException ;

import org.astrogrid.filestore.common.FileStore ;
import org.astrogrid.filestore.common.FileStoreMock ;
import org.astrogrid.filestore.common.FileStoreService ;
import org.astrogrid.filestore.common.FileStoreServiceLocator ;

/**
 * A SOAP client implementation of the delegate interface.
 *
 */
public class FileStoreSoapDelegate
	extends FileStoreCoreDelegate
	{
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Our FileStore service locator.
     *
     */
    private FileStoreService locator = new FileStoreServiceLocator() ;

    /**
     * Our endpoint address.
     *
     */
    private URL endpoint ;

    /**
     * Get our endpoint address.
     *
     */
    public URL getEndpoint()
        {
        return this.endpoint ;
        }

    /**
     * Public constructor, for a specific endpoint URL.
     * @param endpoint The service endpoint URL.
     * @todo Trap null param.
     * @todo Trap MalformedURLException.
     *
     */
    public FileStoreSoapDelegate(String endpoint)
        throws MalformedURLException
        {
        this(new URL(endpoint)) ;
        }

    /**
     * Public constructor, for a specific endpoint URL.
     * @param endpoint The service endpoint URL.
     * @todo Better Exception handling.
     * @todo Trap null param.
     *
     */
    public FileStoreSoapDelegate(URL endpoint)
        {
        super() ;
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("FileStoreSoapDelegate()") ;
        if (DEBUG_FLAG) System.out.println("FROG") ;
        if (DEBUG_FLAG) System.out.println("  URL : " + endpoint) ;
        //
        // Check for null param.
        if (null == endpoint)
            {
            throw new IllegalArgumentException(
                "Null endpoint"
                ) ;
            }
        //
        // Set our endpoint address.
        this.endpoint = endpoint ;
        //
        // Try locate our service instance.
        try {
            this.service = locator.getFileStore(
	            this.endpoint
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
            }
        }
	}

