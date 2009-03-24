/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URL;
import java.util.List;

/** send a sleect-rowlist message
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 15, 200911:25:52 AM
 */
public interface SelectRowListMessageSender extends MessageSender {




    /** Select a subset of rows in table
     * Table is identified by either votableURL or tableID - other may be null.
     * @param votableURL url of the table
     * @param tableID  id of the table
     * @param indexes array of 0-origin rows to highlight
     * 
     */    
    void selectRowlist(URL votableURL, String tableID, List<Integer> indexes);


}
