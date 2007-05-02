/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.net.URI;
import java.util.List;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.desktop.icons.IconHelper;

/** dispay further info webpage for the selected resource.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 200712:05:11 PM
 */
public class FurtherInfoActivity extends AbstractResourceActivity {

	protected boolean invokable(Resource r) {
		return r.getContent().getReferenceURI() != null;
	}
	public void someSelected(Resource[] arr) {
		noneSelected();
	}
	public void actionPerformed(ActionEvent e) {
		List l = computeInvokable();
		Resource r = (Resource)l.get(0);		
		URI referenceURI = r.getContent().getReferenceURI() ;
		try {
			browser.openURL(referenceURI.toURL());
		} catch (Exception x) { // @todo display error dialogue.
			logger.error("Failed to open further information",x);
		}	
	}
	protected final BrowserControl browser;
	/**
	 * 
	 */
	public FurtherInfoActivity(BrowserControl browser) {
		this.browser = browser;
		setIcon(IconHelper.loadIcon("help16.png"));
		setText("Further information");
	    setToolTipText("Display a webpage of further information");		
	}

}
