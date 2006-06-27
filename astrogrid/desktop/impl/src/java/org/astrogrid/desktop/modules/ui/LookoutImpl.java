/*$Id: LookoutImpl.java,v 1.14 2006/06/27 10:35:11 nw Exp $
 * Created on 26-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.Jobs;
import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.acr.astrogrid.RemoteProcessManager;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.ApplicationLauncher;
import org.astrogrid.acr.ui.Lookout;
import org.astrogrid.acr.ui.ParameterizedWorkflowLauncher;
import org.astrogrid.acr.ui.WorkflowBuilder;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.JobsInternal;
import org.astrogrid.desktop.modules.ag.MessageRecorderImpl;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal;
import org.astrogrid.desktop.modules.ag.RemoteProcessStrategy;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal.Folder;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal.MessageContainer;
import org.astrogrid.desktop.modules.ag.recorder.ResultsExecutionMessage;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.lookout.FolderTreeCellRenderer;
import org.astrogrid.desktop.modules.ui.lookout.MessageDisplayPane;
import org.astrogrid.desktop.modules.ui.lookout.MessageTable;
import org.astrogrid.desktop.modules.ui.lookout.ResultsList;
import org.astrogrid.desktop.modules.ui.sendto.SendToMenu;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Oct-2005
 *   
 *
 */
public class LookoutImpl extends UIComponentImpl implements  Lookout{
	
	/** display a workflow transceript */

