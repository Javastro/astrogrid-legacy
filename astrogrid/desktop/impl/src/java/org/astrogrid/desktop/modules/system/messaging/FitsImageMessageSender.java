/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URL;

/** Message to send a fits image.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 15, 200911:19:35 AM
 */
public interface FitsImageMessageSender extends MessageSender {
    void sendFitsImage(URL fitsURL, String imageId,String imageName);
}
