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
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.acr.astrogrid.Portal;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.HelpServer;
import org.astrogrid.acr.system.UI;
import org.astrogrid.acr.ui.JobMonitor;
import org.astrogrid.acr.ui.WorkflowBuilderLauncher;
import org.astrogrid.applications.beans.v1.Interface;
import org.astrogrid.applications.beans.v1.InterfacesType;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.community.beans.v1.Group;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.BasicInfoPanel;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserDialog;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.dialogs.ScriptPanel;
import org.astrogrid.desktop.modules.dialogs.StepPanel;
import org.astrogrid.desktop.modules.dialogs.TaskInfoPanel;
import org.astrogrid.desktop.modules.dialogs.WorkflowDetailsDialog;
import org.astrogrid.desktop.modules.system.WorkflowTreeModel;
import org.astrogrid.desktop.modules.system.XmlTreeModel;
import org.astrogrid.desktop.modules.ui.AbstractVospaceBrowser.CurrentNodeManager;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationDescriptionSummary;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.portal.workflow.intf.WorkflowBuilder;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.portal.workflow.intf.WorkflowManager;
import org.astrogrid.query.sql.Sql2Adql;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.scripting.Toolbox;
import org.astrogrid.store.Ivorn;
import org.astrogrid.util.DomHelper;
import org.astrogrid.workflow.beans.v1.Flow;
import org.astrogrid.workflow.beans.v1.For;
import org.astrogrid.workflow.beans.v1.If;
import org.astrogrid.workflow.beans.v1.Parfor;
import org.astrogrid.workflow.beans.v1.Scope;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Unset;
import org.astrogrid.workflow.beans.v1.While;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * @author Phil Nicolson pjn3@star.le.ac.uk
 *
 */
public class WorkflowBuilderLauncherImpl extends UIComponent implements WorkflowBuilderLauncher,  UserLoginListener {
	
    /** save a workflow */
	protected final class SaveAction extends AbstractAction {
	    public SaveAction() {
	        super("Save", IconHelper.loadIcon("icon_Save.gif"));
	        this.putValue(SHORT_DESCRIPTION,"Save this workflow transcript");
	        this.setEnabled(true);            
	    }
        public void actionPerformed(ActionEvent e) {
        	try{
                (new BackgroundOperation("Saving Workflow") {
                    protected Object construct() throws Exception {        		
                    URI u = ResourceChooserDialog.chooseResource(vos,"Save a copy of this workflow",true);
	                if (u != null && u.getScheme() != null) {
	                    Writer writer = null;
	                
	                    if (u.getScheme().equals("ivo")) {
	                        Ivorn ivo = new Ivorn(u.toString());
	                        FileManagerNode target = null;
	                        if (vos.exists(ivo)) {
	                            target = vos.node(ivo);
	                        } else {
	                            target = vos.createFile(ivo);
	                        }
	                        OutputStream os = target.writeContent();
	                        writer = new OutputStreamWriter(os);               
	                     } 
	                     if (u.getScheme().equals("file")) {
	                         File f = new File(u);
	                         writer = new FileWriter(f);
	                     }
	                     workflow.marshal(writer);	                
	                     writer.close();
        	             }
                         return null;
                     }
                }).start();	            
        	}
	        catch(Exception ex) {
	            logger.error("Error saving workflow: " + ex.getMessage());
	            JOptionPane.showMessageDialog(null,ex,"Error",JOptionPane.ERROR_MESSAGE);
	        }	            
	    }
	}
    /** load a workflow */
	protected final class LoadAction extends AbstractAction {
	    public LoadAction() {
	        super("Load", IconHelper.loadIcon("import_log.gif"));
	        this.putValue(SHORT_DESCRIPTION,"Load a workflow transcript");
	        this.setEnabled(true);            
	    }

