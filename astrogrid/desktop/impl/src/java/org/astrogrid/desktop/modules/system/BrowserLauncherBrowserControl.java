/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.net.URL;

import net.sf.wraplog.AbstractLogger;
import net.sf.wraplog.Level;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.alternatives.FallbackBrowserControl;
import org.astrogrid.desktop.modules.system.ui.UIContext;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;


/** Control system browser using BrowserLauncher library.
 * Fallback for when not using webstart.
 * @author Noel Winstanley
 * @since Apr 25, 200610:54:20 AM
 */
public class BrowserLauncherBrowserControl extends FallbackBrowserControl {
    private static final Log logger = LogFactory
    	.getLog(BrowserLauncherBrowserControl.class);
	public BrowserLauncherBrowserControl(WebServer root,UIContext ui) {
		super(root);
		this.ui = ui;
		browserLogger = new WorkbenchLogger();
		try {
			launcher = new BrowserLauncher(browserLogger);
		} catch (BrowserLaunchingInitializingException x) {
			logger.error("BrowserLaunchingInitializingException",x);
		} catch (UnsupportedOperatingSystemException x) {
			logger.error("UnsupportedOperatingSystemException",x);
		}
	}
	protected final UIContext ui;
	private BrowserLauncher launcher;
	private final AbstractLogger browserLogger;


	public void openURL(URL url) throws ACRException {
		if (launcher == null) {
			super.openURL(url);
		}
		try {
			launcher.openURLinBrowser(url.toString());
		} catch (Exception x) {
			logger.error("Failed to control browser - falling back",x);
			super.openURL(url);
		}
	}
	
	// report a faulure in the ui.
	private class WorkbenchLogger extends AbstractLogger {
		{
			setLevel(Level.ERROR);
		}
		protected void reallyLog(int arg0, String arg1, Throwable arg2) throws Exception {
			ui.findMainWindow().showError(arg1,arg2);
		}
	}

}
