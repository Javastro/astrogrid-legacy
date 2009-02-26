/**
 * 
 */
package org.astrogrid.desktop.modules.file;

import java.net.URI;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.file.Info;
import org.astrogrid.acr.file.Manager;
import org.astrogrid.acr.file.Name;
import org.astrogrid.acr.file.Systems;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** Test that hivemind can instantiate all file module components.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 30, 20083:45:26 PM
 */
public class FileModuleIntegrationTest extends InARTestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    
    public void testManager() throws Exception {
        final Manager m = (Manager)assertServiceExists(Manager.class,"file.manager");
        m.listChildren(new URI("/"));
    }
    
    public void testSystems() throws Exception {
        final Systems s = (Systems)assertServiceExists(Systems.class,"file.systems");
        s.listSchemes();
        }
    
    public void testInfo() throws Exception {
       final Info i = (Info)assertServiceExists(Info.class,"file.info");
       i.isFolder(new URI("/"));
        
    }

    public void testName() throws Exception {
        final Name n = (Name)assertServiceExists(Name.class,"file.name");
        n.getScheme(URI.create("http://foo.bar.com"));
    }

    
    public static Test suite() {
        return new ARTestSetup(new TestSuite(FileModuleIntegrationTest.class));
    }

}