	        public void actionPerformed(ActionEvent e) {
	        	try{
	                (new BackgroundOperation("Loading Workflow") {
	                    protected Object construct() throws Exception {	        		
		        	    URI u = ResourceChooserDialog.chooseResource(vos,"Load workflow",true);
		                if (u != null && u.getScheme() != null) {
		                    Reader reader = null;		                
		                    if (u.getScheme().equals("ivo")) {
		                        Ivorn ivo = new Ivorn(u.toString());
		                        logger.error("ivo: " + ivo);
		                        FileManagerNode target = null;		                   
		                        target = vos.node(ivo);		                   
		                        InputStream is = target.readContent();
		                        reader = new InputStreamReader(is);               
		                    } 
		                    if (u.getScheme().equals("file")) {
		                        File f = new File(u.getPath());
		                        reader = new FileReader(f);
		                    }
		                    workflow = (Workflow)Unmarshaller.unmarshal(Workflow.class, reader);	                
		                    reader.close();
		                                        	    
                    	    //tree.setModel(getXmlTreeModel(workflow));
                            workflowTreeModel = new WorkflowTreeModel(workflow);
                           // workflowTreeModel.addTreeModelListener(new TreeModelListener(){
                            	                            							
                            tree.setModel(workflowTreeModel);
                            //tree.setCellRenderer(new workflowTreeRenderer());
                            tree.setCellRenderer(new WorkflowTreeCellRenderer());
                            tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
                            tree.addTreeSelectionListener(new WorkflowTreeSelectionListener());
                            
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
	        	            }
	                    return null;
	                    }
	                }).start();	                	                            	
	        	}
		        catch(Exception ex) {
		            logger.error("Error loading workflow: " + ex.getMessage());
		            JOptionPane.showMessageDialog(null,ex,"Error",JOptionPane.ERROR_MESSAGE);
		        }		        
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
        	try {
        		JobURN id = submit();
                ResultDialog rd = new ResultDialog(null,"Workflow Submitted \nJob ID is \n" + id.getContent());
                rd.show();
                monitor.show(); // brings monitor to the front, if not already there.
                monitor.refresh();
        	}
        	catch(Exception ex){
	            logger.error("Error submitting workflow: " + ex.getMessage());
	            JOptionPane.showMessageDialog(null,ex,"Error",JOptionPane.ERROR_MESSAGE);        		
        	}
        }
    }
    /** create a workflow */
    protected final class CreateAction extends AbstractAction {
        public CreateAction() {
            super("Create Workflow",IconHelper.loadIcon("wf_small.gif"));
            this.putValue(SHORT_DESCRIPTION,"Create a new workflow");
            this.setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
        	workflow = editWF( createWorkflow() );
        	tabbedPaneWF.setEnabledAt(0, true);
        	tabbedPaneWF.setEnabledAt(1, true);
        	tabbedPaneWF.setSelectedIndex(0);
            // tree.setModel(getXmlTreeModel(workflow));
            workflowTreeModel = new WorkflowTreeModel(workflow);
            tree.setModel(workflowTreeModel);
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
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(WorkflowBuilderLauncherImpl.class);
	
    protected final BrowserControl browser;
    protected final Portal portal;	
    protected final Community community;
    protected final Myspace vos;
    protected final JobMonitor monitor;
    protected ApplicationRegistry reg;
    private final Applications apps;
    private CurrentNodeManager currentNodeManager;
    protected WorkflowTreeModel workflowTreeModel = null;
    
    // actions
    protected Action saveAction;
    protected Action loadAction;
    protected Action submitAction;
    protected Action createAction;
    protected Action closeAction;
    
    private WorkflowManager workflowManager;
    private Workflow workflow = null;
    private ApplicationDescriptionSummary appDescSum[] = null;
    
    private JButton clearButton, searchButton = null;
    private JEditorPane htmlPane, descPane, docTextArea;
    private JFileChooser fileChooser = null;
    private JList list, list1, taskSearchList, searchList;
    private JLabel infoLabel;
    private JMenuBar jJMenuBar = null;
    private JMenu wfMenu = null;
    private JPanel workAreaTree, workAreaDoc, tabWf, tabAct, tabTask, tabWel, buttonBox = null;
    private JToolBar toolbar = null;    
    private DefaultListModel activityListModel, taskListModel, taskSearchListModel  = null;
    private JTree tree = null;
    private JTabbedPane tabbedPaneWF, tabbedPaneTasks, tabbedPaneDetails;
    private JTextArea wfDescArea = null;
    private JTextField titleSearchField, nameSearchField, descSearchField, wfNameField, wfFileField;   
    private JScrollPane listView, listView1, scrollPane, taskSearchPane, searchListView = null;
    private URL helpUrl;
    
 
    	
	
    /** 
     * production constructor 
     * @throws Exception
     * 
     * */
    public WorkflowBuilderLauncherImpl(Applications apps, Community community, JobMonitor monitor, Portal portal, Myspace vos, BrowserControl browser, UI ui, HelpServer hs, Configuration conf) throws Exception {
        super(conf,hs,ui);
        this.browser = browser;
        this.portal = portal;
        this.community = community;
        this.vos = vos;
        this.monitor = monitor;
        this.apps = apps;
        community.addUserLoginListener(this);
        initialize();
        
    }   
    
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
		createWorkflow();
		this.setTitle("Workflow Builder");
		this.setJMenuBar(getJJMenuBar());	
		
		JPanel leftPanel = createVericalBoxPanel();		
		JPanel rightPanel = createVericalBoxPanel();
		JPanel topPanel = createHorizontalBoxPanel();
		JPanel bottomPanel = createHorizontalBoxPanel();
	    
	    //left side
		tabbedPaneTasks = new JTabbedPane();
		
		tabbedPaneTasks.addTab("Task List", null, createPanelForComponent(getTaskList(), "All available tasks"), "List of all tasks currently available");
		tabbedPaneTasks.addTab("Task Search", null, createPanelForComponent(getTaskSearch(), "Search for specific task/s"), "Search for a specific task");
		tabbedPaneTasks.setEnabledAt(1,false);
		topPanel.setPreferredSize(new Dimension(250,600));
		topPanel.setMinimumSize(new Dimension(250,300));
		topPanel.add(tabbedPaneTasks);
		bottomPanel.setPreferredSize(new Dimension(250, 225));
		bottomPanel.setMinimumSize(new Dimension(250, 200));
		bottomPanel.add(createPanelForComponent(getActivityList(), "Activity List"));	
		JSplitPane splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
		//splitPane1.setOneTouchExpandable(true);		
		leftPanel.add(splitPane1);
		
		//right side
		tabbedPaneDetails = new JTabbedPane();
		tabbedPaneDetails.addTab("Workflow details", null, getTabbedWorkflowDetailsPanel(), "Workflow details");
		tabbedPaneDetails.addTab("Task details", null, getTabbedTaskDetailsPanel(), "Task details");
		tabbedPaneDetails.addTab("Activity details", null, getTabbedActivityDetailsPanel(), "Activity details");
		tabbedPaneDetails.addTab("Welcome", null, getTabbedWelcomeDetailsPanel(), "Welcome details");
		tabbedPaneDetails.setSelectedIndex(3);
		
		tabbedPaneWF = new JTabbedPane();		
		tabbedPaneWF.addTab ("Tree View", null, getTabbedTreePanel(), "Display workflow graphically");
		tabbedPaneWF.addTab("Document", null, getTabbedDocPanel(), "Display text version of workflow");
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
		rightPanel.add(splitPane2);
		//rightPanel.add(tabbedPaneDetails);
		//rightPanel.add(tabbedPaneWF);		
	    		
		//both
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
		//splitPane.setOneTouchExpandable(true);
		
		JPanel pane = getJContentPane();
		pane.add(splitPane, BorderLayout.CENTER);
		pane.add(getToolbar(), BorderLayout.NORTH);
		
		addMenuOptions();
        
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
        populateTaskList();        
    }
    /**
     * loading of Workflow builder not held up due to querying registry
     */
    private void populateTaskList() {
    	setStatusMessage("Loading task information from registry");
		setBusy(true);			
		try {			
		  appDescSum = fullList();						
	      for (int i = 0; i < appDescSum.length ; i++){
	      	for (int j = 0; j < appDescSum[i].getInterfaceNames().length; j++){
	      		String iNames[] = appDescSum[i].getInterfaceNames();
	      		for (int k = 0; k < iNames.length; k++) {
		      		TaskDetails td = new TaskDetails(appDescSum[i].getName(),
								                     appDescSum[i].getUIName(),
		                                             iNames[k]);
                    taskListModel.addElement(td);
	      		}
	      	}		    
	      }									
		}
	    catch(WorkflowInterfaceException wi){
		  taskListModel.addElement("Unable to read tasks..");
		  logger.error("Error thrown reading task list: " + wi.getMessage());
	    }
	    setStatusMessage("");
	    setBusy(false);
	    tabbedPaneTasks.setEnabledAt(1,true);	    
    }
    private void populateTaskSearchList(Document doc) {
    	setStatusMessage("Searching registry");
		setBusy(true);		
		try {
		   taskSearchListModel.clear();
		   NodeList nl = doc.getElementsByTagName("title");	   
		   for ( int i=0; i<=nl.getLength(); i++  ) {		
		       Node node = nl.item(i);		 		 
			   if( node instanceof org.w3c.dom.Element ) {				  				
			      for (int j = 0; j < appDescSum.length ; j++){
			          if (appDescSum[j].getUIName().equalsIgnoreCase(nl.item(i).getFirstChild().getNodeValue()))
			      	      {
				      	  for (int k = 0; k < appDescSum[j].getInterfaceNames().length; k++){
				      	      String iNames[] = appDescSum[j].getInterfaceNames();
				      	      for (int l = 0; l < iNames.length; l++) {
					      		TaskDetails td = new TaskDetails(appDescSum[i].getName(),
					                                             appDescSum[i].getUIName(),
                                                                 iNames[l]);
					      		taskSearchListModel.addElement(td);
				      	      }
				      	  }
				      	break;
			      	  }		    
			      } 
			  }
		   }														
		}
	    catch(Exception wi){
	    	taskSearchListModel.addElement("Unable to read tasks..");
		    logger.error("Error thrown reading task list: " + wi.getMessage());
	    }
	    setStatusMessage("");
	    setBusy(false);
	    tabbedPaneTasks.setEnabledAt(1,true);
    }    
	/**
	 * This method initializes jJMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */    
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getWfMenu());
		}
		return jJMenuBar;
	}
	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */    
	private JMenu getWfMenu() {
		if (wfMenu == null) {
			wfMenu = new JMenu();
			wfMenu.setText("Workflow Builder");
		}
		return wfMenu;
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
			activityListModel.addElement("ForLoop");
			activityListModel.addElement("ParallelForLoop");
			activityListModel.addElement("WhileLoop");
			list = new JList(activityListModel){
		        // This method is called as the cursor moves within the list.
		        public String getToolTipText(MouseEvent evt) {
		            // Get item index
		            int index = locationToIndex(evt.getPoint());		    
		            // Get item
		            Object item = getModel().getElementAt(index);		    
		            // Return the tool tip text
		            return item.toString();
		        }
		    };
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setDragEnabled(true);
			list.setCellRenderer(new ActivityListRenderer());
			list.addMouseListener(new MouseAdapter(){
		          public void mouseClicked(MouseEvent event)
		          {
		          	tabbedPaneDetails.setSelectedIndex(3);
		          	readHelpFile("help_" + list.getSelectedValue().toString() + ".html");
		          };		          
			});
			listView = new JScrollPane(list);
		}
		return listView;
	}
	/**
	 * This method returns the task list	
	 * 	
	 * @return javax.swing.JScrollpane	
	 */    
	private JScrollPane getTaskList() {
		if (taskListModel == null) {			
			taskListModel = new DefaultListModel();	
			list1 = new JList(taskListModel){
		        // This method is called as the cursor moves within the list.
		        public String getToolTipText(MouseEvent evt) {
		        	try {
		                int index = locationToIndex(evt.getPoint());		    
		                TaskDetails td = (TaskDetails)getModel().getElementAt(index);
                        // Return the tool tip text
                        return "<html>Task: "+td.getTaskName()+"<br>Interface: "+td.getInterfaceName() + "</html>";
		        	}
		        	catch (ArrayIndexOutOfBoundsException ex) {
		        		// prevents tooltip attempting to display before list populated
		        		return null;
		        	}		            		                  
		        }
		    };
			list1.setDragEnabled(true);	
			list1.addMouseListener(new PopulateTaskPanelListListener());
			listView1 = new JScrollPane(list1);			
		}
		return listView1;
	}
	/**
	 * Initializes task search panel
	 * 
	 * @return JScrollPane
	 */
	private JScrollPane getTaskSearch() {
		if (taskSearchPane == null) {			
	    	JLabel label1 = new JLabel("Name: ");
	    	JLabel label2 = new JLabel("Title: ");
	    	JLabel label3 = new JLabel("Desc: ");
	        nameSearchField = new JTextField(15);
	        nameSearchField.setToolTipText("eg: org.astrogrid (AuthorityID or ResourceKey)");
	        nameSearchField.addKeyListener(new KeyAdapter(){
	        	public void keyTyped(KeyEvent e){clearButton.setEnabled(true);};
	        });
	        titleSearchField = new JTextField(15);
	        titleSearchField.setToolTipText("eg: Trace");
	        titleSearchField.addKeyListener(new KeyAdapter(){
	        	public void keyTyped(KeyEvent e){clearButton.setEnabled(true);};
	        });
	        descSearchField = new JTextField(15);
	        descSearchField.setToolTipText("eg: SIAP");
	        descSearchField.addKeyListener(new KeyAdapter(){
	        	public void keyTyped(KeyEvent e){clearButton.setEnabled(true);};
	        });
			JPanel taskSearchPanel = new JPanel(new SpringLayout());
			taskSearchPanel.add(label1);
			taskSearchPanel.add(nameSearchField);
			taskSearchPanel.add(label2);
			taskSearchPanel.add(titleSearchField);
			taskSearchPanel.add(label3);
			taskSearchPanel.add(descSearchField);
			makeCompactGrid(taskSearchPanel, 3,2,2,2,2,2);
			JPanel p = new JPanel();
			p.setLayout(new BorderLayout());
			p.add(taskSearchPanel, BorderLayout.NORTH);
			p.add(getTaskSearchButtonBox(), BorderLayout.CENTER);
			p.add(getTaskSearchList(), BorderLayout.SOUTH);		
			taskSearchPane = new JScrollPane(p);
			taskSearchPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
			taskSearchPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return taskSearchPane;
	}

	/**
	 * This method returns the task list	
	 * 	
	 * @return javax.swing.JScrollpane	
	 */    
	private JScrollPane getTaskSearchList() {
		if (taskSearchListModel == null) {
			taskSearchListModel = new DefaultListModel();
			searchList = new JList(taskSearchListModel){
		        // This method is called as the cursor moves within the list.
		        public String getToolTipText(MouseEvent evt) {
			        try {
			            int index = locationToIndex(evt.getPoint());		    
			            TaskDetails td = (TaskDetails)getModel().getElementAt(index);
	                    // Return the tool tip text
	                    return "<html>Task: "+td.getTaskName()+"<br>Interface: "+td.getInterfaceName() + "</html>";
			        }
			        catch (ArrayIndexOutOfBoundsException ex) {
			        	// prevents tooltip attempting to display before list populated
			        	return null;
			        }		            		                  
			    }
			};
		}		
		searchList.setDragEnabled(true);
		searchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		searchList.addMouseListener(new PopulateTaskPanelListListener());	
		searchListView = new JScrollPane(searchList);	
		searchListView.setPreferredSize(new Dimension(220,400));										
		return searchListView;
	}

	
	
	
   private void taskQuery() {
     String taskJoin = "OR"; 
     String sqlQuery = "";
	 boolean andreqd = false; // used to add 'and' between e.g. (constraints) and (wavelength)

     try{
     	Document doc = DomHelper.newDocument();
         // Lets build up the XML for a query.
         sqlQuery = " Select * from Registry where ( @xsi:type = 'cea:CeaApplicationType' or ";
         sqlQuery += " @xsi:type = 'cea:CeaHttpApplicationType' ";
         sqlQuery += ") and (@status = 'active'";              
       
	     // is this an empty search?
	     if ( nameSearchField.getText().length() > 0 || titleSearchField.getText().length() > 0 || descSearchField.getText().length() > 0 )
	     {				
             sqlQuery += " and (";       
             if ( nameSearchField.getText().length() > 0 ) 
             {
                 sqlQuery += " vr:identifier like '%" + nameSearchField.getText() + "%' ";
			     andreqd = true;
             }
             if ( titleSearchField.getText().length() > 0 ) 
             {
			     if (andreqd)
                  sqlQuery += taskJoin;    
			     sqlQuery += " vr:title like '%" + titleSearchField.getText() + "%' ";
			     andreqd = true;
             }
             if ( descSearchField.getText().length() > 0 ) 
             {
		         if (andreqd) 
                  sqlQuery += taskJoin;           
		         sqlQuery += " vr:content/vr:description like '%" + descSearchField.getText() + "%' ";
             }		
            sqlQuery += ")";
		    }		                 			         
	    sqlQuery += ")"; // End of query
	    logger.info("sql query: " + sqlQuery );
	    String adqlString = Sql2Adql.translateToAdql074(sqlQuery);
	    RegistryService rs = RegistryDelegateFactory.createQuery( );
	    doc = rs.search(adqlString);
	    
	    populateTaskSearchList(doc);
	    
     }
     catch(Exception e){
     	logger.error("Error generating task search: " + e.getMessage());
     }
   } // end of taskQuery()	
   /**
    * Create a DefaultListModel of task titles from a Document
    * @param doc Document 
    * @return DefaultListModel
    */
   private DefaultListModel docToListmodel(Document doc) {
   	   DefaultListModel lm = new DefaultListModel();
	   NodeList nl = doc.getElementsByTagName("title");	   
	   for ( int i=0; i<=nl.getLength(); i++  ) {		
		 Node node = nl.item(i);		 		 
		 if( node instanceof org.w3c.dom.Element ) {
			lm.addElement(nl.item(i).getFirstChild().getNodeValue() );
		 }
	   }  	   
   	 return lm;
   }	
	    
	private JPanel getTabbedTreePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		tree = new JTree();
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(false);
		tree.setScrollsOnExpand(true);			
		tree.setModel(null);
		scrollPane = new JScrollPane(tree);
		panel.add(scrollPane);			
		panel.setBorder(BorderFactory.createTitledBorder("Tree view"));
		panel.setPreferredSize(new Dimension(800,500));
		panel.setMinimumSize(new Dimension(700,400));
		
		return panel;
	}		
	
	
	private JPanel getTabbedWorkflowDetailsPanel() {
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
	    	descPane.setPreferredSize(new Dimension(200,100));
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
			makeCompactGrid(p, 3,2,2,2,10,3);
			p.setPreferredSize(new Dimension(500,100));
			p.setMinimumSize(new Dimension(400,100));
			tabWf.add(p);
			tabWf.setPreferredSize(new Dimension(800,230));
			tabWf.setMinimumSize(new Dimension(700,150));
		}
		return tabWf;
	}
	
	private JPanel getTabbedTaskDetailsPanel() {
		if (tabTask == null) {
			tabTask = new JPanel(new BorderLayout());
			tabTask.setPreferredSize(new Dimension(800,230));
			tabTask.setMinimumSize(new Dimension(700,150));
		}
		return tabTask;
	}
	
	private JPanel getTabbedActivityDetailsPanel() {
		if (tabAct == null) {
			tabAct = new JPanel();					
			infoLabel = new JLabel("");
			infoLabel.setVerticalAlignment(SwingConstants.CENTER);
			infoLabel.setHorizontalAlignment(SwingConstants.CENTER);	        			
	        JPanel panel = new JPanel();
	        panel.add(infoLabel, BorderLayout.LINE_START );
	        panel.setPreferredSize(new Dimension(790,220));
	        panel.setMinimumSize(new Dimension(700,150));
	        
			JScrollPane pane = new JScrollPane(panel,
					                           JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					                           JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);			
			tabAct.add(pane);
			tabAct.setPreferredSize(new Dimension(800,225));
			tabAct.setMinimumSize(new Dimension(700,150));
		}
		return tabAct;
	}
	
	private JPanel getTabbedWelcomeDetailsPanel() {
		if (tabWel == null) {
			tabWel = new JPanel();	        
			
	        htmlPane = new JEditorPane();
	        htmlPane.setEditable(false);
	        readHelpFile("welcome.html");
	        htmlPane.setPreferredSize(new Dimension(790,210));
	        htmlPane.setMinimumSize(new Dimension(700,150));
	        
			JScrollPane pane = new JScrollPane(htmlPane,
					                           JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					                           JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);			
			tabWel.add(pane);
			tabWel.setPreferredSize(new Dimension(800,225));
			tabWel.setMinimumSize(new Dimension(700,150));
		}
		return tabWel;
	}
	
	private void readHelpFile(String fileName) {	
		try {
			helpUrl = this.getClass().getResource(fileName);
            htmlPane.setPage(helpUrl);
	    } 
	    catch(Exception ex) {
	    	logger.error("Error reading help file " + fileName + " : " + ex.getMessage());
	    	htmlPane.setText("Unable to read help file");	    
	    }
	}
	
	private JPanel getTabbedDocPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		docTextArea = new JEditorPane();		
		docTextArea.setEditable(false);	
		scrollPane = new JScrollPane(docTextArea);
		panel.add(scrollPane, BorderLayout.CENTER);			
		panel.setBorder(BorderFactory.createTitledBorder("Workflow document"));
		panel.setPreferredSize(new Dimension(800,500));
		panel.setMinimumSize(new Dimension(700,400));
		
		return panel;
	}	
	/**
	 * Create an XmlTreeModel from Object
	 * @param o
	 * @return xmlTreeModel
	 */
    public XmlTreeModel getXmlTreeModel(Object o){
		Document doc= null;
		XmlTreeModel xMod = null;
		try{
		    doc = DomHelper.newDocument();  
            Marshaller.marshal(o,doc);
            xMod = new XmlTreeModel(doc);
		}
		catch(Exception ex){
			logger.error("Error creating XmlTreeModel: " + ex.getMessage() );
		}
    	return xMod;
    }
	/**
	 * Create a Document from Object
	 * @param Object
	 * @return Document
	 */
    public Document getXmlDoc(Object o){
		Document doc= null;
		try{
		    doc = DomHelper.newDocument();  
            Marshaller.marshal(o,doc);
		}
		catch(Exception ex){
			logger.error(ex.getMessage());
		}
    	return doc;
    }    
    /**
     * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogout(org.astrogrid.desktop.modules.ag.UserLoginEvent)
     */
    public void userLogout(UserLoginEvent e) {
    	;
    }	
    /**
     * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogin(org.astrogrid.desktop.modules.ag.UserLoginEvent)
     */
    public void userLogin(UserLoginEvent e) {
    	;
    } 
    /**
     * 
     * @return
     * @throws WorkflowInterfaceException
     */
    private ApplicationRegistry getAppReg() throws WorkflowInterfaceException {
        if (reg == null) {
         reg = community.getEnv().getAstrogrid().getWorkflowManager().getToolRegistry();
        } 
        return reg;
    }
    /**
     * 
     * @return
     * @throws WorkflowInterfaceException
     */
    public String[] listTasks() throws WorkflowInterfaceException {    	
        return getAppReg().listApplications();
      
    }
    /**
     * 
     * @return
     * @throws WorkflowInterfaceException
     */
    public ApplicationDescriptionSummary[] fullList() throws WorkflowInterfaceException {
        return getAppReg().listUIApplications();
    }
    /** 
     * submit a workflow to jes.
     * @param wf Workflow to submit
     * @return JOBURN of submitted job
     * @throws WorkflowInterfaceException
     */
    private JobURN submit() throws WorkflowInterfaceException {
       return getAstrogrid().getWorkflowManager().getJobExecutionService().submitWorkflow(workflow);
    }    
    /**
     * create a workflow
     * @param name name of workflow
     * @param desc description of workflow
     */
    private Workflow createWorkflow() {
    	try{  		 
    		String name = "Workflow name";    		
    		String desc = "Worflow description";
    		
        	WorkflowBuilder workflowBuilder = getAstrogrid().getWorkflowManager().getWorkflowBuilder();
        	
        	Credentials credentials = new Credentials();
            credentials.setAccount( community.getEnv().getAccount() );
            Group group = new Group();
            group.setName( community.getEnv().getAccount().getName() );
            group.setCommunity( community.getEnv().getAccount().getCommunity() );
            credentials.setGroup( group );
            credentials.setSecurityToken( "dummy" );
            
            workflow = workflowBuilder.createWorkflow( credentials, name, desc ) ;                   	
    	}
    	
    	catch(WorkflowInterfaceException wiex) {
    		logger.error("Error creating workflow: " + wiex.getMessage());    		
    	}   	
    	return workflow;
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
    

      	    private Toolbox getAstrogrid() {
      	        return community.getEnv().getAstrogrid();
      	    }      	     

      	    protected Tool editTool(ApplicationDescription desc) {
//      	        ToolEditorDialog editor = new ToolEditorDialog(desc,null);
//      	        editor.show();
//      	        return editor.getTool();
      	    	return null;
      	    }
      
  	/**
  	 * This method initializes task search buttons jPanel	
  	 * 	
  	 * @return javax.swing.JPanel	
  	 */    
  	private JPanel getTaskSearchButtonBox() {
  		if (buttonBox == null) {
  			buttonBox = new JPanel();
  			buttonBox.add(getSearchButton(), null);
  			buttonBox.add(getCancelButton(), null);
  		}
  		return buttonBox;
  	}
	/**
	 * This method initializes Search jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getSearchButton() {
		if (searchButton == null) {
			searchButton = new JButton();
			searchButton.setText("Search");
			searchButton.setToolTipText("Search registry");
			searchButton.setMnemonic('s');
			searchButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {
					taskQuery();
					searchList.setModel(taskSearchListModel);
				}
			});
		}
		return searchButton;
	}
	/**
	 * This method initializes Clear button
	 * actionPerformed will reset textFields and disable Clear Button	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getCancelButton() {
		if (clearButton == null) {
			clearButton = new JButton();
			clearButton.setText("Clear");
			clearButton.setToolTipText("Clear fields");
			clearButton.setMnemonic('c');
			clearButton.setEnabled(false);
			clearButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
                    nameSearchField.setText("");
                    titleSearchField.setText("");
                    descSearchField.setText("");
                    clearButton.setEnabled(false);
				}
			});
		}
		return clearButton;
	}  	

      /*
       * Add various actions to toolbar and wfMenu
       */
      private void addMenuOptions() {
        submitAction = new SubmitAction();
        submitAction.setEnabled(false); // Initially false until a workflow is loaded/created
        loadAction = new LoadAction();
        createAction = new CreateAction(); 
        createAction.setEnabled(false); // Not needed for viewer
        saveAction = new SaveAction();
        saveAction.setEnabled(false); // Initially false until a workflow is loaded/created
        closeAction = new CloseAction();
        
        toolbar.add(createAction);
        toolbar.add(submitAction);
        toolbar.add(loadAction);
        toolbar.add(saveAction);
        
        wfMenu.add(createAction);
        wfMenu.add(new JSeparator());
        wfMenu.add(submitAction);
        wfMenu.add(loadAction);
        wfMenu.add(saveAction);      
        wfMenu.add(new JSeparator());
        wfMenu.add(closeAction);
      }
      
      /**
       * Aligns the first <code>rows</code> * <code>cols</code>
       * components of <code>parent</code> in
       * a grid. Each component in a column is as wide as the maximum
       * preferred width of the components in that column;
       * height is similarly determined for each row.
       * The parent is made just big enough to fit them all.
       *
       * @param rows number of rows
       * @param cols number of columns
       * @param initialX x location to start the grid at
       * @param initialY y location to start the grid at
       * @param xPad x padding between cells
       * @param yPad y padding between cells
       */
      public static void makeCompactGrid(Container parent,
                                  int rows, int cols,
                                  int initialX, int initialY,
                                  int xPad, int yPad) {
          SpringLayout layout;
          try {
              layout = (SpringLayout)parent.getLayout();
          } catch (ClassCastException exc) {
              logger.error("The first argument to makeCompactGrid must use SpringLayout.");
              return;
          }

          //Align all cells in each column and make them the same width.
          Spring x = Spring.constant(initialX);
          for (int c = 0; c < cols; c++) {
              Spring width = Spring.constant(0);
              for (int r = 0; r < rows; r++) {
                  width = Spring.max(width,
                                     getConstraintsForCell(r, c, parent, cols).
                                         getWidth());
              }
              for (int r = 0; r < rows; r++) {
                  SpringLayout.Constraints constraints =
                          getConstraintsForCell(r, c, parent, cols);
                  constraints.setX(x);
                  constraints.setWidth(width);
              }
              x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
          }

          //Align all cells in each row and make them the same height.
          Spring y = Spring.constant(initialY);
          for (int r = 0; r < rows; r++) {
              Spring height = Spring.constant(0);
              for (int c = 0; c < cols; c++) {
                  height = Spring.max(height,
                                      getConstraintsForCell(r, c, parent, cols).
                                          getHeight());
              }
              for (int c = 0; c < cols; c++) {
                  SpringLayout.Constraints constraints =
                          getConstraintsForCell(r, c, parent, cols);
                  constraints.setY(y);
                  constraints.setHeight(height);
              }
              y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
          }

          //Set the parent's size.
          SpringLayout.Constraints pCons = layout.getConstraints(parent);
          pCons.setConstraint(SpringLayout.SOUTH, y);
          pCons.setConstraint(SpringLayout.EAST, x);
      }

      /* Used by makeCompactGrid. */
      private static SpringLayout.Constraints getConstraintsForCell(
                                                  int row, int col,
                                                  Container parent,
                                                  int cols) {
          SpringLayout layout = (SpringLayout) parent.getLayout();
          Component c = parent.getComponent(row * cols + col);
          return layout.getConstraints(c);
      }
      /** 
       * Custom renderer for activity list - adds icons
       */
      public class ActivityListRenderer extends DefaultListCellRenderer {
    	public Component getListCellRendererComponent(JList list,
    			                                      Object value,
													  int index,
													  boolean isSelected,
													  boolean hasFocus) {
    		JLabel label = (JLabel)super.getListCellRendererComponent(list,
    				                                                  value,
																	  index,
																	  isSelected,
																	  hasFocus);
    		
    		if (value.toString().indexOf("Loop") == -1) {
            	label.setIcon(IconHelper.loadIcon("icon_"+value+".gif"));	
            } else {
            	label.setIcon(IconHelper.loadIcon("icon_Loop.gif"));
            }
    		
    		return(label);																	 
    	}
    } 
      /** 
       * Custom renderer for Workflow tree - adds icons and text
       */      
      public class WorkflowTreeCellRenderer extends DefaultTreeCellRenderer {
    	public Component getTreeCellRendererComponent(JTree tree, 
    			                                      Object value,
													  boolean isSelected,
													  boolean expanded, 
													  boolean leaf, 
													  int row, 
													  boolean hasFocus) {
    		JLabel label = (JLabel)super.getTreeCellRendererComponent(tree, 
    				                                                  value, 
																	  isSelected, 
																	  expanded, 
																	  leaf, 
																	  row, 
																	  hasFocus);
    		if (value instanceof Sequence){  
    			label.setToolTipText("Sequence");
    			label.setIcon(IconHelper.loadIcon("icon_Sequence.gif"));
    			label.setText("Sequence");
    		}
    		else if (value instanceof Flow){ 
    			label.setToolTipText("Flow");
    			label.setIcon(IconHelper.loadIcon("icon_Flow.gif"));
    			label.setText("Flow");   			
    		}    		
    		else if (value instanceof Step){ 
    			Step s = (Step)value;
    			label.setIcon(IconHelper.loadIcon("icon_Step.gif"));
    			label.setText("Step: " + s.getName() + ", task: " + s.getTool().getName());
//    			 TODO need to add how above selection works.... or link task + activity panels for this activity
    			label.setToolTipText("Step");
    		}
    		else if (value instanceof Set){ 
    			Set s = (Set)value;
    			label.setIcon(IconHelper.loadIcon("icon_Set.gif"));
    			label.setText("Set");
    			label.setToolTipText("var: " + s.getVar() + " val: " + s.getValue());
    		}
    		else if (value instanceof Unset){ 
    			Unset s = (Unset)value;
    			label.setIcon(IconHelper.loadIcon("icon_Unset.gif"));
    			label.setText("Unset");
    			label.setToolTipText("var: " + s.getVar());
    		}    		
    		else if (value instanceof Script){ 
    			Script s = (Script)value;
    			label.setIcon(IconHelper.loadIcon("icon_Script.gif"));
    			label.setText("Script");
    			label.setToolTipText("Script");
    		} 
    		else if (value instanceof For){ 
    			For f = (For)value;
    			label.setIcon(IconHelper.loadIcon("icon_Loop.gif"));
    			label.setText("For");
    			label.setToolTipText("var: " + f.getVar() + " items: " + f.getItems());
    		}
    		else if (value instanceof Scope){ 
    			label.setIcon(IconHelper.loadIcon("icon_Scope.gif"));
    			label.setText("Scope");
    		}    		
    		else {    			
    			label.setText("to do");
    		}   		
    		return(label);																	 
    	}
    } 
      /** 
       * Custom listener - dictates which panels are displayed and their contents
       */    
      public class WorkflowTreeSelectionListener implements TreeSelectionListener {
    	public void valueChanged(TreeSelectionEvent event){
    		tabbedPaneDetails.setSelectedIndex(2);
    		Object ob = tree.getLastSelectedPathComponent();
    		if (ob == null) 
    			return;
    		if (ob instanceof Step){
    			Step s = (Step)ob;
    			if (s.getTool() != null) {
                    TaskInfoPanel taskPanel = new TaskInfoPanel(s.getTool(), apps);
                    tabTask.removeAll();
                    tabTask.add(taskPanel);
                    tabTask.validate();
    			}    			
                StepPanel stepPanel = new StepPanel(s);
                tabAct.removeAll();
                tabAct.add(stepPanel);
                tabAct.validate();
                tabbedPaneDetails.setSelectedIndex(2);
    		}
    		else if (ob instanceof Script) {
    			Script s = (Script)ob;
                ScriptPanel scriptPanel = new ScriptPanel(s);
                tabAct.removeAll();
                tabAct.add(scriptPanel);
                tabAct.validate();
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
                tabAct.removeAll();
                tabAct.validate();
                tabbedPaneDetails.setSelectedIndex(2);
    			// An activity with nothing to display
    		}
    	}
    } 
      /**
       * 
       * @param values an array of label/value pairs
       */
      private void displayBasicInfo(String[] values) {
		BasicInfoPanel basicInfo  = new BasicInfoPanel(values);
        tabAct.removeAll();
        tabAct.add(basicInfo);
        tabAct.validate();
        tabbedPaneDetails.setSelectedIndex(2);      	
      }

      public class PopulateTaskPanelListListener extends MouseAdapter {
          public void mouseClicked(MouseEvent event)
          {
	          tabbedPaneDetails.setSelectedIndex(1);
	          ApplicationDescription description = null;
	          try { 
	  	          JList list = (JList)event.getSource();
	  	          boolean intFound = false;
	  	          TaskDetails td = (TaskDetails)list.getSelectedValue();

	              ApplicationRegistry applRegistry = getAstrogrid().getWorkflowManager().getToolRegistry();      	       
	              description = applRegistry.getDescriptionFor( td.getTaskName() );		                  
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
	              tabTask.removeAll();
	              tabTask.add(taskPanel);
	              tabTask.validate();							   
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
	      	      }
	      	      catch(WorkflowInterfaceException wi){
	      	      	logger.error("Error creating task panel: " + wi.getMessage());
	      	        JOptionPane.showMessageDialog(null,wi.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	              }
	              catch(NullPointerException ex) {
	                  logger.error("Error loading task details for task: " + description.getName() );
	                  JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);        	
	              }		      	      			      	      		          		          	    
        	
        }      	
      }
      /**
       * TaskDetails - used to populate task lists
       */
      private class TaskDetails {
      	String uiName;
      	String taskName;
      	String interfaceName;
      	/**
      	 * 
      	 * @param taskName
      	 * @param uiName
      	 * @param interfaceName
      	 */
      	private TaskDetails(String taskName, String uiName, String interfaceName){
      		this.uiName = uiName;
      		this.taskName = taskName;
      		this.interfaceName = interfaceName;
      	}
      	public String toString() {
      		return uiName + " (" + interfaceName +")" ;
      	}
      	public String getTaskName() {
      		return taskName;
      	}
      	public String getUIName() {
      		return uiName;
      	}      	
      	public String getInterfaceName() {
      		return interfaceName;
      	}
      }
} 
