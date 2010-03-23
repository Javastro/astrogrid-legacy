package org.astrogrid.desktop.modules.ui.actions;

import javax.swing.JMenuItem;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.messaging.ExternalMessageTarget;
import org.astrogrid.desktop.modules.system.messaging.ResourceSetMessageType;

import com.l2fprod.common.swing.JLinkButton;

/** Send a  Resource message for a resource (either via SAMP or PLASTIC).
 * I've modified this so that it only appears on the main menu - as it's advanced, and because of poor resource typeing, not universally applicable.
 *  */
public class MessageResourcesActivity extends AbstractMessageResourceActivity {


    @Override
    protected boolean invokable(final Resource r) {
		return true;
	}
	public MessageResourcesActivity(final ExternalMessageTarget descr) {
		super(ResourceSetMessageType.instance,descr);
		setHelpID("activity.plastic.resource");
		MessagingScavenger.configureActivity("resource descriptions",this,samp);
	}

	  // create components but keep them invisible.
    @Override
    public JLinkButton createLinkButton() {
        final JLinkButton b = new JLinkButton(this);
        b.setVisible(false);
        return b;
    }
    @Override
    public JMenuItem createHidingMenuItem() {
        final JMenuItem i = new JMenuItem(this);
        i.setVisible(false);
        return i;
    }
//	public JMenuItem createMenuItem() {
//	    return super.createHidingMenuItem();
//	}


	
}