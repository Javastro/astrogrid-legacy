/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.messaging.ExternalMessageTarget;
import org.astrogrid.desktop.modules.system.messaging.MessageType;
import org.astrogrid.desktop.modules.system.messaging.ResourceSetMessageSender;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.samp.Response;

/** abstract class for activities which send a set of resources over SAMP.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 23, 20101:14:39 PM
 */
 abstract class AbstractMessageResourceActivity extends AbstractResourceActivity {


    /**
     * @param mtype the message type that the concrete subtype of this class will support
     * @param samp the target to send messages to.
     */
    public AbstractMessageResourceActivity(
            final MessageType<ResourceSetMessageSender> mtype,
            final ExternalMessageTarget samp) {
        super();
        this.mtype = mtype;
        this.samp = samp;
    }


    protected final MessageType<ResourceSetMessageSender> mtype;
    protected final ExternalMessageTarget samp;
    

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
        (new BackgroundWorker<Response>(uiParent.get(),"Sending to " + samp.getName(),Thread.MAX_PRIORITY) {
            {
                setTransient(true);
            }
            @Override
            protected Response construct() throws Exception {
                final List<URI> us = new ArrayList(resources.size());
                for (final Resource resource : resources) {
                    us.add(resource.getId());
                }
                final ResourceSetMessageSender sender = samp.createMessageSender(mtype);
                return sender.sendResourceSet(us,null);             
            }
            @Override
            protected void doFinished(final Response response) {
                if (response.isOK()) {    
                    parent.showTransientMessage(samp.getIcon(),samp.getName() + " received resources", "");          
            } else {
                parent.showTransientWarning(samp.getName() + ": " + response.getStatus(),response.getErrInfo().getErrortxt());
                logger.warn(response);
            }   
            }           
        }).start();                 
    }
    
    
}
