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
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.HelpServer;
import org.astrogrid.acr.ui.JobMonitor;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.JobsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ForDialog;
import org.astrogrid.desktop.modules.dialogs.IfDialog;
import org.astrogrid.desktop.modules.dialogs.ParforDialog;
import org.astrogrid.desktop.modules.dialogs.RegistryChooserDialog;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.dialogs.ScriptDialog;
import org.astrogrid.desktop.modules.dialogs.SetDialog;
import org.astrogrid.desktop.modules.dialogs.StepDialog;
import org.astrogrid.desktop.modules.dialogs.ToolEditorInternal;
import org.astrogrid.desktop.modules.dialogs.UnsetDialog;
import org.astrogrid.desktop.modules.dialogs.WhileDialog;
import org.astrogrid.desktop.modules.dialogs.WorkflowDetailsDialog;
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
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Document;

import com.l2fprod.common.swing.StatusBar;


/**
 * @author Phil Nicolson pjn3@star.le.ac.uk
 *@modified nww smoothed up backgrounnd operaitons, added new tool editor, removed top tabs.
 *
 */
public class WorkflowBuilderImpl extends UIComponent implements org.astrogrid.acr.ui.WorkflowBuilder {
	
    /** save a workflow */
	protected final class SaveAction extends AbstractAction {
	    public SaveAction() {
	        super("Save", IconHelper.loadIcon("fileexport.png"));
	        this.putValue(SHORT_DESCRIPTION,"Save this workflow");
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
            this.setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
        	(new BackgroundOperation("Submitting Workflow"){

                protected Object construct() throws Exception {
                    return jobs.submitWorkflow(getModel().getWorkflow());
                }
                protected void doFinished(Object o) {
                    URI id = (URI)o;
                    ResultDialog rd = new ResultDialog(WorkflowBuilderImpl.this,"Workflow Submitted \nJob ID is \n" + id);
                    rd.show();
                    monitor.show(); // brings monitor to the front, if not already there.
                    monitor.refresh();                    
                }
            }).start();
        }
    }
    /** create a workflow */
    protected final class CreateAction extends AbstractAction {
        public CreateAction() {
            super("New",IconHelper.loadIcon("newfile_wiz.gif"));
            this.putValue(SHORT_DESCRIPTION,"Create a new workflow");
            this.setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
            //@todo factor out commonality with 'loadWorkflow' - or even better, replace with action listeners.
            try {
                getModel().setWorkflow(jobs.createWorkflow());
                getTree().expandAll(true);
            } catch (ServiceException ex) { // quite unlikely.
                showError("Failed to create workflow",ex);
            }
            tabbedPaneWF.setSelectedIndex(0);
    	    tabbedPaneWF.setEnabledAt(0, true);
    	    tabbedPaneWF.setEnabledAt(1, true);

            submitAction.setEnabled(true);
            saveAction.setEnabled(true);
        }
    }    

    protected final class CloseAction extends AbstractAction {
        /**
         * Commons Logger for this class
         */
        private final Log logger = LogFactory.getLog(CloseAction.class);

        public CloseAction() {
            super("Close",IconHelper.loadIcon("exit_small.png"));
            this.putValue(SHORT_DESCRIPTION,"Close the Workflow Builder");
        }

        public void actionPerformed(ActionEvent e) {
            hide();
            dispose();
        }
    }    

    // components this ui uses.
    protected final ToolEditorInternal toolEditor;
    protected final BrowserControl browser;
    protected final MyspaceInternal vos;
    protected final ResourceChooserInternal chooser;
    protected final JobMonitor monitor;
    private final ApplicationsInternal apps;
    private final JobsInternal jobs;
    
    // actions
    protected Action saveAction;
    protected Action loadAction;
    protected Action submitAction;
    protected Action createAction;
    protected Action closeAction;
    

    
    private JButton  findButton = null;
    private JEditorPane docTextArea;
    private JList list;
    private JLabel statusLabel, workflowStatusLabel, wastebinLabel = null;
    private JMenuBar jJMenuBar = null;
    private JMenu fileMenu = null;
    private JToolBar toolbar = null;    
    private DefaultListModel activityListModel= null;
    private WorkflowDnDTree tree = null;
    private JTabbedPane tabbedPaneWF;
    private JTextField  wfDocFindField;   
    private JScrollPane listView = null;
    private URL helpUrl;
    private int caretPos = 0;
    private Configuration conf;
    private Registry reg; 
    private StatusBar statusBar = null;
	
