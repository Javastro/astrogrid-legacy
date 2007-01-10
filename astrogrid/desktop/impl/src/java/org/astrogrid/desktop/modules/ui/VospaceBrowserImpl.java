/*$Id: VospaceBrowserImpl.java,v 1.17 2007/01/10 19:12:15 nw Exp $
 * Created on 22-Mar-2005
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
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.MyspaceBrowser;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.Preference;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.sendto.SendToMenu;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.client.NodeMetadata;
import org.astrogrid.store.Ivorn;

import com.l2fprod.common.swing.JTaskPane;
import com.l2fprod.common.swing.JTaskPaneGroup;

/**
 * 
 * Implementation of the myspaceBrowser component
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Mar-2005
 *  
 */
public class VospaceBrowserImpl extends AbstractVospaceBrowser implements MyspaceBrowser {

    protected final class CopyAction extends AbstractAction implements FileAction /*, FolderAction @todo not supported on folders at the moment*/ {
        public CopyAction() {
            super("Copy", IconHelper.loadIcon("copy.gif"));
            this.putValue(SHORT_DESCRIPTION, "Copy node");
            this.setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            final FileManagerNode n = getCurrentNodeManager().getCurrent();
            if (n == null) {
                return;
            }
            clipboard.action = Clipboard.COPY;
            clipboard.node = n;
            VospaceBrowserImpl.this.setStatusMessage("Copied " + n.getName() + " to clipboard");
        }
    }
    
    
    protected final class CutAction extends AbstractAction implements FileAction/*, FolderAction@todo not supported on folders a the moment */ {
        public CutAction() {
            super("Cut", IconHelper.loadIcon("cut_edit.gif"));
            this.putValue(SHORT_DESCRIPTION, "Cut node");
            this.setEnabled(false);
        }
        public void actionPerformed(ActionEvent e) {
            final FileManagerNode n = getCurrentNodeManager().getCurrent();
            if (n == null) {
                return;
            }
            clipboard.action = Clipboard.CUT;
            clipboard.node = n;
            //@todo mark the node as 'cut' in some way.
            VospaceBrowserImpl.this.setStatusMessage("Cut " + n.getName() + " to clipboard");
        }
    }    
    static class Clipboard {
        public FileManagerNode node;
        public int action;
        public static final int CUT = 1;
        public static final int COPY = 2;
    }
          
    protected Clipboard clipboard = new Clipboard();
    
