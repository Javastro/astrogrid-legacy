/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/ivorn/FileStoreIvornParser.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/08/18 19:00:01 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: FileStoreIvornParser.java,v $
 *   Revision 1.3  2004/08/18 19:00:01  dave
 *   Myspace manager modified to use remote filestore.
 *   Tested before checkin - integration tests at 91%.
 *
 *   Revision 1.2.10.2  2004/08/09 15:43:00  dave
 *   Fixed bug in ivorn parser
 *
 *   Revision 1.2.10.1  2004/07/28 03:00:17  dave
 *   Refactored resolver constructors and added mock ivorn
 *
 *   Revision 1.2  2004/07/23 09:11:16  dave
 *   Merged development branch, dave-dev-200407221513, into HEAD
 *
 *   Revision 1.1.2.4  2004/07/23 03:55:38  dave
 *   Added getServiceIvorn to parser
 *
 *   Revision 1.1.2.3  2004/07/23 03:37:06  dave
 *   Debugged tests and parser bugs
 *
 *   Revision 1.1.2.2  2004/07/23 03:08:37  dave
 *   Updated parser tests
 *
 *   Revision 1.1.2.1  2004/07/23 02:10:58  dave
 *   Added IvornFactory and IvornParser
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common.ivorn ;

import java.net.URI ;
import java.net.URISyntaxException ;

import java.util.regex.Matcher ;
import java.util.regex.Pattern ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.config.Config ;
import org.astrogrid.config.SimpleConfig ;
import org.astrogrid.config.PropertyNotFoundException ;

import org.astrogrid.filestore.common.exception.FileStoreServiceException ;
import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;

/**
 * A parser for handling Filestore identifiers.
 *
 */
public class FileStoreIvornParser
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

	/**
	 * The mock service identifier.
	 * Used to create mock ivorns in JUnit tests.
	 *
	 */
	public static String MOCK_SERVICE_IDENT = "org.astrogrid.mock" ;

    /**
     * Our AstroGrid configuration.
     *
     */
    protected static Config config = SimpleConfig.getSingleton() ;

    /**
     * Public constructor for a specific Ivorn.
     * @param ivorn A vaild Ivorn identifier.
     * @throws FileStoreIdentifierException If the identifier is not valid.
     *
     */
    public FileStoreIvornParser(Ivorn ivorn)
        throws FileStoreIdentifierException
        {
        this.setIvorn(ivorn) ;
        }

    /**
     * Public constructor for a specific identifier.
     * @param ident A vaild identifier.
     * @throws FileStoreIdentifierException If the identifier is not valid.
     *
     */
    public FileStoreIvornParser(String ident)
        throws FileStoreIdentifierException
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
     * @throws FileStoreIdentifierException If the identifier is not a valid ivorn.
     *
     */
    protected static Ivorn parse(String ident)
        throws FileStoreIdentifierException
        {
        if (null == ident)
            {
            throw new FileStoreIdentifierException(
                "Null identifier"
                ) ;
            }
        try {
            return new Ivorn(ident) ;
            }
        catch (URISyntaxException ouch)
            {
            throw new FileStoreIdentifierException(
                ouch
                ) ;
            }
        }

    /**
     * Set our target Ivorn.
     * @param A vaild Ivorn identifier.
     * @throws FileStoreIdentifierException If the identifier is not valid.
     *
     */
    protected void setIvorn(Ivorn ivorn)
        throws FileStoreIdentifierException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("FilestoreIvornParser.setIvorn()") ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
        //
        // Check for null param.
        if (null == ivorn)
            {
            throw new FileStoreIdentifierException(
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
            // Parse the filestore ident.
            this.parseServiceIdent() ;
            //
            // Parse resource ident.
            this.parseResourceIdent() ;
            }
        //
        // All Ivorn should be valid URI.
        catch (URISyntaxException ouch)
            {
            throw new FileStoreIdentifierException(
                ouch
                ) ;
            }
        }

    /**
     * Parse the filestore ident.
     * @throws FileStoreIdentifierException If the identifier is not a valid ivorn.
     *
     */
    protected void parseServiceIdent()
		throws FileStoreIdentifierException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("FilestoreIvornParser.parseServiceIdent()") ;
        if (DEBUG_FLAG) System.out.println("  Ivorn    : " + ivorn) ;
        if (null != uri)
            {
			String auth = uri.getAuthority() ;
			String path = uri.getPath() ;
			if (DEBUG_FLAG) System.out.println("  Auth     : " + auth) ;
			if (DEBUG_FLAG) System.out.println("  Path     : " + path) ;
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
					throw new FileStoreIdentifierException(
						"Invalid identifier",
						uri.toString()
						) ;
					}
				}
			//
			// If the URI does not have an authority ident.
			else {
				throw new FileStoreIdentifierException(
					"Invalid identifier",
					uri.toString()
					) ;
				}
			}
		if (DEBUG_FLAG) System.out.println("  Service  : " + this.service) ;
		}

    /**
     * Parse the resource ident.
     *
     */
    protected void parseResourceIdent()
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("FilestoreIvornParser.parseResourceIdent()") ;
        if (DEBUG_FLAG) System.out.println("  Ivorn    : " + ivorn) ;
        if (null != uri)
            {
            this.resource = uri.getFragment() ;
            }
        if (DEBUG_FLAG) System.out.println("  Resource : " + this.resource) ;
        }

    /**
     * Get the filestore ident as a string.
     * @return The filestore ident, or null if no match was found.
     *
     */
    public String getServiceIdent()
        {
        return this.service ;
        }

    /**
     * Get the filestore ident as an ivorn.
     * @return The filestore ivorn, or null if no match was found.
	 * @throws FileStoreIdentifierException If the service identifier is not valid.
     *
     */
    public Ivorn getServiceIvorn()
		throws FileStoreIdentifierException
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
			throw new FileStoreIdentifierException(
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
        return "FilestoreIvornParser : " + ((null != ivorn) ? ivorn.toString() : null) ;
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
