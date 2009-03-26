/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.desktop.modules.system.contributions.HelpItemContribution;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.joda.time.Duration;

import junit.framework.TestCase;
import static org.astrogrid.Fixture.*;
import static org.easymock.EasyMock.*;

/** Unit test for help server.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 22, 20088:07:45 PM
 */
public class HelpServerImplUnitTest extends TestCase {

    private BrowserControl browser;
    private UIContext context;
    private URL emptyHelpMap;
    private URL helpMap;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.browser = createMockBrowser();
        this.context = createMockContext();
        emptyHelpMap = this.getClass().getResource("emptyHelpMap.xml");
        assertNotNull(emptyHelpMap);
        this.helpMap = this.getClass().getResource("testHelpMap.xml");
        assertNotNull(helpMap);
    }           

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        browser = null;
        context = null;
        emptyHelpMap = null;
        helpMap = null;
    }
    /**quickly test the help item contribution bean */
    public void testHelpItemContributionBean() throws Exception {
        HelpItemContribution a = new HelpItemContribution();
        HelpItemContribution b = new HelpItemContribution();
        assertEquals(a,b);
        a.setId("foo");
        assertFalse(a.equals(b));
        assertNotNull(a.toString());
        assertNotNull(b.toString());
        assertTrue(a.hashCode() != b.hashCode());
    }
    
    public void testEmptyHelpMap() throws Exception {
        replay(context,browser);
        HelpServerImpl help = createHelpServer(emptyHelpMap.toString());
        // if we ask for any key, expect nothing to happen.
        help.showHelpForTarget(null);
        help.showHelpForTarget(HelpItemContribution.HOME_ID);
        // 
        verify(context,browser);
    }
    
    /** should be same behaviour as empty help map */
    public void testInaccessibleHelpMap() throws Exception {
        replay(context,browser);
        HelpServerImpl help = createHelpServer(emptyHelpMap.toString() + "-missing");
        help.showHelpForTarget(null);
        help.showHelpForTarget(HelpItemContribution.HOME_ID);
        // 
        verify(context,browser);        
    }
    
    /** expect it to return the default 'top' url in both cases. */
    public void testShowTop() throws Exception {
        expectTop();
        expectTop();        
        replay(context,browser);
        HelpServerImpl help = createHelpServer(helpMap.toString());
        // ask for an item with a missing url
        help.showHelp();
        help.showHelpForTarget(HelpItemContribution.HOME_ID);
        // 
        verify(context,browser);        
    }
        
    
    /** expect it to return specified url */
    public void testKnownHelpId() throws Exception {
        expectHelpdesk();
        replay(context,browser);
        HelpServerImpl help = createHelpServer(helpMap.toString());
        // ask for an item with a known url
        help.showHelpForTarget("ag.helpdesk");
        // 
        verify(context,browser);        
    }
    
    /** expect it to fallback to default help id. */
    public void testUnknownHelpId() throws Exception {
        expectTop();
        replay(context,browser);
        HelpServerImpl help = createHelpServer(helpMap.toString());
        // ask for an item with a missing url
        help.showHelpForTarget("unknownHelpId");
        // 
        verify(context,browser);        
    }
    
    /** test that we can handle a help map with an entry that lacks a url 
     * parser is error tolerant, so this is about the only issue that could leak
     * into the system.
     * 
     * behaviour - entry with missing url is discarded, so when this 
     * key is requested, as it's not present, the TOP is shown.
     * 
     * @throws Exception
     */
    public void testMissingURL() throws Exception {
        expectTop();
        replay(context,browser);
        HelpServerImpl help = createHelpServer(helpMap.toString());
        // ask for an item with a missing url
        help.showHelpForTarget("missingURL");
        // 
        verify(context,browser);        
    }

    
    /** if an entry has a malformed URL, same behaviour as a missing entry
     * - top is shown in its place
     * @throws Exception
     */
    public void testMalformedURL() throws Exception {
        expectTop();
        replay(context,browser);
        HelpServerImpl help = createHelpServer(helpMap.toString());
        // ask for an item with a missing url
        help.showHelpForTarget("malformedURL");
        // 
        verify(context,browser);        
    }
    
    // can't write a test for case of a missing id - but ttest file contains one of these too.
    
    
    /** test that it creates help buttons that work. */
    public void testCreateHelpButton() throws Exception {
        expectHelpdesk();
        replay(context,browser);
        HelpServerImpl help = createHelpServer(helpMap.toString());
        JButton b = help.createHelpButton("ag.helpdesk");
        assertNotNull(b);
        b.doClick();
        verify(context,browser);       
    }

    // would be nice to test EnableHelpKey, but I don't know how to fire keyboard events at a component to test it.
    
    

/// supporting methods.    
    /** set an expectation on the browser that the 'helpdesk' entry will be shown.
     * @throws ACRException
     * @throws MalformedURLException
     */
    private void expectHelpdesk() throws ACRException, MalformedURLException {
        browser.openURL(new URL("http://www.astrogrid.org/support/"));
    }
    
    /** set an expectation on the browser that the TOP help entry will be shown.
     * @throws ACRException
     * @throws MalformedURLException
     */
    private void expectTop() throws ACRException, MalformedURLException {
        browser.openURL(new URL("http://help.astrogrid.org"));
    }
    /**
     * @param empty
     * @return
     * @throws MalformedURLException
     * @throws InvocationTargetException 
     * @throws InterruptedException 
     */
    private HelpServerImpl createHelpServer(String mapURL)
    throws MalformedURLException, InterruptedException, InvocationTargetException {
        HelpServerImpl help = new HelpServerImpl(browser
                ,mapURL
                ,context);
        // pretend to be the scheduler, and call the load method.
        assertEquals(Duration.ZERO,help.getDelay());
        assertNull(help.execute()); // no continuation

        return help;
    }
}