    protected final class PasteAction extends AbstractAction implements FolderAction {
        public PasteAction() {
            super("Paste", IconHelper.loadIcon("paste_edit.gif"));
            this.putValue(SHORT_DESCRIPTION, "Paste node");
            this.setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            final FileManagerNode target = getCurrentNodeManager().getCurrent();
            if (target == null) {
                return;
            }                        
            if (clipboard.node == null) {
                return;
            }
            final String operationName = clipboard.action == Clipboard.CUT? "Move " : "Copy ";
            
            int result = JOptionPane.showConfirmDialog(VospaceBrowserImpl.this, 
                    operationName + clipboard.node.getName() + " to " + target.getName()
                    , "Confirm Paste",JOptionPane.YES_NO_OPTION );
        
            if (result == JOptionPane.NO_OPTION) {
                return;
            }
            getBlockingGlassPane().setVisible(true);            
            (new BackgroundOperation("Performing " + operationName){

                protected Object construct() throws Exception {
                    if (clipboard.action == Clipboard.CUT) {
                        //@todo known bug in filemanager client - will always fail for now.
                        clipboard.node.move(null,target,null);
                    } else {
                        FileManagerNode newNode = clipboard.node.copy(null,target,null);
                        // @todo do something with this.
                    }
                    return null;
                }
                protected void doAlways() {
                    getBlockingGlassPane().setVisible(false);
                }
                protected void doFinished(Object o) {
                    if (clipboard.node.isFolder()) {
                            getFolderTreeModel().reload(target);
                    }
                    // file list updates automatically.
                }                
            }).start();
        }
    }

  
    protected final class DeleteAction extends AbstractAction implements FileAction, FolderAction {
        public DeleteAction() {
            super("Delete", IconHelper.loadIcon("delete_obj.gif"));
            this.putValue(SHORT_DESCRIPTION, "Delete node");
            this.setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            getBlockingGlassPane().setVisible(true);
            final FileManagerNode n = getCurrentNodeManager().getCurrent();
            if (n == null) {
                return;
            }
            int option = JOptionPane.showConfirmDialog(VospaceBrowserImpl.this, "Delete "
                    + n.getName(), "Are you sure", JOptionPane.YES_NO_OPTION);
            if (option != JOptionPane.YES_OPTION) {
                return;
            }
            final boolean isFile = n.isFile(); // take note of these, before I
                                               // delete the thing.
            (new BackgroundOperation("Deleting " + n.getName()) {
                protected Object construct() throws Exception {
                    FileManagerNode parentNode = n.getParentNode(); // find it's
                                                                // parent - so
                                                                // we can inform
                                                                // it of the
                                                                // removal.
                    n.delete();
                    return parentNode;
                }

                protected void doAlways() {
                    getCurrentNodeManager().clear();
                    getBlockingGlassPane().setVisible(false);
                }
                protected void doFinished(Object o) {
                    if (o != null) {
                        if (isFile) {
                            getFileListModel().repopulate((FileManagerNode) o);
                        } else {
                            getFolderTreeModel().reload((FileManagerNode) o);
                        }
                    }
                }
            }).start();

        }
    }
/* replaced by save on send-to action
    protected final class GetContentAction extends AbstractAction implements FileAction {
        public GetContentAction() {
            super("Export data", IconHelper.loadIcon("export_log.gif"));
            this.putValue(SHORT_DESCRIPTION, "Export vospace file contents to a URL or local file");
            this.setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            final FileManagerNode n = getCurrentNodeManager().getCurrent();
            if (n == null) {
                return;
            }
            final URI u = chooser.chooseResourceWithParent("Select resource to write data to", false, true, true, true,VospaceBrowserImpl.this);
            if (u == null) {
                return;
            }
            (new BackgroundOperation("Copying data from vospace file " + n.getName() + " to " + u) {

                protected Object construct() throws Exception {
                    getVospace().copyContentToURL(new URI(n.getIvorn().toString()), u
                            .toURL());
                    return null;
                }
            }).start();

        }
    }
*/
    protected final class RelocateAction extends AbstractAction implements FileAction {
        public RelocateAction() {
            super("Relocate",IconHelper.loadIcon("repo_rep.gif"));
            this.putValue(SHORT_DESCRIPTION,"Relocate content to another filestore");
            this.setEnabled(false);
        }
 
