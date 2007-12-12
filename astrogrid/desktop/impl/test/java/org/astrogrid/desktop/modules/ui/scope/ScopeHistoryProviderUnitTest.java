/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.astrogrid.acr.cds.SesamePositionBean;
import org.astrogrid.desktop.alternatives.InThreadExecutor;
import org.astrogrid.desktop.modules.system.XStreamXmlPersist;
import org.astrogrid.desktop.modules.system.XmlPersist;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.system.ui.UIContextImpl;
import org.astrogrid.desktop.modules.ui.comp.DoubleDimension;
import org.astrogrid.desktop.modules.ui.scope.ScopeHistoryProvider.PositionHistoryItem;
import org.astrogrid.io.Piper;

import junit.framework.TestCase;

/** test the persistence mechanism in scopeHistoryProvider.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 31, 20074:17:20 PM
 */
public class ScopeHistoryProviderUnitTest extends TestCase {

    private XmlPersist xml;
    private ScopeHistoryProvider prov;
    private Preference pref;
    private UIContext ui;

    protected void setUp() throws Exception {
        super.setUp();
        xml = new XStreamXmlPersist();
        pref = new Preference();
        File f = File.createTempFile("ScopeHistoryProvider",null);
        f.delete();
        f.mkdir();
        pref.setValue(f.toString());
        f.deleteOnExit();
        // a ui context that executes background threads right away.
        ui = new UIContextImpl(null,new InThreadExecutor(),null,null);
      
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        xml = null;
        prov = null;
        pref = null;
        ui = null;
    }
    
    public void testGetEmptyList() throws Exception {
        this.prov = new ScopeHistoryProvider(ui,pref,xml);
        assertNotNull(prov.getList());
        assertEquals(0,prov.getList().size());
    }
    
    public void testPopulatedList() throws Exception {
        this.prov = new ScopeHistoryProvider(ui,pref,xml);

        final PositionHistoryItem i1 = new PositionHistoryItem();
        i1.setRadius(new DoubleDimension(2.0,1.1));
        SesamePositionBean pos = new SesamePositionBean();
        pos.setTarget("m32");
        pos.setService("NED");
        pos.setRa(234.2434534);
        pos.setDec(-12.666);
        i1.setPosition(pos);
        
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                prov.getList().add(i1); // triggers a save happening.
                // were doing this on the EDT to prevent a race condition.
            }
        });
        
        //uncomment to peek at the written output.
       //Piper.pipe(new FileInputStream(prov.getStorageLocation()),System.out);
        
        // maually deserialize, and compare..
        List candidate = new ArrayList();
        prov.load(prov.getStorageLocation(),candidate);
        assertEquals(1,candidate.size());
        
        assertEquals(i1,candidate.get(0));
        
    }
    
    
    

}
