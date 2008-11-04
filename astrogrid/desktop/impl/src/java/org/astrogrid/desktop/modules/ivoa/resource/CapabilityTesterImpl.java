/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.Map;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Stap;
import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.ConeCapability;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.SiapCapability;
import org.astrogrid.acr.ivoa.resource.SsapCapability;
import org.astrogrid.acr.ivoa.resource.StapCapability;
import org.astrogrid.acr.ivoa.resource.ConeCapability.Query;
import org.astrogrid.desktop.modules.system.ProgrammerError;

/**
 * Implementation of a {@code CapabilityTester}
 *  @future - maybe refactor these out into separate classes - associate the 
 * tests with the protocol more closely?
 * and make accessible from ar.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 19, 20083:55:52 PM
 */
public class CapabilityTesterImpl implements CapabilityTester {

    /**
     * 
     */
    private static final String NO_ROWS_MESSAGE = "The service returned no rows of results. At least one row was expected. We suggest you attempt a real query anyhow";
    private static final String INVALID_QUERY_MESSAGE = "The test query defined by this resource is invalid";
    private final Cone cone;
    private final Ssap ssap;
    private final Stap stap;
    private final Siap siap;
    
    
    public boolean testCapability(final Capability cap) throws ServiceException {
        if (cap instanceof ConeCapability) {
            return testConeCapability((ConeCapability) cap);
        } else if (cap instanceof SsapCapability) {
            return testSsapCapability((SsapCapability)cap);
        } else if (cap instanceof StapCapability) {
            return testStapCapability((StapCapability)cap);
        } else if (cap instanceof SiapCapability) {
            return testSiapCapability((SiapCapability)cap);
        } else {
            return false; 
        }
    }
  


    /**
     * @param cap
     */
    private boolean testConeCapability(final ConeCapability cap) throws ServiceException {
        final Query testQuery = cap.getTestQuery();
        if (testQuery == null) {
            throw new ProgrammerError("No test query provided");
        }
        final URI endpoint = findEndpoint(cap);
        try {
        URL query = cone.constructQuery(endpoint,testQuery.getRa(),testQuery.getDec(),testQuery.getSr());
        if (testQuery.getVerb() != 0) {
            query = cone.addOption(query,"VERB",testQuery.getVerb()+"");
        }
        //@todo there's also a catalog parameter - but no idea what to do with it.
        if (testQuery.getExtras() != null) {
            query = new URL(query.toString() + "&" + testQuery.getExtras());
        }
        final Map[] map = cone.execute(query);
        if (map == null || map.length == 0) {
            throw new ServiceException(NO_ROWS_MESSAGE);
        }
        return true;
        } catch (final InvalidArgumentException e) {
            throw new ServiceException(INVALID_QUERY_MESSAGE,e);
        } catch (final MalformedURLException e) {
            throw new ServiceException(INVALID_QUERY_MESSAGE,e);
        } catch (final NotFoundException e) { // unlikely, as only comes from resolving ivo to endpoint, and we're not doing this.
            throw new ServiceException(INVALID_QUERY_MESSAGE,e);            
        }
    }

    /**
     * @param cap
     */
    private boolean testSiapCapability(final SiapCapability cap) throws ServiceException{
        final org.astrogrid.acr.ivoa.resource.SiapCapability.Query testQuery = cap.getTestQuery();
        if (testQuery == null) {
            throw new ProgrammerError("No test query provided");
        }
        final URI endpoint = findEndpoint(cap);
        try {
        URL query = siap.constructQuery(endpoint
                ,testQuery.getPos().getLong()
                ,testQuery.getPos().getLat()
                ,testQuery.getSize().getLat()
                );
        if (testQuery.getVerb() != 0) {
            query = siap.addOption(query,"VERB",testQuery.getVerb() + "");
        }
        if (testQuery.getExtras() != null) {
            query = new URL(query.toString() + "&" + testQuery.getExtras());
        }        
        final Map[] map = siap.execute(query);
        if (map == null || map.length == 0) {
            throw new ServiceException(NO_ROWS_MESSAGE);
        }
        return true;
        } catch (final InvalidArgumentException e) {
            throw new ServiceException(INVALID_QUERY_MESSAGE,e);
        } catch (final MalformedURLException e) {
            throw new ServiceException(INVALID_QUERY_MESSAGE,e);
        } catch (final NotFoundException e) { // unlikely, as only comes from resolving ivo to endpoint, and we're not doing this.
            throw new ServiceException(INVALID_QUERY_MESSAGE,e);            
        }        
    }

// stap and ssap are untested - can't find any real-world examples.

