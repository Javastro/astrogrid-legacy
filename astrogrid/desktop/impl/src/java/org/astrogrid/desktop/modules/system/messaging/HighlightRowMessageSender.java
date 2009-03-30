/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URL;

import org.astrogrid.samp.Response;

/** send a row-hilight message
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 15, 200911:25:10 AM
 */
public interface HighlightRowMessageSender extends MessageSender {
    

    
    
    /** hilight a row in a table.
     * Table is identified by either votableURL or tableID - other may be null.
     * @param votableURL url of the table
     * @param tableID  id of the table
     * @param index 0-origin row to highlight
     * 
     */   
    Response highlightRow(URL votableURL, String tableID, int index);

}
