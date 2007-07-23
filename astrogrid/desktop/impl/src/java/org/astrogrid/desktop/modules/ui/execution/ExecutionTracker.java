/**
 * 
 */
package org.astrogrid.desktop.modules.ui.execution;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Observable;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.text.StrBuilder;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.ProcessMonitor;
import org.astrogrid.desktop.modules.ag.RemoteProcessManagerInternal;
import org.astrogrid.desktop.modules.ag.ProcessMonitor.ProcessEvent;
import org.astrogrid.desktop.modules.ag.ProcessMonitor.ProcessListener;
import org.astrogrid.desktop.modules.ui.comp.ObservableConnector;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.ObservableElementList;
import ca.odell.glazedlists.swing.JEventListPanel;

/** Tracks the execution of a series of remote processes
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 16, 20072:06:17 PM
 */
public class ExecutionTracker{


	public ExecutionTracker(RemoteProcessManagerInternal rpmi) {
		super();
		this.rpmi = rpmi;
		monitors = new BasicEventList();
		// map each monitor to a bean of UI components.
		EventList components = new FunctionList(monitors, new FunctionList.Function() {
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
	
	/** add a monitor for the specified id to the tracker */
	public void add(URI execId) {
		ProcessMonitor m = rpmi.findMonitor(execId);
		if (m != null) {
			add(m);
		}
	}
	
	/** add a new monitor to the tracker */
	public void add(ProcessMonitor pm) {
		monitors.add(pm);
	}
	public JPanel getPanel() {
		return panel;
	}



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
	private static class ProcessMonitorDisplay extends Observable implements ProcessListener {
		private static final Icon COMPLETED_ICON = IconHelper.loadIcon("tick16.png");
		private static final Icon ERROR_ICON = IconHelper.loadIcon("no16.png");
		private static final Icon RUNNING_ICON = IconHelper.loadIcon("greenled16.png");
		private static final Icon UNKNOWN_ICON = IconHelper.loadIcon("idle16.png");
		private static final Icon PENDING_ICON = IconHelper.loadIcon("yellowled16.png");
		//private static final Icon RUNNING_ICON = IconHelper.loadIcon("loader.gif");
		//private static final Icon COMPLETED_ICON = IconHelper.loadIcon("greenled16.png");
		//private static final Icon ERROR_ICON = IconHelper.loadIcon("redled16.png");		

		public static int getComponentsPerElement() {
			return 3;
		}

		public ProcessMonitorDisplay(ProcessMonitor pm) {
			super();
			this.pm = pm;
			pm.addProcessListener(this);
			msg.setFont(UIConstants.SMALL_DIALOG_FONT);
			// populate it on the EDT.
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					populateMsgLabel();			
					populateStatusLabel();			
					populateTitleLabel();
				}
			});
		}

		private final ProcessMonitor pm;
		private final JLabel msg = new JLabel();
		private final JLabel status = new JLabel(PENDING_ICON);		
		private final JLabel title = new JLabel();
		public JComponent getComponent(int ix) {
			switch(ix) {
			case 0:
				return title;
			case 1:
				return status;
			case 2:
				return msg;
			default:
				return new JLabel("invalid index: " + ix);
			}
		}

		private void triggerUpdate() {
			super.setChanged();
			super.notifyObservers();			
		}

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
					
					//@todo display results somehow

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
		private void populateMsgLabel() {
			try {
				ExecutionMessage[] messages = pm.getMessages();
				if (messages.length > 0) {
					ExecutionMessage m = messages[messages.length-1]; // last one
					//@todo format this better.
					msg.setText(m.getContent());
					msg.setToolTipText("Message received at " + df.format(m.getTimestamp()));
				}
			} catch (ACRException x) {
				msg.setText("inaccessible: " + x.getMessage());
			}
		}
		// can share this - as is only ever run on EDT.
		private static DateFormat df = SimpleDateFormat.getDateTimeInstance(); 
		private void populateStatusLabel() {
			String st = pm.getStatus();
			if (st.equalsIgnoreCase("error")) {
				status.setIcon(ERROR_ICON);
			} else if (st.equalsIgnoreCase("completed")) {
				status.setIcon(COMPLETED_ICON);
			} else if (st.equalsIgnoreCase("pending")) {
				status.setIcon(PENDING_ICON);
			} else if (st.equalsIgnoreCase("running")) {
				status.setIcon(RUNNING_ICON);
			} else {
				status.setIcon(UNKNOWN_ICON);
			}
			status.setToolTipText(st);
		}

		private void populateTitleLabel() {
			try {
				ExecutionInformation ei = pm.getExecutionInformation();
				title.setText(ei.getName());
				StrBuilder sb = new StrBuilder("<html>");
				sb.append(ei.getId());
				if (ei.getStartTime() != null) {
					sb.append("<br>");
					sb.append("Started ");
					sb.append(df.format(ei.getStartTime()));
				} 
				if (ei.getFinishTime() != null) {
					sb.append("<br>");
					sb.append("Finished ");
					sb.append(df.format(ei.getFinishTime()));
				}
				title.setToolTipText(sb.toString());
				//result.setToolTipText(ei.getDescription());

			} catch (ACRException x) {
				title.setText("inaccessible: " + x.getMessage());
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
			super("p,d"// rows
					,"20px,fill:pref:grow" // cols
					,"2dlu" // row spacing
					,"0dlu" // colspacing
					, new String[]{"2,1","1,1","2,2"}
			);
		}

		public JComponent getComponent(Object element, int component) {
			return ((ProcessMonitorDisplay)element).getComponent(component);
		}

		public int getComponentsPerElement() {
			return ProcessMonitorDisplay.getComponentsPerElement();
		}


	}

}
