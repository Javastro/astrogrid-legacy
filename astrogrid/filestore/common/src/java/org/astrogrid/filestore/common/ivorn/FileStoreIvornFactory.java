/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/ivorn/FileStoreIvornFactory.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/23 09:11:16 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: FileStoreIvornFactory.java,v $
 *   Revision 1.2  2004/07/23 09:11:16  dave
 *   Merged development branch, dave-dev-200407221513, into HEAD
 *
 *   Revision 1.1.2.1  2004/07/23 02:10:58  dave
 *   Added IvornFactory and IvornParser
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common.ivorn ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;

import java.net.URISyntaxException ;

/**
 * A factory for generating filestore related Ivorn identifiers.
 *
 */
public class FileStoreIvornFactory
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Create a filestore ivorn.
     * @param  service The filestore service identifier.
     * @param  resource  The resource identifier.
     * @return A new ivorn.
     * @throws FileStoreIdentifierException if the filestore or resource identifiers are null.
     *
     */
    protected static Ivorn createIvorn(String service, String resource)
        throws FileStoreIdentifierException
		{
		try {
			return new Ivorn(
				createIdent(
					service,
					resource
					)
				) ;
			}
		catch (URISyntaxException ouch)
			{
			throw new FileStoreIdentifierException(
				ouch
				) ;
			}
		}

    /**
     * Create a filestore ident.
     * @param  service The filestore service identifier.
     * @param  resource  The resource identifier.
     * @return A new identifer.
     * @throws FileStoreIdentifierException if the filestore or resource identifiers are null.
     *
     */
    protected static String createIdent(String service, String resource)
        throws FileStoreIdentifierException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("FilestoreIvornFactory.createIdent()") ;
        if (DEBUG_FLAG) System.out.println("  Service  : " + service) ;
        if (DEBUG_FLAG) System.out.println("  Resource : " + resource) ;
        //
        // Check for null params.
        if (null == service)
        	{
        	throw new FileStoreIdentifierException(
        		"Null service identifier"
        		) ;
        	}
        if (null == resource)
        	{
        	throw new FileStoreIdentifierException(
        		"Null resource identifier"
        		) ;
        	}
        //
        // Put it all together.
        StringBuffer buffer = new StringBuffer() ;
        //
        // If the service identifier isn't an Ivorn yet.
        if (false == service.startsWith(Ivorn.SCHEME))
            {
            buffer.append(Ivorn.SCHEME) ;
            buffer.append("://") ;
            }
        buffer.append(service) ;
        buffer.append("#") ;
        buffer.append(resource) ;
        if (DEBUG_FLAG) System.out.println("  Result   : " + buffer.toString()) ;
        //
        // Return the new string.
        return buffer.toString() ;
        }
    }
