/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URI;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 16, 20093:52:16 PM
 */
public final class FitsTableMessageType extends MessageType<FitsTableMessageSender> {

    /**
     * 
     */
    private static final String SAMP_MTYPE = "table.load.fits";
    private final static FitsTableMessageType instance = new FitsTableMessageType();
    
    @Override
    protected FitsTableMessageSender createPlasticSender(
            final PlasticApplicationDescription plas) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected MessageUnmarshaller<FitsTableMessageSender> createPlasticUnmarshaller() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected FitsTableMessageSender createSampSender(final SampMessageTarget somethingSamp) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected MessageUnmarshaller<FitsTableMessageSender> createSampUnmarshaller() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected URI getPlasticMessageType() {
        return null;
    }

    @Override
    protected String getSampMType() {
        return SAMP_MTYPE;
    }

}