        public void actionPerformed(ActionEvent e) {
            final FileManagerNode n = getCurrentNodeManager().getCurrent();
            if (n == null) {
                return;
            }
            // check it's got data to relocate.
            if (n.getMetadata().getSize() == null || n.getMetadata().getSize().intValue() ==0) {
                JOptionPane.showMessageDialog(VospaceBrowserImpl.this,"Can't relocate " + n.getName() + " as it has no data","Not possible",JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            // prompt for store to relocate to.
            // achieve this by popping up a registry micro-browser here.
            /*
            String newStore = JOptionPane.showInputDialog(VospaceBrowserImpl.this,
                    "<html>" + n.getName() +" is currently located at " + n.getMetadata().getContentLocation() + "<br>Enter ivorn of filestore to relocate to</html>"
                    ,"Relocate " + n.getName(),JOptionPane.QUESTION_MESSAGE);
                    */
            try {
            ResourceInformation[] rds = getVospace().listAvailableStores();
            int currentPos = 0;
            URI[] stores = new URI[rds.length];
            for (int i = 0;  i < stores.length; i++) {
                URI ivo = rds[i].getId();
                stores[i] = ivo;
                if (ivo.toString().equals(n.getMetadata().getContentLocation().toString())) {
                    currentPos = i;
                }
            }
            final URI uri = (URI)JOptionPane.showInputDialog(VospaceBrowserImpl.this,
                    "<html>" + n.getName() +" is currently located at " + n.getMetadata().getContentLocation() + "<br>Select another filestore to relocate this file to</html>"
                    ,"Relocate " + n.getName(), JOptionPane.QUESTION_MESSAGE, null
                    ,stores, stores[currentPos]
                    );
            if (uri == null) { // user pressed cancel.
                return;
            }

            (new BackgroundOperation("Relocating " + n.getName() + " to " + uri) {

                protected Object construct() throws Exception {
                    n.move(null,null,new Ivorn(uri.toString()));
                    return null;
                }
            }).start();
  
            } catch (ServiceException e1) {
                showError("Could not get list of available stores from registry",e1);
            }
        }
    }

    /** close action */
    protected final class CloseAction extends AbstractAction {
        public CloseAction() {
            super("Close",IconHelper.loadIcon("exit_small.png"));
            this.putValue(SHORT_DESCRIPTION,"Close the Workflow Builder");
            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
        }
        public void actionPerformed(ActionEvent e) {
        	hide();
        	dispose();
        }
    }
    
    protected final class CreateContentAction extends AbstractAction implements FileAction, FolderAction {

        public CreateContentAction() {
            super("Import data", IconHelper.loadIcon("import_log.gif"));
            this.putValue(SHORT_DESCRIPTION,
                    "Store data (from URL or local file) into a vospace file");
            this.setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            final FileManagerNode n = getCurrentNodeManager().getCurrent();
            if (n == null) {
                return;
            }            
            final URI u = chooser.chooseResourceWithParent("Select resource to read data from ", false, true, true,VospaceBrowserImpl.this);
            if (u == null) {
                return;
            }
            (new BackgroundOperation("Copying data from " + u + " to vospace file " + n.getName()) {
                protected Object construct() throws Exception {
                    // simple original was             getVospace().copyURLToContent(u.toURL(), new URI(n.getIvorn().toString()));                         
                    transferFiles(u,n);
                    return null;
                }
            }).start();
        }    

    private String conformToMyspaceName(String n) {
    	String name = n;
        name = name.replaceAll(" ", "_");
        name = name.replaceAll("/", "_");
        return name;
    }

    /**
     * Name: transferFiles
     * Description: method to copy a resource (local file or url) to Myspace.  Acts like a basic cp or copy command.
     * Also detects based on file extension
     * name if a file can be uncompressed or unarchived into myspace.  Currently supports tar, tar.gz, gz,  zip, jar, and war extensions.
     * @param uri uri to a internal or external resource such as a local file ex: (file://) or url ex: (http://)
     * @param node the current selected file or directory node in myspace. 
     * @throws Exception
     */
    void transferFiles(URI uri, FileManagerNode node) throws Exception { 
        URL url = uri.toURL();
        File file = new File(uri.getPath());
        
        String name = file.getName();
        String voName = conformToMyspaceName(name);
            //check if the myspace selected node is a file, if so then normal overwrite occurrs.
            if(node.isFile()) {
                getVospace().copyURLToContent(url, new URI(node.getIvorn().toString()));
            } else {
                    getVospace().copyURLToContent(url, new URI(node.getIvorn().toString() + "/" + voName));
            }//else
    }
   
    } // end of create content action
    

    protected final class RefreshAction extends AbstractAction implements FileAction, FolderAction {
        public RefreshAction() {
            super("Refresh", IconHelper.loadIcon("update.gif"));
            this.putValue(SHORT_DESCRIPTION, "Refresh node with server");
            this.setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            final FileManagerNode n = getCurrentNodeManager().getCurrent();
            if (n == null) {
                return;
            }
            (new BackgroundOperation("Refreshing " + n.getName()) {
                protected Object construct() throws Exception {
                    if (n.isFile()) {
                        n.getParentNode().refresh();
                    } else {                            
                        n.refresh();
                    }
                    return null;
                }

                protected void doAlways() {
                    // notify tree of changes..
                    if (n.isFolder()) {
                        getFolderTreeModel().reload(n);
                    } else {
                        getFolderTreeModel().reload(n.getParent());
                    }
                    // always refresh file list - dunno where the view is - just
                    // to be safe.
                    getFileListModel().reload();
                }
            }).start();
        }
    }

    protected final class RenameAction extends AbstractAction implements FileAction, FolderAction {
        public RenameAction() {
            super("Rename", IconHelper.loadIcon("text_obj.gif"));
            this.putValue(SHORT_DESCRIPTION, "Rename node");
            this.setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            final FileManagerNode n = getCurrentNodeManager().getCurrent();
            if (n == null) {
                return;
            }
            String newName = JOptionPane.showInputDialog(VospaceBrowserImpl.this,"Enter new name for " + n.getName() ,n.getName());
            if (newName == null) {
                return;
            }
            final String fname1 = newName.replaceAll(" ", "_");    
            getBlockingGlassPane().setVisible(true);            
            (new BackgroundOperation("Renaming " + n.getName()) {
                protected Object construct() throws Exception {                    
                    n.move(fname1,null,null);
                    return n.getParentNode();
                }

                protected void doAlways() {
                    getBlockingGlassPane().setVisible(false);
                }
                protected void doFinished(Object o) {
                        if (n.isFile()) {
                            getFileListModel().repopulate((FileManagerNode)o);
                        } else {
                            getFolderTreeModel().reload(n);
                        }                   
                }
            }).start();            
        }
    }

    
    protected final class SendToAction extends AbstractAction implements FileAction {

        public SendToAction() {
            super("Send To", IconHelper.loadIcon("read_obj.gif"));
            this.putValue(SHORT_DESCRIPTION, "Send file contents somewhere");
            this.setEnabled(false);
        }


        public void actionPerformed(ActionEvent e) {
        	try{
        		sendTo.show(getPreferredTransferable(),VospaceBrowserImpl.this,(Component)e.getSource(),1,1);
        	} catch(Exception ex) {
        		sendTo.show(getPreferredTransferable(),VospaceBrowserImpl.this,VospaceBrowserImpl.this,1,1);
        	}
        }
    }

    /** pane that displays information about a node 
     * implements the observer interface - which notifies of changes to the selected node.
     * implements property listener, which notifies changes to preferences that control what is displayed.
     * */
    static class InformationPane extends JTaskPane implements Observer, PropertyChangeListener {

        private final JLabel furtherInformation;
        private final JLabel information;
        private final JTaskPaneGroup furtherGroup ;
        {
            JTaskPaneGroup infoGroup = new JTaskPaneGroup();
            infoGroup.setText("Properties");
            infoGroup.setIcon(IconHelper.loadIcon("info_obj.gif"));
            infoGroup.setSpecial(true);

            furtherGroup = new JTaskPaneGroup();
            furtherGroup.setText("Advanced");
            furtherGroup.setIcon(IconHelper.loadIcon("read_obj.gif"));
            furtherGroup.setExpanded(false);
            

            add(infoGroup);
            add(furtherGroup);

            information = new JLabel();
            infoGroup.add(information);
            furtherInformation = new JLabel();
            furtherGroup.add(furtherInformation);
        }

        private final DateFormat df = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.MEDIUM,
                SimpleDateFormat.MEDIUM);


        /** display properties of a file node */
        public void displayInformation(FileManagerNode n) {
            observe(n);
            StringBuffer buff = new StringBuffer();
            NodeMetadata m = n.getMetadata();
            buff.append("<html>").append("<b>").append(n.getName()).append("</b><dl>").append(
                    "<dt>Created<dd>").append(df.format(m.getCreateDate().getTime())).append(
                    "<dt>Modified<dd>").append(df.format(m.getModifyDate().getTime())).append(
                    "<dt>Node Ivorn<dd>").append(m.getNodeIvorn());
            if (n.isFile()) {
            	final long rawSize = m.getSize().longValue();
				// dave pointed out thiat this shows <1K files as 0 in size.
                //int sz = Math.round(m.getSize().longValue() / 1024);
            	float kbs = rawSize / 1024 ;
            	int sz = rawSize == 0 ? 0  :  ( kbs < 1 ? 1 : Math.round(kbs));
				buff.append("<dt>Size<dd>").append(sz)
                        .append(" Kb").append("<dt>Store<dd>").append(m.getContentLocation());
            } else {
                buff.append("<dt>Children<dd>").append(n.getChildCount());
            }
            buff.append("</html>");
            information.setText(buff.toString());
            buff = new StringBuffer();
            buff.append("<html><dl>");
            for (Iterator i = m.getAttributes().entrySet().iterator(); i.hasNext();) {
                Map.Entry e = (Map.Entry) i.next();
                buff.append("<dt>").append(e.getKey()).append("<dd>").append(e.getValue());
            }
            buff.append("</dl></html>");
            furtherInformation.setText(buff.toString());
        }
        // the currently watched node
        private FileManagerNode watched;
        // set the node to observe.
        private synchronized void observe(FileManagerNode n) {
            if (watched != n ) { // already watching this one..
                if (watched != null) { // remove registration on previous node..
                    watched.deleteObserver(this);
                }
                watched = n;
                n.addObserver(this);
            }
        }

        /**
         * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
         */
        public synchronized void update(Observable o, Object arg) {
            // we've already got a reference to the node we're observing - so just redisplay on update
            // - neatly handles any concurrency issues too.
            displayInformation(watched);
        }
        
        public void clear() {
            information.setText("");
            furtherInformation.setText("");
        }

		public void propertyChange(PropertyChangeEvent evt) {
			boolean showAdvanced = Boolean.parseBoolean(evt.getNewValue().toString());
			furtherGroup.setVisible(showAdvanced);
		}
    }

