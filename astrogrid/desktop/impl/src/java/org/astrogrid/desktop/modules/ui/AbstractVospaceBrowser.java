/*$Id: AbstractVospaceBrowser.java,v 1.14 2007/05/10 19:35:26 nw Exp $
 * Created on 21-Apr-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.ui;

import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.iterators.FilterIterator;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.comp.GlassPane;
import org.astrogrid.filemanager.client.FileManagerNode;

/** Abstract class for displays of myspace - in file chooser dialogues and myspace browser.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 21-Apr-2005
 *  @todo make tree expansion behave same as clicking on name - both show child folders, and contents.
 */
public abstract class AbstractVospaceBrowser extends UIComponentImpl implements UserLoginListener {

    /** class that manages the currently selected node */
    public class CurrentNodeManager implements TreeSelectionListener, ListSelectionListener {
        private FileManagerNode current;

        public FileManagerNode getCurrent() {
            return current;
        }

        

        public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
            FileManagerNode n = (FileManagerNode) folderTree.getLastSelectedPathComponent();
            if (n != null) {
                this.current = n;
                updateDisplay();
                files.repopulate(n);
            }
        }

        public void valueChanged(ListSelectionEvent e) {
            FileManagerNode n = (FileManagerNode) fileList.getSelectedValue();
            if (n != null) {
                this.current = n;
                updateDisplay();
            }
        }
        
        public void clear() {
            current = null;
            getActions().disableAllActions();
            getFolderTree().clearSelection();
            getFileList().clearSelection();
        }

