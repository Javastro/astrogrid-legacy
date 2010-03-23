/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.TapService;
import org.astrogrid.desktop.modules.system.messaging.ExternalMessageTarget;
import org.astrogrid.desktop.modules.system.messaging.TapSetMessageType;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 23, 20101:49:37 PM
 */
public class MessageTapActivity extends AbstractMessageResourceActivity {

    @Override
    protected boolean invokable(final Resource r) {
        return r instanceof TapService;
    }
    /**
     * @param samp
     */
    public MessageTapActivity(final ExternalMessageTarget samp) {
        super(TapSetMessageType.instance,samp);
        MessagingScavenger.configureActivity("catalog query services",this,samp);
    }
    

}
