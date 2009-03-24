/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

/** send a point at sky message
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 15, 200911:26:40 AM
 */
public interface PointAtSkyMessageSender extends MessageSender {

    
    

    /** point at a particular position in the sky
     * 
     * @param ra
     * @param dec
     */
    void pointAtSky(float ra,float dec);
    
}
