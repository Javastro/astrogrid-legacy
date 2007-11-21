/**
 * 
 */
package org.astrogrid.desktop.modules.ui.execution;

import java.awt.Component;
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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.WordUtils;
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
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.ivoa.resource.HtmlBuilder;
import org.astrogrid.desktop.modules.system.ui.ActivitiesManager;
import org.astrogrid.desktop.modules.system.ui.ActivityFactory;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.actions.InfoActivity;
import org.astrogrid.desktop.modules.ui.actions.PlasticScavenger;
import org.astrogrid.desktop.modules.ui.actions.RevealFileActivity;
import org.astrogrid.desktop.modules.ui.actions.SimpleDownloadActivity;
import org.astrogrid.desktop.modules.ui.actions.ViewInBrowserActivity;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.desktop.modules.ui.comp.ObservableConnector;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileNavigator;
import org.astrogrid.desktop.modules.ui.fileexplorer.NavigableFilesList;
import org.astrogrid.desktop.modules.ui.taskrunner.TaskRunnerImpl;
import org.astrogrid.workflow.beans.v1.Tool;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.ObservableElementList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.impl.ThreadSafeList;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import ca.odell.glazedlists.swing.JEventListPanel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.l2fprod.common.swing.JTaskPane;

/** Tracks the execution of a series of remote processes
 * 
 * allows one or more components to register as ShowDetailsListeners - when the user presses the 'show details' button for a process, this event is fired.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 16, 20072:06:17 PM
 */
