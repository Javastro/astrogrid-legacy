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
import org.astrogrid.samp.Response;

/** Send a 'bibcode' message for a resource (either over SAMP or PLASTIC)
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
		(new BackgroundWorker<Response>(uiParent.get(),"Sending to " + target.getName(),Thread.MAX_PRIORITY) {						
			@Override
            protected Response construct() throws Exception {
			    final BibcodeMessageSender sender = target.createMessageSender(BibcodeMessageType.instance);
				return sender.sendBibcode(r.getContent().getSource().getValue());
			}
			// indicate when hand-off happended.
			@Override
            protected void doFinished(final Response response) {
                if (response.isOK()) {    
                    parent.showTransientMessage(target.getIcon(),target.getName() + " received bibcode", "");          
            } else {
                parent.showTransientWarning(target.getName() + ": " + response.getStatus(),response.getErrInfo().getErrortxt());
                logger.warn(response);
            }       			}
			
		}).start();
	}

	
}