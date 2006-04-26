package org.astrogrid.registry.server.query;

import org.w3c.dom.Document;

public interface ISearch {
    
    public Document Search(Document query);
    
    public Document XQuerySearch(Document query);
    
    public Document KeywordSearch(Document query);
    
    public Document GetResource(Document query);
    
    public Document GetRegistries(Document query);
    
    public Document loadRegistry(Document query);    
    
    public QueryHelper getQueryHelper();
    
    public String getWSDLNameSpace();
    
    public String getContractVersion();
    
    public String getResourceVersion();
    
    public String getQueryInterfaceRoot(String wsInterfaceMethod);    
    
    
}