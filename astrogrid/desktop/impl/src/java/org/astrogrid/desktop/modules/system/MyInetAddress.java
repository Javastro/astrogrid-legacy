/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Class with a single static accessor for the current Internet address.
 * factors out code common to RmiServer & WebServer
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 22, 20071:06:21 PM
 */
public final class MyInetAddress {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(MyInetAddress.class);

    private static InetAddress addr;
    
    public static synchronized final InetAddress myAddress() {
        if (addr == null) {
            try {
                addr = InetAddress.getLocalHost(); 
            } catch (final UnknownHostException x) {
                logger.warn("Failed to resolve local ip - falling back to loopback address");
                logger.debug("Cause was",x);
                try {
                    addr = InetAddress.getByName("127.0.0.1");
                } catch (final UnknownHostException x1) {
                    logger.fatal("Loopback address considered invalid",x1); 
                } 
            }             
        }
        return addr;
    }
}
