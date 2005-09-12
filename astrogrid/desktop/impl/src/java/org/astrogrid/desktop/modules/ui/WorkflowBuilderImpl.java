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

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.HelpServer;
import org.astrogrid.acr.ui.JobMonitor;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.JobsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.dialogs.ScriptDialog;
import org.astrogrid.desktop.modules.dialogs.ToolEditorInternal;
import org.astrogrid.desktop.modules.dialogs.WorkflowDetailsDialog; 
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.workflowBuilder.dragAndDrop.DefaultActivityTransferHandler;
import org.astrogrid.desktop.modules.workflowBuilder.dragAndDrop.WorkflowDnDTree;
import org.astrogrid.desktop.modules.workflowBuilder.models.SimpleWorkflowTreeModel;
import org.astrogrid.desktop.modules.workflowBuilder.renderers.ActivityListRenderer;
import org.astrogrid.util.DomHelper;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Document;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * @author Phil Nicolson pjn3@star.le.ac.uk
 *@modified nww smoothed up backgrounnd operaitons, added new tool editor, removed top tabs.
 *@todo add  editor dialogues for other kinds of activity.
 *@todo finish drag-n-drop - ensure changes are made within the workflow document objects.
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
                    		submitAction.setEnabled(true);
                    		saveAction.setEnabled(true);
                    		caretPos = 0;
                    		wfDocFindField.setText("");	        	            	                   
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
    
    private String helpLocation = "/org/astrogrid/desktop/modules/workflowBuilder/helpText/";
    
 
    	
	
    /** 
     * production constructor 
     * @throws Exception
     * 
     * */
    public WorkflowBuilderImpl(ApplicationsInternal apps, JobsInternal jobs,JobMonitor monitor,  MyspaceInternal vos, BrowserControl browser,
            ToolEditorInternal toolEditor,UIInternal ui, HelpServer hs, Configuration conf,ResourceChooserInternal chooser) throws Exception {
        super(conf,hs,ui);
        this.browser = browser;
        this.vos = vos;
        this.chooser = chooser;
        this.monitor = monitor;
        this.apps = apps;
        this.jobs = jobs;
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
			list.setTransferHandler(new DefaultActivityTransferHandler());
			listView = new JScrollPane(list);
		}
		return listView;
    }
	



    private SimpleWorkflowTreeModel model;
    
    /** build the model that manages the workflow for the JTree */
    private SimpleWorkflowTreeModel getModel() {
        if (model == null) {
            model = new SimpleWorkflowTreeModel();
        }
        return model;
    }
	
    /** build / access the tree */
    private WorkflowDnDTree getTree() {
        if (tree == null) {
           tree =  new WorkflowDnDTree(apps);
           tree.setModel(getModel());
           tree.addMouseListener(new MouseAdapter() { //@todo add other cases to this - each kind of activiity should pop up some kind of dialogue
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 ) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
                    Object o = node.getUserObject();
                    if (o instanceof String ) {
                        getScriptDialog().setText(tree.getLastSelectedPathComponent().toString());
                        getScriptDialog().setVisible(true);
                        String edit = getScriptDialog().getEditedScript();
                        if (edit != null) {
                            node.setUserObject(edit);
                            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)node.getParent();
                            Script sc = (Script)parentNode.getUserObject();
                            sc.setBody(edit);
                            getModel().nodeChanged(node);                            
                        }
                    } else if (o instanceof Tool ) {
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
                    } // @add dialogues for other kinds of activity.
                }
            }
           });                   
        }
        return tree;
    }
   
    private ScriptDialog scriptDialog;
    private ScriptDialog getScriptDialog() {
        if (scriptDialog == null) {
            scriptDialog = new ScriptDialog(this);
        }
        return scriptDialog;
    }
    
	    
	private JPanel getTabbedTreePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(getTree());
		panel.add(scrollPane);			
		panel.setBorder(BorderFactory.createTitledBorder("Tree view"));
		panel.setPreferredSize(new Dimension(800,200));		
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
     * prompt user in some way to fill in fields in the template 
     * returns null to indicate canceled operation.
     * */
    protected Workflow editWF(Workflow wf) {
    	WorkflowDetailsDialog dialog = new WorkflowDetailsDialog(wf);
    	dialog.setVisible(true);
        return dialog.getWorkflow();
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

     
 

} 
