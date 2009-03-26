/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.samp.Message;
import org.astrogrid.samp.client.HubConnection;
import org.astrogrid.samp.client.SampException;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 15, 20093:02:01 PM
 */
public final class BibcodeMessageType extends MessageType<BibcodeMessageSender> {
    private static final URI PASTIC_BIBCODE_MESSAGE = URI.create("ivo://votech.org/bibcode");

    private static final String SAMP_BIBCODE_MESSAGE = "bibcode.load";
    
    public final static BibcodeMessageType instance = new BibcodeMessageType();

    
    @Override
    protected BibcodeMessageSender createPlasticSender(final PlasticApplicationDescription plas) {
        return new PlasticSenderImpl(plas);
    }
    @Override
    protected BibcodeMessageSender createSampSender(final SampMessageTarget somethingSamp) {
        return new SampSender(somethingSamp);
    }
    
    @Override
    protected URI getPlasticMessageType() {
        return PASTIC_BIBCODE_MESSAGE;
    }

    @Override
    protected String getSampMType() {
        return SAMP_BIBCODE_MESSAGE;
    }
    
    @Override
    protected MessageUnmarshaller<BibcodeMessageSender> createPlasticUnmarshaller() {
        return new MessageUnmarshaller<BibcodeMessageSender>() {

            public Object handle(final ExternalMessageTarget source, final List args,
                    final BibcodeMessageSender handler) throws Exception {
                final Object o = args.get(0);
                if (o == null) {                    
                    return Boolean.FALSE;
                } else {
                    final String bibcode = o.toString();
                    if (StringUtils.isEmpty(bibcode)) {                       
                        return Boolean.FALSE;       
                    }
                    handler.setSource(source);
                    handler.sendBibcode(bibcode);
                    return Boolean.TRUE;
                }
            }
        };
    }
    
    @Override
    protected MessageUnmarshaller<BibcodeMessageSender> createSampUnmarshaller() {
        return new MessageUnmarshaller<BibcodeMessageSender>() {

            public Object handle(final ExternalMessageTarget source, final List rawInputs,
                    final BibcodeMessageSender handler) throws Exception {
                // look through inputs, one of which shuld be called 'bibcode'.
                String bibcode = null;
                for (final Map.Entry<?,?> e : (List<Map.Entry<?,?>>)rawInputs) {
                    if ("bibcode".equals(e.getKey())) {
                        bibcode = e.getValue().toString();
                    }
                }
                if (bibcode == null) {
                    throw new IllegalArgumentException("No 'bibcode' parameter provided");
                }
                handler.setSource(source);
                handler.sendBibcode(bibcode);
                return null;
            }
        };
    }    
    
    private class SampSender extends AbstractMessageSender implements BibcodeMessageSender {

        public SampSender(final SampMessageTarget t) {
            super(t);
        }

        public void sendBibcode(final String bibcode) {
            final SampMessageTarget t = (SampMessageTarget)getTarget();            
            try {
                final HubConnection connection = t.getHubConnector().getConnection();
                final Map params = new HashMap();
                params.put("bibcode",bibcode);              
                final Message msg = new Message(SAMP_BIBCODE_MESSAGE,params);
                connection.notify(t.getId(),msg);
            } catch (final SampException x) {
               throw new RuntimeException(x);
            }            
        }
    }

    private class PlasticSenderImpl extends AbstractMessageSender implements BibcodeMessageSender {

        /**
         * @param target
         */
        public PlasticSenderImpl(final PlasticApplicationDescription target) {            
            super(target);
        }
        
        public void sendBibcode(final String bibcode) {
            final List l = new ArrayList();        
            l.add(bibcode);
            final PlasticApplicationDescription plasTarget = (PlasticApplicationDescription)getTarget();
                        
            plasTarget.getTupperware().singleTargetFireAndForgetMessage(
                    getPlasticMessageType()
                    ,l
                    ,plasTarget.id);
        }

    }


}
