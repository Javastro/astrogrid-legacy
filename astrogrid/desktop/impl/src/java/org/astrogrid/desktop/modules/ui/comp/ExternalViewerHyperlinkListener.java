/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.net.URL;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.ui.RegistryBrowser;

/** Hyperlink listener component that displays link in an external viewer
 * @author Noel Winstanley
 * @since Aug 7, 20067:30:42 PM
 */
public class ExternalViewerHyperlinkListener implements HyperlinkListener {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(ExternalViewerHyperlinkListener.class);

	private final BrowserControl browser;
	private final RegistryBrowser registry;
	
	public ExternalViewerHyperlinkListener(final BrowserControl browser, final RegistryBrowser registry) {
		super();
		this.browser = browser;
		this.registry = registry;
	}

	public void hyperlinkUpdate(HyperlinkEvent e) {
		try {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			URL u = e.getURL();
			if (u.getProtocol().equals("ivo")) {
				registry.search(StringUtils.substringBefore(u.toString(),"#"));
			} else {
				browser.openURL(u);
			}
		}
		} catch (Exception ex) {
			logger.warn("Failed to open browser",ex);
		}
	}

}
