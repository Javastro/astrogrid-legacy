package org.astrogrid.desktop.modules.ui.voexplorer;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.WindowConstants;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.system.XmlPersist;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.folders.BranchBean;
import org.astrogrid.desktop.modules.ui.folders.ResourceBranch;
import org.astrogrid.desktop.modules.ui.folders.ResourceFolder;
import org.astrogrid.desktop.modules.ui.folders.ResourceTreeModel;
import org.astrogrid.desktop.modules.ui.folders.SmartList;
import org.astrogrid.desktop.modules.ui.folders.StaticList;
import org.astrogrid.desktop.modules.ui.folders.XQueryList;

import com.l2fprod.common.swing.BaseDialog;

/**
 * {@link JTree} containing resource folders, displayed on left hand side of VOExplorer.
 * The tree model is a {@link javax.swing.tree.DefaultTreeModel},
 * and its nodes are {@link javax.swing.tree.DefaultMutableTreeNodes}
 * with user objects which are instances of 
 * {@link org.astrogrid.desktop.modules.ui.voexplorer.ResourceFolder}.
 *
 * <p>Major parts of the implementation have been lifted from the
 * <code>org.astrogrid.desktop.modules.ui.voexplorer.ResourceLists</code> class.
 *
 * @author   Mark Taylor
 * @author   Noel Winstanley
 * @since    7 Sep 2007
 */
public class ResourceTree extends JTree {

    /** Singleton renderer. */
    private static final TreeCellRenderer RESOURCE_RENDERER =
        new ResourceRenderer();


    private final static DataFlavor[] inputFlavors = new DataFlavor[]{
        VoDataFlavour.LOCAL_RESOURCE_ARRAY
        ,VoDataFlavour.LOCAL_RESOURCE
        ,VoDataFlavour.RESOURCE_ARRAY
        ,VoDataFlavour.RESOURCE
        ,VoDataFlavour.LOCAL_URI
        ,VoDataFlavour.LOCAL_URI_ARRAY
        ,VoDataFlavour.URI_LIST
        ,VoDataFlavour.PLAIN
        ,VoDataFlavour.STRING
        ,VoDataFlavour.TREENODE
    };
    private final static Predicate dragPredicate = new Predicate() {
        public boolean evaluate(final Object arg0) {
            return ArrayUtils.contains(inputFlavors,arg0);
        }
    };
    private static final Log logger = LogFactory.getLog(ResourceTree.class);

    private ResourceTreeModel model;
    private final VOExplorerImpl parent;
    private final ResourceChooserInternal chooser;
    private final XmlPersist persister;
    private final Action addStatic;
    private final Action addSmart;
    private final Action addXQuery;
    private final Action addBranch;
    private final Action addSubscription;
    private final Action remove;
    private final Action properties;
    private final Action duplicate;
    private final Action rename;
    private final Action export;
    private final Action ymport;
    private final JPopupMenu popup;
    private final Expander expander;
    private Point mousePos;
    private boolean dndIsDragging;
    private boolean transferIsDragging;


    private final Action refresh;


    /**
     * Constructor.
     *
     * @param   tree model - must be a ResourceTreeModel with correct content
     */
    public ResourceTree(final TreeModel model, final VOExplorerImpl parent,
                        final ResourceChooserInternal chooser, final XmlPersist persister
                        , final Action refreshAction) {
        super(model);
        this.refresh = refreshAction;
        setModel(model);
        this.parent = parent;
        this.chooser = chooser;
        this.persister = persister;
        CSH.setHelpIDString(this, "reg.resourceTree");

        // Install a handler for dragging'n'dropping.
        setTransferHandler(new TreeTransferHandler());
       
        // Arrange to call the TransferHandler's exportAsDrag method when
        // a drag has been initiated.  It would be easier just to call 
        // setDragEnabled(true) here, but that only permits dragging
        // the currently selected node.  We would like to be able to 
        // drag whatever is under the cursor.
        final DragGestureListener dgl = new DragGestureListener() {
            public void dragGestureRecognized(final DragGestureEvent evt) {
                final JComponent comp = ResourceTree.this;
                final TransferHandler th = comp.getTransferHandler();
                th.exportAsDrag(comp, evt.getTriggerEvent(),
                                evt.getDragAction());
            }
        };
        DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, dgl);

