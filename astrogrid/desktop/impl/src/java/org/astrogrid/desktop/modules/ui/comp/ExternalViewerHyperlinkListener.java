/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.net.URI;
import java.net.URL;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.ui.RegistryBrowser;

/** Hyperlink listener that passes link to system webbrowser, or voexplorer, according to URI scheme.
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
	
	/** listener for just http links */
	   public ExternalViewerHyperlinkListener(final BrowserControl browser) {
	        super();
	        this.browser = browser;
	        this.registry = null;
	    }

	public void hyperlinkUpdate(final HyperlinkEvent e) {
		try {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			final URL u = e.getURL();
			if (u != null) { // it's a url the system knows about - so hand it off to the browser.
				if (u.getProtocol().equals("ivo") && registry != null) { // double check - incase somehow a URL is produced from a ivo://
					//registry.search(StringUtils.substringBefore(u.toString(),"#"));
					registry.open(new URI(u.toString()));
				} else {
					browser.openURL(u);
				}
			} else { // most probably an ivo:// reference. now need to go digging for it.
				final Element el = e.getSourceElement();
				final AttributeSet attr = el.getAttributes();
				final AttributeSet a = (AttributeSet)attr.getAttribute(HTML.Tag.A);
				if (a != null) {
					final String ivoid= (String)a.getAttribute(HTML.Attribute.HREF);
					if (ivoid != null && registry != null) {
						registry.open(new URI(ivoid));
					}
				}
			}
		}
		} catch (final Exception ex) {
			logger.warn("Failed to open browser",ex);
		}
	}

}