    protected final BrowserControl browser;
    protected final ResourceChooserInternal chooser;
    private Actions actions = null;

    private JMenu fileMenu, editMenu;

    InformationPane informationPane;

    private JMenuBar jJMenuBar = null;

    private JSplitPane jSplitPane = null;
    private final Preference advancedPreference;
    /**
     * This is the default constructor

     */
    public VospaceBrowserImpl(Configuration conf, HelpServerInternal hs,UIInternal ui, MyspaceInternal vos, SendToMenu sendTo,BrowserControl browser,ResourceChooserInternal chooser, Preference pref) {
        super(conf, hs,ui,vos, sendTo);       
        this.browser = browser;
        this.chooser =chooser;
        this.advancedPreference = pref;
        initialize();
    }

    /**
     * @see org.astrogrid.desktop.modules.ui.AbstractVospaceBrowser#createCurrentNodeManager()
     */
    protected CurrentNodeManager createCurrentNodeManager() {
        return new CurrentNodeManager() {
            protected void updateDisplay() {
                super.updateDisplay();
                informationPane.displayInformation(getCurrent());
            }
        };
    }

    protected Actions getActions() {
        if (actions == null) {
            actions = new Actions(new Action[] { 
            		   new CreateFileAction(), 
            		   new CreateFolderAction(),
                       new SendToAction(), /*new GetContentAction(), new PutContentAction(), */
                       new CreateContentAction()
                     , new RenameAction()
                     , new CopyAction()
                     , new CutAction()
                     , new PasteAction() 
                     , new RelocateAction()
                     , new RefreshAction()                    
                     , new DeleteAction()  });
        }
        return actions;
    }

