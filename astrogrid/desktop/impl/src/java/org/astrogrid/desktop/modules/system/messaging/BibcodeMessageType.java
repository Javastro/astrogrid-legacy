/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 15, 20093:02:01 PM
 * @implement
 */
public final class BibcodeMessageType extends MessageType<BibcodeMessageSender> {
    private static final URI PASTIC_BIBCODE_MESSAGE = URI.create("ivo://votech.org/bibcode");

    public final static BibcodeMessageType instance = new BibcodeMessageType();

    
    @Override
    protected BibcodeMessageSender createPlasticSender(final PlasticApplicationDescription plas) {
        return new PlasticSenderImpl(plas);
    }
    @Override
    protected BibcodeMessageSender createSampSender(final Object somethingSamp) {
        return null; //@implement
    }
    
    @Override
    protected URI getPlasticMessageType() {
        return PASTIC_BIBCODE_MESSAGE;
    }

    @Override
    protected String getSampMType() {
        return null; //@implement
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
                    //@todo worrk out how to get return value.
                    return Boolean.TRUE;
                }
            }
        };
    }
    
    @Override
    protected MessageUnmarshaller<BibcodeMessageSender> createSampUnmarshaller() {
        return null; //@implement
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
