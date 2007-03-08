package org.astrogrid.desktop.alternatives;

import java.awt.Component;

import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.system.BackgroundExecutor;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
/**
 * Implementation of UI suitable for use in headless environments.
 * @author noel
 * @since Apr 10, 20063:51:06 PM
 */
public class HeadlessUI implements UIInternal {
	/**
	 * Logger for this class
	 */
	private  Log logger;

	protected final BackgroundExecutor executor;
	public HeadlessUI(final String category, final BackgroundExecutor executor) {
		super();
		this.logger =LogFactory.getLog(category);
		this.executor = executor;
	}

	public Component getComponent() {
		return null;
	}

	public BackgroundExecutor getExecutor() {
		return executor;
	}

	public BackgroundWorker wrap(final Runnable r) {
        return new BackgroundWorker(this,"Background Task") {

            protected Object construct() throws Exception {
                r.run();
                return null;
            }
        };
	}

	public void hide() {
		// does nothing

	}

	public void setLoggedIn(boolean arg0) {
		logger.info("Logged in = " + arg0);
	}



	public void show() {
		// does nothing.
	}

	public void startThrobbing() {
		// does nothing.
	}

	public void stopThrobbing() {
		// does nothing.

	}

		private int max = 0;
		private int progress = 0;
		
		public void addBackgroundWorker(BackgroundWorker w) {
			// does nothing.
		}

		public JFrame getFrame() {
			return null;
		}

		public int getProgressMax() {
			return max;
		}

		public int getProgressValue() {
			return progress;
		}

		public UIInternal getUI() {
			return this;
		}

		public void removeBackgroundWorker(BackgroundWorker w) {
			//does nothing.
		}

		public void setBusy(boolean b) {
		}

		public void setProgressMax(int i) {
			max = i;
		}

		public void setProgressValue(int i) {
			progress = i;
		}

		public void setStatusMessage(String s) {
			logger.info(s);
		}

		public void showError(String msg, Throwable e) {
			logger.error(msg,e);
		
		}

		public void showAboutDialog() {
			// do nothing
		}

		public void showPreferencesDialog() {
			// do nothing
		}

		public void haltAll() {
			// does nothing , unfortunatly. @todo try to implemnt this maybe.
		}
}

