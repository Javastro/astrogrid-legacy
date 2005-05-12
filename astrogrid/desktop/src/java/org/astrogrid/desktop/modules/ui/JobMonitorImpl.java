/*$Id: JobMonitorImpl.java,v 1.4 2005/05/12 15:37:35 clq2 Exp $
 * Created on 31-Mar-2005
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
import org.astrogrid.acr.astrogrid.Jobs;
import org.astrogrid.acr.astrogrid.Portal;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.UI;
import org.astrogrid.acr.ui.JobMonitor;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.transformers.WorkflowResultTransformerSet;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;
import org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType;

import EDU.oswego.cs.dl.util.concurrent.misc.SwingWorker;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 31-Mar-2005
 *
 */
public class JobMonitorImpl extends UIComponent implements JobMonitor, UserLoginListener {
    
    /** action to submit a job fo execution */
    protected final class SubmitAction extends AbstractAction {
        public SubmitAction() {
            super("Submit Workflow",IconHelper.loadIcon("run_tool.gif"));
            this.putValue(SHORT_DESCRIPTION,"Submit a workflow for execution");
            this.setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
            int result = getLoadFileChooser().showOpenDialog(JobMonitorImpl.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                final File f=getLoadFileChooser().getSelectedFile();            
                (new BackgroundOperation("Submitting Workflow") {

                    protected Object construct() throws Exception {
                        return jobs.submitJobFile(f.toURL());
                    }
                    protected void doFinished(Object o) {
                        JobURN urn = (JobURN)o;
                        JOptionPane.showMessageDialog(JobMonitorImpl.this,"Job URN: " + urn.getContent(),"Workflow Submitted",JOptionPane.INFORMATION_MESSAGE);
                    }
                    protected void doAlways() {
                        refresh();
                    }
                }).start();
            }
        }
    }
    
    protected final class CancelAction extends AbstractAction {
        protected CancelAction() {
            super("Cancel", IconHelper.loadIcon("stop.gif"));
            this.putValue(SHORT_DESCRIPTION,"Cancel the execution of this workflow");
            this.setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            final JobURN urn = tableModel.getRow(contentTable.getSelectedRow()).getJobId();
            (new BackgroundOperation("Cancelling Workflow"){

             protected Object construct() throws Exception {
                 jobs.cancelJob(urn);
                 return null;
             }
             protected void doAlways() {
                 refresh();
             }
            }).start();
        }
    }
    
    protected final class DeleteAction extends AbstractAction {

        public DeleteAction() {
            super("Delete", IconHelper.loadIcon("delete_obj.gif"));
            this.putValue(SHORT_DESCRIPTION,"Delete this workflow transcript");
            this.setEnabled(false);            
        }

        public void actionPerformed(ActionEvent e) {
            final JobURN urn = tableModel.getRow(contentTable.getSelectedRow()).getJobId();
           (new BackgroundOperation("Deleting Workflow"){

            protected Object construct() throws Exception {
                jobs.deleteJob(urn);
                return null;
            }
            protected void doAlways() {
                refresh();
            }
           }).start();
       }
    }
    
    
    protected final class JobsTableModel extends AbstractTableModel {

        protected WorkflowSummaryType[]  jobs = new WorkflowSummaryType[]{};
        public WorkflowSummaryType getRow(int rowIndex) {
            if (rowIndex < 0 || rowIndex >= jobs.length) {
                return null;
            } else {
            return jobs[rowIndex];
            }
        }
        private final static int COLUMN_COUNT = 5;
        /**
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        /**
         * @see javax.swing.table.TableModel#getRowCount()
         */
        public int getRowCount() {
            return jobs.length;
        }

