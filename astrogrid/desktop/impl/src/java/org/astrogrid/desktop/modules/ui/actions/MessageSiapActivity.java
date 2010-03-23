/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.SiapService;
import org.astrogrid.desktop.modules.system.messaging.ExternalMessageTarget;
import org.astrogrid.desktop.modules.system.messaging.SiapSetMessageType;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 23, 20101:48:28 PM
 */
public class MessageSiapActivity extends AbstractMessageResourceActivity {

    @Override
    protected boolean invokable(final Resource r) {
        return r instanceof SiapService;
    }
    
    
    /**
     * @param samp
     */
    public MessageSiapActivity(final ExternalMessageTarget samp) {
        super(SiapSetMessageType.instance,samp);
        MessagingScavenger.configureActivity("image services",this,samp);
    }
    

}
