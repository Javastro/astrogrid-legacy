/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.folders;

import java.io.File;

import junit.framework.TestCase;

import org.astrogrid.desktop.alternatives.HeadlessUIComponent;
import org.astrogrid.desktop.alternatives.InThreadExecutor;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.system.ui.UIContextImpl;
import org.easymock.MockControl;

import ca.odell.glazedlists.EventList;

/** unit test for a general abstractFoldesprovider.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 27, 20078:07:47 PM
 */
public class AbstractFoldersProviderUnitTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		folder = new Folder();
		ui = new UIContextImpl(null,new InThreadExecutor(),null,null);
		f = File.createTempFile(this.getClass().getName(),".xml");
		f.deleteOnExit();
		prov = new TestFoldersProvider(ui,f);
	}
	Folder folder;
	FoldersProvider prov;
	File f;
	UIContext ui;

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
	//@fixme get this working again.
	public void testUpdateList() throws Exception {
		EventList el = prov.getList();
		Folder f2 = new Folder("fred","icon");
		el.add(f2);
		
		// now load it in separate instance, and check contents are the same - ie. it's been persisted.
		FoldersProvider prov1 = new TestFoldersProvider(ui,f);
		EventList el1 = prov1.getList();
		assertEquals(el,el1);
	}
	
	public void testUpdateListWithSubtype() throws Exception {
		EventList el = prov.getList();
		Folder f2 = new DumbResourceFolder("fred","icon");
		el.add(f2);
		
		// now load it in separate instance, and check contents are the same - ie. it's been persisted.
		FoldersProvider prov1 = new TestFoldersProvider(ui,f);
		EventList el1 = prov1.getList();
		assertEquals(el,el1);
	}
	
	public class TestFoldersProvider extends AbstractFoldersProvider {

		/**
		 * @param parent
		 * @param storage
		 */
		public TestFoldersProvider(UIContext parent, File storage) {
			super(parent, storage);
		}

		protected void initializeFolderList() {
			folderList.add(folder);
		}
	}

}