public class ExecutionTracker implements ListSelectionListener{
    private final static DateFormat df = SimpleDateFormat.getDateTimeInstance();
    
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ExecutionTracker.class);
    
    public ExecutionTracker(UIComponent parent,RemoteProcessManagerInternal rpmi, TypesafeObjectBuilder uiBuilder, ActivityFactory actFact) {
		super();
		logger.debug("creating execution tracker");
        this.parent = parent;
		this.rpmi = rpmi;
        this.uiBuilder = uiBuilder;
        this.acts = actFact.create(parent,new Class[]{
                ViewInBrowserActivity.class
                ,SimpleDownloadActivity.class 
                ,PlasticScavenger.class
                ,RevealFileActivity.class
                ,InfoActivity.class                
        });
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
    
    private final ActivitiesManager acts;
    private final EventList components;
 
    private final ArrayList listeners = new ArrayList();
    private final EventList monitors;


	private JEventListPanel panel;

    final UIComponent parent;

    // necessary so I can delete the monitors.
	private final RemoteProcessManagerInternal rpmi;


    private final TypesafeObjectBuilder uiBuilder;
	/** add a new monitor to the tracker 
	 * @threads can be called on any thread - notifications and updates to display
	 * are dispatched onto the EDT by this component.
	 * */
	public void add(ProcessMonitor pm) {
	    // modified to add to 'top' of list.
		monitors.add(0,pm);
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

	public void addShowDetailsListener(ShowDetailsListener l) {
        listeners.add(l);
    }
	
	public final ActivitiesManager getActs() {
        return this.acts;
    }
	
	
	public JPanel getPanel() {
		return panel;
	}
	
	
	/** access the task pane for working with activities */
	public JTaskPane getTaskPane() {
	    return acts.getTaskPane();
	}
	public void remove(ProcessMonitor pm) {
	    monitors.remove(pm);
	}
	public void removeShowDetailsListener(ShowDetailsListener l) {
        listeners.remove(l);
    }


	public void valueChanged(ListSelectionEvent e) {
        NavigableFilesList src = (NavigableFilesList) e.getSource();
        if (! src.isSelectedIndex(e.getFirstIndex())) {
            return; // only care about selection events, not deselections.
        }
        Transferable currentSelection = acts.getCurrentSelection();
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
        acts.setSelection(currentSelection);
        
        
    }


    private void fireShowDetails(ProcessMonitor pm) {
        ShowDetailsEvent e= new ShowDetailsEvent(this,pm);
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            ShowDetailsListener l = (ShowDetailsListener) i.next();
            l.showDetails(e);
        }
    } 
	public static class ShowDetailsEvent extends EventObject {
    
   /**
         * @param source
         */
        public ShowDetailsEvent(Object source,ProcessMonitor moitor) {
            super(source);
            this.moitor = moitor;
            
        }

private final ProcessMonitor moitor;

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

	// list selection listener interface. - used to remove selection in other file views when a click happens in one file view.
	
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
	private class ProcessMonitorDisplay extends Observable implements ProcessListener {

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
			
			JButton controls = new JButton(IconHelper.loadIcon("downarrow16.png"));
			final JPopupMenu controlsMenu = new JPopupMenu();
			controlsMenu.add(refresh);
			controlsMenu.add(transcript);
			controlsMenu.add(halt);
			controlsMenu.add(delete);
			if (pm instanceof ProcessMonitor.Advanced) {
			    controlsMenu.addSeparator();
			    controlsMenu.add(loadParams);
			}
			controls.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Component c = (Component) e.getSource();
                    int px = c.getX();
                    int py = c.getY() + c.getHeight() + 2;        
                    if(!controlsMenu.isShowing()) {                 
                        controlsMenu.show( c, 0, py  );
                    } else { 
                        controlsMenu.setVisible(false);
                    }                  
                }
			});            
            navigator = uiBuilder.createFileNavigator(parent,acts);
            results = new NavigableFilesList(navigator);
            results.getActionMap().remove(UIComponentMenuBar.EditMenuBuilder.PASTE); // don't allow pasting into this menu.

            
            results.addListSelectionListener(ExecutionTracker.this);
					populateMsgLabel();			
					populateStatusLabel();			
					populateTitleLabel();
					triggerUpdate();
					
			CellConstraints cc = new CellConstraints();
			FormLayout l = new FormLayout( "20px,fill:60px:grow,d", "p,d,d");
            PanelBuilder pb = new PanelBuilder(l);
            pb.add(status,cc.xy(1,1));
            pb.add(title,cc.xy(2,1));
            pb.add(controls,cc.xy(3,1));
            pb.add(messageLabel,cc.xy(2,2));
            pb.add(results,cc.xyw(1,3,3));
            displayPanel = pb.getPanel();
            ActionMap actionMap = title.getActionMap();
            title.setFocusable(true);
            actionMap.put(UIComponentMenuBar.EditMenuBuilder.DELETE,delete);
            actionMap.put(TaskRunnerImpl.HALT,halt);
            actionMap.put(TaskRunnerImpl.REFRESH,refresh);
            actionMap.put(TaskRunnerImpl.POPULATE,loadParams);
		}
        
        private final Action delete = new DeleteAction();
        private final Action halt = new HaltAction();
        private final Action loadParams = new LoadParamsAction();
        private final Action refresh = new RefreshAction();
        private final Action transcript = new ShowTranscriptAction();
        
        private final JLabel messageLabel = new JLabel();
        private FileNavigator navigator;
        private final ProcessMonitor pm;
		private int previousMsgCount = 0;
		private final NavigableFilesList results ;
		private final JPanel displayPanel;
		private final JLabel status = new JLabel(UIConstants.PENDING_ICON);	
		private final JLabel title = new JLabel();

		public JComponent getComponent(int ix) {
			switch(ix) {
			case 0:
				return displayPanel;
			default:
				return new JLabel("invalid index: " + ix);
			}
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
		private boolean hasFinished() {
            if (! pm.started()) {
                return false;
            }
            final String stat = pm.getStatus();
            return stat.equalsIgnoreCase("ERROR") || stat.equalsIgnoreCase("COMPLETED");
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
                        builder.appendWrap(m.getContent(),100);
                        builder.p();        
                                 
                    }
					messageLabel.setToolTipText(builder.toString());
				}
			} catch (ACRException x) {
				messageLabel.setText("inaccessible: " + x.getMessage());
			}
		}
		
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
		        
		        protected void doError(Throwable ex) {
		            messageLabel.setText("Failed to fetch results: " + ExceptionFormatter.formatException(ex));
	                parent.showTransientError("Failed to fetch results",ExceptionFormatter.formatException(ex));
	                  
		        }

		        protected void doFinished(Object result) {
		            if (result != null) {
		                navigator.move((FileObject)result);		                
		            } else if  (alreadyFoundRoot) {
		                navigator.refresh();
		            }
		        }
		    }).start();
		    // got a results root by this point.
		}
		// can share this - as is only ever run on EDT.
		private void populateStatusLabel() {
			String st = pm.getStatus();
			if (st.equalsIgnoreCase("error")) {
			    delete.setEnabled(true);
			    halt.setEnabled(false);
			    refresh.setEnabled(false);
				status.setIcon(UIConstants.ERROR_ICON);
				populateResults();
			} else if (st.equalsIgnoreCase("completed")) {
				status.setIcon(UIConstants.COMPLETED_ICON);
                delete.setEnabled(true);
                halt.setEnabled(false);
                refresh.setEnabled(false);
				populateResults();
			} else if (st.equalsIgnoreCase("pending")) {
			    refresh.setEnabled(true);
				status.setIcon(UIConstants.PENDING_ICON);
			} else if (st.equalsIgnoreCase("running")) {
			    refresh.setEnabled(true);
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
				sb.appendWrap(ei.getId(),100);
				sb.br().appendWrap(ei.getDescription(),100);
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
				        sb.appendWrap(StringEscapeUtils.escapeHtml(ps[i].getValue()),100);
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
		private void triggerUpdate() {
			super.setChanged();
			super.notifyObservers();			
		}

		/** does the same job as halt - just different labelling */
		private class DeleteAction extends HaltAction {
	
            public DeleteAction() {
                super("Delete Task",IconHelper.loadIcon("editdelete16.png"));
                putValue(SHORT_DESCRIPTION,"Delete this task");
                setEnabled(false);
            }

        }

        private class HaltAction extends AbstractAction {
            public HaltAction(String s, Icon i) {
                super(s,i);
            }
            public HaltAction() {
                super("Halt Task",IconHelper.loadIcon("stop16.png"));
                putValue(SHORT_DESCRIPTION,"Halt execution of this task");   
            }
            public void actionPerformed(ActionEvent e) {
                pm.removeProcessListener(ProcessMonitorDisplay.this);
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
        private class LoadParamsAction extends AbstractAction {

            public LoadParamsAction() {
                super("Populate Form",IconHelper.loadIcon("edit16.png"));
                putValue(SHORT_DESCRIPTION,"Load the parameters used to run this task back into the parameter editor");                    
            }
            public void actionPerformed(ActionEvent e) {
                fireShowDetails(pm);
            }
        }
        
        private class ShowTranscriptAction extends AbstractAction {
            /**
             * 
             */
            public ShowTranscriptAction() {
                super("Show Transcript");
                putValue(SHORT_DESCRIPTION,"<html>Display the execution transcript for this task in a popup window." +  
                            "<br><i>It can also be accessed by hovering the mouse over the message field");
            }
            public void actionPerformed(ActionEvent e) {
                String msg = title.getToolTipText() + "<h3>Messages</h3>" + messageLabel.getToolTipText();
                ResultDialog rd = ResultDialog.newResultDialog(parent.getComponent(),msg);
                rd.setTitle("Execution Transcript");
                rd.getBanner().setVisible(true);
                rd.getBanner().setTitle("Execution Transcript");
                rd.getBanner().setSubtitleVisible(false);
                rd.pack();
                rd.show();
            }
            
        }
        
        private class RefreshAction extends AbstractAction {
            public RefreshAction() {
                super("Refresh Task",IconHelper.loadIcon("reload16.png"));
                putValue(SHORT_DESCRIPTION,"Refresh the status of this task");
                setEnabled(false);
            }
            public void actionPerformed(ActionEvent e) {
                if (!pm.started()) {
                    return;
                }
                (new BackgroundWorker(parent,"Refreshing") {
                    {
                        parent.showTransientMessage("Refreshing task status","");
                        setTransient(true);
                        setEnabled(false); // disable the action, to prevent it being called multiple times.
                        // will be re-enabled by the populateStatusLabel method if appropriate.
                    }

                    protected Object construct() throws Exception {
                        pm.refresh();
                        return null;
                    }
                }).start();                
            }
        }
	}

    /** produices a panel for each monitor bean - delegates it's implementation to ProcessMonitorDisplay
	 * 
	 * @author Noel.Winstanley@manchester.ac.uk
	 * @since Jul 16, 20075:49:58 PM
	 */
	private static class TrackerFormat extends JEventListPanel.AbstractFormat {

		public TrackerFormat() {
			super("pref"// rows
					,"fill:pref:grow" // cols
					,"2dlu" // row spacing
					,"0dlu" // colspacing
					, new String[]{"1,1"}
			);
		}


		public JComponent getComponent(Object element, int component) {
			return ((ProcessMonitorDisplay)element).getComponent(component);
		}

		public int getComponentsPerElement() {
		    return 1;
		}


	}

}
