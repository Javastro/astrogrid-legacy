/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/client/src/java/org/astrogrid/filestore/resolver/FileStoreEndpointResolverImpl.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:19:21 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: FileStoreEndpointResolverImpl.java,v $
 *   Revision 1.2  2004/11/25 00:19:21  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.2  2004/11/09 17:41:36  dave
 *   Added file:// URL handling to allow server URLs to be tested.
 *   Added importInit and exportInit to server implementation.
 *   Moved remaining tests out of extended test abd removed it.
 *
 *   Revision 1.1.2.1  2004/10/19 14:56:15  dave
 *   Refactored config and resolver to enable multiple instances of mock implementation.
 *   Required to implement handling of multiple FileStore(s) in FileManager.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.resolver ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URL ;
import java.net.MalformedURLException ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.RegistryDelegateFactory ;
import org.astrogrid.registry.client.query.RegistryService ;

import org.astrogrid.filestore.common.ivorn.FileStoreIvornParser ;
import org.astrogrid.filestore.common.ivorn.FileStoreIvornFactory ;

import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;


/**
 * A helper class to resolve an Ivron into a service endpoint.
 *
 */
public class FileStoreEndpointResolverImpl
	implements FileStoreEndpointResolver
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FileStoreEndpointResolverImpl.class);

    /**
     * Public constructor, using the default Registry service.
     *
     */
    public FileStoreEndpointResolverImpl()
        {
		this(
			(URL) null
			) ;
        }

    /**
     * Public constructor, for a specific Registry service.
     * @param endpoint The endpoint address for our RegistryDelegate.
     *
     */
    public FileStoreEndpointResolverImpl(URL registry)
		{
		this(
			registry,
			new RegistryDelegateFactory()
			) ;
		}

    /**
     * Public constructor, for a specific Registry service.
     * @param registry The endpoint address for our registry service.
     * @param factory A factory to create our registry delegate.
     *
     */
    public FileStoreEndpointResolverImpl(URL registry, RegistryDelegateFactory factory)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("FileStoreEndpointResolverImpl()") ;
        log.debug("  Registry : " + registry) ;
		if (null == factory)
			{
			throw new IllegalArgumentException(
				"Null registry delegate factory"
				) ;
			}
		if (null == registry)
			{
	        this.registry = factory.createQuery() ;
			}
		else {
	        this.registry = factory.createQuery(registry) ;
			}
        }

    /**
     * Public constructor, using a specific registry delegate.
     * @param registry The registry delegate.
     *
     */
    public FileStoreEndpointResolverImpl(RegistryService registry)
        {
		if (null == registry)
			{
			throw new IllegalArgumentException(
				"Null registry delegate"
				) ;
			}
		this.registry = registry ;
		}

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
     *
     */
    public URL resolve(Ivorn ivorn)
        throws FileStoreIdentifierException, FileStoreResolverException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("FileStoreEndpointResolverImpl.resolve()") ;
        log.debug("  Ivorn : " + ivorn) ;
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
     * Resolve an Ivorn parser into a service endpoint.
     * @param parser A FileStoreIvornParser containing the Filestore identifier.
     * @return The endpoint address for the service.
     * @throws FileStoreIdentifierException If the identifier is not valid.
     * @throws FileStoreResolverException If unable to resolve the identifier.
     *
     */
    public URL resolve(FileStoreIvornParser parser)
        throws FileStoreIdentifierException, FileStoreResolverException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("FileStoreEndpointResolverImpl.resolve()") ;
        log.debug("  Ivorn : " + ((null != parser) ? parser.getIvorn() : null)) ;
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
                "Null filestore identifier"
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
                ivorn
                ) ;
            }
        catch (Throwable ouch)
            {
            log.debug("FAIL : Registry lookup failed")  ;
            log.debug("  Exception : " + ouch)  ;
            throw new FileStoreResolverException(
                "Registry lookup failed",
                ouch
                ) ;
            }
        //
        // If we found an entry in the Registry.
        if (null != endpoint)
            {
            log.debug("PASS : Got service endpoint")  ;
            log.debug("  Endpoint : " + endpoint)  ;
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

