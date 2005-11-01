/*$Id: LookoutImpl.java,v 1.1 2005/11/01 09:19:46 nw Exp $
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

import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.Jobs;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.ApplicationLauncher;
import org.astrogrid.acr.ui.ParameterizedWorkflowLauncher;
import org.astrogrid.acr.ui.WorkflowBuilder;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.background.MessageRecorder;
import org.astrogrid.desktop.modules.background.MessageRecorderImpl;
import org.astrogrid.desktop.modules.background.MessageRecorder.Folder;
import org.astrogrid.desktop.modules.background.MessageRecorder.Message;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Oct-2005
 *@todo style result messages.
 *
 *   
 *
 */
public class LookoutImpl extends UIComponent implements  Lookout {
    
    /** delete an alert message, or a task folder
     * listens to both tree and list to work out whether enabled or not.
     */
    private final class DeleteAction extends AbstractAction implements TreeSelectionListener, ListSelectionListener {

        public DeleteAction() {
            super("Delete", IconHelper.loadIcon("delete_obj.gif"));
            this.putValue(SHORT_DESCRIPTION,"Delete a task record, or event message");
            this.setEnabled(false);            
        }

        public void actionPerformed(ActionEvent e) {   
            try {
            if (folderMode) { // delete a task            
                Folder f = (Folder)getFolderTree().getLastSelectedPathComponent();
                recorder.deleteFolder(f);
                //@todo remove from cea server in a background thread.
            } else { // delete an alert message
                int row = getMessageTable().getSelectedRow();                
                recorder.deleteMessage(row);
            }
            } catch (IOException ex) {
                showError("Failed to delete",ex);
            }
        }

        public void valueChanged(ListSelectionEvent e) {
            folderMode=false;
            int index = getMessageTable().getSelectedRow();
            Folder f = (Folder)getFolderTree().getLastSelectedPathComponent();
            setEnabled(f != null && f.getUserObject() instanceof MessageRecorderImpl.AlertFolderSummary
                    && index > 0 &&  index < getMessageTable().getRowCount()) ;            
        }

        public void valueChanged(TreeSelectionEvent e) {
            folderMode = true;
            Folder f = (Folder)getFolderTree().getLastSelectedPathComponent();
            setEnabled(f != null &&  f.getUserObject() instanceof MessageRecorderImpl.TaskFolderSummary);             
        }
        private boolean folderMode;
    }
    
    /** action for halting something. listens to current tree selection to determine whether enabled or not */
    private final class HaltAction extends AbstractAction implements TreeSelectionListener{
        public HaltAction() {
            super("Halt", IconHelper.loadIcon("stop.gif"));
            this.putValue(SHORT_DESCRIPTION,"Halt the execution of a task or job");
            this.setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
                final Folder f = (Folder)getFolderTree().getLastSelectedPathComponent();                
                (new BackgroundOperation("Cancellig Application") {
                    protected Object construct() throws Exception {
                        URI id = new URI(f.getKey());
                        apps.cancel(id);
                        return null;
                    }                    
                }).start();                
            }

        public void valueChanged(TreeSelectionEvent e) {
            Folder f = (Folder)getFolderTree().getLastSelectedPathComponent();
            // @todo extend to jobs
            setEnabled(f != null &&  f.getUserObject() instanceof MessageRecorderImpl.TaskFolderSummary); 
        }        
    }
    
    private final class ParameterizedWorkflowAction extends AbstractAction {
        public ParameterizedWorkflowAction() {
            super("Launch a canned workflow",IconHelper.loadIcon("run_tool.gif"));
            this.putValue(SHORT_DESCRIPTION,"Launch a parameterized workflow");
            this.setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
            pwLauncher.run();
        }
    }    

    private final class SubmitTaskAction extends AbstractAction {
        public SubmitTaskAction() {
            super("Submit Task",IconHelper.loadIcon("file_obj.gif"));
            this.putValue(SHORT_DESCRIPTION,"Submit a saved task document for execution");
            this.setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
            final URI u =  chooser.chooseResourceWithParent("Select workflow to submit",true, true, true,LookoutImpl.this);
            if (u == null) {
                return;
            }                
            (new BackgroundOperation("Submitting Workflow") {

                protected Object construct() throws Exception {
                    return apps.submitStored(u);
                }
            }).start();
        }
    }

