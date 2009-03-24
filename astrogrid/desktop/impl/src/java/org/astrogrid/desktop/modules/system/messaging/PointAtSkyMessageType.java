/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URI;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 16, 20093:57:55 PM
 */
public final class PointAtSkyMessageType extends MessageType<PointAtSkyMessageSender> {

    private final static PointAtSkyMessageType instance = new PointAtSkyMessageType();
    private static final URI PLASTIC_MESSAGE = URI.create("ivo://votech.org/sky/pointAtCoords");
    @Override
    protected PointAtSkyMessageSender createPlasticSender(
            final PlasticApplicationDescription plas) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected MessageUnmarshaller<PointAtSkyMessageSender> createPlasticUnmarshaller() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected PointAtSkyMessageSender createSampSender(final Object somethingSamp) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected MessageUnmarshaller<PointAtSkyMessageSender> createSampUnmarshaller() {
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
