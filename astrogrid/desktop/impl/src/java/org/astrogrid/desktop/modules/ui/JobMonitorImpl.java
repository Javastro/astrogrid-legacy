/*$Id: JobMonitorImpl.java,v 1.4 2005/10/04 20:46:48 KevinBenson Exp $
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

import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.Jobs;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.HelpServer;
import org.astrogrid.acr.system.SystemTray;
import org.astrogrid.acr.ui.JobMonitor;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.system.transformers.WorkflowResultTransformerSet;

import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Document;

import EDU.oswego.cs.dl.util.concurrent.misc.SwingWorker;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
/** Implementation of the job monitor component
 * @author Noel Winstanley nw@jb.man.ac.uk 31-Mar-2005
 */
public class JobMonitorImpl extends UIComponent implements JobMonitor, UserLoginListener {
    
    private static final int APPLICATIONS_TAB = 1;
    private static final int JES_TAB = 0;
    /** action to submit a job fo execution */
    protected final class SubmitAction extends AbstractAction {
        public SubmitAction() {
            super("Submit Workflow",IconHelper.loadIcon("run_tool.gif"));
            this.putValue(SHORT_DESCRIPTION,"Submit a workflow for execution");
            this.setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
                final URI u = chooser.chooseResourceWithParent("Select workflow to submit",true, true, false, true,JobMonitorImpl.this);
                if (u == null) {
                    return;
                }                
                (new BackgroundOperation("Submitting Workflow") {

                    protected Object construct() throws Exception {
                        InputStream is = vos.getInputStream(u);
                        Document doc = XMLUtils.newDocument(is);          
                        return jobs.submitJob(doc);
                    }
                    protected void doFinished(Object o) {
                        URI urn = (URI)o;
                        ResultDialog rd = new ResultDialog(JobMonitorImpl.this,"Workflow Submitted\nJob URN: " + urn);
                    }
                    protected void doAlways() {
                        refresh();
                    }
                }).start();
            }        
    }
    
    protected final class CancelAction extends AbstractAction {
        protected CancelAction() {
            super("Cancel", IconHelper.loadIcon("stop.gif"));
            this.putValue(SHORT_DESCRIPTION,"Cancel the execution of this process");
            this.setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            if (getPanes().getSelectedIndex() == JES_TAB) {
            final URI urn = jobsTableModel.getRow(jobsTable.getSelectedRow()).getId();
            (new BackgroundOperation("Cancelling Workflow"){

             protected Object construct() throws Exception {
                 jobs.cancelJob(urn);
                 return null;
             }
             protected void doAlways() {
                 refresh();
             }
            }).start();
            } else {
                final URI id = applicationsTableModel.getRow(applicationsTable.getSelectedRow()).getId();
                (new BackgroundOperation("Cancelling Application") {

                    protected Object construct() throws Exception {
                        applications.cancel(id);
                        return null;
                    }
                    protected void doAlways() {
                        refresh();
                    }
                    
                }).start();
            }
        }
    }
    
    protected final class DeleteAction extends AbstractAction {

        public DeleteAction() {
            super("Delete", IconHelper.loadIcon("delete_obj.gif"));
            this.putValue(SHORT_DESCRIPTION,"Delete this process");
            this.setEnabled(false);            
        }

        public void actionPerformed(ActionEvent e) {
            if (getPanes().getSelectedIndex() == JES_TAB) {            
                final URI urn = jobsTableModel.getRow(jobsTable.getSelectedRow()).getId();
                (new BackgroundOperation("Deleting Workflow"){

            protected Object construct() throws Exception {
                jobs.deleteJob(urn);
                return null;
            }
            protected void doAlways() {
                refresh();
            }
           }).start();
       } else {
           ExecutionInformation es=  applicationsTableModel.getRow(applicationsTable.getSelectedRow());
           try {
            applicationsTableModel.removeApplication(es)        ;
        } catch (IOException e1) {
            showError("Could not write back list of managed applicaitons",e1);
        }
        }
        }
    }
    
    protected class ApplicationsTableModel extends AbstractTableModel {
        private static final String MONITORED_APPLICATION_KEY = "monitored.applications.list";
        public ApplicationsTableModel()  {
            String value = configuration.getKey(MONITORED_APPLICATION_KEY);
            logger.info(value);
            props = new HashMap();
            if (value != null) {
                StringTokenizer tok = new StringTokenizer(value);                
                logger.info("Reading " + tok.countTokens() +" persisted application records");
                while(tok.hasMoreTokens()) {
                    String next = tok.nextToken();
                    int pos = next.indexOf('#');
                    try {
                    ExecutionInformation e = new ExecutionInformation(
                            new URI(next)
                            ,next.substring(0,pos)
                            ,"an application"
                            ,ExecutionInformation.UNKNOWN
                            ,null
                            ,null                            
                    );
                    appList.add(e);
                    } catch (URISyntaxException e) {
                        logger.warn("Could not unpickle record " + next,e);
                    }
                    
                }
            }
        }
        private final List appList = new ArrayList();
        private final Map props;
        
