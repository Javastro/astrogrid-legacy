/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/resolver/FileManagerEndpointResolverImpl.java,v $</cvs:source>
 * <cvs:author>$Author: pah $</cvs:author>
 * <cvs:date>$Date: 2008/01/15 16:11:01 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: FileManagerEndpointResolverImpl.java,v $
 *   Revision 1.6  2008/01/15 16:11:01  pah
 *   uses v1.0 registry client to look up filemanager endpoint
 *
 *   Revision 1.5  2007/04/04 10:19:12  nw
 *   loosened up visibility
 *
 *   Revision 1.4  2005/03/11 13:37:05  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.3.8.6  2005/03/01 15:07:35  nw
 *   close to finished now.
 *
 *   Revision 1.3.8.5  2005/02/18 15:50:14  nw
 *   lots of changes.
 *   introduced new schema-driven soap binding, got soap-based unit tests
 *   working again (still some failures)
 *
 *   Revision 1.3.8.4  2005/02/11 17:16:03  nw
 *   knock on effect of renaming and making IvornFactory static
 *
 *   Revision 1.3.8.3  2005/02/11 16:03:20  nw
 *   refactoring cuts these down quite a bit. which is nice.
 *
 *   Revision 1.3.8.2  2005/02/11 14:31:47  nw
 *   still need to refactor these.
 *
 *   Revision 1.3.8.1  2005/02/10 16:23:14  nw
 *   formatted code
 *
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
package org.astrogrid.filemanager.resolver;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.filemanager.common.ivorn.IvornParser;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.v1_0.RegistryService;
import org.astrogrid.store.Ivorn;

/**
 * A helper class to resolve an Ivron into a service endpoint. Note, this class
 * should not be used by external components.
 * 
 * @modified nww removed unused helper methods.
 * @modified nww made package-private - so it can't be used by external
 *                   components.
 * @modifed nww called static methods on registry delegate factory in static
 *                   way.
 * @modified nww removed constructors only used by tetsts, and vice versa.
 * @modified nww inverted conditionals to make logic shorted
 *  
 */
public class FileManagerEndpointResolverImpl implements FileManagerEndpointResolver {
    /**
     * Our debug logger.
     *  
     */
    private static Log log = LogFactory
            .getLog(FileManagerEndpointResolverImpl.class);

    /**
     * Public constructor,using the default Registry service.
     *  
     */
    public FileManagerEndpointResolverImpl() {
        this.registry = RegistryDelegateFactory.createQueryv1_0();
    }

    /**
     * Our Registry delegate.
     *  
     */
    private final RegistryService registry;
 
    /**
     * Resolve an Ivorn into a service endpoint.
     * 
     * @param ivorn
     *                    An Ivorn containing a filemanager identifier.
     * @return The endpoint address for the service.
     * @throws FileManagerResolverException
     *                     If unable to resolve the identifier.
     *  
     */
    public URL resolve(Ivorn ivorn) throws FileManagerResolverException {
        log.debug("FileManagerEndpointResolverImpl.resolve(" + ivorn + ")");
        if (null == ivorn) {
            throw new IllegalArgumentException("Null service ivorn");
        }
        // Get our service Ivorn.
        Ivorn ivorn1 = null;
        try {
            IvornParser parser = new IvornParser(ivorn);
            ivorn1 = parser.getServiceIvorn();
        } catch (URISyntaxException ouch) {
            throw new FileManagerResolverException(
                    "Unable to parse service ivorn : '" + ivorn.toString()
                            + "'");
        }
        // Lookup the service in the registry.
        String endpoint = null;
        try {
            endpoint = registry.getEndpointByIdentifier(ivorn1.toString(),"ivo://org.astrogrid/std/myspace/v1.0#myspace");
        } catch (Throwable ouch2) {
            log.debug("FAIL : Registry lookup failed", ouch2);
            throw new FileManagerResolverException(
                    "Registry lookup failed for ivorn : '" + ivorn1.toString()
                            + "'", ouch2);
        }
        if (endpoint == null) { // failed to find it in the reg.
            throw new FileManagerResolverException(
                    "Registry returned null endpoint address for ivorn", ivorn1);
        }
        log.debug("PASS : Got service endpoint " + endpoint);
        try {
            return new URL(endpoint);
        } catch (MalformedURLException ouch3) {
            throw new FileManagerResolverException(
                    "Unable to parse Registry response into endpoint URL",
                    ivorn1);
        }

    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FileManagerEndpointResolverImpl:");
        buffer.append(" registry: ");
        buffer.append(registry);
        buffer.append("]");
        return buffer.toString();
    }
}

