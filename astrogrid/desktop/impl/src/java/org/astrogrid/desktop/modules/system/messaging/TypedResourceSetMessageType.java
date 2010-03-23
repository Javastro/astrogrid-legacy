/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URI;

/** baseclass for samp messages that exchange a strongly typed set of resources.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 23, 20101:26:57 PM
 */
 public abstract class TypedResourceSetMessageType extends ResourceSetMessageType {

    //implementors must provide this - deifnes the suffix to add to end of the base resource mtype
    public abstract String suffix() ;
    // adjust the message name
    @Override
    protected final String getSampMType() {
        return super.getSampMType() + suffix();
    }    

    // stub out the plastic side - not supported
    @Override
    protected final URI getPlasticMessageType() {
        return null;
    }
    
    @Override
    protected final ResourceSetMessageSender createPlasticSender(
            final PlasticApplicationDescription plas) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected final MessageUnmarshaller<ResourceSetMessageSender> createPlasticUnmarshaller() {
        throw new UnsupportedOperationException();
    }
}
