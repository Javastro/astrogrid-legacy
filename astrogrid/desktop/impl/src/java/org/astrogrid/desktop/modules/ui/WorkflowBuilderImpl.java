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
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.HelpServer;
import org.astrogrid.acr.ui.JobMonitor;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.JobsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.BasicInfoPanel;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.dialogs.ScriptDialog;
import org.astrogrid.desktop.modules.dialogs.ScriptPanel;
import org.astrogrid.desktop.modules.dialogs.StepPanel;
import org.astrogrid.desktop.modules.dialogs.WorkflowDetailsDialog;
import org.astrogrid.desktop.modules.dialogs.editors.AbstractToolEditorPanel;
import org.astrogrid.desktop.modules.dialogs.editors.CompositeToolEditorPanel;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.workflowBuilder.dragAndDrop.DefaultActivityTransferHandler;
import org.astrogrid.desktop.modules.workflowBuilder.dragAndDrop.DefaultListTransferHandler;
import org.astrogrid.desktop.modules.workflowBuilder.dragAndDrop.WorkflowDnDTree;
import org.astrogrid.desktop.modules.workflowBuilder.models.WorkflowTreeModel;
import org.astrogrid.desktop.modules.workflowBuilder.renderers.ActivityListRenderer;
import org.astrogrid.util.DomHelper;
import org.astrogrid.workflow.beans.v1.For;
import org.astrogrid.workflow.beans.v1.If;
import org.astrogrid.workflow.beans.v1.Parfor;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Unset;
import org.astrogrid.workflow.beans.v1.While;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.picocontainer.PicoContainer;
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

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


