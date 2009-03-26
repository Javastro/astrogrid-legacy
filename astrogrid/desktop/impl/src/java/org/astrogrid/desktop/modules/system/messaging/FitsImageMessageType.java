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
 * @since Mar 16, 20093:50:19 PM
 */
public final class FitsImageMessageType extends MessageType<FitsImageMessageSender> {

    /**
     * 
     */
    private static final String SAMP_MTYPE = "image.load.fits";
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

    private class SampSender extends AbstractMessageSender implements FitsImageMessageSender {

        public SampSender(final SampMessageTarget t) {
            super(t);
        }

        public void sendFitsImage(final URL fitsURL, final String imageId, final String imageName) {
            final SampMessageTarget t = (SampMessageTarget)getTarget();            
            try {
                final HubConnection connection = t.getHubConnector().getConnection();
                final Map params = new HashMap();
                params.put("url",fitsURL.toString());
                if (imageId != null) {
                    params.put("image-id",imageId);
                }
                if (imageName != null) {
                    params.put("name",imageName);
                }
                final Message msg = new Message(SAMP_MTYPE,params);
                connection.notify(t.getId(),msg);
            } catch (final SampException x) {
                throw new RuntimeException(x);
            }              
        }
    }


    @Override
    protected MessageUnmarshaller<FitsImageMessageSender> createPlasticUnmarshaller() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected FitsImageMessageSender createSampSender(final SampMessageTarget somethingSamp) {
        return new SampSender(somethingSamp);
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
        return SAMP_MTYPE;
    }

}
