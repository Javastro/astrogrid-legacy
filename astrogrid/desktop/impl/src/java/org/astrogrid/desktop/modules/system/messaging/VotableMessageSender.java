/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URL;

import org.astrogrid.samp.Response;

/** Send a Votable message
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 15, 200911:22:56 AM
 */
public interface VotableMessageSender extends MessageSender {
    Response sendVotable(URL votableURL, String tableID, String tableName);
}
