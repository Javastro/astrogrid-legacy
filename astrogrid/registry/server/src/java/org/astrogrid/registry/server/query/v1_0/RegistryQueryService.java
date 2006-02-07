package org.astrogrid.registry.server.query.v1_0;

import org.astrogrid.registry.server.query.DefaultQueryService;
import org.astrogrid.registry.server.query.ISearch;

/**
 * Class: RegistryQueryService
 * Description: The main class for all queries to the Registry to go to via Web Service or via internal
 * calls such as jsp pages or other classes.  The main focus is Web Service Interface methods are here
 * such as Search, KeywordSearch, and GetResourceByIdentifier.
 *
 * @author Kevin Benson
 */
public class RegistryQueryService extends DefaultQueryService implements ISearch {
    

    private static final String QUERY_WSDL_NS = "http://www.something";
    
    private static final String CONTRACT_VERSION = "1.0";    
    
    private static final String VORESOURCE_VERSION = "0.1";

    public RegistryQueryService() {
        super(QUERY_WSDL_NS, CONTRACT_VERSION, VORESOURCE_VERSION);     
    }
   
    public String getWSDLNameSpace() {return this.QUERY_WSDL_NS;}
    public String getContractVersion() { return this.CONTRACT_VERSION;}
    public String getResourceVersion() { return this.VORESOURCE_VERSION;}
    
    
}