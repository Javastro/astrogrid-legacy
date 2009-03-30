/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URL;

import org.astrogrid.samp.Response;

/** Send a fits table mesage
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 15, 200911:23:49 AM
 */
public interface FitsTableMessageSender extends MessageSender {
    Response sendFitsTable(URL fitsURL, String tableID, String tableName);

    

}
