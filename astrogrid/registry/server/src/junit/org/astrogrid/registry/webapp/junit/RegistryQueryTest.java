package org.astrogrid.registry.webapp.junit;

import java.io.InputStream;
import java.io.File;
import java.util.Iterator;
import org.astrogrid.xmldb.client.XMLDBFactory;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

import org.apache.axis.AxisFault;

import java.util.HashMap;

import junit.framework.*;
import org.astrogrid.registry.server.query.RegistryQueryService;
import org.astrogrid.registry.server.RegistryServerHelper;
import org.astrogrid.registry.common.RegistrySchemaMap;
import java.util.Map;
import java.util.Iterator;
import org.astrogrid.config.Config;
//import org.astrogrid.config.FallbackConfig;

public class RegistryQueryTest extends TestCase {
    
    
    public RegistryQueryTest() {
        
    }
    
    /**
     * Setup our test.
     *
    */ 
    public void setUp() throws Exception {
        super.setUp();
      //  XMLDBFactory xdf = new XMLDBFactory();
        File fi = new File("target/test-classes/exist.xml");
        if(fi != null) {
          XMLDBFactory.registerDB(fi.getAbsolutePath());
        }
    }
    
    public void testSearchByIdent() throws Exception {
        RegistryQueryService rqs = new RegistryQueryService();
        Document adql = askQueryFromFile("QueryForIdentifier--adql-v0.7.4.xml");
        Document doc = rqs.Search(adql);
        assertNotNull(doc);
        //assertTrue((doc.getElementsByTagNameNS("*","Resource") > 0));
    }
    
    public void testKeywordQuery() throws Exception {
        RegistryQueryService rqs = new RegistryQueryService();
        Document doc = rqs.keywordQuery("Full",true);
        assertNotNull(doc);        
        //assertTrue((doc.getElementsByTagNameNS("*","Resource") > 0));
    }
    
    public void testGetResources() throws Exception {
       
        RegistryQueryService rqs = new RegistryQueryService();
        Document doc = rqs.getResourcesByIdentifier(" ivo://registry.test","0.10");
        assertNotNull(doc);        
        //assertTrue((doc.getElementsByTagNameNS("*","Resource") > 0));
    }   
    
    public void testGetResourceByIdentifier() throws Exception {
        
         RegistryQueryService rqs = new RegistryQueryService();
         Document doc = rqs.getResourcesByIdentifier(" ivo://registry.test/org.astrogrid.registry.RegistryService","0.10");
         assertNotNull(doc);        
         //assertTrue((doc.getElementsByTagNameNS("*","Resource") > 0));
     }      
    
           
    protected Document askQueryFromFile(String queryFile) throws Exception {
        assertNotNull(queryFile);
        InputStream is = this.getClass().getResourceAsStream(queryFile);
        
        assertNotNull("Could not open query file :" + queryFile,is);
        Document queryDoc = DomHelper.newDocument(is);
        
        //Document queryDoc = DomHelper.newDocument(new File(queryFile));
        return queryDoc;
    }    
}