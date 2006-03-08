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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.PrintGraphics;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.awt.dnd.DnDConstants;
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
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.NotFoundException;
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
import org.astrogrid.desktop.modules.workflowBuilder.dragAndDrop.DefaultTreeTransferHandler;
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
import org.exolab.castor.xml.CastorException;
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
                    protected void doFinished(Object o) {
                        locationField.setText(u.toString());
                        locationPanel.setVisible(true);
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
                            // pre-populate the application information  cache.
                            Iterator i = wf.findXPathIterator("//tool"); // find al tools.
                            while (i.hasNext()) {
                                Tool t = (Tool)i.next();
                                try {
                                    apps.getInfoForTool(t);
                                } catch (NotFoundException e) {
                                    logger.warn("Failed to find app info for " + t.getName());
                                }                                
                            }
                            return wf;
                        }
                        protected void doFinished(Object o) { // do all updating of ui in this method, as runs on swing thread
                            getModel().setWorkflow((Workflow)o, false);   
                            getTree().expandAll(true);
                            tabbedPaneWF.setSelectedIndex(0);
                    	    activateMenus();
		                    locationField.setText(u.toString());
		                    locationPanel.setVisible(true);
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
            this.setEnabled(false);
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
        			getModel().setWorkflow(jobs.createWorkflow(), false);
        			getTree().expandAll(true);
        		} 
        		catch (ServiceException ex) { // quite unlikely.
        			showError("Failed to create workflow",ex);
        		}
        	} else {
        	    i = JOptionPane.showConfirmDialog(null, "Creating a new workflow will mean current workflow is deleted. \n" 
        	    		                               +"Do you wish to save your workflow prior to creating new one?", "Create new workflow", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); 
        	}
        	if (i == JOptionPane.YES_OPTION) {
        		saveAction.actionPerformed(null);
        		try {
        			getModel().setWorkflow(jobs.createWorkflow(), false);
        			getTree().expandAll(true);
        		} 
        		catch (ServiceException ex) { // quite unlikely.
        			showError("Failed to create workflow",ex);
        		}
        	}
        	if (i == JOptionPane.NO_OPTION) {
        		try {
        			getModel().setWorkflow(jobs.createWorkflow(), false);
        			getTree().expandAll(true);
        		} 
        		catch (ServiceException ex) { // quite unlikely.
        			showError("Failed to create workflow",ex);
        		}
        	}
        	locationField.setText(null);
            locationPanel.setVisible(false);
    		tabbedPaneWF.setSelectedIndex(0);
        	activateMenus();
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
    /** collapse workflow */
	protected final class CollapseAction extends AbstractAction {
	    public CollapseAction() {
	        super("Collapse", IconHelper.loadIcon("collapse.gif"));
	        this.putValue(SHORT_DESCRIPTION,"Collapse tree");
	        this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
	        this.setEnabled(false);            
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
	        this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_X));
	        this.setEnabled(false);            
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
	        this.setEnabled(false);
	    }
        public void actionPerformed(ActionEvent e) {
        	moveNode(true);
	    }
	}
    /** demote node */
	protected final class DemoteAction extends AbstractAction {
	    public DemoteAction() {
	        super("Move down", IconHelper.loadIcon("demote.gif"));
	        this.putValue(SHORT_DESCRIPTION,"Move activity down");
	        this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_D));
	        this.setEnabled(false);            
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
	        this.setEnabled(false);            
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
	        this.setEnabled(false);            
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
	        this.setEnabled(false);            
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
	        this.setEnabled(false);            
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
    			try {
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
    			} catch (CastorException cex) {
    				logger.error("Error creating copy of node, " + cex.getMessage());
    			}
    		} else {
    			setStatusMessage("Unable to paste activity here, paste into either Sequence or Flow activities");
    		}	            	        	            	
	    }
	}
    /** print workflow transcript */
	protected final class PrintAction extends AbstractAction {
	    public PrintAction() {
	        super("Print", IconHelper.loadIcon("printer.gif"));
	        this.putValue(SHORT_DESCRIPTION,"Print the workflow transcript");
	        this.putValue(ACCELERATOR_KEY, KeyStroke.getAWTKeyStroke(new Integer(KeyEvent.VK_P).intValue(),InputEvent.CTRL_DOWN_MASK));
	        this.setEnabled(true); 
	    }
        public void actionPerformed(ActionEvent e) {
            (new BackgroundOperation("Printing....") {
                protected Object construct() throws Exception {
                	populateTextArea();
                    tabbedPaneWF.setSelectedIndex(1);
                    Properties p = new Properties();
                    Toolkit toolkit = parent.getToolkit();
                    PrintJob printJob = toolkit.getPrintJob(parent, "Workflow transcript", p);
                    try {
                       	if (printJob != null) {
                            Graphics pg = printJob.getGraphics();
                            if (pg != null) {
                            	printLongString(printJob, pg, docTextArea.getText());
                            	pg.dispose();
                            }
                            printJob.end();
                        }
                    } catch(Exception ex) {
                    	showError("An error occured during printing... ", ex);
                    }                    	
                return null;
                }
            }).start();	                
	    }
	}
	
    /** strip transcript of execution information and 
     * refresh to open workflow builder */
	protected final class StripTranscriptAction extends AbstractAction {
	    public StripTranscriptAction() {
	        super("strip execution info", IconHelper.loadIcon("file_obj.gif"));
	        this.putValue(SHORT_DESCRIPTION,"<html>Remove execution information from transcript <br>and open in workflow editor</html>");
	        this.putValue(ACCELERATOR_KEY, KeyStroke.getAWTKeyStroke(new Integer(KeyEvent.VK_S).intValue(),InputEvent.CTRL_DOWN_MASK));
	        this.setEnabled(false); 
	    }
        public void actionPerformed(ActionEvent e) {
            (new BackgroundOperation("Stripping execution information....") {
                    protected Object construct() throws Exception {
                    	Workflow wf = getModel().getStrippedWorkflow();
                    	model.setWorkflow(wf, true);
                        return null;
            }
            protected void doFinished(Object o) { // do all updating of ui in this method, as runs on swing thread
                getTree().expandAll(true);
        	    activateMenus();
        	    populateTextArea();
        		validateWorkflow();
            }
            }).start();	   
	    }
	}

	
    /** Insert Action
     * Menus added to insert activities for occasions where Dnd 
     * isn't working  */
	public class InsertAction extends AbstractAction {		
        public void actionPerformed(ActionEvent e) {
        	if (((JMenuItem)e.getSource()).getText() == null) // shouldn't happen
        	    return;

        	DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        	if (parentNode == null) {
        		setStatusMessage("Select node within tree where you wish to insert " + ((JMenuItem)e.getSource()).getText());
        		return;
        	}
        	
        	DefaultTreeTransferHandler dtth = new DefaultTreeTransferHandler(tree, DnDConstants.ACTION_COPY);
        	DefaultMutableTreeNode childNode = dtth.getActivityNode(null, ((JMenuItem)e.getSource()).getText());
       	
        	setStatusMessage(insertNode(parentNode, childNode));
	    }
	}

	
	/**
	 * Take care of printing transcripts
	 * @param printJob PrintJob
	 * @param pg Graphics
	 * @param s String to print
	 */
	private void printLongString(PrintJob printJob, Graphics pg, String s) {
		int pageNum = 1;
		int linesForThisPage = 0;
		int linesForThisJob = 0;
		
		if (!(pg instanceof PrintGraphics)) {
			throw new IllegalArgumentException("Graphics context not PrintGraphics");
		}
		// create stringreader and linereader objects
		StringReader sr = new StringReader(s);
		LineNumberReader lnr = new LineNumberReader(sr);
		String nextLine;
		// get the page height
		int pageHeight = printJob.getPageDimension().height;
		// set font to print with
		Font helv = new Font("Helvetica", Font.PLAIN, 8);
		pg.setFont(helv);
		// get dimensions of the fone
		FontMetrics fm = pg.getFontMetrics(helv);
		int fontHeight = fm.getHeight();
		int fontDescent = fm.getDescent();
		int curHeight = 0;
		try {
			do {
				// get next line from text area
				nextLine = lnr.readLine();
				if ((curHeight + fontHeight) > pageHeight) {
					// if we are over the page, create a new one
					pageNum ++;
					linesForThisPage = 0;
					pg.dispose();
					pg = printJob.getGraphics();
					if (pg != null) {
						pg.setFont(helv);
					}
					curHeight = 0;
				}
				curHeight += fontHeight;
				if (pg != null) {
					//draw the line to the painter
					pg.setColor(Color.BLACK);
					pg.drawString(nextLine, 20, curHeight - fontDescent + 20);
					linesForThisPage++;
					linesForThisJob++;
				} else {
					logger.error("printJob is null");
				}
			} while (nextLine != null);
		} catch (EOFException eof) {
			; // fine, ignore
		} catch (NullPointerException ne) {
			; // fine, ignore
		} catch (Exception ex) {
			logger.error("Error printing: " + ex.getMessage());
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
    protected Action demoteAction;
    protected Action deleteAction;
    protected Action copyAction;
    protected Action cutAction;
    protected Action pasteAction;
    protected Action printAction;
    protected Action insertAction;
    protected Action stripAction;
    
    private JButton  findButton = null;
    private JEditorPane docTextArea;
    private JList list;
    private JLabel statusLabel, workflowStatusLabel, wastebinLabel, locationLabel = null;
    private JPanel pane, locationPanel;
    private JMenuBar jJMenuBar = null;
    private JMenu fileMenu, editMenu, insertMenu = null;
    private JToolBar toolbar = null;    
    private DefaultListModel activityListModel= null;
    private WorkflowDnDTree tree = null;
    private JTabbedPane tabbedPaneWF;
    private JTextField  wfDocFindField, locationField;   
    private JScrollPane listView = null;
    private URL helpUrl;
    private int caretPos = 0;
    private StatusBar statusBar = null;
    private DefaultMutableTreeNode copiedNode; // used for cut and copy
    private WastebinDropListener w;
    public boolean autoPopUp = true;  
    private boolean workflowTranscript = false; // Indicate whether we are viewing a transcript    
    
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

		this.setJMenuBar(getJJMenuBar());
        setIconImage(IconHelper.loadIcon("tree.gif").getImage());        
        getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.workflowBuilder");  			
		tabbedPaneWF = new JTabbedPane();		
		tabbedPaneWF.addTab ("Tree View", IconHelper.loadIcon("tree.gif"), getTabbedTreePanel(), "Display workflow graphically");
		tabbedPaneWF.setMnemonicAt(0,KeyEvent.VK_T);
		tabbedPaneWF.addTab("Document", IconHelper.loadIcon("document.gif"), getTabbedDocPanel(), "Display text version of workflow");
		tabbedPaneWF.setMnemonicAt(1,KeyEvent.VK_D);
		tabbedPaneWF.addMouseListener(new MouseAdapter(){
	        public void mouseClicked(MouseEvent event)
	        {
	        	setStatusMessage("");
	        	if (((JTabbedPane)event.getSource()).getSelectedIndex() == 1) { // only if user is selecting text tab
            		populateTextArea();
	        	}
	        }
	    });
			
		pane = getJContentPane();
		pane.add(tabbedPaneWF, BorderLayout.CENTER);
		pane.add(getToolbar(), BorderLayout.NORTH);
		
		submitAction = new SubmitAction();
        loadAction = new LoadAction();
        createAction = new CreateAction(); 
        saveAction = new SaveAction();
        closeAction = new CloseAction();
        collapseAction = new CollapseAction();
        expandAction = new ExpandAction();
        promoteAction = new PromoteAction();
        demoteAction = new DemoteAction();
        deleteAction = new DeleteAction();
        copyAction = new CopyAction();
        cutAction = new CutAction();
        pasteAction = new PasteAction();
        printAction = new PrintAction(); 
        stripAction = new StripTranscriptAction();
        
        toolbar.add(createAction);
        toolbar.add(loadAction);
        toolbar.add(saveAction);
        toolbar.add(submitAction);
        toolbar.add(collapseAction);
        toolbar.add(expandAction);
        toolbar.add(promoteAction);
        toolbar.add(demoteAction);
        toolbar.add(deleteAction);
        toolbar.add(copyAction);
        toolbar.add(cutAction);
        toolbar.add(pasteAction);
        toolbar.add(printAction);
        toolbar.add(stripAction);
        toolbar.setRollover(true);
        toolbar.add(new JSeparator(SwingConstants.VERTICAL));

        toolbar.add(getLocationPanel());
        
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
        fileMenu.add(printAction);
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
        editMenu.add(demoteAction);
        editMenu.add(new JSeparator());
        editMenu.add(getInsertMenu());
        editMenu.add(stripAction);
        
        pane.add(getActivityList(),BorderLayout.WEST);
		this.setContentPane(pane);
		pack();
           this.setSize(800,800);        
		this.w = new WastebinDropListener();
		createAction.actionPerformed(null); // fire the create action to initialize everything.
        // keep guy happy.
        this.setStatusMessage("Drag activities from the list and drop them into the Tree View to build a Workflow");
        // set tree focus to root sequence
		TreePath treePath = new TreePath(getModel().getChild(getModel().getRoot(), 0));
		tree.scrollPathToVisible(treePath);
		tree.setSelectionPath(treePath);
		this.addWindowFocusListener( new WindowFocusListener(){
            public void windowGainedFocus(WindowEvent e) {
                if(getDialogRef() != null){
                }
            } 
            public void windowLostFocus(WindowEvent e) {           	
            }            
        });
	}
	
	/**
	 * Menu item to enable user to build workflows if DnD not present eg on Macs running Java 1.4
	 * @return JMenuItem
	 */
	private JMenu getInsertMenu() {
		insertAction = new InsertAction();
        insertMenu = new JMenu("Insert");
        insertMenu.setMnemonic(KeyEvent.VK_I);
        insertMenu.setToolTipText("If DnD not working use arrow keys to navigate tree and insert activities");
        JMenuItem activity1 = new JMenuItem(insertAction);
        activity1.setText("Step");
        activity1.setMnemonic(KeyEvent.VK_T);
        activity1.setIcon(IconHelper.loadIcon("icon_Step.gif"));
        JMenuItem activity2 = new JMenuItem(insertAction);
        activity2.setText("Flow");
        activity2.setMnemonic(KeyEvent.VK_L);
        activity2.setIcon(IconHelper.loadIcon("icon_Flow.gif"));
        JMenuItem activity3 = new JMenuItem(insertAction);
        activity3.setText("Sequence");
        activity3.setMnemonic(KeyEvent.VK_Q);
        activity3.setIcon(IconHelper.loadIcon("icon_Sequence.gif"));
        JMenuItem activity4 = new JMenuItem(insertAction);
        activity4.setText("If");
        activity4.setMnemonic(KeyEvent.VK_I);
        activity4.setIcon(IconHelper.loadIcon("icon_If.gif"));
        JMenuItem activity5 = new JMenuItem(insertAction);
        activity5.setText("Else");
        activity5.setMnemonic(KeyEvent.VK_E);
        activity5.setIcon(IconHelper.loadIcon("icon_Else.gif"));
        JMenuItem activity6 = new JMenuItem(insertAction);
        activity6.setText("Scope");
        activity6.setMnemonic(KeyEvent.VK_C);
        activity6.setIcon(IconHelper.loadIcon("icon_Scope.gif"));
        JMenuItem activity7 = new JMenuItem(insertAction);
        activity7.setText("Script");
        activity7.setMnemonic(KeyEvent.VK_P);
        activity7.setIcon(IconHelper.loadIcon("icon_Script.gif"));
        JMenuItem activity8 = new JMenuItem(insertAction);
        activity8.setText("Set");
        activity8.setMnemonic(KeyEvent.VK_S);
        activity8.setIcon(IconHelper.loadIcon("icon_Set.gif"));
        JMenuItem activity9 = new JMenuItem(insertAction);
        activity9.setText("Unset");
        activity9.setMnemonic(KeyEvent.VK_U);
        activity9.setIcon(IconHelper.loadIcon("icon_Unset.gif"));
        JMenuItem activity10 = new JMenuItem(insertAction);
        activity10.setText("For loop");
        activity10.setMnemonic(KeyEvent.VK_P);
        activity10.setIcon(IconHelper.loadIcon("icon_Loop.gif"));
        JMenuItem activity11 = new JMenuItem(insertAction);
        activity11.setText("Parallel loop");
        activity11.setMnemonic(KeyEvent.VK_R);
        activity11.setIcon(IconHelper.loadIcon("icon_Loop.gif"));
        JMenuItem activity12 = new JMenuItem(insertAction);
        activity12.setText("While loop");
        activity12.setMnemonic(KeyEvent.VK_W);
        activity12.setIcon(IconHelper.loadIcon("icon_Loop.gif"));
        insertMenu.add(activity1);
        insertMenu.add(activity2);
        insertMenu.add(activity3);
        insertMenu.add(activity4);
        insertMenu.add(activity5);
        insertMenu.add(activity6);
        insertMenu.add(activity7);
        insertMenu.add(activity8);
        insertMenu.add(activity9);
        insertMenu.add(activity10);
        insertMenu.add(activity11);
        insertMenu.add(activity12);
        return insertMenu;
	}
	
	/**
	 * Marshal workflow object and set docTextArea to this 
	 *
	 */
	private void populateTextArea() {
		caretPos = 0;
		wfDocFindField.setText("");
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
                String message = ex.getLocation().toString().replaceFirst("XPATH:", "Error at:");
                setStatusMessage("" + message);
                showError("Your workflow document contains invalid xml",ex);			                
        	} catch(Exception exc) {
        		logger.error("Error unmarshalling workflow object with validation turned off");
        	}	                    
        }
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
			list.setToolTipText("Drag activities from this list and drop them into the Tree View to build a workflow");
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
    	if (workflowTranscript)
    		return;
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
        	if (newStep != null && newStep.getResultVar().equalsIgnoreCase(""))
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
                	deleteNode(node);
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
    
    /**
     * activate menus as workflow is loaded or work is commenced on a workflow.
     * Do not activate menus when builder is being used as a transcript viewer.
     * Stop dnd from activity list etc
     */
    private void activateMenus() {
    	if (getModel().getWorkflow().getJobExecutionRecord() == null) {
    		workflowTranscript = false;
    		this.setTitle("Workflow Builder");
    		tree.allowDnD(true);
    		saveAction.putValue(Action.SHORT_DESCRIPTION,"Save this workflow");
    		demoteAction.setEnabled(true);
    		promoteAction.setEnabled(true);
    		expandAction.setEnabled(true);
    		collapseAction.setEnabled(true);
    		deleteAction.setEnabled(true);
    		cutAction.setEnabled(true);
    		copyAction.setEnabled(true);
    		pasteAction.setEnabled(true);
    		submitAction.setEnabled(true);
    		insertMenu.setEnabled(true);
    		stripAction.setEnabled(false);
    		pane.add(getActivityList(),BorderLayout.WEST);
    		setContentPane(pane);
    	} else {
    		workflowTranscript = true;
    		this.setTitle("Transcript Viewer");
    		setStatusMessage("Transcript Viewer - transcripts are not editable");
    		tree.allowDnD(false);
    		saveAction.putValue(Action.SHORT_DESCRIPTION,"Save this workflow transcript");
    		demoteAction.setEnabled(false);
    		promoteAction.setEnabled(false);
    		expandAction.setEnabled(true);
    		collapseAction.setEnabled(true);
    		deleteAction.setEnabled(false);
    		cutAction.setEnabled(false);
    		copyAction.setEnabled(false);
    		pasteAction.setEnabled(false);
    		submitAction.setEnabled(false);
    		insertMenu.setEnabled(false);
    		stripAction.setEnabled(true);
    		pane.remove((Component)getActivityList());
    		setContentPane(pane);
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
            scriptDialog = new ScriptDialog(this, chooser, vos);
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
			if (!workflowTranscript)
			    submitAction.setEnabled(true);
			workflowStatusLabel.setEnabled(true);
		} else {
			submitAction.setEnabled(false);	
			workflowStatusLabel.setEnabled(false);
		}
	}
	
	/**
	 * Open workflow transcript in viewer
	 * @param wf String
	 */
	public void showTranscript(String wf) {	
		try {
			Reader reader = new StringReader(wf);
			Workflow workflow = (Workflow)Unmarshaller.unmarshal(Workflow.class, reader);
			model.setWorkflow(workflow, false);
			reader.close();
			activateMenus();
			locationField.setText(null);
			locationPanel.setVisible(false);
			show();
			tree.expandAll(true);
		} catch(ValidationException ve) {
			logger.error("" + ve);
			showError("Unable to display transcript ",ve);
		} catch (MarshalException me) {
			logger.error("" + me);
			showError("Unable to display transcript ",me);
		} catch (IOException ioe) {
			logger.error("" + ioe);
			showError("Unable to display transcript ",ioe);
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

	/** Insert node. Logic rules applied
	 * 
	 */
	
	private String insertNode(DefaultMutableTreeNode parentNode, DefaultMutableTreeNode childNode) {		
			
		if (parentNode.getAllowsChildren() && 
			(parentNode.getUserObject() instanceof Flow ||
			 parentNode.getUserObject() instanceof Sequence)) {
			 getModel().insertNodeInto(childNode, parentNode, 0);
		} 
		else if ((!parentNode.getAllowsChildren() && 
				 (!parentNode.isRoot() && 
				  (parentNode.getUserObject() instanceof Step ||
				  parentNode.getUserObject() instanceof Script  ||
				  !(parentNode.getUserObject() instanceof String) ||
				  parentNode.getUserObject() instanceof For || 
				  parentNode.getUserObject() instanceof If || 
				  parentNode.getUserObject() instanceof While ||
				  parentNode.getUserObject() instanceof Parfor || 
				  parentNode.getUserObject() instanceof Scope ))))  {
			DefaultMutableTreeNode tempNode = parentNode;
			parentNode = (DefaultMutableTreeNode)tempNode.getParent();
			int index = parentNode.getIndex(tempNode);
			if (index == -1) 												
				return "Unable to insert activity";
			getModel().insertNodeInto(childNode, parentNode, index);
		} 
		else if (parentNode.getUserObject() instanceof String) {
			DefaultMutableTreeNode scriptNode = (DefaultMutableTreeNode)parentNode.getParent();
			DefaultMutableTreeNode scriptParentNode = (DefaultMutableTreeNode)scriptNode.getParent();
			int index = scriptParentNode.getIndex(scriptNode);
			getModel().insertNodeInto(childNode, scriptParentNode, index);
			}
		else if (parentNode.getUserObject() instanceof If && 
				 childNode.getUserObject() instanceof Else && 
				 parentNode.getChildCount() <= 1 ) {
					getModel().insertNodeInto(childNode, parentNode, 1);
			}		
		else if (parentNode.getUserObject() instanceof Then && 
				 childNode.getUserObject() instanceof Else &&
				 ((DefaultMutableTreeNode)parentNode.getParent()).getChildCount() <= 1) {
					DefaultMutableTreeNode ifNode = (DefaultMutableTreeNode)parentNode.getParent();
					getModel().insertNodeInto(childNode, ifNode, 1);
		}				
		else {
			return "Unable to insert activity";
		}
		 TreePath treePath = new TreePath(childNode.getFirstLeaf().getPath());
		 tree.scrollPathToVisible(treePath);
		 tree.setSelectionPath(treePath);
		if (autoPopUp)
			editNode(childNode, true);
		return "";
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
	
	private JPanel getLocationPanel() {
        locationPanel = new JPanel(new FlowLayout());
        locationLabel = new JLabel("Location: ");
        locationField = new JTextField(30);
        locationField.setEditable(false);  
        locationField.setToolTipText("Details where this workflow was loaded from");
        locationPanel.setToolTipText("Details where this workflow was loaded from");
        locationPanel.add(locationLabel);
        locationPanel.add(locationField);
        locationPanel.setVisible(false);
        return locationPanel;
	}
	
	private JDialog focusDialog = null;
	public void setDialogRef(JDialog c) {
		focusDialog = c;
	}
	
	private JDialog getDialogRef() {
		return focusDialog;
	}
	
	public void removeDialogRef() {
		focusDialog = null;
	}
} 
