/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/client/src/java/org/astrogrid/filestore/resolver/FileStoreEndpointResolver.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/23 09:11:16 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: FileStoreEndpointResolver.java,v $
 *   Revision 1.2  2004/07/23 09:11:16  dave
 *   Merged development branch, dave-dev-200407221513, into HEAD
 *
 *   Revision 1.1.2.3  2004/07/23 04:29:27  dave
 *   Initial resolver test
 *
 *   Revision 1.1.2.2  2004/07/23 04:09:02  dave
 *   Fixed resolver bugs
 *
 *   Revision 1.1.2.1  2004/07/23 03:09:00  dave
 *   Added new resolver
 *
 *   Revision 1.1.2.1  2004/07/23 02:10:58  dave
 *   Added IvornFactory and IvornParser
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.resolver ;

import java.net.URL ;
import java.net.MalformedURLException ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.registry.client.RegistryDelegateFactory ;
import org.astrogrid.registry.client.query.RegistryService ;

import org.astrogrid.config.Config ;
import org.astrogrid.config.SimpleConfig ;

import org.astrogrid.filestore.common.ivorn.FileStoreIvornParser ;
import org.astrogrid.filestore.common.ivorn.FileStoreIvornFactory ;

import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;

import org.astrogrid.registry.RegistryException;

/**
 * This is a local wrapper for the RegistryDelegate resolve Ivorns into service endpoints.
 *
 */
public class FileStoreEndpointResolver
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Our reference to the AstroGrid config.
     *
     */
    protected static Config conf = SimpleConfig.getSingleton() ;

    /**
     * Public constructor, using the default Registry service.
     *
     */
    public FileStoreEndpointResolver()
        {
        //
        // Initialise our default registry delegate.
        this.registry = factory.createQuery() ;
        }

    /**
     * Public constructor, for a specific Registry service.
     * @param registry The endpoint address for our RegistryDelegate.
     *
     */
    public FileStoreEndpointResolver(URL registry)
        {
		//
		// Check for null URL.
		if (null == registry)
			{
            throw new IllegalArgumentException(
                "Null registry URL"
                ) ;
			}
        //
        // Initialise our registry delegate.
        this.registry = factory.createQuery(registry) ;
        }

    /**
     * Our Registry delegate factory.
     *
     */
    private static RegistryDelegateFactory factory = new RegistryDelegateFactory() ;

    /**
     * Our Registry delegate.
     *
     */
    private RegistryService registry ;

    /**
     * Resolve an Ivorn into a service endpoint.
     * @param ivorn An Ivorn containing a filestore identifier.
     * @return The endpoint address for the service.
     * @throws FileStoreIdentifierException If the identifier is not valid.
     * @throws FileStoreResolverException If unable to resolve the identifier.
     * @throws RegistryException If the Registry is unable to handle the identifier.
     *
     */
    public URL resolve(Ivorn ivorn)
        throws RegistryException, FileStoreIdentifierException, FileStoreResolverException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("FileStoreEndpointResolver.resolve()") ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
        //
        // Check for null ivorn.
        if (null == ivorn)
            {
            throw new FileStoreIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Parse the ivorn and resolve it.
        return this.resolve(
            new FileStoreIvornParser(ivorn)
            ) ;
        }

    /**
     * Resolve data from a FileStoreIvornParser.
     * @param parser A FileStoreIvornParser containing the Filestore identifier.
     * @return The endpoint address for the service.
     * @throws FileStoreIdentifierException If the identifier is not valid.
     * @throws FileStoreResolverException If unable to resolve the identifier.
     * @throws RegistryException If the Registry is unable to handle the identifier.
     *
     */
    public URL resolve(FileStoreIvornParser parser)
        throws RegistryException, FileStoreIdentifierException, FileStoreResolverException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("FileStoreEndpointResolver.resolve()") ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ((null != parser) ? parser.getIvorn() : null)) ;
        //
        // Check for null parser.
        if (null == parser)
            {
            throw new FileStoreIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Check for null service.
        if (null == parser.getServiceIdent())
            {
            throw new FileStoreIdentifierException(
                "Null community identifier"
                ) ;
            }
        //
        // Get our service Ivorn.
        Ivorn ivorn  = parser.getServiceIvorn() ;
        //
        // Lookup the service in the registry.
        String endpoint = null ;
        try {
            endpoint = registry.getEndPointByIdentifier(
                ivorn.toString()
                ) ;
            }
        catch (Throwable ouch)
            {
            if (DEBUG_FLAG) System.out.println("FAIL : Registry lookup failed")  ;
            if (DEBUG_FLAG) System.out.println("  Exception : " + ouch)  ;
            throw new FileStoreResolverException(
                "Registry lookup failed",
                ouch
                ) ;
            }
        //
        // If we found an entry in the Registry.
        if (null != endpoint)
            {
            if (DEBUG_FLAG) System.out.println("PASS : Got service endpoint")  ;
            if (DEBUG_FLAG) System.out.println("  Endpoint : " + endpoint)  ;
            //
            // Convert it into an endpoint URL.
            try {
                return new URL(endpoint) ;
                }
            //
            // Report the problem in a Exception.
            catch (MalformedURLException ouch)
                {
                throw new FileStoreResolverException(
                    "Unable to parse Registry response into endpoint URL",
                    ivorn
                    ) ;
                }
            }
        //
        // If we didn't get a service endpoint.
        else {
            //
            // Report the problem in a Exception.
            throw new FileStoreResolverException(
                "Registry returned null endpoint address for ivorn",
                ivorn
                ) ;
            }
        }
    }

