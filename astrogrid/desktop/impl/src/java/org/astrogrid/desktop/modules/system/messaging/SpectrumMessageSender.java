/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URL;
import java.util.Map;

import org.astrogrid.samp.Response;

/** Send a spectrum message
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 15, 200911:22:13 AM
 */
public interface SpectrumMessageSender extends MessageSender {
    Response sendSpectrum(URL spectrumURL, Map metadata, String specID, String specName);
    
}
