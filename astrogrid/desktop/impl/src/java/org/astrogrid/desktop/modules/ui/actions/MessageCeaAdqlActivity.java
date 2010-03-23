/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.CeaService;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.messaging.CeaAdqlSetMessageType;
import org.astrogrid.desktop.modules.system.messaging.ExternalMessageTarget;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 23, 20101:48:55 PM
 */
public class MessageCeaAdqlActivity extends AbstractMessageResourceActivity {

    @Override
    protected boolean invokable(final Resource r) {
        if (r instanceof CatalogService && r instanceof CeaService) { // it's a dsa
            return true;
        }
        
        if (! (r instanceof CeaApplication)) { // else it must be a cea service.
            return false;
        }
        final int code = BuildQueryActivity.whatKindOfInterfaces((CeaApplication)r);
        return code >= 0;
        
    }
    
    public MessageCeaAdqlActivity(final ExternalMessageTarget samp) {
        super(CeaAdqlSetMessageType.instance,samp);
        MessagingScavenger.configureActivity("catalog query services (CEA)",this,samp);
    }

}
