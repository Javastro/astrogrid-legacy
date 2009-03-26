/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import javax.swing.RepaintManager;
import junit.framework.TestSuite;

import org.astrogrid.acr.ui.ApplicationLauncher;
import org.astrogrid.acr.ui.AstroScope;
import org.astrogrid.acr.ui.FileManager;
import org.astrogrid.acr.ui.QueryBuilder;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.jdesktop.swinghelper.debug.JunitCheckThreadViolationRepaintManager;

/** Integration test that exercises UI and checks for EDT violations.
 * Each test just needs to open a ui. the instumentation code in the fixture will post
 * a junit failure if anything is amiss.
 * 
 * Cannot be run in same AR instance as other unit tests, as requires installation of a
 * custom repaint manager.
 * 
 * This test enforces the design rule that any AR service that displays a UI component
 * (be that UI window, or a dialogue), must be defensively thread safe - so that 
 * if it is invoked on an arbitrary thread, it's the responsibility of the service implementation
 * to ensure that all swing instantiation and operations are on the EDT, etc.
 * Whether this is done within the service implementation, or using service interceptors
 * doesn't matter - either way, a client of the service should be able to call from any thread
 * and get the correct behaviour.
 * 
 * (this is the only way to be sure of EDT-safety - considering these methods can
 * be called from within UI, and via xmlrpc / rmi interface
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 31, 200712:12:32 PM
 */
public class EdtViolationIntegrationTest extends InARTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        JunitCheckThreadViolationRepaintManager.checkAndClearFailure(); // actually does the verification
    }
    
    public void testStartup() throws Exception {
        // no need to do anything - if the test gets run, then startup has worked correctly.
    }
    
    public void testFileExplorer() throws Exception {
        final FileManager fm = assertComponentExists(FileManager.class,"ui.fileManager");
        fm.show();
    }
    
    public void testAstroscope() throws Exception {
        final AstroScope as = assertComponentExists(AstroScope.class,"ui.astroscope");
        as.show();
    }
    
    public void testTaskRunner() throws Exception {
        final ApplicationLauncher al = assertComponentExists(ApplicationLauncher.class,"ui.applicationLauncher");
        al.show();
    }
    
    public void testVoExplorer() throws Exception {
        final RegistryBrowser rb = assertComponentExists(RegistryBrowser.class,"ui.registryBrowser");
        rb.show();
    }
    
    public void testQueryBuilder() throws Exception {
        final QueryBuilder qb = assertComponentExists(QueryBuilder.class,"ui.queryBuilder");
        qb.show();
    }
    // test all the dialogues...no possible automatically, as the they're blocking - so can't progress the test without user input.
//    public void testPreferencesDialogue() throws Exception {
//        final Runnable config = (Runnable)assertComponentExists(Runnable.class,"system.configDialogue");
//        // should be thread-safe to run from any thread.
//        config.run();
//    }
//    
//    
//    public void testLoginDialogue() throws Exception {
//        final Community comm = (Community)assertComponentExists(Community.class,"astrogrid.community");
//        comm.guiLogin();
//    }
//    
//    public void testResourceChooser() throws Exception {
//        final ResourceChooser rc = (ResourceChooser)assertComponentExists(ResourceChooser.class,"dialogs.resourceChooser");
//        rc.chooseFolder("test",true);        
//    }
//    
//    public void testToolEditor() throws Exception {
//        ToolEditor ti = (ToolEditor)assertComponentExists(ToolEditor.class,"dialogs.toolEditor");
//        ti.selectAndBuild();
//    }
//    
//    public void testRegistryGoogle() throws Exception {
//        RegistryGoogle rg = (RegistryGoogle)assertComponentExists(RegistryGoogle.class,"dialogs.registryGoogle");
//        rg.selectResources("test",true);
//    }

    public static junit.framework.Test suite() {
        return new ARTestSetup(new TestSuite(EdtViolationIntegrationTest.class)){
            @Override
            protected void setUp() throws Exception {
                // install repaint manager.
                if (fixture != null) {
                    fail("Can't run this test - fixture has already been created, and so UI is already present");
                }               
                RepaintManager.setCurrentManager(new JunitCheckThreadViolationRepaintManager());
                super.setUp();
            }
        };
        
    }
    
}
