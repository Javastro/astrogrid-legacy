package uk.ac.roe.astrogrid.tests.workbench.gui;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.ui.ApplicationLauncher;
import org.astrogrid.acr.ui.AstroScope;
import org.astrogrid.acr.ui.HelioScope;
import org.astrogrid.acr.ui.Lookout;
import org.astrogrid.acr.ui.MyspaceBrowser;
import org.astrogrid.acr.ui.RegistryBrowser;

import uk.ac.roe.astrogrid.tests.RuntimeRequiringTestCase;
import uk.ac.roe.astrogrid.tests.agatroe.TestUser;

/**
 * Test that the dialog classes are present in the workbench.
 * This test requires user intervention.
 * @author jdt
 *
 */
public class DialogsTest extends RuntimeRequiringTestCase {

    
    public void setUp() throws Exception {
        super.setUp();
        getLog().info("Close the dialog if/when it opens");

        
    }
    private void login() throws Exception {
        Community comm = (Community) getAcr().getService(Community.class);
        comm.login(TestUser.USER, TestUser.PASS, TestUser.COMMUNITY);
    }
    public void tearDown() throws Exception {
        super.tearDown();
        getLog().info("Done");
        logout();
    }
    private void logout() throws Exception {
        Community comm = (Community) getAcr().getService(Community.class);
        comm.logout();
    }
    public void testRegistryBrowserPresent() throws Exception {
        getLog().debug("Opening registry browser...");
        RegistryBrowser browser = (RegistryBrowser) getAcr().getService(RegistryBrowser.class);
        browser.show();
        
    }

    public void testAstroScopePresent() throws Exception {
        getLog().debug("Opening AstroScope...");
        AstroScope as = (AstroScope) getAcr().getService(AstroScope.class);
        as.show();
    
    }
    
    public void testHelioScopePresent() throws Exception {
        getLog().debug("Opening HelioScope...");
        HelioScope as = (HelioScope) getAcr().getService(HelioScope.class);
        as.show();
    }
    
    public void testMySpaceBrowserPresent() throws Exception {
        login();
        getLog().debug("Opening MySpace browser...");
        MyspaceBrowser as = (MyspaceBrowser) getAcr().getService(MyspaceBrowser.class);
        as.show();
    }
    
    public void testLookoutPresent() throws Exception {
        login();
        getLog().debug("Opening Lookout...");
        Lookout as = (Lookout) getAcr().getService(Lookout.class);
        as.show();
    }

    public void testTaskLauncherPresent() throws Exception {
        login();
        getLog().debug("Opening TaskLauncher...");
        ApplicationLauncher as = (ApplicationLauncher) getAcr().getService(ApplicationLauncher.class);
        as.show();
    }

}