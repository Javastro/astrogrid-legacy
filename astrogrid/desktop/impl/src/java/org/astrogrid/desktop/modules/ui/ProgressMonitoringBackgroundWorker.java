/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import javax.swing.ProgressMonitor;

/** A background worker that integrates a progress monitor
 *  - this allows a dialogue to be displayed to indicate progress, and enable a 'cancel' button.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 3, 200712:51:15 AM
 */
public abstract class ProgressMonitoringBackgroundWorker extends BackgroundWorker {


	public ProgressMonitoringBackgroundWorker(UIComponent parent, String msg, int priority) {
		super(parent, msg, priority);
	}

	public ProgressMonitoringBackgroundWorker(UIComponent parent, String msg, long msecs, int priority) {
		super(parent, msg, msecs, priority);
	}

	public ProgressMonitoringBackgroundWorker(UIComponent parent, String msg, long msecs) {
		super(parent, msg, msecs);
	}

	public ProgressMonitoringBackgroundWorker(UIComponent parent, String msg) {
		super(parent, msg);
	}

	private ProgressMonitor monitor;
	
	public void run() {
		// start the monitor
		monitor = new ProgressMonitor(parent.getFrame(),msg,"",0,1);
		super.run();
	}

}
