/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URI;

/** Message Type for exchanging a 'hilight row' command.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 16, 20093:53:38 PM
 */
public final class HighlightRowMessageType extends MessageType<HighlightRowMessageSender> {

    /**
     * 
     */
    private static final String SAMP_MTYPE = "table.highlight.row";
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
    protected HighlightRowMessageSender createSampSender(final SampMessageTarget somethingSamp) {
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
        return SAMP_MTYPE;
    }

}
