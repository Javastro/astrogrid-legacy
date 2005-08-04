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
import org.astrogrid.registry.server.XSLHelper;
import org.astrogrid.registry.common.RegistrySchemaMap;
import java.util.Map;
import java.util.Iterator;
import org.astrogrid.config.Config;
//import org.astrogrid.config.FallbackConfig;
/**
 * Class: XSLHelperTest
 * Description: Unit tests for testing out the XSL that happens on the server side of a registry.  Typically these
 * methods are already called from updates and queries on the registry.  But this tests verifies the xsl's are 
 * happening and receiving results.
 * @author Kevin Benson
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class XSLHelperTest extends TestCase {
    
    
    public XSLHelperTest() {
        
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
    
    /**
     * Method: testXSLADQLToXQLTest
     * Description: test to transform ADQL to XQL.
     * @throws Exception standard junit exception to be thrown.
     */        
    public void testXSLADQLToXQLTest() throws Exception {        
        Document adql = askQueryFromFile("QueryForIdentifier--adql-v0.7.4.xml");        
        XSLHelper xsl = new XSLHelper();
        String xql = xsl.transformADQLToXQL(adql,"0.7.4","vor:Resource","declare namespace hello;");
        assertTrue((xql.length() > 0));        
    }  
    
    /**
     * Method: testXSLResourceToOAITest
     * Description: test to transform an XML Resource to the OAI version of a XML Resource..
     * @throws Exception standard junit exception to be thrown.
     */        
    public void testXSLResourceToOAITest() throws Exception {        
        Document res = askQueryFromFile("Cambridge0_10_Reg.xml");
        XSLHelper xsl = new XSLHelper();
        Document doc = xsl.transformToOAI(res.getDocumentElement(),"0.10");
        assertNotNull(doc);  
        assertTrue((doc.getElementsByTagNameNS("*","record").getLength() > 0));        
    }    
    
    /**
     * Method: testXSLUpdateResource
     * Description: test to transform an XML Resource to a xml resource acceptable for updating into the registry.
     * @throws Exception standard junit exception to be thrown.
     */        
    public void testXSLUpdateResource() throws Exception {        
        Document res = askQueryFromFile("Cambridge0_10_Reg.xml");
        XSLHelper xsl = new XSLHelper();
        Document doc = xsl.transformUpdate(res.getDocumentElement(),"0.10",false);        
        assertNotNull(doc);        
        assertTrue((doc.getElementsByTagNameNS("*","Resource").getLength() > 0));
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