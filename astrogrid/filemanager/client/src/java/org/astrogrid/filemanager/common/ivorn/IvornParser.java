/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/common/ivorn/IvornParser.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 13:37:06 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: IvornParser.java,v $
 *   Revision 1.2  2005/03/11 13:37:06  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
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
 *   Revision 1.3.8.1  2005/02/10 16:23:14  nw
 *   formatted code
 *
 *   Revision 1.3  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.2.4.1  2005/01/12 13:16:27  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.2  2004/11/25 00:20:29  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.2  2004/11/16 03:25:37  dave
 *   Updated API to use full ivorn rather than ident ...
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

import java.net.URI;
import java.net.URISyntaxException;

/**
 * A parser for handling Filemanager identifiers.
 * 
 * @modified nww inlined single-use helper methods - then able to make all
 *                   fields final
 * 
 * @modified nww inverted some conditionals - makes logic clearer
 */
public class IvornParser {
    /**
     * Our debug logger.
     *  
     */
    private static Log log = LogFactory.getLog(IvornParser.class);

    /**
     * The mock service identifier. Used to create mock ivorns in JUnit tests.
     *  
     */
    public static String MOCK_SERVICE_IDENT = "org.astrogrid.mock";

    /**
     * Our AstroGrid configuration.
     * 
     * protected static Config config = SimpleConfig.getSingleton() ;
     */

    /**
     * Public constructor for a specific Ivorn.
     * 
     * @param ivorn
     *                    A vaild Ivorn identifier.
     * @throws URISyntaxException
     * @throws FileManagerIdentifierException
     *                     If the identifier is not valid.
     *  
     */
    public IvornParser(Ivorn ivorn) throws URISyntaxException {
        log.debug("FilemanagerIvornParser(" + ivorn + ")");
        if (null == ivorn) {
            throw new IllegalArgumentException("Null identifier");
        }
        this.ivorn = ivorn;

            this.uri = new URI(ivorn.toString());            
 
        if (this.uri == null) {
            throw new URISyntaxException("<null>","Null uri");
        }
        String auth = uri.getAuthority();
        String path = uri.getPath();
        log.debug("  Auth     : " + auth + "  Path     : " + path);
        if (auth == null || auth.trim().length() == 0) {
            throw new URISyntaxException(uri.toString(),"Invalid identifier ");
        }

        // If we have a path.
        if (null != path && path.length() > 1) {
            //@todo shouldn't this be >0??
            this.service = auth + path;
        } else {
            this.service = auth;
        }
        this.resource = this.uri.getFragment();
        log.debug(this.toString());

    }

    /**
     * Public constructor for a specific identifier.
     * 
     * @param ident
     *                    A vaild identifier.
     * @throws URISyntaxException
     * @throws FileManagerIdentifierException
     *                     If the identifier is not valid.
     *  
     */
    public IvornParser(String ident) throws URISyntaxException{
        this(parse(ident));
    }

    /**
     * Our target Ivorn.
     *  
     */
    private final Ivorn ivorn;

    /**
     * Our corresponding URI.
     *  
     */
    private final URI uri;

    /**
     * The service ident.
     *  
     */
    private final String service;

    /**
     * The resource ident.
     *  
     */
    private final String resource;

    /**
     * Get our target Ivorn.
     *  
     */
    public Ivorn getIvorn() {
        return this.ivorn;
    }

    /**
     * Convert a string identifier into an Ivorn.
     * 
     * @param ident
     *                    The identifier.
     * @return a new Ivorn containing the identifier.
     * @throws URISyntaxException
     * @throws FileManagerIdentifierException
     *                     If the identifier is not a valid ivorn.
     *  
     */
    protected static Ivorn parse(String ident) throws URISyntaxException{
        if (null == ident) {
            throw new IllegalArgumentException("Null identifier");
        }
            return new Ivorn(ident);
    }

    /**
     * Get the filemanager ident as a string.
     * 
     * @return The filemanager ident, or null if no match was found.
     *  
     */
    public String getServiceIdent() {
        return this.service;
    }

    /**
     * Get the filemanager ident as an ivorn.
     * 
     * @return The filemanager ivorn, or null if no match was found.
     * @throws URISyntaxException
     * @throws FileManagerIdentifierException
     *                     If the service identifier is not valid.
     *  
     */
    public Ivorn getServiceIvorn() throws URISyntaxException  {
            StringBuffer buffer = new StringBuffer();
            buffer.append(Ivorn.SCHEME);
            buffer.append("://");
            buffer.append(this.service);
            return new Ivorn(buffer.toString());

    }

    /**
     * Get the resource ident as a string.
     * 
     * @return The resource ident, or null if no match was found.
     *  
     */
    public String getResourceIdent() {
        return this.resource;
    }

    /**
     * Check if this is a mock ivorn.
     * 
     * @return true if this is a mock ivorn.
     *  
     */
    public boolean isMock() {
        if (null != this.service) {
            return this.service.startsWith(MOCK_SERVICE_IDENT);
        } else {
            return false;
        }
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FileManagerIvornParser:");
        buffer.append(" ivorn: ");
        buffer.append(ivorn);
        buffer.append(" uri: ");
        buffer.append(uri);
        buffer.append(" service: ");
        buffer.append(service);
        buffer.append(" resource: ");
        buffer.append(resource);
        buffer.append("]");
        return buffer.toString();
    }
}