/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import org.astrogrid.samp.Response;

/** Interface to sending a bibcode message.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 15, 200911:02:29 AM
 */
public interface BibcodeMessageSender extends MessageSender {

    /**
     * send a bibcode message
     * @param bibcode the bibcode to send
     * required)
     */
    public Response sendBibcode(String bibcode);
}
