/*
 * 
 */
package org.votech.plastic;

import java.net.URI;



/** Constants for messages sent by a PLASTIC Hub.
 * @author jdt@roe.ac.uk
 */
public interface HubMessageConstants {

    /**
     * Hub-related operations.
     * Note that these are still under discussion and liable to change.
     */
    String HUB_EVENT = "/hub/event";
    URI APPLICATION_REGISTERED_EVENT = URI.create(CommonMessageConstants.PREFIX+HUB_EVENT+"/ApplicationRegistered");
    URI APPLICATION_UNREGISTERED_EVENT = URI.create(CommonMessageConstants.PREFIX+HUB_EVENT+"/ApplicationUnregistered");
    URI HUB_STOPPING_EVENT = URI.create(CommonMessageConstants.PREFIX+HUB_EVENT+"/HubStopping");

}
