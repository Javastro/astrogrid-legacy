/**
 * 
 */
package org.astrogrid.desktop.alternatives;

import org.astrogrid.desktop.framework.SessionManagerInternal;
import org.astrogrid.desktop.modules.system.BackgroundExecutor;
import org.astrogrid.desktop.modules.system.BackgroundExecutorImpl;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.easymock.MockControl;

/** Factory that takes care of the circular dependency between HeadlessUI and BackgroundExecutor
 * @author Noel Winstanley
 * @since Jun 6, 20065:40:24 PM
 */
public class HeadlessUIFactory implements BackgroundExecutor {
	
	public HeadlessUIFactory() {
		// rats - there's a cycle here.
		ui = new HeadlessUI("TESTING",this);
		MockControl sessControl = MockControl.createNiceControl(SessionManagerInternal.class);
		SessionManagerInternal sess = (SessionManagerInternal)sessControl.getMock();
		sessControl.replay();
		this.be = new BackgroundExecutorImpl(ui,sess);
		this.be.init();
	}
	
	
//	 implementation of the background executor iterface - used to work around
	// construction cycle.
	// implementation lazily delegates to be.
	public void execute(Runnable arg0) throws InterruptedException {
		this.be.execute(arg0);
	}

	public void executeWorker(BackgroundWorker worker) {
		this.be.executeWorker(worker);
	}

	public void interrupt(Runnable r) {
		this.be.interrupt(r);
	}
	
	private final BackgroundExecutorImpl be;
	protected final UIInternal ui;
	
	public BackgroundExecutorImpl getExecutor() {
		return be;
	}
	public UIInternal getUI() {
		return ui;
	}
		

}
