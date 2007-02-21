package org.astrogrid.registry.server.query;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import junit.framework.AssertionFailedError;
import javax.xml.stream.*;

import org.xmldb.api.base.ResourceSet;

public interface ISearch  {
    
    public XMLStreamReader Search(Document query);
    
    public XMLStreamReader XQuerySearch(Document query);
    
    public XMLStreamReader KeywordSearch(Document query);
    
    public XMLStreamReader GetResource(Document query);
    
    public XMLStreamReader GetRegistries(Document query);
    
    public XMLStreamReader loadRegistry(Document query);   
    
    public XMLStreamReader GetIdentity(Document query);  
    
    public QueryHelper getQueryHelper();
    
    public String getWSDLNameSpace();
    
    public String getContractVersion();
    
    public String getResourceVersion();
    
    public boolean validateSOAPSearch(Document searchDoc) throws AssertionFailedError;
    
    public boolean validateSOAPResolve(Document resolveDoc) throws AssertionFailedError;
    
    public String getQueryInterfaceRoot(String wsInterfaceMethod);    
    
    public XMLStreamReader processSingleResult(Node resultDBNode,String responseWrapper);
    
    public XMLStreamReader processSingleResult(ResourceSet resultSet,String responseWrapper);    
    
    public XMLStreamReader processResults(ResourceSet resultSet,String responseWrapper);    
    
    public XMLStreamReader processResults(ResourceSet resultSet,String responseWrapper, String start, String max, String identOnly);    
}