/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/ivorn/FileStoreIvornFactory.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/17 06:57:10 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: FileStoreIvornFactory.java,v $
 *   Revision 1.4  2004/09/17 06:57:10  dave
 *   Added commons logging to FileStore.
 *   Updated logging properties in Community.
 *   Fixed bug in AGINAB deployment.
 *   Removed MySpace tests with hard coded grendel address.
 *
 *   Revision 1.3.32.1  2004/09/17 01:08:36  dave
 *   Updated debug to use commons logging API ....
 *
 *   Revision 1.3  2004/08/18 19:00:01  dave
 *   Myspace manager modified to use remote filestore.
 *   Tested before checkin - integration tests at 91%.
 *
 *   Revision 1.2.10.2  2004/08/02 14:54:15  dave
 *   Trying to get integration tests to run
 *
 *   Revision 1.2.10.1  2004/07/28 03:00:17  dave
 *   Refactored resolver constructors and added mock ivorn
 *
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

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

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
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FileStoreIvornFactory.class);

    /**
     * Create a filestore ivorn.
     * @param  service The filestore service identifier.
     * @param  resource  The resource identifier.
     * @return A new ivorn.
     * @throws FileStoreIdentifierException if the filestore or resource identifiers are invalid or null.
     *
     */
    public static Ivorn createIvorn(String service, String resource)
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
     * Create a mock filestore ivorn.
     * @param  resource  The resource identifier.
     * @return A new mock ivorn.
     * @throws FileStoreIdentifierException if the filestore or resource identifiers are null.
     *
     */
    public static Ivorn createMock(String resource)
        throws FileStoreIdentifierException
		{
		try {
			return new Ivorn(
				createIdent(
					FileStoreIvornParser.MOCK_SERVICE_IDENT,
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
    public static String createIdent(String service, String resource)
        throws FileStoreIdentifierException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("FilestoreIvornFactory.createIdent()") ;
        log.debug("  Service  : " + service) ;
        log.debug("  Resource : " + resource) ;
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
        log.debug("  Result   : " + buffer.toString()) ;
        //
        // Return the new string.
        return buffer.toString() ;
        }
    }