    protected InformationPane getInformationPane() {
        if (informationPane == null) {
            informationPane = new InformationPane();
            advancedPreference.addPropertyChangeListener(informationPane);
            // initialize it by firing in an event.
            String s= advancedPreference.getValue();
            PropertyChangeEvent e = new PropertyChangeEvent(advancedPreference,"value",s,s);
			informationPane.propertyChange(e);
        }
        return informationPane;
    }

    private JMenu getFileMenu() {
        if (fileMenu == null) {
            fileMenu = new JMenu();
            fileMenu.setText("File");
            fileMenu.setMnemonic(KeyEvent.VK_F);
            Action[] acts = getActions().list();
            fileMenu.add(acts[0]); //CreateFileAction()
            fileMenu.add(acts[1]); //CreateFolderAction()
            fileMenu.add(new JSeparator());
            fileMenu.add(acts[2]); //SendToAction()
            fileMenu.add(acts[3]); //CreateContentAction()
            fileMenu.add(acts[8]); //RelocateAction()
            fileMenu.add(acts[4]); //RenameAction()
            fileMenu.add(acts[9]); //RefreshAction()
            fileMenu.add(acts[10]);//DeleteAction()
            fileMenu.add(new JSeparator());
            fileMenu.add(new CloseAction());
          //  Action[] acts = getActions().list();
          //  for (int i = 0; i < acts.length; i++) {
          //  	if (! (acts[i] instanceof SendToAction)) { // skip this one            			
          //  		fileMenu.add(acts[i]);
          //  	}
          //  }
        }
        return fileMenu;
    }
    
