/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.votech.plastic.CommonMessageConstants;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 16, 20094:08:34 PM
 */
public final class VotableMessageType extends MessageType<VotableMessageSender> {

    public static final VotableMessageType instance = new VotableMessageType();
    
    @Override
    protected VotableMessageSender createPlasticSender(
            final PlasticApplicationDescription plas) {
        return new PlasticSender(plas);
    }

    private class PlasticSender extends AbstractMessageSender implements VotableMessageSender {

        /**
         * @param target
         */
        public PlasticSender(final PlasticApplicationDescription target) {
            super(target);
        }

        public void sendVotable(final URL votableURL, final String tableID, final String tableName) {
            final PlasticApplicationDescription plasTarget = (PlasticApplicationDescription)getTarget();
                        
            final List l = new ArrayList();
            l.add(votableURL.toString());
            l.add(tableName);  
            plasTarget.getTupperware().singleTargetFireAndForgetMessage(
                    getPlasticMessageType()
                    ,l
                    ,plasTarget.id);
        }
    }

    
    @Override
    protected MessageUnmarshaller<VotableMessageSender> createPlasticUnmarshaller() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected VotableMessageSender createSampSender(final Object somethingSamp) {
        return null; //@implement
    }

    @Override
    protected MessageUnmarshaller<VotableMessageSender> createSampUnmarshaller() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected URI getPlasticMessageType() {
        return CommonMessageConstants.VOTABLE_LOAD_FROM_URL;
    }

    @Override
    protected String getSampMType() {
        return null; //@implement
    }

}
