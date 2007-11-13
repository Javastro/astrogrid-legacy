package org.astrogrid.desktop.modules.ui.voexplorer;

import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DnDConstants;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
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

import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileSystemManager;
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
import org.astrogrid.desktop.modules.ui.voexplorer.VOExplorerImpl;
import org.astrogrid.desktop.modules.ui.voexplorer.RegistryGooglePanel;

/**
 * Tree containing resource folders.
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
        public boolean evaluate(Object arg0) {
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
    private Point mousePos;
    private boolean isDragging;


    private final Action refresh;


    /**
     * Constructor.
     *
     * @param   tree model - must be a ResourceTreeModel with correct content
     */
    public ResourceTree(TreeModel model, VOExplorerImpl parent,
                        ResourceChooserInternal chooser, XmlPersist persister
                        , Action refreshAction) {
        super(model);
        this.refresh = refreshAction;
        setModel(model);
        this.parent = parent;
        this.chooser = chooser;
        this.persister = persister;
        CSH.setHelpIDString(this, "resourceTree");

        // Install a handler for dragging'n'dropping.
        setTransferHandler(new TreeTransferHandler());
       
        // Arrange to call the TransferHandler's exportAsDrag method when
        // a drag has been initiated.  It would be easier just to call 
        // setDragEnabled(true) here, but that only permits dragging
        // the currently selected node.  We would like to be able to 
        // drag whatever is under the cursor.
        DragGestureListener dgl = new DragGestureListener() {
            public void dragGestureRecognized(DragGestureEvent evt) {
                JComponent comp = ResourceTree.this;
                TransferHandler th = comp.getTransferHandler();
                th.exportAsDrag(comp, evt.getTriggerEvent(),
                                evt.getDragAction());
            }
        };
        DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, dgl);

        // We need to keep track of the mouse position for use when working
        // out what to drag.
        addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent evt) {
                mousePos = evt.getPoint();
            }
            public void mouseDragged(MouseEvent evt) {
            }
        });

        // Configure visual properties.
        getSelectionModel().setSelectionMode(TreeSelectionModel
                                            .SINGLE_TREE_SELECTION);
       // NW - would prefer not to have the root shown.
        // setRootVisible(true);
        setRootVisible(false);
       // setShowsRootHandles(false);
        setExpandsSelectedPaths(true);
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
                                   "Duplicate this item"
                                   ,KeyStroke.getKeyStroke(KeyEvent.VK_D,UIComponentMenuBar.MENU_KEYMASK)                                   
                                   );
        duplicate.setEnabled(false);
        remove = new TreeAction("Delete",
                                IconHelper.loadIcon("editremove16.png"),
                                "Remove this item"
                                ,KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE,UIComponentMenuBar.MENU_KEYMASK)                                
                                );
        remove.setEnabled(false);
        rename = new TreeAction("Rename"+UIComponentMenuBar.ELLIPSIS,
                                IconHelper.loadIcon("rename16.png"),
                                "Rename this item");
        rename.setEnabled(false);
        properties = new TreeAction("Edit" + UIComponentMenuBar.ELLIPSIS,
                                    IconHelper.loadIcon("edit16.png"),
                                    "Edit this item"
                                    ,KeyStroke.getKeyStroke(KeyEvent.VK_E,UIComponentMenuBar.MENU_KEYMASK)                                    
                                    );
        properties.setEnabled(false);
        export = new TreeAction("Export" + UIComponentMenuBar.ELLIPSIS,
                                IconHelper.loadIcon("filesave16.png"),
                                "Export the description of this folder to an XML file");
        export.setEnabled(false);
        ymport = new TreeAction("Import" + UIComponentMenuBar.ELLIPSIS,
                                IconHelper.loadIcon("fileopen16.png"),
                                "Import a folder description from an XML file");
        
        popup = new JPopupMenu("Lists");
        JMenu nu = new JMenu("New");
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
            public void mousePressed(MouseEvent evt) {
                checkForTriggerEvent(evt);
            }
            public void mouseReleased(MouseEvent evt) {
                checkForTriggerEvent(evt);
            }
        });
        
        // listen for changes to selection, and adjust menu entries accordingly.
        getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = getSelectionPath();
                DefaultMutableTreeNode node = path == null
                            ? null
                            : (DefaultMutableTreeNode) path.getLastPathComponent();
                tailorActionsToResource(node);
            }
        });
        
        // finally, set up the action map - so some of these ops can be found from the menu.
        ActionMap map = getActionMap();
     //   map.put(UIComponentMenuBar.EditMenuBuilder.COPY,TransferHandler.getCopyAction());
        map.put(UIComponentMenuBar.EditMenuBuilder.PASTE,TransferHandler.getPasteAction());
    }

    public void setModel(TreeModel model) {
        super.setModel(model);
        this.model = (ResourceTreeModel) model;
    }

    /**
     * Sets up this tree in a reasonable state for viewing once it has been
     * populated.
     */
    public void initialiseViewAndSelection() {
        expandPath(new TreePath(getModel().getRoot()));
        int nrow = getRowCount();
        for (int ir = 0; ir < nrow; ir++) {
            ResourceFolder folder = getFolder(getPathForRow(ir));
            boolean isSuitable = true;
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
    public void setSelectedFolder(ResourceFolder folder) {
        TreePath path = getPath(folder);
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
    public ResourceFolder getFolder(TreePath path) {
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
    public ResourceFolder getFolder(TreeNode node) {
        return (ResourceFolder) ((DefaultMutableTreeNode) node).getUserObject();
    }

    /**
     * Returns the TreeNode in this tree associated with a given folder,
     * or null if it's not in the tree.
     *
     * @param  folder  folder
     * @return   node, or null
     */
    public DefaultMutableTreeNode getNode(ResourceFolder folder) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        for (Enumeration en = root.depthFirstEnumeration();
             en.hasMoreElements();) {
            DefaultMutableTreeNode node =
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
    public TreePath getPath(ResourceFolder folder) {
        DefaultMutableTreeNode node = getNode(folder);
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
    public void store(ResourceFolder folder) {
        DefaultMutableTreeNode node = getNode(folder);
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
    public void appendFolder(ResourceFolder folder) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode child =
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
        return this.isDragging;
    }

    /**
     * Invoked for mouse events which might be popup menu triggers.
     */
    private void checkForTriggerEvent(MouseEvent evt) {
        if (evt.isPopupTrigger()) {
            int x = evt.getX();
            int y = evt.getY();
            TreePath path = getClosestPathForLocation(x, y);
            DefaultMutableTreeNode node = path == null
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
    private void tailorActionsToResource(DefaultMutableTreeNode node) {
        final boolean notFixed;
        final boolean parentNotFixed;
        if (node != null) {
            notFixed = ! isFixed(node);
            DefaultMutableTreeNode parent =
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
    private boolean isFixed(DefaultMutableTreeNode node) {
        Object[] path = node.getUserObjectPath();

        // Test whether any of the node's ancestors have non-empty 
        // subscriptions.  If so, the whole content will be overwritten next
        // time the program is run, so any changes would get lost.
        for (int i = path.length - 1; i >= 0; i--) {
            String subs = ((ResourceFolder) path[i]).getSubscription();
            if (subs != null && subs.trim().length() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Allows the user to save the currently selected node to an XML file.
     *
     * @param  node  node to export
     */
    private void exportNode(DefaultMutableTreeNode node) {
        final Object bean = BranchBean.fromTreeNode(node);
        final URI loc = chooser
            .chooseResourceWithParent("Save Folder", true, true, true, this);
        if (loc == null) {
            return;
        }
        new BackgroundWorker(parent, "Saving folder description") {
            {
                setTransient(true); // display errors as transient popup.
            }
            protected Object construct() throws IOException, ServiceException {
                OutputStream os = null;
                try {
                    os = chooser.getVFS().resolveFile(loc.toString()).getContent()
                                                        .getOutputStream();
                    persister.toXml(bean, os);
                }
                finally {
                    if (os != null) {
                        os.close();
                    }
                }
                return null;
            }
            protected void doFinished(Object result) {
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
        new BackgroundWorker(parent, "Loading folder description") {
            public Object construct() throws IOException, ServiceException {
                InputStream is = null;
                try {
                    is = chooser.getVFS().resolveFile(loc.toString()).getContent()
                            .getInputStream();
                    Object bean = persister.fromXml(is);
                    return bean;
                }
                finally {
                    if (is != null) {
                        try {
                            is.close();
                        }
                        catch (IOException e) {
                            // ignore
                        }
                    }
                }
            }
            protected void doFinished(Object bean) {
                if (bean != null) {
                    String fname = loc.toString().replaceFirst("^.*/", "");
                    DefaultMutableTreeNode newNode =
                    BranchBean.toTreeRoot(bean, fname);
                        DefaultMutableTreeNode root =
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
    private static boolean isBranch(ResourceFolder folder) {
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
        TreeAction(String name, Icon icon, String shortDesc) {
            super(name, icon);
            putValue(SHORT_DESCRIPTION, shortDesc);
        }

        TreeAction(String name, Icon icon, String shortDesc,KeyStroke accel) {
            super(name, icon);
            putValue(SHORT_DESCRIPTION, shortDesc);
            putValue(ACCELERATOR_KEY,accel);
        }
        
        public void actionPerformed(ActionEvent evt) {
            TreePath path = getSelectionPath(); 
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                (path == null ? null
                              : path.getLastPathComponent());
            ResourceFolder folder = (ResourceFolder)
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
                new ResourceBranch("New Subscription").editSubscriptionAsNew(parent);
            }
            else if (this == remove && node != null) {
                boolean deleteSelected = true;
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
                String nuName =
                    JOptionPane.showInputDialog(parent, "Enter a new name",
                                                folder.getName());
                if (StringUtils.isNotEmpty(nuName)) {
                    folder.setName(nuName);
                    model.nodeStructureChanged(node);
                }
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
    }
    /**
     * TransferHandler which defines almost all of the drag'n'drop 
     * functionality for this tree.
     */
    private class TreeTransferHandler extends TransferHandler {

        public boolean canImport(JComponent comp, DataFlavor[] flavors) {
            return CollectionUtils.exists(Arrays.asList(flavors),
                                          dragPredicate);
        }

        protected Transferable createTransferable(JComponent comp) {
            Point pos = ResourceTree.this.mousePos;
            TreePath path = getClosestPathForLocation(pos.x, pos.y);
            if (path == null) {
                return null;
            }
            else {
                DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode) path.getLastPathComponent();
                return new TreeNodeTransferable(node);
            }
        }

        public int getSourceActions(JComponent comp) {
            return TransferHandler.COPY_OR_MOVE;
        }

        public Icon getVisualRepresentation(Transferable trans) {
            if (trans instanceof TreeNodeTransferable) {
                DefaultMutableTreeNode node =
                    ((TreeNodeTransferable) trans).node;
                ResourceFolder folder = getFolder(node);
                return folder.getIcon();
            }
            else {
                return super.getVisualRepresentation(trans);
            }
        }

        public boolean importData(JComponent comp, Transferable trans) {
            try {
                return doImportData(comp, trans);
            }
            catch (Exception e) {
                logger.info("Tree drop failed", e);
                return false;
            }
        }

        public void exportAsDrag(JComponent comp, InputEvent evt, int action ) {
            isDragging = true;
            super.exportAsDrag(comp, evt, action);
        }

        protected void exportDone(JComponent comp, Transferable trans, int action) {
            if (action == TransferHandler.MOVE) {
                if (trans.isDataFlavorSupported(VoDataFlavour.TREENODE)) {
                    DefaultMutableTreeNode movedNode = null;
                    try {
                        movedNode = (DefaultMutableTreeNode)
                                    trans.getTransferData(VoDataFlavour.TREENODE);
                    }
                    catch (UnsupportedFlavorException e) {
                        logger.warn("Strange", e);
                    }
                    catch (IOException e) {
                        logger.warn("Strange", e);
                    }
                    if (movedNode != null) {
                        DefaultMutableTreeNode parent =
                            (DefaultMutableTreeNode) movedNode.getParent();
                        if (parent != null && !isFixed(parent)) {
                            model.removeNodeFromParent(movedNode);
                        }
                    }
                }
            }
            isDragging = false;
            super.exportDone(comp, trans, action);
        }

        /**
         * Performs actual data imports.  This is called by the 
         * importData method but has some declared exceptions.
         */
        private boolean doImportData(JComponent comp, Transferable trans) throws InvalidArgumentException, IOException, UnsupportedFlavorException {
            TreePath path = getSelectionPath();
            DefaultMutableTreeNode targetNode = path == null
                ? null
                : (DefaultMutableTreeNode) path.getLastPathComponent();
            if (isFixed(targetNode)) {
                return false;
            }
            if (trans.isDataFlavorSupported(VoDataFlavour.LOCAL_RESOURCE_ARRAY)) {
                return dropResources(targetNode, (Resource[]) trans.getTransferData(VoDataFlavour.LOCAL_RESOURCE_ARRAY));
            }
            else if (trans.isDataFlavorSupported(VoDataFlavour.LOCAL_RESOURCE)) {
                return dropResources(targetNode, new Resource[] {(Resource) trans.getTransferData(VoDataFlavour.LOCAL_RESOURCE)});
            }
            else if (trans.isDataFlavorSupported(VoDataFlavour.RESOURCE_ARRAY)) {
                return dropResources(targetNode, (Resource[]) trans.getTransferData(VoDataFlavour.RESOURCE_ARRAY));
            }
            else if (trans.isDataFlavorSupported(VoDataFlavour.RESOURCE)) {
                return dropResources(targetNode, new Resource[] {(Resource) trans.getTransferData(VoDataFlavour.RESOURCE)});
            }
            else if (trans.isDataFlavorSupported(VoDataFlavour.TREENODE)) {
                return dropTreeNode(targetNode, (DefaultMutableTreeNode) trans.getTransferData(VoDataFlavour.TREENODE));
            }
            else if (trans.isDataFlavorSupported(VoDataFlavour.LOCAL_URI)) {
                return dropUris(targetNode, new URI[] {(URI) trans.getTransferData(VoDataFlavour.LOCAL_URI)});
            }
            else if (trans.isDataFlavorSupported(VoDataFlavour.LOCAL_URI_ARRAY)) {
                return dropUris(targetNode, (URI[]) trans.getTransferData(VoDataFlavour.LOCAL_URI_ARRAY));
            }
            else if (trans.isDataFlavorSupported(VoDataFlavour.URI_LIST)) {
                return dropUris(targetNode, convertUnknownToUriList(trans.getTransferData(VoDataFlavour.URI_LIST)));
            }
            else if (trans.isDataFlavorSupported(VoDataFlavour.PLAIN)) {
                return dropUris(targetNode, convertUnknownToUriList(trans.getTransferData(VoDataFlavour.PLAIN)));
            }
            else if (trans.isDataFlavorSupported(VoDataFlavour.STRING)) {
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
        private boolean dropResources(DefaultMutableTreeNode targetNode, Resource[] resources) {
            URI[] uris = new URI[resources.length];
            for (int i = 0; i < resources.length; i++) {
                uris[i] = resources[i].getId();
            }
            return dropUris(targetNode, uris);
        }

        /**
         * Does the work for dropping a list of URIs.
         *
         * @param  targetNode   node to drop onto
         * @param  ids   URIs representing resource IDs
         * @return   true iff the drop was successful
         */
        private boolean dropUris(DefaultMutableTreeNode targetNode, URI[] ids) {
            if (ids == null || ids.length == 0) {
                logger.debug("Empty ids list passed in - bailing out here");
                return false;
            }
            ResourceFolder folder = getFolder(targetNode);

            // Dropped on existing dumb folder
            if (folder instanceof StaticList) {
                StaticList list = (StaticList) folder;
                for (int i = 0; i < ids.length; i++) {
                    list.getResourceSet().add(ids[i]);
                }
                model.nodeStructureChanged(targetNode);
            }

            // Dropped in space or on smart list - create new folder
            else {
                StaticList list = new StaticList();
                for (int i = 0; i < ids.length; i++) {
                    list.getResourceSet().add(ids[i]);
                }
                appendFolder(list);
            }
            return true;
        }

        /**
         * Does the work for dropping a TreeNode.  This will have been 
         * transferred from elsewhere in this tree.
         *
         * @param   targetNode  node to drop onto
         * @param   childNode   node to drop
         * @return   true iff the drop was successful
         */
        private boolean dropTreeNode(DefaultMutableTreeNode targetNode, DefaultMutableTreeNode childNode) {
            final DefaultMutableTreeNode targetParent;
            final int childIndex;
            if (targetNode == null) {
                targetParent = (DefaultMutableTreeNode) model.getRoot();
                childIndex = targetParent.getChildCount();
            }
            else {
                ResourceFolder targetFolder = (ResourceFolder) targetNode.getUserObject();
                if (targetFolder instanceof ResourceBranch) {
                    targetParent = targetNode;
                    childIndex = targetNode.getChildCount();
                }
                else {
                    targetParent = (DefaultMutableTreeNode) targetNode.getParent();
                    childIndex = targetParent.getIndex(targetNode) + 1;
                }
            }

            // We insert a clone of the node rather than the node itself.
            // This is because we don't yet know whether this is a MOVE or
            // COPY, so the original may or may not need to get left in place.
            // Removal of the original on MOVE is handled in exportDone.
            model.insertNodeInto(model.duplicateNode(childNode, true), targetParent, childIndex);
            return true; 
        }

        /** try and convert something unknown into a list of URIs.
         * assume it's going to be a stream / reader / string, with a uri on each line.
         * @param o
         * @return a list of the resource ids we've managed to parse from it. Will be ivo:// format - but not guaranteed that they
         * point to actual resources.
         * @throws IOException
         */
        private URI[] convertUnknownToUriList(Object o) throws InvalidArgumentException, IOException {
            if (o == null) {
                throw new InvalidArgumentException("null dropped - how odd");
            }
            BufferedReader r = null;
            if (o instanceof String) {
                r = new BufferedReader(new StringReader((String)o));
            } else if (o instanceof InputStream ) {
                r = new BufferedReader(new InputStreamReader((InputStream)o));
            } else if (o instanceof Reader) {
                r = new BufferedReader((Reader)o);
            } else {
                throw new InvalidArgumentException("Unknow type dropped " + o.getClass().getName());
            }
            try {
                List result = new ArrayList();
                String line;
                while ((line = r.readLine()) != null) {
                    try {
                        URI u = new URI(line);
                        if (! u.getScheme().equals("ivo")) { // we only want ivo uris.
                            continue;
                        }
                        result.add(u);
                    } catch (URISyntaxException e) {
                        logger.debug("Dropping " + line);
                    }
                }
                if (result.isEmpty()) {
                    throw new InvalidArgumentException("No resource ids found");
                }
                return (URI[])result.toArray(new URI[result.size()]);
            } finally {
                if (r != null) {
                    try {
                        r.close();
                    } catch (IOException e) {
                        //netch
                    }
                }
            }
        }
    }

    /**
     * Transferable implementation for TreeNodes.
     * Only works within the same JVM.
     */
    private static class TreeNodeTransferable implements Transferable {
        private final DefaultMutableTreeNode node;

        TreeNodeTransferable(DefaultMutableTreeNode node) {
            this.node = node;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] {VoDataFlavour.TREENODE};
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor == VoDataFlavour.TREENODE;
        }

        public Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException {
            if (flavor == VoDataFlavour.TREENODE) {
                return node;
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
         *
         * @param  emph  true for emphasised, false for plain
         * @return font
         */
        private Font getFont(boolean emph) {
            if (plainFont == null) {
                Font font = getFont();
                plainFont = font.deriveFont(Font.PLAIN);
                emFont = font.deriveFont(Font.ITALIC);
            }
            return emph ? emFont : plainFont;
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean selected,
                                                      boolean expanded,
                                                      boolean leaf, int row,
                                                      boolean hasFocus) {
            Component comp =
                super.getTreeCellRendererComponent(tree, value, selected,
                                                   expanded, leaf, row,
                                                   hasFocus);
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                ResourceFolder folder = 
                    (ResourceFolder)
                    ((DefaultMutableTreeNode) value).getUserObject();
                label.setFont(getFont(folder.getSubscription() != null));
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
}
