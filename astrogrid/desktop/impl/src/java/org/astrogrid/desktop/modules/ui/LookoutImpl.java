/*$Id: LookoutImpl.java,v 1.3 2005/11/10 16:28:26 nw Exp $
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

import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.acr.astrogrid.RemoteProcessManager;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.ApplicationLauncher;
import org.astrogrid.acr.ui.ParameterizedWorkflowLauncher;
import org.astrogrid.acr.ui.WorkflowBuilder;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.MessageRecorderImpl;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal.Folder;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal.MessageContainer;
import org.astrogrid.desktop.modules.ag.recorder.ResultsExecutionMessage;
import org.astrogrid.desktop.modules.background.JesStrategyInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.system.transformers.Votable2XhtmlTransformer;
import org.astrogrid.desktop.modules.system.transformers.WorkflowResultTransformerSet;
import org.astrogrid.desktop.modules.system.transformers.Xml2XhtmlTransformer;
import org.astrogrid.io.Piper;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Oct-2005
 *
 *   
 *
 */
public class LookoutImpl extends UIComponent implements  Lookout {
    
    private final class RefreshAction extends AbstractAction {
        public RefreshAction() {
            super("Refresh",IconHelper.loadIcon("update.gif"));
            this.putValue(SHORT_DESCRIPTION,"Check for new events and messages now");            
        }
        public void actionPerformed(ActionEvent e) {
            jesStrategy.triggerUpdate();
            LookoutImpl.this.setStatusMessage("Refreshing..");
        }
    }
    
