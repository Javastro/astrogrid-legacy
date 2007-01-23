package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.List;

/**
 * Include tests only appropriate for plastic listeners that can respond to messages.
 * @see AbstractPlasticBase
 * @author jdt
 *
 */
public abstract class AbstractPlasticBaseNotDeaf extends AbstractPlasticBase {
 
    
    /**
     * Once you've registered with a message, that should be it - you should
     * be immune to any changes made on the client side.
     *
     */
    public void testSupportedMessagesImmutable() throws Exception {
        URI plid1 = createAndRegisterCleanApp(1,null);
        List supportedMessages = hub.getUnderstoodMessages(plid1);
        assertTrue("Test app doesn't support any messages",supportedMessages.size()>0);
      //NW: URI vs String problem  URI firstMessage = (URI) supportedMessages.get(0);
        URI firstMessage = new URI(supportedMessages.get(0).toString());
        List appsUnderstandingMessage = hub.getMessageRegisteredIds(firstMessage);
        assertTrue("Application doesn't understand a message it registered for", appsUnderstandingMessage.contains(plid1));
        try {
            supportedMessages.remove(firstMessage);
        } catch (UnsupportedOperationException e) {
            //that's ok - means that the supportedmessages are immutable
            return;
        }
        
        List appsNowUnderstandingMessage = hub.getMessageRegisteredIds(firstMessage);
        assertTrue("Application doesn't understand a message it registered for, following deletion client-side", appsNowUnderstandingMessage.contains(plid1));
        
        
        
    }
    
  

}