/**
 * @author Phil Nicolson pjn3@star.le.ac.uk
 *@modified nww smoothed up backgrounnd operaitons, added new tool editor, removed top tabs.
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
	                     workflow.marshal(writer);	                
	                     writer.close();
	                     wfFileField.setText(u.getPath());        	             
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
                            // NWW: bit concerned about the zapping of member variables here.
                            // think it'd probably be safer to have some kind of 'model' object, with a get/setWorkflow method
                            // that clients could register listeners with - so that they're notified with the workflow model changes.
                            // instead, this method has to remember what needs to be poked,
                            WorkflowBuilderImpl.this.workflow = (Workflow)o;
                           // workflowTreeModel = new WorkflowTreeModel(workflow);
                           // workflowTreeModel.addTreeModelListener(new TreeModelListener(){
                                                     							
                            //tree.setModel(workflowTreeModel);
                            tree.setModel(new DefaultTreeModel(tree.createTree(workflow)));
                            //TODO: expandAll should work when tree model completed (eg getChildCount)
                            //tree.setCellRenderer(new WorkflowTreeCellRenderer());
                            //tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
                            tree.addTreeSelectionListener(new WorkflowTreeSelectionListener());
                            tree.addMouseListener(new MouseAdapter() {
                            	public void mouseClicked(MouseEvent evt) {
                            		if (evt.getClickCount() == 2 ) {
                            			DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
                            			if (node.getUserObject() instanceof String ) {
                            				ScriptDialog sd = new ScriptDialog(null, tree.getLastSelectedPathComponent());
                            				sd.show();
                            			}
                            		}
                            	}
                            });                            
                            // show workflow panel and enable tabs for tree and doc view
                            tabbedPaneWF.setSelectedIndex(0);
                    	    tabbedPaneWF.setEnabledAt(0, true);
                    	    tabbedPaneWF.setEnabledAt(1, true);
                    	    // set labels to show workflow details and show panel
                    	    wfNameField.setText(workflow.getName());
                    	    descPane.setText(workflow.getDescription());
                    	    wfFileField.setText(u.getPath());
                    		tabbedPaneDetails.setSelectedIndex(0);
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
                    return jobs.submitWorkflow(workflow);
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
        	workflow = editWF( jobs.createWorkflow());
            } catch (ServiceException ex) { // quite unlikely.
                showError("Failed to create workflow",ex);
            }
        	tabbedPaneWF.setEnabledAt(0, true);
        	tabbedPaneWF.setEnabledAt(1, true);
        	tabbedPaneWF.setSelectedIndex(0);
        	tree.setModel(new DefaultTreeModel(tree.createTree(workflow)));
            //workflowTreeModel = new WorkflowTreeModel(workflow);
            //tree.setModel(workflowTreeModel);
            //tree.setCellRenderer(new WorkflowTreeCellRenderer());
            //tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            tree.addTreeSelectionListener(new WorkflowTreeSelectionListener());
            tree.addMouseListener(new MouseAdapter() {
            	public void mouseClicked(MouseEvent evt) {
            		if (evt.getClickCount() == 2 ) {
            			DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            			if (node.getUserObject() instanceof String ) {
            				ScriptDialog sd = new ScriptDialog(null, tree.getLastSelectedPathComponent());
            				sd.show();
            			}
            		}
            	}
            }); 
            tabbedPaneWF.setSelectedIndex(0);
    	    tabbedPaneWF.setEnabledAt(0, true);
    	    tabbedPaneWF.setEnabledAt(1, true);
    	    tabbedPaneDetails.setSelectedIndex(0);
    	    wfNameField.setText(workflow.getName());
    	    descPane.setText(workflow.getDescription());
    	    wfFileField.setText("");
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
    protected final PicoContainer pico;
    protected final BrowserControl browser;
    protected final MyspaceInternal vos;
    protected final ResourceChooserInternal chooser;
    protected final JobMonitor monitor;
    private final ApplicationsInternal apps;
    private final JobsInternal jobs;
    protected WorkflowTreeModel workflowTreeModel = null;
    
    // actions
    protected Action saveAction;
    protected Action loadAction;
    protected Action submitAction;
    protected Action createAction;
    protected Action closeAction;
    
    //@todo - check whether this is needed - seems a bit dodgy having this hanging around - shouldn't it be hidden in a model?
    private Workflow workflow = null;
    
    private JButton  findButton = null;
    private JEditorPane htmlWelcomePane, htmlActivityPane, htmlTaskPane, descPane, docTextArea;
    private JList list;
    private JMenuBar jJMenuBar = null;
    private JMenu fileMenu = null;
    private JPanel tabWf = null;
    private JToolBar toolbar = null;    
    private DefaultListModel activityListModel= null;
    private WorkflowDnDTree tree = null;
    private JTabbedPane tabbedPaneWF,tabbedPaneDetails;
    private JTextField wfNameField, wfFileField, wfDocFindField;   
    private JScrollPane listView,  scrollPane,  activityDetailsPane, taskDetailsPane, welcomePane= null;
    private URL helpUrl;
    private int caretPos = 0;
    
    private String helpLocation = "/org/astrogrid/desktop/modules/workflowBuilder/helpText/";
    
 
    	
	
    /** 
     * production constructor 
     * @throws Exception
     * 
     * */
    public WorkflowBuilderImpl(PicoContainer pico,ApplicationsInternal apps, JobsInternal jobs,JobMonitor monitor,  MyspaceInternal vos, BrowserControl browser, UIInternal ui, HelpServer hs, Configuration conf,ResourceChooserInternal chooser) throws Exception {
        super(conf,hs,ui);
        this.browser = browser;
        this.vos = vos;
        this.chooser = chooser;
        this.monitor = monitor;
        this.apps = apps;
        this.jobs = jobs;
        this.pico = pico;
        initialize();
        
    }   
    
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
        try {
		workflow =   jobs.createWorkflow();
        } catch (ServiceException e) {
            showError("Faild to initialize workflow",e);
        }
		this.setTitle("Workflow Builder");
		this.setJMenuBar(getJJMenuBar());	
	
		JPanel rightPanel = createVericalBoxPanel();
	    

		
		//right side
		tabbedPaneDetails = new JTabbedPane();
        // most of the info on these panes is redundant - already in the tree. need to check whether any of these are worth keeping.
		tabbedPaneDetails.addTab("Workflow details", null, getTabbedWorkflowDetailsPanel(), "Workflow details");
		tabbedPaneDetails.setMnemonicAt(0,KeyEvent.VK_1);
		tabbedPaneDetails.addTab("Task details", null, getTabbedTaskDetailsPanel(), "Task details");
		tabbedPaneDetails.setMnemonicAt(1,KeyEvent.VK_2);
		tabbedPaneDetails.addTab("Activity details", null, getTabbedActivityDetailsPanel(), "Activity details");
		tabbedPaneDetails.setMnemonicAt(2,KeyEvent.VK_3);        
		tabbedPaneDetails.addTab("Welcome", null, getTabbedWelcomeDetailsPanel(), "Welcome details");
		tabbedPaneDetails.setMnemonicAt(3,KeyEvent.VK_4);
		tabbedPaneDetails.setSelectedIndex(3);
		
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
	                Marshaller.marshal(workflow,doc);
	                StringWriter sw = new StringWriter();
	                XMLUtils.PrettyDocumentToWriter(doc,sw);
	                docTextArea.setText(sw.toString());
	                docTextArea.setCaretPosition(0);
	            }
	            catch(Exception ex){
	            	logger.error(ex.getMessage());
	            }
	        }
	    });
		
		JSplitPane splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabbedPaneDetails, tabbedPaneWF);
		splitPane2.setOneTouchExpandable(true);
		rightPanel.add(splitPane2);	


		JPanel pane = getJContentPane();
		pane.add(rightPanel, BorderLayout.CENTER);
		pane.add(getToolbar(), BorderLayout.NORTH);
		
		addMenuOptions();
        pane.add(getActivityList(),BorderLayout.WEST);
		this.setContentPane(pane);
		pack();

	}
	
	/**
	 * Create a vertical box panel
	 * @return JPanel
	 */
	protected JPanel createVericalBoxPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		p.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		return p;
	}
	/**
	 * Create a horizontal box panel
	 * @return JPanel
	 */
	protected JPanel createHorizontalBoxPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		p.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		return p;
	}	
	/**
	 * This method initializes jPanel for component	
	 * 	
	 * @return javax.swing.JPanel	
	 */	
	protected JPanel createPanelForComponent(JComponent comp, String title){
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(comp, BorderLayout.CENTER);
		if (title != null) {
			panel.setBorder(BorderFactory.createTitledBorder(title));
		}
		return panel;
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
			list.addMouseListener(new MouseAdapter(){
		          public void mouseClicked(MouseEvent event)
		          {
		          	tabbedPaneDetails.setSelectedIndex(2);
		          	htmlActivityPane = readHelpFile(helpLocation + "help_"+list.getSelectedValue()+".html");
		          	activityDetailsPane = new JScrollPane(htmlActivityPane);
		          	tabbedPaneDetails.setComponentAt(2, activityDetailsPane);
		          };		          
			});
			listView = new JScrollPane(list);
		}
		return listView;
    }
	



	
	
   
	    
	private JPanel getTabbedTreePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		tree = new WorkflowDnDTree(apps);
		tree.setModel(null);
		scrollPane = new JScrollPane(tree);
		panel.add(scrollPane);			
		panel.setBorder(BorderFactory.createTitledBorder("Tree view"));
		panel.setPreferredSize(new Dimension(800,200));		
		return panel;
	}		
	
	
	private JScrollPane getTabbedWorkflowDetailsPanel() {
		if (tabWf == null) {
			tabWf = new JPanel();								
	    	JLabel label1 = new JLabel("Workflow name: ", JLabel.TRAILING);
	    	wfNameField = new JTextField(25);
	    	wfNameField.setEditable(false);
	    	wfNameField.setText("No workflow selected");
	    	JLabel label2 = new JLabel("Workflow description: ", JLabel.TRAILING);
	    	descPane = new JEditorPane();
	    	descPane.setEditable(false);
	    	descPane.setText("--");
	    	descPane.setCaretPosition(0);
	    	descPane.setPreferredSize(new Dimension(400,80));
	    	JScrollPane pane = new JScrollPane(descPane);
	    	pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    	pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    	JLabel label3 = new JLabel("File location: ", JLabel.TRAILING);
	    	wfFileField = new JTextField(25);
	    	wfFileField.setEditable(false);
	    	wfFileField.setText("--");
	    	wfFileField.setToolTipText("Location of current workflow");
			JPanel p = new JPanel(new SpringLayout());
			p.add(label1);
			p.add(wfNameField);
			p.add(label2);
			p.add(pane);
			p.add(label3);
			p.add(wfFileField);		
			SpringLayoutHelper.makeCompactGrid(p, 3,2,2,2,10,3);
			//p.setPreferredSize(new Dimension(500,100));
			tabWf.add(p);
		}
		JScrollPane jsp = new JScrollPane(tabWf);
		return jsp;
		//return tabWf;
	}
	
	private JScrollPane getTabbedTaskDetailsPanel() {
		if (taskDetailsPane == null) {
			htmlTaskPane = readHelpFile(helpLocation + "task.html");        
			taskDetailsPane = new JScrollPane(htmlTaskPane);
		}
		return taskDetailsPane;
	}
	
	private JScrollPane getTabbedActivityDetailsPanel() {
		if (activityDetailsPane == null) {								
	        htmlActivityPane = readHelpFile(helpLocation + "activity.html");        
			activityDetailsPane = new JScrollPane(htmlActivityPane);
		}
		return activityDetailsPane;
	}
	
	private JScrollPane getTabbedWelcomeDetailsPanel() {
		if (welcomePane == null) {	        			
	        htmlWelcomePane = readHelpFile(helpLocation + "welcome.html");
	        htmlWelcomePane.setPreferredSize(new Dimension(200,225));
			welcomePane = new JScrollPane(htmlWelcomePane);
		}
		return welcomePane;
	}
	
	private JEditorPane readHelpFile(String fileName) {	
		JEditorPane pane = new JEditorPane();
		try {			
			helpUrl = this.getClass().getResource(fileName);
			pane.setPage(helpUrl); 
			pane.setEditable(false);
	    } 
	    catch(Exception ex) {
	    	logger.error("Error reading help file " + fileName + " : " + ex.getMessage());
	    	pane.setText("Unable to read help file");	    
	    }
	    return pane;
	}
	
	private JPanel getTabbedDocPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		docTextArea = new JEditorPane();		
		docTextArea.setEditable(false);	
		scrollPane = new JScrollPane(docTextArea);
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

      /*
       * Add various actions to toolbar and wfMenu
       */
      private void addMenuOptions() {
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
      }
      

      private AbstractToolEditorPanel taskPanel;
    private AbstractToolEditorPanel getToolEditorPanel() {
        if (taskPanel == null) {
            taskPanel = new CompositeToolEditorPanel(this,pico);
        }
        return taskPanel;
    }
     
      /** 
       * Custom listener - dictates which panels are displayed and their contents
       */    
      public class WorkflowTreeSelectionListener implements TreeSelectionListener {
    	public void valueChanged(TreeSelectionEvent event){
    		Object ob = tree.getLastSelectedPathComponent();
    		if (ob == null) 
    			return;
    		if (ob instanceof DefaultMutableTreeNode)
    			ob = ((DefaultMutableTreeNode)ob).getUserObject();
    		if (ob instanceof Step){
    			Step s = (Step)ob;
    			if (s.getTool() != null) {
                    try {
                    ApplicationInformation info = apps.getInfoForTool(s.getTool());
                    getToolEditorPanel().getToolModel().populate(s.getTool(),info);
                    } catch (ACRException e) {
                        showError("Unrecognized application",e);
                    }
                    tabbedPaneDetails.setComponentAt(1, getToolEditorPanel());
                    tabbedPaneDetails.setSelectedIndex(1);
    			}    			
                StepPanel stepPanel = new StepPanel(s);
	          	activityDetailsPane = new JScrollPane(stepPanel);
	          	tabbedPaneDetails.setComponentAt(2, activityDetailsPane);
                tabbedPaneDetails.setSelectedIndex(2);
    		}
    		else if (ob instanceof Script) {
    			Script s = (Script)ob;
                ScriptPanel scriptPanel = new ScriptPanel(s);
	          	activityDetailsPane = new JScrollPane(scriptPanel);
	          	tabbedPaneDetails.setComponentAt(2, activityDetailsPane);
    			tabbedPaneDetails.setSelectedIndex(2);
    		}    		
    		else if (ob instanceof Set) {
    			Set s = (Set)ob;
    			String[] values = new String[4];
    			values[0] = "Variable: ";
    			values[1] = s.getVar();
    			values[2] = "Value: ";
    			values[3] = s.getValue();
    			displayBasicInfo(values);
    		}
    		else if (ob instanceof Unset) {
    			Unset u = (Unset)ob;
    			String[] values = new String[2];
    			values[0] = "Variable: ";
    			values[1] = u.getVar();
    			displayBasicInfo(values);   			
    		}
    		else if (ob instanceof For) {
    			For f = (For)ob;
    			String[] values = new String[4];
    			values[0] = "Variable: ";
    			values[1] = f.getVar();
    			values[2] = "Items: ";
    			values[3] = f.getItems();
    			displayBasicInfo(values);
    		}
    		else if (ob instanceof Parfor) {
    			Parfor p = (Parfor)ob;
    			String[] values = new String[4];
    			values[0] = "Variable: ";
    			values[1] = p.getVar();
    			values[2] = "Items: ";
    			values[3] = p.getItems();
    			displayBasicInfo(values);    				
    		}    		
    		else if (ob instanceof If) {
    			If i = (If)ob;
    			String[] values = new String[2];
    			values[0] = "Test: ";
    			values[1] = i.getTest();
    			displayBasicInfo(values);
    		}
    		else if (ob instanceof While) {
    			While w = (While)ob;
    			String[] values = new String[2];
    			values[0] = "Test: ";
    			values[1] = w.getTest();
    			displayBasicInfo(values);   			
    		}     		
    		else {
    			return;
    			// An activity with nothing to display
    		}
    		tabbedPaneDetails.setSelectedIndex(2);
    	}
    } 
      /**
       * 
       * @param values an array of label/value pairs
       */
      private void displayBasicInfo(String[] values) {
		BasicInfoPanel basicInfo  = new BasicInfoPanel(values);      	
      	tabbedPaneDetails.setComponentAt(2, basicInfo);
        tabbedPaneDetails.setSelectedIndex(2);      	
      }
