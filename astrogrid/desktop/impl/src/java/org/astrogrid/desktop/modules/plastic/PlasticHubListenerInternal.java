/*
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.util.List;

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
}