    private final class ViewTranscriptAction extends AbstractAction {
        private final Jobs jobs;
        private final WorkflowBuilder transcriptViewer;
        public ViewTranscriptAction(Jobs jobs, WorkflowBuilder transcriptViewer) {
            super("View Transcript",IconHelper.loadIcon("tree.gif"));
            this.jobs = jobs;
            this.transcriptViewer = transcriptViewer;
            this.putValue(SHORT_DESCRIPTION,"View transcript of selected workflow");
            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_V));
            this.setEnabled(false);
        }
        public void actionPerformed(ActionEvent e) {
            (new BackgroundOperation("Launching transcript viewer") {
                protected Object construct() throws Exception {
                	Document doc = jobs.getJobTranscript(getCurrentFolder().getInformation().getId());
                		transcriptViewer.showTranscript(DomHelper.DocumentToString(doc));     	                	
                    return null;
                }
            }).start();
        }
    }
    
    /** delete an alert message, or a task folder
     * listens to both tree and list to work out whether enabled or not.
     */
    private final class DeleteAction extends AbstractAction implements TreeSelectionListener, ListSelectionListener {
        
        private boolean folderMode;
        
        public DeleteAction() {
            super("Delete", IconHelper.loadIcon("delete_obj.gif"));
            this.putValue(SHORT_DESCRIPTION,"Delete a task record, or event message");
            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_D));
            this.setEnabled(false);            
            getFolderTree().addTreeSelectionListener(this);
            getMessageTable().getSelectionModel().addListSelectionListener(this);
        }
        
        public void actionPerformed(ActionEvent e) {   
                if (folderMode) { // delete a task            
                    final Folder f = getCurrentFolder();
                    (new BackgroundOperation("Deleting Process Record") {
                        protected Object construct() throws Exception {
                            if (isRunning(f.getInformation().getStatus())) {
                                try {
                                    manager.halt(f.getInformation().getId());
                                } catch (Exception ex) {
                                    //  @todo warn someone.
                                }
                            }
                            manager.delete(f.getInformation().getId()); // this in turn fires a table update event, in the swing thread.
                            return null;
                        }
                    }).start();
               
                } else { // delete an alert message
                    (new BackgroundOperation("Deleting Message") {
                        protected Object construct() throws Exception {                    
                            int row = getMessageTable().getSelectedRow();                
                            recorder.deleteMessage(row);
                            return null;
                        }
                    }).start();
                }
        }
        
        public void valueChanged(ListSelectionEvent e) {
            folderMode=false;
            setEnabled(false) ;            
        }
        
        public void valueChanged(TreeSelectionEvent e) {
            folderMode = true;
            // can't optimize this - race condition.
            Folder f = (Folder)getFolderTree().getLastSelectedPathComponent();            
            setEnabled(f != null &&  isTaskFolder(f.getInformation().getId()));        
        }
    }
    
    /** action for halting something. listens to current tree selection to determine whether enabled or not */
    private final class HaltAction extends AbstractAction implements TreeSelectionListener{
        public HaltAction() {
            super("Halt", IconHelper.loadIcon("stop.gif"));
            this.putValue(SHORT_DESCRIPTION,"Halt the execution of a task or job");
            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_H));
            this.setEnabled(false);
            getFolderTree().addTreeSelectionListener(this);
        }
        
        public void actionPerformed(ActionEvent e) {
            final Folder f = (Folder)getCurrentFolder();                
            (new BackgroundOperation("Cancelling Application") {
                protected Object construct() throws Exception {
                    manager.halt(f.getInformation().getId());
                    return null;
                }                    
            }).start();                
        }
        
        public void valueChanged(TreeSelectionEvent e) {
            Folder f = (Folder)getFolderTree().getLastSelectedPathComponent();
            setEnabled(f != null &&  isTaskFolder(f.getInformation().getId()) && isRunning(f.getInformation().getStatus())); 
        }        
    }
    
    private final class MarkAllReadAction extends AbstractAction implements TreeSelectionListener {
        public MarkAllReadAction() {
            super("Mark all Read", IconHelper.loadIcon("complete_status.gif"));
            this.putValue(SHORT_DESCRIPTION,"Mark all messages from this process as read");
            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_M));
            this.setEnabled(false);
            getFolderTree().addTreeSelectionListener(this);            
        }
        public void actionPerformed(ActionEvent e) {
            (new BackgroundOperation("Marking messages as read") {// therre's a lot of inefficient IO here, so do in background.
                protected Object construct() throws Exception {         
                Folder f =getCurrentFolder();
                MessageContainer[] msgs = recorder.listFolder(f);
                for (int i = 0; i < msgs.length; i++) {
                    if (msgs[i].isUnread()) {
                        msgs[i].setUnread(false);
                        recorder.updateMessage(msgs[i]);
                    }
                    f.setUnreadCount(0);
                    recorder.updateFolder(f);
                }
                return null;
                }
            }).start();

        }
        public void valueChanged(TreeSelectionEvent e) {
            //@todo - unsure whether it's safe to optimze this by calling 'getCurrentFolder()' - race condition..
            Folder f = (Folder)getFolderTree().getLastSelectedPathComponent();
            setEnabled(f != null && isTaskFolder(f.getInformation().getId()));
        }
    }

    private final class RefreshAction extends AbstractAction {
    	private final List strategies;
        public RefreshAction(List strategies) {
            super("Refresh",IconHelper.loadIcon("update.gif"));
            this.strategies = strategies;
            this.putValue(SHORT_DESCRIPTION,"Check for new events and messages now");
            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_R));
        }
        public void actionPerformed(ActionEvent e) {
        	for (Iterator i = strategies.iterator(); i.hasNext(); ) {
        		RemoteProcessStrategy rps = (RemoteProcessStrategy)i.next();
        		rps.triggerUpdate();
        	}
            //@todo add a 'completed' task that sets the status message.
            LookoutImpl.this.setStatusMessage("Refreshing..");
        }
    }    
    


    /** close action */
    protected final class CloseAction extends AbstractAction {

        public CloseAction() {
            super("Close",IconHelper.loadIcon("exit_small.png"));
            this.putValue(SHORT_DESCRIPTION,"Close Lookout");
            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
        }
        public void actionPerformed(ActionEvent e) {
            hide();
            dispose();
        }
    }
    private MessageDisplayPane contentPane;
    private Folder currentFolder;
   private final DeleteAction deleteAction;
    private JTree folderTree;
    private final HaltAction haltAction;
    private JMenuBar jJMenuBar;
    private JMenu fileMenu;
    private final CloseAction closeAction;
    private JMenu manageMenu;
    private final MarkAllReadAction markAllReadAction;
    
    private JPanel messageDetails;
    
    private MessageTable messageTable;
    private final RefreshAction refreshAction;
    private final ViewTranscriptAction transcriptAction;
    private final ResultsList results;

    private JToolBar toolbar;

    private final RemoteProcessManager manager;


    private final MessageRecorderInternal recorder;
    
    /** Construct a new Lookout
     * @param conf
     * @param hs
     * @param ui
     * @throws HeadlessException
     */
    public LookoutImpl(Configuration conf, HelpServerInternal hs, UIInternal ui
            , MessageRecorderInternal recorder
            , RemoteProcessManager manager
            ,Myspace vos
            ,List strategies
			,SendToMenu sendTo
			,Jobs jobs
			,WorkflowBuilder transcriptViewer
			
    )
    throws HeadlessException {
        super(conf, hs, ui);
        this.manager = manager;
        this.recorder = recorder;
        
        results = new ResultsList(sendTo,vos,this);
        getHelpServer().enableHelp(results,"lookout.resultsTable");
        refreshAction = new RefreshAction(strategies);
        closeAction = new CloseAction();
        deleteAction = new DeleteAction();
        haltAction = new HaltAction();
        markAllReadAction= new MarkAllReadAction();
        transcriptAction = new ViewTranscriptAction(jobs,transcriptViewer);
        initialize();
    }
   
    public void refresh() {
        refreshAction.actionPerformed(null); // should make the system refresh.            
    }

    private Folder getCurrentFolder() {
        return currentFolder;
    }
    private JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new JMenuBar();
            jJMenuBar.add(getFileMenu());
            jJMenuBar.add(getManageMenu());
            jJMenuBar.add(Box.createHorizontalGlue());
            jJMenuBar.add(createHelpMenu());
        }
        return jJMenuBar;
    }

    private JMenu getFileMenu() {
    	if (fileMenu == null) {
    		fileMenu = new JMenu();
    		fileMenu.setText("File");
    		fileMenu.setMnemonic(KeyEvent.VK_F);
    		fileMenu.add(closeAction);
    	}
    	return fileMenu;
    }
    
    private JMenu getManageMenu() {
        if (manageMenu == null) {
            manageMenu = new JMenu();
            manageMenu.setText("Manage");
            manageMenu.setMnemonic(KeyEvent.VK_M);
            manageMenu.add(refreshAction);
            manageMenu.add(transcriptAction);
            manageMenu.add(haltAction);
            manageMenu.add(deleteAction);   
            manageMenu.add(new JSeparator());                        
            manageMenu.add(markAllReadAction);
        }
        return manageMenu;        
    }
    
    private MessageDisplayPane getMessageContentPane() {
        if (contentPane == null) {
            contentPane = new MessageDisplayPane();
        }
        return contentPane;
    }
    
    private JToolBar getToolbar() {
        if (toolbar == null) {
            toolbar = new JToolBar();
            toolbar.setFloatable(false);
            toolbar.setRollover(true);
            toolbar.add(refreshAction);
            toolbar.add(transcriptAction);
            toolbar.add(haltAction);
            toolbar.add(deleteAction);
            toolbar.add(new JToolBar.Separator());
            toolbar.add(markAllReadAction);
        }
        return toolbar;
    }
    private void initialize() {
        getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.lookout");
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setJMenuBar(getJJMenuBar());
        JPanel pane = getMainPanel();   
        this.setTitle("VO Lookout");
 
        JSplitPane leftRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        leftRight.setDividerSize(5);
        leftRight.setDividerLocation(200);
        leftRight.setTopComponent(new JScrollPane(getFolderTree()));
        JSplitPane topBottom = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        leftRight.setBottomComponent(topBottom);
        
        JScrollPane tableScrollPane = new JScrollPane(getMessageTable());
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        topBottom.setTopComponent(tableScrollPane);
        topBottom.setBottomComponent(getMessageDetails());
        topBottom.setDividerSize(5);
        topBottom.setDividerLocation(300);
        pane.add(getToolbar(),BorderLayout.NORTH);
        pane.add(leftRight,BorderLayout.CENTER);
        this.setContentPane(pane);
        this.setSize(565,800);
        setIconImage(IconHelper.loadIcon("thread_and_monitor_view.gif").getImage()); 
    }
    
    private void setCurrentFolder(Folder f) {
        this.currentFolder = f;
    }
    
    JTree getFolderTree() {
        if (folderTree == null) {                        
            folderTree = new JTree(recorder.getFolderList());
            ToolTipManager.sharedInstance().registerComponent(folderTree);
            getHelpServer().enableHelp(folderTree,"lo.folderTree");
            folderTree.putClientProperty("JTree.lineStyle", "None");            
            folderTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            folderTree.setShowsRootHandles(false);
            folderTree.setCellRenderer(new FolderTreeCellRenderer());
            folderTree.addTreeSelectionListener(new TreeSelectionListener() {            	           
                // if I _knew_ that listeners were called on order of addition, I could optimize this.
                public void valueChanged(TreeSelectionEvent e) {     	                	
                    Folder f = (Folder)folderTree.getLastSelectedPathComponent();
                    if (f != null && folderTree.getModel().isLeaf(f)) {
                        getMessageTable().clear();
                        setCurrentFolder(f);
                        try {
                            recorder.displayMessages(f);
                            int last = getMessageTable().getRowCount() -1;
                            getMessageTable().getSelectionModel().setSelectionInterval(last,last);
                            transcriptAction.setEnabled(f.getInformation().getId().getScheme().equals("jes"));
                              } catch (IOException e1) {
                            showError("Failed to display folder",e1);
                        }
                    }
                }
            });
            // when things get added, make sure they're displayed         
            folderTree.getModel().addTreeModelListener(new TreeModelListener() {            
                public void treeNodesChanged(TreeModelEvent e) {
                }
                public void treeNodesInserted(TreeModelEvent e) {   
                    folderTree.expandPath(e.getTreePath());                    
                }
                public void treeNodesRemoved(TreeModelEvent e) {
                }
                public void treeStructureChanged(TreeModelEvent e) {
                }
            });
        }
        return folderTree;
    }
    
    JPanel getMessageDetails() {
        if (messageDetails == null) {
            messageDetails = new JPanel(new BorderLayout());
            messageDetails.add(new JScrollPane(getMessageContentPane()),BorderLayout.CENTER);
            messageDetails.add(results,BorderLayout.SOUTH);
        }
        return messageDetails;
    }        
    
    MessageTable getMessageTable() {
        if (messageTable == null) {
            messageTable = new MessageTable(recorder);       
            getHelpServer().enableHelp(messageTable,"lo.messageTable");       
            messageTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {            	
                public void valueChanged(ListSelectionEvent e) {
                    int index = messageTable.getSelectedRow();
                    if (index >= messageTable.getRowCount()) {
                    	// ignore
                    	return;
                    }
                    if ( index < 0 ) {// selection cleared
                        // clear all displays message displays.
                        results.clear();
                        getMessageContentPane().clear();                    	
                    }                        
                    try {
                        MessageContainer m = recorder.getMessage(index);
                        if (m == null) {
                            // must be trying to get a message that's not there. no matter.
                            return;
                        }
                        getMessageContentPane().setMessage(m);
                        if (m.getMessage() instanceof ResultsExecutionMessage) {
                        	results.setResults((ResultsExecutionMessage)m.getMessage());
                        } else {                       	
                            results.clear();
                        }
                        if (m.isUnread()) {
                            // mark as read.
                            m.setUnread(false);
                            recorder.updateMessage(m);
                            Folder f= getCurrentFolder();
                            f.setUnreadCount(f.getUnreadCount()-1);
                            recorder.updateFolder(f);
                        }
                    } catch (IOException ex) {
                        showError("Failed to display message",ex);
                    }
                }
            });  
        }
        return messageTable;
    }
    boolean isRunning(String status) {
        return ! (status.equals(ExecutionInformation.ERROR) || status.equals(ExecutionInformation.COMPLETED));
    }
    
    boolean isTaskFolder(URI uri) {
        return ! ( uri.equals(MessageRecorderImpl.JOBS)
                || uri.equals(MessageRecorderImpl.QUERIES)
                || uri.equals(MessageRecorderImpl.ROOT)
                || uri.equals(MessageRecorderImpl.TASKS)
        );
    }
    
    
}


