/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.astrogrid.samp.ErrInfo;
import org.astrogrid.samp.Message;
import org.astrogrid.samp.Response;
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

        public Response sendFitsImage(final URL fitsURL, final String imageId, final String imageName) {
            final PlasticApplicationDescription plasTarget = (PlasticApplicationDescription)getTarget();
            try {
                final List<Object> l = new ArrayList<Object>();
                l.add(maybeRewriteForAccessability(fitsURL,plasTarget.getTupperware().getWebserverRoot()).toString());                            
                plasTarget.getTupperware().singleTargetFireAndForgetMessage(
                        CommonMessageConstants.FITS_LOAD_FROM_URL
                        ,l
                        ,plasTarget.id);
                //mock up a response.
                return new Response(Response.OK_STATUS,null,null);    
            } catch (final MalformedURLException x) {
                final ErrInfo err = new ErrInfo(x);
                err.setErrortxt("Failed to rewrite source URL");
                return new Response(Response.ERROR_STATUS,null,err);                
            }               
        }
    }

    private class SampSender extends AbstractMessageSender implements FitsImageMessageSender {

        public SampSender(final SampMessageTarget t) {
            super(t);
        }

        public Response sendFitsImage(final URL fitsURL, final String imageId, final String imageName) {
            final SampMessageTarget t = (SampMessageTarget)getTarget();            
            try {
                final Map params = new HashMap();
                params.put("url",maybeRewriteForAccessability(fitsURL,t.getSampImpl().getWebserverRoot()).toString());
                if (imageId != null) {
                    params.put("image-id",imageId);
                }
                if (imageName != null) {
                    params.put("name",imageName);
                }
                final Message msg = new Message(SAMP_MTYPE,params);
                return t.getHubConnector().callAndWait(t.getId(),msg,DEFAULT_TIMEOUT);
            } catch (final SampException x) {
                final ErrInfo err = new ErrInfo(x);
                err.setErrortxt("Failed to send message");
                return new Response(Response.ERROR_STATUS,null,err);  
            } catch (final MalformedURLException x) {
                final ErrInfo err = new ErrInfo(x);
                err.setErrortxt("Failed to rewrite source URL");
                return new Response(Response.ERROR_STATUS,null,err);
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
