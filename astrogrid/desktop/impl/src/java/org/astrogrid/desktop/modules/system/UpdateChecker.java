/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.awt.Component;
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
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;

import com.l2fprod.common.swing.BaseDialog;

/** checks for updates to the software.
 * @author Noel Winstanley
 * @since Jun 27, 20067:35:22 PM
 */
public class UpdateChecker implements Runnable {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(UpdateChecker.class);

	private static final String versionURLString = "http://www.astrogrid.org/desktop/download/version.txt";
	private static final String downloadURLString = "http://www.astrogrid.org/desktop/download";
	public UpdateChecker(UIContext ui, BrowserControl browser, String currentVersion,Preference check) throws MalformedURLException {
		versionURL = new URL(versionURLString);
		downloadURL = new URL(downloadURLString);
		this.ui = ui;
		this.browser = browser;
		this.currentVersion = currentVersion;
		this.check = check;
	}
	protected final Preference check;
	protected final String currentVersion;
	protected final URL versionURL;
	protected final URL downloadURL;
	protected final UIContext ui;
	protected final BrowserControl browser;
	public void run() {
		if (!check.asBoolean()) {
			return; //
		}
		if (! currentVersion.equals( "${astrogrid.desktop.version}") ) { // not in development mode
			(new BackgroundWorker(ui,"Checking for software updates",BackgroundWorker.SHORT_TIMEOUT,Thread.MIN_PRIORITY) {
				protected Object construct() throws Exception {
					
					BufferedReader vr =null;
					try {
						vr = new BufferedReader(new InputStreamReader(versionURL.openStream()));
						if (vr != null) {
							String newVersion = vr.readLine();
							if (newVersion != null) {
							    newVersion = newVersion.trim();
							    logger.info("Current Version: " + currentVersion);
							    logger.info("New Version: " + newVersion);
							    return currentVersion.compareTo(newVersion) < 0 ? newVersion : null;
							} else {
							    return null;
							}
						}
					} catch (IOException t) {
						logger.info("Update checker failed",t);
					} finally {
						if (vr != null) {
							try {
								vr.close();
							} catch (IOException ignored) {
							    //meh
							}
						}
					}
					return null;
				}
				protected void doFinished(final Object result) {
					if (result != null) {
					    BaseDialog bd = new BaseDialog() {
					        {
					            setModal(false);
					            setTitle("New version available");
					            getBanner().setTitle("A new version of VODesktop (v " + result + ") is available");
					            getBanner().setSubtitle("Open download site?");
					            pack();
					            centerOnScreen();
					        }
					        public void ok() {
					            super.ok();
					            // ok has been clicked - lets process it.
		                        try {
		                            browser.openURL(downloadURL);
		                        } catch (ACRException x) {
		                            parent.showTransientError("Failed to open browser",ExceptionFormatter.formatException(x));
		                        }					            
					        }
					    };
					    bd.setVisible(true);
					    bd.toFront();
					}
				}
				
				protected void doError(Throwable ex) {
					//logger.error("Failed to check for update",ex);
				}
			}).start();
		}
	}

}
