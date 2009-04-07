package org.astrogrid.desktop.modules.ui.comp;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkListener;

import org.astrogrid.acr.ivoa.Vosi;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ivoa.resource.CapabilityTester;
import org.astrogrid.desktop.modules.ivoa.resource.PrettierResourceFormatter;
import org.astrogrid.desktop.modules.ui.voexplorer.google.CapabilityIconFactory;

/** Custom widget to display resources.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 11, 20072:22:46 PM
 */
public class ResourceDisplayPane extends JEditorPane {
	private final CapabilityTester capTester;
    private final Vosi vosiTester;
	
	/** construct a display pane with additional support for following hyperlinks */
	public ResourceDisplayPane(final HyperlinkListener hyper, final CapabilityTester capTester, final Vosi vosiTester, final CapabilityIconFactory iconFactory) {
		this.vosiTester = vosiTester;
        this.iconFactory = iconFactory;
        setContentType("text/html");
		setBorder(null);
		setEditable(false);
		putClientProperty("JEditorPane.honorDisplayProperties", Boolean.TRUE);		// this key is only defined on 1.5 - no effect on 1.4
		setFont(UIConstants.SANS_FONT);
		setName(ResourceDisplayPane.class.getName());
        this.capTester = capTester;
		addHyperlinkListener(hyper);
	}
	
	protected Resource currentResource;
	private String currentHTML;
    private final CapabilityIconFactory iconFactory;
	public void display(final Resource r) {
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
    public final Vosi getAvailabilityTester() {
        return this.vosiTester;
    }
    
    public final CapabilityIconFactory getIconFactory() {
        return this.iconFactory;
    }
}