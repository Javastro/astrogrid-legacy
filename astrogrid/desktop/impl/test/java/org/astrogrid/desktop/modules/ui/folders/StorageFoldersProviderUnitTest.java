/**
 * 
 */
package org.astrogrid.desktop.modules.ui.folders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.astrogrid.desktop.alternatives.InThreadExecutor;
import org.astrogrid.desktop.modules.system.XStreamXmlPersist;
import org.astrogrid.desktop.modules.system.XmlPersist;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.system.ui.UIContextImpl;
import org.astrogrid.desktop.modules.ui.scope.ScopeHistoryProvider;
import org.astrogrid.io.Piper;

import junit.framework.TestCase;

/** unit test for storage folders - check that we can at least
 * initialize, and persist the initialized contents to disk.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 31, 20075:58:48 PM
 */
public class StorageFoldersProviderUnitTest extends TestCase {

    private XmlPersist xml;
    private Preference pref;
    private UIContext ui;

    protected void setUp() throws Exception {
        super.setUp();
        xml = new XStreamXmlPersist();
        pref = new Preference();
        File f = File.createTempFile("StorageFoldersProviderTest",null);
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
        pref = null;
        ui = null;
    }
    
    public void testIt() throws Exception {
        // do it in the EDT, so that the write-out to file happens without a racecondition.
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {        
                StorageFoldersProvider prov = new StorageFoldersProvider(ui,pref,xml);
                assertNotNull(prov.getList());
                assertEquals(3,prov.getList().size());
                assertTrue(prov.getList().get(0) instanceof StorageFolder);
                
                //uncomment to peek at the written output.
              /* try {
                    Piper.pipe(new FileInputStream(prov.getStorageLocation()),System.out);
                } catch (FileNotFoundException x) {
                    fail(x.getMessage());
                } catch (IOException x) {
                    fail(x.getMessage());
                }*/
                    
                // now try to reload this.
                List candidate = new ArrayList();
                prov.load(prov.getStorageLocation(),candidate);
                assertEquals(prov.getList().size(),candidate.size());
                
                assertEquals(prov.getList().get(0),candidate.get(0));                  
            }
        });
    }

}
