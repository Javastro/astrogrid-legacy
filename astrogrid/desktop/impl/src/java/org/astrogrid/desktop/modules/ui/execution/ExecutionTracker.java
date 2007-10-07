/**
 * 
 */
package org.astrogrid.desktop.modules.ui.execution;

import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Iterator;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.AbstractProcessMonitor;
import org.astrogrid.desktop.modules.ag.ProcessMonitor;
import org.astrogrid.desktop.modules.ag.RemoteProcessManagerInternal;
import org.astrogrid.desktop.modules.ag.ProcessMonitor.ProcessEvent;
import org.astrogrid.desktop.modules.ag.ProcessMonitor.ProcessListener;
import org.astrogrid.desktop.modules.ivoa.resource.HtmlBuilder;
import org.astrogrid.desktop.modules.system.ui.ActivitiesManager;
import org.astrogrid.desktop.modules.system.ui.ActivityFactory;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.desktop.modules.ui.comp.ObservableConnector;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileNavigator;
import org.astrogrid.desktop.modules.ui.fileexplorer.NavigableFilesList;
import org.astrogrid.workflow.beans.v1.Tool;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.ObservableElementList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.impl.ThreadSafeList;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import ca.odell.glazedlists.swing.JEventListPanel;

import com.l2fprod.common.swing.JTaskPane;

/** Tracks the execution of a series of remote processes
 * 
 * allows one or more components to register as ShowDetailsListeners - when the user presses the 'show details' button for a process, this event is fired.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 16, 20072:06:17 PM
 */
public class ExecutionTracker implements ListSelectionListener{
    public static class ShowDetailsEvent extends EventObject {
    
   private final ProcessMonitor moitor;

/**
         * @param source
         */
        public ShowDetailsEvent(Object source,ProcessMonitor moitor) {
            super(source);
            this.moitor = moitor;
            
        }

/** access the monitor to show details for .
 * @return the moitor
 */
public final ProcessMonitor getMoitor() {
    return this.moitor;
}
    }
    
    /** listener inteface for a client that displays the details of an executioin.
     poorly named - at the moment used to reload the invocation params back in the editor.
     */
    public interface ShowDetailsListener extends EventListener {
        public void showDetails(ShowDetailsEvent e);
    }
    
    public void addShowDetailsListener(ShowDetailsListener l) {
        listeners.add(l);
    }
    
    public void removeShowDetailsListener(ShowDetailsListener l) {
        listeners.remove(l);
    }
    private final ArrayList listeners = new ArrayList();
 
