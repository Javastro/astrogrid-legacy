/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/resolver/FileManagerEndpointResolverImpl.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: FileManagerEndpointResolverImpl.java,v $
 *   Revision 1.3  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.2.4.2  2005/01/12 14:20:57  dave
 *   Replaced tabs with spaces ....
 *
 *   Revision 1.2.4.1  2004/12/22 07:38:36  dave
 *   Started to move towards StoreClient API ...
 *
 *   Revision 1.2  2004/11/25 00:20:27  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.1  2004/11/18 16:06:11  dave
 *   Added delegate resolver and tests ....
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.resolver ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URL ;
import java.net.MalformedURLException ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.RegistryDelegateFactory ;
import org.astrogrid.registry.client.query.RegistryService ;

import org.astrogrid.filemanager.common.ivorn.FileManagerIvornParser ;
import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory ;

import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException ;


/**
 * A helper class to resolve an Ivron into a service endpoint.
 * Note, this class should not be used by external components.
 *
 */
public class FileManagerEndpointResolverImpl
    implements FileManagerEndpointResolver
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FileManagerEndpointResolverImpl.class);

    /**
     * Protected constructor, using the default Registry service.
     *
     */
    protected FileManagerEndpointResolverImpl()
        {
        this(
            (URL) null
            ) ;
        }

    /**
     * Protected constructor, for a specific Registry service.
     * @param endpoint The endpoint address for our RegistryDelegate.
     *
     */
    protected FileManagerEndpointResolverImpl(URL registry)
        {
        this(
            registry,
            new RegistryDelegateFactory()
            ) ;
        }

    /**
     * Protected constructor, for a specific Registry service.
     * @param registry The endpoint address for our registry service.
     * @param factory A factory to create our registry delegate.
     *
     */
    protected FileManagerEndpointResolverImpl(URL registry, RegistryDelegateFactory factory)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("FileManagerEndpointResolverImpl()") ;
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
    protected FileManagerEndpointResolverImpl(RegistryService registry)
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
     * @param ivorn An Ivorn containing a filemanager identifier.
     * @return The endpoint address for the service.
     * @throws FileManagerResolverException If unable to resolve the identifier.
     *
     */
    public URL resolve(Ivorn ivorn)
        throws FileManagerResolverException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("FileManagerEndpointResolverImpl.resolve()") ;
        log.debug("  Ivorn : " + ((null != ivorn) ? ivorn.toString() : "null")) ;
        //
        // Check for null ivorn.
        if (null == ivorn)
            {
            throw new IllegalArgumentException(
                "Null service ivorn"
                ) ;
            }
        //
        // Parse the ivorn and resolve it.
        try {
            return this.resolve(
                new FileManagerIvornParser(ivorn)
                ) ;
            }
        catch (FileManagerIdentifierException ouch)
            {
            throw new FileManagerResolverException(
                "Unable to parse service ivorn : '" + ivorn.toString() + "'"
                );
            }
        }

    /**
     * Resolve an Ivorn parser into a service endpoint.
     * @param parser A FileManagerIvornParser containing the Filestore identifier.
     * @return The endpoint address for the service.
     * @throws FileManagerResolverException If unable to resolve the identifier.
     *
     */
    protected URL resolve(FileManagerIvornParser parser)
        throws FileManagerResolverException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("FileManagerEndpointResolverImpl.resolve()") ;
        log.debug("  Ivorn : " + ((null != parser) ? parser.getIvorn().toString() : null)) ;
        //
        // Check for null parser.
        if (null == parser)
            {
            throw new IllegalArgumentException(
                "Null ivorn parser"
                ) ;
            }
        //
        // Get our service Ivorn.
        Ivorn ivorn = null ;
        try {
            ivorn = parser.getServiceIvorn() ;
            }
        catch (FileManagerIdentifierException ouch)
            {
            throw new FileManagerResolverException(
                "Unable to parse service ivorn"
                );
            }
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
            throw new FileManagerResolverException(
                "Registry lookup failed for ivorn : '" + ivorn.toString() + "'",
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
                return new URL(
                    endpoint
                    ) ;
                }
            //
            // Report the problem in a Exception.
            catch (MalformedURLException ouch)
                {
                throw new FileManagerResolverException(
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
            throw new FileManagerResolverException(
                "Registry returned null endpoint address for ivorn",
                ivorn
                ) ;
            }
        }
    }

