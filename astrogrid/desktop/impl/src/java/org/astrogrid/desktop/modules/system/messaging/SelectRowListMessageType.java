/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URI;

import org.votech.plastic.CommonMessageConstants;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 16, 20094:04:03 PM
 */
public final class SelectRowListMessageType extends MessageType<SelectRowListMessageSender> {

    /**
     * 
     */
    private static final String SAMP_MTYPE = "table.select.rowList";
    public static final SelectRowListMessageType instance = new SelectRowListMessageType();
    
    @Override
    protected SelectRowListMessageSender createPlasticSender(
            final PlasticApplicationDescription plas) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected MessageUnmarshaller<SelectRowListMessageSender> createPlasticUnmarshaller() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected SelectRowListMessageSender createSampSender(final SampMessageTarget somethingSamp) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected MessageUnmarshaller<SelectRowListMessageSender> createSampUnmarshaller() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected URI getPlasticMessageType() {
        return CommonMessageConstants.VO_SHOW_OBJECTS;
    }

    @Override
    protected String getSampMType() {
        return SAMP_MTYPE;
    }

}