    private void fireShowDetails(ProcessMonitor pm) {
        ShowDetailsEvent e= new ShowDetailsEvent(this,pm);
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            ShowDetailsListener l = (ShowDetailsListener) i.next();
            l.showDetails(e);
        }
    }
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ExecutionTracker.class);


	private final TypesafeObjectBuilder uiBuilder;

    private ActivitiesManager activities;

    private final UIComponent parent;


    public ExecutionTracker(UIComponent parent,RemoteProcessManagerInternal rpmi, TypesafeObjectBuilder uiBuilder, ActivityFactory actFact) {
		super();
		logger.debug("creating executioon tracker");
        this.parent = parent;
		this.rpmi = rpmi;
        this.uiBuilder = uiBuilder;
        this.activities = actFact.create(parent);
        logger.debug("created activities");
		monitors = new ThreadSafeList(new BasicEventList()); // trying this instead of a basic event list, to provide a bit more thread-safety without all the hassle of locking myself.
		// wrap the monitors list with a proxy that fires all updates on the EDT
		// means that monitors can be added to the list from any thread, and the 
		// UI will update correctluy.
		TransformedList proxyList = GlazedListsSwing.swingThreadProxyList(monitors);
		components = new FunctionList(proxyList, new FunctionList.Function() {
			public Object evaluate(Object sourceValue) {
			        return new ProcessMonitorDisplay((ProcessMonitor)sourceValue);			    
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
	    // modified to add to 'top' of list.
		monitors.add(0,pm);
	}
	public void remove(ProcessMonitor pm) {
	    monitors.remove(pm);
	}
	public JPanel getPanel() {
		return panel;
	}


	private final static DateFormat df = SimpleDateFormat.getDateTimeInstance();


    private final EventList components; 
	/** container class that holds the ui components required to display a single process monitor
	 * 
	 *  listens to the process manager, and updates ui components on changes.
	 *  acts as an observable, to notify managing list when this has changed.
	 *  
	 *  observable notifications and ui component updates are lifted onto the EDT.
	 *  the original notification from ProcessManager will not be on EDT.
	 * 
	 * displays additional components when wrapped around a ProcessMonitor.Advanced
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
	
			deleteButton = new JButton(IconHelper.loadIcon("stop16.png"));
			//deleteButton.putClientProperty("is3DEnabled",Boolean.FALSE);
			deleteButton.setRolloverEnabled(true);
			//deleteButton.setBorderPainted(false);
			deleteButton.setToolTipText("Cancel task");
			deleteButton.addActionListener(this);
			
			refreshButton = new JButton(IconHelper.loadIcon("reload16.png"));
            //refreshButton.putClientProperty("is3DEnabled",Boolean.FALSE);
            //refreshButton.setBorderPainted(false);
            refreshButton.setRolloverEnabled(true);
            refreshButton.setToolTipText("Refresh task");
            refreshButton.addActionListener(this);
            if (pm instanceof ProcessMonitor.Advanced) {
                
                loadParamsButton = new JButton(IconHelper.loadIcon("edit16.png"));
                loadParamsButton.setToolTipText("Load the parameters used to run this task back into the parameter editor");
                loadParamsButton.addActionListener(this);
            } else {     // not available unless it's an 'advanced' monitor.
                loadParamsButton = null;
            }
            
            navigator = uiBuilder.createFileNavigator(parent,activities);
            results = new NavigableFilesList(navigator);
            results.addListSelectionListener(ExecutionTracker.this);
					populateMsgLabel();			
					populateStatusLabel();			
					populateTitleLabel();
					triggerUpdate();
		}

		private final ProcessMonitor pm;
		private final JLabel messageLabel = new JLabel();
		private final JLabel status = new JLabel(UIConstants.PENDING_ICON);
		private final JLabel title = new JLabel();
		private final JButton deleteButton;
		private final JButton refreshButton;	
		private final JButton loadParamsButton;
		private final NavigableFilesList results ;
		
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
			case 6:
			    return loadParamsButton;
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
		private FileObject resultsRoot;
		/** not to be called before task has been 'init()' - as it's not got an ID at that stage */
		private void populateResults() {
		    if (!pm.started()) {
		        // not started - so won't have any results to retrieve.
		        return;
		    }
		    
		    (new BackgroundWorker(parent,"Listing results") {
		        private boolean alreadyFoundRoot;
		        protected Object construct() throws Exception {
		            if (! alreadyFoundRoot) {
		                final FileObject root = pm.getResultsFileSystem().getRoot();
		                if (root.exists()) { // it's created lazily, once children are present.
		                    alreadyFoundRoot = true;
		                    return root; // returned on first time.
		                }
		            }
		            return null;
		        }
		        
		        protected void doFinished(Object result) {
		            if (result != null) {
		                navigator.move((FileObject)result);		                
		            } else if  (alreadyFoundRoot) {
		                navigator.refresh();
		            }
		        }

		        protected void doError(Throwable ex) {
		            messageLabel.setText("Failed to fetch results: " + ex.getMessage());
	                parent.showTransientError("Failed to fetch results",ExceptionFormatter.formatException(ex));
	                  
		        }
		    }).start();
		    // got a results root by this point.
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
                        builder.br().append("Message: ");
                        builder.appendWrap(m.getContent(),50);
                        builder.p();        
                                 
                    }
					messageLabel.setToolTipText(builder.toString());
				}
			} catch (ACRException x) {
				messageLabel.setText("inaccessible: " + x.getMessage());
			}
		}
		private int previousMsgCount = 0;
        private FileNavigator navigator;
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
				sb.br().appendWrap(ei.getDescription(),50);
				sb.append("<h3>Status: ").append(ei.getStatus()).append("</h3>");
				if (ei.getStartTime() != null) {
					sb.append("Started: ")
					    .append(df.format(ei.getStartTime()));
				} 
				if (ei.getFinishTime() != null) {
					sb.br()
					    .append("Finished: ")
					    .append(df.format(ei.getFinishTime()));
				}
				// display input and output information
				if (pm instanceof ProcessMonitor.Advanced) {
				    Tool t = ((ProcessMonitor.Advanced)pm).getInvocationTool();
				    sb.h3("Inputs");
				    ParameterValue[] ps = t.getInput().getParameter();
				    for (int i = 0; i < ps.length; i++) {
				        //issue - special case for adql - convert to adqls. - at the moment <select> is getitng treated as a html tag.
				        // hmm - leave for now - instad will just html-escape all parameter values.
				        sb.append(ps[i].getName()).append(" = ");
				        sb.appendWrap(StringEscapeUtils.escapeHtml(ps[i].getValue()),50);
                        sb.br();
                    }
                    sb.h3("Outputs");
                    ps = t.getOutput().getParameter();
                    for (int i = 0; i < ps.length; i++) {
                        sb.append(ps[i].getName()).append(" = ").append(ps[i].getValue());
                        sb.br();
                    }				    
				}
				title.setToolTipText(sb.toString());

			} catch (ACRException x) {
				title.setText("inaccessible: " + x.getMessage());
			}
		}

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == refreshButton && pm.started()) {
                (new BackgroundWorker(parent,"Refreshing") {
                    {
                        setTransient(true);
                    }

                    protected Object construct() throws Exception {
                        pm.refresh();
                        return null;
                    }
                }).start();
            } else if (e.getSource() == loadParamsButton) {
                fireShowDetails(pm);
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
					,"20px,fill:60px:grow,d,20px,20px" // cols
					,"2dlu" // row spacing
					,"0dlu" // colspacing
					, new String[]{"2,1","1,1","4,1","5,1","2,2","1,3,4,1","3,1"}
			);
		}

		public JComponent getComponent(Object element, int component) {
			return ((ProcessMonitorDisplay)element).getComponent(component);
		}

		public int getComponentsPerElement() {
		    return 7;
		}


	}

	// list selection listener interface. - used to remove selection in other file views when a click happens in one file view.
	
    public void valueChanged(ListSelectionEvent e) {
        NavigableFilesList src = (NavigableFilesList) e.getSource();
        if (! src.isSelectedIndex(e.getFirstIndex())) {
            return; // only care about selection events, not deselections.
        }
        Transferable currentSelection = activities.getCurrentSelection();
        if (currentSelection == null) { // dunno why I sometimes get a null here, but it seems to happen breifly.
            return;
        }
        // go through the other items of the the list, and if the selection is non-null, clear it.
        for (Iterator i = components.iterator(); i.hasNext();) {
            ProcessMonitorDisplay proc = (ProcessMonitorDisplay) i.next();
            NavigableFilesList list = proc.results;
            if (list == src) { 
                continue;
            }
            if (! list.isSelectionEmpty()) {
                list.clearSelection(); // sadly tis fires an event to the shared activities manager, notifying that the selection has now been cleared.
            }
        }
        // a bit ikky -  activity manager has lost it's selection - so now remind it what it's meant to be displaying.        
        activities.setSelection(currentSelection);
        
        
    }

}