        public void addApplication(ExecutionInformation e) throws IOException {
            int pos = appList.size();
            appList.add(e);
           this.fireTableRowsInserted(pos,pos);
           props.put(e.getId() ,e.getName());
           writeBackProperties();
        }
        
        private void writeBackProperties() throws IOException {
            StringBuffer buff = new StringBuffer();
            for (Iterator i = props.keySet().iterator(); i.hasNext(); ) {
                buff.append(i.next());
                buff.append(" ");
            }                    
            configuration.setKey(MONITORED_APPLICATION_KEY,buff.toString());
        }
        
        public void removeApplication(ExecutionInformation e) throws IOException {  
            appList.remove(e);            
            this.fireTableDataChanged();
            props.remove(e.getId());
            writeBackProperties();
        }

        public ExecutionInformation getRow(int i) {
            return (ExecutionInformation)appList.get(i);
        }
        
        public void setRow(int i, ExecutionInformation new1) {
            appList.set(i,new1);
        }
        public int getColumnCount() {            
            return COLUMN_COUNT;
        }
        
        public int getRowCount() {
            return appList.size();
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex >= appList.size()){
                return null;               
            }
            if (columnIndex >= COLUMN_COUNT) {
                return null;
            }

            ExecutionInformation e = (ExecutionInformation)appList.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return e.getName();
                case 1:
                    return fmt(e.getStatus());
                case 2:
                    return e.getId();
                default:
                    return null;
            }          
        }
        private final static int COLUMN_COUNT = 3;
        public String getColumnName(int column) {
            switch(column) {
                case 0: return "Name";
                case 1: return "Status";
                case 2: return "ID";
                default: return "";
            }
        }


    }
    protected class JobsTableModel extends AbstractTableModel {

        protected ExecutionInformation[]  jobs = new ExecutionInformation[]{};
        public ExecutionInformation getRow(int rowIndex) {
            if (rowIndex < 0 || rowIndex >= jobs.length) {
                return null;
            } else {
            return jobs[rowIndex];
            }
        }
        private final static int COLUMN_COUNT = 5;

        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public int getRowCount() {
            return jobs.length;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex >= jobs.length) {
                return null;               
            }
            if (columnIndex >= COLUMN_COUNT) {
                return null;
            }

            ExecutionInformation j = jobs[rowIndex];
            switch (columnIndex) {
                case 0:
                    return j.getName();
                case 1:
                    return j.getStartTime();
                case 2:
                    return j.getFinishTime();
                case 3:                    
                    return fmt(j.getStatus());
                case 4:
                    
                    return j.getId();
                default:
                    return null;
            }
        }
        




        public void setList(ExecutionInformation[] latest) {
            this.jobs = latest;
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
            this.putValue(SHORT_DESCRIPTION,"Refresh the process list now");
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
            ExecutionInformation[] results = jobs.listFully();
            for (int i = 0; i < applicationsTableModel.getRowCount(); i++) {
                ExecutionInformation e = applicationsTableModel.getRow(i);
                // updating status inplace in the model - but not firing notification.
                ExecutionInformation eNew = applications.getExecutionInformation(e.getId());
                String newPhase = eNew.getStatus();
                if (e.getStatus() == null || ! newPhase.equals(e.getStatus())) {
                    applicationsTableModel.setRow(i,eNew);                                        
                    if (tray != null  && !(e.getStatus().equals(ExecutionInformation.COMPLETED) || e.getStatus().equals(ExecutionInformation.ERROR))) {
                            if (newPhase.equals(ExecutionInformation.COMPLETED)) {
                                tray.displayInfoMessage("Application Complete",e.getName());
                            } else if (newPhase.equals(ExecutionInformation.ERROR)) {
                                    tray.displayWarningMessage("Application Error",e.getName());                               
                        }
                    }
                }
            }
            return results;
           
        }
        protected void doFinished(Object o) {   
               ExecutionInformation[] latest = (ExecutionInformation[])o;
               if (tray != null) { // no point if the tray isn't available.
                   alertCompleted(latest);
               }
                jobsTableModel.setList(latest);
                applicationsTableModel.fireTableDataChanged();
        } 
        /** if the system tray is present, popup alertrts when jobs complete */
       protected void alertCompleted(ExecutionInformation[] latest) {
           for (int i =0; i < jobsTableModel.getRowCount(); i++) {
               ExecutionInformation current = jobsTableModel.getRow(i);
               if (! (current.getStatus().equals(ExecutionInformation.COMPLETED) 
                       ||  current.getStatus().equals(ExecutionInformation.ERROR) )) {
                   ExecutionInformation updated = findSummaryFor(current.getId(),latest);
                   if (updated == null) {
                       continue;
                   }
                       if (updated.getStatus().equals(ExecutionInformation.COMPLETED) ){
                           tray.displayInfoMessage("Workflow Complete",current.getName());
                       }else if (updated.getStatus().equals(ExecutionInformation.ERROR)) {
                           tray.displayWarningMessage("Workflow Error",current.getName());
                   }
                   }                   
               }           
       }
       protected ExecutionInformation findSummaryFor(URI urn,ExecutionInformation[] l) {
           for (int i = 0; i < l.length; i++) {
               if (l[i].getId().equals(urn)) {
                   return l[i];
               }
           }
               return null;                          
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
            this.putValue(SHORT_DESCRIPTION,"Save a copy of execution results");
            this.setEnabled(false);            
        }

        public void actionPerformed(ActionEvent e) {

                if (getPanes().getSelectedIndex() == JES_TAB) {
                    final URI u = chooser.chooseResourceWithParent("Save workflow transcript",true,true, false, true,JobMonitorImpl.this);
                    if (u == null) {
                        return;
                    }                    
                    final URI urn = jobsTableModel.getRow(jobsTable.getSelectedRow()).getId();                
                    (new BackgroundOperation("Saving Workflow Transcript") {                                               
                        protected Object construct() throws Exception {                   
                            Document wf = jobs.getJobTranscript(urn);
                            OutputStream os = vos.getOutputStream(u);
                            XMLUtils.DocumentToStream(wf,os);                       
                            return null;
                        }
                    }).start();
                } else {
                    final URI u = chooser.chooseResource("Save Execution Results",true);
                    if (u == null) {
                        return;
                    }                         
                    final URI id = applicationsTableModel.getRow(applicationsTable.getSelectedRow()).getId();
                    (new BackgroundOperation("Saving Execution Results") {
                        protected Object construct() throws Exception {
                            Map m = applications.getResults(id);
                            Writer w= new OutputStreamWriter(vos.getOutputStream(u));
                            if (m.size() == 1) {
                                w.write(m.values().iterator().next().toString());
                            } else {                                    
                                w.write(m.toString());
                            }
                            w.close();
                            return null;
                        }
                    }).start();
                }

            }
        
    }


    protected  final class ViewAction extends AbstractAction {
        protected Transformer workflowTransformer;
        private ViewAction() throws TransformerConfigurationException, TransformerFactoryConfigurationError {            
            super("View", IconHelper.loadIcon("Resource.gif"));
            this.putValue(SHORT_DESCRIPTION,"View execution results");
            this.setEnabled(false);
            Source styleSource = WorkflowResultTransformerSet.Workflow2XhtmlTransformer.getStyleSource();
            workflowTransformer = TransformerFactory.newInstance().newTransformer(styleSource);
            workflowTransformer.setOutputProperty(OutputKeys.METHOD,"html");  
                    
        }

        public void actionPerformed(ActionEvent e) {
            if (getPanes().getSelectedIndex() == JES_TAB) {                    
            final URI urn = jobsTableModel.getRow(jobsTable.getSelectedRow()).getId();
            (new BackgroundOperation("Displaying Workflow") {
                protected Object construct() throws Exception {
                    File f = File.createTempFile("workflow",".html");
                    OutputStream out = new FileOutputStream(f);
                    Result result = new StreamResult(out);                    
                    Document doc = jobs.getJobTranscript(urn);
                    // necessary, as xslt won't work over a DOMSource, for some unknown reason.
                    String docString = XMLUtils.DocumentToString(doc);                    
                    Source source = new StreamSource(new ByteArrayInputStream(docString.getBytes()));
                    workflowTransformer.transform(source,result);
                    out.close();
                    browser.openURL(f.toURL());
                    return null;
                }
            }).start();
            } else {
                final URI id = applicationsTableModel.getRow(applicationsTable.getSelectedRow()).getId();
                (new BackgroundOperation("Displaying  Execution Summary") {
                    protected Object construct() throws Exception {
                        File f = File.createTempFile("application",".txt");
                        Writer out = new FileWriter(f);
                        Map results = applications.getResults(id);
                        if (results.size() == 1) {
                            out.write(results.values().iterator().next().toString());
                        } else {
                            out.write(results.toString());
                        }
                        out.close();
                        browser.openURL(f.toURL());
                        
                        return null;
                    }                
            }).start();
            }
            }                    
    }

    protected final BrowserControl browser;
    protected final SystemTray tray;
    protected Action submitAction;
    protected Action cancelAction;
    protected final Jobs jobs;
    protected final Applications applications;
    protected final MyspaceInternal vos;
    protected final ResourceChooserInternal chooser;
    // actions.
    protected Action deleteAction;
    
    protected Action refreshAction;
        
    protected JobsTableModel jobsTableModel;
    protected SwingWorker refreshWorker;
    protected Action saveAction;
    // timer..
    protected Timer timer;
    protected Action viewAction;
	private JTable jobsTable = null;
    
 
    
	private JMenuBar jJMenuBar = null;
	private JLabel jLabel = null;
	private JMenu jobMenu = null;
	private JPanel jPanel1 = null;
	private JPanel jPanel2 = null;
	private JSlider refreshScale = null;
	private JToolBar toolbar = null;

    
    /** production constructor - for platforms without system tray
     * @throws Exception*/
    public JobMonitorImpl(Community community,MyspaceInternal vos,BrowserControl browser, UIInternal ui, HelpServer hs,Configuration conf, Jobs jobs, Applications applications, ResourceChooserInternal chooser) throws Exception {
        this(community,vos,browser,ui,hs,conf,jobs,applications,chooser,null);
 
    }
    
    /** constructor for platforms with system tray */ 
    public JobMonitorImpl(Community community,MyspaceInternal vos,BrowserControl browser, UIInternal ui, HelpServer hs,Configuration conf, Jobs jobs, Applications applications, ResourceChooserInternal chooser,SystemTray tray) throws Exception {
        super(conf,hs,ui);
        this.browser = browser;
        this.vos = vos;
        this.jobs = jobs;
        this.applications = applications;
        this.tray = tray;
        this.chooser = chooser;
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
	private JTable getJobsTable() {
		if (jobsTable == null) {
            this.jobsTableModel =new JobsTableModel();
			jobsTable = new JTable();
            jobsTable.setModel(jobsTableModel);
            jobsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                int previous = -1;
                public void valueChanged(ListSelectionEvent e) {
                    int index = jobsTable.getSelectedRow();
                    if (index == previous) {
                        return;
                    }
                    previous = index;
                    ExecutionInformation job = jobsTableModel.getRow(index);                    
                    boolean value = index != -1;
                    deleteAction.setEnabled(value && (job.getStatus().equals(ExecutionInformation.COMPLETED) 
                            ||  job.getStatus().equals(ExecutionInformation.ERROR) ) );
                    cancelAction.setEnabled(value && !deleteAction.isEnabled() );
                    saveAction.setEnabled(value);
                    viewAction.setEnabled(value);
                    if (value) {
                       setStatusMessage(job.getDescription());
                    }
                }
            });
			jobsTable.setShowVerticalLines(false);
			jobsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			jobsTable.setShowHorizontalLines(false);
            jobsTable.getTableHeader().setReorderingAllowed(false);
		}
		return jobsTable;
	}
    
    private ApplicationsTableModel getApplicationsTableModel() {
        if (applicationsTableModel == null) {
            try {
                applicationsTableModel = new ApplicationsTableModel();
                } catch (Exception e) {
                    logger.error(e);
                    showError("Could not read application list from store",e);
                }
        }
        return applicationsTableModel;
    }
        
    
    private JTable getApplicationsTable() {
        if (applicationsTable == null) {
            applicationsTable = new JTable();
            applicationsTable.setModel(getApplicationsTableModel());
            applicationsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                int previous = -1;
                public void valueChanged(ListSelectionEvent e) {
                    int index = applicationsTable.getSelectedRow();
                    if (index == previous) {
                        return;
                    }
                    previous = index;
                    boolean value = index != -1;                    
                        ExecutionInformation ex = value ? applicationsTableModel.getRow(index) : null;   

                    deleteAction.setEnabled(value && (ex.getStatus().equals(ExecutionInformation.COMPLETED) 
                            ||  ex.getStatus().equals(ExecutionInformation.ERROR) ) );
                    cancelAction.setEnabled(value && !deleteAction.isEnabled() );                    
                    saveAction.setEnabled(value);
                    viewAction.setEnabled(value);

                }
            });            
            applicationsTable.setShowVerticalLines(false);
            applicationsTable.setShowHorizontalLines(false);
            applicationsTable.getTableHeader().setReorderingAllowed(false);
            applicationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        return applicationsTable;
    }
    private JTable applicationsTable;
    private ApplicationsTableModel applicationsTableModel;


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
    
    private JTabbedPane panes;
    private JTabbedPane getPanes() {
        if (panes == null) {
            panes = new JTabbedPane();
            panes.addTab("Workflows",IconHelper.loadIcon("debugt_obj.gif"), new JScrollPane(getJobsTable()));
            panes.addTab("Applications",IconHelper.loadIcon("thread_view.gif"), new JScrollPane(getApplicationsTable()));
            panes.addChangeListener(new ChangeListener() {// when tabs are flipped

                public void stateChanged(ChangeEvent e) {
                    // remove any selected items.- which should mean that all buttons are cleared, etc.
                    jobsTable.clearSelection();
                    applicationsTable.clearSelection();
                    if (panes.getSelectedIndex() == 1) { // applications
                        submitAction.setEnabled(false);
                    } else {
                        submitAction.setEnabled(true);
                    }
                }

            });
        }
        return panes;
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
         pane.add(getPanes(), java.awt.BorderLayout.CENTER);
		this.setContentPane(pane);
		this.setTitle("Jobs Monitor");
        // set up actions.
        submitAction = new SubmitAction();
        deleteAction = new DeleteAction();
        cancelAction = new CancelAction();       
        saveAction = new SaveAction();        
        viewAction = new ViewAction();
        refreshAction = new RefreshAction();
        
        toolbar.add(submitAction);
        toolbar.add(viewAction);
        toolbar.add(saveAction);         
        toolbar.add(cancelAction);
        toolbar.add(deleteAction);    
        toolbar.add(new JSeparator());
        toolbar.add(refreshAction);
        
        jobMenu.add(submitAction);
        jobMenu.add(new JSeparator());        
        jobMenu.add(viewAction);
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

    /**
     * @see org.astrogrid.acr.ui.JobMonitor#addApplication(java.lang.String)
     */
    public void addApplication(String name,URI executionId) {
        ExecutionInformation e = new ExecutionInformation(
                executionId
                ,name
                ,"an application"
                ,ExecutionInformation.UNKNOWN
                ,null
                ,null
                );
        try {
            getApplicationsTableModel().addApplication(e);
            refresh();
        } catch (IOException e1) {
            showError("Failed to add applicaiton",e1);
        }

    }

    /**
     * @see org.astrogrid.acr.ui.JobMonitor#displayApplicationTab()
     */
    public void displayApplicationTab() {
       getPanes().setSelectedIndex(APPLICATIONS_TAB);
    }

    /**
     * @see org.astrogrid.acr.ui.JobMonitor#displayJesTab()
     */
    public void displayJobTab() {
        getPanes().setSelectedIndex(JES_TAB);
    }
    
    
    private String fmt(String status) {
        StringBuffer sb = new StringBuffer("<html>");
        if (status == null) {
            sb.append("<font style='color:blue'>Unknown</font>");
        }else {
        if (ExecutionInformation.COMPLETED.equals(status)) {
                sb.append("<font style='font-weight:bold'>"); 
        } else  if (ExecutionInformation.ERROR.equals(status)) {
                sb.append("<font style='color:red'>");
        } else if(ExecutionInformation.INITIALIZING.equals(status)) {              
                sb.append("<font style='color:blue'>"); 
        } else if (ExecutionInformation.PENDING.equals(status)) {              
                sb.append("<font>"); 
        } else if (ExecutionInformation.RUNNING.equals(status)) {               
                sb.append("<font style='color:green'>"); 
        } else {
                sb.append("<font>"); // nothing
        }
        sb.append(status);
        }
        sb.append("</font></html>");
        return sb.toString();
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"


/* 
$Log: JobMonitorImpl.java,v $
Revision 1.4  2005/10/04 20:46:48  KevinBenson
new datascope launcher and change to module.xml for it.  Vospacebrowserimpl changes to handle file copies to directories on import and export

Revision 1.3  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.11  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.10  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.9  2005/07/08 11:08:01  nw
bug fixes and polishing for the workshop

Revision 1.8  2005/06/22 08:48:52  nw
latest changes - for 1.0.3-beta-1

Revision 1.7  2005/06/20 16:56:40  nw
fixes for 1.0.2-beta-2

Revision 1.6  2005/06/08 14:51:59  clq2
1111

Revision 1.3.8.2  2005/06/02 14:34:33  nw
first release of application launcher

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