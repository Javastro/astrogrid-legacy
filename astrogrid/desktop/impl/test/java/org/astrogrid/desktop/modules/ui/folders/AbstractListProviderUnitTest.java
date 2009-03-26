/**
 * 
 */
package org.astrogrid.desktop.modules.ui.folders;

import java.io.File;

import javax.swing.SwingUtilities;

import junit.framework.TestCase;

import org.astrogrid.desktop.alternatives.InThreadExecutor;
import org.astrogrid.desktop.modules.system.XStreamXmlPersist;
import org.astrogrid.desktop.modules.system.XmlPersist;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.system.ui.UIContextImpl;
import org.astrogrid.desktop.modules.ui.folders.AbstractListProvider;
import org.astrogrid.desktop.modules.ui.folders.Folder;
import org.astrogrid.desktop.modules.ui.folders.ListProvider;
import ca.odell.glazedlists.EventList;

/** unit test for a general abstractFoldesprovider.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 27, 20078:07:47 PM
 */
public class AbstractListProviderUnitTest extends TestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		folder = new Folder();
		ui = new UIContextImpl(null,new InThreadExecutor(),null,null);
		f = File.createTempFile(this.getClass().getName(),".xml");
		f.deleteOnExit();
        xml = new XStreamXmlPersist();
		prov = new TestFoldersProvider(ui,f,xml);
	}
	protected Folder folder;
	protected ListProvider prov;
	protected File f;
	protected UIContext ui;
    protected XmlPersist xml;	

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		folder = null;
		prov = null;
		f = null;
		ui = null;
	}
	public void testList() throws Exception {
		EventList el = prov.getList();
		assertNotNull(el);
		assertEquals(1,el.size());
		assertEquals(folder,el.get(0));
	}
	
	public void testUpdateList() throws Exception {
		final EventList el = prov.getList();
		final Folder f2 = new Folder("fred","icon");
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                el.add(f2); // triggers a save happening.
                // were doing this on the EDT to prevent a race condition.
            }
        });
		
		// now load it in separate instance, and check contents are the same - ie. it's been persisted.
		ListProvider prov1 = new TestFoldersProvider(ui,f,xml);
		EventList el1 = prov1.getList();
		assertEquals(el,el1);
	}
	
	public void testUpdateListWithSubtype() throws Exception {
		final EventList el = prov.getList();
		final Folder f2 = new FolderSubclass("fred","icon",true);

        SwingUtilities.invokeAndWait(new Runnable() {
		
	      public void run() {
              el.add(f2); // triggers a save happening.
              // were doing this on the EDT to prevent a race condition.
          }
      });
      
      // now load it in separate instance, and check contents are the same - ie. it's been persisted.
      ListProvider prov1 = new TestFoldersProvider(ui,f,xml);
      EventList el1 = prov1.getList();
      assertEquals(el,el1);
	}

	/** subclass for testing */
	public static class FolderSubclass extends Folder {

        private boolean b; // can't be final

        // no-args constructor required
        public FolderSubclass() {
        }
        public FolderSubclass(String string, String string2, boolean b) {
            super(string,string2);
            this.b = b;
            
        }
	}
	
	public class TestFoldersProvider extends AbstractListProvider {

		/**
		 * @param parent
		 * @param storage
		 * @param xml 
		 */
		public TestFoldersProvider(UIContext parent, File storage, XmlPersist xml) {
			super(parent, storage, xml);
		}

		@Override
        protected void initializeFolderList() {
			getList().add(folder);
		}
	}

}