    /** 
     * production constructor 
     * @throws Exception
     * 
     * */
    public WorkflowBuilderImpl(ApplicationsInternal apps, JobsInternal jobs,JobMonitor monitor,  MyspaceInternal vos, BrowserControl browser,
            ToolEditorInternal toolEditor,UIInternal ui, HelpServer hs, Configuration conf,ResourceChooserInternal chooser, Registry reg) throws Exception {
        super(conf,hs,ui);
        this.browser = browser;
        this.vos = vos;
        this.chooser = chooser;
        this.monitor = monitor;
        this.apps = apps;
        this.jobs = jobs;
        this.toolEditor = toolEditor;
        this.conf = conf;
        this.reg = reg;
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
	            try{
	                Document doc = DomHelper.newDocument();
	                Marshaller.marshal(getModel().getWorkflow(),doc);
	                StringWriter sw = new StringWriter();
	                XMLUtils.PrettyDocumentToWriter(doc,sw);
	                docTextArea.setText(sw.toString());
	                docTextArea.setCaretPosition(0);
	            }
	            catch(Exception ex){
                    showError("Failed to display xml",ex);
	            }
	        }
	    });
			
		JPanel pane = getMainPanel();
		pane.add(tabbedPaneWF, BorderLayout.CENTER);
		pane.add(getToolbar(), BorderLayout.NORTH);
		
		submitAction = new SubmitAction();
        submitAction.setEnabled(false); // Initially false until a workflow is loaded/created
        loadAction = new LoadAction();
        createAction = new CreateAction(); 
        saveAction = new SaveAction();
        saveAction.setEnabled(false); // Initially false until a workflow is loaded/created
        closeAction = new CloseAction();
        
        toolbar.add(createAction);
        toolbar.add(loadAction);
        toolbar.add(saveAction);
        toolbar.add(submitAction);
        
        fileMenu.add(createAction);
        fileMenu.add(loadAction);
        fileMenu.add(saveAction);      
        fileMenu.add(new JSeparator());
        fileMenu.add(submitAction);
        fileMenu.add(new JSeparator());
        fileMenu.add(closeAction);
        
        pane.add(getActivityList(),BorderLayout.WEST);
        pane.add(getStatusBar(), java.awt.BorderLayout.SOUTH);
		this.setContentPane(pane);
		pack();
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
		}
		return fileMenu;
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
		}
		return toolbar;
	}
	/**
	 * This method initializes statusBar	
	 * 	
	 * @return com.l2fprod.common.swing.StatusBar	
	 */    
	private StatusBar getStatusBar() {
		if (statusBar == null) {
			statusBar = new StatusBar();
            statusBar.addZone("status",getStatusLabel(),"*");
            statusBar.addZone("wastebin",getWasteBin(),"29");
            statusBar.addZone("workflowStatus",getWorkflowStatus(),"27");
            statusBar.addZone("spacer",new JLabel(""),"0");
		}
		return statusBar;
	}
    private JLabel getStatusLabel() {
        if (statusLabel == null) {
            statusLabel = new JLabel();
            statusLabel.setText("");
            statusLabel.setForeground(java.awt.SystemColor.activeCaption);
            statusLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
        }
        return statusLabel;
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
			wastebinLabel.setIcon(IconHelper.loadIcon("wastebin.gif"));
			wastebinLabel.setToolTipText("Wastebin");
			DropTarget dt = new DropTarget(wastebinLabel, new WastebinDropListener(tree));
		}
		return wastebinLabel;
	}
  
	/**
	 * This method returns the activity list
	 * to do: cahnge layout to grid of buttons or an improved layout...	
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
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setDragEnabled(true);
			list.setCellRenderer(new ActivityListRenderer());
			//list.setTransferHandler(new DefaultActivityTransferHandler());
			listView = new JScrollPane(list);
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
           tree =  new WorkflowDnDTree(apps);
           tree.setModel(getModel());
           tree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 ) {
                	setStatusMessage("");
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
                    Object o = node.getUserObject();
                    if (o instanceof String) {
                        getScriptDialog().setText(tree.getLastSelectedPathComponent().toString());
                        DefaultMutableTreeNode scriptNode = (DefaultMutableTreeNode)node.getParent();
                        if (scriptNode.getUserObject() instanceof Script) { // should be as String is script body
                        	getScriptDialog().setDescription(((Script)scriptNode.getUserObject()).getDescription());
                        }                        
                        getScriptDialog().setVisible(true);
                        String edit = getScriptDialog().getEditedScript();
                        String desc = getScriptDialog().getDescription();
                        if (edit != null && desc != null) {
                            node.setUserObject(edit);
                            getModel().nodeChanged(node); // script body may have changed
                            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)node.getParent();
                            Script sc = (Script)parentNode.getUserObject();
                            sc.setBody(edit);
                            sc.setDescription(desc);
                            parentNode.setUserObject(sc);
                            getModel().nodeChanged(parentNode); // script desc may also have changed, attribute of parent node
                        }
                    } 
                    if (o instanceof Script) {
                        getScriptDialog().setText(((Script)o).getBody());
                        getScriptDialog().setDescription(((Script)o).getDescription());
                        getScriptDialog().setVisible(true);
                        String edit = getScriptDialog().getEditedScript();
                        String desc = getScriptDialog().getDescription();
                        if (edit != null && desc != null) {
                            Script sc = (Script)node.getUserObject();
                            sc.setBody(edit);
                            sc.setDescription(desc);
                            node.setUserObject(sc);
                            getModel().nodeChanged(node); // Script desc may have changed
                            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)node.getFirstChild();
                            childNode.setUserObject(edit);
                            getModel().nodeChanged(childNode); // May also have changed body
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
                       }
                        } catch (ACRException e) {
                            showError("Failed to edit tool",e);
                        }
                    } 
                    else if (o instanceof Step) {
                    	Step newStep = getStepDialog((Step)o).getEditedStep();
                    	stepDialog = null;
                    try {
                    	if (newStep != null) {
                    		RegistryChooserDialog registryChooser = getRegistryChooserDialog();
                    		registryChooser.setVisible(true);
                    		ResourceInformation[] resourceInformation = registryChooser.getSelectedResources();
                    		if (resourceInformation != null && resourceInformation.length >0) {
                    			ResourceInformation res = resourceInformation[0]; // only returning single entries
                    			ApplicationInformation applicationInfo = apps.getApplicationInformation(res.getId());
                    			Tool t = apps.createTemplateTool("default",applicationInfo);
                    			if (t != null) {
                    				Tool newTool = toolEditor.editTool(t,WorkflowBuilderImpl.this); 
                    				newStep.setTool(newTool);
                    				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode();
                    				childNode.setUserObject(newTool);
                    				node.add(childNode);
                    				getModel().nodeChanged(childNode);
                    			}
                    		}
                    	node.setUserObject(newStep);
                    	getModel().nodeChanged(node);
                    	registryChooser = null;                    	
                    	}                    	
                    
                    } catch (Exception e) { //(ACRException e) {
                        showError("Failed to edit tool",e);
                    }
                    }
                    else if (o instanceof If) {
                    	If newIf = getIfDialog((If)o).getEditedIf();
                    	if (newIf != null) {
                    		node.setUserObject(newIf);
                    		getModel().nodeChanged(node);
                    	}
                    	ifDialog = null;
                    }
                    else if (o instanceof Set) {
                    	Set newSet = getSetDialog((Set)o).getEditedSet();
                    	if (newSet != null) {
                        	node.setUserObject(newSet);
                        	getModel().nodeChanged(node);
                    	} 
                    	setDialog = null;
                    }
                    else if (o instanceof Unset) {
                    	Unset newUnset = getUnsetDialog((Unset)o).getEditedUnset();
                    	if (newUnset != null) {
                    		node.setUserObject(newUnset);
                    		getModel().nodeChanged(node);
                    	}                    	
                    	unsetDialog = null;
                    }
                    else if (o instanceof For) {
                    	For newFor = getForDialog((For)o).getEditedFor();
                    	if (newFor != null) {
                    		node.setUserObject(newFor);
                    		getModel().nodeChanged(node);
                    	}                    	
                    	forDialog = null;
                    }
                    else if (o instanceof Parfor) {
                    	Parfor newParfor = getParforDialog((Parfor)o).getEditedParfor();
                    	if (newParfor != null) {
                        	node.setUserObject(newParfor);
                        	getModel().nodeChanged(node);
                    	}
                    	parforDialog = null;
                    }                    
                    else if (o instanceof While) {
                    	While newWhile = getWhileDialog((While)o).getEditableWhile();
                    	if (newWhile != null) {
                        	node.setUserObject(newWhile);
                        	getModel().nodeChanged(node);
                    	}
                    	whileDialog = null;
                    }                    
                    else if (o instanceof Workflow) {
                    	Workflow newWorkflow = getWorkflowDetailsDialog((Workflow)o).getEditableWorkflow();
                    	if (newWorkflow != null) {
                    		node.setUserObject(newWorkflow);
                    		getModel().nodeChanged(node);                    		
                    	}
                    	tree.expandAll(true); // nodeChanged will focus to root, so expand again
                    	workflowDialog = null;
                    }
                    else if (o instanceof Sequence || 
                    		 o instanceof Flow || 
							 o instanceof Scope ||
							 o instanceof Else ||
							 o instanceof Then ) {
                    	setStatusMessage("No inputs required for this activity");
                    }                    
                }
            }
           });                   
        }
        return tree;
    }
 
    private ForDialog forDialog;
    private ForDialog getForDialog(For f) {
        if (forDialog == null) {
            forDialog = new ForDialog(this, f);
        }
        return forDialog;
    }
    
    private IfDialog ifDialog;
    private IfDialog getIfDialog(If i) {
        if (ifDialog == null) {
            ifDialog = new IfDialog(this, i);
        }
        return ifDialog;
    }    
    
    private ParforDialog parforDialog;
    private ParforDialog getParforDialog(Parfor p) {
        if (parforDialog == null) {
            parforDialog = new ParforDialog(this, p);
        }
        return parforDialog;
    }    

    private RegistryChooserDialog registryDialog;
    private RegistryChooserDialog getRegistryChooserDialog() {
        if (registryDialog == null) {
        	registryDialog = new RegistryChooserDialog(WorkflowBuilderImpl.this, conf, help, ui, reg);
        	registryDialog.setMultipleResources(false);
        }
        return registryDialog;
    }
    
    private ScriptDialog scriptDialog;
    private ScriptDialog getScriptDialog() {
        if (scriptDialog == null) {
            scriptDialog = new ScriptDialog(this);
        }
        return scriptDialog;
    }    
    
    private SetDialog setDialog;
    private SetDialog getSetDialog(Set s) {
        if (setDialog == null) {
            setDialog = new SetDialog(this, s);
        }
        return setDialog;
    }
    
    private StepDialog stepDialog;
    private StepDialog getStepDialog(Step s) {
        if (stepDialog == null) {
            stepDialog = new StepDialog(this, s);
        }
        return stepDialog;
    }    
    
    private UnsetDialog unsetDialog;
    private UnsetDialog getUnsetDialog(Unset u) {
        if (unsetDialog == null) {
            unsetDialog = new UnsetDialog(this, u);
        }
        return unsetDialog;
    }  
    
    private WhileDialog whileDialog;
    private WhileDialog getWhileDialog(While w) {
        if (whileDialog == null) {
            whileDialog = new WhileDialog(this, w);
        }
        return whileDialog;
    }    
    
    private WorkflowDetailsDialog workflowDialog;
    private WorkflowDetailsDialog getWorkflowDetailsDialog(Workflow w) {
        if (workflowDialog == null) {
        	workflowDialog = new WorkflowDetailsDialog(this, w);
        }
        return workflowDialog;
    }    
    
	    

	private JPanel getTabbedTreePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(getTree());
		panel.add(scrollPane);
		panel.setBorder(BorderFactory.createTitledBorder("Tree view"));
		panel.setPreferredSize(new Dimension(800,300));		
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
     * @see org.astrogrid.acr.system.UI#setStatusMessage(java.lang.String)
     */
    public void setStatusMessage(final String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getStatusLabel().setText(msg);
            }
        });
    }  
} 
