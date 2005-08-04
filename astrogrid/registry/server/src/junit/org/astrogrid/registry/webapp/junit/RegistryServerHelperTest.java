package org.astrogrid.registry.webapp.junit;

import java.io.InputStream;
import java.io.File;
import java.util.Iterator;
import org.astrogrid.xmldb.client.XMLDBFactory;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.apache.axis.AxisFault;

import java.util.HashMap;

import junit.framework.*;
import org.astrogrid.registry.server.admin.RegistryAdminService;
import org.astrogrid.registry.server.RegistryServerHelper;
import org.astrogrid.registry.common.RegistrySchemaMap;
import java.util.Map;
import java.util.Iterator;
import org.astrogrid.config.Config;
//import org.astrogrid.config.FallbackConfig;

/**
 * Class: RegistryServerHelperTest
 * Description: Tests out the REgistryServerHelper which is now only grabbing managed authority ids from the
 * registry the other methods have been factored up to the common side of the registry.
 * @author Kevin Benson
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RegistryServerHelperTest extends TestCase {
    
    
    public RegistryServerHelperTest() {
        
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
     * Method: testGetManagedAuthorities
     * Description: test to query for all managed authorities owned by theis registry.
     * @throws Exception standard junit exception to be thrown.
     */   
    public void testGetManagedAuthorities() throws Exception {
        HashMap hm = RegistryServerHelper.getManagedAuthorities("astrogridv0_10","0.10");
        assertTrue((hm.size() > 0));
    }

}