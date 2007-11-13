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

/** dispay further info webpage for the selected resource.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 200712:05:11 PM
 */
public class FurtherInfoActivity extends AbstractResourceActivity implements Activity.Info {

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
		} catch (Exception x) { 
		    this.uiParent.get().showTransientError("Failed to open url",ExceptionFormatter.formatException(x));
		}	
	}
	protected final BrowserControl browser;
	/**
	 * 
	 */
	public FurtherInfoActivity(BrowserControl browser) {
		this.browser = browser;
		setIcon(IconHelper.loadIcon("help16.png"));
		//setText("Publisher's web page"); 
		setText("Further Info");
		setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,UIComponentMenuBar.MENU_KEYMASK));
	    setToolTipText("Display a webpage provided by the publisher about this resource");		
	}

}