    private final class MarkAllReadAction extends AbstractAction implements TreeSelectionListener {
        public MarkAllReadAction() {
            super("Mark all Read", IconHelper.loadIcon("complete_status.gif"));
            this.putValue(SHORT_DESCRIPTION,"Mark all messages from this process as read");
            this.setEnabled(false);
        }
        public void actionPerformed(ActionEvent e) {
            try {
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
            } catch (IOException ex) {
                showError("Failed to mark all as read",ex);
            }
        }
        public void valueChanged(TreeSelectionEvent e) {
            //@todo - unsure whether it's safe to optimze this by calling 'getCurrentFolder()' - race condition..
            Folder f = (Folder)getFolderTree().getLastSelectedPathComponent();
            setEnabled(f != null && isTaskFolder(f.getInformation().getId()));
        }
    }
    
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
                Folder f = getCurrentFolder();
                //@todo should I do this in a background thread?
                // considering remote process, probably should.
                // but then, what about table update? aghh. leave as is for now.
                // should probably halt first, if still running.
                if (isRunning(f.getInformation().getStatus())) {
                    try {
                        manager.halt(f.getInformation().getId());
                    } catch (Exception ex) {
                        //@todo warn someone.
                    }
                }
                manager.delete(f.getInformation().getId());
            } else { // delete an alert message
                int row = getMessageTable().getSelectedRow();                
                recorder.deleteMessage(row);
            }
            } catch (Exception ex) {
                showError("Failed to delete",ex);
            }
        }

        public void valueChanged(ListSelectionEvent e) {
            folderMode=false;
            int index = getMessageTable().getSelectedRow();           
            Folder f = getCurrentFolder();
            setEnabled(f != null && f.getInformation().getId().equals(MessageRecorderImpl.ALERTS)
                    && index > 0 &&  index < getMessageTable().getRowCount()) ;            
        }

        public void valueChanged(TreeSelectionEvent e) {
            folderMode = true;
            // can't optimize this - race condition.
            Folder f = (Folder)getFolderTree().getLastSelectedPathComponent();            
            setEnabled(f != null &&  isTaskFolder(f.getInformation().getId()));             
        }

        private boolean folderMode;
    }

    private boolean isTaskFolder(URI uri) {
        return ! (uri.equals(MessageRecorderImpl.ALERTS) 
           || uri.equals(MessageRecorderImpl.JOBS)
           || uri.equals(MessageRecorderImpl.QUERIES)
           || uri.equals(MessageRecorderImpl.ROOT)
           || uri.equals(MessageRecorderImpl.TASKS)
           );
    }
    
    private boolean isRunning(String status) {
        return ! (status.equals(ExecutionInformation.ERROR) || status.equals(ExecutionInformation.COMPLETED));
    }        
    
    /** action for halting something. listens to current tree selection to determine whether enabled or not */
    private final class HaltAction extends AbstractAction implements TreeSelectionListener{
        public HaltAction() {
            super("Halt", IconHelper.loadIcon("stop.gif"));
            this.putValue(SHORT_DESCRIPTION,"Halt the execution of a task or job");
            this.setEnabled(false);
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
            this.putValue(SHORT_DESCRIPTION,"Submit a saved task or workflow for execution");
            this.setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
            final URI u =  chooser.chooseResourceWithParent("Select document to execute",true, true, true,LookoutImpl.this);
            if (u == null) {
                return;
            }                
            (new BackgroundOperation("Submitting Document") {

                protected Object construct() throws Exception {
                    return manager.submitStored(u);          
                }
                protected void doFinished(Object result) {
                    refresh();
                }
            }).start();
        }
    }
 
        public void refresh() {

            getRefreshAction().actionPerformed(null); // should make the system refresh.            
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
    final JesStrategyInternal jesStrategy;
    final MessageRecorderInternal recorder;
    final MyspaceInternal vos;
    final ParameterizedWorkflowLauncher pwLauncher;
    final ApplicationLauncher appLauncher;
    final WorkflowBuilder workflowLauncher;
    final RemoteProcessManager manager;
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
    private Action taskEditorAction;
    
    private JToolBar toolbar;
    private Action workflowEditorAction;
    private final BrowserControl browser;
    
    /** Construct a new Lookout
     * @param conf
     * @param hs
     * @param ui
     * @throws HeadlessException
     */
    public LookoutImpl(Configuration conf, HelpServerInternal hs, UIInternal ui
            , MessageRecorderInternal recorder, ResourceChooserInternal chooser
             ,MyspaceInternal vos, ParameterizedWorkflowLauncher pw
            ,WorkflowBuilder workflows, ApplicationLauncher appLauncher
            , RemoteProcessManager manager
            , Community comm
            ,JesStrategyInternal jesStrategy
            , BrowserControl browser
            )
            throws HeadlessException {
        super(conf, hs, ui);
        this.browser = browser;
        this.jesStrategy = jesStrategy;
        this.manager = manager;
        this.recorder = recorder;
        this.chooser = chooser;
        this.vos = vos;
        this.pwLauncher = pw;
        this.workflowLauncher = workflows;
        this.appLauncher = appLauncher;
        // force community visible.
        comm.getUserInformation();
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
    
    private RefreshAction refreshAction;
    private RefreshAction getRefreshAction() {
        if (refreshAction == null) {
            refreshAction = new RefreshAction();
        }
        return refreshAction;
    }
    
    private String calcColour(String status) {
        if ("ERROR".equalsIgnoreCase(status)) {
            return "red";
        } else if ("RUNNING".equalsIgnoreCase(status)) {
            return "green";
        } else if ("PENDING".equalsIgnoreCase(status) || "INITIALIZING".equalsIgnoreCase(status)) {
            return "blue";
        } else {
            return "black";
        }
    }
    
    private final DateFormat df = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT,SimpleDateFormat.SHORT);
    private JTree getFolderTree() {
        if (folderTree == null) {                        
            folderTree = new JTree(recorder.getFolderList());
            ToolTipManager.sharedInstance().registerComponent(folderTree);
            getHelpServer().enableHelp(folderTree,"lo.folderTree");
            folderTree.putClientProperty("JTree.lineStyle", "None");            
            folderTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            folderTree.setShowsRootHandles(false);
            folderTree.addTreeSelectionListener(new TreeSelectionListener() {
                final JPanel p = getMessageDetails();
                final CardLayout c = (CardLayout)p.getLayout();                
                // if I _knew_ that listeners were called on order of addition, I could optimize this.
                public void valueChanged(TreeSelectionEvent e) {
                    Folder f = (Folder)folderTree.getLastSelectedPathComponent();
                    if (f != null && folderTree.getModel().isLeaf(f)) {
                        setCurrentFolder(f);
                        try {
                            getMessageTable().clearSelection();
                            getMessageContentPane().clear();
                            c.show(p,MESSAGE_CONTENT);
                            getResultsTableModel().clear();
                            recorder.displayMessages(f);
                        } catch (IOException e1) {
                            showError("Failed to display folder",e1);
                        }
                    }
                }
            });
            TreeCellRenderer renderer = new DefaultTreeCellRenderer() {
  
                public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                    super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                    Folder f= (Folder)value;
                    ExecutionInformation info = f.getInformation();
                    StringBuffer sb = new StringBuffer();
                    sb.append("<html><p>").append(info.getDescription()).append("<br>").append(info.getId()).append("<br>");
                    if (info.getStartTime() != null) {
                        sb.append(df.format(info.getStartTime()));                        
                    }
                    if (info.getFinishTime() != null) {
                        sb.append(" - ").append(df.format(info.getFinishTime()));
                    }
                    sb.append("</p></html>");
                    setToolTipText(sb.toString());
                    sb = new StringBuffer();
                    sb.append("<html><font color='");
                    sb.append(calcColour(info.getStatus()));
                    sb.append("'>");
                    if (f.getUnreadCount() > 0) {
                        sb.append("<b>");
                    }
                    sb.append(info.getName());                    
                    if (f.getUnreadCount() > 0) {
                        sb.append(" ");
                        sb.append(f.getUnreadCount());
                        sb.append("</b>");
                    }                    
                    sb.append("</font></html>");
                    setText(sb.toString());
                    // finally set the icon.
                    if (info.getId().equals(MessageRecorderImpl.ALERTS)) {
                        setIcon(IconHelper.loadIcon("info_obj.gif"));
                    } else if (info.getId().equals(MessageRecorderImpl.JOBS)) {
                        setIcon(IconHelper.loadIcon("wf_small.gif"));
                    } else if (info.getId().equals(MessageRecorderImpl.QUERIES)) {
                        setIcon(IconHelper.loadIcon("search.gif"));
                    } else if (info.getId().equals(MessageRecorderImpl.TASKS)) {
                        setIcon(IconHelper.loadIcon("exec.png"));    
                    } else if (info.getId().equals(MessageRecorderImpl.ROOT)) {
                        setIcon(IconHelper.loadIcon("package_network.png"));
                    } else {
                        setIcon(IconHelper.loadIcon("thread_view.gif"));
                    }
                    return this;
                }
                
            };
            folderTree.setCellRenderer(renderer);
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
    
    private Folder currentFolder;
    private void setCurrentFolder(Folder f) {
        this.currentFolder = f;
    }
    private Folder getCurrentFolder() {
        return currentFolder;
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
            manageMenu.add(getRefreshAction());
            manageMenu.add(getHaltAction());
            manageMenu.add(getDeleteAction());   
            newMenu.add(new JSeparator());                        
            manageMenu.add(getMarkAllReadAction());
        }
        return manageMenu;
        
    }
    private MessageDisplayPane getMessageContentPane() {
        if (contentPane == null) {
            contentPane = new MessageDisplayPane();

        }
        return contentPane;
    }
    
    private class MessageDisplayPane extends JTextPane {
        public MessageDisplayPane() {
            setContentType("text/html");
            setEditable(false);
        }
        
        public void setMessage(MessageContainer m) {
           setText(fmt(m));
            setCaretPosition(0);
        }
        
        public void clear() {       
            setText("");
        }
        
        private String fmt(MessageContainer m) {
            StringBuffer sb = new StringBuffer();
            ExecutionMessage message = m.getMessage();
            sb.append("<html><p bgcolor='#CCDDEE'>")
                .append("<b>Subject:</b> ").append(m.getSummary()).append("<br>")
                .append("<b>Date: </b> " ).append(message.getTimestamp()).append("<br>")
                .append("<b>From: </b> ").append(message.getSource()).append("<br></p><tt>")
                .append( //todo - work out how to preserve space indentation here..
                                StringUtils.replace(
                                        StringEscapeUtils.escapeHtml(message.getContent())
                                ,"\n"
                                ,"<br>"
                                )
                        )
                .append("</tt></html>");
          return sb.toString();
        }
    }
    
    private JTable getMessageTable() {
        if (messageTable == null) {
            messageTable = new JTable(recorder.getMessageList());
            getHelpServer().enableHelp(messageTable,"lo.messageTable");
            messageTable.setShowVerticalLines(false);
            messageTable.setShowHorizontalLines(false);
            messageTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            messageTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                int previous = -1;
                final JPanel p = getMessageDetails();
                final CardLayout c = (CardLayout)p.getLayout();
                public void valueChanged(ListSelectionEvent e) {
                    int index = messageTable.getSelectedRow();
                    if (index == previous || index < 0 || index >= messageTable.getRowCount()) {
                        return;
                    }
                    previous = index;
                    try {
                        MessageContainer m = recorder.getMessage(index);
                        getMessageContentPane().setMessage(m);
                        if (m.getMessage() instanceof ResultsExecutionMessage) {
                            getResultsTableModel().setResults(((ResultsExecutionMessage)m.getMessage()).getResults());
                            c.show(p,MESSAGE_RESULTS);
                        } else {
                            c.show(p, MESSAGE_CONTENT);
                            getResultsTableModel().clear();
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

            messageTable.getTableHeader().setReorderingAllowed(false);
            TableColumn titleColumn = messageTable.getColumnModel().getColumn(0);
            titleColumn.setPreferredWidth(150);
            titleColumn.setCellRenderer(new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
                    if (recorder.getMessage(row).isUnread()) { 
                        setText("<html><b>" + getText() + "</b></html>");
                    }
                    return this;
                }               
            });
            //@todo - add a consideration of message log level
            
        }
        return messageTable;
    }
    private JMenu getNewMenu() {
        if (newMenu == null) {
            newMenu = new JMenu();
            newMenu.setText("New");
            newMenu.add(getParameterizedWorkflowAction());
            newMenu.add(getWorkflowEditorAction());
            newMenu.add(getTaskEditorAction());            
            newMenu.add(new JSeparator());
            newMenu.add(getSubmitTaskAction());
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

    private Action getTaskEditorAction() {
        if (taskEditorAction == null) {
            taskEditorAction = new TaskEditorAction();
        }
        return taskEditorAction;
    }
    
    private MarkAllReadAction markAllReadAction;
    private Action getMarkAllReadAction() {
        if (markAllReadAction == null) {
            markAllReadAction = new MarkAllReadAction();
            getFolderTree().addTreeSelectionListener(markAllReadAction);
        }
        return markAllReadAction;
    }
    
    private JToolBar getToolbar() {
        if (toolbar == null) {
            toolbar = new JToolBar();
            toolbar.setFloatable(false);
            toolbar.setRollover(true);
            toolbar.add(getParameterizedWorkflowAction());
            toolbar.add(getWorkflowEditorAction());
            toolbar.add(getTaskEditorAction());
            toolbar.add(new JToolBar.Separator());
            toolbar.add(getSubmitTaskAction());
            toolbar.add(new JToolBar.Separator());
            toolbar.add(getRefreshAction());
            toolbar.add(getHaltAction());
            toolbar.add(getDeleteAction());
            toolbar.add(new JToolBar.Separator());
            toolbar.add(getMarkAllReadAction());
        }
        return toolbar;
    }
    private Action getWorkflowEditorAction() {
        if (workflowEditorAction == null) {
            workflowEditorAction = new WorkflowEditorAction();
        }
        return workflowEditorAction;        
    }
    
    private JPanel messageDetails;
    private JPanel getMessageDetails() {
        if (messageDetails == null) {
            messageDetails = new JPanel(new CardLayout());
            messageDetails.add(new JScrollPane(getMessageContentPane()), MESSAGE_CONTENT);
            messageDetails.add(new JScrollPane(getResultsTable()),MESSAGE_RESULTS);
        }
        return messageDetails;
    }
    private static final String MESSAGE_CONTENT = "message_content";
    private static final String MESSAGE_RESULTS = "message_results";
    private ResultListTableModel resultsTableModel;
    private ResultListTableModel getResultsTableModel() {
        if (resultsTableModel == null) {
            resultsTableModel = new ResultListTableModel();
        }
        return resultsTableModel;
    }
    
    private JTable resultsTable;
    private JTable getResultsTable() {
        if (resultsTable == null) {
            //cribbed from http://www.codeguru.com/java/articles/162.shtml
            resultsTable = new JTable(getResultsTableModel()) {
                public TableCellRenderer getCellRenderer(int row, int column) {
                    TableColumn tableColumn = getColumnModel().getColumn(column);
                    TableCellRenderer renderer = tableColumn.getCellRenderer();
                    if (renderer == null) {
                            Class c = getColumnClass(column);
                            if( c.equals(Object.class) )
                            {
                                    Object o = getValueAt(row,column);
                                    if( o != null )
                                            c = getValueAt(row,column).getClass();
                            }
                            renderer = getDefaultRenderer(c);
                    }
                    return renderer;
            }
            
            public TableCellEditor getCellEditor(int row, int column) {
                    TableColumn tableColumn = getColumnModel().getColumn(column);
                    TableCellEditor editor = tableColumn.getCellEditor();
                    if (editor == null) {
                            Class c = getColumnClass(column);
                            if( c.equals(Object.class) )
                            {
                                    Object o = getValueAt(row,column);
                                    if( o != null )
                                            c = getValueAt(row,column).getClass();
                            }
                            editor = getDefaultEditor(c);
                    }
                    return editor;
            }                
            };
            resultsTable.setDefaultRenderer(JComponent.class,new JComponentCellRenderer() );
            resultsTable.setDefaultEditor(JComponent.class,new JComponentCellEditor() );
            getHelpServer().enableHelp(resultsTable,"lookout.resultsTable");
            resultsTable.setShowVerticalLines(false);
            resultsTable.setShowHorizontalLines(false);
            resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  
            TableColumnModel columnModel = resultsTable.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(10);
            columnModel.getColumn(1).setPreferredWidth(40);
            columnModel.getColumn(3).setPreferredWidth(10);
            columnModel.getColumn(4).setPreferredWidth(10);            
        }
        return resultsTable;
    }

    class JComponentCellRenderer implements TableCellRenderer
    {
        public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
            return (JComponent)value;
        }
    }
        public class JComponentCellEditor implements TableCellEditor, TreeCellEditor,
        Serializable {
                
                protected EventListenerList listenerList = new EventListenerList();
                transient protected ChangeEvent changeEvent = null;
                
                protected JComponent editorComponent = null;
                protected JComponent container = null;          // Can be tree or table
                
                
                public Component getComponent() {
                        return editorComponent;
                }
                
                
                public Object getCellEditorValue() {
                        return editorComponent;
                }
                
                public boolean isCellEditable(EventObject anEvent) {
                        return true;
                }
                
                public boolean shouldSelectCell(EventObject anEvent) {
                        if( editorComponent != null && anEvent instanceof MouseEvent
                                && ((MouseEvent)anEvent).getID() == MouseEvent.MOUSE_PRESSED )
                        {
                    Component dispatchComponent = SwingUtilities.getDeepestComponentAt(editorComponent, 3, 3 );
                                MouseEvent e = (MouseEvent)anEvent;
                                MouseEvent e2 = new MouseEvent( dispatchComponent, MouseEvent.MOUSE_RELEASED,
                                        e.getWhen() + 100000, e.getModifiers(), 3, 3, e.getClickCount(),
                                        e.isPopupTrigger() );
                                dispatchComponent.dispatchEvent(e2); 
                                e2 = new MouseEvent( dispatchComponent, MouseEvent.MOUSE_CLICKED,
                                        e.getWhen() + 100001, e.getModifiers(), 3, 3, 1,
                                        e.isPopupTrigger() );
                                dispatchComponent.dispatchEvent(e2); 
                        }
                        return false;
                }
                
                public boolean stopCellEditing() {
                        fireEditingStopped();
                        return true;
                }
                
                public void cancelCellEditing() {
                        fireEditingCanceled();
                }
                
                public void addCellEditorListener(CellEditorListener l) {
                        listenerList.add(CellEditorListener.class, l);
                }
                
                public void removeCellEditorListener(CellEditorListener l) {
                        listenerList.remove(CellEditorListener.class, l);
                }
                
                protected void fireEditingStopped() {
                        Object[] listeners = listenerList.getListenerList();
                        // Process the listeners last to first, notifying
                        // those that are interested in this event
                        for (int i = listeners.length-2; i>=0; i-=2) {
                                if (listeners[i]==CellEditorListener.class) {
                                        // Lazily create the event:
                                        if (changeEvent == null)
                                                changeEvent = new ChangeEvent(this);
                                        ((CellEditorListener)listeners[i+1]).editingStopped(changeEvent);
                                }              
                        }
                }
                
                protected void fireEditingCanceled() {
                        // Guaranteed to return a non-null array
                        Object[] listeners = listenerList.getListenerList();
                        // Process the listeners last to first, notifying
                        // those that are interested in this event
                        for (int i = listeners.length-2; i>=0; i-=2) {
                                if (listeners[i]==CellEditorListener.class) {
                                        // Lazily create the event:
                                        if (changeEvent == null)
                                                changeEvent = new ChangeEvent(this);
                                        ((CellEditorListener)listeners[i+1]).editingCanceled(changeEvent);
                                }              
                        }
                }
                
                // implements javax.swing.tree.TreeCellEditor
                public Component getTreeCellEditorComponent(JTree tree, Object value,
                        boolean isSelected, boolean expanded, boolean leaf, int row) {
                        String         stringValue = tree.convertValueToText(value, isSelected,
                                expanded, leaf, row, false);
                        
                        editorComponent = (JComponent)value;
                        container = tree;
                        return editorComponent;
                }
                
                // implements javax.swing.table.TableCellEditor
                public Component getTableCellEditorComponent(JTable table, Object value,
                        boolean isSelected, int row, int column) {
                        
                        editorComponent = (JComponent)value;
                        container = table;
                        return editorComponent;
                }
                
        } // End of class JComponentCellEditor
        
    /** class for disoaying a table of resultls - also handles button presses, etc.*/
    private class ResultListTableModel extends AbstractTableModel implements ActionListener {
        public ResultListTableModel() {           
        }
        public void clear() {
            arr = new ParameterValue[]{};
            fireTableDataChanged();
        }
        public void setResults(ResultListType results) {
            arr = results.getResult();
            fireTableDataChanged();
        }
        //store for buttons
        private List saveButtonsStore = new ArrayList();
        // nifty list that creats buttons as needed.
        private List saveButtons = ListUtils.lazyList(saveButtonsStore, new Factory() {  
            public Object create() {
                JButton save = new JButton (IconHelper.loadIcon("fileexport.png"));
                save.setActionCommand(SAVE);
                save.setToolTipText("Save this result to myspace or local disk");
                save.addActionListener(ResultListTableModel.this);
                return save;
            }            
        });
        private final String VIEW = "VIEW";
        private final String SAVE = "SAVE";
        private List viewButtonsStore = new ArrayList();
        // dynamically creates buttons as needed.
        private List viewButtons = ListUtils.lazyList(viewButtonsStore, new Factory() {  
            public Object create() {
                JButton view = new JButton (IconHelper.loadIcon("read_obj.gif"));
                view.setActionCommand(VIEW);
                view.setToolTipText("View this result in a browser");
                view.addActionListener(ResultListTableModel.this);
                return view;
            }            
        });        
        
        private ParameterValue[] arr = new ParameterValue[]{};
        

        public void actionPerformed(ActionEvent e) {
            if (SAVE.equals(e.getActionCommand())) {
                // find the row.
                int row = saveButtonsStore.indexOf(e.getSource());
                if (row < 0  || row > arr.length -1) {
                    return;
                }
                final ParameterValue pv = arr[row];
                final URI u =  chooser.chooseResourceWithParent("Save result: " + pv.getName(),true, true, true,LookoutImpl.this);
                if (u == null) {
                    return;
                }                
                (new BackgroundOperation("Saving Result") {

                    protected Object construct() throws Exception {
                        Writer w=  null;
                        try {
                         w= new OutputStreamWriter(vos.getOutputStream(u)); // @todo could specify size here  
                         Piper.pipe(new StringReader(pv.getValue()),w);
                        } finally {
                            if (w != null) {
                                try {
                                w.close();
                                } catch (IOException e) {
                                    logger.warn("error closing write stream",e);
                                }
                            } 
                        }
                        return null;     
                    }
                }).start();
                
            } else if (VIEW.equals(e.getActionCommand())){
                int row = viewButtonsStore.indexOf(e.getSource());
                if (row < 0  || row > arr.length -1) {
                    return;
                }            
                final ParameterValue pv = arr[row];
                // determine how to style.
                (new BackgroundOperation("Displaying Result") {
                    protected Object construct() throws Exception {
                        URL url = displayResult(pv);
                        browser.openURL(url);
                        return null;
                    }                    
                }).start();
            }
        }        
        
        // table model methods.
        
        public Class getColumnClass(int column) {
            switch(column) {
                case 0:
                    return Boolean.class;
                 case 1:
                     return Object.class;
                  case 2:
                    return Object.class;
                  case 3:
                      return JComponent.class;
                  case 4:
                      return JComponent.class;
                  default:
                      return Object.class;
                  
            }
        }
        public String getColumnName(int column) {
            switch(column) {
                case 0:
                    return "Indirect?";
                case 1:
                    return "Name";
                case 2:
                    return "Value";
                case 3:
                    return ""; //view
                 case 4:
                     return ""; //save
                default:
                    return "";
            }
        }
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (rowIndex > arr.length -1|| rowIndex < 0) {
                return false;
            }
            return columnIndex  > 2 && ! arr[rowIndex].getIndirect();  
        }

        public int getColumnCount() {
            return 5;
        }

        public int getRowCount() {
            return arr.length;
        }
 
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex > arr.length -1 || rowIndex < 0) {
                return null;
            }
            ParameterValue pv = arr[rowIndex];
            JComponent c;
            switch(columnIndex) {
                case 0:
                    return Boolean.valueOf(pv.getIndirect());
                case 1:
                    return pv.getName();
                case 2:
                    return StringUtils.abbreviate(pv.getValue(),VALUE_WIDTH);
                 case 3:
                      c = (JComponent) viewButtons.get(rowIndex);
                     c.setEnabled(! pv.getIndirect());
                     return c;
                 case 4:
                         c = (JComponent)saveButtons.get(rowIndex);
                         c.setEnabled(! pv.getIndirect());
                         return c;                         
                  default:
                      return null;
            }
            
        }
        
        public final int VALUE_WIDTH = 60;

        
    }
    
    
    private void initialize() {
        getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.lookout");
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(ui.getComponent());
        this.setJMenuBar(getJJMenuBar());
        this.setSize(700, 800);
        JPanel pane = getJContentPane();    
        this.setTitle("VO Lookout");
        
      
        
        JSplitPane leftRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        leftRight.setDividerSize(5);
        leftRight.setDividerLocation(250);
        leftRight.setTopComponent(new JScrollPane(getFolderTree()));
        JSplitPane topBottom = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        leftRight.setBottomComponent(topBottom);
        
        JScrollPane tableScrollPane = new JScrollPane(getMessageTable());
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        topBottom.setTopComponent(tableScrollPane);
        topBottom.setBottomComponent(getMessageDetails());
        topBottom.setDividerSize(5);
        topBottom.setDividerLocation(200);
        pane.add(getToolbar(),BorderLayout.NORTH);
        pane.add(leftRight,BorderLayout.CENTER);
        this.setContentPane(pane);
    }
    
    
    // dislpaying results
    //@todo hard-coded for now. replace later with a strategy engine -
    // file and mime types, maybe.
    // later handle indirect too.
    private URL displayResult(ParameterValue pv ) throws TransformerFactoryConfigurationError, IOException, TransformerException {
        Transformer trans;
        if (pv.getValue().indexOf("<workflow") != -1) {
            trans = getWorkflowTransformer();
        } else if (pv.getValue().indexOf("<VOTABLE") != -1) {
            trans = getVotableTransformer();
        } else if (pv.getValue().indexOf("<?xml") != -1) {
            trans = getXmlTransformer();
        } else {
            trans = null;
        }
        File f = File.createTempFile(pv.getName(),".html");
        OutputStream out = new FileOutputStream(f);
        if (trans != null) {
            Result result= new StreamResult(out);
            Source source = new StreamSource(new ByteArrayInputStream(pv.getValue().getBytes()));
            trans.transform(source,result);
        } else {
            OutputStreamWriter w = new OutputStreamWriter(out);
            w.write(pv.getValue());
            w.flush();
        }
        out.close();
        return f.toURL();
        
    }
    
    private Transformer workflowTransformer;
    private Transformer votableTransformer;
    private Transformer xmlTransformer;
    private Transformer getWorkflowTransformer() throws TransformerConfigurationException, TransformerFactoryConfigurationError {
        if (workflowTransformer == null) {
            Source styleSource = WorkflowResultTransformerSet.Workflow2XhtmlTransformer.getStyleSource();
            workflowTransformer = TransformerFactory.newInstance().newTransformer(styleSource);
            workflowTransformer.setOutputProperty(OutputKeys.METHOD,"html");              
        }
        return workflowTransformer;
    }
    
    private Transformer getVotableTransformer() throws TransformerConfigurationException, TransformerFactoryConfigurationError {
        if (votableTransformer == null) {
            Source styleSource = Votable2XhtmlTransformer.getStyleSource();
            votableTransformer = TransformerFactory.newInstance().newTransformer(styleSource);
            votableTransformer.setOutputProperty(OutputKeys.METHOD,"html");              
        }
        return votableTransformer;
    }
    
    private Transformer getXmlTransformer() throws TransformerConfigurationException, TransformerFactoryConfigurationError {
        if (xmlTransformer == null) {
            Source styleSource = Xml2XhtmlTransformer.getStyleSource();
            xmlTransformer = TransformerFactory.newInstance().newTransformer(styleSource);
            xmlTransformer.setOutputProperty(OutputKeys.METHOD,"html");              
        }
        return xmlTransformer;
    }
        
   
    

}


/* 
$Log: LookoutImpl.java,v $
Revision 1.3  2005/11/10 16:28:26  nw
added result display to vo lookout.

Revision 1.2  2005/11/10 12:06:18  nw
early draft of volookout

Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/