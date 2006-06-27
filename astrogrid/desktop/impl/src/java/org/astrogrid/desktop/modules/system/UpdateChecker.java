/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;

/** checks for updates to the software.
 * @author Noel Winstanley
 * @since Jun 27, 20067:35:22 PM
 */
public class UpdateChecker implements Runnable {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(UpdateChecker.class);

	private static final String versionURLString = "http://www.astrogrid.org/desktop/version.txt";
	private static final String downloadURLString = "http://www.astrogrid.org/desktop";
	public UpdateChecker(Configuration conf, UIInternal ui, BrowserControl browser) throws MalformedURLException {
		versionURL = new URL(versionURLString);
		downloadURL = new URL(downloadURLString);
		this.conf = conf;
		this.ui = ui;
		this.browser = browser;
	}
	protected final URL versionURL;
	protected final URL downloadURL;
	protected final UIInternal ui;
	protected final Configuration conf;
	protected final BrowserControl browser;
	public void run() {
		final String currentVersion = conf.getKey("astrogrid.desktop.version");
		if (currentVersion != null &&  conf.getKey("asr.mode") == null) { // not in development, and not in headless mode.
			(new BackgroundWorker(ui,"Checking for software updates") {
				protected Object construct() throws Exception {
					
					BufferedReader vr =null;
					try {
						vr = new BufferedReader(new InputStreamReader(versionURL.openStream()));
					String newVersion = vr.readLine().trim();
					return currentVersion.equals(newVersion) ? null : newVersion;
					} finally {
						if (vr != null) {
							try {
								vr.close();
							} catch (IOException ignored) {
							}
						}
					}
				}
				protected void doFinished(Object result) {
					if (result != null) {
					int code = JOptionPane.showConfirmDialog(ui.getComponent(),
							"<html>Latest version is " + result + "<br>Open download site?" 
							,"A new version is available",JOptionPane.YES_NO_OPTION);
					if (code == JOptionPane.YES_OPTION) {
						try {
							browser.openURL(downloadURL);
						} catch (ACRException x) {
							logger.error("Failed to open browser",x);
						}
					}
					}
				}
				
				protected void doError(Throwable ex) {
					// don't matter - fail silently.
				}
			}).start();
		}
	}

}