        /**
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex >= jobs.length) {
                return null;               
            }
            if (columnIndex >= COLUMN_COUNT) {
                return null;
            }

            WorkflowSummaryType j = jobs[rowIndex];
            switch (columnIndex) {
                case 0:
                    return j.getWorkflowName();
                case 1:
                    return j.getStartTime();
                case 2:
                    return j.getFinishTime();
                case 3:                    
                    return fmt(j.getStatus());
                case 4:
                    
                    return j.getJobId().getContent();
                default:
                    return null;
            }
        }
        


        private String fmt(ExecutionPhase status) {
            StringBuffer sb = new StringBuffer("<html>");
            switch (status.getType()) {
                case ExecutionPhase.COMPLETED_TYPE:
                    sb.append("<font style='font-weight:bold'>"); break;
                case ExecutionPhase.ERROR_TYPE:
                    sb.append("<font style='color:red'>"); break;
                case ExecutionPhase.INITIALIZING_TYPE:
                    sb.append("<font style='color:blue'>"); break;
                case ExecutionPhase.PENDING_TYPE:
                    sb.append("<font>"); break;
                case ExecutionPhase.RUNNING_TYPE:
                    sb.append("<font style='color:green'>"); break;
                default:
                    sb.append("<font>"); // nothing
            }
            sb.append(status.toString());
            sb.append("</font></html>");
            return sb.toString();
        }

        public void setList(WorkflowSummaryType[] types) {
            this.jobs = types;
            this.fireTableDataChanged();
        }
        public String getColumnName(int column) {
            switch(column) {
                case 0: return "Name";
                case 1: return "Start";
                case 2: return "Finish";
                case 3: return "Status";
                case 4: return "JobURN";
                default: return "";
            }
        }
    }// end inner class
    
    protected final class RefreshAction extends AbstractAction {

        public RefreshAction() {
            super("Refresh",IconHelper.loadIcon("update.gif"));
            this.putValue(SHORT_DESCRIPTION,"Refresh the list now");
        }
      // no need to synchronize or anything - as only ever the event dispatch thread that calls this method.
        public void actionPerformed(ActionEvent e) {
            if (refreshWorker == null) { // otherwise a refresh is already taking place.
            this.setEnabled(false);
            refreshWorker = new RefreshWorker(); // seems we need to create a new object each time. pity.
            refreshWorker.start();        
            }
        }
    }
    
  
    
    /** worker thread that fetches job list */
   protected final class RefreshWorker extends BackgroundOperation {
        public RefreshWorker() {
            super("Refreshing job list");
        }
        protected Object construct() throws Exception {
            WorkflowSummaryType[] results = jobs.listSummaries();
            /* after bugfix jes-nww-776-agan, this is no longer necessary -makes things a lot faster.
            for (int i = 0; i < results.length; i++) {
               Workflow wf = jobs.getJob(results[i].getJobId());
               results[i].setFinishTime(wf.getJobExecutionRecord().getFinishTime());
               results[i].setStartTime(wf.getJobExecutionRecord().getStartTime());
               results[i].setStatus(wf.getJobExecutionRecord().getStatus());
               results[i].setDescription(wf.getDescription());
            }
            */
            return results;
           
        }
        protected void doFinished(Object o) {         
                tableModel.setList((WorkflowSummaryType[])o);
        } 
        protected void doAlways() {
                //remove reference to self.
                refreshWorker = null;
                refreshAction.setEnabled(true);
            }        
    }
    protected final class SaveAction extends AbstractAction {

        public SaveAction() {
            super("Save", IconHelper.loadIcon("fileexport.png"));
            this.putValue(SHORT_DESCRIPTION,"Save this workflow transcript to local disk");
            this.setEnabled(false);            
        }

        public void actionPerformed(ActionEvent e) {
            final JobURN urn = tableModel.getRow(contentTable.getSelectedRow()).getJobId();
            logger.debug(urn.getContent());
            int result = getSaveFileChooser().showSaveDialog(JobMonitorImpl.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                final File f=getSaveFileChooser().getSelectedFile();
                (new BackgroundOperation("Saving Workflow") {
                    protected Object construct() throws Exception {
                        Workflow wf = jobs.getJob(urn);
                        Writer w = new FileWriter(f);
                        wf.marshal(w);
                        w.close();
                        return null;
                    }
                }).start();

            }
        }
    }

	protected final class ViewPortalAction extends AbstractAction {


        protected ViewPortalAction() {
            super("View in Portal", IconHelper.loadIcon("export_log.gif"));
            this.putValue(SHORT_DESCRIPTION,"View this workflow transcript in the portal");
            this.setEnabled(false);            
        }

        public void actionPerformed(ActionEvent ignored) {
            Map params = new HashMap();
            params.put("action","read-job");
            JobURN urn = tableModel.getRow(contentTable.getSelectedRow()).getJobId();                        
            params.put("jobURN",urn.getContent());
            try {
            portal.openPageWithParams("main/mount/workflow/agjobmanager-job-status.html",params);
            } catch (Exception e) {
                showError("Failed to open portal",e);
            }
         
        }
    }
    protected  final class ViewAction extends AbstractAction {
        protected Transformer transformer;
        private ViewAction() throws TransformerConfigurationException, TransformerFactoryConfigurationError {            
            super("View", IconHelper.loadIcon("Resource.gif"));
            this.putValue(SHORT_DESCRIPTION,"View this workflow transcript");
            this.setEnabled(false);
            Source styleSource = WorkflowResultTransformerSet.Workflow2XhtmlTransformer.getStyleSource();
            transformer = TransformerFactory.newInstance().newTransformer(styleSource);
            transformer.setOutputProperty(OutputKeys.METHOD,"html");
                    
        }

