/**
 * 
 */
package org.astrogrid.desktop.modules.ui.execution;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.sourceforge.hiveutils.service.ObjectBuilder;

import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.AbstractProcessMonitor;
import org.astrogrid.desktop.modules.ag.ProcessMonitor;
import org.astrogrid.desktop.modules.ag.RemoteProcessManagerInternal;
import org.astrogrid.desktop.modules.ag.ProcessMonitor.ProcessEvent;
import org.astrogrid.desktop.modules.ag.ProcessMonitor.ProcessListener;
import org.astrogrid.desktop.modules.ivoa.resource.HtmlBuilder;
import org.astrogrid.desktop.modules.system.ExtendedFileSystemManager;
import org.astrogrid.desktop.modules.system.ui.ActivitiesManager;
import org.astrogrid.desktop.modules.system.ui.ActivityFactory;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.ObservableConnector;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileModel;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectComparator;
import org.astrogrid.desktop.modules.ui.fileexplorer.OperableFilesList;
import org.astrogrid.desktop.modules.ui.fileexplorer.VFSOperationsImpl;

import com.l2fprod.common.swing.JTaskPane;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.ObservableElementList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import ca.odell.glazedlists.swing.JEventListPanel;

/** Tracks the execution of a series of remote processes
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 16, 20072:06:17 PM
 */
