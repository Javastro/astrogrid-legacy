/* WorkflowBuilderImpl.java
 * Created on 29-Apr-2005
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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.Lookout;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.JobsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ForDialog;
import org.astrogrid.desktop.modules.dialogs.IfDialog;
import org.astrogrid.desktop.modules.dialogs.ParforDialog;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.ScriptDialog;
import org.astrogrid.desktop.modules.dialogs.SetDialog;
import org.astrogrid.desktop.modules.dialogs.StepDialog;
import org.astrogrid.desktop.modules.dialogs.ToolEditorInternal;
import org.astrogrid.desktop.modules.dialogs.UnsetDialog;
import org.astrogrid.desktop.modules.dialogs.WhileDialog;
import org.astrogrid.desktop.modules.dialogs.WorkflowDetailsDialog;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.workflowBuilder.dragAndDrop.WorkflowDnDTree;
import org.astrogrid.desktop.modules.workflowBuilder.dragAndDrop.listeners.WastebinDropListener;
import org.astrogrid.desktop.modules.workflowBuilder.dragAndDrop.listeners.WorkflowTreeModelListener;
import org.astrogrid.desktop.modules.workflowBuilder.models.SimpleWorkflowTreeModel;
import org.astrogrid.desktop.modules.workflowBuilder.renderers.ActivityListRenderer;
import org.astrogrid.util.DomHelper;
import org.astrogrid.workflow.beans.v1.Else;
import org.astrogrid.workflow.beans.v1.Flow;
import org.astrogrid.workflow.beans.v1.For;
import org.astrogrid.workflow.beans.v1.If;
import org.astrogrid.workflow.beans.v1.Parfor;
import org.astrogrid.workflow.beans.v1.Scope;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Then;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Unset;
import org.astrogrid.workflow.beans.v1.While;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;

import com.l2fprod.common.swing.StatusBar;


/**
 * @author Phil Nicolson pjn3@star.le.ac.uk
 * @modified nww smoothed up backgrounnd operaitons, added new tool editor, removed top tabs.
 *
 */
public class WorkflowBuilderImpl extends UIComponent implements org.astrogrid.acr.ui.WorkflowBuilder {
	
