/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/ivorn/Attic/FileManagerIvornParser.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: FileManagerIvornParser.java,v $
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
package org.astrogrid.filemanager.common.ivorn ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URI ;
import java.net.URISyntaxException ;

import java.util.regex.Matcher ;
import java.util.regex.Pattern ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filemanager.common.exception.FileManagerServiceException ;
import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException ;

/**
 * A parser for handling Filemanager identifiers.
 *
 */
public class FileManagerIvornParser
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FileManagerIvornParser.class);

    /**
     * The mock service identifier.
     * Used to create mock ivorns in JUnit tests.
     *
     */
    public static String MOCK_SERVICE_IDENT = "org.astrogrid.mock" ;

    /**
     * Our AstroGrid configuration.
     *
    protected static Config config = SimpleConfig.getSingleton() ;
     */

    /**
     * Public constructor for a specific Ivorn.
     * @param ivorn A vaild Ivorn identifier.
     * @throws FileManagerIdentifierException If the identifier is not valid.
     *
     */
    public FileManagerIvornParser(Ivorn ivorn)
        throws FileManagerIdentifierException
        {
        this.setIvorn(ivorn) ;
        }

    /**
     * Public constructor for a specific identifier.
     * @param ident A vaild identifier.
     * @throws FileManagerIdentifierException If the identifier is not valid.
     *
     */
    public FileManagerIvornParser(String ident)
        throws FileManagerIdentifierException
        {
        this.setIvorn(
            parse(ident)
            ) ;
        }

    /**
     * Our target Ivorn.
     *
     */
    private Ivorn ivorn ;

    /**
     * Our corresponding URI.
     *
     */
    private URI uri ;

    /**
     * The service ident.
     *
     */
    private String service ;

    /**
     * The resource ident.
     *
     */
    private String resource ;

    /**
     * Get our target Ivorn.
     *
     */
    public Ivorn getIvorn()
        {
        return this.ivorn ;
        }

    /**
     * Convert a string identifier into an Ivorn.
     * @param ident The identifier.
     * @return a new Ivorn containing the identifier.
     * @throws FileManagerIdentifierException If the identifier is not a valid ivorn.
     *
     */
    protected static Ivorn parse(String ident)
        throws FileManagerIdentifierException
        {
        if (null == ident)
            {
            throw new FileManagerIdentifierException(
                "Null identifier"
                ) ;
            }
        try {
            return new Ivorn(ident) ;
            }
        catch (URISyntaxException ouch)
            {
            throw new FileManagerIdentifierException(
                ouch
                ) ;
            }
        }

    /**
     * Set our target Ivorn.
     * @param A vaild Ivorn identifier.
     * @throws FileManagerIdentifierException If the identifier is not valid.
     *
     */
    protected void setIvorn(Ivorn ivorn)
        throws FileManagerIdentifierException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("FilemanagerIvornParser.setIvorn()") ;
        log.debug("  Ivorn : " + ivorn) ;
        //
        // Check for null param.
        if (null == ivorn)
            {
            throw new FileManagerIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Save the ivorn.
        this.ivorn    = ivorn ;
        //
        // Reset everything else.
        this.uri      = null  ;
        this.service  = null ;
        this.resource = null ;
        //
        // Try convert it into a URI.
        try {
            this.uri = new URI(ivorn.toString()) ;
            //
            // Parse the filemanager ident.
            this.parseServiceIdent() ;
            //
            // Parse resource ident.
            this.parseResourceIdent() ;
            }
        //
        // All Ivorn should be valid URI.
        catch (URISyntaxException ouch)
            {
            throw new FileManagerIdentifierException(
                ouch
                ) ;
            }
        }

    /**
     * Parse the filemanager ident.
     * @throws FileManagerIdentifierException If the identifier is not a valid ivorn.
     *
     */
    protected void parseServiceIdent()
        throws FileManagerIdentifierException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("FilemanagerIvornParser.parseServiceIdent()") ;
        log.debug("  Ivorn    : " + ivorn) ;
        if (null != uri)
            {
            String auth = uri.getAuthority() ;
            String path = uri.getPath() ;
            log.debug("  Auth     : " + auth) ;
            log.debug("  Path     : " + path) ;
            //
            // If the URI has an authority ident.
            if (null != auth)
                {
                //
                // If the authority ident isn't empty.
                if (auth.length() > 0)
                    {
                    //
                    // Start with the URI authority.
                    this.service = auth ;
                    //
                    // If we have a path.
                    if (null != path)
                        {
                        if (path.length() > 1)
                            {
                            StringBuffer buffer =
                                new StringBuffer(
                                    auth
                                    ) ;
                            buffer.append(path) ;
                            this.service = buffer.toString() ;
                            }
                        }
                    }
                //
                // If the authority ident is empty.
                else {
                    throw new FileManagerIdentifierException(
                        "Invalid identifier",
                        uri.toString()
                        ) ;
                    }
                }
            //
            // If the URI does not have an authority ident.
            else {
                throw new FileManagerIdentifierException(
                    "Invalid identifier",
                    uri.toString()
                    ) ;
                }
            }
        log.debug("  Service  : " + this.service) ;
        }

    /**
     * Parse the resource ident.
     *
     */
    protected void parseResourceIdent()
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("FilemanagerIvornParser.parseResourceIdent()") ;
        log.debug("  Ivorn    : " + ivorn) ;
        if (null != uri)
            {
            this.resource = uri.getFragment() ;
            }
        log.debug("  Resource : " + this.resource) ;
        }

    /**
     * Get the filemanager ident as a string.
     * @return The filemanager ident, or null if no match was found.
     *
     */
    public String getServiceIdent()
        {
        return this.service ;
        }

    /**
     * Get the filemanager ident as an ivorn.
     * @return The filemanager ivorn, or null if no match was found.
     * @throws FileManagerIdentifierException If the service identifier is not valid.
     *
     */
    public Ivorn getServiceIvorn()
        throws FileManagerIdentifierException
        {
        try {
            StringBuffer buffer = new StringBuffer() ;
            buffer.append(Ivorn.SCHEME) ;
            buffer.append("://") ;
            buffer.append(this.service) ;
            return new Ivorn(
                buffer.toString()
                ) ;
            }
        catch (URISyntaxException ouch)
            {
            throw new FileManagerIdentifierException(
                ouch
                ) ;
            }
        }

    /**
     * Get the resource ident as a string.
     * @return The resource ident, or null if no match was found.
     *
     */
    public String getResourceIdent()
        {
        return this.resource ;
        }

    /**
     * Convert the parser to a String.
     *
     */
    public String toString()
        {
        return "FilemanagerIvornParser : " + ((null != ivorn) ? ivorn.toString() : null) ;
        }

    /**
     * Check if this is a mock ivorn.
     * @return true if this is a mock ivorn.
     *
     */
    public boolean isMock()
        {
        if (null != this.service)
            {
            return this.service.startsWith(
                MOCK_SERVICE_IDENT
                ) ;
            }
        else {
            return false ;
            }
        }
    }
