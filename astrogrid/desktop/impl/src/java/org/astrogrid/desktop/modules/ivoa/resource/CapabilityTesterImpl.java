/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.Map;

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

/** @future - maybe refactor these out into separate classes - associate the 
 * tests with the protocol more closely?
 * thought - could I frame all this as junit - would it sit better??
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 19, 20083:55:52 PM
 */
public class CapabilityTesterImpl implements CapabilityTester {

    private final Cone cone;
    private final Ssap ssap;
    private final Stap stap;
    private final Siap siap;
    
    
    public boolean testCapability(Capability cap) throws Exception {
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
    private boolean testConeCapability(ConeCapability cap) throws Exception {
        Query testQuery = cap.getTestQuery();
        if (testQuery == null) {
            throw new Exception("No test query provided");
        }
        URI endpoint = findEndpoint(cap);
        URL query = cone.constructQuery(endpoint,testQuery.getRa(),testQuery.getDec(),testQuery.getSr());
        if (testQuery.getVerb() != 0) {
            query = cone.addOption(query,"VERB",testQuery.getVerb()+"");
        }
        //@todo there's also a catalog parameter - but no idea what to do with it.
        if (testQuery.getExtras() != null) {
            query = new URL(query.toString() + "&" + testQuery.getExtras());
        }
        Map[] map = cone.execute(query);
        if (map == null || map.length == 0) {
            throw new Exception("No results returned");
        }
        return true;
    }

    /**
     * @param cap
     */
    private boolean testSiapCapability(SiapCapability cap) throws Exception{
        org.astrogrid.acr.ivoa.resource.SiapCapability.Query testQuery = cap.getTestQuery();
        if (testQuery == null) {
            throw new Exception("No test query provided");
        }
        URI endpoint = findEndpoint(cap);
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
        Map[] map = siap.execute(query);
        if (map == null || map.length == 0) {
            throw new Exception("No results returned");
        }
        return true;        
    }

// stap and ssap are untested - can't find any real-world examples.

    /**
     * @param cap
     */
    private boolean testStapCapability(StapCapability cap) throws Exception {
        org.astrogrid.acr.ivoa.resource.StapCapability.Query testQuery = cap.getTestQuery();
        if (testQuery == null) {
            throw new Exception("No test query provided");
        }
        URI endpoint = findEndpoint(cap);
        Date start = new Date(testQuery.getStart()); //@fixme - won't work - need to parse this correctly using date format, but format hasn't been documented.
        Date end = new Date(testQuery.getEnd());//@fixme too
        URL query;
        
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
        Map[] map = ssap.execute(query);
        if (map == null || map.length == 0) {
            throw new Exception("No results returned");
        }
        return true;       
    }


    /**
     * @param cap
     */
    private boolean testSsapCapability(SsapCapability cap) throws Exception {
        org.astrogrid.acr.ivoa.resource.SsapCapability.Query testQuery = cap.getTestQuery();
        if (testQuery == null) {
            throw new Exception("No test query provided");
        }
        URI endpoint = findEndpoint(cap);
        URL query = ssap.constructQuery(endpoint
                ,testQuery.getPos().getLong()
                ,testQuery.getPos().getLat()
                ,testQuery.getSize()
                );
        //@todo got to handle getPos().getRefframe() too.
        if (testQuery.getQueryDataCmd() != null) {
            query = new URL(query + "&" + testQuery.getQueryDataCmd());
        }
        Map[] map = ssap.execute(query);
        if (map == null || map.length == 0) {
            throw new Exception("No results returned");
        }
        return true;           
    }

    /**
     * @param cap
     * @return
     * @throws Exception
     */
    private URI findEndpoint(Capability cap) throws Exception {
        Interface[] interfaces = cap.getInterfaces();
        if (interfaces == null || interfaces.length == 0) {
            throw new Exception("No interfaces provided");
        }
        AccessURL[] urls = interfaces[0].getAccessUrls();
        if (urls == null || urls.length == 0) {
            throw new Exception("No access urls provided in first interface");
            // should we look elsewhere?
        }
        URI endpoint = urls[0].getValueURI();
        return endpoint;
    } 






    public CapabilityTesterImpl(Cone cone, Ssap ssap, Stap stap, Siap siap) {
        super();
        this.cone = cone;
        this.ssap = ssap;
        this.stap = stap;
        this.siap = siap;
    }

}
