/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer;

import java.awt.Component;
import java.awt.Font;
import java.util.Enumeration;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.folders.ResourceFolder;
import org.astrogrid.desktop.modules.ui.folders.ResourceTreeModel;

/** Resource tree that doesn't allow editing.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 17, 200911:29:07 AM
 */
public class UneditableResourceTree extends JTree {
    protected final Expander expander;
    protected  ResourceTreeModel model;
    /** Singleton renderer. */
    private static final TreeCellRenderer RESOURCE_RENDERER =
        new ResourceRenderer();    
    /**
     * 
     */
    public UneditableResourceTree(final ResourceTreeModel model) {
        super(model);
        setModel(model);
        CSH.setHelpIDString(this, "reg.resourceTree");       

        // Configure visual properties.
        getSelectionModel().setSelectionMode(TreeSelectionModel
                .SINGLE_TREE_SELECTION);
        setRootVisible(true);
        setShowsRootHandles(false);
        setExpandsSelectedPaths(true);



        // Ensure that the root node is never collapsed.  It's not useful to
        // collapse it, and users may find it hard to re-expand if it does
        // get collapse.
        expander = new Expander();
        addTreeWillExpandListener(expander);
        model.addTreeModelListener(expander);
        expander.ensureRootExpanded();

        setCellRenderer(RESOURCE_RENDERER);        
    }
    /**
     * Returns the folder associated with a given tree node.
     *
     * @param  node  tree node
     * @return  folder
     */
    public ResourceFolder getFolder(final TreeNode node) {
        return (ResourceFolder) ((DefaultMutableTreeNode) node).getUserObject();
    }    
    /**
     * Returns the folder associated with a given path in this tree.
     *
     * @param   path  path
     * @return   folder at path
     */
    public ResourceFolder getFolder(final TreePath path) {
        return path == null
        ? null
                : getFolder((TreeNode) path.getLastPathComponent());
    }

    /**
     * Returns the TreeNode in this tree associated with a given folder,
     * or null if it's not in the tree.
     *
     * @param  folder  folder
     * @return   node, or null
     */
    public DefaultMutableTreeNode getNode(final ResourceFolder folder) {
        final DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        for (final Enumeration en = root.depthFirstEnumeration();
        en.hasMoreElements();) {
            final DefaultMutableTreeNode node =
                (DefaultMutableTreeNode) en.nextElement();
            if (node.getUserObject() == folder) {
                return node;
            }
        }
        return null;
    }

    /**
     * Returns the TreePath in this tree associated with a given folder,
     * or null if it's not in the tree.
     *
     * @param  folder  folder
     * @return   path, or null
     */
    public TreePath getPath(final ResourceFolder folder) {
        final DefaultMutableTreeNode node = getNode(folder);
        return node == null ? null
                : new TreePath(node.getPath());
    }

    /**
     * Returns the currently selected folder.
     *
     * @return   selected folder, or null
     */
    public ResourceFolder getSelectedFolder() {
        return getFolder(getSelectionPath());
    }

    /**
     * Sets up this tree in a reasonable state for viewing once it has been
     * populated.
     */
    public void initialiseViewAndSelection() {
        expandPath(new TreePath(getModel().getRoot()));
        final int nrow = getRowCount();
        for (int ir = 0; ir < nrow; ir++) {
            final ResourceFolder folder = getFolder(getPathForRow(ir));
            final boolean isSuitable = true;
            if (isSuitable) {
                setSelectionRows(new int[] {ir});
                return;
            }
        }
    }

    @Override
    public final void setModel(final TreeModel model) {
        if (expander != null) {
            this.model.removeTreeModelListener(expander);
            model.addTreeModelListener(expander);
        }
        super.setModel(model);
        this.model = (ResourceTreeModel) model;
    }

    /**
     * Sets the currently selected folder.
     *
     * @param  folder to select, or null
     */
    public void setSelectedFolder(final ResourceFolder folder) {
        final TreePath path = getPath(folder);
        setSelectionPath(path);
        if (path != null) {
            scrollPathToVisible(path);
        }
    }

