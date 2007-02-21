package org.astrogrid.registry.server.xmldb;

import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.astrogrid.xmldb.client.XMLDBFactory;
import org.astrogrid.xmldb.client.XMLDBService;
import org.astrogrid.config.Config;
import org.astrogrid.registry.server.InvalidStorageNodeException;
import org.astrogrid.util.DomHelper;

import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class XMLDBRegistry {
    
    private XMLDBService xdb = null;
    
    
    /**
     * conf - Config variable to access the configuration for the server normally
     * jndi to a config file.
     * @see org.astrogrid.config.Config
     */   
    public static Config conf = null;

    /**
     * Static to be used on the initiatian of this class for the config
     */   
    static {
       if(conf == null) {
          conf = org.astrogrid.config.SimpleConfig.getSingleton();
       }
    }
    
    /**
     * Logging variable for writing information to the logs
     */
     private static final Log log = LogFactory.getLog(XMLDBRegistry.class);
    
    
    public XMLDBRegistry() {
        xdb = XMLDBFactory.createXMLDBService();
    }
    
    /**
     * Method: queryExist
     * Description: Queries the xml database, on the collection of the registry. This method
     * will read from properties a xql expression and fill out the expression then perform the query. This
     * is a convenience in case of customization for other xml databases.
     * @param xqlString an XQuery to query the database
     * @param collectionName the location in the database to query (sort of like a table)
     * @return xml DOM object returned from the database, which are Resource elements
     */
    public ResourceSet query(String xqlString, String collectionName) throws XMLDBException {
       log.debug("start query");
       Collection coll = null;
       try {
           coll = xdb.openCollection(collectionName);
           log.info("Now querying in collection = " + collectionName + " query = " + xqlString);
           //start a time to see how long the query took.
           long beginQ = System.currentTimeMillis(); 
           ResourceSet rs = xdb.queryXQuery(coll,xqlString);
           
           log.info("Total Query Time = " + (System.currentTimeMillis() - beginQ));
           log.info("Number of results found in query = " + rs.getSize());
           return rs;
       } finally {
           try {
               xdb.closeCollection(coll);
           }catch(XMLDBException xmldb) {
               log.error(xmldb);
           }//try
       }//finally
    }
    
    public XMLResource getResource(String ident, String collectionName) throws XMLDBException {
        XMLResource xmr = null;    
        Collection coll = null;
        try {
            coll = xdb.openCollection(collectionName);
            xmr = (XMLResource)xdb.getResource(coll,ident + ".xml");
        } finally {
            try {
                xdb.closeCollection(coll);
            }catch(XMLDBException xmldb) {
                log.error(xmldb);
            }
        }
        return xmr;
    }
    
    public String[] listRootCollections() throws XMLDBException {        
        Collection coll = null;
        try {
            coll = xdb.openCollection();
            return coll.listChildCollections();        
        } finally {
            try {
                xdb.closeCollection(coll);
            }catch(XMLDBException xmldb) {
                log.error(xmldb);
            }
        }
    }
        
    public boolean storeXMLResource(String ident, String collectionName, Node node) throws XMLDBException, InvalidStorageNodeException {
        if(Node.ELEMENT_NODE == node.getNodeType()) {
            return storeXMLResource(ident, collectionName, DomHelper.ElementToString((Element)node));
        } else if(Node.DOCUMENT_NODE == node.getNodeType()) {
            return storeXMLResource(ident, collectionName, DomHelper.DocumentToString((Document)node));
        }
        throw new InvalidStorageNodeException("Unknown or Invalid Node trying to be stored in the registry db.  Only Document and Element nodes are allowed");
    }

    private boolean storeXMLResource(String ident,String collectionName, String node) throws XMLDBException {
        Collection coll = null;
        try {
            coll = xdb.openAdminCollection(collectionName);
            xdb.storeXMLResource(coll,ident,node);
        }finally {
            xdb.closeCollection(coll);
        }
        return true;
    }
    
    public Collection getCollection(String collectionName, boolean admin) throws XMLDBException {
        if(admin)
            return xdb.openAdminCollection(collectionName);
        else
            return xdb.openCollection(collectionName);
    }
    
    public boolean storeXMLResource(Collection coll, String ident, Node node) throws XMLDBException, InvalidStorageNodeException {
    	
    	 if(Node.ELEMENT_NODE == node.getNodeType()) {  
    		 xdb.storeXMLResource(coll,ident,DomHelper.ElementToString((Element)node));
    		 return true;        
    	 }else if(Node.DOCUMENT_NODE == node.getNodeType() ) {
    		 xdb.storeXMLResource(coll,ident,DomHelper.DocumentToString((Document)node));
    		 return true;        
    	 }    	
        throw new InvalidStorageNodeException("Unknown or Invalid Node trying to be stored in the registry db.  Only Document and Element nodes are allowed");
    }
    
    public boolean closeCollection(Collection coll) throws XMLDBException {
        xdb.closeCollection(coll);
        return true;
    }
        
    
    public void removeResource(String ident, String collectionName) throws XMLDBException {
        Collection coll = null;
        try {    
            coll = xdb.openAdminCollection(collectionName);
            xdb.removeResource(coll,ident);
        } finally {
            xdb.closeCollection(coll);
        }
    }
}