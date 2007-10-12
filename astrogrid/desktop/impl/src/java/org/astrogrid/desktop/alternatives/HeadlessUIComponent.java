package org.astrogrid.desktop.alternatives;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
/**
 * Implementation of UI suitable for use in headless environments.
 * @author noel
 * @since Apr 10, 20063:51:06 PM
 */
public class HeadlessUIComponent implements UIComponent {
	/**
	 * Logger for this class
	 */
	private  Log logger;
	private final UIContext context;
	public HeadlessUIComponent(final String category, final UIContext  context) {
		super();
		this.logger =LogFactory.getLog(category);
		this.context = context;
	}

		private int max = 0;
		private int progress = 0;
		
		public void addBackgroundWorker(BackgroundWorker w) {
			// does nothing.
		}

		public Component getComponent() {
			return null;
		}

		public int getProgressMax() {
			return max;
		}

		public int getProgressValue() {
			return progress;
		}

		public void removeBackgroundWorker(BackgroundWorker w) {
			//does nothing.
		}

		public void setBusy(boolean b) {
		    //does nothing
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

		public void haltMyTasks() {
			// does nothing , unfortunatly. @todo try to implemnt this maybe.
		}

		public UIContext getContext() {
			return context;
		}

		public String getTitle() {
			return null;
		}

		public void setVisible(boolean b) {
		    //does nothing
		}

        public void showError(String msg) {
            // does nothing
        }

        public void showTransientError(String title, String message) {
            // does nothing
        }

        public void showTransientMessage(String title, String message) {
            // does nothing
        }

        public void showTransientWarning(String title, String message) {
            // does nothing
        }

        public JPanel getMainPanel() {
            return null;
        }
}

