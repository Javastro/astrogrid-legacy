/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.SsapService;
import org.astrogrid.desktop.modules.system.messaging.ExternalMessageTarget;
import org.astrogrid.desktop.modules.system.messaging.SsapSetMessageType;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 23, 20101:49:12 PM
 */
public class MessageSsapActivity extends AbstractMessageResourceActivity {

    @Override
    protected boolean invokable(final Resource r) {
        return r instanceof SsapService;
    }
    
    
    /**
     * @param samp
     */
    public MessageSsapActivity(final ExternalMessageTarget samp) {
        super(SsapSetMessageType.instance,samp);
        MessagingScavenger.configureActivity("spectrum services",this,samp);
    }
    

}