    /**
     * @param cap
     */
    private boolean testStapCapability(final StapCapability cap) throws ServiceException {
        final org.astrogrid.acr.ivoa.resource.StapCapability.Query testQuery = cap.getTestQuery();
        if (testQuery == null) {
            throw new ProgrammerError("No test query provided");
        }
        final URI endpoint = findEndpoint(cap);
        final Date start = new Date(testQuery.getStart()); //@fixme - won't work - need to parse this correctly using date format, but format hasn't been documented.
        final Date end = new Date(testQuery.getEnd());//@fixme too
        URL query;
        try {
        if (testQuery.getPos() != null && testQuery.getSize() != null) { // positional
            query = stap.constructQueryS(endpoint
                ,start
                ,end
                ,testQuery.getPos().getLong()
                ,testQuery.getPos().getLat()
                ,testQuery.getSize().getLong()
                ,testQuery.getSize().getLat()
                );
        } else {// just time
            query = stap.constructQuery(endpoint
                    ,start
                    ,end
                    );
        }
        final Map[] map = ssap.execute(query);
        if (map == null || map.length == 0) {
            throw new ServiceException(NO_ROWS_MESSAGE);
        }
        return true;
        } catch (final InvalidArgumentException e) {
            throw new ServiceException(INVALID_QUERY_MESSAGE,e);
        } catch (final NotFoundException e) { // unlikely, as only comes from resolving ivo to endpoint, and we're not doing this.
            throw new ServiceException(INVALID_QUERY_MESSAGE,e);            
        }
    }


    /**
     * @param cap
     */
    private boolean testSsapCapability(final SsapCapability cap) throws ServiceException {
        final org.astrogrid.acr.ivoa.resource.SsapCapability.Query testQuery = cap.getTestQuery();
        if (testQuery == null) {
            throw new ProgrammerError("No test query provided");
        }
        final URI endpoint = findEndpoint(cap);
        try {
        URL query = ssap.constructQuery(endpoint
                ,testQuery.getPos().getLong()
                ,testQuery.getPos().getLat()
                ,testQuery.getSize()
                );
        //@todo got to handle getPos().getRefframe() too.
        if (testQuery.getQueryDataCmd() != null) {
            query = new URL(query + "&" + testQuery.getQueryDataCmd());
        }
        final Map[] map = ssap.execute(query);
        if (map == null || map.length == 0) {
            throw new ServiceException(NO_ROWS_MESSAGE);
        }
        return true;
        } catch (final InvalidArgumentException e) {
            throw new ServiceException(INVALID_QUERY_MESSAGE,e);
        } catch (final MalformedURLException e) {
            throw new ServiceException(INVALID_QUERY_MESSAGE,e);
        } catch (final NotFoundException e) { // unlikely, as only comes from resolving ivo to endpoint, and we're not doing this.
            throw new ServiceException(INVALID_QUERY_MESSAGE,e);            
        }        
    }

    /**
     * @param cap
     * @return
     * @throws Exception
     */
    private URI findEndpoint(final Capability cap) throws ServiceException {
        final Interface[] interfaces = cap.getInterfaces();
        if (interfaces == null || interfaces.length == 0) {
            throw new ServiceException("This resource  does not contain any service interfaces");
        }
        final AccessURL[] urls = interfaces[0].getAccessUrls();
        if (urls == null || urls.length == 0) {
            throw new ServiceException("This resource does not provide an access URL");
            // should we look elsewhere?
        }
        final URI endpoint = urls[0].getValueURI();
        return endpoint;
    } 






    public CapabilityTesterImpl(final Cone cone, final Ssap ssap, final Stap stap, final Siap siap) {
        super();
        this.cone = cone;
        this.ssap = ssap;
        this.stap = stap;
        this.siap = siap;
    }

}