/* 
 
$Log: LookoutImpl.java,v $
Revision 1.14  2006/06/27 10:35:11  nw
refactored into manageable chunks.added send-to menu

Revision 1.13  2006/04/21 13:48:11  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.12  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.11.2.3  2006/04/18 18:49:03  nw
version to merge back into head.

Revision 1.11.2.2  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.11.2.1  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.11  2006/02/24 13:20:41  pjn3
Menu changes

Revision 1.10  2006/02/02 14:50:23  nw
improved results viewing.

Revision 1.9  2006/01/13 14:25:05  pjn3
pjn_workbench_20_12_05 merge

Revision 1.8.4.8  2006/01/13 11:38:54  pjn3
menu tweak

Revision 1.8.4.7  2006/01/11 16:30:24  pjn3
Transcripts loading

Revision 1.8.4.6  2006/01/11 12:20:26  pjn3
loading transcript should work

Revision 1.8.4.5  2006/01/11 11:28:26  pjn3
Tweak to prevent user hiding all messages with no way back

Revision 1.8.4.4  2006/01/10 17:07:04  pjn3
*** empty log message ***

Revision 1.8.4.3  2006/01/10 16:23:11  pjn3
message level popup menu added

Revision 1.8.4.2  2006/01/09 16:20:26  pjn3
add viewTranscriptAction

Revision 1.8.4.1  2005/12/22 12:11:08  pjn3
Removed view in browser button, only allow workflow to be viewed

Revision 1.8  2005/12/13 15:08:43  pjn3
Merge of pjn_workbench_8_12_05

Revision 1.7.2.2  2005/12/13 14:02:50  pjn3
Enable transcript viewer to be launched from Lookout

Revision 1.7.2.1  2005/12/12 18:01:04  pjn3
Initial work to include transaction viewer

Revision 1.7  2005/12/02 13:40:56  nw
minor change

Revision 1.6  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.5.2.2  2005/11/23 18:08:43  nw
code reviewed and tuned

Revision 1.5.2.1  2005/11/23 04:56:29  nw
added checks for nulls.

Revision 1.5  2005/11/11 18:39:40  nw
2 final tweaks

Revision 1.4  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.3  2005/11/10 16:28:26  nw
added result display to vo lookout.

Revision 1.2  2005/11/10 12:06:18  nw
early draft of volookout

Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/