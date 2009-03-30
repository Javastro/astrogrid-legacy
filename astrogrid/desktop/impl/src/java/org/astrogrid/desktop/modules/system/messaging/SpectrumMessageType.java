/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.astrogrid.samp.ErrInfo;
import org.astrogrid.samp.Message;
import org.astrogrid.samp.Response;
import org.astrogrid.samp.client.SampException;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 16, 20094:06:59 PM
 */
public final class SpectrumMessageType extends MessageType<SpectrumMessageSender> {
    private static final URI SPECTRA_LOAD_FROM_URL =  URI.create("ivo://votech.org/spectrum/loadFromURL");
    private static final String SAMP_MTYPE = "spectrum.load.ssa-generic";
    
    public final static SpectrumMessageType instance = new SpectrumMessageType();
    @Override
    protected SpectrumMessageSender createPlasticSender(
            final PlasticApplicationDescription plas) {
        return new PlasticSender(plas);
    }
    
    private class PlasticSender extends AbstractMessageSender implements SpectrumMessageSender {

        /**
         * @param target
         */
        public PlasticSender(final PlasticApplicationDescription target) {
            super(target);
        }

        public Response sendSpectrum(final URL spectrumURL, final Map metadata, final String specID,
                final String specName) {
            final List l = new ArrayList();
            l.add(spectrumURL.toString());// url
            l.add(specName);
            final Hashtable t;
            if (metadata instanceof Hashtable) { // must be hashtable for xmlrpc library
                t = (Hashtable)metadata;
            } else {
                t = new Hashtable(metadata);
            }
            //MapUtils.verbosePrint(System.err,"params",t);
            l.add(t);
            final PlasticApplicationDescription plasTarget = (PlasticApplicationDescription)getTarget();
            
            plasTarget.getTupperware().singleTargetFireAndForgetMessage(
                    getPlasticMessageType()
                    ,l
                    ,plasTarget.id);
            //mock up a response.
            return new Response(Response.OK_STATUS,null,null);

        }
    }
    
    private class SampSender extends AbstractMessageSender implements SpectrumMessageSender {

        /**
         * @param somethingSamp
         */
        public SampSender(final SampMessageTarget t) {
            super(t);
        }

        public Response sendSpectrum(final URL spectrumURL, final Map metadata, final String specID,
                final String specName) {
            final SampMessageTarget t = (SampMessageTarget)getTarget();            
            try {
                final Map<String,Object> params = new HashMap<String,Object>();
                final Map<String,String> ids = new HashMap<String,String>();
                params.put("url",spectrumURL.toString());
                params.put("meta",metadata);
            
                if (specID != null) {
                    params.put("spectrum-id",specID);
                }
                if (specName != null) {
                    params.put("name",specName);
                }
                final Message msg = new Message(SAMP_MTYPE,params);
                return t.getHubConnector().callAndWait(t.getId(),msg,DEFAULT_TIMEOUT);
            } catch (final SampException x) {
                final ErrInfo err = new ErrInfo(x);
                err.setErrortxt("Failed to send message");
                return new Response(Response.ERROR_STATUS,null,err);   
            }
        }
    }


    @Override
    protected MessageUnmarshaller<SpectrumMessageSender> createPlasticUnmarshaller() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected SpectrumMessageSender createSampSender(final SampMessageTarget somethingSamp) {
        return new SampSender(somethingSamp);
    }

    @Override
    protected MessageUnmarshaller<SpectrumMessageSender> createSampUnmarshaller() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected URI getPlasticMessageType() {
        return SPECTRA_LOAD_FROM_URL;
    }

    @Override
    protected String getSampMType() {
        return SAMP_MTYPE;
    }

}
