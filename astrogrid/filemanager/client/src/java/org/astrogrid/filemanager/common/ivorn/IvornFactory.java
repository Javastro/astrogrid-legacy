/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/common/ivorn/IvornFactory.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 13:37:06 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: IvornFactory.java,v $
 *   Revision 1.2  2005/03/11 13:37:06  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.1.2.4  2005/03/01 15:07:36  nw
 *   close to finished now.
 *
 *   Revision 1.1.2.3  2005/02/25 12:33:27  nw
 *   finished transactional store
 *
 *   Revision 1.1.2.2  2005/02/18 15:50:14  nw
 *   lots of changes.
 *   introduced new schema-driven soap binding, got soap-based unit tests
 *   working again (still some failures)
 *
 *   Revision 1.1.2.1  2005/02/11 17:15:12  nw
 *   renamed, made factory static
 *
 *   Revision 1.1.2.2  2005/02/11 14:29:27  nw
 *   simplified
 *
 *   Revision 1.1.2.1  2005/02/10 18:01:06  jdt
 *   Moved common into client.
 *
 *   Revision 1.4.8.1  2005/02/10 16:23:14  nw
 *   formatted code
 *
 *   Revision 1.4  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.3.4.2  2005/01/12 13:16:27  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.3.4.1  2004/12/24 02:05:05  dave
 *   Refactored exception handling, removing IdentifierException from the public API ...
 *
 *   Revision 1.3  2004/12/16 17:25:49  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.1.2.5  2004/11/24 16:15:08  dave
 *   Added node functions to client ...
 *
 *   Revision 1.1.2.4  2004/11/18 17:10:21  dave
 *   Updated mock ivorn handling
 *
 *   Revision 1.1.2.3  2004/11/18 16:06:11  dave
 *   Added delegate resolver and tests ....
 *
 *   Revision 1.1.2.2  2004/11/05 02:23:45  dave
 *   Refactored identifiers are properties ...
 *
 *   Revision 1.1.2.1  2004/11/04 15:50:17  dave
 *   Added ivorn pareser and factory.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common.ivorn;

import org.astrogrid.store.Ivorn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URISyntaxException;

/**
 * A factory for generating filemanager related Ivorn identifiers.
 *  @modified nww renamed methods to describe what they do.
 * @modified nww closed down visibility of the helper methods
 * @modified nww inlined helper methods only used in one place.
 */
public class IvornFactory  {
    /**
     * Commons Logger for this class
     */
    private static final Log log = LogFactory.getLog(IvornFactory.class);

    private IvornFactory() {
    }
    /**
     * Create a filemanager ivorn.
     * 
     * @param base
     *            The base filemanager ivorn.
     * @param path
     *            The node identifier or path.
     * @return A new resource ivorn.
     * @throws URISyntaxException
     * @throws FileManagerIdentifierException
     *             if the filemanager or resource identifiers are invalid or
     *             null.
     *  
     */
    public static Ivorn createIvorn(Ivorn base, String path) throws URISyntaxException{
            return new Ivorn(createIdent(base.toString(), path));

    }
    
    /**
     * Create a filemanager ident.
     * 
     * @param base
     *            The filemanager service ivorn.
     * @param ident
     *            The resource identifier.
     * @return A new (ivorn) identifer.
     * @throws FileManagerIdentifierException
     *             if the filemanager or resource identifiers are null.
     *  
     */
    protected static String createIdent(String base, String ident) {
    
        log.debug("FileManagerIvornFactory.ident(" + base + "," + ident +")");
        //
        // Check for null params.
        if (null == base) {
            throw new IllegalArgumentException("Null service identifier");
        }
        if (null == ident) {
            throw new IllegalArgumentException("Null resource identifier");
        }
        //
        // Put it all together.
        StringBuffer buffer = new StringBuffer();
        //
        // If the service identifier isn't an Ivorn yet.
        if (false == base.startsWith(Ivorn.SCHEME)) {
            buffer.append(Ivorn.SCHEME);
            buffer.append("://");
        }
        buffer.append(base);
        //
        // If the base already has a fragment delimiter.
        if (-1 != base.indexOf('#')) {
            buffer.append("/");
            buffer.append(ident);
        }
        //
        // If the base does not have a fragment delimiter.
        else {
            buffer.append("#");
            buffer.append(ident);
        }
        log.debug("  Result : " + buffer.toString());
        //
        // Return the new string.
        return buffer.toString();
    }


}