        protected void updateDisplay() {
            // enable correct set of actions, and remove selection in other pane.
            //logger.debug(current.getName());
            if (getCurrent().isFile()) {
                getActions().enableFileActions();
                getFolderTree().clearSelection();
            } else {
                getActions().enableFolderActions();
                getFileList().clearSelection();
            }
        }

    }

    protected class FileManagerNodeListModel extends DefaultListModel implements Observer {
        private final FilterIterator fi;
        {
            fi = new FilterIterator();
            fi.setPredicate(filePredicate);
        }



        /** fire events to tell all listeners that maybe something has changed.. */
        public void reload() {
            this.fireContentsChanged(this, 0, this.getSize() - 1);
        }

        public void repopulate(FileManagerNode n) {
            observe(n);
            super.clear();
            try {
                for (fi.setIterator(n.iterator()); fi.hasNext();) {
                    super.addElement(fi.next());
                }
            } catch (Exception e) {
                showError("Could not display contents", e);
            }

        }
        private FileManagerNode watched;
        private synchronized void observe(FileManagerNode n) {
            if (watched != n) { // otherwise already watching this one..
                if (watched != null) {
                    watched.deleteObserver(this);
                }
                watched = n;
                watched.addObserver(this);
            }            
        }
        // we know already what we're watching..
        public synchronized void update(Observable o, Object arg) {
            repopulate(watched);
        }
    }

    // marker interfaces.
    /** marker interface for an action that applies to myspace files */
    protected interface FileAction {
    }
    protected interface SendToAction {
    }

    /** marker interface for an action that applies to myspace folders */
    protected interface FolderAction {
    }


    protected final class CreateFolderAction extends AbstractAction implements FolderAction {
        public CreateFolderAction() {
            super("Create Folder", IconHelper.loadIcon("foldernew16.png"));
            this.putValue(SHORT_DESCRIPTION, "Create a new folder within the selected folder");
            this.setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            final FileManagerNode n = getCurrentNodeManager().getCurrent();
            if (n == null) {
                return;
            }
            String fname = JOptionPane.showInputDialog(AbstractVospaceBrowser.this,
                    "Name for new folder", "NewFolder");
            if (fname == null || fname.trim().length() < 1) {
                return;
            }
            final String fname1 = fname.replaceAll(" ", "_");
            getBlockingGlassPane().setVisible(true);
            (new BackgroundOperation("Creating " + fname1) {

                protected Object construct() throws Exception {
                    return n.addFolder(fname1);
                }

                protected void doAlways() {
                    getBlockingGlassPane().setVisible(false);
                }

                protected void doFinished(Object o) {
                    getFolderTreeModel().reload(n);
                    JTree folderTree2 = getFolderTree();
                    folderTree2.expandPath(folderTree2.getSelectionPath());;
                }
            }).start();
        }
    }
    
    protected abstract Actions getActions() ;

    /** class to manage all this application's actions */
    protected static class Actions {
        private final Action[] acts;

        public Actions(Action[] acts) {
            this.acts = acts;
        }

        public void disableAllActions() {
            for (int i = 0; i < acts.length; i++) {
                acts[i].setEnabled(false);
            }
        }

        public void enableFileActions() {
            for (int i = 0; i < acts.length; i++) {
                acts[i].setEnabled(acts[i] instanceof FileAction || acts[i] instanceof SendToAction);
            }
        }

        public void enableFolderActions() {
            for (int i = 0; i < acts.length; i++) {
                acts[i].setEnabled(acts[i] instanceof FolderAction);
            }
        }

        public Action[] list() {
            return acts;
        }
    }

    
    protected final class CreateFileAction extends AbstractAction implements FolderAction {
        public CreateFileAction() {
            super("New File", IconHelper.loadIcon("filenew16.png"));
            this.putValue(SHORT_DESCRIPTION, "Create a new file within the selected folder");
            this.setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            final FileManagerNode n = getCurrentNodeManager().getCurrent();
            if (n == null) {
                return;
            }
            String fname = JOptionPane.showInputDialog(AbstractVospaceBrowser.this,
                    "Name for new file", "NewFile");
            if (fname == null || fname.trim().length() < 1) {
                return;
            }
            final String fname1 = fname.replaceAll(" ", "_");
            getBlockingGlassPane().setVisible(true);
            (new BackgroundOperation("Creating " + fname1) {

                protected Object construct() throws Exception {
                    FileManagerNode newNode =  n.addFile(fname1);
                    return newNode;
                }
                protected void doFinished(Object o) {
                    FileManagerNode newFile = (FileManagerNode)o;
                    getFileList().setSelectedValue(newFile,true);
                    // following is maybe a bit belt-and-braces, but it makes sure the tree never vanishes.
                    getFolderTreeModel().reload(newFile.getParent());
                }
                protected void doAlways() {

                    getBlockingGlassPane().setVisible(false);
                }

            }).start();
        }
    }

    
    /**
     * tree model that filters the file tree, just displaying folders. also
     * written so that tree traversal takes place in worker threads (as it may
     * require calls to the server under-the-hood to retreive the next layer of
     * nodes, etc.
     * 
     * @author Noel Winstanley noel.winstanley@manchester.ac.uk 14-Apr-2005
     *  
     */
    protected class FolderTreeModel extends DefaultTreeModel {

     
        /**
         * use this to keep track of which nodes we're currently retreiving - so
         * we don't do two simultaneous retreivals of the same node.
         */
        protected Set workingNodes = new HashSet();

        private final FilterIterator fi;

        private final Predicate folderPredicate = new Predicate() {
            public boolean evaluate(Object arg0) {
                return ((FileManagerNode) arg0).isFolder();
            }
        };

        /**
         * Construct a new FolderTreeModel
         * 
         * @param root
         */
        public FolderTreeModel(TreeNode root) {
            super(root);
            fi = new FilterIterator();
            fi.setPredicate(folderPredicate);
        }

        public Object getChild(Object parent, int index) {
            logger.debug("getChild");
            try {
                fi.setIterator(((FileManagerNode) parent).iterator());
                if (!fi.hasNext()) {
                    return null;
                }
                for (int i = 0; i < index; i++) {
                    fi.next();
                    if (!fi.hasNext()) {
                        return null;
                    }
                }
                return fi.next();
            } catch (Exception e) {
                showError("Error traversing tree", e);
                return null;
            }

        }

        /**
         * this is always the first method that causes another tier of nodes to
         * be retreived - so adjust this one to retrieve in the background,
         * leaving a temporary node there for now..
         * 
         * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
         */
        public int getChildCount(Object parent) {
            logger.debug("getChildCount");
            final FileManagerNode p = (FileManagerNode) parent;
            if (p.isChildrenInCache()) { // ok. already grabbed these nodes.
                try {
                    fi.setIterator(((FileManagerNode) parent).iterator());
                    int ix;
                    for (ix = 0; fi.hasNext(); ix++, fi.next()) {
                        // intentionally empty.
                    }
                    return ix;
                } catch (Exception e) {
                    showError("Error traversing tree", e);
                }
                return 0;
            } else { // start background process to retreive this node.
                if (workingNodes.contains(p)) { // already being worked on -
                                                // just stall for now.
                    return 0;
                }
                logger.debug("Blocking to retrieve " + p.getName());
                workingNodes.add(p);
                getBlockingGlassPane().setVisible(true);
                (new BackgroundOperation("Fetching subtree of " + p.getName()) {
                    protected int acc;

                    protected Object construct() throws Exception {
                        // need a new iterator here - as running in different
                        // thread
                        Iterator it = new FilterIterator(p.iterator(), folderPredicate);
                        for (acc = 0; it.hasNext(); acc++, it.next()) {
                            //intentionally empty
                        }
                        return null;
                    }

                    /** always executed. */
                    protected void doAlways() {
                        workingNodes.remove(p);
                        getBlockingGlassPane().setVisible(false);
                        logger.debug("Completed retreival of " + p.getName());
                    }

                    protected void doFinished(Object o) {
                        if (acc > 0) { // found something - notify
                            int[] arr = new int[acc];
                            for (int i = 0; i < acc; i++) {
                                arr[i] = i;
                            }
                            nodesWereInserted(p, arr);
                        }
                    }
                }).start();
                return 0; //temporary value, until background operation
                          // returns.
            }
        }

        public int getIndexOfChild(Object parent, Object child) {
            logger.debug("getIndexOfChild");
            if (parent == null || child == null) {
                return -1;
            }
            try {
                fi.setIterator(((FileManagerNode) parent).iterator());
                for (int ix = 0; fi.hasNext(); ix++) {
                    if (child.equals(fi.next())) {
                        return ix;
                    }
                }
            } catch (Exception e) {
                showError("Error traversing tree", e);
            }
            return -1;
        }

        public boolean isLeaf(Object node) {
            // we want no leaves - want all folders to look the same.
            return false;
            /*
            FileManagerNode n = (FileManagerNode) node;
            return n.getChildCount() == 0 || workingNodes.contains(n);
            */
        }
    }

    private static final class FileNodeCellRenderer extends DefaultListCellRenderer {
        Icon i = (IconHelper.loadIcon("ascii16.png"));

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            FileManagerNode n = (FileManagerNode) value;
            setText(n.getName());
            this.setIcon(i);
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            return this;
        }
    }

    private CurrentNodeManager currentNodeManager;

    private JList fileList;

    private final Predicate filePredicate = new Predicate() {
        public boolean evaluate(Object arg0) {
            return ((FileManagerNode) arg0).isFile();
        }
    };

    private FileManagerNodeListModel files;

    private JTree folderTree = null;

    private FolderTreeModel folderTreeModel = null;

    private GlassPane glassPane = null;
    
  //  protected final SendToMenu sendTo;

	//private  FileManagerNodePreferredTransferable atom ;


    /**
     * Construct a new AbstractVospaceBrowser
     *  - sutable for long-lived windows.
     * @param conf
     * @param ui
     * @throws HeadlessException
     */
    public AbstractVospaceBrowser(UIContext context,MyspaceInternal vos, Object sendTo) throws HeadlessException {
        super(context);
        this.vos = vos;
    //    this.sendTo = sendTo;

    }

    protected abstract CurrentNodeManager createCurrentNodeManager();

    protected GlassPane getBlockingGlassPane() {
        if (glassPane == null) {
            glassPane = GlassPane.mount(getFolderTree(), true);
        }
        return glassPane;
    }

    /**
     * @return
     */
    protected CurrentNodeManager getCurrentNodeManager() {
        if (currentNodeManager == null) {
            currentNodeManager = createCurrentNodeManager();
        }
        return currentNodeManager;
    }

    public JList getFileList() {
        if (fileList == null) {
            fileList = new JList(getFileListModel());
            fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            fileList.addListSelectionListener(getCurrentNodeManager());
            fileList.setCellRenderer(new FileNodeCellRenderer());
            fileList.addMouseListener(new MouseAdapter() {
            	public void mousePressed(MouseEvent e) {
            		if (e.isPopupTrigger()) {
            			processPopup(e);
            		}
            	}
            	public void mouseReleased(MouseEvent e) {
            		if (e.isPopupTrigger()) {
            			processPopup(e);
            		}
            	}
            	private void processPopup(MouseEvent e) {
            		// we want right-clicks to select an item too.
            		int selectedIndex = fileList.locationToIndex(e.getPoint());
            		fileList.setSelectedIndex(selectedIndex);
            	//	sendTo.show(getPreferredTransferable(),AbstractVospaceBrowser.this,fileList,e.getX(),e.getY());
            	}
            });
            
        }
        return fileList;
    }

    protected FileManagerNodeListModel getFileListModel() {
        if (files == null) {
            files = new FileManagerNodeListModel();
        }
        return files;
    }

    /**
     * This method initializes jTree
     * 
     * @return javax.swing.JTree
     */
    public JTree getFolderTree() {
        if (folderTree == null) {
            folderTree = new JTree() {
                public String convertValueToText(Object value, boolean a, boolean b, boolean c, int i, boolean d) {
                    if (value instanceof FileManagerNode) {
                        return ((FileManagerNode) value).getName();
                    } else {
                        return super.convertValueToText(value, a, b, c, i, d);
                    }
                }
                
            };
            folderTree.setModel(getFolderTreeModel());
            folderTree.setCellEditor(null);
            folderTree.addTreeSelectionListener(getCurrentNodeManager());
       
        }
        return folderTree;
    }

    protected FolderTreeModel getFolderTreeModel() {
        if (folderTreeModel == null) {
            folderTreeModel = new FolderTreeModel(null);
        }
        return folderTreeModel;
    }

    private final MyspaceInternal vos;
    
    public MyspaceInternal getVospace() {
        return vos;
    }

    public void readRoot() {
        (new BackgroundOperation("Reading Vospace Root") {
    
            protected Object construct() throws Exception {
                //return vos.node(vos.getHome());
            	return vos.getClient().home();
            }
    
            protected void doFinished(Object result) {
                getFolderTreeModel().setRoot((FileManagerNode) result);
                getFolderTree().setSelectionRow(0);                
            }
        }).start();
    
    }

    private JToolBar jToolBar = null;

    /**
     * This method initializes jToolBar
     * 
     * @return javax.swing.JToolBar
     */
    public JToolBar getToolBar() {
        if (jToolBar == null) {
            jToolBar = new JToolBar();
            jToolBar.setRollover(true);
            jToolBar.setFocusable(false);
            Action[] acts = getActions().list();
            for (int i = 0; i < acts.length; i++) {
                jToolBar.add(acts[i]);
            }
        }
        return jToolBar;
    }

    /**
     * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogin(org.astrogrid.desktop.modules.ag.UserLoginEvent)
     */
    public void userLogin(UserLoginEvent e) {
        readRoot();
        
    }

    /**
     * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogout(org.astrogrid.desktop.modules.ag.UserLoginEvent)
     */
    public void userLogout(UserLoginEvent e) {
        getCurrentNodeManager().clear();
        getFolderTreeModel().setRoot(null);
        getFileListModel().clear();

        
    }
}

