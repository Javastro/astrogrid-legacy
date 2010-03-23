/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.VospaceService;
import org.astrogrid.desktop.modules.system.messaging.ExternalMessageTarget;
import org.astrogrid.desktop.modules.system.messaging.VospaceSetMessageType;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 23, 20101:49:49 PM
 */
public class MessageVospaceActivity extends AbstractMessageResourceActivity {

    /**
     * @param mtype
     * @param samp
     */
    public MessageVospaceActivity( final ExternalMessageTarget samp) {
        super(VospaceSetMessageType.instance, samp);
        MessagingScavenger.configureActivity("vospace services",this,samp);
    }

    @Override
    protected boolean invokable(final Resource r) {
        return r instanceof VospaceService;
    }
    

}
