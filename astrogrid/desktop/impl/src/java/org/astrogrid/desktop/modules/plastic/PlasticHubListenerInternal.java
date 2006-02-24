/*
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.io.IOException;
import java.util.List;

import org.astrogrid.acr.ACRException;

/**
 * @author jdt@roe.ac.uk
 * @date 22-Nov-2005
 */
public interface PlasticHubListenerInternal {
    /**
     * Ping registered applications, mark any that don't respond for potential purging.
     * 
     * @return the list ids of dead apps.
     */
    List markUnresponsiveApps();

    /**
     * Unregister any apps marked as unresponsive.
     * 
     * @return the list of ids of unregistered apps.
     */
    List purgeUnresponsiveApps();
    
    /**
     * Instruct the hub to open a web page listing details of all the registered apps.
     * This is meant to be a bit more human friendly than the other info methods.
     * @throws IOException 
     * @throws ACRException 
     *
     */
    void prettyPrintRegisteredApps() throws IOException, ACRException;
}
