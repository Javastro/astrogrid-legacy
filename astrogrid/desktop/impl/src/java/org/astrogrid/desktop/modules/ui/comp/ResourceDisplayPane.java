package org.astrogrid.desktop.modules.ui.comp;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.modules.ivoa.VosiAvailability;
import org.astrogrid.desktop.modules.ivoa.resource.CapabilityTester;
import org.astrogrid.desktop.modules.ivoa.resource.PrettierResourceFormatter;

/** Exgtensaion to an JEditorPane  with methods for displaying resources,
 * (and a performance-optimization)
 * 
 * this component is named 'ResourceDisplayPane.class.getName()'
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 11, 20072:22:46 PM
 */
public class ResourceDisplayPane extends JEditorPane {
	private final CapabilityTester capTester;
    private final VosiAvailability vosiTester;
	
	/** construct a display pane with additional support for following hyperlinks */
	public ResourceDisplayPane(final BrowserControl browser, final RegistryBrowser regBrowser, CapabilityTester capTester, VosiAvailability vosiTester) {
		this.vosiTester = vosiTester;
        setContentType("text/html");
		setBorder(null);
		setEditable(false);
		putClientProperty("JEditorPane.honorDisplayProperties", Boolean.TRUE);		// this key is only defined on 1.5 - no effect on 1.4
		setFont(UIConstants.SANS_FONT);
		setName(ResourceDisplayPane.class.getName());
        this.capTester = capTester;
		addHyperlinkListener(new ExternalViewerHyperlinkListener(browser, regBrowser));
	}
	
	protected Resource currentResource;
	private String currentHTML;
	public void display(Resource r) {
		if (currentResource != r) {
			currentResource = r;
			currentHTML = PrettierResourceFormatter.renderResourceAsHTML(r);
		}
		setText(currentHTML); // will do this, even if it was the cached content - as pane may have been setTexted() some other way.
		setCaretPosition(0);
	}
	
	public void clear() {
		setText("<html><body></body></html>");
		currentResource = null;
	}

	/** acccess the resource currently being displayed */
    public final Resource getCurrentResource() {
        return this.currentResource;
    }
    
    /** get a compoent to run test queries for capabilities */
    public final CapabilityTester getCapabilityTester() {
        return this.capTester;
    }
    /** get a component to fetch vosi availability infomation.*/
    public final VosiAvailability getAvailabilityTester() {
        return this.vosiTester;
    }
}