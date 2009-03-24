/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 16, 20094:06:59 PM
 */
public final class SpectrumMessageType extends MessageType<SpectrumMessageSender> {
    private static final URI SPECTRA_LOAD_FROM_URL =  URI.create("ivo://votech.org/spectrum/loadFromURL");

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

        public void sendSpectrum(final URL spectrumURL, final Map metadata, final String specID,
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
            //@todo handle response.

        }
    }


    @Override
    protected MessageUnmarshaller<SpectrumMessageSender> createPlasticUnmarshaller() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected SpectrumMessageSender createSampSender(final Object somethingSamp) {
        return null; //@implement
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
        return null; //@implement
    }

}
