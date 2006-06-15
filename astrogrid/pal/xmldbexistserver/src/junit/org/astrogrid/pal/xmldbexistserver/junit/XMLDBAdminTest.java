package org.astrogrid.pal.xmldbexistserver.junit;

import java.io.InputStream;
import java.io.File;
import java.util.Iterator;
import org.astrogrid.xmldb.client.XMLDBManager;
import org.astrogrid.xmldb.client.XMLDBFactory;
import org.astrogrid.xmldb.client.XMLDBService;
import org.xmldb.api.base.Collection;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;

import junit.framework.*;
import java.util.Map;
import java.util.Iterator;
import java.util.Properties;

import org.astrogrid.config.Config;
//import org.astrogrid.config.FallbackConfig;

/**
 * Class: RegistryAdminTest
 * Description: Test for the admin ability of the registry which is mainly adding&updating xml resources to the
 * xml database (eXist).
 * @author Kevin Benson
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class XMLDBAdminTest extends TestCase {
    
    public XMLDBAdminTest() {
        
    }
    
    XMLDBService xdbService;
    
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
        xdbService = XMLDBFactory.createXMLDBService();
    }
    
        
    public void testUpdateDocument() throws Exception {
        Document doc = askQueryFromFile("ARegistryv10.xml");
        Collection coll = xdbService.openAdminCollection("junit-tests");
        xdbService.storeXMLResource(coll,doc);
    }
    
    public void testUpdateElement() throws Exception {
        Document doc = askQueryFromFile("AstrogridStandardAuthorityv10.xml");
        Collection coll = xdbService.openAdminCollection("junit-tests");
        xdbService.storeXMLResource(coll,doc.getDocumentElement());
    }
    
    public void testUpdateDocumentWID() throws Exception {
        Document doc = askQueryFromFile("myspacev10.xml");
        Collection coll = xdbService.openAdminCollection("junit-tests");
        xdbService.storeXMLResource(coll,"test-one",doc);
    }
    
    public void testUpdateElementWID() throws Exception {
        Document doc = askQueryFromFile("filestore-onev10.xml");
        Collection coll = xdbService.openAdminCollection("junit-tests");
        xdbService.storeXMLResource(coll,"test-two", doc.getDocumentElement());
    }    
        
    public void testUpdateThree() throws Exception {
        Document doc = askQueryFromFile("Communityv10.xml");
        Collection coll = xdbService.openAdminCollection("junit-tests");
        xdbService.storeXMLResource(coll,"test-three", DomHelper.ElementToString(doc.getDocumentElement()));
    }
        
    
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