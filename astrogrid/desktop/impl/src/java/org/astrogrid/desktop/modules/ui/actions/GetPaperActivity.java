/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.List;

import javax.swing.KeyStroke;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ivoa.resource.PrettierResourceFormatter;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;

/** Display a link to the webpage associated with a bibcode.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 200712:05:11 PM
 */
public class GetPaperActivity extends AbstractResourceActivity implements Activity.Info {

	protected boolean invokable(final Resource r) {
		return r.getContent() != null 
		    && r.getContent().getSource() != null
		    && "bibcode".equalsIgnoreCase(r.getContent().getSource().getFormat());
	}
	public void someSelected(final Resource[] arr) {
		noneSelected();
	}
	public void actionPerformed(final ActionEvent e) {
		final List l = computeInvokable();
		final Resource r = (Resource)l.get(0);	
		final String bibcode = r.getContent().getSource().getValue();
		try {
		    final URL url = new URL(PrettierResourceFormatter.BIBCODE_URL + bibcode);	
			browser.openURL(url);
		} catch (final Exception x) { 
		    this.uiParent.get().showTransientError("Failed to open url",ExceptionFormatter.formatException(x));
		}	
	}
	protected final BrowserControl browser;
	/**
	 * 
	 */
	public GetPaperActivity(final BrowserControl browser) {
	    setHelpID("activity.getpaper");
		this.browser = browser;
		setIcon(IconHelper.loadIcon("document16.png"));
		//setText("Publisher's web page"); 
		setText("Publication");
		setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,UIComponentMenuBar.MENU_KEYMASK));
	    setToolTipText("Display a webpage describing the publication that this resource relates to");		
	}

}
