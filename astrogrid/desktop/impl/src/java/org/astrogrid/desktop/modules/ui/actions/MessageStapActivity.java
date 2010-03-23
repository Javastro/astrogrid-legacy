/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.StapService;
import org.astrogrid.desktop.modules.system.messaging.ExternalMessageTarget;
import org.astrogrid.desktop.modules.system.messaging.StapSetMessageType;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 23, 20101:49:23 PM
 */
public class MessageStapActivity extends AbstractMessageResourceActivity {

    @Override
    protected boolean invokable(final Resource r) {
        return r instanceof StapService;
    }
    
    /**
     * @param samp
     */
    public MessageStapActivity(final ExternalMessageTarget samp) {
        super(StapSetMessageType.instance,samp);
        MessagingScavenger.configureActivity("time-range services",this,samp);
    }
    

}
