/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URI;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 16, 20093:53:38 PM
 */
public final class HighlightRowMessageType extends MessageType<HighlightRowMessageSender> {

    private static final HighlightRowMessageType instance = new HighlightRowMessageType();
    private static final URI PLASTIC_MESSAGE = URI.create("ivo://votech.org/votable/highlightObject");
    @Override
    protected HighlightRowMessageSender createPlasticSender(
            final PlasticApplicationDescription plas) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected MessageUnmarshaller<HighlightRowMessageSender> createPlasticUnmarshaller() {
       throw new UnsupportedOperationException();
    }

    @Override
    protected HighlightRowMessageSender createSampSender(final Object somethingSamp) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected MessageUnmarshaller<HighlightRowMessageSender> createSampUnmarshaller() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected URI getPlasticMessageType() {
        return PLASTIC_MESSAGE;
    }

    @Override
    protected String getSampMType() {
        return null; //@implement
    }

}
