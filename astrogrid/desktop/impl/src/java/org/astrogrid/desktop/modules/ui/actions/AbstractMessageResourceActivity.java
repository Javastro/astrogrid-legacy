/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.Tuple;
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
            
            private final ResourceSummarizer summarizer = new ResourceSummarizer();
            
            @Override
            protected Response construct() throws Exception {

                summarizer.clear();
                // pack all the resource ids into a list.
                final List<URI> us = new ArrayList(resources.size());
                for (final Resource resource : resources) {
                    summarizer.add(resource);
                    us.add(resource.getId());
                }

                final ResourceSetMessageSender sender = samp.createMessageSender(mtype);
                return sender.sendResourceSet(us,mkTitle());             
            }
            
            /** construct a title for this message */
            private String mkTitle() {
                if (resources.size() == 1) { // pluck a field from the single resource.
                    final Resource r =resources.get(0);
                    if (StringUtils.isNotBlank(r.getShortName())) {
                        return r.getShortName();
                    } else {
                        return r.getTitle();
                    }
                } else {
                    // format a summary of the resources to use as a title for the message
                    final StringBuilder title = new StringBuilder();
                    boolean first = true;
                    for (final Tuple<Integer,String> tuple : summarizer) {
                        if (first) {
                            first = false;
                        } else {
                            title.append(", ");
                        }
                        title.append(tuple.fst())
                        .append(" ")
                        .append(tuple.snd());
                    }
                    return title.toString();
                }
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
