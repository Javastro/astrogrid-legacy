/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.apache.commons.collections.Factory;
import org.astrogrid.acr.ui.MyspaceBrowser;
import org.votech.plastic.incoming.handlers.MessageHandler;

/** Interface to the file explorer factory. a conjunction of other interfaces./
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 1, 20071:46:30 AM
 */
public interface FileManagerFactory extends Factory, MessageHandler,
		MyspaceBrowser, FileManagerInternal {

    

}