    private JMenu getEditMenu() {
        if (editMenu == null) {
            editMenu = new JMenu();
            editMenu.setText("Edit");
            editMenu.setMnemonic(KeyEvent.VK_E);
            Action[] acts = getActions().list();
            editMenu.add(acts[5]); //CopyAction()
            editMenu.add(acts[6]); //CutAction()
            editMenu.add(acts[7]); //PasteAction()
          //  for (int i = 0; i < acts.length; i++) {
          //  	if (! (acts[i] instanceof SendToAction)) { // skip this one            			
          //  		editMenu.add(acts[i]);
          //  	}
          //  }
        }
        return editMenu;
    }

    /**
     * This method initializes jJMenuBar
     * 
     * @return javax.swing.JMenuBar
     */
    private JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new JMenuBar();
            jJMenuBar.add(getFileMenu());
            jJMenuBar.add(getEditMenu());
            /** crap - doesn't work. @todo need to find way to splice send-to menu into menu bar
            Action[] acts = getActions().list();
            for (int i = 0; i < acts.length; i++) {
            	if ((acts[i] instanceof SendToAction)) { //splice in the send-to menu  
            		jJMenuBar.add(new JMenuItem(acts[i]));
            	}
            }            */
            jJMenuBar.add(Box.createHorizontalGlue());
            jJMenuBar.add(createHelpMenu());            
        }
        return jJMenuBar;
    }
    
    

    /**
     * This method initializes jSplitPane
     * 
     * @return javax.swing.JSplitPane
     */
    private JSplitPane getJSplitPane() {
        if (jSplitPane == null) {
            jSplitPane = new JSplitPane();
            JSplitPane innerSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            innerSplitPane.setDividerSize(5);
            innerSplitPane.setDividerLocation(300);
            innerSplitPane.setOneTouchExpandable(true);
            innerSplitPane.setLeftComponent(new JScrollPane(getFolderTree()));
            innerSplitPane.setRightComponent(new JScrollPane(getInformationPane()));

            jSplitPane.setDividerSize(5);
            jSplitPane.setDividerLocation(300);
            jSplitPane.setLeftComponent(innerSplitPane);
            jSplitPane.setRightComponent(new JScrollPane(getFileList()));
        }
        return jSplitPane;
    }

    /*
     * This method initializes this
     * 
     * @return void @throws CommunityException @throws RegistryException @throws
     * URISyntaxException @throws RemoteException @throws NodeNotFoundFault
     * @throws FileManagerFault
     */
    private void initialize() {
        this.setJMenuBar(getJJMenuBar());
        this.setName("MyspaceBrowser");
        getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.myspaceBrowser");          
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(IconHelper.loadIcon("filenav_nav.gif").getImage());         
        JPanel pane = getMainPanel();
        pane.add(getJSplitPane(), java.awt.BorderLayout.CENTER);
        JToolBar toolBar = getToolBar();
        pane.add(toolBar, java.awt.BorderLayout.NORTH);
        this.setContentPane(pane);
        this.setTitle("MySpace Browser");
        this.setSize(565, 800);
        readRoot();
    }
    
    public void userLogout(UserLoginEvent e) {
        getInformationPane().clear();
        super.userLogout(e);

        
    }

}

