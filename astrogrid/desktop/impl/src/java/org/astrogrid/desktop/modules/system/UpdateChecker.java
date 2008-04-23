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
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;
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
 * 
 * Previous code was a bit too brittle. have replaced previous method with an xml-update document - that way I don't need to hard-code a download URL.
 * something like.
 * <release>
 *      <date>2008-03-01</date>
 *      <version>2008.1</version>
 *      <download>http://wibble</download>
 *      <description>First main release</description>
 * </release>
 * 
 * algorithm: fetch and parse this file.
 * contains a single <release> block, containing latest version.
 * compare version ids. if no match, display to the user.
 * 
 * @author Noel Winstanley
 * @since Jun 27, 20067:35:22 PM
 * @TEST unit test for parser and reasoning logic.
 */
public class UpdateChecker implements Runnable {
	/**
	 * Logger for this class
	 */ 
	private static final Log logger = LogFactory.getLog(UpdateChecker.class);

	public UpdateChecker(UIContext ui, BrowserControl browser, String currentVersion,String releaseURL,Preference check) throws MalformedURLException {
		releaseInfo = new URL(releaseURL);
		this.ui = ui;
		this.browser = browser;
		this.currentVersion = currentVersion;
		this.check = check;
	}
	protected final Preference check;
	protected final String currentVersion;
	protected final URL releaseInfo;
	protected final UIContext ui;
	protected final BrowserControl browser;
			
	public void run() {
		if (!check.asBoolean()) {
			return; //
		}
		if ( ! currentVersion.equals( "${astrogrid.desktop.version}") && ! currentVersion.equals("unreleased") ) { // not in development mode
			(new BackgroundWorker(ui,"Checking for software updates",BackgroundWorker.SHORT_TIMEOUT,Thread.MIN_PRIORITY) {
			    private String date;
			    private String latestVersion;
			    private URL download;
			    private String description;
				protected Object construct() throws Exception {
					final XMLInputFactory fac = XMLInputFactory.newInstance();
					XMLStreamReader in = null;
					try {
					    in = fac.createXMLStreamReader(releaseInfo.openStream());
					    while(in.hasNext()) {
					        in.next();
					        if (in.isStartElement()) {
					            final String localName = in.getLocalName();
					            if ("date".equals(localName)) {
					                date = in.getElementText();
					            } else if ("version".equals(localName)) {
					                latestVersion = in.getElementText();
                                } else if ("download".equals(localName)) {
                                    download = new URL(in.getElementText()); // will fail the process if not a valid url. - good.
                                } else if ("description".equals(localName)) { 
                                    description = in.getElementText();
					            }
					        }
					    } // end while.
					} finally {
						if (in != null) {
						    try {
						        in.close();
						    } catch (XMLStreamException e) {
						        // ignored
						    }
						}
					}
					return null; // result is stored in member variables.
				}
				protected void doError(Throwable ex) {
				    logger.warn("Failed to check for update",ex);
				}
				protected void doFinished(final Object result) {
					// check we've got something new.
				    if (StringUtils.isEmpty(latestVersion)
				            || currentVersion.equals(latestVersion)) {
				        return; // nothing more.
				    }
				    logger.info("A new version of VODesktop is available: " + latestVersion + ", " + date + ", " + description);
					    BaseDialog bd = new BaseDialog() {
					        {
					            setModal(false);
					            setTitle("A new version of VODesktop is available! ");
					            getBanner().setTitle("Version: " + latestVersion + ", " + description + ", " + date);
					            getBanner().setSubtitle("Click 'OK' to open the download site in your web browser");
					            pack();
					            centerOnScreen();
					        }
					        public void ok() {
					            super.ok();
					            // ok has been clicked - lets process it.
		                        try {
		                            browser.openURL(download);
		                        } catch (ACRException x) {
		                            parent.showTransientError("Failed to open browser",ExceptionFormatter.formatException(x));
		                        }					            
					        }
					    };
					    bd.setVisible(true);
					    bd.toFront();
					
				}
				
			}).start();
		}
	}

}
