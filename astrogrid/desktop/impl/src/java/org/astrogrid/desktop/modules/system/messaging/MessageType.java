/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/** Abstract class for message types.
 * A message type represents a particular kind of message, and handles the differences in sending this
 * message over either PLASTIC or SAMP.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 15, 20092:58:32 PM
 */
public abstract class MessageType<S extends MessageSender> {

    
    /** return the SAMP MType this message type matches against.
     * 
     *  return null if not applicatblae for this protocol*/
    protected abstract String getSampMType() ;
    
    /** return the Plastic message this message type matches against. 
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
    
    /** create a sender that will send a message of this type to a
     * SAMP application.
     * 
     * @return null if this message cannot be sent via SAMP
     */
    protected abstract S createSampSender(SampMessageTarget somethingSamp);
    
    /**
     * Create an unmarshaller that can accept a message of this type received from a PLASTIC application
     * @return a message unmarshaller for this message type
     */
    protected abstract MessageUnmarshaller<S> createPlasticUnmarshaller();
    
    /** Create an unmarshaller that can accept a message of this type received from a SAMP application
     * 
     * @return a message unmarshaller for this message type
     */
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
    
// utilities for working with URL references.

    /** returns true only if the scheme is one of the commonly accessible ones */
    public static boolean isMessagingAccessibleURL(final URL u) {
        final String scheme = u.getProtocol();
        return "http".equals(scheme) || "file".equals(scheme) || "ftp".equals(scheme);
    }
    
    /** rewrite URL to make is messaging accessible
     * 
     * @param u original url
     * @param root root URL of the local webserver. (can be found at {@code SampMessageTarget.getSampImpl().getWebserverRoot()}
     * or {@code PlasticApplicationDescription.getTupperware().getWebserverRoot()}
     * @return u, unless {@link #isMessagingAccessibleURL(URL)} is true, in which 
     * case will rewrite to point to file through file-access servlet
     * @throws MalformedURLException 
     */
    public static URL maybeRewriteForAccessability(final URL u, final URL root ) throws MalformedURLException {
        if (isMessagingAccessibleURL(u)) {
            return u;
        } else {            
            return new URL(root + "file-access?id=" + u);
        }
    }
    
    
}
