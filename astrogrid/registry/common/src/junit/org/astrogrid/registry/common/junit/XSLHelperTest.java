package org.astrogrid.registry.common.junit;

import java.io.InputStream;
import java.util.Iterator;

import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

import junit.framework.*;
import org.astrogrid.registry.common.RegistryValidator;
import org.astrogrid.registry.common.XSLHelper;
import org.astrogrid.registry.common.RegistrySchemaMap;
import java.util.Map;
import java.util.Iterator;

/**
 * Class: XSLHelperTest
 * Description: tests out the common xsl that happens in the registry.  Which is mainly just the xsl ability
 * to go from one version of a xml resource document to another version.  Currently only 0.10->0.9 is done.
 * @author Kevin Benson
 *
 */
public class XSLHelperTest extends TestCase {
    
    /**
     * Empty Constructor
     *
     */
    public XSLHelperTest() {
        
    }

    /**
     * Method: testValidCambridge10
     * Description: tests taking a XML resource Document object from 0.10 to 0.9
     * @throws Exception standard junit exception to be thrown.
     */
    public void testValidCambridgev10() throws Exception {
        Document queryDoc = askQueryFromFile("Cambridge0_10.xml");
        XSLHelper xsl = new XSLHelper();
        xsl.transformResourceToResource(queryDoc,"0.10","0.9");
        RegistryValidator.isValid(queryDoc);
    }
    
    /**
     * Method: testValidCambridgev10Element
     * Description: tests taking a XML resource Element object from 0.10 to 0.9
     * @throws Exception standard junit exception to be thrown.
     */    
    public void testValidCambridgev10Element() throws Exception {
        Document queryDoc = askQueryFromFile("Cambridge0_10.xml");
        XSLHelper xsl = new XSLHelper();
        xsl.transformResourceToResource(queryDoc.getDocumentElement(),"0.10","0.9");
        RegistryValidator.isValid(queryDoc);
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