        public void actionPerformed(ActionEvent e) {
            final JobURN urn = tableModel.getRow(contentTable.getSelectedRow()).getJobId();
            (new BackgroundOperation("Displaying Workflow") {
                protected Object construct() throws Exception {
                    File f = File.createTempFile("workflow",".html");
                    OutputStream out = new FileOutputStream(f);
                    Result result = new StreamResult(out);                    
                    Workflow wf = jobs.getJob(urn);
                    StringWriter sw = new StringWriter();
                    wf.marshal(sw);
                    sw.close();
                    Source source = new StreamSource(new StringReader(sw.toString()));
                    transformer.transform(source,result);
                    out.close();
                    browser.openURL(f.toURL());
                    return null;
                }
            }).start();


            }                    
    }

    protected final BrowserControl browser;
    protected Action submitAction;
    protected Action cancelAction;
    protected final Jobs jobs;
    // actions.
    protected Action deleteAction;
    protected final Portal portal;
    
    protected Action refreshAction;
        
    protected JobsTableModel tableModel;
    protected SwingWorker refreshWorker;
    protected Action saveAction;
    // timer..
    protected Timer timer;
    protected Action viewAction;
    protected Action viewPortalAction;
	private JTable contentTable = null;
	private JFileChooser fileChooser = null;  //  @jve:decl-index=0:visual-constraint="766,266"
    
 
    
	private JMenuBar jJMenuBar = null;
	private JLabel jLabel = null;
	private JMenu jobMenu = null;
	private JPanel jPanel1 = null;
	private JPanel jPanel2 = null;
		private JScrollPane jScrollPane = null;
	private JSlider refreshScale = null;
	private JToolBar toolbar = null;
	/**
	 * This is thedevelopment constructor only.
	 * @throws Exception
	 */
	public JobMonitorImpl() throws Exception {
		super();
		initialize();
        this.browser = null;
        this.portal = null;
        this.jobs = null;
	}
    
    /** production constructor 
     * @throws Exception*/
    public JobMonitorImpl(Community community,Portal portal, BrowserControl browser, UI ui, Configuration conf, Jobs jobs) throws Exception {
        super(conf,ui);
        this.browser = browser;
        this.portal = portal;
        this.jobs = jobs;
        //this.community = community;
        community.addUserLoginListener(this);
        initialize();
    }
    
