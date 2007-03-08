/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.tasks;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.astrogrid.acr.ivoa.resource.Contact;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.desktop.icons.IconHelper;

/** contac the curator person for a resource.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 200711:56:23 AM
 */
public class ContactTask extends ResourceTask {

	protected boolean invokable(Resource r) {
		 Contact[] contacts = r.getCuration().getContacts();
		 return contacts != null && contacts.length > 0 && contacts[0].getEmail() != null;
	}
	public void someSelected(List list) {
		noneSelected();
	}
	public void oneSelected(Resource resource) {
		if (invokable(resource)) {
			setEnabled(true);
			setText(resource.getCuration().getContacts()[0].getName().getValue());
			current = Collections.singletonList(resource);
		} else {
			setEnabled( false);
			current = null;
		}
	}
	  
	private final BrowserControl browser;
/**
 * 
 */
public ContactTask(BrowserControl browser) {
	this.browser = browser;
	setIcon(IconHelper.loadIcon("person16.png"));
	setToolTipText("Email the contact responsible for this resouce");
	setText("Email curator"); // @todo display contact name here.
}

public void actionPerformed(ActionEvent e) {
	List l = computeInvokable();
	Resource r = (Resource)l.get(0);
	String email = r.getCuration().getContacts()[0].getEmail();
	try {
		browser.openURL(new URL("mailto:"+ email));
	} catch (Exception x) { // @todo display error dialogue.
		logger.error("Failed to open mailto:",x);
	}	
}

}
