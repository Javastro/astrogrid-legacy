/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/common/ivorn/MockIvorn.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: MockIvorn.java,v $
 *   Revision 1.7  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.6.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.6  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.5.36.1  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.common.ivorn ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URI ;
import java.net.URISyntaxException ;

import org.astrogrid.store.Ivorn ;

/**
 * A factory for mock Ivorn identifiers.
 * Extends the basic Ivorn, but makes sure that the authority starts with a fixed prefix, 'org.astrogrid.mock'.
 * Our delegate factories can use this to create mock delegates.
 * @todo Refactor this as a factory rather than extends Ivorn.
 * @todo Refactor this to be consistent with the other Ivorn factories.
 *
 */
public class MockIvorn
    extends Ivorn
    {

    /**
     * The default mock identifier.
     * If an Ivorn matches this ident, delegate factories should generate a mock delegate.
     *
     */
    public static final String MOCK_IDENT = "org.astrogrid.mock" ;

    /**
     * Create a mock Ivorn.
     * @param ident The ivo ident, with no extra fields.
     *
     */
    public MockIvorn(String ident)
        throws URISyntaxException
        {
        super(
            createIdent(ident)
            ) ;
        }

    /**
     * Create a mock Ivorn.
     * @param ident The ivo ident, with no extra fields.
     * @param path  The path, added after the ident.
     *
     */
    public MockIvorn(String ident, String path)
        throws URISyntaxException
        {
        super(
            createIdent(ident, path)
            ) ;
        }

    /**
     * Create a mock Ivorn.
     * @param ident The ivo ident, with no extra fields.
     * @param path  The path, added after the ident.
     * @param fragment  The URI fragment string.
     *
     */
    protected MockIvorn(String ident, String path, String fragment)
        throws URISyntaxException
        {
        super(
            createIdent(ident, path, fragment)
            ) ;
        }

    /**
     * Create a mock ident.
     * @param ident The ivo ident, with no extra fields.
     *
     */
    protected static String createIdent(String ident)
        {
        return createIdent(ident, null, null, null) ;
        }

    /**
     * Create a mock ident.
     * @param ident The ivo ident, with no extra fields.
     * @param path  The path, added after the ident.
     *
     */
    protected static String createIdent(String ident, String path)
        {
        return createIdent(ident, path, null, null) ;
        }

    /**
     * Create a mock ident.
     * @param ident     The ivo ident, with no extra fields.
     * @param path      The path, added after the ident.
     * @param fragment  The URI fragment string.
     *
     */
    protected static String createIdent(String ident, String path, String fragment)
        {
        return createIdent(ident, path, null, fragment) ;
        }

    /**
     * Create a mock ident.
     * This allows you to set almost all of the URI fields.
     * @param ident     The ivo ident, with no extra fields.
     * @param path      The path, added after the ident.
     * @param query     The URI query string.
     * @param fragment  The URI fragment string.
     *
     */
    protected static String createIdent(String ident, String path, String query, String fragment)
        {
        //
        // Put it all back together.
        StringBuffer buffer = new StringBuffer() ;
        //
        // Start with the ivo scheme.
        buffer.append(Ivorn.SCHEME) ;
        buffer.append("://") ;
        //
        // If we have an ident.
        if (null != ident)
            {
            //
            // If the ident is not empty.
            if (ident.length() > 0)
                {
                //
                // If the ident is not a mock ident
                if (false == ident.startsWith(MOCK_IDENT))
                    {
                    //
                    // Add the mock ident to the beginning.
                    buffer.append(MOCK_IDENT) ;
                    buffer.append(".") ;
                    }
                buffer.append(ident) ;
                }
            //
            // If the ident is empty.
            else {
                //
                // Use the mock ident instead.
                buffer.append(MOCK_IDENT) ;
                }
            }
        //
        // If we don't have an ident.
        else {
            //
            // Use the mock ident.
            buffer.append(MOCK_IDENT) ;
            }
        //
        // Add the rest of the elements.
        if (null != path)
            {
            buffer.append("/") ;
            buffer.append(path) ;
            }
        if (null != query)
            {
            buffer.append("?") ;
            buffer.append(query) ;
            }
        if (null != fragment)
            {
            buffer.append("#") ;
            buffer.append(fragment) ;
            }
        //
        // Return the new string.
        return buffer.toString() ;
        }

    /**
     * Returns true if the Ivorn is a mock identifier.
     * If this works, then it should probably be move to org.astrogrid.store.Ivorn.
     *
     */
    public static boolean isMock(Ivorn ivorn)
        {
        //
        // Convert the Ivorn into a String.
        return isMock(
            ivorn.toString()
            ) ;
        }

    /**
     * Returns true if the String is a mock identifier.
     * If this works, then it should probably be move to org.astrogrid.store.Ivorn.
     *
     */
    public static boolean isMock(String string)
        {
        //
        // Convert the String into a URI.
        try {
            return isMock(
                new URI(string)
                ) ;
            }
        //
        // If it isn't valid, then it isn't a valid mock either.
        catch (URISyntaxException ouch)
            {
            return false ;
            }
        }

    /**
     * Returns true if the URI is a mock identifier.
     * If this works, then it should probably be move to org.astrogrid.store.Ivorn.
     *
     */
    public static boolean isMock(URI uri)
        {
        //
        // Check if the host part starts with the mock ident.
        return (null != uri.getHost()) ? uri.getHost().startsWith(MOCK_IDENT) : false ;
        }
    }
