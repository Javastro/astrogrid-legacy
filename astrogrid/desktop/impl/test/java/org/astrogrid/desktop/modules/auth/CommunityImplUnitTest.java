package org.astrogrid.desktop.modules.auth;

import ca.odell.glazedlists.EventList;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.JMenu;
import junit.framework.TestCase;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.desktop.modules.system.BackgroundExecutor;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.SnitchImpl;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.BackgroundWorkersMonitor;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.security.SecurityGuard;

/**
 * JUnit tests for CommunityImpl.
 *
 * @author Guy Rixon
 */
public class CommunityImplUnitTest extends TestCase {
  
  /**
   * To allow unit testing, some of the objects used by the security guard
   * must make mockeries of themselves.
   */
  public void setUp() {
    SimpleConfig.getSingleton().setProperty(
      "org.astrogrid.security.community.RegistryClient.mock",
      "true"
    );
    SimpleConfig.getSingleton().setProperty(
      "org.astrogrid.security.community.SsoClient.mock",
      "true"
    );
  }

  public void testGoodUser() throws Exception {
    Preference dontSnitch = new Preference();
    dontSnitch.setDefaultValue("false");
    SnitchImpl snitch = new SnitchImpl(null, null, null, null, dontSnitch);
    CommunityImpl sut = 
        new CommunityImpl(new MockUiContext(), 
                          null, 
                          snitch,
                          "/etc/grid-security/certificates");
    
    sut.login("frog", "croakcroak", "pond");
    
    /* These tests always fail until the security facade is updated to
     * supply mock credentials. Specifically, it must supply a certificate
     * chain from which the X500Principal may be taken.
    assertTrue(sut.isLoggedIn());
    SecurityGuard g = sut.getSecurityGuard();
    assertNotNull(g.getX500Principal());
    assertNotNull(g.getPrivateKey());
     */
  }
  
  
  public class MockUiContext implements UIContext {
    
    public Configuration getConfiguration() {
      return null;
    }

    public HelpServerInternal getHelpServer() {
      return null;
    }

    public BackgroundExecutor getExecutor() {
      return null;
    }

    public BrowserControl getBrowser() {
      return null;
    }

    public Map getWindowFactories() {
      return null;
    }

    public void showAboutDialog() {
      // Do nothing.
    }

    public void showPreferencesDialog() {
      // Do nothing.
    }

    public ButtonModel getLoggedInModel() {
      return new DefaultButtonModel();
    }

    public ButtonModel getThrobbingModel() {
      return null;
    }

    public BackgroundWorkersMonitor getWorkersMonitor() {
      return null;
    }

    public ButtonModel getVisibleModel() {
      return null;
    }

    public EventList getWindowList() {
      return null;
    }

    public EventList getPlasticList() {
      return null;
    }

    public EventList getTasksList() {
      return null;
    }

    public void registerWindow(UIComponent window) {
      // Do nothing.
    }

    public void unregisterWindow(UIComponent window) {
      // Do nothing.
    }

    public UIComponent findMainWindow() {
      return null;
    }

    public JMenu createWindowMenu() {
      return null;
    }

    public void show() {
      // Do nothing.
    }

    public void hide() {
      // Do nothing.
    }

    public void startThrobbing() {
      // Do nothing.
    }

    public void stopThrobbing() {
      // Do nothing.
    }

    public void setLoggedIn(boolean b) {
      // Do nothing.
    }

    public void setStatusMessage(String string) {
      // Do nothing.
    }

    public void actionPerformed(ActionEvent actionEvent) {
      // Do nothing.
    }
  }
  
}
