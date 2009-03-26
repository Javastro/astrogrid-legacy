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


/** Control system webbrowser using <a href='http://browserlaunch2.sourceforge.net/'>BrowserLauncher</a> library.
 * Fallback for when not using webstart.
 * @author Noel Winstanley
 * @since Apr 25, 200610:54:20 AM
 */
public class BrowserLauncherBrowserControl extends FallbackBrowserControl {
    private static final Log logger = LogFactory
    	.getLog(BrowserLauncherBrowserControl.class);
	public BrowserLauncherBrowserControl(final WebServer root,final UIContext ui) {
		super(root);
		this.ui = ui;
		browserLogger = new WorkbenchLogger();
		try {
			launcher = new BrowserLauncher(browserLogger);
		} catch (final BrowserLaunchingInitializingException x) {
			logger.error("BrowserLaunchingInitializingException",x);
		} catch (final UnsupportedOperatingSystemException x) {
			logger.error("UnsupportedOperatingSystemException",x);
		}
	}
	protected final UIContext ui;
	private BrowserLauncher launcher;
	private final AbstractLogger browserLogger;


	@Override
    public void openURL(final URL url) throws ACRException {
		if (launcher == null) {
			super.openURL(url);
		}
		try {
			launcher.openURLinBrowser(url.toString());
		} catch (final Exception x) {
			logger.error("Failed to control browser - falling back",x);
			super.openURL(url);
		}
	}
	
	// report a faulure in the ui.
	private class WorkbenchLogger extends AbstractLogger {
		{
			setLevel(Level.ERROR);
		}
		@Override
        protected void reallyLog(final int arg0, final String arg1, final Throwable arg2) throws Exception {
			ui.findMainWindow().showError(arg1,arg2);
		}
	}

}
