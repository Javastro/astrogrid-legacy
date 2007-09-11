/**
 * 
 */
package org.astrogrid.desktop.modules.ui.folders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
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

/** unit test for resource  folders - check that we can at least
 * initialize, and persist the initialized contents to disk.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 31, 20075:58:48 PM
 */
public class ResourceListsProviderUnitTest extends TestCase {

    private XmlPersist xml;
    private Preference pref;
    private UIContext ui;

    protected void setUp() throws Exception {
        super.setUp();
        xml = new XStreamXmlPersist();
        pref = new Preference();
        File f = File.createTempFile("ResourceFoldersProviderTest",null);
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
                ResourceListsProvider prov = new ResourceListsProvider(ui,pref,xml);
                assertNotNull(prov.getList());
//                assertEquals(6,prov.getList().size());
                assertTrue(prov.getList().get(0) instanceof ResourceFolder);
                //@todo I really should check for subclasses here too.
                
                // now test a static - as it's not in the default setup at the moment.
                StaticList stat = new StaticList("testing");
                List resourceSet = new ArrayList();
                resourceSet.add(URI.create("ivo://foo/bar"));
                resourceSet.add(URI.create("ivo://fnind.dds/r"));
                resourceSet.add(URI.create("ivo://fdsdoo/bar"));                
                stat.setResourceSet(resourceSet);
                prov.getList().add(stat);
                
                //uncomment to peek at the written output.
/*                try {
                    Piper.pipe(new FileInputStream(prov.getStorageLocation()),System.out);
                } catch (FileNotFoundException x) {
                    fail(x.getMessage());
                } catch (IOException x) {
                    fail(x.getMessage());
                } 
   */                
                // now try to reload this.
                List candidate = new ArrayList();
                prov.load(prov.getStorageLocation(),candidate);
                int sz = candidate.size();
                assertEquals(prov.getList().size(),candidate.size());
                
                assertEquals(prov.getList().get(0),candidate.get(0));     
                
                // and now check that the static bit works...
                Object o = candidate.get(sz -1);
                assertTrue(o instanceof StaticList);
                Iterator expected = resourceSet.iterator();
                for (java.util.Iterator i = ((StaticList)o).getResourceSet().iterator(); i.hasNext(); ) {
                    assertEquals(expected.next(),i.next());
                }

            }
        });
    }

}
