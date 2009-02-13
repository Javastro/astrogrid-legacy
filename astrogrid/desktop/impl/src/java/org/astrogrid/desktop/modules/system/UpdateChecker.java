/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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

/** Checks for updates to the application.
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
 * Note: there was a bug in this in 1.2.0 - 1.2.2. These versions
 * do not update correctly. Pity - but it's hard to work out how to
 * trigger the upgrade on these broken versions and get a corrected version to work
 * so will leave these, and hope a news item suffices.
 * 
 * @author Noel Winstanley
 * @since Jun 27, 20067:35:22 PM
 * 
 * 
 */
public class UpdateChecker implements Runnable {
	/**
	 * Logger for this class
	 */ 
	private static final Log logger = LogFactory.getLog(UpdateChecker.class);

	public UpdateChecker(final UIContext ui, final BrowserControl browser, final String currentVersion,final String releaseURL,final Preference check) throws MalformedURLException {
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
	protected final Comparator<String> comparator = new VersionComparator();
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
				@Override
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
						    } catch (final XMLStreamException e) {
						        // ignored
						    }
						}
					}
					return null; // result is stored in member variables.
				}
				@Override
                protected void doError(final Throwable ex) {
				    logger.warn("Failed to check for update",ex);
				}
				@Override
                protected void doFinished(final Object result) {
					// check we've got something new.
				    if (! isNewer(latestVersion)) {
				        return; // nothing more.
				    }
				    logger.info("A new version of VODesktop is available: " + latestVersion + ", " + date + ", " + description);
					    final BaseDialog bd = new BaseDialog() {
					        {
					            setModal(false);
					            setTitle("A new version of VODesktop is available! ");
					            getBanner().setTitle("Version: " + latestVersion + ", " + description + ", " + date);
					            getBanner().setSubtitle("Click 'OK' to open the download site in your web browser");
					            pack();
					            centerOnScreen();
					        }
					        @Override
                            public void ok() {
					            super.ok();
					            // ok has been clicked - lets process it.
		                        try {
		                            browser.openURL(download);
		                        } catch (final ACRException x) {
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
	/** Test whether latest version is newer than what we've got.
     * @return
     */
    public boolean isNewer(final String grabbedVersion) {
        return StringUtils.isNotEmpty(grabbedVersion)
                && comparator.compare(currentVersion,grabbedVersion) < 0;
    }
    /** comparator for version strings */
	public static class VersionComparator implements Comparator<String> {

        public int compare(final String left, final String right) {
            final List<String> as = new ArrayList<String>(Arrays.asList(StringUtils.split(left,".")));
            final List<String> bs = new ArrayList<String>(Arrays.asList(StringUtils.split(right,".")));
            while(! as.isEmpty() && ! bs.isEmpty()) {
                final String a = as.remove(0);
                final String b = bs.remove(0);
                if (StringUtils.isNumeric(a) != StringUtils.isNumeric(b)) {
                    // numeric at the same level is always greater
                    return StringUtils.isNumeric(a) ? 1 : -1;
                }
                final int v = a.compareTo(b);
                if (v != 0) {
                    return v;
                }
            }
            // ok. whoever is longer wins - if they're both still numbers.
            if (as.isEmpty() && bs.isEmpty()) {
                return 0; // equal.
            }
            if (as.isEmpty()) {
                return StringUtils.isNumeric(bs.remove(0)) ? -1 : 1 ;
            } else { // bs empty
                return StringUtils.isNumeric(as.remove(0)) ? 1 : -1 ;
            }
        }
	}

}