    private final class SubmitWorkflowAction extends AbstractAction {
        public SubmitWorkflowAction() {
            super("Submit Workflow",IconHelper.loadIcon("file_obj.gif"));
            this.putValue(SHORT_DESCRIPTION,"Submit a saved workflow document for execution");
            this.setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
                final URI u =  chooser.chooseResourceWithParent("Select workflow to submit",true, true, true,LookoutImpl.this);
                if (u == null) {
                    return;
                }                
                (new BackgroundOperation("Submitting Workflow") {
                    protected Object construct() throws Exception {
                        return jobs.submitStoredJob(u);
                    }

                }).start();
            }        
    }
    
    private final class TaskEditorAction extends AbstractAction {
        public TaskEditorAction() {
            super("Open task editor",IconHelper.loadIcon("thread_view.gif"));
            this.putValue(SHORT_DESCRIPTION,"Create and execute a stand-alone query or task");
            this.setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
            appLauncher.show();
        }
    }
    
    private final class WorkflowEditorAction extends AbstractAction {
        public WorkflowEditorAction() {
            super("Open workflow editor",IconHelper.loadIcon("wf_small.gif"));
            this.putValue(SHORT_DESCRIPTION,"Create and execute a workflow");
            this.setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
            workflowLauncher.show();
        }
    }
    final ResourceChooserInternal chooser;
    final Jobs jobs;

    final MessageRecorder recorder;
    final Applications apps;
    final MyspaceInternal vos;
    final ParameterizedWorkflowLauncher pwLauncher;
    final ApplicationLauncher appLauncher;
    final WorkflowBuilder workflowLauncher;
    private MessageDisplayPane contentPane;
    private DeleteAction deleteAction;
    private JTree folderTree;
    private HaltAction haltAction;
    private JMenuBar jJMenuBar;
    private JMenu manageMenu;
    private JTable messageTable;
    
    private JMenu newMenu;
    private Action parameteriedWorkflowAction;
    
    private Action submitTaskAction;
    private Action submitWorkflowAction;
    private Action taskEditorAction;
    
