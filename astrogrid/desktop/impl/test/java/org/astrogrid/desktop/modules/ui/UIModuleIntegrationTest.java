/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.ui.ApplicationLauncher;
import org.astrogrid.acr.ui.AstroScope;
import org.astrogrid.acr.ui.FileManager;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 9, 20085:03:51 PM
 */
public class UIModuleIntegrationTest  extends InARTestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testApplicationLauncher() throws Exception {
        assertComponentExists(ApplicationLauncher.class,"ui.applicationLauncher");
    }

    public void testAstroscope() throws Exception {
        assertComponentExists(AstroScope.class,"ui.astroscope");
    }
    
    public void testFileManager() throws Exception {
        assertComponentExists(FileManager.class,"ui.fileManager");
    }
    
    
    public void testRegistryBrowser() throws Exception {
        assertComponentExists(RegistryBrowser.class,"ui.registryBrowser");
    }    
    
    
    public static Test suite() {
        return new ARTestSetup(new TestSuite(UIModuleIntegrationTest.class));
    }

}