/*
 * $Log: AbstractVospaceBrowser.java,v $
 * Revision 1.14  2007/05/10 19:35:26  nw
 * reqwork
 *
 * Revision 1.13  2007/04/18 15:47:05  nw
 * tidied up voexplorer, removed front pane.
 *
 * Revision 1.12  2007/03/08 17:43:58  nw
 * first draft of voexplorer
 *
 * Revision 1.11  2007/01/29 11:11:37  nw
 * updated contact details.
 *
 * Revision 1.10  2006/11/09 12:08:33  nw
 * final set of changes for 2006.4.rc1
 *
 * Revision 1.9  2006/10/16 14:31:25  pjn3
 * Bugs # 1892 and 1896
 * Menu alterred to include File/Edit and close
 *
 * Revision 1.8  2006/06/27 10:30:33  nw
 * findbugs tweaks & send-to actions.
 *
 * Revision 1.7  2006/04/18 23:25:43  nw
 * merged asr development.
 *
 * Revision 1.6.42.1  2006/04/14 02:45:01  nw
 * finished code.extruded plastic hub.
 *
 * Revision 1.6  2005/11/01 09:19:46  nw
 * messsaging for applicaitons.
 *
 * Revision 1.5  2005/10/12 13:30:10  nw
 * merged in fixes for 1_2_4_beta_1
 *
 * Revision 1.3.10.2  2005/10/12 09:21:38  nw
 * added java help system
 *
 * Revision 1.3.10.1  2005/10/10 18:12:36  nw
 * merged kev's datascope lite.
 *
 * Revision 1.4  2005/10/04 20:46:48  KevinBenson
 * new datascope launcher and change to module.xml for it.  Vospacebrowserimpl changes to handle file copies to directories on import and export
 *
 * Revision 1.3  2005/09/02 14:03:34  nw
 * javadocs for impl
 *
 * Revision 1.2  2005/08/25 16:59:58  nw
 * 1.1-beta-3
 *
 * Revision 1.1  2005/08/11 10:15:00  nw
 * finished split
 *
 * Revision 1.7  2005/08/05 11:46:55  nw
 * reimplemented acr interfaces, added system tests.
 *
 * Revision 1.6  2005/07/08 11:08:01  nw
 * bug fixes and polishing for the workshop
 *
 * Revision 1.5  2005/06/20 16:56:40  nw
 * fixes for 1.0.2-beta-2
 *
 * Revision 1.4  2005/05/12 15:59:08  clq2
 * nww 1111 again
 *
 * Revision 1.2.8.1  2005/05/09 14:51:02  nw
 * renamed to 'myspace' and 'workbench'
 * added confirmation on app exit.
 *
 * Revision 1.2  2005/04/27 13:42:40  clq2
 * 1082
 *
 * Revision 1.1.2.5  2005/04/26 17:26:04  nw
 * added move / copy / rename  / relocate to vospace browser.
 *
 * Revision 1.1.2.4  2005/04/25 17:19:11  nw
 * more rendering improvements
 *
 * Revision 1.1.2.3  2005/04/25 16:41:29  nw
 * added file relocation (move data between stores)
 *
 * Revision 1.1.2.2  2005/04/25 11:18:50  nw
 * split component interfaces into separate package hierarchy
 * - improved documentation
 *
 * Revision 1.1.2.1  2005/04/22 10:55:32  nw
 * implemented vospace file chooser dialogue.
 *
 */