    private JToolBar toolbar;
    private Action workflowEditorAction;
    
    
    /** Construct a new Lookout
     * @param conf
     * @param hs
     * @param ui
     * @throws HeadlessException
     */
    public LookoutImpl(Configuration conf, HelpServerInternal hs, UIInternal ui
            , MessageRecorder recorder, ResourceChooserInternal chooser
            ,Jobs jobs, MyspaceInternal vos, ParameterizedWorkflowLauncher pw
            ,WorkflowBuilder workflows, ApplicationLauncher appLauncher, Applications apps
            )
            throws HeadlessException {
        super(conf, hs, ui);
        this.recorder = recorder;
        this.chooser = chooser;
        this.vos = vos;
        this.jobs = jobs;
        this.pwLauncher = pw;
        this.workflowLauncher = workflows;
        this.appLauncher = appLauncher;
        this.apps = apps;
        initialize();
    }

 
    private DeleteAction getDeleteAction() {
        if (deleteAction == null) {
            deleteAction = new DeleteAction();
            getFolderTree().addTreeSelectionListener(deleteAction);
            getMessageTable().getSelectionModel().addListSelectionListener(deleteAction);
        }
        return deleteAction;
    }
    private JTree getFolderTree() {
        if (folderTree == null) {            
            folderTree = new JTree(recorder.getFolderList());
            folderTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);                    
            folderTree.addTreeSelectionListener(new TreeSelectionListener() {
                
                public void valueChanged(TreeSelectionEvent e) {
                    Folder f = (Folder)folderTree.getLastSelectedPathComponent();
                    if (f != null && folderTree.getModel().isLeaf(f)) {
                        try {
                            recorder.displayMessages(f);
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
        getHelpServer().enableHelp(folderTree,"lo.folderTree");
        return folderTree;
        
    }
    private HaltAction getHaltAction() {
        if (haltAction == null) {
            haltAction = new HaltAction();
            getFolderTree().addTreeSelectionListener(haltAction);
        }
        return haltAction;
    }
    private JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new JMenuBar();
            jJMenuBar.add(getNewMenu());
            jJMenuBar.add(getManageMenu());
        }
        return jJMenuBar;
    }
    private JMenu getManageMenu() {
        if (manageMenu == null) {
            manageMenu = new JMenu();
            manageMenu.setText("Manage");
            manageMenu.add(getHaltAction());
            manageMenu.add(getDeleteAction());            
        }
        return manageMenu;
        
    }
    private MessageDisplayPane getMessageContentPane() {
        if (contentPane == null) {
            contentPane = new MessageDisplayPane();

        }
        return contentPane;
    }
    
    private class MessageDisplayPane extends JEditorPane {
        {
            setEditable(false);
            setContentType("text/html");
        }
        public void setMessage(Message m) {
            setText(fmt(m));
            setCaretPosition(0);
        }
        private String fmt(Message m) {
            StringBuffer sb = new StringBuffer();
            sb.append("<html><p bgcolor='#CCDDEE'>")
                .append("<b>Subject:</b> ").append(m.getSummary()).append("<br>")
                .append("<b>Date: </b> " ).append(m.getTimestamp()).append("<br></p>")
                .append("<pre>").append(m.getText())
                .append("</pre></html>");
          return sb.toString();
        }
    }
    
    private JTable getMessageTable() {
        if (messageTable == null) {
            messageTable = new JTable(recorder.getMessageList());
            messageTable.setShowVerticalLines(false);
            messageTable.setShowHorizontalLines(false);
            messageTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            messageTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                int previous = -1;
                public void valueChanged(ListSelectionEvent e) {
                    int index = messageTable.getSelectedRow();
                    if (index == previous || index < 0 || index >= messageTable.getRowCount()) {
                        return;
                    }
                    previous = index;
                    try {
                        Message m = recorder.getMessage(index);
                        getMessageContentPane().setMessage(m);
                    } catch (IOException ex) {
                        showError("Failed to display message",ex);
                    }
                }
            });
            messageTable.getTableHeader().setReorderingAllowed(false);
            messageTable.getColumnModel().getColumn(1).setPreferredWidth(100);
            getHelpServer().enableHelp(messageTable,"lo.messageTable");
            
        }
        return messageTable;
    }
    private JMenu getNewMenu() {
        if (newMenu == null) {
            newMenu = new JMenu();
            newMenu.setText("New");
            newMenu.add(getParameterizedWorkflowAction());
            newMenu.add(getSubmitWorkflowAction());
            newMenu.add(getWorkflowEditorAction());
            newMenu.add(new JSeparator());
            newMenu.add(getSubmitTaskAction());
            newMenu.add(getTaskEditorAction());            
        }
        return newMenu;
    }
    private Action getParameterizedWorkflowAction() {
        if (parameteriedWorkflowAction == null) {
            parameteriedWorkflowAction = new ParameterizedWorkflowAction();
        }
        return parameteriedWorkflowAction;
    }
    private Action getSubmitTaskAction() {
        if (submitTaskAction == null) {
            submitTaskAction = new SubmitTaskAction();
        }
        return submitTaskAction;
    }
    private Action getSubmitWorkflowAction() {
        if (submitWorkflowAction == null) {
            submitWorkflowAction = new SubmitWorkflowAction();
        }
        return submitWorkflowAction;
    }
    private Action getTaskEditorAction() {
        if (taskEditorAction == null) {
            taskEditorAction = new TaskEditorAction();
        }
        return taskEditorAction;
    }
    private JToolBar getToolbar() {
        if (toolbar == null) {
            toolbar = new JToolBar();
            toolbar.setFloatable(false);
            toolbar.setRollover(true);
            toolbar.add(getParameterizedWorkflowAction());
            toolbar.add(getSubmitWorkflowAction());
            toolbar.add(getWorkflowEditorAction());
            toolbar.add(new JToolBar.Separator());
            toolbar.add(getSubmitTaskAction());
            toolbar.add(getTaskEditorAction());
            toolbar.add(new JToolBar.Separator());
            toolbar.add(getHaltAction());
            toolbar.add(getDeleteAction());
        }
        return toolbar;
    }
    private Action getWorkflowEditorAction() {
        if (workflowEditorAction == null) {
            workflowEditorAction = new WorkflowEditorAction();
        }
        return workflowEditorAction;        
    }
    
    
    private void initialize() {
        getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.lookout");
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(ui.getComponent());
        this.setJMenuBar(getJJMenuBar());
        this.setSize(600, 500);
        JPanel pane = getJContentPane();    
        this.setTitle("VO Lookout");
        
      
        
        JSplitPane leftRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        leftRight.setDividerSize(5);
        leftRight.setDividerLocation(200);
        leftRight.setTopComponent(new JScrollPane(getFolderTree()));
        JSplitPane topBottom = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        leftRight.setBottomComponent(topBottom);
        
        topBottom.setTopComponent(new JScrollPane(getMessageTable()));
        topBottom.setBottomComponent(new JScrollPane(getMessageContentPane()));
        topBottom.setDividerSize(5);
        topBottom.setDividerLocation(100);
        pane.add(getToolbar(),BorderLayout.NORTH);
        pane.add(leftRight,BorderLayout.CENTER);
        this.setContentPane(pane);
    }

}


/* 
$Log: LookoutImpl.java,v $
Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/