package org.astrogrid.desktop.modules.ui.folders;

import java.io.File;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import junit.framework.TestCase;

import org.astrogrid.desktop.alternatives.InThreadExecutor;
import org.astrogrid.desktop.modules.system.XmlPersist;
import org.astrogrid.desktop.modules.system.XStreamXmlPersist;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.system.ui.UIContextImpl;

public class PersistentTreeProviderUnitTest extends TestCase {

    private UIContext ui;
    private File file;
    private final XmlPersist persister = new XStreamXmlPersist();
    private final DefaultMutableTreeNode testNode = 
         (DefaultMutableTreeNode) new JTree().getModel().getRoot();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ui = new UIContextImpl(null, new InThreadExecutor(), null, null);
        file = File.createTempFile(getClass().getName(), ".xml");
        file.deleteOnExit();
    }

    public void testTree() throws Exception {
        TreeNode node1 =
            (TreeNode) createTreeProvider().getTreeModel().getRoot();
        assertNotNull(node1);
        TreeNode node2 = createTreeProvider().getDefaultRoot();
        assertEquals(BranchBeanUnitTest.toString(node1),
                     BranchBeanUnitTest.toString(node2));
    }

    public void testPersistentTreeProvider() {
        PersistentTreeProvider prov = createTreeProvider();
        assertEquals(prov.getTreeModel(), prov.getDefaultTreeModel());
    }

    public void testUpdateList() throws Exception {
        PersistentTreeProvider prov1 = createTreeProvider();
        DefaultTreeModel model1 = prov1.getDefaultTreeModel();
        DefaultMutableTreeNode root1 = 
            (DefaultMutableTreeNode) model1.getRoot();
        int nChild = root1.getChildCount();
        assertTrue(nChild > 1);
       
        DefaultMutableTreeNode newChild =
            new DefaultMutableTreeNode(new Folder("name", "ic"), false);
        root1.add(newChild);
        model1.nodeStructureChanged(root1);
        prov1.waitForUpdate();

        assertEquals(nChild + 1, root1.getChildCount());

        TreeProvider prov2 = createTreeProvider();
        assertTrue(prov1 != prov2);
        DefaultMutableTreeNode root2 =
            (DefaultMutableTreeNode) prov2.getTreeModel().getRoot();
        assertTrue(root1 != root2);
        assertEquals(nChild + 1, root2.getChildCount());

        assertEquals(newChild.toString(),
                     root1.getChildAt(nChild).toString());
        assertEquals(root1.getChildAt(nChild).toString(),
                     root2.getChildAt(nChild).toString());

        assertEquals(BranchBeanUnitTest.toString(root1),
                     BranchBeanUnitTest.toString(root2));
    }

    /**
     * Returns a new test TreeProvider.
     *
     * @return  provider
     */
    private PersistentTreeProvider createTreeProvider() {
        return new TestTreeProvider(ui, file, persister);
    }

    /**
     * PersistentTreeProvider for use in testing.
     */
    private static class TestTreeProvider extends PersistentTreeProvider {

        public TestTreeProvider(UIContext parent, File storage,
                                XmlPersist persister) {
            super(parent, storage, persister);
            init(new DefaultTreeModel(new DefaultMutableTreeNode("root",true)));
        }

        @Override
        public DefaultMutableTreeNode getDefaultRoot() {

            // Uses the noddy TreeModel provided by JTree.  If the JRE
            // changes sufficiently that this is no longer present or useful
            // then will have to provide a little hand-built TreeModel instead.
            return (DefaultMutableTreeNode) new JTree().getModel().getRoot();
        }
    }
}