/* NWW: removed for now - will replace with query editor, and friends.
      public class PopulateTaskPanelListListener extends MouseAdapter {
          public void mouseClicked(MouseEvent event)
          {  	          
	          ApplicationDescription description = null;
	          try { 		          	
	  	          taskListSelection = (JList)event.getSource();	  	          	  	          
                  (new BackgroundOperation("Reading task details from registry") {
                    protected Object construct() throws Exception {
                      boolean intFound = false; 
                      ApplicationInformation td = (TaskDetails)taskListSelection.getSelectedValue();
	                  ApplicationRegistry applRegistry = getAstrogrid().getWorkflowManager().getToolRegistry();      	       
	                  ApplicationDescription description = applRegistry.getDescriptionFor( td.getTaskName() );		                  
	  			      InterfacesType intTypes = description.getInterfaces() ;
			          Interface intf = null ;			      
				      for (int j=0 ; j < intTypes.get_interfaceCount() ; j++) 
				      {
				        if ( intTypes.get_interface(j).getName().equalsIgnoreCase(td.getInterfaceName())) 
					    {
					      intf = intTypes.get_interface(j) ;
						  intFound = true ;
					      break ;
					    }
				      }
				      Tool t = null;
				      if (intFound)
				      {
					    t = description.createToolFromInterface( intf );
			          }
				      else
			          {				  	
					    // an interface should always be found - in case not use default interface
					    t = description.createToolFromDefaultInterface();
				      }	
	                  TaskInfoPanel taskPanel = new TaskInfoPanel(t, apps);
                      taskDetailsPane = new JScrollPane(taskPanel);
                      tabbedPaneDetails.setComponentAt(1, taskDetailsPane);
//	                       ToolInfoDialog toolInfo = new ToolInfoDialog(description, null);
//	                       toolInfo.show();
//	                  }
//	                    *************************      	              
//	      	          test of TaskEditorDialog
//	                    *************************      	              
//	                  if (event.getButton() == 3) { 
//	                    logger.info("TaskEditorDialog");
//	                  	int i =0;
//	               	    for (; i<= appDescSum.length; i++) {
//	               	       if (list.getSelectedValue().equals(appDescSum[i].getUIName()))
//	               	           break;
//	               	    }                    
//	   	                ApplicationRegistry applRegistry = getAstrogrid().getWorkflowManager().getToolRegistry();      	       
//	                    ApplicationDescription description = applRegistry.getDescriptionFor( appDescSum[i].getName() );
//	                    logger.info("creating tool");
//	    	            Tool t = editTool(description);
//	    	            if (t != null)
//	    	                  logger.info("tool created: " + t.getName());      	          	      	          	    	                  
//	    	          }
//	
    	            
                      return null;
                      }
                    }).start();
                tabbedPaneDetails.setSelectedIndex(1);
	            }
	            catch(Exception ex) {
	                logger.error("Error loading task details for task: " + description.getName() + ", " + ex.getMessage() );
	                JOptionPane.showMessageDialog(null,"Error loading task details for task: " + description.getName(),"Errorloading task details",JOptionPane.ERROR_MESSAGE);        	
	            }		      	      			      	      		          		          	            	
          }      	
      }
            */
} 
