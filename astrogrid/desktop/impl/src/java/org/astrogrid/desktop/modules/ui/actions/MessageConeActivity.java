/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.messaging.ConeSetMessageType;
import org.astrogrid.desktop.modules.system.messaging.ExternalMessageTarget;

/** Send a set of cone-capability resources via samp
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 23, 201012:46:42 PM
 */
public class MessageConeActivity extends AbstractMessageResourceActivity {

    @Override
    protected boolean invokable(final Resource r) {
        return r instanceof ConeService;
    }
    
    /**
     * @param samp
     */
    public MessageConeActivity(final ExternalMessageTarget samp) {
        super(ConeSetMessageType.instance,samp);
        MessagingScavenger.configureActivity("cone services",this,samp);
    }
    

    

}
