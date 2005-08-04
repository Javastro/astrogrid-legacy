package org.astrogrid.registry.common.junit;

import java.io.InputStream;
import java.util.Iterator;

import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

import junit.framework.*;
import org.astrogrid.registry.common.RegistryValidator;
import org.astrogrid.registry.common.RegistrySchemaMap;
import java.util.Map;
import java.util.Iterator;
/**
 * Class: ValidatorTest
 * Description: A Class that simply tests out a lot of various XML resources based on the Registry schemas.
 * @author Kevin Benson
 *
 */
public class ValidatorTest extends TestCase {
    
    
    public ValidatorTest() {
        
    }
    
    /**
     * Setup our test.
     *
     
    public void setUp() throws Exception {
        super.setUp() ;
    }
    */
    
    public void testPrintMap() throws Exception {
        Map schemaLocations = RegistrySchemaMap.ALL;
        for (Iterator i = schemaLocations.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry)i.next();
            assertNotNull(e.getKey());
            assertNotNull(e.getValue());
            System.out.println("Key = " + e.getKey() + " value = " + e.getValue());
        }//for
    }
    
    public void testValidAuthority() throws Exception {
        Document queryDoc = askQueryFromFile("AuthorityTest.xml");
        RegistryValidator.isValid(queryDoc);
    }
    
    public void testValidCEAEntries() throws Exception {
        Document queryDoc = askQueryFromFile("CEAEntries.xml");
        RegistryValidator.isValid(queryDoc);
    }
    
    public void testValidCEAEntriesv10() throws Exception {
        Document queryDoc = askQueryFromFile("CEAEntriesv10.xml");
        RegistryValidator.isValid(queryDoc);
    }
    
    public void testValidCEAHTTPEntries() throws Exception {
        Document queryDoc = askQueryFromFile("CEAHttpapps.xml");
        RegistryValidator.isValid(queryDoc);
    }    
    
    public void testValidCEAHTTPEntriesv10() throws Exception {
        Document queryDoc = askQueryFromFile("CEAHttpappsv10.xml");
        RegistryValidator.isValid(queryDoc);
    }

    public void testValidCEAHTTPEntriesLive() throws Exception {
        Document queryDoc = askQueryFromFile("HTTPAppsLive.xml");
        RegistryValidator.isValid(queryDoc);
    }
    
    public void testValidCEAHTTPEntriesLivev10() throws Exception {
        Document queryDoc = askQueryFromFile("HTTPAppsLivev10.xml");
        RegistryValidator.isValid(queryDoc);
    }   
    
    public void testValidExternalEntries() throws Exception {
        Document queryDoc = askQueryFromFile("ExternalCEAEntries.xml");
        RegistryValidator.isValid(queryDoc);
    }    

    public void testValidCambridge() throws Exception {
        Document queryDoc = askQueryFromFile("Cambridge0_9.xml");
        RegistryValidator.isValid(queryDoc);
    }    

    public void testValidCambridgev10() throws Exception {
        Document queryDoc = askQueryFromFile("Cambridge0_10.xml");
        RegistryValidator.isValid(queryDoc);
    }    
        
    
    public void testValidfilestore() throws Exception {
        Document queryDoc = askQueryFromFile("filestore-one.xml");
        RegistryValidator.isValid(queryDoc);
    } 
    
    public void testValidfilemgr() throws Exception {
        Document queryDoc = askQueryFromFile("filemanager-one.xml");
        RegistryValidator.isValid(queryDoc);
    } 
    
    public void testValidCEAHttpRealApps() throws Exception {
        Document queryDoc = askQueryFromFile("CEAHttpRealapps.xml");
        RegistryValidator.isValid(queryDoc);
    } 
    
    public void testValidAuthorityVODescription() throws Exception {
        Document queryDoc = askQueryFromFile("AuthorityTest.xml");
        RegistryValidator.isValid(queryDoc,"VODescription");
    }
    
    public void testValidRegistry() throws Exception {
        Document queryDoc = askQueryFromFile("ARegistry.xml");
        RegistryValidator.isValid(queryDoc);
    }
    
    public void testValidRegistry10() throws Exception {
        Document queryDoc = askQueryFromFile("ARegistryv10.xml");
        RegistryValidator.isValid(queryDoc,"VOResources");
    }
    
    public void testValidFullMSSL() throws Exception {
        Document queryDoc = askQueryFromFile("MSSLRegistryEntryv10.xml");
        RegistryValidator.isValid(queryDoc,"VOResources");
    }
       
    public void testValidFileStore10() throws Exception {
        Document queryDoc = askQueryFromFile("filestore-onev10.xml");
        RegistryValidator.isValid(queryDoc,"VOResources");
    }
    
    public void testValidMySpace10() throws Exception {
        Document queryDoc = askQueryFromFile("myspacev10.xml");
        RegistryValidator.isValid(queryDoc,"VOResources");
    }

    public void testValidAGServiceKinds10() throws Exception {
        Document queryDoc = askQueryFromFile("AGServiceKindsv10.xml");
        RegistryValidator.isValid(queryDoc,"VOResources");
    }
    
    public void testValidSIAP10() throws Exception {
        Document queryDoc = askQueryFromFile("INT-WFS-SIAPv10.xml");
        RegistryValidator.isValid(queryDoc,"VOResources");
    }
    
    public void testValidAuthority10() throws Exception {
        Document queryDoc = askQueryFromFile("AstrogridStandardAuthorityv10.xml");
        RegistryValidator.isValid(queryDoc,"VOResources");
    }

    
    public void testValidCEAMSSLRegistryEntryv10() throws Exception {
        Document queryDoc = askQueryFromFile("CEAMSSLRegistryEntryv10.xml");
        RegistryValidator.isValid(queryDoc,"VOResources");
    }
    
    public void testValidTabularSkyServicev10() throws Exception {
        Document queryDoc = askQueryFromFile("TabularSkyService_MSSL_TRACEv10.xml");
        RegistryValidator.isValid(queryDoc,"VOResources");
    }
    
    public void testValidCommunity() throws Exception {
        Document queryDoc = askQueryFromFile("Community.xml");
        RegistryValidator.isValid(queryDoc);
    }
    
    public void testValidCommunityv10() throws Exception {
        Document queryDoc = askQueryFromFile("Communityv10.xml");
        RegistryValidator.isValid(queryDoc);
    }    

    public void testValidCommunityAuthority() throws Exception {
        Document queryDoc = askQueryFromFile("CommunityAuthority.xml");
        RegistryValidator.isValid(queryDoc);
    }

    

    public void testValidBasicResource() throws Exception {
        Document queryDoc = askQueryFromFile("MySpace.xml");
        RegistryValidator.isValid(queryDoc);
    }
    
    public void testValidMultMSSLResources() throws Exception {
        Document queryDoc = askQueryFromFile("MSSL_Registry_Entries.xml");
        RegistryValidator.isValid(queryDoc);
    }
    
    public void testInValidBasicResource() throws Exception {
        Document queryDoc = askQueryFromFile("MySpace_Invalid.xml");
        try {
            RegistryValidator.isValid(queryDoc);
        }catch(AssertionFailedError afe) {
            System.out.println("Caught an invalid exception expected: " + afe.toString());        
            System.out.println("Caught an invalid exception expected2: " + afe.getMessage());
        }
    }

    
    public void testValidSIA() throws Exception {
        Document queryDoc = askQueryFromFile("INT-WFS-SIAP.xml");
        RegistryValidator.isValid(queryDoc);
    }

    public void testValidTabularSkyService() throws Exception {
        Document queryDoc = askQueryFromFile("TabularSkyService_Fits_TRACE.xml");
        RegistryValidator.isValid(queryDoc);
    }
    
    
    public void testValidCEA() throws Exception {
        Document queryDoc = askQueryFromFile("CEATestapps.xml");
        RegistryValidator.isValid(queryDoc);
    }
    
    public void testValidCEADCRegistry() throws Exception {
        Document queryDoc = askQueryFromFile("CEADataCentreCDSRegistryEntry.xml");
        RegistryValidator.isValid(queryDoc);
    }
    
    public void testValidCEADataRegistry() throws Exception {
        Document queryDoc = askQueryFromFile("CEADataCentreRegistryEntry.xml");
        RegistryValidator.isValid(queryDoc);
    }
    
    public void testValidCEARegistry() throws Exception {
        Document queryDoc = askQueryFromFile("CEARegistry.xml");
        RegistryValidator.isValid(queryDoc);
    }    
    
    
    public void testValidCEASEC() throws Exception {
        Document queryDoc = askQueryFromFile("CEADataCentreSECRegistryEntry.xml");
        RegistryValidator.isValid(queryDoc);
    }
    
    
    
    
    public void testValidCEAHTTP() throws Exception {
        Document queryDoc = askQueryFromFile("CEAHttpRealapps.xml");
        RegistryValidator.isValid(queryDoc,"VODescription");
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