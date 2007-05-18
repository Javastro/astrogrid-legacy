/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.Component;
import java.net.URL;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.netbeans.api.wizard.WizardDisplayer;
import org.netbeans.spi.wizard.DeferredWizardResult;
import org.netbeans.spi.wizard.ResultProgressHandle;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardException;
import org.netbeans.spi.wizard.WizardPage;
import org.netbeans.spi.wizard.WizardPage.WizardResultProducer;

/** common subclass for all wizards used in workbench
 * Extends external wizard framework and integrates it with Background Worker, etc.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 17, 200710:56:43 AM
 */
public class AstroWizard {
	static {
		//System.setProperty ("wizard.sidebar.image","org/astrogrid/desktop/icons/ivoa-transparent-mid.png");
	}

	//@todo define some handy wizard panes here.

	public static class InfoWizardPane extends WizardPage {
		public InfoWizardPane(String s) {
			super("Introduction");
			JLabel l = new JLabel(s);
			add(l);
		}

	}

	public static class FileChooserWizardPane extends WizardPage {
		public FileChooserWizardPane(String prompt, String resultName,boolean warnOnOverwrite) {
			super(prompt);
			fc = new JFileChooser();
			fc.setControlButtonsAreShown(false);
			fc.setName(resultName);
			add(fc);
			this.warnOnOverwrite = warnOnOverwrite;
		}
		private final JFileChooser fc;
		public JFileChooser getFileChooser() {
			return fc;
		}
		private final boolean warnOnOverwrite;

		protected String validateContents(Component component, Object event) {
			if (component == fc) {
				System.out.println("Validating file componnet");
				if (fc.getSelectedFile() == null) {
					return "No File selected";
				}else if (fc.getSelectedFile().exists()) {
					int i = JOptionPane.showConfirmDialog(this,"This file exists - overwrite?");
					if (i != JOptionPane.YES_OPTION && i != JOptionPane.OK_OPTION) {
						return "File exists - select another";
					}
				}
			} 
			// in all other cases return null;
			return null;
		}
	}

	/** create a new wizard - a more type-specific version of the creation methods in WizardPage */
	public static Wizard createWizard(String title,WizardPage[] wp,WizardBackgroundWorker worker) {
		return WizardPage.createWizard(title,wp,worker);
	}

	public static abstract class WizardBackgroundWorker extends BackgroundWorker implements WizardResultProducer {
//		construct a new worker.
		public WizardBackgroundWorker(UIComponent parent, String msg) {
			super(parent, msg);
		}

		public WizardBackgroundWorker(UIContext context, String msg) {
			super(context, msg);
		}
//		initialize the worker from the result of the wizard
		public final void init(Map arg0,ResultProgressHandle arg1) {
			this.args = arg0;
			this.progress = arg1;
		}
		protected Map args;
		protected ResultProgressHandle progress;

		// wizard result producer interface
		public boolean cancel(Map arg0) {
			return true;
		}

		// return a deferred wizard result, which in turn initializes and starts this background worker.
		public final Object finish(Map arg0) throws WizardException {
			return new DeferredWizardResult() {

				public void start(Map m, ResultProgressHandle arg1) {
					init(m,arg1);
					WizardBackgroundWorker.this.start();
				}
			};
		}

		protected void doError(Throwable ex) {
			progress.failed(ex.getMessage(),false);
		}

		protected void doFinished(Object result) {
			progress.finished(result);
		}

	}

}
