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
 * @since Mar 16, 20093:50:19 PM
 * @todo implement
 */
public final class FitsImageMessageType extends MessageType<FitsImageMessageSender> {

    public static final FitsImageMessageType instance = new FitsImageMessageType();
    
    @Override
    protected FitsImageMessageSender createPlasticSender(
            final PlasticApplicationDescription plas) {
       return new PlasticSender(plas);
    }
    
    
    private class PlasticSender extends AbstractMessageSender implements FitsImageMessageSender {

        /**
         * @param target
         */
        public PlasticSender(final PlasticApplicationDescription target) {
            super(target);
        }

        public void sendFitsImage(final URL fitsURL, final String imageId, final String imageName) {
            final PlasticApplicationDescription plasTarget = (PlasticApplicationDescription)getTarget();
                        
            final List l = new ArrayList();
            l.add(fitsURL.toString());
            plasTarget.getTupperware().singleTargetFireAndForgetMessage(
                    CommonMessageConstants.FITS_LOAD_FROM_URL
                    ,l
                    ,plasTarget.id);   
        }
    }



    @Override
    protected MessageUnmarshaller<FitsImageMessageSender> createPlasticUnmarshaller() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected FitsImageMessageSender createSampSender(final Object somethingSamp) {
        return null; //@implement
    }

    @Override
    protected MessageUnmarshaller<FitsImageMessageSender> createSampUnmarshaller() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected URI getPlasticMessageType() {
        return CommonMessageConstants.FITS_LOAD_FROM_URL;
    }

    @Override
    protected String getSampMType() {
        return null; //@implement
    }

}
