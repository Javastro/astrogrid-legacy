package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.List;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Source;
import org.astrogrid.desktop.modules.system.messaging.BibcodeMessageSender;
import org.astrogrid.desktop.modules.system.messaging.BibcodeMessageType;
import org.astrogrid.desktop.modules.system.messaging.ExternalMessageTarget;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;

/** Send a PLASTIC 'bibcode' message for a resource.
 *  */
public class MessageBibcodeActivity extends AbstractResourceActivity {
	private final ExternalMessageTarget target;

	@Override
    protected boolean invokable(final Resource r) {
	final Source source = r.getContent().getSource();
	return source != null 
	    && source.getFormat() != null
	    && source.getValue() != null
	    && source.getFormat().equals("bibcode")
	    ;
	}
	
	// doesn't accept multiple selections.
	@Override
    public void someSelected(final Resource[] list) {
	    noneSelected();
	}
	
	
	public MessageBibcodeActivity(final ExternalMessageTarget descr) {
		super();
		setHelpID("activity.plastic.bibcode");
		this.target = descr;
		MessagingScavenger.configureActivity("bibcode",this,target);
	}


	@Override
    public void actionPerformed(final ActionEvent e) {
		final List l = computeInvokable();
        final Runnable act= new Runnable() {

            public void run() {
		for (final Iterator i = l.iterator(); i.hasNext();) {
            final Resource r = (Resource) i.next();
            sendBibcodeMessage(r);
        }
            }
        };
        final int sz = l.size();
        confirmWhenOverThreshold(sz,"Send all " + " resources?",act);	
	}
	private void sendBibcodeMessage(final Resource r) {
		(new BackgroundWorker(uiParent.get(),"Sending to " + target.getName(),Thread.MAX_PRIORITY) {						
			@Override
            protected Object construct() throws Exception {
			    final BibcodeMessageSender sender = target.createMessageSender(BibcodeMessageType.instance);
			    //@todo maybe add a response listener.
			   
				sender.sendBibcode(r.getContent().getSource().getValue());
				return null;
			}
			// indicate when hand-off happended.
			@Override
            protected void doFinished(final Object result) {
			    parent.showTransientMessage("Message sent","Sent bibode to " + target.getName());				
			}
			
		}).start();
	}

	
}