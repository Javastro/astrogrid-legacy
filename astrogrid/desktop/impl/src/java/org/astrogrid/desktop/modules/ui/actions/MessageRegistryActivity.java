package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.messaging.ExternalMessageTarget;
import org.astrogrid.desktop.modules.system.messaging.ResourceSetMessageSender;
import org.astrogrid.desktop.modules.system.messaging.ResourceSetMessageType;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.samp.Response;

import com.l2fprod.common.swing.JLinkButton;

/** Send a  Resource message for a resource (either via SAMP or PLASTIC).
 * I've modified this so that it only appears on the main menu - as it's advanced, and because of poor resource typeing, not universally applicable.
 *  */
public class MessageRegistryActivity extends AbstractResourceActivity {

	private final ExternalMessageTarget plas;
	
    @Override
    protected boolean invokable(final Resource r) {
		return true;
	}
	public MessageRegistryActivity(final ExternalMessageTarget descr) {
		super();
		setHelpID("activity.plastic.resource");
		this.plas = descr;
		MessagingScavenger.configureActivity("resource descriptions",this,plas);
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

	@Override
    public void actionPerformed(final ActionEvent e) {
		final List l = computeInvokable();
		final int sz = l.size();
		final Runnable r = new Runnable() {

            public void run() {                    
                sendLoadListMessage(l);
            }
		};
		confirmWhenOverThreshold(sz,"Send all " + sz + " resources?",r);
	}


	private void sendLoadListMessage(final List<Resource> resources) {
		(new BackgroundWorker<Response>(uiParent.get(),"Sending to " + plas.getName(),Thread.MAX_PRIORITY) {
		    {
		        setTransient(true);
		    }
			@Override
            protected Response construct() throws Exception {
				final List<URI> us = new ArrayList(resources.size());
				for (final Resource resource : resources) {
                    us.add(resource.getId());
                }
				final ResourceSetMessageSender sender = plas.createMessageSender(ResourceSetMessageType.instance);
				return sender.sendResourceSet(us,null);				
			}
			@Override
            protected void doFinished(final Response response) {
                if (response.isOK()) {    
                    parent.showTransientMessage(plas.getIcon(),plas.getName() + " received resources", "");          
            } else {
                parent.showTransientWarning(plas.getName() + ": " + response.getStatus(),response.getErrInfo().getErrortxt());
                logger.warn(response);
            }  	
			}			
		}).start();					
	}

	
}