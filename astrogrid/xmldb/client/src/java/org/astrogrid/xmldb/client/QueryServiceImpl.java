package org.astrogrid.xmldb.client;


import org.xmldb.api.modules.XQueryService;
import org.xmldb.api.modules.XPathQueryService;
import org.xmldb.api.modules.XUpdateQueryService;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import javax.xml.transform.OutputKeys;
import org.xmldb.api.base.ErrorCodes;

import org.astrogrid.config.Config;


/**
 * Class: XQueryServiceImpl
 * Purpose: implement a common query service to a XML database that can use either XQuery or XPath Services.
 * Determined in the properties (or more likely JNDI evironment variable) as to which service to use.
 * @author Kevin Benson
 *
 */
public class QueryServiceImpl implements QueryService {
    


    public static Config conf = null;
    private static String queryService = null;
    private Collection coll = null;
    
    private static final String XQUERY_SERVICE_TYPE = "XQueryService";
    
    private static final String XPATHQUERY_SERVICE_TYPE = "XPathQueryService";

    static {
       if(conf == null) {
          //instantiate the configuration.
          conf = org.astrogrid.config.SimpleConfig.getSingleton();
          //instantiate the xmldb service.
          queryService = conf.getString("xmldb.query.service","XQueryService");
       }
    }
    
    /**
     * XQueryServiceImpl having an opened collection object that needs to obtain a type of query service.
     */
    public QueryServiceImpl(Collection coll) {
        this.coll = coll;        
    }
    
    /**
     * Method: query
     * Purpose: query on a XMLDB:API implementted database and return the results.
     * @param queryString - XQL or XPath type query string.
     * @throws XMLDBException exception in the query.
     * @return ResourceSet containting a set of the results of the query (if any).
     */    
    public ResourceSet query(String queryString) throws XMLDBException {
        Service service = null;
        if(XQUERY_SERVICE_TYPE.equals(queryService)) {
            return ((XQueryService)getXQueryService()).query(queryString);
        }else if(XPATHQUERY_SERVICE_TYPE.equals(queryService)) {
            return ((XPathQueryService)getXPathQueryService()).query(queryString);
        }else {
            throw new XMLDBException(ErrorCodes.NO_SUCH_SERVICE);
        }
    }
    
    /**
     * Method: getXQueryService
     * Purpose: get a XQuery Service type from the XMLDB:API.
     * @throws XMLDBExceptin exception finding a XQueryService
     * @return XQueryService
     */
    private XQueryService getXQueryService() throws XMLDBException {
        XQueryService service =
            (XQueryService) coll.getService( "XQueryService", "1.0" );
        service.setProperty( OutputKeys.INDENT, "yes" );
        service.setProperty( OutputKeys.ENCODING, "UTF-8" );
        return service;
    }

    /**
     * Method: getXPathQueryService
     * Purpose: get a XPath Query Service type from the XMLDB:API.
     * @throws XMLDBExceptin exception finding a XPathQueryService
     * @return XPathQueryService
     */
    private XQueryService getXPathQueryService() throws XMLDBException {
        XQueryService service =
            (XQueryService) coll.getService( "XPathQueryService", "1.0" );
        service.setProperty( OutputKeys.INDENT, "yes" );
        service.setProperty( OutputKeys.ENCODING, "UTF-8" );
        return service;
    }
}