/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.dialogs.RegistryGoogle;
import org.astrogrid.acr.dialogs.ResourceChooser;
import org.astrogrid.acr.dialogs.ToolEditor;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 9, 20084:57:15 PM
 */
public class DialogModuleIntegrationTest extends InARTestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testRegistryGoogle() throws Exception {
        assertComponentExists(RegistryGoogle.class,"dialogs.registryGoogle");
    }

    public void testResourceChooser() throws Exception {
        assertComponentExists(ResourceChooser.class,"dialogs.resourceChooser");
    }
    
    public void testToolEditor() throws Exception {
        assertComponentExists(ToolEditor.class,"dialogs.toolEditor");
    }
    
    
    public static Test suite() {
        return new ARTestSetup(new TestSuite(DialogModuleIntegrationTest.class));
    }
}
