package org.astrogrid.xmldb.client;

import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.base.ResourceSet;
/**
 * Small Interface set for querying on a XMLDB:API XQueryService or XPathService
 * @author Kevin Benson
 *
 */
public interface QueryService {

    /**
     * Method: query
     * Purpose: query on a XMLDB:API implementted database and return the results.
     * @param queryString - XQL or XPath type query string.
     * @return ResourceSet containting a set of the results of the query (if any).
     */
    public ResourceSet query(String queryString) throws XMLDBException;
    
}