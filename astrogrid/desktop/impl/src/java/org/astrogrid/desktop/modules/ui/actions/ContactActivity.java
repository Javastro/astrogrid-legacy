/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.List;

import javax.swing.KeyStroke;

import org.astrogrid.acr.ivoa.resource.Contact;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;

/** Contact the curator of a resource.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 200711:56:23 AM
 */
public class ContactActivity extends AbstractResourceActivity implements Activity.Info {

	protected boolean invokable(final Resource r) {
		 final Contact[] contacts = r.getCuration().getContacts();
		 return contacts != null && contacts.length > 0 && contacts[0].getEmail() != null;
	}
	public void someSelected(final Resource[] list) {
		noneSelected();
	}
	public void oneSelected(final Resource resource) {
		if (invokable(resource)) {
			setEnabled(true);
			current = new Resource[]{resource};
		} else {
			setEnabled( false);
			current = null;
		}
	}
	  
	private final BrowserControl browser;
/**
 * 
 */
public ContactActivity(final BrowserControl browser) {
    setHelpID("activity.contact");
	this.browser = browser;
	setIcon(IconHelper.loadIcon("person16.png"));
	setToolTipText("Email the contact responsible for this resouce");
	setText("Email Curator");
	setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,UIComponentMenuBar.MENU_KEYMASK));
}

public void actionPerformed(final ActionEvent e) {
	final List l = computeInvokable();
	final Resource r = (Resource)l.get(0);
	final String email = r.getCuration().getContacts()[0].getEmail();
	try {
		browser.openURL(new URL("mailto:"+ email));
	} catch (final Exception x) { 
	    this.uiParent.get().showTransientError("Unable to open mailto: link",ExceptionFormatter.formatException(x));
	}	
}

}
