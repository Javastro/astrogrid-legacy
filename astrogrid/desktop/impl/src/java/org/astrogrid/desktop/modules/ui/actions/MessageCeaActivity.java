/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.messaging.CeaSetMessageType;
import org.astrogrid.desktop.modules.system.messaging.ExternalMessageTarget;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 23, 20101:48:42 PM
 */
public class MessageCeaActivity extends AbstractMessageResourceActivity {

    @Override
    protected boolean invokable(final Resource r) {
        return r instanceof CeaApplication;
    }
    
    public MessageCeaActivity(final ExternalMessageTarget samp) {
        super(CeaSetMessageType.instance,samp);
        MessagingScavenger.configureActivity("remote applications(CEA)",this,samp);
    }

}
