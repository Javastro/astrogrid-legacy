/**
 * 
 */
package org.astrogrid.desktop.alternatives;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.system.BackgroundExecutor;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;

import EDU.oswego.cs.dl.util.concurrent.DirectExecutor;
import EDU.oswego.cs.dl.util.concurrent.Executor;

/** Alternative implementation of Background Executor - implements the task in-thread.
 * 
 * @author noel
 * @since Apr 10, 20063:21:22 PM
 */
public class InThreadExecutor implements BackgroundExecutor {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(InThreadExecutor.class);

	private final Executor exec = new DirectExecutor();
	/* overridden
	 */
	public void executeWorker(BackgroundWorker worker) {
		try {
			exec.execute(worker);
		} catch (InterruptedException x) {
			logger.warn("interrupted", x);
			
		}
	}

	/* overridden
	 */
	public void interrupt(Runnable r) {
		// not possible. just log and ignore.
	}

	/* overridden
	 */
	public void execute(Runnable worker) throws InterruptedException {
		try {
			exec.execute(worker);
		} catch (InterruptedException x) {
			logger.warn("interrupted", x);
			
		} catch (Throwable t) {
			// catch and log all exceptions - preserves semantics of original background executor, where runnables are run
			// in separate thread, and so any thrown exceptions are just swallowed.
			logger.error("Worker Task threw",t);
		}

	}

}