    /** save a workflow */
	protected final class SaveAction extends AbstractAction {
	    public SaveAction() {
	        super("Save", IconHelper.loadIcon("fileexport.png"));
	        this.putValue(SHORT_DESCRIPTION,"Save this workflow");
	        this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_S));
	        this.setEnabled(true); 
	    }
        public void actionPerformed(ActionEvent e) {
            final URI u = chooser.chooseResource("Save Workflow",true);
            if (u == null) {
                return;
            }
            (new BackgroundOperation("Saving Workflow") {
                    protected Object construct() throws Exception {        		
	                    Writer writer = new OutputStreamWriter(vos.getOutputStream(u));	          
                        getModel().getWorkflow().marshal(writer); 
	                     writer.close();     	             
                         return null;
                     }
                }).start();	            
    
	    }
	}
    /** load a workflow */
	protected final class LoadAction extends AbstractAction {
	    public LoadAction() {
	        super("Open", IconHelper.loadIcon("file_obj.gif"));
	        this.putValue(SHORT_DESCRIPTION,"Load a workflow from storage");
	        this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_O));
	        this.setEnabled(true);            
	    }

	        public void actionPerformed(ActionEvent e) {
		        	    final URI u = chooser.chooseResource("Load workflow",true);
                        if (u == null) {
                            return;
                        }
	                (new BackgroundOperation("Loading Workflow") {
	                    protected Object construct() throws Exception {	       // do all long-runing tasks in this method, as runs in background thread
	                    	
		                    Reader reader = new InputStreamReader(vos.getInputStream(u));		                		         
		                    Workflow wf = (Workflow)Unmarshaller.unmarshal(Workflow.class, reader);	                
		                    reader.close();
                            return wf;
                        }
                        protected void doFinished(Object o) { // do all updating of ui in this method, as runs on swing thread
                            getModel().setWorkflow((Workflow)o);   
                            getTree().expandAll(true);
                            tabbedPaneWF.setSelectedIndex(0);
                    	    tabbedPaneWF.setEnabledAt(0, true);
                    	    tabbedPaneWF.setEnabledAt(1, true);
                    	    //@todo tidy this up.
                    		saveAction.setEnabled(true);
                    		caretPos = 0;
                    		wfDocFindField.setText("");
                    		validateWorkflow();
	                    }
	                }).start();	                	    
	        }
	    }	
    /** submit a workflow for execution */
    protected final class SubmitAction extends AbstractAction {
        public SubmitAction() {
            super("Submit Workflow",IconHelper.loadIcon("run_tool.gif"));
            this.putValue(SHORT_DESCRIPTION,"Submit a workflow for execution");
            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_W));
            this.setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
        	(new BackgroundOperation("Submitting Workflow"){

                protected Object construct() throws Exception {
                    return jobs.submitWorkflow(getModel().getWorkflow());
                }
                protected void doFinished(Object o) {
                    /* just hop straight to monitor
                    URI id = (URI)o;
                    ResultDialog rd = new ResultDialog(WorkflowBuilderImpl.this,"Workflow Submitted \nJob ID is \n" + id);
                    rd.show();
                    */
                    lookout.show(); // brings monitor to the front, if not already there.
                }
            }).start();
        }
    }
    /** create a workflow */
    protected final class CreateAction extends AbstractAction {
        public CreateAction() {
            super("New",IconHelper.loadIcon("newfile_wiz.gif"));
            this.putValue(SHORT_DESCRIPTION,"Create a new workflow");
            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
            this.setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
            //@todo factor out commonality with 'loadWorkflow' - or even better, replace with action listeners.
        	int i = -1;
        	if (e == null) { // createAction called as builder initialized
        		try {        			
        			getModel().setWorkflow(jobs.createWorkflow());
        			getTree().expandAll(true);
        		} 
        		catch (ServiceException ex) { // quite unlikely.
        			showError("Failed to create workflow",ex);
        		}
        		tabbedPaneWF.setSelectedIndex(0);
        		tabbedPaneWF.setEnabledAt(0, true);
        		tabbedPaneWF.setEnabledAt(1, true);

        		submitAction.setEnabled(true);
        		saveAction.setEnabled(true);
        	} else {
        	    i = JOptionPane.showConfirmDialog(null, "Creating a new workflow will mean current workflow is deleted. \n" 
        	    		                               +"Do you wish to save your workflow prior to creating new one?", "Create new workflow", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); 
        	}
        	if (i == JOptionPane.YES_OPTION) {
        		saveAction.actionPerformed(null);
        		try {
        			getModel().setWorkflow(jobs.createWorkflow());
        			getTree().expandAll(true);
        		} 
        		catch (ServiceException ex) { // quite unlikely.
        			showError("Failed to create workflow",ex);
        		}
        		tabbedPaneWF.setSelectedIndex(0);
        		tabbedPaneWF.setEnabledAt(0, true);
        		tabbedPaneWF.setEnabledAt(1, true);

        		submitAction.setEnabled(true);
        		saveAction.setEnabled(true);
        	}
        	if (i == JOptionPane.NO_OPTION) {
        		try {
        			getModel().setWorkflow(jobs.createWorkflow());
        			getTree().expandAll(true);
        		} 
        		catch (ServiceException ex) { // quite unlikely.
        			showError("Failed to create workflow",ex);
        		}
        		tabbedPaneWF.setSelectedIndex(0);
        		tabbedPaneWF.setEnabledAt(0, true);
        		tabbedPaneWF.setEnabledAt(1, true);

        		submitAction.setEnabled(true);
        		saveAction.setEnabled(true);
        	}
        }
    }    
    /** close action */
    protected final class CloseAction extends AbstractAction {
        /**
         * Commons Logger for this class
         */
        private final Log logger = LogFactory.getLog(CloseAction.class);

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
    /** collapse workflow */
	protected final class CollapseAction extends AbstractAction {
	    public CollapseAction() {
	        super("Collapse", IconHelper.loadIcon("collapse.gif"));
	        this.putValue(SHORT_DESCRIPTION,"Collapse tree");
	        this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
	        this.setEnabled(true);            
	    }
        public void actionPerformed(ActionEvent e) {
       //     TreeNode node = (TreeNode)tree.getLastSelectedPathComponent();
       //     if (node != null) {
       //     	tree.expandAll(node,false);
       //     } else {
       //         tree.expandAll(false);
       //     }
        	tree.collapse();
	    }
	}
    /** expand workflow */
	protected final class ExpandAction extends AbstractAction {
	    public ExpandAction() {
	        super("Expand", IconHelper.loadIcon("expand.gif"));
	        this.putValue(SHORT_DESCRIPTION,"Expand tree");
	        this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_E));
	        this.setEnabled(true);            
	    }
        public void actionPerformed(ActionEvent e) {
       //     TreeNode node = (TreeNode)tree.getLastSelectedPathComponent();
       //     if (node != null) {
       //     	tree.expandAll(node,true);
       //     } else {
       //     	tree.expandAll(true);
       //     }
        	tree.expand();            
	    }
	}
    /** promote node */
	protected final class PromoteAction extends AbstractAction {
	    public PromoteAction() {
	        super("Move up", IconHelper.loadIcon("promote.gif"));
	        this.putValue(SHORT_DESCRIPTION,"Move activity up");
	        this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_U));
	        this.setEnabled(true);
	    }
        public void actionPerformed(ActionEvent e) {
        	moveNode(true);
	    }
	}
    /** demote node */
	protected final class DemoteActivity extends AbstractAction {
	    public DemoteActivity() {
	        super("Move down", IconHelper.loadIcon("demote.gif"));
	        this.putValue(SHORT_DESCRIPTION,"Move activity down");
	        this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_D));
	        this.setEnabled(true);            
	    }
        public void actionPerformed(ActionEvent e) {
        	moveNode(false);
	    }
	}
    /** delete node (non DnD)*/
	protected final class DeleteAction extends AbstractAction {
	    public DeleteAction() {
	        super("Delete", IconHelper.loadIcon("delete_obj.gif"));
	        this.putValue(SHORT_DESCRIPTION,"Delete selected activity");
	        this.putValue(ACCELERATOR_KEY, KeyStroke.getAWTKeyStroke(new Integer(KeyEvent.VK_D).intValue(),InputEvent.CTRL_DOWN_MASK));
	        this.setEnabled(true);            
	    }
        public void actionPerformed(ActionEvent e) {
        	DefaultMutableTreeNode deleteNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
    		if (deleteNode == null) {
    			setStatusMessage("No activity selected");
    		}		
    		else if (w.canPerformDrop(deleteNode)) {
    			deleteNode(deleteNode);
    			setStatusMessage("");
    		} else {
    			setStatusMessage("Unable to delete this activity");;
    		}
	    }
	}
    /** copy node */
	protected final class CopyAction extends AbstractAction {
	    public CopyAction() {
	        super("Copy", IconHelper.loadIcon("copy.gif"));
	        this.putValue(SHORT_DESCRIPTION,"Copy selected activity");
	        this.putValue(ACCELERATOR_KEY, KeyStroke.getAWTKeyStroke(new Integer(KeyEvent.VK_C).intValue(),InputEvent.SHIFT_DOWN_MASK));
	        this.setEnabled(true);            
	    }
        public void actionPerformed(ActionEvent e) {
        	/**
        	 * Copy node. Only nodes that can be copied will be copied, uses
        	 * canPerformDrop to confirm if OK to copy node.
        	 * @see org.astrogrid.desktop.modules.workflowBuilder.dragAndDrop.listeners.WastebinDropListener#canPerformDrop(DefaultMutableTreeNode)
        	 * @param copyNode node to be copied
        	 * @return boolean indicating if delete succeeded
        	 */
        	DefaultMutableTreeNode copyNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        	if (copyNode == null) {
        		setStatusMessage("No activity selected");
        	}		
        	else if (w.canPerformDrop(copyNode)) {
        		copiedNode = copyNode;
        		setStatusMessage("");
        	} else {
        		setStatusMessage("Cannot copy this activity");
        	}
	    }
	}
    /** cut node */
	protected final class CutAction extends AbstractAction {
	    public CutAction() {
	        super("Cut", IconHelper.loadIcon("cut_edit.gif"));
	        this.putValue(SHORT_DESCRIPTION,"Cut selected activity");
	        this.putValue(ACCELERATOR_KEY, KeyStroke.getAWTKeyStroke(new Integer(KeyEvent.VK_X).intValue(),InputEvent.SHIFT_DOWN_MASK));
	        this.setEnabled(true);            
	    }
        public void actionPerformed(ActionEvent e) {
        	/**
        	 * Cut node. Only nodes that can be cut will be cut, uses
        	 * canPerformDrop in deleteNode to confirm if OK to cut node.
        	 * @see deleteNode()
        	 * @param cutNode node to be cut
        	 * @return boolean indicating if delete succeeded
        	 */
        	DefaultMutableTreeNode cutNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
    		if (cutNode == null) {
    			setStatusMessage("No activity selected");
    		}		
    		else if (deleteNode(cutNode)) {    		    		
    			copiedNode = cutNode;
    			setStatusMessage("");
    		} else {
    			setStatusMessage("Unable to cut this activity");
    		}
        }
	}
    /** paste node */
	protected final class PasteAction extends AbstractAction {
	    public PasteAction() {
	        super("Paste", IconHelper.loadIcon("paste_edit.gif"));
	        this.putValue(SHORT_DESCRIPTION,"Paste copied activity");
	        this.putValue(ACCELERATOR_KEY, KeyStroke.getAWTKeyStroke(new Integer(KeyEvent.VK_V).intValue(),InputEvent.SHIFT_DOWN_MASK));
	        this.setEnabled(true);            
	    }
        public void actionPerformed(ActionEvent e) {
        	DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        	if (copiedNode == null) {
    			setStatusMessage("Clipboard contains no activity");
    		}
        	else if (parentNode == null) {
    			setStatusMessage("Select where you wish to paste activity");
    		}
    		else if (w.canPerformPaste(copiedNode, parentNode)) {
    			tree.expandPath(new TreePath(parentNode.getFirstLeaf().getPath()));
    			((DefaultTreeModel)tree.getModel()).insertNodeInto(getModel().copyTree(copiedNode), parentNode, 0);
    			TreePath treePath = new TreePath(copiedNode.getPath());
    			int i = 0;		
    			for (Enumeration enumeration = copiedNode.depthFirstEnumeration(); enumeration.hasMoreElements(); i++ ) {
    				DefaultMutableTreeNode element = (DefaultMutableTreeNode)enumeration.nextElement();
    			    TreePath path = new TreePath(element.getPath());
    			    tree.expandPath(path);
    			}		
    			tree.scrollPathToVisible(treePath);
    			tree.setSelectionPath(treePath);    			
    			setStatusMessage("");
    		} else {
    			setStatusMessage("Unable to paste activity here, paste into either Sequence or Flow activities");
    		}	            	        	            	
	    }
	}
	// components this ui uses.
    protected final ToolEditorInternal toolEditor;
    protected final BrowserControl browser;
    protected final MyspaceInternal vos;
    protected final ResourceChooserInternal chooser;
    protected final Lookout lookout;
    private final ApplicationsInternal apps;
    private final JobsInternal jobs;
    
    // actions
    protected Action saveAction;
    protected Action loadAction;
    protected Action submitAction;
    protected Action createAction;
    protected Action closeAction;
    protected Action collapseAction;
    protected Action expandAction;
    protected Action promoteAction;
    protected Action demoteActivity;
    protected Action deleteAction;
    protected Action copyAction;
    protected Action cutAction;
    protected Action pasteAction;
    
    private JButton  findButton = null;
    private JEditorPane docTextArea;
    private JList list;
    private JLabel statusLabel, workflowStatusLabel, wastebinLabel = null;
    private JMenuBar jJMenuBar = null;
    private JMenu fileMenu, editMenu = null;
    private JToolBar toolbar = null;    
    private DefaultListModel activityListModel= null;
    private WorkflowDnDTree tree = null;
    private JTabbedPane tabbedPaneWF;
    private JTextField  wfDocFindField;   
    private JScrollPane listView = null;
    private URL helpUrl;
    private int caretPos = 0;
    private StatusBar statusBar = null;
    private DefaultMutableTreeNode copiedNode; // used for cut and copy
    WastebinDropListener w;
    public boolean autoPopUp = true;    
    
    ActivityListRenderer activityListRenderer = new ActivityListRenderer();
	
    /** 
     * production constructor 
     * @throws Exception
     * 
     * */
    public WorkflowBuilderImpl(ApplicationsInternal apps, JobsInternal jobs,Lookout monitor,  MyspaceInternal vos, BrowserControl browser,
            ToolEditorInternal toolEditor,ResourceChooserInternal chooser
             ,UIInternal ui, HelpServerInternal hs, Configuration conf) throws Exception {
        super(conf,hs,ui);
        this.browser = browser;
        this.vos = vos;
        this.lookout = monitor;
        this.apps = apps;
        this.jobs = jobs;
        this.chooser = chooser;
        this.toolEditor = toolEditor;
        initialize();
        
    }   
    
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {

		this.setTitle("Workflow Builder");
		this.setJMenuBar(getJJMenuBar());
        setIconImage(IconHelper.loadIcon("wf_small.gif").getImage());        
        getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.workflowBuilder");  			
		tabbedPaneWF = new JTabbedPane();		
		tabbedPaneWF.addTab ("Tree View", IconHelper.loadIcon("wf_small.gif"), getTabbedTreePanel(), "Display workflow graphically");
		tabbedPaneWF.setMnemonicAt(0,KeyEvent.VK_T);
		tabbedPaneWF.addTab("Document", IconHelper.loadIcon("document.gif"), getTabbedDocPanel(), "Display text version of workflow");
		tabbedPaneWF.setMnemonicAt(1,KeyEvent.VK_D);
		tabbedPaneWF.setEnabledAt(0, false);
		tabbedPaneWF.setEnabledAt(1, false);
		tabbedPaneWF.addMouseListener(new MouseAdapter(){
	        public void mouseClicked(MouseEvent event)
	        {
	        	if (((JTabbedPane)event.getSource()).getSelectedIndex() == 1) { // only if user is selecting text tab	        		
		            try{
		                Document doc = DomHelper.newDocument();
		                Marshaller.marshal(getModel().getWorkflow(),doc);
		                StringWriter sw = new StringWriter();
		                XMLUtils.PrettyDocumentToWriter(doc,sw);
		                docTextArea.setText(sw.toString());
		                docTextArea.setCaretPosition(0);
		            }
		            catch(ParserConfigurationException ex){
	                    logger.error("ParserConfigurationException thrown: " + ex);
		            }
		            catch(MarshalException ex){
		            	logger.error("MarshalException thrown: " + ex);
		            }
		            catch(ValidationException ex){
		            	try {		            		
		            		Document doc = DomHelper.newDocument();
		            		Marshaller marshaller = new Marshaller(doc);
		            		marshaller.setValidation(false);
		            		marshaller.marshal(getModel().getWorkflow());
		            		StringWriter sw = new StringWriter();
		            		XMLUtils.PrettyDocumentToWriter(doc,sw);
		            		docTextArea.setText(sw.toString());
			                docTextArea.setCaretPosition(0);
			                showError("Your workflow document contains invalid xml",ex);
		            	} catch(Exception exc) {
		            		logger.error("Error unmarshalling workflow object with validation turned off");
		            	}	                    
		            }
	        	}
	        }
	    });
			
		JPanel pane = getJContentPane();
		pane.add(tabbedPaneWF, BorderLayout.CENTER);
		pane.add(getToolbar(), BorderLayout.NORTH);
		
		submitAction = new SubmitAction();
        submitAction.setEnabled(false); // Initially false until a workflow is loaded/created
        loadAction = new LoadAction();
        createAction = new CreateAction(); 
        saveAction = new SaveAction();
        saveAction.setEnabled(false); // Initially false until a workflow is loaded/created
        closeAction = new CloseAction();
        collapseAction = new CollapseAction();
        expandAction = new ExpandAction();
        promoteAction = new PromoteAction();
        demoteActivity = new DemoteActivity();
        deleteAction = new DeleteAction();
        copyAction = new CopyAction();
        cutAction = new CutAction();
        pasteAction = new PasteAction();
        
        toolbar.add(createAction);
        toolbar.add(loadAction);
        toolbar.add(saveAction);
        toolbar.add(submitAction);
        toolbar.add(collapseAction);
        toolbar.add(expandAction);
        toolbar.add(promoteAction);
        toolbar.add(demoteActivity);
        toolbar.add(deleteAction);
        toolbar.add(copyAction);
        toolbar.add(cutAction);
        toolbar.add(pasteAction);
        
        fileMenu.add(createAction);
        fileMenu.add(loadAction);
        fileMenu.add(saveAction);      
        fileMenu.add(new JSeparator());
        fileMenu.add(submitAction);
        fileMenu.add(new JSeparator());
        JMenu preferencesMenu = new JMenu("Preferences");
        preferencesMenu.setMnemonic(KeyEvent.VK_P);
        JCheckBoxMenuItem cbMenuItem1 = new JCheckBoxMenuItem("Pop-up editor on insert");
        cbMenuItem1.setToolTipText("If checked a pop-up for entering parameter values will automatically appear \n as activites are enterred into the workflow.");
        cbMenuItem1.setSelected(true);
        cbMenuItem1.setMnemonic(KeyEvent.VK_P);
        cbMenuItem1.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent e) {
        		if (e.getStateChange() == ItemEvent.SELECTED) {
        			autoPopUp = true;
        		} else {
        			autoPopUp = false;
        		}
        		;
        	}
        });
        JCheckBoxMenuItem cbMenuItem2 = new JCheckBoxMenuItem("Show activity tooltips");
        cbMenuItem2.setToolTipText("If checked detailed tool tips for activities will be displayed.");
        cbMenuItem2.setSelected(true);
        cbMenuItem2.setMnemonic(KeyEvent.VK_S);
        cbMenuItem2.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent e) {
        		if (e.getStateChange() == ItemEvent.SELECTED) {
        			activityListRenderer.setToolTipVisible(true);
        		} else {
        			activityListRenderer.setToolTipVisible(false);
        		}
        		;
        	}
        });
        preferencesMenu.add(cbMenuItem1);
        preferencesMenu.add(cbMenuItem2);
        fileMenu.add(preferencesMenu);
        fileMenu.add(new JSeparator());
        fileMenu.add(closeAction);
        
        editMenu.add(cutAction);
        editMenu.add(copyAction);
        editMenu.add(pasteAction);
        editMenu.add(new JSeparator());
        editMenu.add(deleteAction);
        editMenu.add(new JSeparator());
        editMenu.add(expandAction);
        editMenu.add(collapseAction);
        editMenu.add(promoteAction);
        editMenu.add(demoteActivity);
        
        pane.add(getActivityList(),BorderLayout.WEST);
		this.setContentPane(pane);
		pack();
           this.setSize(565,800);        
		this.w = new WastebinDropListener();
		createAction.actionPerformed(null); // fire the create action to initialize everything.
	}



	/**
	 * 
	 */
    public void show() {
        super.show();
        requestFocus();    
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
		}
		return jJMenuBar;
	}
	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */    
	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.setMnemonic(KeyEvent.VK_F);
			getHelpServer().enableHelp(fileMenu,"userInterface.workflowBuilder.fileMenu");
		}
		return fileMenu;
	}
	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */    
	private JMenu getEditMenu() {
		if (editMenu == null) {
			editMenu = new JMenu();
			editMenu.setText("Edit");
			editMenu.setMnemonic(KeyEvent.VK_E);
			getHelpServer().enableHelp(editMenu,"userInterface.workflowBuilder.editMenu");
		}
		return editMenu;
	}
	/**
	 * This method initializes jToolBar	
	 * 	
	 * @return javax.swing.JToolBar	
	 */    
	private JToolBar getToolbar() {
		if (toolbar == null) {
			toolbar = new JToolBar();
			toolbar.setFloatable(false);
            toolbar.setRollover(true);
            getHelpServer().enableHelp(toolbar,"userInterface.workflowBuilder.toolbar");
		}
		return toolbar;
	}
    /** extend the existing status bar */
	protected StatusBar getBottomPanel() {
        if (statusBar == null) { // hasn't been accessed yet
            statusBar = super.getBottomPanel();
            statusBar.addZone("wastebin",getWasteBin(),"29");
            statusBar.addZone("workflowStatus",getWorkflowStatus(),"27");
            getHelpServer().enableHelp(statusBar,"userInterface.workflowBuilder.statusBar");
        }
        return statusBar;
	}
    
   
	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */    
	private JLabel getWorkflowStatus() {
		if (workflowStatusLabel == null) {
			workflowStatusLabel = new JLabel();
			workflowStatusLabel.setText("");
			workflowStatusLabel.setDisabledIcon(IconHelper.loadIcon("redcross.gif"));
			workflowStatusLabel.setIcon(IconHelper.loadIcon("greentick.gif"));
			workflowStatusLabel.setEnabled(false);
			workflowStatusLabel.setToolTipText("Indicates whether workflow is valid");			
		}
		return workflowStatusLabel;
	} 
 	
	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */    
	private JLabel getWasteBin() {
		if (wastebinLabel == null) {
			wastebinLabel = new JLabel();
			wastebinLabel.setText("");
			wastebinLabel.setIcon(IconHelper.loadIcon("delete_obj.gif"));
			wastebinLabel.setToolTipText("Drop items here to delete");
			DropTarget dt = new DropTarget(wastebinLabel, new WastebinDropListener(tree));
		}
		return wastebinLabel;
	}
  
	/**
	 * This method returns the activity list
	 * 	
	 * @return javax.swing.JScrollpane	
	 */    
	private JScrollPane getActivityList() {
		if (activityListModel == null) {
			activityListModel = new DefaultListModel();			
			activityListModel.addElement("Step"); 
			activityListModel.addElement("Flow");
			activityListModel.addElement("Sequence");
			activityListModel.addElement("If");
			activityListModel.addElement("Else");
			activityListModel.addElement("Scope");
			activityListModel.addElement("Script");
			activityListModel.addElement("Set");
			activityListModel.addElement("Unset");
			activityListModel.addElement("For");
			activityListModel.addElement("ParFor");
			activityListModel.addElement("While");
			list = new JList(activityListModel);			
			list.setDragEnabled(true);
			singleClickDnD(list);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setCellRenderer(activityListRenderer);
			list.addMouseListener(new MouseAdapter() {				
				public void mousePressed(MouseEvent evt) {
					setStatusMessage("");
				}
			});
			//list.setTransferHandler(new DefaultActivityTransferHandler());
			listView = new JScrollPane(list);
			getHelpServer().enableHelp(listView,"userInterface.workflowBuilder.activityList");
		}
		return listView;
    }
	

    private SimpleWorkflowTreeModel model;
    
    /** build the model that manages the workflow for the JTree */
    private SimpleWorkflowTreeModel getModel() {
        if (model == null) {
            model = new SimpleWorkflowTreeModel();
            model.addTreeModelListener(new WorkflowTreeModelListener(this));
        }
        return model;
    }
	
    /** build / access the tree */
    private WorkflowDnDTree getTree() {
        if (tree == null) {
           tree =  new WorkflowDnDTree(apps, this);
           tree.setModel(getModel());
           tree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 1 ) {                	
                	setStatusMessage("");
                }
                else if (evt.getClickCount() == 2 ) {                	
                	editNode((DefaultMutableTreeNode)tree.getLastSelectedPathComponent(), false);
                }
             }
           });                   
        }
        return tree;
    }
    
    /**
     * Displays required pop-up to allow users to edit node.
     * 
     * @param node DefaultMutableTreeNode that user wishes to edit
     * @param boolean enableCancel used to test if cancel button pressed
     */
    public void editNode(DefaultMutableTreeNode node, boolean enableCancel) {
    	setStatusMessage("");        
        Object o = node.getUserObject();
        
        if (o instanceof String) {                        
            DefaultMutableTreeNode scriptNode = (DefaultMutableTreeNode)node.getParent();
            if (scriptNode.getUserObject() instanceof Script) { // expected to be true always - should be as String is script body
                Script newScript = showScriptDialog((Script)scriptNode.getUserObject()).getScript();
                if (newScript != null) {
                    getModel().nodeChanged(scriptNode);
                    node.setUserObject(newScript.getBody());
                    getModel().nodeChanged(node);
                } 
            }                        
        } 
        else if (o instanceof Script) {
            Script newScript = showScriptDialog((Script)o).getScript();
            if (newScript != null) {
                getModel().nodeChanged(node);
                DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)node.getFirstChild();
                childNode.setUserObject(newScript.getBody());
                getModel().nodeChanged(childNode);
            } else if (enableCancel) {
        		deleteNode(node); // user pressed cancel on pop-up, don't want to have null nodes inserted!
        	}                                  
        }                    
        else if (o instanceof Tool ) {
          try {
              Tool newTool = toolEditor.editTool((Tool)o,WorkflowBuilderImpl.this);
              if (newTool != null) { // i.e. changes have been made.
                 node.setUserObject(newTool); // store node back in ui model
                 DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)node.getParent();
                 Step s = (Step) parentNode.getUserObject(); // splice back into workflow.
                 s.setTool(newTool); 
                 getModel().nodeChanged(node);
              } else if (enableCancel) {
              	deleteNode(node); // user pressed cancel on pop-up, don't want to have null nodes inserted!
       	      }
           } catch (ACRException e) {
                showError("Failed to edit tool",e);
           }
        } 
        else if (o instanceof Step) {            
        	Step newStep = showStepDialog((Step)o).getStep();
        	if (newStep.getResultVar().equalsIgnoreCase(""))
        		newStep.setResultVar(null);
        	if (newStep != null && newStep.getTool() == null) { // no tool selected yet.
                try {
                	Tool newTool = toolEditor.selectAndBuildTool(WorkflowBuilderImpl.this);
                	if (newTool != null) {
                		newStep.setTool(newTool);
                		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode();
                		childNode.setUserObject(newTool);
                		node.add(childNode);
                		getModel().nodeChanged(childNode);        			
                	} else if (enableCancel) {
                		deleteNode(node); // user pressed cancel on pop-up, don't want to have null nodes inserted!
                	} 
                } catch (ACRException e) {
                    showError("Failed to edit tool",e);
                }
            node.setUserObject(newStep);
            getModel().nodeChanged(node);
        	} else if (enableCancel) {
        		deleteNode(node); // user pressed cancel on pop-up, don't want to have null nodes inserted!
     	    }      	                    	                    
        }
        else if (o instanceof If) {
        	If newIf = showIfDialog((If)o).getIf();
        	if (newIf != null) {
                // aleady here - it's an alias
        		getModel().nodeChanged(node);
        	} else if (enableCancel) {
        		deleteNode(node); // user pressed cancel on pop-up, don't want to have null nodes inserted!
        	}
        }
        else if (o instanceof Set) {
        	Set newSet = showSetDialog((Set)o).getSet();
        	if (newSet != null) {
            	getModel().nodeChanged(node);
        	} else if (enableCancel) {
        		deleteNode(node); // user pressed cancel on pop-up, don't want to have null nodes inserted!
        	}
        }
        else if (o instanceof Unset) {
        	Unset newUnset = showUnsetDialog((Unset)o).getUnset();
        	if (newUnset != null) {
        		getModel().nodeChanged(node);
        	} else if (enableCancel) {
        		deleteNode(node); // user pressed cancel on pop-up, don't want to have null nodes inserted!
        	}                    	
        }
        else if (o instanceof For) {
        	For newFor = showForDialog((For)o).getFor();
        	if (newFor != null) {
        		getModel().nodeChanged(node);
        	} else if (enableCancel) {
        		deleteNode(node); // user pressed cancel on pop-up, don't want to have null nodes inserted!
        	}                    	
        }
        else if (o instanceof Parfor) {
        	Parfor newParfor = showParforDialog((Parfor)o).getParfor();
        	if (newParfor != null) {
            	getModel().nodeChanged(node);
        	} else if (enableCancel) {
        		deleteNode(node); // user pressed cancel on pop-up, don't want to have null nodes inserted!
        	}
        }                    
        else if (o instanceof While) {
        	While newWhile = showWhileDialog((While)o).getWhile();
        	if (newWhile != null) {
            	getModel().nodeChanged(node);
        	} else if (enableCancel) {
        		deleteNode(node); // user pressed cancel on pop-up, don't want to have null nodes inserted!
        	}
        }                    
        else if (o instanceof Workflow) {
        	Workflow newWorkflow = showWorkflowDetailsDialog((Workflow)o).getWorkflow();
        	if (newWorkflow != null) {
        		getModel().nodeChanged(node);                    		
        	}
        	tree.expandAll(true); // nodeChanged will focus to root, so expand again
        }
        else if (o instanceof Sequence || 
        		 o instanceof Flow || 
				 o instanceof Scope ||
				 o instanceof Else ||
				 o instanceof Then ) {        	
        	setStatusMessage("No inputs required for this activity");
        }                    
    
    }
 
    private ForDialog forDialog;
    private ForDialog showForDialog(For f) {
        if (forDialog == null) {
            forDialog = new ForDialog(this);
        }
        forDialog.setFor(f);
        forDialog.setVisible(true);
        return forDialog;
    }
    
    private IfDialog ifDialog;
    private IfDialog showIfDialog(If i) {
        if (ifDialog == null) {
            ifDialog = new IfDialog(this);
        }
        ifDialog.setIf(i);
        ifDialog.setVisible(true);
        return ifDialog;
    }    
    
    private ParforDialog parforDialog;
    private ParforDialog showParforDialog(Parfor p) {
        if (parforDialog == null) {
            parforDialog = new ParforDialog(this);
        }
        parforDialog.setParfor(p);
        parforDialog.setVisible(true);
        return parforDialog;
    }    

    private ScriptDialog scriptDialog;
    private ScriptDialog showScriptDialog(Script s) {
        if (scriptDialog == null) {
            scriptDialog = new ScriptDialog(this);
        }
        scriptDialog.setScript(s);
        scriptDialog.setVisible(true);
        return scriptDialog;
    }    
    
    private SetDialog setDialog;
    private SetDialog showSetDialog(Set s) {
        if (setDialog == null) {
            setDialog = new SetDialog(this);
        }
        setDialog.setSet(s);
        setDialog.setVisible(true);
        return setDialog;
    }
    
    private StepDialog stepDialog;
    private StepDialog showStepDialog(Step s) {
        if (stepDialog == null) {
            stepDialog = new StepDialog(this);
        }
        stepDialog.setStep(s);
        stepDialog.setVisible(true);
        return stepDialog;
    }    
    
    private UnsetDialog unsetDialog;
    private UnsetDialog showUnsetDialog(Unset u) {
        if (unsetDialog == null) {
            unsetDialog = new UnsetDialog(this);
        }
        unsetDialog.setUnset(u);
        unsetDialog.setVisible(true);
        return unsetDialog;
    }  
    
    private WhileDialog whileDialog;
    private WhileDialog showWhileDialog(While w) {
        if (whileDialog == null) {
            whileDialog = new WhileDialog(this);
        }
        whileDialog.setWhile(w);
        whileDialog.setVisible(true);
        return whileDialog;
    }    
    
    private WorkflowDetailsDialog workflowDialog;
    private WorkflowDetailsDialog showWorkflowDetailsDialog(Workflow w) {
        if (workflowDialog == null) {
        	workflowDialog = new WorkflowDetailsDialog(this);
        }
        workflowDialog.setWorkflow(w);
        workflowDialog.setVisible(true);
        return workflowDialog;
    }    
    
	private JPanel getTabbedTreePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(getTree());
		panel.add(scrollPane);
		panel.setBorder(BorderFactory.createTitledBorder("Tree view"));
		panel.setPreferredSize(new Dimension(800,300));
		getHelpServer().enableHelp(panel,"userInterface.workflowBuilder.treePanel");
		return panel;
	}
	
	private JPanel getTabbedDocPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		docTextArea = new JEditorPane();		
		docTextArea.setEditable(false);	
		JScrollPane scrollPane = new JScrollPane(docTextArea);
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(getFindPanel(), BorderLayout.SOUTH);
		panel.setBorder(BorderFactory.createTitledBorder("Workflow document"));
		panel.setPreferredSize(new Dimension(800,200));	
		getHelpServer().enableHelp(panel,"userInterface.workflowBuilder.docPanel");
		return panel;
	}
	
	private JPanel getFindPanel() {
		JPanel p = new JPanel(new FlowLayout());
		p.add(getFindButton());
		wfDocFindField = new JTextField(30);
		wfDocFindField.setToolTipText("Text to search workflow document for...");
		p.add(wfDocFindField);
		return p;
	}

      
	/**
	 * This method initializes search button
	 * actionPerformed will find specified text in docTextArea	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getFindButton() {
		if (findButton == null) {
			findButton = new JButton();
			findButton.setText("Find");
			findButton.setToolTipText("Find specified text in workflow document");
			findButton.setMnemonic(KeyEvent.VK_F);
			findButton.addActionListener(new java.awt.event.ActionListener() {
				
				public void actionPerformed(java.awt.event.ActionEvent e) {	
					if (wfDocFindField.getText() != null && wfDocFindField.getText().length() > 0) {
					    docTextArea.setSelectionColor(Color.white);
					    docTextArea.selectAll();
                        String cleanDoc = docTextArea.getSelectedText();
                        caretPos = (cleanDoc.indexOf(wfDocFindField.getText(), caretPos));
                        if (caretPos >= 0) {
                    	    docTextArea.setCaretPosition(caretPos);
                    	    docTextArea.setSelectionStart(caretPos);
                    	    docTextArea.setSelectionEnd(caretPos + wfDocFindField.getText().length());
                    	    docTextArea.setSelectionColor(Color.RED);
                            caretPos++;
		                    }
		                    else {
		                        caretPos = 0;
		                        docTextArea.setCaretPosition(caretPos);
		                    }
		                    docTextArea.requestFocus();		            
				        }
				}
			});
		}
		return findButton;
	}	
	/**
	 * Sets submit button and workflow valid icon depending if workflow is valid 
	 *
	 */
	public void validateWorkflow() {
		if (getModel().getWorkflow().isValid()) {
			submitAction.setEnabled(true);
			workflowStatusLabel.setEnabled(true);
		} else {
			submitAction.setEnabled(false);	
			workflowStatusLabel.setEnabled(false);
		}
	}
	
	/**
	 * Change default drag operation so that list item can be dragged without first clicking to selecting it
	 * @param list
	 */
	public static void singleClickDnD(JList list) {
		MouseListener dragListener = null;
		try {
			
			Class clazz = Class.forName("javax.swing.plaf.basic.BasicDragGestureRecognizer");
			MouseListener[] mouseListeners = list.getMouseListeners();
			for (int i = 0; i<mouseListeners.length; i++){
				if (clazz.isAssignableFrom(mouseListeners[i].getClass())) {
					dragListener = mouseListeners[i];
					break;
				}
			}
			if (dragListener != null) {
				list.removeMouseListener(dragListener);
				list.removeMouseMotionListener((MouseMotionListener)dragListener);
				list.addMouseListener(dragListener);
				list.addMouseMotionListener((MouseMotionListener)dragListener);
			}
		} catch(ClassNotFoundException e) {
			logger.error("Error adding single click DnD to activity list: " + e);
		}
	}
	 
	/**
	 * Moves selected node up or down depending on value of param.
	 * Prevents incorrect moves e.g. moving first child node up, or last child node down,
	 * moving the workflow node or trying to move without selecting a node first.
	 * @param b boolean - true moves node up, false down
	 * @return boolean indicating whether move successful
	 */
	private boolean moveNode(boolean b) {		
		DefaultMutableTreeNode moveNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		if (moveNode == null) {
			setStatusMessage("No activity selected");
			return false;
		}
		if (moveNode.getUserObject() instanceof Workflow ||
			moveNode.getUserObject() instanceof Else ||
			moveNode.getUserObject() instanceof Then) {
			setStatusMessage("Unable to move activity");
			return false;
		}
		
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)moveNode.getParent();

		int index = parentNode.getIndex(moveNode);
		int count = parentNode.getChildCount();
		if (count <= 1) { 
			moveNode = null;
			setStatusMessage("Cannot move this activity any " + (b?"higher":"lower") + " as it is the only child");
			return false;
		}
		if (parentNode.getUserObject() instanceof Flow) {
			setStatusMessage("Flow executes activities simultaneously, moving this node has no effect");
			return false;
		}
		if (b && index >= 1) { //moving up
			getModel().removeNodeFromParent(moveNode);
			getModel().insertNodeInto(moveNode, parentNode, index - 1);			
		} else if (!b && index <= count -2) { // moving down
			getModel().removeNodeFromParent(moveNode);
			getModel().insertNodeInto(moveNode, parentNode, index + 1);			
		} else {
			setStatusMessage("Cannot move this activity any " + (b?"higher":"lower"));
			return false;
		}
		
		TreePath treePath = new TreePath(moveNode.getPath());
		int i = 0;		
		for (Enumeration enumeration = moveNode.depthFirstEnumeration(); enumeration.hasMoreElements(); i++ ) {
			DefaultMutableTreeNode element = (DefaultMutableTreeNode)enumeration.nextElement();
		    TreePath path = new TreePath(element.getPath());
		    tree.expandPath(path);
		}
		tree.scrollPathToVisible(treePath);
		tree.setSelectionPath(new TreePath(moveNode.getPath()));
		moveNode = null;
		setStatusMessage("");
		return true;
	}

	

	/**
	 * Delete node. Only deletes nodes that can be deleted, uses 
	 * canPerformDrop to confirm if OK to delete.
	 * @see org.astrogrid.desktop.modules.workflowBuilder.dragAndDrop.listeners.WastebinDropListener#canPerformDrop(DefaultMutableTreeNode)
	 * @param deleteNode
	 * @return boolean
	 */
	private boolean deleteNode(DefaultMutableTreeNode deleteNode) {		
		if (deleteNode == null) {
			setStatusMessage("No activity selected");
			return false;
		}
		WastebinDropListener w = new WastebinDropListener();
		if (w.canPerformDrop(deleteNode)) {
			((DefaultTreeModel)tree.getModel()).removeNodeFromParent(deleteNode);
			deleteNode.removeFromParent();
			setStatusMessage("");
			return true;
		} else {
			setStatusMessage("Unable to delete this activity");
			return false;
		}
	}
} 
