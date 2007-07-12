package org.astrogrid.desktop.modules.ui.comp;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.modules.ivoa.resource.ResourceFormatter;

/** Exgtensaion to the resource display pane with methods for displaying resources,
 * (and a performance-optimization)
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 11, 20072:22:46 PM
 */
public class ResourceDisplayPane extends JEditorPane {
	public ResourceDisplayPane() {
		setContentType("text/html");
		setBorder(BorderFactory.createEmptyBorder());
		setEditable(false);
		putClientProperty("JEditorPane.honorDisplayProperties", Boolean.TRUE);		// this key is only defined on 1.5 - no effect on 1.4
		setFont(UIConstants.SANS_FONT);
	}
	
	/** construct a display pane with additional support for following hyperlinks */
	public ResourceDisplayPane(final BrowserControl browser, final RegistryBrowser regBrowser) {
		this();
		addHyperlinkListener(new ExternalViewerHyperlinkListener(browser, regBrowser));
	}
	
	private Resource currentResource;
	private String currentHTML;
	public void display(Resource r) {
		if (currentResource != r) {
			currentResource = r;
			currentHTML = ResourceFormatter.renderResourceAsHTML(r);
		}
		setText(currentHTML); // will do this, even if it was the cached content - as pane may have been setTexted() some other way.
		setCaretPosition(0);
	}
	
	public void clear() {
		setText("<html><body></body></html>");
	}
}