        // We need to keep track of the mouse position for use when working
        // out what to drag.
        addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(final MouseEvent evt) {
                mousePos = evt.getPoint();
            }
            public void mouseDragged(final MouseEvent evt) {
            }
        });

        // Keep track of when a drag is in progress.
        DragSource.getDefaultDragSource().addDragSourceListener(new DragSourceAdapter() {
            @Override
            public void dragEnter(final DragSourceDragEvent evt) {
                dndIsDragging = true;
            }
            @Override
            public void dragExit(final DragSourceEvent evt) {
                dndIsDragging = false;
                endDrag();
            }
            @Override
            public void dragOver(final DragSourceDragEvent evt) {
                dndIsDragging = true;
            }
            @Override
            public void dragDropEnd(final DragSourceDropEvent evt) {
                dndIsDragging = false;
                endDrag();
            }
        });

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
        // Set up a popup menu.
        addSmart = new TreeAction("New Smart List",
                                  IconHelper.loadIcon("editadd16.png"),
                                  "Use rules to define a new set of resources"
                                  ,KeyStroke.getKeyStroke(KeyEvent.VK_N,UIComponentMenuBar.MENU_KEYMASK)
                                  );
        addStatic = new TreeAction("New Static List",
                                   IconHelper.loadIcon("editadd16.png"),
                                   "Create a new list of resources"
                                   ,KeyStroke.getKeyStroke(KeyEvent.VK_N,UIComponentMenuBar.SHIFT_MENU_KEYMASK)                                   
                                   );
        addXQuery = new TreeAction("New XQuery List",
                                   IconHelper.loadIcon("editadd16.png"),
                                   "Use a XQuery to define a new list of resources");
        addBranch = new TreeAction("New Folder",
                                   IconHelper.loadIcon("editadd16.png"),
                                   "Create a new container for resource collections");
        addSubscription = new TreeAction("New Subscription",
                                         IconHelper.loadIcon("editadd16.png"),
                                         "Create a new item which loads its definition from an external source");
        duplicate = new TreeAction("Duplicate",
                                   IconHelper.loadIcon("editcopy16.png"),
                                   "Duplicate selected item"
                                   ,KeyStroke.getKeyStroke(KeyEvent.VK_D,UIComponentMenuBar.MENU_KEYMASK)                                   
                                   );
        duplicate.setEnabled(false);
        remove = new TreeAction("Delete",
                                IconHelper.loadIcon("editremove16.png"),
                                "Remove selected item"
                                ,KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE,UIComponentMenuBar.MENU_KEYMASK)                                
                                );
        remove.setEnabled(false);
        rename = new TreeAction("Rename"+UIComponentMenuBar.ELLIPSIS,
                                IconHelper.loadIcon("rename16.png"),
                                "Rename selected item");
        rename.setEnabled(false);
        properties = new TreeAction("Edit" + UIComponentMenuBar.ELLIPSIS,
                                    IconHelper.loadIcon("edit16.png"),
                                    "Edit selected item"
                                    ,KeyStroke.getKeyStroke(KeyEvent.VK_E,UIComponentMenuBar.MENU_KEYMASK)                                    
                                    );
        properties.setEnabled(false);
        export = new TreeAction("Export" + UIComponentMenuBar.ELLIPSIS,
                                IconHelper.loadIcon("filesave16.png"),
                                "Export the description the selected folder or resource list to an XML file");
        export.setEnabled(false);
        ymport = new TreeAction("Import" + UIComponentMenuBar.ELLIPSIS,
                                IconHelper.loadIcon("fileopen16.png"),
                                "Import a folder description from an XML file");
        
        popup = new JPopupMenu("Lists");
        final JMenu nu = new JMenu("New");
        nu.add(new JMenuItem(addSmart));
        nu.add(new JMenuItem(addStatic));
        nu.add(new JMenuItem(addXQuery));
        nu.add(new JMenuItem(addBranch));
        nu.add(new JMenuItem(addSubscription));
        popup.add(nu);
        popup.add(refresh);
        popup.addSeparator();
        popup.add(new JMenuItem(rename));
        popup.add(new JMenuItem(properties));
        popup.add(new JMenuItem(duplicate));
        popup.add(new JMenuItem(remove));
        popup.addSeparator();
        popup.add(new JMenuItem(ymport));
        popup.add(new JMenuItem(export));

        // Listen for popup menu trigger.
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent evt) {
                checkForTriggerEvent(evt);
            }
            @Override
            public void mouseReleased(final MouseEvent evt) {
                checkForTriggerEvent(evt);
            }
        });
        
        // listen for changes to selection, and adjust menu entries accordingly.
        getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(final TreeSelectionEvent e) {
                final TreePath path = getSelectionPath();
                final DefaultMutableTreeNode node = path == null
                            ? null
                            : (DefaultMutableTreeNode) path.getLastPathComponent();
                tailorActionsToResource(node);
            }
        });
        
        // finally, set up the action map - so some of these ops can be found from the menu.
        final ActionMap map = getActionMap();
     //   map.put(UIComponentMenuBar.EditMenuBuilder.COPY,TransferHandler.getCopyAction());
        map.put(UIComponentMenuBar.EditMenuBuilder.PASTE,TransferHandler.getPasteAction());
    }

    @Override
    final public void setModel(final TreeModel model) {
        if (expander != null) {
            this.model.removeTreeModelListener(expander);
            model.addTreeModelListener(expander);
        }
        super.setModel(model);
        this.model = (ResourceTreeModel) model;
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

    /**
     * Returns the currently selected folder.
     *
     * @return   selected folder, or null
     */
    public ResourceFolder getSelectedFolder() {
        return getFolder(getSelectionPath());
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
     * Returns the folder associated with a given tree node.
     *
     * @param  node  tree node
     * @return  folder
     */
    public ResourceFolder getFolder(final TreeNode node) {
        return (ResourceFolder) ((DefaultMutableTreeNode) node).getUserObject();
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
     * Either updates a folder in the tree or, if it is not present, adds it.
     * A side effect will be an update to the persistent storage of the
     * tree model on disk.
     *
     * @param  folder  folder to be stored/updated
     */
    public void store(final ResourceFolder folder) {
        final DefaultMutableTreeNode node = getNode(folder);
        if (node == null) {
            appendFolder(folder);
        }
        else {
            model.nodeStructureChanged(node);
        }
    }

    /**
     * Adds a given folder to the end of this tree.
     *
     * @param   folder   folder to add
     */
    public void appendFolder(final ResourceFolder folder) {
        final DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        final DefaultMutableTreeNode child =
            new DefaultMutableTreeNode(folder, isBranch(folder));
        model.insertNodeInto(child, root, root.getChildCount());
    }

    /**
     * Indicates whether a drag gesture is currently in progress.
     * If it is, changes in the selection model should probably avoid 
     * triggering anything expensive.
     *
     * @return   true iff a drag is in progress
     */
    public boolean isDragging() {

        // Belt and braces.  The dnd test, based on a DragSourceListener,
        // seems to work for me, but the javadocs talk a lot about 
        // platform-dependence, so this may be fragile.  The transfer handler
        // test should be reliable, but only works for drags which originate
        // on this component, not for ones which start elsewhere.
        return this.dndIsDragging || this.transferIsDragging;
    }

    /**
     * Called when a drag event may have completed.  Makes sure that selection
     * listeners are correctly informed about the current selection.
     * This is required because JTree's default TransferHandler co-opts
     * the tree selection mechanism for display purposes during a drag, 
     * but since some selection listeners don't want to change state when
     * selection changes only as a consequence of a drag, they ignore
     * selection events if dragging is on.  So once it's off, we make sure
     * they have up-to-date state.
     */
    private void endDrag() {
        if (! isDragging()) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    final TreeSelectionModel selModel = getSelectionModel();
                    final TreePath[] paths = selModel.getSelectionPaths();
                    selModel.clearSelection();
                    selModel.setSelectionPaths(paths);
                }
            });
        }
    }

    /**
     * Invoked for mouse events which might be popup menu triggers.
     */
    private void checkForTriggerEvent(final MouseEvent evt) {
        if (evt.isPopupTrigger()) {
            final int x = evt.getX();
            final int y = evt.getY();
            final TreePath path = getClosestPathForLocation(x, y);
            final DefaultMutableTreeNode node = path == null
                ? null
                : (DefaultMutableTreeNode) path.getLastPathComponent();
            tailorActionsToResource(node);
            popup.show(evt.getComponent(), x, y);
        }
    }

    /**
     * Enable/disable actions depending on the currently selected node.
     *
     * @param  node  selected node
     */
    private void tailorActionsToResource(final DefaultMutableTreeNode node) {
        final boolean notFixed;
        final boolean parentNotFixed;
        if (node != null) {
            notFixed = ! isFixed(node);
            final DefaultMutableTreeNode parent =
                (DefaultMutableTreeNode) node.getParent();
            if (parent != null) {
                parentNotFixed = ! isFixed(parent);
            }
            else {
                parentNotFixed = false;
            }
        }
        else {
            notFixed = false;
            parentNotFixed = false;
        }
        remove.setEnabled(parentNotFixed);
        properties.setEnabled(notFixed &&
                              !isBranch((ResourceFolder)node.getUserObject()));
        rename.setEnabled(parentNotFixed);
        duplicate.setEnabled(node != null);
        refresh.setEnabled(node != null);
        export.setEnabled(node != null);
    }

    /**
     * Works out whether a node is fixed, meaning that no user-directed 
     * changes in its structure should be permitted.
     *
     * @param   node  node to test
     * @return   true iff node should not be altered
     */
    private boolean isFixed(final DefaultMutableTreeNode node) {

        // Test whether any of the node's ancestors have non-empty 
        // subscriptions.  If so, the whole content will be overwritten next
        // time the program is run, so any changes would get lost.
        return getSubscribedAncestor(node) != null;
    }

    /**
     * Finds the closest ancestor of a given node, including itself, 
     * which has a non-empty subscription.
     *
     * @param  node   node to test
     * @return  ancestor folder object with a subscription
     */
    private ResourceFolder getSubscribedAncestor(final DefaultMutableTreeNode node) {
        final Object[] path = node.getUserObjectPath();
        for (int i = path.length - 1; i >= 0; i--) {
            final ResourceFolder folder = (ResourceFolder) path[i];
            final String subs = folder.getSubscription();
            if (subs != null && subs.trim().length() > 0) {
                return folder;
            }
        }
        return null;
    }

    /**
     * Allows the user to save the currently selected node to an XML file.
     *
     * @param  node  node to export
     */
    private void exportNode(final DefaultMutableTreeNode node) {
        final Object bean = BranchBean.fromTreeNode(node);
        final URI loc = chooser
            .chooseResourceWithParent("Save Folder", true, true, true, this);
        if (loc == null) {
            return;
        }
        new BackgroundWorker(parent, "Saving folder description",BackgroundWorker.LONG_TIMEOUT,Thread.MIN_PRIORITY + 3) {
            {
                setTransient(true); // display errors as transient popup.
            }
            @Override
            protected Object construct() throws IOException, ServiceException {
                OutputStream os = null;
                try {
                    reportProgress("Resolving file");
                    final FileObject fo = chooser.getVFS().resolveFile(loc.toString());
                    reportProgress("Opening file");
                    os = fo.getContent().getOutputStream();
                    reportProgress("Exporting data");
                    persister.toXml(bean, os);
                    reportProgress("Completed");
                }
                finally {
                    IOUtils.closeQuietly(os);
                }
                return null;
            }
            @Override
            protected void doFinished(final Object result) {
                parent.showTransientMessage("Folder saved","to " + loc);
            }
            
        }.start();
    }

    /**
     * Allows the user to select a file containing an XML representation of
     * a tree node (as saved by exportNode) and insert it into the tree.
     */
    private void importNode() {
        final URI loc = chooser
            .chooseResourceWithParent("Load folder", true, true, true, this);
        if (loc == null) {
            return;
        }
        new BackgroundWorker(parent, "Loading folder description",BackgroundWorker.LONG_TIMEOUT) {
            @Override
            public Object construct() throws IOException, ServiceException {
                InputStream is = null;
                try {
                    reportProgress("Resolving file");
                    final FileObject fo = chooser.getVFS().resolveFile(loc.toString());
                    reportProgress("Opening file");
                    is = fo.getContent()
                            .getInputStream();
                    reportProgress("Reading contents");
                    final Object bean = persister.fromXml(is);
                    reportProgress("Completed");
                    return bean;
                }
                finally {
                  IOUtils.closeQuietly(is);
                }
            }
            @Override
            protected void doFinished(final Object bean) {
                if (bean != null) {
                    final String fname = loc.toString().replaceFirst("^.*/", "");
                    final DefaultMutableTreeNode newNode =
                    BranchBean.toTreeRoot(bean, fname);
                        final DefaultMutableTreeNode root =
                            (DefaultMutableTreeNode) model.getRoot();
                        model.insertNodeInto(newNode, root, root.getChildCount());
                }
            }
        }.start();
    }

    /**
     * Indicates whether a folder represents a tree branch node.
     *
     * @return   true for branch node, false for leaf node
     */
    private static boolean isBranch(final ResourceFolder folder) {
        return folder instanceof ResourceBranch;
    }

    /**
     * Superclass for actions defined within this class, defining action
     * behaviour.
     */
    private class TreeAction extends AbstractAction {

        /**
         * Constructor.
         *
         * @param  name  action name
         * @param  icon  action icon
         * @param  shortDesc  action description (used for tool tip)
         */
        TreeAction(final String name, final Icon icon, final String shortDesc) {
            super(name, icon);
            putValue(SHORT_DESCRIPTION, shortDesc);
        }

        TreeAction(final String name, final Icon icon, final String shortDesc,final KeyStroke accel) {
            super(name, icon);
            putValue(SHORT_DESCRIPTION, shortDesc);
            putValue(ACCELERATOR_KEY,accel);
        }
        
        public void actionPerformed(final ActionEvent evt) {
            final TreePath path = getSelectionPath(); 
            final DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                (path == null ? null
                              : path.getLastPathComponent());
            final ResourceFolder folder = (ResourceFolder)
                (node == null ? null
                              : node.getUserObject());
            if (this == addStatic) {
                new StaticList().editAsNew(parent);
            }
            else if (this == addSmart) {
                new SmartList().editAsNew(parent);
            }
            else if (this == addXQuery) {
                new XQueryList().editAsNew(parent);
            }
            else if (this == addBranch) {
                new ResourceBranch().editAsNew(parent);
            }
            else if (this == addSubscription) {
                final ResourceBranch subs = new ResourceBranch("New Subscription");
                subs.setIconName("myspace16.png"); // make it look like a subscription.
                subs.editSubscriptionAsNew(parent);
            }
            else if (this == remove && node != null) {
                final boolean deleteSelected = true;
                model.removeNodeFromParent(node);
                if (deleteSelected) {
                    setSelectionRow(0);
                }
            }
            else if (this == duplicate && node != null) {
                DefaultMutableTreeNode parent =
                    (DefaultMutableTreeNode) node.getParent();
                if (isFixed(parent)) {
                    parent = null;
                }
                int childIndex;
                if (parent == null) {
                    parent = (DefaultMutableTreeNode) model.getRoot();
                    childIndex = parent.getChildCount();
                }
                else {
                    childIndex = parent.getIndex(node) + 1;
                }
                model.insertNodeInto(model.duplicateNode(node, false), parent,
                                     childIndex);
            }
            else if (this == properties && folder != null) {
                if (folder.getSubscription() == null) {
                    folder.edit(parent);
                }
                else {
                    folder.editSubscription(parent);
                }
            }
            else if (this == rename && folder != null) {
                final Component pc = parent.getComponent();
                final Window w = pc instanceof Window
                         ? (Window) pc
                         : SwingUtilities.getWindowAncestor(pc);
                
                final BaseDialog d;
                if (w instanceof Frame) {
                    d = new RenameDialog((Frame)w, folder,node);
                } else if (w instanceof Dialog) {
                    d = new RenameDialog((Dialog)w, folder,node);          
                } else {
                    d = new RenameDialog(folder,node);         
                }
                d.setVisible(true);                
            }
            else if (this == export && node != null) {
                exportNode(node);
            }
            else if (this == ymport) {
                importNode();
            }
            else {
                assert false;
            }
        }
        
        private class RenameDialog extends BaseDialog {
            private final ResourceFolder folder;
            private final DefaultMutableTreeNode node;

            public RenameDialog(final ResourceFolder folder, final DefaultMutableTreeNode node) throws HeadlessException {
                super();
                this.folder = folder;
                this.node = node;
                init();
            }
            public RenameDialog(final Frame f,final ResourceFolder folder, final DefaultMutableTreeNode node) throws HeadlessException {
                super(f);
                this.folder = folder;
                this.node = node;
                init();
            }
            public RenameDialog(final Dialog f,final ResourceFolder folder, final DefaultMutableTreeNode node) throws HeadlessException {
                super(f);
                this.folder = folder;
                this.node = node;
                init();
            }            
            private final JTextField tf = new JTextField(20);
            private void init() {
                setModal( false);
                setDialogMode(BaseDialog.OK_CANCEL_DIALOG);
                setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                setTitle("Rename");
                getBanner().setTitle("Renaming " + folder.getName());
                getBanner().setSubtitle("Enter a new name");
                getBanner().setSubtitleVisible(false);
                tf.setText(folder.getName());
                
                final Container cp = getContentPane();
                cp.setLayout(new java.awt.FlowLayout());
                cp.add(new JLabel("Name :"));
                cp.add(tf);                
                pack();
                setLocationRelativeTo(parent.getComponent());
            }
            
            @Override
            public void ok() {
                super.ok();
                final String nuName =tf.getText();
                if (StringUtils.isNotEmpty(nuName)) {
                    folder.setName(nuName);
                    model.nodeStructureChanged(node);
                }                
            }
        }
    }
    
    /**
     * TransferHandler which defines almost all of the drag'n'drop 
     * functionality for this tree.
     */
    private class TreeTransferHandler extends TransferHandler {

        @Override
        public boolean canImport(final JComponent comp, final DataFlavor[] flavors) {
            return CollectionUtils.exists(Arrays.asList(flavors),
                                          dragPredicate);
        }

        @Override
        protected Transferable createTransferable(final JComponent comp) {
            final Point pos = ResourceTree.this.mousePos;
            final TreePath path = getClosestPathForLocation(pos.x, pos.y);
            if (path == null) {
                return null;
            }
            else {
                final DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode) path.getLastPathComponent();
                return new TreeNodeTransferable(node);
            }
        }

        @Override
        public int getSourceActions(final JComponent comp) {
            return TransferHandler.COPY_OR_MOVE;
        }

        @Override
        public Icon getVisualRepresentation(final Transferable trans) {
            if (trans instanceof TreeNodeTransferable) {
                final DefaultMutableTreeNode node =
                    ((TreeNodeTransferable) trans).node;
                final ResourceFolder folder = getFolder(node);
                return folder.getIcon();
            }
            else {
                return super.getVisualRepresentation(trans);
            }
        }

        @Override
        public boolean importData(final JComponent comp, final Transferable trans) {
            try {
                return doImportData(comp, trans);
            }
            catch (final Exception e) {
                logger.info("Tree drop failed", e);
                return false;
            }
        }

        @Override
        public void exportAsDrag(final JComponent comp, final InputEvent evt, final int action ) {
            transferIsDragging = true;
            super.exportAsDrag(comp, evt, action);
        }

        @Override
        protected void exportDone(final JComponent comp, final Transferable trans, final int action) {
            if (action == TransferHandler.MOVE) {
                if (trans.isDataFlavorSupported(VoDataFlavour.TREENODE)) {
                    DefaultMutableTreeNode movedNode = null;
                    try {
                        movedNode = (DefaultMutableTreeNode)
                                    trans.getTransferData(VoDataFlavour.TREENODE);
                    }
                    catch (final UnsupportedFlavorException e) {
                        logger.warn("Strange", e);
                    }
                    catch (final IOException e) {
                        logger.warn("Strange", e);
                    }
                    if (movedNode != null) {
                        final DefaultMutableTreeNode parent =
                            (DefaultMutableTreeNode) movedNode.getParent();
                        if (parent != null && !isFixed(parent)) {
                            model.removeNodeFromParent(movedNode);
                        }
                    }
                }
            }
            transferIsDragging = false;
            endDrag();
            super.exportDone(comp, trans, action);
        }

        /**
         * Performs actual data imports.  This is called by the 
         * importData method but has some declared exceptions.
         */
        private boolean doImportData(final JComponent comp, final Transferable trans) throws InvalidArgumentException, IOException, UnsupportedFlavorException {
            final TreePath path = getSelectionPath();
            final DefaultMutableTreeNode targetNode = path == null
                ? null
                : (DefaultMutableTreeNode) path.getLastPathComponent();
            if (trans.isDataFlavorSupported(VoDataFlavour.LOCAL_RESOURCE_ARRAY)) {
                logger.debug("local resource array");
                return dropResources(targetNode, (Resource[]) trans.getTransferData(VoDataFlavour.LOCAL_RESOURCE_ARRAY));
            }
            else if (trans.isDataFlavorSupported(VoDataFlavour.LOCAL_RESOURCE)) {
                logger.debug("local resource");
                return dropResources(targetNode, new Resource[] {(Resource) trans.getTransferData(VoDataFlavour.LOCAL_RESOURCE)});
            }
            else if (trans.isDataFlavorSupported(VoDataFlavour.RESOURCE_ARRAY)) {
                logger.debug("resource array");
                return dropResources(targetNode, (Resource[]) trans.getTransferData(VoDataFlavour.RESOURCE_ARRAY));
            }
            else if (trans.isDataFlavorSupported(VoDataFlavour.RESOURCE)) {
                logger.debug("resource");
                return dropResources(targetNode, new Resource[] {(Resource) trans.getTransferData(VoDataFlavour.RESOURCE)});
            }
            else if (trans.isDataFlavorSupported(VoDataFlavour.TREENODE)) {
                logger.debug("treenode");
                return dropTreeNode(targetNode, (DefaultMutableTreeNode) trans.getTransferData(VoDataFlavour.TREENODE));
            }
            else if (trans.isDataFlavorSupported(VoDataFlavour.LOCAL_URI)) {
                logger.debug("local uri");
                return dropUris(targetNode, new URI[] {(URI) trans.getTransferData(VoDataFlavour.LOCAL_URI)});
            }
            else if (trans.isDataFlavorSupported(VoDataFlavour.LOCAL_URI_ARRAY)) {
                logger.debug("local uri array");
                return dropUris(targetNode, (URI[]) trans.getTransferData(VoDataFlavour.LOCAL_URI_ARRAY));
            }
            else if (trans.isDataFlavorSupported(VoDataFlavour.URI_LIST)) {
                logger.debug("uri list");
                return dropUris(targetNode, convertUnknownToUriList(trans.getTransferData(VoDataFlavour.URI_LIST)));
            }
            else if (trans.isDataFlavorSupported(VoDataFlavour.PLAIN)) {
                logger.debug("plain");
                return dropUris(targetNode, convertUnknownToUriList(trans.getTransferData(VoDataFlavour.PLAIN)));
            }
            else if (trans.isDataFlavorSupported(VoDataFlavour.STRING)) {
                logger.debug("string");
                return dropUris(targetNode, convertUnknownToUriList(trans.getTransferData(VoDataFlavour.STRING)));
            }
            else {
                return false;
            }
        }

        /**
         * Does the work for dropping a list of Resource objects.
         *
         * @param  targetNode  node to drop onto
         * @param  resources   array of resource objects to drop
         * @return   true iff the drop was successful
         */
        private boolean dropResources(final DefaultMutableTreeNode targetNode, final Resource[] resources) {
            final URI[] uris = new URI[resources.length];
            for (int i = 0; i < resources.length; i++) {
                uris[i] = resources[i].getId();
            }
            return dropUris(targetNode, uris);
        }

        /**
         * Does the work for dropping a list of URIs.
         *
         * @param  targetNode   node to drop onto (may be null)
         * @param  ids   URIs representing resource IDs
         * @return   true iff the drop was successful
         */
        private boolean dropUris(final DefaultMutableTreeNode targetNode, final URI[] ids) {
            if (ids == null || ids.length == 0) {
                logger.debug("Empty ids list passed in - bailing out here");
                return false;
            }
            final ResourceFolder folder = targetNode == null
                                  ? null
                                  : (ResourceFolder) targetNode.getUserObject();

            // If dropped on to static list, insert items into it.
            if (folder instanceof StaticList) {
                if (isFixed(targetNode)) {
                    failToModifySubscription(targetNode);
                    return false;
                }
                else {
                    final StaticList list = (StaticList) folder;
                    for (int i = 0; i < ids.length; i++) {
                        list.getResourceSet().add(ids[i]);
                    }
                    clearSelection();
                    model.nodeStructureChanged(targetNode);
                    setSelectionPath(new TreePath(targetNode.getPath()));
                    return true;
                }
            }

            // If dropped in space or onto an unsuitable kind of node, create new static list
            else {
                final StaticList list = new StaticList();
                for (int i = 0; i < ids.length; i++) {
                    list.getResourceSet().add(ids[i]);
                }
                return insertNodeAfter(new DefaultMutableTreeNode(list, false), targetNode);
            }
        }

        /**
         * Does the work for dropping a TreeNode.  This will have been 
         * transferred from elsewhere in this tree.
         *
         * @param   targetNode  node to drop onto
         * @param   childNode   node to drop
         * @return   true iff the drop was successful
         */
        private boolean dropTreeNode(final DefaultMutableTreeNode targetNode, final DefaultMutableTreeNode childNode) {

            // We insert a clone of the node rather than the node itself.
            // This is because we don't yet know whether this is a MOVE or
            // COPY, so the original may or may not need to get left in place.
            // Removal of the original on MOVE is handled in exportDone.
            return insertNodeAfter(model.duplicateNode(childNode, true), targetNode);
        }

        /**
         * Notifies the user asynchronously that a tree modification has failed
         * because a node is subscribed (hence not permitted to change).
         *
         * @param   node  node for which changes were attempted
         */
        private void failToModifySubscription(final DefaultMutableTreeNode node) {
            final ResourceFolder folder = getSubscribedAncestor(node);
            final StringBuffer msgbuf = new StringBuffer()
                .append("Drop action failed - ")
                .append("cannot modify structure of subscribed node");
            if (folder != null) {
                msgbuf.append( " \"" )
                      .append(folder.getName())
                      .append( "\"" );
            }
            parent.showTransientWarning("Drop Failed",msgbuf.toString());

        }

        /**
         * Inserts a node into the tree after a given target node.
         * It basically goes on the row after the target node.
         * This means that it only goes into a branch-type node if that node
         * is currently explanded.  Doing it the other way (going into a
         * collapsed branch node) would mean that it was hard to append 
         * items to the end of the tree in the case that a branch was the
         * last item currently there.
         *
         * @param  newNode  node to insert
         * @param  targetNode  currently highlighted node which indicates to the
         *         user where the new one is going to go
         * @return  success status
         */
        private boolean insertNodeAfter(final DefaultMutableTreeNode newNode, final DefaultMutableTreeNode targetNode) {
            DefaultMutableTreeNode parentNode;
            int childIndex;
            if (targetNode == null) {
                parentNode = (DefaultMutableTreeNode) model.getRoot();
                childIndex = parentNode.getChildCount();
            }
            else if (isExpanded(new TreePath(targetNode.getPath())) ||
                     targetNode.getParent() == null ||
                     (targetNode.getAllowsChildren() && targetNode.getChildCount() == 0)) {
                parentNode = targetNode;
                childIndex = 0;
            }
            else {
                parentNode = (DefaultMutableTreeNode) targetNode.getParent();
                childIndex = parentNode.getIndex(targetNode) + 1;
            }
            //NWW - removed as unused.
          //  ResourceFolder folder = getSubscribedAncestor(parentNode);
            if (isFixed(parentNode)) {
                failToModifySubscription(parentNode);
                return false;
            }
            else {
                model.insertNodeInto(newNode, parentNode, childIndex);
                setSelectionPath(new TreePath(newNode.getPath()));
                return true;
            }
        }

        /** try and convert something unknown into a list of URIs.
         * assume it's going to be a stream / reader / string, with a uri on each line.
         * @param o
         * @return a list of the resource ids we've managed to parse from it. Will be ivo:// format - but not guaranteed that they
         * point to actual resources.
         * @throws IOException
         */
        private URI[] convertUnknownToUriList(final Object o) throws InvalidArgumentException, IOException {
            if (o == null) {
                throw new InvalidArgumentException("null dropped - how odd");
            }
            Reader r = null;
            LineIterator it = null;
            if (o instanceof String) {
                r = new StringReader((String)o);
            } else if (o instanceof InputStream ) {
                r = new InputStreamReader((InputStream)o);
            } else if (o instanceof Reader) {
                r = (Reader)o;
            } else {
                throw new InvalidArgumentException("Unknow type dropped " + o.getClass().getName());
            }
            try {
                final List result = new ArrayList();
                it = IOUtils.lineIterator(r);
                while (it.hasNext()) {
                    final String line = it.nextLine();               
                    try {
                        final URI u = new URI(line);
                        if (! u.getScheme().equals("ivo")) { // we only want ivo uris.
                            continue;
                        }
                        result.add(u);
                    } catch (final URISyntaxException e) {
                        logger.debug("Dropping " + line);
                    }
                }
                if (result.isEmpty()) {
                    throw new InvalidArgumentException("No resource ids found");
                }
                return (URI[])result.toArray(new URI[result.size()]);
            } finally {               
               LineIterator.closeQuietly(it);
                
            }
        }
    }

    /**
     * Transferable implementation for TreeNodes.
     */
    private class TreeNodeTransferable implements Transferable {
        private final DefaultMutableTreeNode node;

        TreeNodeTransferable(final DefaultMutableTreeNode node) {
            this.node = node;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] {VoDataFlavour.TREENODE,VoDataFlavour.XML};
        }

        public boolean isDataFlavorSupported(final DataFlavor flavor) {
            return VoDataFlavour.TREENODE.equals(flavor)
                || VoDataFlavour.XML.equals(flavor);
        }

        public Object getTransferData(final DataFlavor flavor)
                throws UnsupportedFlavorException, IOException {
            if (VoDataFlavour.TREENODE.equals(flavor)) {
                return node;
            }
            else if (VoDataFlavour.XML.equals(flavor)) {
                final ByteArrayOutputStream os = new ByteArrayOutputStream();
                try {
                    persister.toXml(BranchBean.fromTreeNode(node), os);
                }
                catch (final ServiceException e) {
                    throw (IOException) new IOException("Serialization error")
                                       .initCause(e);
                }
                IOUtils.closeQuietly(os);
                return new ByteArrayInputStream(os.toByteArray());
            }
            else {
                throw new UnsupportedFlavorException(flavor);
            }
        }
    }

    /**
     * Renderer implementation for ResourceFolder nodes.
     */
    private static class ResourceRenderer extends DefaultTreeCellRenderer {

        private Font plainFont;
        private Font emFont;

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
    }

    /**
     * @return the addStatic
     */
    public final Action getAddStatic() {
        return this.addStatic;
    }

    /**
     * @return the addSmart
     */
    public final Action getAddSmart() {
        return this.addSmart;
    }

    /**
     * @return the addXQuery
     */
    public final Action getAddXQuery() {
        return this.addXQuery;
    }

    /**
     * @return the addBranch
     */
    public final Action getAddBranch() {
        return this.addBranch;
    }

    
    /**
     * @return the addSubscription
     */
    public final Action getAddSubscription() {
        return this.addSubscription;
    }

    /**
     * @return the remove
     */
    public final Action getRemove() {
        return this.remove;
    }

    /**
     * @return the properties
     */
    public final Action getProperties() {
        return this.properties;
    }

    /**
     * @return the duplicate
     */
    public final Action getDuplicate() {
        return this.duplicate;
    }

    /**
     * @return the rename
     */
    public final Action getRename() {
        return this.rename;
    }

    /**
     * @return the export
     */
    public final Action getExport() {
        return this.export;
    }

    /**
     * @return the import
     */
    public final Action getImport() {
        return this.ymport;
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
        public void treeWillCollapse(final TreeExpansionEvent evt) throws ExpandVetoException {
            // Never permit root collapse.
            if (isRoot(evt.getPath())) {
                throw new ExpandVetoException(evt, "Refuse to collapse root");
            }
        }
        public void treeWillExpand(final TreeExpansionEvent evt) {
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
}
