/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.util.List;

import javax.swing.KeyStroke;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;

/** Dispay the further info webpage for a resource.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 200712:05:11 PM
 */
public class FurtherInfoActivity extends AbstractResourceActivity implements Activity.Info {

	@Override
    protected boolean invokable(final Resource r) {
		return r.getContent() != null &&  r.getContent().getReferenceURI() != null;
	}
	@Override
    public void someSelected(final Resource[] arr) {
		noneSelected();
	}
	@Override
    public void actionPerformed(final ActionEvent e) {
		final List l = computeInvokable();
		final Resource r = (Resource)l.get(0);		
		final URI referenceURI = r.getContent().getReferenceURI() ;
		try {
			browser.openURL(referenceURI.toURL());
		} catch (final Exception x) { 
		    this.uiParent.get().showTransientError("Failed to open url",ExceptionFormatter.formatException(x));
		}	
	}
	protected final BrowserControl browser;
	/**
	 * 
	 */
	public FurtherInfoActivity(final BrowserControl browser) {
	    setHelpID("activity.furtherinfo");
		this.browser = browser;
		setIcon(IconHelper.loadIcon("help16.png"));
		//setText("Publisher's web page"); 
		setText("Further Info");
		setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,UIComponentMenuBar.MENU_KEYMASK));
	    setToolTipText("Display a webpage provided by the publisher about this resource");		
	}

}