/*
 * $Log: VospaceBrowserImpl.java,v $
 * Revision 1.17  2007/01/10 19:12:15  nw
 * integrated with preferences.
 *
 * Revision 1.16  2007/01/09 16:14:02  nw
 * added use of monitoring service
 *
 * Revision 1.15  2006/11/14 10:30:39  nw
 * fixed my previous fix.
 *
 * Revision 1.14  2006/11/14 09:58:36  nw
 * myspace fixlet.
 *
 * Revision 1.13  2006/10/16 14:32:44  pjn3
 * Bugs # 1892 and 1896
 * Menu alterred to include File/Edit and close
 *
 * Revision 1.12  2006/06/27 10:37:44  nw
 * removed unused cruft.added send-to menu
 *
 * Revision 1.11  2006/04/18 23:25:43  nw
 * merged asr development.
 *
 * Revision 1.10.30.2  2006/04/14 02:45:01  nw
 * finished code.extruded plastic hub.
 *
 * Revision 1.10.30.1  2006/04/04 10:31:26  nw
 * preparing to move to mac.
 *
 * Revision 1.10  2005/11/24 01:13:24  nw
 * merged in final changes from release branch.
 *
 * Revision 1.9.2.1  2005/11/23 04:41:26  nw
 * make sure blocking glass pane is down - so same resource can't be deleted twice.
 *
 * Revision 1.9  2005/11/11 10:08:18  nw
 * cosmetic fixes
 *
 * Revision 1.8  2005/10/14 14:20:41  nw
 * work around for problems with FileStoreOutputStream
 *
 * Revision 1.7  2005/10/12 13:30:10  nw
 * merged in fixes for 1_2_4_beta_1
 *
 * Revision 1.3.10.2  2005/10/12 09:21:38  nw
 * added java help system
 *
 * Revision 1.3.10.1  2005/10/10 18:12:36  nw
 * merged kev's datascope lite.
 *
 * Revision 1.6  2005/10/07 12:12:21  KevinBenson
 * resorted back to adding to the ResoruceChooserInterface a new method for selecting directories.
 * And then put back the older one.
 *
 * Revision 1.5  2005/10/06 09:20:24  KevinBenson
 * took out some println's still need to comment a little more later.
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
 * Revision 1.9  2005/08/05 11:46:55  nw
 * reimplemented acr interfaces, added system tests.
 *
 * Revision 1.8  2005/07/10 18:07:38  nw
 * files for 1.0.5
 *
 * Revision 1.7  2005/07/08 11:08:01  nw
 * bug fixes and polishing for the workshop
 *
 * Revision 1.6  2005/06/20 16:56:40  nw
 * fixes for 1.0.2-beta-2
 *
 * Revision 1.5  2005/05/12 15:59:08  clq2
 * nww 1111 again
 *
 * Revision 1.3.8.1  2005/05/09 14:51:02  nw
 * renamed to 'myspace' and 'workbench'
 * added confirmation on app exit.
 *
 * Revision 1.3  2005/04/27 13:42:40  clq2
 * 1082
 *
 * Revision 1.2.2.6  2005/04/26 17:26:04  nw
 * added move / copy / rename  / relocate to vospace browser.
 *
 * Revision 1.2.2.5  2005/04/25 17:19:11  nw
 * more rendering improvements
 *
 * Revision 1.2.2.4  2005/04/25 16:41:29  nw
 * added file relocation (move data between stores)
 *
 * Revision 1.2.2.3  2005/04/25 11:18:50  nw
 * split component interfaces into separate package hierarchy
 * - improved documentation
 *
 * Revision 1.2.2.2  2005/04/22 10:55:32  nw
 * implemented vospace file chooser dialogue.
 * Revision 1.2.2.1 2005/04/15 13:00:45 nw got
 * vospace browser working. Revision 1.2 2005/04/13 12:59:18 nw checkin from
 * branch desktop-nww-998
 * 
 * Revision 1.1.2.2 2005/04/13 12:23:27 nw refactored a common base class for ui
 * components
 * 
 * Revision 1.1.2.1 2005/04/04 16:43:48 nw made frames remember their previous
 * positions. synchronized guiLogin, so only one login box ever comes up. made
 * refresh action in jobmonitor more robust
 * 
 * Revision 1.1.2.2 2005/04/04 08:49:27 nw working job monitor, tied into pw
 * launcher.
 * 
 * Revision 1.1.2.1 2005/03/22 12:04:03 nw working draft of system and ag
 * components.
 *  
 */