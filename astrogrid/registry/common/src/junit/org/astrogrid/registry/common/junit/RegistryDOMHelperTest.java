package org.astrogrid.registry.common.junit;

import java.io.InputStream;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

import junit.framework.*;
import org.astrogrid.registry.common.RegistryDOMHelper;

/**
 * Class: RegistryDOMHelperTest
 * Description: Tests out the various methods from RegistryDOMHelper in the common registry classes.
 * @author Kevin Benson
 *
 */
public class RegistryDOMHelperTest extends TestCase {
    
    /**
     * Empty Constructor
     *
     */
    public RegistryDOMHelperTest() {
        
    }
    
    /**
     * Method: testgetDefaultVersion
     * Description: test to verify the default version in the properties file.
     * @throws Exception standard junit exception to be thrown.
     */    
    public void testgetDefaultVersion() throws Exception {
        assertEquals("0.10",RegistryDOMHelper.getDefaultVersionNumber());
    }
   
    /**
     * Method: testGetAuthorityID
     * Description: test to extract an authority id.
     * @throws Exception standard junit exception to be thrown.
     */      
    public void testGetAuthorityID() throws Exception {
        Document queryDoc = askQueryFromFile("Cambridge0_10.xml");
        assertEquals("uk.ac.cam.ast",
                     RegistryDOMHelper.getAuthorityID(queryDoc.getDocumentElement()));
    }
    
    /**
     * Method: testGetResourceKey
     * Description: test to extract an resource key.
     * @throws Exception standard junit exception to be thrown.
     */         
    public void testGetResourceKey() throws Exception {
        Document queryDoc = askQueryFromFile("Cambridge0_10.xml");
        assertEquals("org.astrogrid.registry.RegistryService",
                     RegistryDOMHelper.getResourceKey(queryDoc.getDocumentElement()));
    }
    
    /**
     * Method: testGetIdentifier
     * Description: test to extract an identifer which is essentially authorityid/resource key.
     * @throws Exception standard junit exception to be thrown.
     */       
    public void testGetIdentifier() throws Exception {
        Document queryDoc = askQueryFromFile("Cambridge0_10.xml");
        assertEquals("ivo://uk.ac.cam.ast/org.astrogrid.registry.RegistryService",
                     RegistryDOMHelper.getIdentifier(queryDoc.getDocumentElement()));
    }
    
    
    public void testfindRegistryVersionFromXML() throws Exception {
        Document queryDoc = askQueryFromFile("Cambridge0_10.xml");
        String version = RegistryDOMHelper.findVOResourceVersionFromNode(queryDoc.getDocumentElement());
        System.out.println("the version returned = " + version);
        
    }
    
    public void testfindADQLVersionFromXML() throws Exception {
        Document adql = askQueryFromFile("QueryForIdentifier--adql-v0.7.4.xml");
        String version = RegistryDOMHelper.findADQLVersionFromNode(adql.getDocumentElement());
        System.out.println("the version returned2 = " + version);
        
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