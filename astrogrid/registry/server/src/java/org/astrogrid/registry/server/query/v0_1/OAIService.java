package org.astrogrid.registry.server.query.v0_1;


import org.astrogrid.registry.server.query.DefaultOAIService;

/**
 * Class: RegistryQueryService
 * Description: The main class for all queries to the Registry to go to via Web Service or via internal
 * calls such as jsp pages or other classes.  The main focus is Web Service Interface methods are here
 * such as Search, KeywordSearch, and GetResourceByIdentifier.
 *
 * @author Kevin Benson
 */
public class OAIService extends DefaultOAIService implements org.astrogrid.registry.server.query.IOAIHarvestService {
    
    public static final String OAI_WSDL_NS = "http://www.ivoa.net/wsdl/RegistryHarvest/v0.1";
    
    public static final String CONTRACT_VERSION = "0.1";    

    public OAIService() {
        super(OAI_WSDL_NS, CONTRACT_VERSION);       
    }
   
}