    public void dispose() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }        
        super.dispose();
    }
    
    public void hide() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        super.hide();        
    }
    
    public void show() {
        super.show();
        requestFocus();
        if (timer != null && ! timer.isRunning()) {
            timer.start();
        }
    }
    /**
     * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogin(org.astrogrid.desktop.modules.ag.UserLoginEvent)
     */
    public void userLogin(UserLoginEvent e) {
        if (timer != null && !timer.isRunning()) {
            timer.start();
        }
    }
    /**
     * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogout(org.astrogrid.desktop.modules.ag.UserLoginEvent)
     */
    public void userLogout(UserLoginEvent e) {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }
	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */    
	private JTable getContentTable() {
		if (contentTable == null) {
            this.tableModel =new JobsTableModel();
			contentTable = new JTable();
            contentTable.setModel(tableModel);
            contentTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent e) {
                    int index = contentTable.getSelectedRow();                    
                    WorkflowSummaryType job = tableModel.getRow(index);                    
                    boolean value = index != -1;
                    cancelAction.setEnabled(value && job.getStatus().getType() < ExecutionPhase.COMPLETED_TYPE);
                    deleteAction.setEnabled(value && job.getStatus().getType() >= ExecutionPhase.COMPLETED_TYPE);
                    saveAction.setEnabled(value);
                    viewAction.setEnabled(value);
                    viewPortalAction.setEnabled(value);
                    if (value) {
                       setStatusMessage(job.getDescription());
                    }
                }
            });
			contentTable.setShowVerticalLines(false);
			contentTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			contentTable.setShowHorizontalLines(false);
            contentTable.getTableHeader().setReorderingAllowed(false);
		}
		return contentTable;
	}
	/**
	 * This method initializes jFileChooser	
	 * 	
	 * @return javax.swing.JFileChooser	
	 */    
	private JFileChooser getSaveFileChooser() {
		if (fileChooser == null) {
			fileChooser = new JFileChooser();

		}
        fileChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        fileChooser.setDialogTitle("Save Workflow..");          
        fileChooser.cancelSelection();        
		return fileChooser;
	}
    private JFileChooser getLoadFileChooser() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();

        }
        fileChooser.setDialogType(javax.swing.JFileChooser.OPEN_DIALOG);
        fileChooser.setDialogTitle("Submit Workflow..");        
        fileChooser.cancelSelection();
        return fileChooser;
    }    

	/**
	 * This method initializes jJMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */    
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getJobMenu());
		}
		return jJMenuBar;
	}
	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */    
	private JMenu getJobMenu() {
		if (jobMenu == null) {
			jobMenu = new JMenu();
			jobMenu.setText("Job");
		}
		return jobMenu;
	}

    /**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jLabel = new JLabel();
			jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.X_AXIS));
			jLabel.setText("Refresh Rate");
			jPanel1.add(jLabel, null);
			jPanel1.add(getRefreshScale(), null);
		}
		return jPanel1;
	}
	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			jPanel2 = new JPanel();
			jPanel2.setLayout(flowLayout1);
			flowLayout1.setAlignment(java.awt.FlowLayout.LEFT);
			jPanel2.add(getToolbar(), null);
			jPanel2.add(getJPanel1(), null);
		}
		return jPanel2;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getContentTable());
		}
		return jScrollPane;
	}
	/**
	 * This method initializes jSlider	
	 * 	
	 * @return javax.swing.JSlider	
	 */    
	private JSlider getRefreshScale() {
		if (refreshScale == null) {
			refreshScale = new JSlider();
			refreshScale.setMaximum(10);
			refreshScale.setValue(3);
            refreshScale.setMajorTickSpacing(1);
			refreshScale.setSnapToTicks(true);
			refreshScale.setPaintLabels(true);
			refreshScale.setPaintTicks(true);
            refreshScale.setToolTipText("Refresh rate (minutes)");
			refreshScale.addChangeListener(new javax.swing.event.ChangeListener() { 
				public void stateChanged(javax.swing.event.ChangeEvent e) {    
                    if (!refreshScale.getValueIsAdjusting()) { // check it's not just noise.
                        int value = refreshScale.getValue();
                        logger.debug("Changing refresh rate " + value);
                        timer.stop();
                        if (value > 0) {
                        timer.setDelay(value * 60 * 1000);
                        timer.start();
                        }
                    }
				}
			});
		}
		return refreshScale;
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
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() throws Exception {
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(ui.getComponent());
		this.setJMenuBar(getJJMenuBar());
		this.setSize(730, 350);
		JPanel pane = getJContentPane();
        
        //        jContentPane.add(getJPanel(), java.awt.BorderLayout.SOUTH);
         pane.add(getJPanel2(), java.awt.BorderLayout.NORTH);
         pane.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
		this.setContentPane(pane);
		this.setTitle("Jobs Monitor");
        // set up actions.
        submitAction = new SubmitAction();
        deleteAction = new DeleteAction();
        cancelAction = new CancelAction();       
        saveAction = new SaveAction();        
        viewAction = new ViewAction();
        viewPortalAction = new ViewPortalAction();
        refreshAction = new RefreshAction();
        
        toolbar.add(submitAction);
        toolbar.add(viewAction);
        toolbar.add(viewPortalAction);
        toolbar.add(saveAction);         
        toolbar.add(cancelAction);
        toolbar.add(deleteAction);    
        toolbar.add(new JSeparator());
        toolbar.add(refreshAction);
        
        jobMenu.add(submitAction);
        jobMenu.add(new JSeparator());        
        jobMenu.add(viewAction);
        jobMenu.add(viewPortalAction);
        jobMenu.add(saveAction);
        jobMenu.add(cancelAction);
        jobMenu.add(deleteAction);
        jobMenu.add(new JSeparator());
        jobMenu.add(refreshAction);
        
        
        timer = new Timer(3 * 60 * 1000, refreshAction);
        timer.setInitialDelay(0);
        timer.start();
    }


    /**
     * @see org.astrogrid.acr.ui.JobMonitor#refresh()
     */
    public void refresh() {
        // either refresh oursevles (if on event dsipatch thread), toehrwise invoke later.
        Runnable r = new Runnable() {

            public void run() {
                    refreshAction.actionPerformed(null);               
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"


/* 
$Log: JobMonitorImpl.java,v $
Revision 1.4  2005/05/12 15:37:35  clq2
nww 1111

Revision 1.3.8.1  2005/05/11 14:25:22  nw
javadoc, improved result transformers for xml

Revision 1.3  2005/04/27 13:42:40  clq2
1082

Revision 1.2.2.2  2005/04/25 19:49:35  nw
changed job monitor to take advantage of fixed bug 776

Revision 1.2.2.1  2005/04/25 11:18:50  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2  2005/04/13 12:59:18  nw
checkin from branch desktop-nww-998

Revision 1.1.2.6  2005/04/13 12:23:28  nw
refactored a common base class for ui components

Revision 1.1.2.5  2005/04/06 16:18:50  nw
finished icon set

Revision 1.1.2.4  2005/04/05 11:43:16  nw
added submit action
moved to using jobs component

Revision 1.1.2.3  2005/04/04 16:43:48  nw
made frames remember their previous positions.
synchronized guiLogin, so only one login box ever comes up.
made refresh action in jobmonitor more robust

Revision 1.1.2.2  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.1  2005/04/01 19:03:10  nw
beta of job monitor
 
*/