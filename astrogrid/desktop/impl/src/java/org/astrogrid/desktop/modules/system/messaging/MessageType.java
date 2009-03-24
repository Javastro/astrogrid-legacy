/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URI;

/** Abstract class for message types.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 15, 20092:58:32 PM
 */
public abstract class MessageType<S extends MessageSender> {

    
    /** return the SAMP MType this message type matches against
     * 
     *  return null if not applicatblae for this protocol*/
    protected abstract String getSampMType() ;
    
    /** return the Plastic message this message type matches against 
     * 
     * return null if not applicable to htis protocol.
     * */
    protected abstract URI getPlasticMessageType();
    
    
    /** create a sender that will send a message of this type to 
     * a plastic application 
     * 
     * @param plas
     * @return null if this message cannot be sent via plastic.
     */
    protected abstract S createPlasticSender(PlasticApplicationDescription plas);
    
    /** crate a sender that will send a message of this type to a
     * SAMP application.
     * 
     * @return null if this message cannot be sent via SAMP
     */
    protected abstract S createSampSender(Object somethingSamp);
    
    protected abstract MessageUnmarshaller<S> createPlasticUnmarshaller();
    
    protected abstract MessageUnmarshaller<S> createSampUnmarshaller();
    
    @Override
    public final String toString() {
        return "<Message Type: " + getClass().getName() + ">";
    }
    /** implementesd as class equality, not object equality */
    @Override
    public final boolean equals(final Object obj) {
        return  this.getClass().equals(obj.getClass());
    }
    
    /** implemented to return hashcode of class */
    @Override    
    public final int hashCode() {
        return this.getClass().hashCode();
    }
    
}
