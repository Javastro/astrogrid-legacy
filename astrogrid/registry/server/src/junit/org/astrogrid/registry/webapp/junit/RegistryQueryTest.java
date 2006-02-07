package org.astrogrid.registry.webapp.junit;

import java.io.InputStream;
import java.io.File;
import java.util.Iterator;
import org.astrogrid.xmldb.client.XMLDBManager;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

import org.apache.axis.AxisFault;

import java.util.HashMap;

import junit.framework.*;
import org.astrogrid.registry.server.query.v0_1.RegistryQueryService;
import java.util.Map;
import java.util.Iterator;
import java.util.Properties;
import org.astrogrid.config.Config;
//import org.astrogrid.config.FallbackConfig;

/**
 * Class: RegistryQueryTest
 * Description: Tests out the various Query methods of the server side Registry.
 * @author Kevin Benson
 *
 */
public class RegistryQueryTest extends TestCase {
    
    
    public RegistryQueryTest() {
        
    }
    
    /**
     * Setup our test.
     *
    */ 
    public void setUp() throws Exception {
        super.setUp();
        File fi = new File("target/test-classes/conf.xml");
        Properties props = new Properties();
        props.setProperty("create-database", "true");
        props.setProperty("configuration",fi.getAbsolutePath());
        
        if(fi != null) {
          XMLDBManager.registerDB(props);
        }
    }
    
    /**
     * Method: testSearchByIdent
     * Description: test to perform an adql query for an identifier known in the registry.
     * @throws Exception standard junit exception to be thrown.
     */    
    public void testSearchByIdent() throws Exception {
        RegistryQueryService rqs = new RegistryQueryService();
        Document adql = askQueryFromFile("QueryForIdentifier--adql-v0.7.4.xml");
        Document doc = rqs.Search(adql);
        assertNotNull(doc);
        System.out.println("the doc in Search = " + DomHelper.DocumentToString(doc));
        assertTrue((doc.getElementsByTagNameNS("*","Resource").getLength() > 0));
    }
    

    /**
     * Method: testKeywordQueryService
     * Description: test to perform a keyword search on the registry via the use of the common 
     * web service interface method.
     * @throws Exception standard junit exception to be thrown.
     */      
    public void testKeywordQueryService() throws Exception {
        RegistryQueryService rqs = new RegistryQueryService();
        Document keywordDoc = DomHelper.newDocument("<KeywordSearch><keywords>Full</keywords></KeywordSearch>");
        Document doc = rqs.KeywordSearch(keywordDoc);        
        assertNotNull(doc);
        System.out.println("the doc in Search = " + DomHelper.DocumentToString(doc));
        assertTrue((doc.getElementsByTagNameNS("*","Resource").getLength() > 0));
    }
    
    /**
     * Method: testXQuerySearch
     * Description: test to perform a xquery search on the registry via the use of the common 
     * web service interface method.
     * @throws Exception standard junit exception to be thrown.
     */       
    public void testXQuerySearch() throws Exception {
        RegistryQueryService rqs = new RegistryQueryService();
        Document xqueryDoc = DomHelper.newDocument("<XQuerySearch><XQuery>declare namespace vr = \"http://www.ivoa.net/xml/VOResource/v0.10\"; declare namespace vor=\"http://www.ivoa.net/xml/RegistryInterface/v0.1\"; for $x in //vor:Resource where $x/vr:identifier = 'ivo://registry.test/org.astrogrid.registry.RegistryService' return $x</XQuery></XQuerySearch>");
        Document doc = rqs.XQuerySearch(xqueryDoc);
        System.out.println("the results of a xquerysearch = " + DomHelper.DocumentToString(doc));
        assertNotNull(doc);
        assertTrue((doc.getElementsByTagNameNS("*","Resource").getLength() > 0));
    }
    
    /**
     * Method: testGetResourceByIdentifier
     * Description: test to perform a query for one particular identifier in the registry not based on adql.
     * @throws Exception standard junit exception to be thrown.
     */    
    public void testGetResourceByIdentifier() throws Exception {        
         RegistryQueryService rqs = new RegistryQueryService();
         Document doc = rqs.getQueryHelper().getResourceByIdentifier("ivo://registry.test/org.astrogrid.registry.RegistryService");
         assertNotNull(doc);        
         assertTrue((doc.getElementsByTagNameNS("*","Resource").getLength() > 0));
         System.out.println("the testgetresbyident = " + DomHelper.DocumentToString(doc));
     }
    
    /**
     * Method: testGetResourceByIdentifier
     * Description: test to perform a query for one particular identifier in the registry not based on adql.
     * @throws Exception standard junit exception to be thrown.
     */    
    public void testGetResourceByIdentifierNotFound() throws Exception {
         System.out.println("begin testGetResourceByIdentifierNotFound");
         RegistryQueryService rqs = new RegistryQueryService();
         Document doc = rqs.getQueryHelper().getResourceByIdentifier("ivo://sometestnotfound");
         assertNotNull(doc);
         System.out.println("the testgetresbyident = " + DomHelper.DocumentToString(doc));
     }
    
    /**
     * Method: testGetResourceByIdentifier
     * Description: test to perform a query for one particular identifier in the registry not based on adql.
     * @throws Exception standard junit exception to be thrown.
     */    
    public void testGetResourcesByIdentifier() throws Exception {        
         RegistryQueryService rqs = new RegistryQueryService();
         Document doc = rqs.getQueryHelper().getResourcesByIdentifier("ivo://registry.test/org.astrogrid.registry.RegistryService");
         assertNotNull(doc);        
         assertTrue((doc.getElementsByTagNameNS("*","Resource").getLength() > 0));
     }    
    
    /**
     * Method: testGetResourceByIdentifier
     * Description: test to perform a query for one particular identifier in the registry not based on adql.
     * @throws Exception standard junit exception to be thrown.
     */    
    public void testGetResourcesByIdentifierNotFound() throws Exception {
         System.out.println("begin testGetResourceByIdentifierNotFound");
         RegistryQueryService rqs = new RegistryQueryService();
         Document doc = rqs.getQueryHelper().getResourcesByIdentifier("ivo://registry.test/someregtest");
         assertNotNull(doc);        
         assertTrue((doc.getElementsByTagNameNS("*","VOResources").getLength() > 0));
     }          
  
    
    /**
     * Method: testKeywordQuery
     * Description: test to perform a keyword search on the registry.
     * @throws Exception standard junit exception to be thrown.
       
    public void testKeywordQuery() throws Exception {
        RegistryQueryService rqs = new RegistryQueryService();
        Document doc = rqs.keywordQuery("Full",true,"0.10");
        assertNotNull(doc);        
        assertTrue((doc.getElementsByTagNameNS("*","Resource").getLength() > 0));
    } 
     */     
    
         
    /**
     * Method: askQueryFromFile
     * Description: Obtains a File on the local system as a inputstream and feeds it into a Document DOM object to be
     * returned.
     * @param queryFile - File name of a xml resource.
     * @return Document DOM object of a XML file.
     * @throws Exception  Any IO Exceptions or DOM Parsing exceptions could be thrown.
     */       
    protected Document askQueryFromFile(String queryFile) throws Exception {
        assertNotNull(queryFile);
        InputStream is = this.getClass().getResourceAsStream(queryFile);
        
        assertNotNull("Could not open query file :" + queryFile,is);
        Document queryDoc = DomHelper.newDocument(is);
        
        //Document queryDoc = DomHelper.newDocument(new File(queryFile));
        return queryDoc;
    }    
}