public class ExecutionTracker{
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ExecutionTracker.class);


	private final TypesafeObjectBuilder uiBuilder;

    private ActivitiesManager activities;

    private final UIComponent parent;


    private final VFSOperationsImpl vfsOps;


    private final ExtendedFileSystemManager vfs;

    public ExecutionTracker(UIComponent parent,RemoteProcessManagerInternal rpmi, TypesafeObjectBuilder uiBuilder, ActivityFactory actFact, ExtendedFileSystemManager vfs) {
		super();
        this.vfs = vfs;
		logger.debug("creating executioon tracker");
        this.parent = parent;
		this.rpmi = rpmi;
        this.uiBuilder = uiBuilder;
        this.activities = actFact.create(parent);
        logger.debug("created activities");
		monitors = new BasicEventList();
		// wrap the monitors list with a proxy that fires all updates on the EDT
		// means that monitors can be added to the list from any thread, and the 
		// UI will update correctluy.
		TransformedList proxyList = GlazedListsSwing.swingThreadProxyList(monitors);
		// map each monitor to a bean of UI components.
		EventList components = new FunctionList(proxyList, new FunctionList.Function() {
			public Object evaluate(Object sourceValue) {
				return new ProcessMonitorDisplay((ProcessMonitor)sourceValue);
			}
		});
		this.vfsOps = uiBuilder.createVFSOperations(parent,new VFSOperationsImpl.Current() {
		    // provides a way of getting at the current directory
            public FileObject get() {
                //@fixme implement to get the currently selected item in the monitors list.
                return null;
            }
		});
		// make this a self-observing list.
		EventList observing = new ObservableElementList(components,new ObservableConnector());

		// layout this list of beans of components to a panel
		panel = new JEventListPanel(observing, new TrackerFormat());
		panel.setElementColumns(1);


	}
	private final EventList monitors;

	private JEventListPanel panel;

	// necessary so I can delete the monitors.
	private final RemoteProcessManagerInternal rpmi;
	
	/** access the task pane for working with activities */
	public JTaskPane getTaskPane() {
	    return activities.getTaskPane();
	}
	
	
	/** add a monitor for the specified id to the tracker 
     * @threads can be called on any thread - notifications and updates to display
     * are dispatched onto the EDT by this component.	 
	 * */
	public void add(URI execId) {
		ProcessMonitor m = rpmi.findMonitor(execId);
		if (m != null) {
			add(m);
		}
	}
	
	
	/** add a new monitor to the tracker 
	 * @threads can be called on any thread - notifications and updates to display
	 * are dispatched onto the EDT by this component.
	 * */
	public void add(ProcessMonitor pm) {
		monitors.add(pm);
	}
	public void remove(ProcessMonitor pm) {
	    monitors.remove(pm);
	}
	public JPanel getPanel() {
		return panel;
	}


	private static DateFormat df = SimpleDateFormat.getDateTimeInstance(); 
	/** container class that holds the ui components required to display a single process monitor
	 * 
	 *  listens to the process manager, and updates ui components on changes.
	 *  acts as an observable, to notify managing list when this has changed.
	 *  
	 *  observable notifications and ui component updates are lifted onto the EDT.
	 *  the original notification from ProcessManager will not be on EDT.
	 * 
	 * 
	 * tightly integrates with the TrackerFormat.
	 * @author Noel.Winstanley@manchester.ac.uk
	 * @since Jul 16, 20073:14:55 PM
	 */
	private class ProcessMonitorDisplay extends Observable implements ProcessListener, ActionListener {

	    /** constructor is always called on the EDT, 
	     * however, need to delegate to the EDT when receiving
	     * events from the processMonitor itself.
	     * @param pm
	     */
        public ProcessMonitorDisplay(ProcessMonitor pm) {
			super();
			this.pm = pm;
			pm.addProcessListener(this);
			messageLabel.setFont(UIConstants.SMALL_DIALOG_FONT);
			
			files = new BasicEventList();
			deleteButton = new JButton(IconHelper.loadIcon("stop16.png"));
			deleteButton.putClientProperty("is3DEnabled",Boolean.FALSE);
			deleteButton.setRolloverEnabled(true);
			deleteButton.setBorderPainted(false);
			deleteButton.setToolTipText("Cancel task");
			deleteButton.addActionListener(this);
			
			refreshButton = new JButton(IconHelper.loadIcon("reload16.png"));
            refreshButton.putClientProperty("is3DEnabled",Boolean.FALSE);
            refreshButton.setBorderPainted(false);
            refreshButton.setRolloverEnabled(true);
            refreshButton.setToolTipText("Refresh task");
            refreshButton.addActionListener(this);
            
			FileModel model = uiBuilder.createFileModel(new SortedList(files,FileObjectComparator.getInstance())
			    ,activities,vfsOps);
			results = uiBuilder.createOperableFilesList(model);
					populateMsgLabel();			
					populateStatusLabel();			
					populateTitleLabel();
					triggerUpdate();
		}

		private final ProcessMonitor pm;
		private final JLabel messageLabel = new JLabel();
		private final JLabel status = new JLabel(UIConstants.PENDING_ICON);
		private final JLabel title = new JLabel();
		private final EventList files;
		private final JButton deleteButton;
		private final JButton refreshButton;
		
		private final OperableFilesList results ;
		public JComponent getComponent(int ix) {
			switch(ix) {
			case 0:
				return title;
			case 1:
				return status;
			case 2:
			    return refreshButton;
			case 3:
			    return deleteButton;
			case 4:
			    return messageLabel;
			case 5:
			    return results;
			default:
				return new JLabel("invalid index: " + ix);
			}
		}

		private void triggerUpdate() {
			super.setChanged();
			super.notifyObservers();			
		}

		// need to delegate to the edt here.
		public void messageReceived(ProcessEvent ev) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {			
					populateMsgLabel();		
					triggerUpdate();
				}
			});
		}
		public void resultsReceived(ProcessEvent ev) {
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {	
				    populateResults();
					triggerUpdate();
				}
			});
		}
		public void statusChanged(ProcessEvent ev) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {			
					populateStatusLabel();
					populateTitleLabel();
					triggerUpdate();			
				}
			});
		}
		
		/** not to be called before task has been 'init()' - as it's not got an ID at that stage */
		private void populateResults() {
		    if (!pm.started()) {
		        // not started - so won't have any results to retrieve.
		        return;
		    }
		    logger.debug("Fetching results");
		    
		    (new BackgroundWorker(parent,"Listing results") {

                protected Object construct() throws Exception {
                        FileObject resultFolder = vfs.resolveFile("task:/" + URLEncoder.encode(pm.getId().toString()));
                        // resultFolder = vfs.getResultsFilesystem().resolveFile("/" + URLEncoder.encode(pm.getId().toString()));
                        logger.debug(resultFolder);
                        logger.debug(resultFolder.getClass().getName());
                        return Arrays.asList(resultFolder.getChildren());
                }
                protected void doFinished(Object result) {
                    List contents = (List)result;
                    logger.debug(contents);
                    files.clear();
                    files.addAll(contents);
                }
                protected void doError(Throwable ex) {
                    messageLabel.setText(ex.getMessage());
                    logger.warn("Failed to fetch results",ex);
                }
		    }).start();
		}
		
		private void populateMsgLabel() {
			try {
				ExecutionMessage[] messages = pm.getMessages();
				if (messages.length > previousMsgCount) { // new messages seen.
				    previousMsgCount = messages.length;
				    // set label to content of latest message
					messageLabel.setText(messages[messages.length-1].getContent());
					
					// put transcript of all other messages into the tooltip 
					HtmlBuilder builder = new HtmlBuilder();
					for (int i = 0; i < messages.length; i++) {
                        ExecutionMessage m = messages[i];
                        if (m.getSource().equals(AbstractProcessMonitor.MONITOR_MESSAGE_SOURCE)
                                && m.getLevel().equals(LogLevel.INFO.toString())) {
                            continue; // not interesting.
                        }
                        builder.append(df.format(m.getTimestamp()))
                                .append(" Status: ")
                                .append(m.getStatus());
                        builder.br().appendWrap(m.getContent(),50);
                        builder.p();        
                                 
                    }
					messageLabel.setToolTipText(builder.toString());
				}
			} catch (ACRException x) {
				messageLabel.setText("inaccessible: " + x.getMessage());
			}
		}
		private int previousMsgCount = 0;
		// can share this - as is only ever run on EDT.
		private void populateStatusLabel() {
			String st = pm.getStatus();
			if (st.equalsIgnoreCase("error")) {
			    alterButton();
				status.setIcon(UIConstants.ERROR_ICON);
				populateResults();
			} else if (st.equalsIgnoreCase("completed")) {
				status.setIcon(UIConstants.COMPLETED_ICON);
				alterButton();
				populateResults();
			} else if (st.equalsIgnoreCase("pending")) {
				status.setIcon(UIConstants.PENDING_ICON);
			} else if (st.equalsIgnoreCase("running")) {
				status.setIcon(UIConstants.RUNNING_ICON);
			} else {
				status.setIcon(UIConstants.UNKNOWN_ICON);
			}
			status.setToolTipText(st);
		}

		private void populateTitleLabel() {
			try {
				ExecutionInformation ei = pm.getExecutionInformation();
				title.setText(ei.getName());
				HtmlBuilder sb = new HtmlBuilder();
				sb.appendWrap(ei.getId(),50);
				sb.br().append("Status: ").append(ei.getStatus());
				sb.br().appendWrap(ei.getDescription(),50);
				if (ei.getStartTime() != null) {
					sb.br()
					    .append("Started: ")
					    .append(df.format(ei.getStartTime()));
				} 
				if (ei.getFinishTime() != null) {
					sb.br()
					    .append("Finished: ")
					    .append(df.format(ei.getFinishTime()));
				}
				title.setToolTipText(sb.toString());
				//result.setToolTipText(ei.getDescription());

			} catch (ACRException x) {
				title.setText("inaccessible: " + x.getMessage());
			}
		}

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == refreshButton && pm.started()) {
                (new BackgroundWorker(parent,"Refreshing") {

                    protected Object construct() throws Exception {
                        pm.refresh();
                        return null;
                    }
                }).start();
            } else {                // do a delete/halt
                pm.removeProcessListener(this);
                monitors.remove(pm);
                // record these conditions now.. else they might change.
                final boolean running = pm.started() && ! hasFinished();
                (new BackgroundWorker(parent,"Cleaning up") {                    
                    protected Object construct() throws Exception {
                        if (running) {
                            pm.halt();
                        }
                        rpmi.delete(pm);
                        return null;
                    }
                    protected void doError(Throwable ex) {
                        logger.error("NotFoundException",ex);
                    }
                }).start();
            }            
        }
        private boolean hasFinished() {
            if (! pm.started()) {
                return false;
            }
            final String stat = pm.getStatus();
            return stat.equalsIgnoreCase("ERROR") || stat.equalsIgnoreCase("COMPLETED");
        }
        
        private void alterButton() {
            deleteButton.setToolTipText("Delete this task");
            deleteButton.setIcon(IconHelper.loadIcon("editdelete16.png"));
        }
	}

	/** produices a panel for each monitor bean - delegates it's implementation to ProcessMonitorDisplay
	 * 
	 * @author Noel.Winstanley@manchester.ac.uk
	 * @since Jul 16, 20075:49:58 PM
	 */
	private static class TrackerFormat extends JEventListPanel.AbstractFormat {

		public TrackerFormat() {
			super("p,d,d"// rows
					,"20px,fill:60px:grow,20px,20px" // cols
					,"2dlu" // row spacing
					,"0dlu" // colspacing
					, new String[]{"2,1","1,1","3,1","4,1","2,2","1,3,4,1"}
			);
		}

		public JComponent getComponent(Object element, int component) {
			return ((ProcessMonitorDisplay)element).getComponent(component);
		}

		public int getComponentsPerElement() {
		    return 6;
		}


	}

}