    /**
     * Implements policy for keeping the expansion state of the tree sensible.
     *
     * The policy is:
     *  - The root of the tree is never permitted to collapse: there's no good 
     *    reason to do this, and it would be confusing for the user if 
     *    it happened (not obvious how to undo it).
     *  - If the root has only a single child, presumably the canned Examples
     *    node, this is expanded.  Although anomalous, it means that a user
     *    seeing this for the first time will get the right idea about the
     *    hierarchical nature of it.
     */
    private class Expander implements TreeWillExpandListener, TreeModelListener {
        /**
         * Ensures that the policy is implemented.  Defers execution, which is
         * required when invoking from tree listener callbacks.
         */
        public void ensureRootExpanded() {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    doEnsureRootExpanded();
                }
            });
        }
        public void treeNodesChanged(final TreeModelEvent evt) { 
        }

        public void treeNodesInserted(final TreeModelEvent evt) {
            // Not obvious, but this is required.  Otherwise the root can be
            // expanded when empty, which behaves like being collapsed.
            if (isRoot(evt.getTreePath())) {
                ensureRootExpanded();
            }
        }
        public void treeNodesRemoved(final TreeModelEvent evt) {
        }
        public void treeStructureChanged(final TreeModelEvent evt) {
        }
        public void treeWillCollapse(final TreeExpansionEvent evt) throws ExpandVetoException {
            // Never permit root collapse.
            if (isRoot(evt.getPath())) {
                throw new ExpandVetoException(evt, "Refuse to collapse root");
            }
        }

        public void treeWillExpand(final TreeExpansionEvent evt) {
        }

        /** Does the work in-thread for ensureRootExpanded. */
        private void doEnsureRootExpanded() {

            // Root should always be expanded.
            if (!isExpanded(0) && model.getChildCount(model.getRoot()) > 0) {
                expandRow(0);
            }

            // If the root has only one child (presumably the canned Examples
            // node) then expand this as well.  Although anomalous, it
            // means that users seeing this component for the first time
            // will get a list of resources they can click on rather than
            // just a single folder which it's less obvious what to do with.
            if (model.getChildCount(model.getRoot()) == 1) {
                expandRow(1);
            }
        }

        /** Determines whether a path represents the root of the tree model. */
        private boolean isRoot(final TreePath path) {
            return path.getPathCount() == 1;
        }
    }

    /**
     * Renderer implementation for ResourceFolder nodes.
     */
    private static class ResourceRenderer extends DefaultTreeCellRenderer {

        private Font plainFont;
        private Font emFont;

        @Override
        public Component getTreeCellRendererComponent(final JTree tree, final Object value,
                final boolean selected,
                final boolean expanded,
                final boolean leaf, final int row,
                final boolean hasFocus) {
            final Component comp =
                super.getTreeCellRendererComponent(tree, value, selected,
                        expanded, leaf, row,
                        hasFocus);
            if (comp instanceof JLabel) {
                final JLabel label = (JLabel) comp;
                final ResourceFolder folder = 
                    (ResourceFolder)
                    ((DefaultMutableTreeNode) value).getUserObject();
                //removed use of italics for notation of subscribed resources.        label.setFont(getFont(folder.getSubscription() != null));
                label.setText(folder.getName());
                label.setIcon(folder.getIcon());
                label.setDisabledIcon(null);
            }
            return comp;
        }

        /**
         * Returns a suitable font.
         *@todo unused - remove?
         * @param  emph  true for emphasised, false for plain
         * @return font
         */
        private Font getFont(final boolean emph) {
            if (plainFont == null) {
                final Font font = getFont();
                plainFont = font.deriveFont(Font.PLAIN);
                emFont = font.deriveFont(Font.ITALIC);
            }
            return emph ? emFont : plainFont;
        }
    }
}
