/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.astrogrid.samp.Message;
import org.astrogrid.samp.client.HubConnection;
import org.astrogrid.samp.client.SampException;
import org.votech.plastic.CommonMessageConstants;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 16, 20094:08:34 PM
 */
public final class VotableMessageType extends MessageType<VotableMessageSender> {

    /**
     * 
     */
    private static final String SAMP_MTYPE = "table.load.votable";
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
    protected VotableMessageSender createSampSender(final SampMessageTarget somethingSamp) {
        return new SampSender(somethingSamp);
    }
    
    private class SampSender extends AbstractMessageSender implements VotableMessageSender {

        /**
         * @param somethingSamp
         */
        public SampSender(final SampMessageTarget target) {
            super(target);
        }

        public void sendVotable(final URL votableURL, final String tableID, final String tableName) {
            final SampMessageTarget t = (SampMessageTarget)getTarget();
                    
            try {
                final HubConnection connection = t.getHubConnector().getConnection();
                final Map params = new HashMap();
                params.put("url",votableURL.toString());
                if (tableID != null) {
                    params.put("table-id",tableID);
                }
                if (tableName != null) {
                    params.put("name",tableName);
                }
                final Message msg = new Message(SAMP_MTYPE,params);
                connection.notify(t.getId(),msg);
            } catch (final SampException x) {
                throw new RuntimeException(x);
            }
            
        }
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
        return SAMP_MTYPE;
    }

}
